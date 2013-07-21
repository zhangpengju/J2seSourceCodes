package com.mxgraph.swing.examples; 

import java.awt.Color;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 
import javax.swing.JFrame; 
import javax.swing.UnsupportedLookAndFeelException; 

import com.mxgraph.swing.mxGraphComponent; 
import com.mxgraph.view.mxGraph; 

public class ClickHandler extends JFrame 
{ 

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public ClickHandler() 
{ 
super("Hello, World!测试"); 

final mxGraph graph = new mxGraph();   
Object parent = graph.getDefaultParent(); 
graph.getModel().beginUpdate();         
try   
{ 
  Object v1 = graph.insertVertex(parent, null, "中文hello", 20, 20, 80, 
        30); 
  Object v2 = graph.insertVertex(parent, null, "World!", 
        240, 150, 80, 30); 
  graph.insertEdge(parent, null, "Edge", v1, v2); 
} 
finally 
{ 
	/*graph.selectAll(); 
	graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, "宋体,Arial"); 
	graph.selectCells(false, false); */

  graph.getModel().endUpdate(); 
} 

final mxGraphComponent graphComponent = new mxGraphComponent(graph);
/*
 * 设置graph的背景颜色
 */
graphComponent.getViewport().setOpaque(false);
graphComponent.setBackground(Color.MAGENTA);

getContentPane().add(graphComponent); 

graphComponent.getGraphControl().addMouseListener(new MouseAdapter() 
{ 

public void mouseReleased(MouseEvent e) 
{ 
Object cell = graphComponent.getCellAt(e.getX(), e.getY()); 

if (cell != null) 
{ 
System.out.println("cell="+graph.getLabel(cell)); 
} 
} 
}); 
} 

public static void main(String[] args) 
{ 
ClickHandler frame = new ClickHandler(); 
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
frame.setSize(400, 320); 
frame.setVisible(true); 
} 

} 