package com.mxgraph.examples.swing.editor;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.mxgraph.examples.swing.GraphEditor;
//import com.mxgraph.examples.swing.GraphEditor.CustomGraph;
import com.mxgraph.examples.swing.CDSoftWare.CustomGraph;
import com.mxgraph.examples.swing.editor.EditorActions.SaveAction;
import com.mxgraph.examples.swing.CDSoftWare;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

public class BasicGraphEditor extends JInternalFrame implements InternalFrameListener
//public class BasicGraphEditor extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6561623072112577140L;

	/**
	 * Adds required resources for i18n
	 */
	static
	{
		mxResources.add("com/mxgraph/examples/swing/resources/editor_zh-CN");
		mxResources.add("com/mxgraph/examples/swing/resources/shape_zh-CN");
	}

	/**
	 * 
	 */
	protected mxGraphComponent graphComponent;

	/**
	 * 
	 */
	protected mxGraphOutline graphOutline;

	/**
	 * 
	 */
	protected JTabbedPane libraryPane;

	/**
	 * 
	 */
	protected mxUndoManager undoManager;

	/**
	 * 
	 */
	protected String appTitle;

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}
	/**
	 * 
	 */
	protected JLabel statusBar;

	/**
	 * 
	 */
	protected File currentFile;

	/**
	 * 
	 */
	protected boolean modified = false;

	/**
	 * 
	 */
	protected mxRubberband rubberband;

	/**
	 * 
	 */
	protected mxKeyboardHandler keyboardHandler;

	/**
	 * 
	 */
	protected mxIEventListener undoHandler = new mxIEventListener()
	{
		public void invoke(Object source, mxEventObject evt)
		{
			undoManager.undoableEditHappened((mxUndoableEdit) evt
					.getProperty("edit"));
		}
	};

	/**
	 * 
	 */
	protected mxIEventListener changeTracker = new mxIEventListener()
	{
		public void invoke(Object source, mxEventObject evt)
		{
			setModified(true);
		}
	};


	 static int openFrameCount = 0;
	 static final int xOffset = 30, yOffset = 30;
	/**
	 * 
	 */
	public BasicGraphEditor(String appTitle, mxGraphComponent component)
	{
		super(appTitle, 
	              true, //resizable
	              true, //closable
	              true, //maximizable
	              true);//iconifiable
		// Stores and updates the frame title
		this.appTitle = appTitle;

		// Stores a reference to the graph and creates the command history
		graphComponent = component;
		final mxGraph graph = graphComponent.getGraph();
		undoManager = new mxUndoManager();

		// Updates the modified flag if the graph model changes
		graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

		// Adds the command history to the model and view
		graph.getModel().addListener(mxEvent.UNDO, undoHandler);
		graph.getView().addListener(mxEvent.UNDO, undoHandler);

		// Keeps the selection in sync with the command history
		mxIEventListener undoHandler = new mxIEventListener()
		{
			public void invoke(Object source, mxEventObject evt)
			{
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt
						.getProperty("edit")).getChanges();
				graph.setSelectionCells(graph
						.getSelectionCellsForChanges(changes));
			}
		};

		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);

		// Creates the graph outline component
		graphOutline = new mxGraphOutline(graphComponent);

		// Creates the library pane that contains the tabs with the palettes
		libraryPane = new JTabbedPane();

		// Creates the inner split pane that contains the library with the
		// palettes and the graph outline on the left side of the window
		JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				libraryPane, graphOutline);
		inner.setDividerLocation(320);
		inner.setResizeWeight(1);
		inner.setDividerSize(6);
		inner.setBorder(null);

		// Creates the outer split pane that contains the inner split pane and
		// the graph component on the right side of the window
		JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inner,
				graphComponent);
		outer.setOneTouchExpandable(true);
		outer.setDividerLocation(200);
		outer.setDividerSize(6);
		outer.setBorder(null);

		// Creates the status bar
		statusBar = createStatusBar();

		// Display some useful information about repaint events
		installRepaintListener();

		// Puts everything together
		setLayout(new BorderLayout());
		add(outer, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		installToolBar();



		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				 int result = JOptionPane.showConfirmDialog(null, "确认关闭吗", "关闭确认框", JOptionPane.YES_NO_OPTION);
				    System.out.println("#chose : " + result);
				    if(result==1){
				     System.out.println("#阻止关闭..");
				     setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				    }else{
				     System.out.println("#确认关闭");
				     setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				    }
			}});

		// Installs rubberband selection and handling for some special
		// keystrokes such as F2, Control-C, -V, X, A etc.
		installHandlers();
		installListeners();
		updateTitle();
		
		// Creates the shapes palette
		EditorPalette shapesPalette = insertPalette(mxResources.get("shapes"));


		// 如果一个edge被点击，那就在之后的节点连接中用此edge形状
		shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{
				Object tmp = evt.getProperty("transferable");

				if (tmp instanceof mxGraphTransferable)
				{
					mxGraphTransferable t = (mxGraphTransferable) tmp;
					Object cell = t.getCells()[0];

					if (graph.getModel().isEdge(cell))
					{
						((CustomGraph) graph).setEdgeTemplate(cell);
					}
				}
			}

		});

		// 增加一些 模板 cells for dropping into the graph

		shapesPalette
				.addTemplate(
						mxResources.get("label"),
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/rectangle.png")),
						null, 80, 60, "");
		shapesPalette
		.addTemplate(
				mxResources.get("roundrect"),
				new ImageIcon(
						CDSoftWare.class
								.getResource("/com/mxgraph/examples/swing/images/rounded.png")),
				"rounded=1;shape=roundrect", 80, 60, "");
