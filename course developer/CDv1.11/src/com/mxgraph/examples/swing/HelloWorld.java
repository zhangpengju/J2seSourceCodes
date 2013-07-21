package com.mxgraph.examples.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.util.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxConstants;
import com.sun.org.apache.bcel.internal.generic.NEW;


public class HelloWorld extends JFrame implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	private JLayeredPane lp1=null;
	private mxGraphComponent graphComponent=null;
	private mxGraphComponent graphComponent2=null;
	public HelloWorld()
	{
		super("Hello, World!");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try
		{
			Object v1 = graph.insertVertex(parent,null, "Hello", 20, 20, 80,
					30);
			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
					80, 30);
			graph.insertEdge(parent, null, "Edge", v1, v2);
			Object v3 = graph.insertVertex(parent,null, "Hello", 20, 50, 80,
					30);
			Object v4 = graph.insertVertex(parent, null, "Hello", 220, 250, 80,
					30);
			graph.insertEdge(v1, null, "my", v3, v4);
		}
		finally
		{
			graph.selectAll(); 
		    //graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, "ËÎÌו,Arial"); 
		    graph.selectCells(false, false);	
			graph.getModel().endUpdate();
		}

		graphComponent = new mxGraphComponent(graph);
		graphComponent2 = new mxGraphComponent(graph);
		
		lp1=this.getLayeredPane();
		lp1.add(graphComponent,new Integer(300));
		lp1.add(graphComponent2,new Integer(200));
		
		graphComponent.setBounds(100,100,500,500);
		graphComponent.setVisible(true);
		graphComponent2.setBounds(50,50,500,500);
		graphComponent2.setVisible(true);
		
		 graphComponent.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("1111");
				if(e.getActionCommand().equals(graphComponent)){
				lp1.setLayer(graphComponent, new Integer(300));
				lp1.setLayer(graphComponent2, new Integer(200));}
			}
		});
		 graphComponent2.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					System.out.println("2222");
					// TODO Auto-generated method stub
					if(e.getActionCommand().equals(graphComponent2)){
					lp1.setLayer(graphComponent, new Integer(200));
					lp1.setLayer(graphComponent2, new Integer(300));}
				}
			});
		//getContentPane().add(graphComponent,BorderLayout.EAST);
		//getContentPane().add(lp1,BorderLayout.CENTER);
	}

	public static void main(String[] args)
	{
		HelloWorld frame = new HelloWorld();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(4000, 3200);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals(null)){
			
		}
	}

}
