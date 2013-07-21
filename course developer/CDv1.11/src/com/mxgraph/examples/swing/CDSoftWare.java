package com.mxgraph.examples.swing;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.w3c.dom.Document;

import com.mxgraph.examples.swing.GraphEditor.CustomGraph;
import com.mxgraph.examples.swing.GraphEditor.CustomGraphComponent;
import com.mxgraph.examples.swing.editor.BasicGraphEditor;
import com.mxgraph.examples.swing.editor.EditorMenuBar;
import com.mxgraph.examples.swing.editor.EditorToolBar;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
/**
 * Holds the main Frame of the CDsoftware. 
 */
public class CDSoftWare extends JFrame implements ActionListener,InternalFrameListener{
	
	/**
	 * Holds the shared number formatter.
	 * 
	 * @see NumberFormat#getInstance()
	 */
	public static final NumberFormat numberFormat = NumberFormat.getInstance();
	

	/**
	 * 
	 */
	protected boolean CDmodified = false;
	
	

	protected File currentFile;
	/**
	 * 定义一个数组，以存储软件中所有的BasicgraphEditor
	 */	
	public ArrayList<BasicGraphEditor> bgraphArrayList=new ArrayList<BasicGraphEditor>();


	
	public ArrayList<BasicGraphEditor> getBgraphArrayList() {
		return bgraphArrayList;
	}

	public void setBgraphArrayList(ArrayList<BasicGraphEditor> bgraphArrayList) {
		this.bgraphArrayList = bgraphArrayList;
	}

	public int counters=1;
	public int getCounters() {
		return counters;
	}

	public void setCounters(int counters) {
		this.counters = counters;
	}

	/**
	 * 
	 */
	protected mxGraphComponent graphComponent;
	
	public void setGraphComponent(mxGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}

	public BasicGraphEditor basicGraphEditor;
	public BasicGraphEditor getBasicGraphEditor() {
		return basicGraphEditor;
	}

	public void setBasicGraphEditor(BasicGraphEditor basicGraphEditor) {
		this.basicGraphEditor = basicGraphEditor;
	}

	/**
	 * Holds the URL for the icon to be used as a handle for creating new
	 * connections. This is currently unused.
	 */
	public static URL url = null;

	//GraphEditor.class.getResource("/com/mxgraph/examples/swing/images/connector.gif");

	JDesktopPane desktop;
	JDesktopPane desktopPane;

	
	
       public JDesktopPane getDesktop() {
		return desktop;
	}

	public void setDesktop(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public CDSoftWare(){
        super("课程开发建模软件");

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        desktopPane=new JDesktopPane();
        createFrame("new File"+(counters++), new CustomGraphComponent(new CustomGraph())); //create first "window"
        setContentPane(desktopPane);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new EditorToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);
        getContentPane().add(desktop,BorderLayout.CENTER);
        setJMenuBar(new EditorMenuBar(this));
        
        //setJMenuBar(createMenuBar());

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        addWindowListener(new WindowAdapter() {
        	   public void windowClosing(WindowEvent e) {
        	   int value=JOptionPane.showConfirmDialog(null, "确定要关闭软件吗？");
        	   if(value==JOptionPane.NO_OPTION){
        		   setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        		   System.out.println("I don't want to close the window");
        	   } else if(value==JOptionPane.CANCEL_OPTION){
        		   setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        		   System.out.println("I cancel  close the window");
        	   }else 
        	   if (value==JOptionPane.OK_OPTION) {
        	    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	    }
        	   }
        	   
        	  });

       }
        
       //React to menu selections.
       public void actionPerformed(ActionEvent e) {
//           this.setBasicGraphEditor((BasicGraphEditor)(desktop.getSelectedFrame()));
//           this.setGraphComponent(basicGraphEditor.getGraphComponent());
       }