//		shapesPalette
//		.addTemplate(
//				"Double Ellipse",
//				new ImageIcon(
//						GraphEditor.class
//								.getResource("/com/mxgraph/examples/swing/images/doubleellipse.png")),
//				"ellipse;shape=doubleEllipse", 160, 120, "");
		shapesPalette
				.addTemplate(
						mxResources.get("ellipse"),
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/ellipse.png")),
						"ellipse", 80, 80, "");
		shapesPalette
				.addTemplate(
						mxResources.get("rhombus"),
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/rhombus.png")),
						"rhombus", 80, 80, "");
		shapesPalette
				.addTemplate(
						mxResources.get("cylinder"),
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/cylinder.png")),
						"shape=cylinder", 60, 80, "");

		shapesPalette.addTemplate(mxResources.get("cloud"), new ImageIcon(CDSoftWare.class
				.getResource("/com/mxgraph/examples/swing/images/cloud.png")),
				"ellipse;shape=cloud", 80, 60, "");

		shapesPalette
				.addEdgeTemplate(
						"直线",
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/straight.png")),
						"straight", 60, 60, "");
		shapesPalette
				.addEdgeTemplate(
						"水平连线",
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/connect.png")),
						null, 50, 50, "");
		shapesPalette
				.addEdgeTemplate(
						"垂直连线",
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/vertical.png")),
						"vertical", 50, 50, "");
		shapesPalette
				.addEdgeTemplate(
						"实体关系线",
						new ImageIcon(
								CDSoftWare.class
										.getResource("/com/mxgraph/examples/swing/images/entity.png")),
						"entity", 50, 50, "");
	
		//set the window's size
		
		 setSize(600,600);

