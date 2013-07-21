package com.mxgraph.viewManage;

import java.util.ArrayList;
import java.util.List;
import com.mxgraph.model.*;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxICell;

public class mxCombinEqui extends mxGraphViewManage{
	/**
	 * An array of all edges to be deal with.
	 */
	protected Object[] edgeArray;

	public mxCombinEqui(mxGraph graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	public void execute(Object parent) {
		// TODO Auto-generated method stub
		mxIGraphModel model = graph.getModel();

		// Finds the relevant edges for the layout
		Object[] edges=graph.getChildEdges(parent);
		//edges of a cell
		Object[] conedges;
		//source of an edge
		
		List<Object> tmp = new ArrayList<Object>(edges.length);

		for (int i = 0; i < edges.length; i++)
		{
			if (!isVertexIgnored(edges[i]))
			{
				tmp.add(edges[i]);
			}
		}

		edgeArray = tmp.toArray();
		
		int n=edgeArray.length;
		
		model.beginUpdate();
		try{
			for(int j=0;j<n;j++)
			{
				if(graph.getLabel((mxICell)edgeArray[j])=="等价"){
				//TODO	System.out.println("这是一个等价边");
					Object source=graph.getSource(edgeArray[j]);
					Object target=graph.getTarget(edgeArray[j]);
					//conedges=graph.getAllEdges(target);
                    graph.convertEdges(source, target);
                    model.remove(edgeArray[j]);
                    model.remove(target);
				}
			}
		}
		finally{
			model.endUpdate();
		}
	}
   
}
