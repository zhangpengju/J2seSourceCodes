package com.mxgraph.cdanalysis;

import com.mxgraph.model.mxCell;

/**
 * The graph ADT
 * @author zhangpengju
 * 
 * */
public interface Graph {

	/**
	 * init the edge node
	 * */
	public static interface Edge{
		public String getvalue();
	}
	public static interface Vertex{
		public String getvalue();
	}
	int vertexesNum();      // the vettexes num
	int edgeNum();          // the edges num 
	boolean isEdge(Edge edge);// judge if an edge or not
	
	void setEdge(mxCell from,mxCell to,String value);//set an edge
	Edge firstEdge(int vertex);  //get the first edge of a vertex
	Edge nextEdge(Edge edge);    //get the next edge of an edge
	int toVertex(Edge edge);     //get the target of the edge
	mxCell fromVertex(Edge edge);   //get the source of the edge
	String getVertexLabels(int vertex);//get the value of the vertex
	void assignLabels();
	
}