//		 setde
//		 setDefaultLocale(true);
//		 setLookAndFeel(clazz);
	        //Set the window's location.
	     setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
	}

	/**
	 * 
	 */
	protected void installHandlers()
	{
		rubberband = new mxRubberband(graphComponent);
		keyboardHandler = new EditorKeyboardHandler(graphComponent);
	}

	/**
	 * 
	 */
	protected void installToolBar()
	{
		//add(new EditorToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);
	}

	/**
	 * 
	 */
	protected JLabel createStatusBar()
	{
		JLabel statusBar = new JLabel(mxResources.get("ready"));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

		return statusBar;
	}

	/**
	 * 
	 */
	protected void installRepaintListener()
	{
		graphComponent.getGraph().addListener(mxEvent.REPAINT,
				new mxIEventListener()
				{
					public void invoke(Object source, mxEventObject evt)
					{
						String buffer = (graphComponent.getTripleBuffer() != null) ? ""
								: " (unbuffered)";
						mxRectangle dirty = (mxRectangle) evt
								.getProperty("region");

						if (dirty == null)
						{
							status("Repaint all" + buffer);
						}
						else
						{
							status("Repaint: x=" + (int) (dirty.getX()) + " y="
									+ (int) (dirty.getY()) + " w="
									+ (int) (dirty.getWidth()) + " h="
									+ (int) (dirty.getHeight()) + buffer);
						}
					}
				});
	}

	/**
	 * 
	 */
	public EditorPalette insertPalette(String title)
	{
		final EditorPalette palette = new EditorPalette();
		final JScrollPane scrollPane = new JScrollPane(palette);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryPane.add(title, scrollPane);

		// Updates the widths of the palettes if the container size changes
		libraryPane.addComponentListener(new ComponentAdapter()
		{
			/**
			 * 
			 */
			public void componentResized(ComponentEvent e)
			{
				int w = scrollPane.getWidth()
						- scrollPane.getVerticalScrollBar().getWidth();
				palette.setPreferredWidth(w);
			}

		});

		return palette;
	}

	/**
	 * 
	 */
	protected void mouseWheelMoved(MouseWheelEvent e)
	{
		if (e.getWheelRotation() < 0)
		{
			graphComponent.zoomIn();
		}
		else
		{
			graphComponent.zoomOut();
		}

		status(mxResources.get("scale") + ": "
				+ (int) (100 * graphComponent.getGraph().getView().getScale())
				+ "%");
	}

	/**
	 * 
	 */
	protected void showOutlinePopupMenu(MouseEvent e)
	{
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(mxResources
				.get("magnifyPage"));
		item.setSelected(graphOutline.isFitPage());

		item.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				graphOutline.setFitPage(!graphOutline.isFitPage());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(mxResources
				.get("showLabels"));
		item2.setSelected(graphOutline.isDrawLabels());

		item2.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(mxResources
				.get("buffering"));
		item3.setSelected(graphOutline.isTripleBuffered());

		item3.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				graphOutline
						.setTripleBuffered(!graphOutline.isTripleBuffered());
				graphOutline.repaint();
			}
		});

		JPopupMenu menu = new JPopupMenu();
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}

	/**
	 * 为cell（节点或边）添加右键响应事件
	 */
	protected void showGraphPopupMenu(MouseEvent e)
	{   
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		EditorPopupMenu menu = new EditorPopupMenu(BasicGraphEditor.this);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}

	/**
	 * 
	 */
	protected void mouseLocationChanged(MouseEvent e)
	{
		status(e.getX() + ", " + e.getY());
	}

	/**
	 * 
	 */
	protected void installListeners()
	{
		// Installs mouse wheel listener for zooming
		//为outline安装鼠标轴滚动事件
		MouseWheelListener wheelTracker = new MouseWheelListener()
		{
			/**
			 * 
			 */
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if (e.getSource() instanceof mxGraphOutline
						|| e.isControlDown())
				{
					BasicGraphEditor.this.mouseWheelMoved(e);
				}
			}

		};

		// Handles mouse wheel events in the outline and graph component
		graphOutline.addMouseWheelListener(wheelTracker);
		graphComponent.addMouseWheelListener(wheelTracker);

		// Installs the popup menu in the outline
		/**
		 * 为左下方的outline安装右键弹出菜单
		 */
		graphOutline.addMouseListener(new MouseAdapter()
		{

			/**
			 * 
			 */
			public void mousePressed(MouseEvent e)
			{
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showOutlinePopupMenu(e);
				}
			}

		});

		// Installs the popup menu in the graph component
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{

			/**
			 * 
			 */
			public void mousePressed(MouseEvent e)
			{
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showGraphPopupMenu(e);
				}
			}

		});

		// Installs a mouse motion listener to display the mouse location
		graphComponent.getGraphControl().addMouseMotionListener(
				new MouseMotionListener()
				{

					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
					 */
					public void mouseDragged(MouseEvent e)
					{
						mouseLocationChanged(e);
					}

					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
					 */
					public void mouseMoved(MouseEvent e)
					{
						mouseDragged(e);
					}

				});
	}

	/**
	 * 
	 */
	public void setCurrentFile(File file)
	{
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);

		if (oldValue != file)
		{
			updateTitle();
		}
	}

	/**
	 * 
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}

	/**
	 * 
	 * @param modified
	 */
	public void setModified(boolean modified)
	{
		boolean oldValue = this.modified;
		this.modified = modified;

		firePropertyChange("modified", oldValue, modified);

		if (oldValue != modified)
		{
			updateTitle();
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isModified()
	{
		return modified;
	}

	/**
	 * 
	 */
	public mxGraphComponent getGraphComponent()
	{
		return graphComponent;
	}

	/**
	 * 
	 */
	public mxGraphOutline getGraphOutline()
	{
		return graphOutline;
	}

	/**
	 * 
	 */
	public mxUndoManager getUndoManager()
	{
		return undoManager;
	}

	/**
	 * 
	 * @param name
	 * @param action
	 * @return
	 */
	public Action bind(String name, final Action action)
	{
		return bind(name, action, null);
	}

	/**
	 * 
	 * @param name
	 * @param action
	 * @return
	 */
	@SuppressWarnings("serial")
	public Action bind(String name, final Action action, String iconUrl)
	{
		return new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
				BasicGraphEditor.class.getResource(iconUrl)) : null)
		{
			public void actionPerformed(ActionEvent e)
			{
				action.actionPerformed(new ActionEvent(getGraphComponent(), e
						.getID(), e.getActionCommand()));
			}
		};
	}

	/**
	 * 
	 * @param msg
	 */
	public void status(String msg)
	{
		statusBar.setText(msg);
	}

	/**
	 * 
	 */
	public void updateTitle()
	{
//		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
//                
//                if(frame instanceof CDSoftWare){
//                  BasicGraphEditor beditor=((CDSoftWare)frame).getBasicGraphEditor();
//                  String title = (currentFile != null) ? currentFile
//					.getAbsolutePath() : mxResources.get("newDiagram");
//
//			if (modified)
//			{
//				title += "*";
//			}
//
//			beditor.setTitle(title + " - " + appTitle);
//                }
        
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
                
                if(frame instanceof CDSoftWare){
                  BasicGraphEditor beditor=((CDSoftWare)frame).getBasicGraphEditor();
                  if(currentFile != null){
                	  String string=currentFile.getAbsolutePath();
                	  String[] ss=string.split("\\\\");
                	  title=ss[ss.length-1];
                  }else{
                	  title=mxResources.get("newDiagram");
                  }

			if (modified)
			{
				title += "*";
			}

			beditor.setTitle(title + " - " + appTitle);
                }

//		if (frame != null)
//		{
//			String title = (currentFile != null) ? currentFile
//					.getAbsolutePath() : mxResources.get("newDiagram");

//			if (modified)
//			{
//				title += "*";
//			}

//			frame.setTitle(title + " - " + appTitle);
//		}
	}

	/**
	 * 
	 */
	public void about()
	{
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null)
		{
			EditorAboutFrame about = new EditorAboutFrame(frame);
			about.setModal(true);

			// Centers inside the application frame
			int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
			int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
			about.setLocation(x, y);

			// Shows the modal dialog and waits
			about.setVisible(true);
		}
	}

	/**
	 * 
	 */
	public void exit()
	{
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null)
		{
			frame.dispose();
		}
	}

	/**
	 * 
	 */
	public void setLookAndFeel(String clazz)
	{
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null)
		{
			try
			{
				UIManager.setLookAndFeel(clazz);
				SwingUtilities.updateComponentTreeUI(frame);

				// Needs to assign the key bindings again
				keyboardHandler = new EditorKeyboardHandler(graphComponent);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public JFrame createFrame(JMenuBar menuBar)
	{
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setSize(870, 640);

		// Updates the frame title
		updateTitle();

		return frame;
	}

	/**
	 * Creates and executes the specified layout.
	 * 
	 * @param key Key to be used for getting the label from mxResources and also
	 * to create the layout instance for the commercial graph editor example.
	 * @return
	 */
	@SuppressWarnings("serial")
	public Action graphLayout(final String key)
	{
		final mxIGraphLayout layout = createLayout(key); 

		if (layout != null)
		{
			return new AbstractAction(mxResources.get(key))
			{
				public void actionPerformed(ActionEvent e)
				{
					if (layout != null)
					{
						Object cell = mxGraphActions.getGraph(e)
								.getSelectionCell();

						if (cell == null
								|| mxGraphActions.getGraph(e).getModel()
										.getChildCount(cell) == 0)
						{
							cell = mxGraphActions.getGraph(e).getDefaultParent();
						}

						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0)
								+ " ms");
					}
				}

			};
		}
		else
		{
			return new AbstractAction(mxResources.get(key))
			{

				public void actionPerformed(ActionEvent e)
				{
					JOptionPane.showMessageDialog(mxGraphActions.getEditor(e), mxResources
							.get("noLayout"));
				}

			};
		}
	}

	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident)
	{
		mxIGraphLayout layout = null;

		if (ident != null)
		{
			//graphComponent=mxGraphActions.getEditor(e);
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph);
			}
			else if (ident.equals("horizontalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			}
			else if (ident.equals("verticalTree"))
			{
				layout = new mxCompactTreeLayout(graph, false);
			}
			else if (ident.equals("horizontalTree"))
			{
				layout = new mxCompactTreeLayout(graph, true);
			}
			else if (ident.equals("parallelEdges"))
			{
				layout = new mxParallelEdgeLayout(graph);
			}
			else if (ident.equals("placeEdgeLabels"))
			{
				layout = new mxEdgeLabelLayout(graph);
			}
			else if (ident.equals("organicLayout"))
			{
				layout = new mxFastOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition"))
			{
				layout = new mxPartitionLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalPartition"))
			{
				layout = new mxPartitionLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("verticalStack"))
			{
				layout = new mxStackLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalStack"))
			{
				layout = new mxStackLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("circleLayout"))
			{
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}
	/**
	 * Creates and executes the specified viewManage.
	 * 
	 * @param key Key to be used for getting the label from mxResources and also
	 * to create the viewManage instance for the commercial graph editor example.
	 * @return
	 */
	@SuppressWarnings("serial")
	public Action viewManage(final String key)
	{
		final mxIGraphLayout layout = createManage(key); 

		if (layout != null)
		{
			return new AbstractAction(mxResources.get(key))
			{
				public void actionPerformed(ActionEvent e)
				{
					if (layout != null)
					{
						Object cell = graphComponent.getGraph()
								.getSelectionCell();

						if (cell == null
								|| graphComponent.getGraph().getModel()
										.getChildCount(cell) == 0)
						{
							cell = graphComponent.getGraph().getDefaultParent();
						}

						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0)
								+ " ms");
					}
				}

			};
		}
		else
		{
			return new AbstractAction(mxResources.get(key))
			{

				public void actionPerformed(ActionEvent e)
				{
					JOptionPane.showMessageDialog(graphComponent, mxResources
							.get("noLayout"));
				}

			};
		}
	}
	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createManage(String ident)
	{
		mxIGraphLayout layout = null;

		if (ident != null)
		{
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph);
			}
			else if (ident.equals("horizontalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			}
			else if (ident.equals("verticalTree"))
			{
				layout = new mxCompactTreeLayout(graph, false);
			}
			else if (ident.equals("horizontalTree"))
			{
				layout = new mxCompactTreeLayout(graph, true);
			}
			else if (ident.equals("parallelEdges"))
			{
				layout = new mxParallelEdgeLayout(graph);
			}
			else if (ident.equals("placeEdgeLabels"))
			{
				layout = new mxEdgeLabelLayout(graph);
			}
			else if (ident.equals("organicLayout"))
			{
				layout = new mxFastOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition"))
			{
				layout = new mxPartitionLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalPartition"))
			{
				layout = new mxPartitionLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("verticalStack"))
			{
				layout = new mxStackLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalStack"))
			{
				layout = new mxStackLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("circleLayout"))
			{
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}

	public void internalFrameActivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosed(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		 int result = JOptionPane.showConfirmDialog(null, "确认关闭吗", "关闭确认框", JOptionPane.YES_NO_OPTION);
		    System.out.println("#chose : " + result);
		    if(result==1){
		     System.out.println("#阻止关闭..");
		     setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		    }else{
		     System.out.println("#确认关闭");
		     setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    }

//		if(this.isModified()){
//			int i=JOptionPane.showConfirmDialog(this, mxResources
//					.get("ifsavethegraph"));
//			if(i == JOptionPane.YES_OPTION){
//				new SaveAction(false);
//				this.doDefaultCloseAction();
//			}
//			else if(i == JOptionPane.NO_OPTION){
//				this.doDefaultCloseAction();
//			}
//		}else {
//			System.out.print("the graph has not been modified");
//			this.doDefaultCloseAction();
//		}
	}

	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameIconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameOpened(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
