package com.mxgraph.cdanalysis;
import com.mxgraph.model.mxCell;
public class CDGraph implements Graph{

	private static class CDEdge implements Edge{
		private static final CDEdge NULL_EDGE=new CDEdge();
		
//		CDVertex from;
//		CDVertex to;
		mxCell from;
		mxCell to;
		String value;
		CDEdge nextEdge;
		private CDEdge(){
			value=null;
		}
		CDEdge(mxCell from,mxCell to,String value){
			this.from=from;
			this.to=to;
	        this.value=value;
		}
		CDEdge(mxCell edge){
			this.value=(String) edge.getValue();
			if(value.equals("是一种")||value.equals("构成组成")){
				this.from=(mxCell) edge.getTarget();
				this.to=(mxCell) edge.getSource();
			}else{
				this.from=(mxCell) edge.getSource();
				this.to=(mxCell) edge.getTarget();
			}
		}

		public String getvalue() {
			// TODO Auto-generated method stub
			return value;
		}
	}
	
	private static class EdgeStaticQueue{
		CDEdge first;
		CDEdge last;
	}
	private int numVertexes;
	private String[] labels;
	private int numEdges;
	private EdgeStaticQueue[] edgeQueues;
	private boolean[] VisitTags;
	private static class CDVertex implements Vertex{

		public String getvalue() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public CDGraph(mxCell g){
		
	}
	public void assignLabels() {
		// TODO Auto-generated method stub
		
	}

	public int edgeNum() {
		// TODO Auto-generated method stub
		return numEdges;
	}

	public Edge firstEdge(int v) {
		// TODO Auto-generated method stub 
		if(v>=numVertexes)    throw new IllegalArgumentException();
        return edgeQueues[v].first;
	}

	public mxCell fromVertex(Edge edge) {
		// TODO Auto-generated method stub
		return ((CDEdge)edge).from;
	}

	public String getVertexLabels(int v) {
		// TODO Auto-generated method stub
		return labels[v];
	}

	public boolean isEdge(Edge edge) {
		// TODO Auto-generated method stub
		return (edge!=CDEdge.NULL_EDGE);
	}

	public Edge nextEdge(Edge edge) {
		// TODO Auto-generated method stub
		return ((CDEdge)edge).nextEdge;
	}

	public void setEdge(mxCell from, mxCell to, String value) {
		// TODO Auto-generated method stub
		CDEdge edge=new CDEdge(from,to,value);
		edge.nextEdge=edge.NULL_EDGE;

	}

	public int toVertex(Edge edge) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int vertexesNum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
