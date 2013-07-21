package com.mxgraph.examples.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.view.mxGraph;

public class ClickHandler extends JFrame
{
	protected transient JComponent preview;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2764911804288120883L;

	public ClickHandler()
	{
		super("Hello, World!");

		final mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
		   Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
		         30);
		   Object v2 = graph.insertVertex(parent, null, "World!",
		         240, 150, 80, 30);
		   Object v3 = graph.insertVertex(parent, null, "nihao", 320, 20, 80,
			         30);
		   Object v4 = graph.insertVertex(parent, null, "shijie!",
			         440, 150, 80, 30);
		   Object edge=graph.insertEdge(parent, null, "等价", v1, v2);
		   Object edge2=graph.insertEdge(parent, null, "haha", v2, v3);
		   Object edge3=graph.insertEdge(parent, null, "哦", v4, v2);
//		   if(graph.getLabel(edge)=="等价"){
//			   System.out.println("an equi edge");
//		   }
		   
		}
		finally
		{
		   graph.getModel().endUpdate();
		}
		
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		//graphComponent.setBackground(Color.YELLOW);
		JButton button=new JButton("等价合并");
		JPanel panel=new JPanel();
		panel.add(button);
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object[] edgeArray;
				mxGraph graph=graphComponent.getGraph();
				mxIGraphModel model=graph.getModel();
				Object  parent=graph.getDefaultParent();
				// Finds the relevant edges for the layout
				Object[] edges=graph.getChildEdges(parent);
				//source of an edge
				
				List<Object> tmp = new ArrayList<Object>(edges.length);

				for (int i = 0; i < edges.length; i++)
				{					
						tmp.add(edges[i]);					
				}

				edgeArray = tmp.toArray();
				
				int n=edgeArray.length;
				
				model.beginUpdate();
				try{
				for(int j=0;j<n;j++)
				{
					if(graph.getLabel((mxICell)edgeArray[j])=="等价"){
					System.out.println("这是一个等价边");
					Object source=graph.getSource(edgeArray[j]);
					Object target=graph.getTarget(edgeArray[j]);
					//conedges=graph.getAllEdges(target);
                    graph.convertEdges(source, target);
                    model.remove(edgeArray[j]);
                    model.remove(target);
                    graph.refresh();
					}
					
				}
				}
				finally{
					model.endUpdate();
				}
			}
			
		});
		getContentPane().add(BorderLayout.NORTH,graphComponent);
		getContentPane().add(BorderLayout.SOUTH,panel);
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
		frame.setSize(600, 700);
		frame.setVisible(true);
	}

}
