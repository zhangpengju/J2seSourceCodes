package com.mxgraph.viewManage;

import java.util.Arrays;
import java.util.HashSet;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
public class cellManage extends mxGraph{

	public cellManage(){
		super();

	}
	public void edgesRemoved(){
		
	}
	/**
	 * get source of an edge
	 * */
	public Object getSource(Object edge){
		Object source=null;
		if((mxICell)edge!=null){
				 source=view.getVisibleTerminal(edge, true);				
		}
		return source;
	}
	/**
	 * get target of an edge
	 * */
public Object getTarget(Object edge){
	Object target=null;
	if((mxICell)edge!=null){
		target=view.getVisibleTerminal(edge, false);
	}
	return target;
}
/**
 * convert  v2'edges to v1
 * */
public void convertEdges(Object v1,Object v2){
	model.beginUpdate();
	try{
		Object [] conedges;
	 conedges = mxGraphModel.getEdges(model, v2);
	 for(int k=0;k<conedges.length;k++){
		mxCell edge=(mxCell)conedges[k];
		if(edge.getTarget()==v2){
			edge.setTarget((mxICell)v1);
		}
		if(edge.getSource()==v2){
			edge.setSource((mxICell)v1);
		}
	 }
	}
	catch(Exception e){
		System.out.println("不能将一个vertex的边成功转向另一个vertex");
	}
	finally{
		model.endUpdate();
	}
}
}