       //Create a new internal frame.
          public void createFrame(String appTitle, mxGraphComponent component) {
           //this.setBasicGraphEditor(null);
           BasicGraphEditor frame = new BasicGraphEditor(appTitle,component);
           frame.addInternalFrameListener(this);
           frame.setVisible(true); //necessary as of 1.3s
           desktop.add(frame);
           this.setBasicGraphEditor(frame);
           this.setGraphComponent(frame.getGraphComponent());
           try {
               frame.setSelected(true);
              // graphComponent=frame.getGraphComponent();
           } catch (java.beans.PropertyVetoException e) {}
       }

       //Quit the application.
//       protected void quit() {
//           System.exit(0);
//       }

       /**
        * Create the GUI and show it.  For thread safety,
        * this method should be invoked from the
        * event-dispatching thread.
        */
       private static void createAndShowGUI() {
           //Make sure we have nice window decorations.
           JFrame.setDefaultLookAndFeelDecorated(true);

           //Create and set up the window.
           CDSoftWare frame = new CDSoftWare();

           //Display the window.
           frame.setVisible(true);
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

       public static void main(String[] args) {
           //Schedule a job for the event-dispatching thread:
           //creating and showing this application's GUI.
           javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   createAndShowGUI();
               }
           });
       }
   	/**
   	 * 
   	 */
   	public static class CustomGraphComponent extends mxGraphComponent
   	{

   		/**
   		 * 
   		 */
   		private static final long serialVersionUID = -6833603133512882012L;

   		/**
   		 * 
   		 * @param graph
   		 * 设置操作graph的页面
   		 */
   		public CustomGraphComponent(mxGraph graph)
   		{
   			super(graph);

   			// Sets switches typically used in an editor
   			setPageVisible(true);
   			//setPageVisible(false);
   			setGridVisible(true);
   			setToolTips(true);
   			getConnectionHandler().setCreateTarget(true);

   			// Loads the defalt stylesheet from an external file
   			mxCodec codec = new mxCodec();
   			Document doc = mxUtils.loadDocument(GraphEditor.class.getResource(
   					"/com/mxgraph/examples/swing/resources/default-style.xml")
   					.toString());
   			codec.decode(doc.getDocumentElement(), graph.getStylesheet());

   			// Sets the background to white
   			getViewport().setOpaque(false);
   			setBackground(Color.WHITE);
   		}

   		/**
   		 * Overrides drop behaviour to set the cell style if the target
   		 * is not a valid drop target and the cells are of the same
   		 * type (eg. both vertices or both edges). 
   		 */
   		public Object[] importCells(Object[] cells, double dx, double dy,
   				Object target, Point location)
   		{
   			if (target == null && cells.length == 1 && location != null)
   			{
   				target = getCellAt(location.x, location.y);

   				if (target instanceof mxICell && cells[0] instanceof mxICell)
   				{
   					mxICell targetCell = (mxICell) target;
   					mxICell dropCell = (mxICell) cells[0];

   					if (targetCell.isVertex() == dropCell.isVertex()
   							|| targetCell.isEdge() == dropCell.isEdge())
   					{
   						mxIGraphModel model = graph.getModel();
   						model.setStyle(target, model.getStyle(cells[0]));
   						graph.setSelectionCell(target);

   						return null;
   					}
   				}
   			}

   			return super.importCells(cells, dx, dy, target, location);
   		}

   	}

   	/**
   	 * A graph that creates new edges from a given template edge.
   	 */
   	public static class CustomGraph extends mxGraph
   	{
   		/**
   		 * Holds the edge to be used as a template for inserting new edges.
   		 */
   		protected Object edgeTemplate;

   		/**
   		 * Custom graph that defines the alternate edge style to be used when
   		 * the middle control point of edges is double clicked (flipped).
   		 */
   		public CustomGraph()
   		{
   			setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
   		}

   		/**
   		 * Sets the edge template to be used to inserting edges.
   		 */
   		public void setEdgeTemplate(Object template)
   		{
   			edgeTemplate = template;
   		}

   		/**
   		 * Prints out some useful information about the cell in the tooltip.
   		 */
   		public String getToolTipForCell(Object cell)
   		{
   			String tip = "<html>";
   			mxGeometry geo = getModel().getGeometry(cell);
   			mxCellState state = getView().getState(cell);

   			if (getModel().isEdge(cell))
   			{
   				tip += "points={";

   				if (geo != null)
   				{
   					List<mxPoint> points = geo.getPoints();

   					if (points != null)
   					{
   						Iterator<mxPoint> it = points.iterator();

   						while (it.hasNext())
   						{
   							mxPoint point = it.next();
   							tip += "[x=" + numberFormat.format(point.getX())
   									+ ",y=" + numberFormat.format(point.getY())
   									+ "],";
   						}

   						tip = tip.substring(0, tip.length() - 1);
   					}
   				}

   				tip += "}<br>";
   				tip += "absPoints={";

   				if (state != null)
   				{

   					for (int i = 0; i < state.getAbsolutePointCount(); i++)
   					{
   						mxPoint point = state.getAbsolutePoint(i);
   						tip += "[x=" + numberFormat.format(point.getX())
   								+ ",y=" + numberFormat.format(point.getY())
   								+ "],";
   					}

   					tip = tip.substring(0, tip.length() - 1);
   				}

   				tip += "}";
   			}
   			else
   			{
   				tip += "geo=[";

   				if (geo != null)
   				{
   					tip += "x=" + numberFormat.format(geo.getX()) + ",y="
   							+ numberFormat.format(geo.getY()) + ",width="
   							+ numberFormat.format(geo.getWidth()) + ",height="
   							+ numberFormat.format(geo.getHeight());
   				}

   				tip += "]<br>";
   				tip += "state=[";

   				if (state != null)
   				{
   					tip += "x=" + numberFormat.format(state.getX()) + ",y="
   							+ numberFormat.format(state.getY()) + ",width="
   							+ numberFormat.format(state.getWidth())
   							+ ",height="
   							+ numberFormat.format(state.getHeight());
   				}

   				tip += "]";
   			}

   			mxPoint trans = getView().getTranslate();

   			tip += "<br>scale=" + numberFormat.format(getView().getScale())
   					+ ", translate=[x=" + numberFormat.format(trans.getX())
   					+ ",y=" + numberFormat.format(trans.getY()) + "]";
   			tip += "</html>";

   			return tip;
   		}

   		/**
   		 * Overrides the method to use the currently selected edge template for
   		 * new edges.
   		 * 
   		 * @param graph
   		 * @param parent
   		 * @param id
   		 * @param value
   		 * @param source
   		 * @param target
   		 * @param style
   		 * @return
   		 */
   		public Object createEdge(Object parent, String id, Object value,
   				Object source, Object target, String style)
   		{
   			if (edgeTemplate != null)
   			{
   				mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
   				edge.setId(id);

   				return edge;
   			}

   			return super.createEdge(parent, id, value, source, target, style);
   		}

   	}

	public void internalFrameActivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
	  BasicGraphEditor basicGraphEditor=(BasicGraphEditor)arg0.getSource();
      this.setBasicGraphEditor(basicGraphEditor);
      this.setGraphComponent(basicGraphEditor.getGraphComponent());
      System.out.println(basicGraphEditor.getAppTitle());
	}

	public void internalFrameClosed(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub
		
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
	
	public boolean isCDmodified() {
		JDesktopPane desktopPane=this.getDesktop();
		JInternalFrame[] frames = desktopPane.getAllFrames(); 
		for(int i=0;i<frames.length;i++){ 
			if(frames[i] instanceof BasicGraphEditor){
				bgraphArrayList.add((BasicGraphEditor)frames[i]);
			    BasicGraphEditor basicGraphEditor=(BasicGraphEditor)frames[i];
			    if(basicGraphEditor.isModified()){
			    	return true;
			    }
			}
		}
		
		return CDmodified;
	}
	/**
	 * 未完待续
	 */
	public void setCDmodified(boolean cDmodified) {
		boolean oldValue = this.CDmodified;
		this.CDmodified = cDmodified;

		firePropertyChange("modified", oldValue, CDmodified);

		if (oldValue != CDmodified)
		{
			updateTitle();
		}
	}
	/**
	 * 未完待续
	 */
	public void updateTitle()
       {}

}
