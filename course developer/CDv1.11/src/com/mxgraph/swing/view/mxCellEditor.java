/**
 * $Id: mxCellEditor.java,v 1.1 2011/06/23 06:41:35 administrator Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package com.mxgraph.swing.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import javax.swing.JComboBox;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * To control this editor, use mxGraph.invokesStopCellEditing, mxGraph.
 * enterStopsCellEditing and mxGraph.escapeEnabled.
 */
public class mxCellEditor implements mxICellEditor
{

	/**
	 * 
	 */
	public static int DEFAULT_MIN_WIDTH = 100;

	/**
	 * 
	 */
	public static int DEFAULT_MIN_HEIGHT = 60;

	/**
	 * 
	 */
	public static double DEFAULT_MINIMUM_EDITOR_SCALE = 1;
	

	/**
	 * 
	 */
	protected mxGraphComponent graphComponent;

	/**
	 * Defines the minimum scale to be used for the editor. Set this to
	 * 0 if the font size in the editor 
	 */
	protected double minimumEditorScale = DEFAULT_MINIMUM_EDITOR_SCALE;

	/**
	 * 
	 */
	protected int minimumWidth = DEFAULT_MIN_WIDTH;

	/**
	 * 
	 */
	protected int minimumHeight = DEFAULT_MIN_HEIGHT;

	/**
	 * 
	 */
	protected transient Object editingCell;

	/**
	 * 
	 */
	protected transient EventObject trigger;

	/**
	 * 
	 */
	protected transient JScrollPane scrollPane;

	/**
	 * Holds the editor for plain text editing.
	 */
	protected transient JTextArea textArea;

	/**
	 * Holds the editor for HTML editing.
	 */
	protected transient JEditorPane editorPane;

	/**
	 * Holds the editor for comboBox editing.
	 */
	protected transient JComboBox comboBox;
	/**
	 * Holds the labels for comboBox editing.
	 */
	protected  String labels[]={"包含","构成组成","是一种","具有属性","具有特征","定义","并列","是前提","等价于","是工具","支持","相关"};
	/**
	 * 
	 */
	String root  = System.getProperty("user.dir");
	/**
	 * Adds required resources for i18n
	 */
	static
	{
		mxResources.add("com/mxgraph/examples/swing/resources/relation_zh-CN");
		//mxResources.add("com/mxgraph/examples/swing/resources/editorTestuft8");
	}
	protected  ArrayList<String> arrayList=new ArrayList<String>();
	protected transient KeyAdapter keyListener = new KeyAdapter()
	{
		/**
		 * 
		 */
		protected transient boolean ignoreEnter = false;

		
		/**
		 * 
		 */
		public void keyPressed(KeyEvent e)
		{
			if (graphComponent.isEnterStopsCellEditing()
					&& e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if (e.isShiftDown() || e.isControlDown() || e.isAltDown())
				{ 
					if (!ignoreEnter)
					{
						ignoreEnter = true;

						// Redirects the event with no modifier keys
						try
						{
							KeyEvent event = new KeyEvent((Component) e
									.getSource(), e.getID(), e.getWhen(), 0, e
									.getKeyCode(), e.getKeyChar());
							((Component) e.getSource()).dispatchEvent(event);
						}
						finally
						{
							ignoreEnter = false;
						}
					}
				}
				else if (!ignoreEnter)
				{
					stopEditing(false);
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				stopEditing(true);
			}
		}
	};

	/**
	 * 
	 */
	public mxCellEditor(mxGraphComponent graphComponent)
	{
//		root="/"+root;
		if(!root.contains("CDv1.11")){
			root=root+"/CDv1.11";
		}
		this.graphComponent = graphComponent;

		// Creates the plain text editor
		textArea = new JTextArea();
		textArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		textArea.setOpaque(false);

		// Creates the HTML editor
		editorPane = new JEditorPane();
		editorPane.setOpaque(false);
		editorPane.setContentType("text/html");
		
		//Creates the comboBox editor
		comboBox=new JComboBox();//set the items null
		comboBox.setBorder(BorderFactory.createEmptyBorder());
		comboBox.setOpaque(false);
		comboBox.setVisible(false);
		
		installKeyHandler();

		// Creates the scollpane that contains the editor
		// FIXME: Cursor not visible when scrolling
		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setVisible(false);
		scrollPane.setOpaque(false);
		
		
	}

	/**
	 * Installs the keyListener in the textArea and editorPane
	 * for handling the enter keystroke and updating the modified state.
	 */
	protected void installKeyHandler()
	{
		textArea.addKeyListener(keyListener);
		editorPane.addKeyListener(keyListener);
		comboBox.addKeyListener(keyListener);
	}

	/**
	 * Returns the current editor or null if no editing is in progress.
	 */
	public Component getEditor()
	{
		if (textArea.getParent() != null)
		{
			return textArea;
		}
		else if (editingCell != null)
		{
			return editorPane;
		}

		return null;
	}

	/**
	 * Returns true if the label bounds of the state should be used for the
	 * editor.
	 */
	protected boolean useLabelBounds(mxCellState state)
	{
		mxIGraphModel model = state.getView().getGraph().getModel();
		mxGeometry geometry = model.getGeometry(state.getCell());

		return ((geometry != null && geometry.getOffset() != null
				&& !geometry.isRelative() && (geometry.getOffset().getX() != 0 || geometry
				.getOffset().getY() != 0)) || model.isEdge(state.getCell()));
	}

	/**
	 * Returns the bounds to be used for the editor.
	 */
	public Rectangle getEditorBounds(mxCellState state, double scale)
	{
		mxIGraphModel model = state.getView().getGraph().getModel();
		Rectangle bounds = null;

		if (useLabelBounds(state))
		{
			bounds = state.getLabelBounds().getRectangle();
			bounds.height += 10;
		}
		else
		{
			bounds = state.getRectangle();
		}

		// Applies the horizontal and vertical label positions
		if (model.isVertex(state.getCell()))
		{
			String horizontal = mxUtils.getString(state.getStyle(),
					mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);

			if (horizontal.equals(mxConstants.ALIGN_LEFT))
			{
				bounds.x -= state.getWidth();
			}
			else if (horizontal.equals(mxConstants.ALIGN_RIGHT))
			{
				bounds.x += state.getWidth();
			}

			String vertical = mxUtils.getString(state.getStyle(),
					mxConstants.STYLE_VERTICAL_LABEL_POSITION,
					mxConstants.ALIGN_MIDDLE);

			if (vertical.equals(mxConstants.ALIGN_TOP))
			{
				bounds.y -= state.getHeight();
			}
			else if (vertical.equals(mxConstants.ALIGN_BOTTOM))
			{
				bounds.y += state.getHeight();
			}
		}

		bounds.setSize((int) Math.max(bounds.getWidth(), Math
				.round(minimumWidth * scale)), (int) Math.max(bounds
				.getHeight(), Math.round(minimumHeight * scale)));

		return bounds;
	}
	/**
	 * Returns the bounds to be used for the comboBox.
	 */
	public Rectangle getComboBoxBounds(mxCellState state, double scale){
		mxIGraphModel model = state.getView().getGraph().getModel();
		Rectangle bounds = null;

		if (useLabelBounds(state))
		{
			bounds = state.getLabelBounds().getRectangle();
			//bounds.height += 10;
			System.out.println("useLabelBounds");
		}
		else
		{
			bounds = state.getRectangle();
			bounds.height-=20;
			System.out.println("not use label bounds");
		}

		// Applies the horizontal and vertical label positions
		if (model.isVertex(state.getCell()))
		{
			String horizontal = mxUtils.getString(state.getStyle(),
					mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);

			
			if (horizontal.equals(mxConstants.ALIGN_LEFT))
			{
				bounds.x -= state.getWidth();
			}
			else if (horizontal.equals(mxConstants.ALIGN_RIGHT))
			{
				bounds.x += state.getWidth();
			}

			String vertical = mxUtils.getString(state.getStyle(),
					mxConstants.STYLE_VERTICAL_LABEL_POSITION,
					mxConstants.ALIGN_MIDDLE);

			if (vertical.equals(mxConstants.ALIGN_TOP))
			{
				bounds.y -= state.getHeight();
			}
			else if (vertical.equals(mxConstants.ALIGN_BOTTOM))
			{
				bounds.y += state.getHeight();
			}
		}
		else{
			bounds.x -= (state.getWidth()/8);//设置Combobox为居中
		}

//		bounds.setSize((int) Math.max(bounds.getWidth(), Math
//				.round(minimumWidth * scale)), (int) Math.max(bounds
//				.getHeight(), Math.round(minimumHeight * scale)));
		bounds.setSize((int) Math.max(bounds.getWidth(), Math
				.round(minimumWidth * scale)), (int) Math.max(bounds
				.getHeight(), Math.round(minimumHeight * scale))/2);

		return bounds;
	}

	/*
	 * (non-Javadoc)
	 * @see com.mxgraph.swing.view.mxICellEditor#startEditing(java.lang.Object, java.util.EventObject)
	 */
	public void startEditing(Object cell, EventObject trigger)
	{
		if (editingCell != null)
		{
			stopEditing(true);
		}

		mxCellState state = graphComponent.getGraph().getView().getState(cell);
        double scale = Math.max(minimumEditorScale, graphComponent
					.getGraph().getView().getScale());
		if (state != null)
		{
			mxGraph graph=graphComponent.getGraph();
			Object source;
			Object target;
			String sourcestyle;
			String targetstyle;
			if(((mxCell)cell).isEdge()){
				source=((mxCell)cell).getSource();
				target=((mxCell)cell).getTarget();
				if(source!=null&&target!=null){
					sourcestyle=mxUtils.getString(graph.getCellStyle((mxCell)source), mxConstants.STYLE_SHAPE);
					targetstyle=mxUtils.getString(graph.getCellStyle((mxCell)target), mxConstants.STYLE_SHAPE);
					if(sourcestyle.equals("ellipse")){
						if(targetstyle.equals("ellipse")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("elel")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("label")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration<?> en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("ella")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("roundrect")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("elro")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("cylinder")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("elcy")){
					                String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("cloud")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("elcl")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("rhombus")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("elrh")){
					                	 String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					               
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else{
							comboBox.removeAllItems();
						}
					}else 
					if(sourcestyle.equals("roundrect")){
						if(targetstyle.equals("ellipse")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("roel")){
					                	 String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					               
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("roundrect")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("roro")){
					                	 String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					               
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("label")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("rola")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("rhombus")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("rorh")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
							if(targetstyle.equals("cylinder")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rocy")){
						                String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else {
							comboBox.removeAllItems();
						}
					}else 
					if(sourcestyle.equals("label")){
						if(targetstyle.equals("ellipse")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("lael")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("label")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("lala")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("rhombus")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("larh")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("roundrect")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("laro")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("cylinder")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("lacy")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("cloud")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("lacl")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else {
							comboBox.removeAllItems();
						}
					}else
						if(sourcestyle.equals("rhombus")){
							if(targetstyle.equals("ellipse")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhel")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else 
							if(targetstyle.equals("rhombus")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhrh")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else if(targetstyle.equals("label")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhla")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else if(targetstyle.equals("roundrect")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhro")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else if(targetstyle.equals("cylinder")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhcy")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else if(targetstyle.equals("cloud")){
								comboBox.removeAllItems();
								Properties props = new Properties();
						        try{
						            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
						            props.load(in);
						            Enumeration en = props.propertyNames();
						            while(en.hasMoreElements()){
						                String key = (String)en.nextElement();
						                if(key.contains("rhcl")){
						                	String property = props.getProperty(key);
						                comboBox.addItem(property);
						                }
						                
						            }
						          in.close();
						        }catch(Exception e){
						            e.printStackTrace();
						        }
							}else {
								comboBox.removeAllItems();
							}
						}else 
					if(sourcestyle.equals("cylinder")){
						if(targetstyle.equals("ellipse")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cyel")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("roundrect")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cyro")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("cylinder")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cycy")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("label")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cyla")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("rhombus")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cyrh")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else if(targetstyle.equals("cloud")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("cycl")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else {
							comboBox.removeAllItems();
						}
					}else 
					if(sourcestyle.equals("cloud")){
						if(targetstyle.equals("rhombus")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clrh")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else 
						if(targetstyle.equals("ellipse")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clel")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("label")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clla")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("roundrect")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clro")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("cylinder")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clcy")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else
						if(targetstyle.equals("cloud")){
							comboBox.removeAllItems();
							Properties props = new Properties();
					        try{
					            InputStream in = new BufferedInputStream(new FileInputStream(root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties"));
					            props.load(in);
					            Enumeration en = props.propertyNames();
					            while(en.hasMoreElements()){
					                String key = (String)en.nextElement();
					                if(key.contains("clcl")){
					                	String property = props.getProperty(key);
					                comboBox.addItem(property);
					                }
					                
					            }
					          in.close();
					        }catch(Exception e){
					            e.printStackTrace();
					        }
						}else {
							comboBox.removeAllItems();
						}
					}
				}else {
					comboBox.removeAllItems();
				}
			   	this.trigger=trigger;
			   	editingCell=cell;
			   	
			   	comboBox.setBounds(getComboBoxBounds(state, scale));
			   	comboBox.setEditable(false);
			   	comboBox.setVisible(true);
			   	
			   	String value = getInitialValue(state,trigger);
			   	
			   	comboBox.setSelectedItem(value);
			   	//if(value.equals(arg0)){}
			   	graphComponent.getGraphControl().add(comboBox, 0);
			   	
			   	comboBox.revalidate();
				comboBox.requestFocusInWindow();
			}
//		  if(((mxCell)cell).isEdge()){
//			   	this.trigger=trigger;
//			   	editingCell=cell;
//			   	
//			   	comboBox.setBounds(getComboBoxBounds(state, scale));
//			   	comboBox.setEditable(false);
//			   	comboBox.setVisible(true);
//			   	
//			   	String value = getInitialValue(state,trigger);
//			   	
//			   	comboBox.setSelectedItem(value);
//			   	//if(value.equals(arg0)){}
//			   	graphComponent.getGraphControl().add(comboBox, 0);
//			   	
//			   	comboBox.revalidate();
//				comboBox.requestFocusInWindow();
//			   	
//			   	
//		  }
		  else{
			
			JTextComponent currentEditor = null;
			this.trigger = trigger;
			editingCell = cell;

			scrollPane.setBounds(getEditorBounds(state, scale));
			scrollPane.setVisible(true);

			String value = getInitialValue(state, trigger);

			// Configures the style of the in-place editor
			if (graphComponent.getGraph().isHtmlLabel(cell))
			{
				editorPane.setDocument(mxUtils.createHtmlDocumentObject(state
						.getStyle(), scale));
				editorPane.setText(mxUtils.getBodyMarkup(value, true));

				// Workaround for wordwrapping in editor pane
				// FIXME: Cursor not visible at end of line
				JPanel wrapper = new JPanel(new BorderLayout());
				wrapper.setOpaque(false);
				wrapper.add(editorPane, BorderLayout.CENTER);
				scrollPane.setViewportView(wrapper);

				currentEditor = editorPane;
			}
			else
			{
				textArea.setFont(mxUtils.getFont(state.getStyle(), scale));
				Color fontColor = mxUtils.getColor(state.getStyle(),
						mxConstants.STYLE_FONTCOLOR, Color.black);
				textArea.setForeground(fontColor);
				textArea.setText(value);

				scrollPane.setViewportView(textArea);
				currentEditor = textArea;
			}

			graphComponent.getGraphControl().add(scrollPane, 0);

			if (isHideLabel(state))
			{
				graphComponent.redraw(state);
			}


			currentEditor.revalidate();
			currentEditor.requestFocusInWindow();
			currentEditor.selectAll();
		  }
		}
	}

	/**
	 * 
	 */
	protected boolean isHideLabel(mxCellState state)
	{
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.mxgraph.swing.view.mxICellEditor#stopEditing(boolean)
	 */
	public void stopEditing(boolean cancel)
	{
		if (editingCell != null)
		{
			if(((mxCell)editingCell).isEdge()){
				comboBox.transferFocusUpCycle();
				Object cell = editingCell;
				editingCell = null;
				
				String string=(String)(comboBox.getSelectedItem());
				if (!cancel)
				{
					EventObject trig = trigger;
					trigger = null;
					graphComponent.labelChanged(cell, string, trig);
				}
				else
				{
					mxCellState state = graphComponent.getGraph().getView()
							.getState(cell);
					graphComponent.redraw(state);
				}
				if (scrollPane != null)
				{
					comboBox.setVisible(false);
					comboBox.getParent().remove(comboBox);
				}

				graphComponent.requestFocusInWindow();
				
			}
			else
			{
			scrollPane.transferFocusUpCycle();
			Object cell = editingCell;
			editingCell = null;

			String string=textArea.getText();
			if (!cancel)
			{
				EventObject trig = trigger;
				trigger = null;
				graphComponent.labelChanged(cell, string, trig);
			}
			else
			{
				mxCellState state = graphComponent.getGraph().getView()
						.getState(cell);
				graphComponent.redraw(state);
			}

			if (scrollPane.getParent() != null)
			{
				scrollPane.setVisible(false);
				scrollPane.getParent().remove(scrollPane);
			}

			graphComponent.requestFocusInWindow();
		}
	}
	}

	/**
	 * Gets the initial editing value for the given cell.
	 */
	protected String getInitialValue(mxCellState state, EventObject trigger)
	{
		return graphComponent.getEditingValue(state.getCell(), trigger);
	}

	/**
	 * Returns the current editing value.
	 */
	public String getCurrentValue()
	{
		if(comboBox!=null){
			System.out.println("the coumboBOx label returned");
			String string=(String)(comboBox.getSelectedItem());
			return string;
		}
		System.out.println("the textarea label returned");
		return textArea.getText();

	}

	/*
	 * (non-Javadoc)
	 * @see com.mxgraph.swing.view.mxICellEditor#getEditingCell()
	 */
	public Object getEditingCell()
	{
		return editingCell;
	}

	/**
	 * @return the minimumEditorScale
	 */
	public double getMinimumEditorScale()
	{
		return minimumEditorScale;
	}

	/**
	 * @param minimumEditorScale the minimumEditorScale to set
	 */
	public void setMinimumEditorScale(double minimumEditorScale)
	{
		this.minimumEditorScale = minimumEditorScale;
	}

	/**
	 * @return the minimumWidth
	 */
	public int getMinimumWidth()
	{
		return minimumWidth;
	}

	/**
	 * @param minimumWidth the minimumWidth to set
	 */
	public void setMinimumWidth(int minimumWidth)
	{
		this.minimumWidth = minimumWidth;
	}

	/**
	 * @return the minimumHeight
	 */
	public int getMinimumHeight()
	{
		return minimumHeight;
	}

	/**
	 * @param minimumHeight the minimumHeight to set
	 */
	public void setMinimumHeight(int minimumHeight)
	{
		this.minimumHeight = minimumHeight;
	}

}
