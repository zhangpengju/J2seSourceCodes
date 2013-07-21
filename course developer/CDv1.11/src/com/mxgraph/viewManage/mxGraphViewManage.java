package com.mxgraph.viewManage;

import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 * Abstract bass class for ViewManage
 */
public abstract class mxGraphViewManage implements mxIGraphViewManage{

	/**
	 * Holds the enclosing graph.
	 */
	protected mxGraph graph;
	/**
	 * Constructs a new viewManage for the specified graph.
	 */
	public mxGraphViewManage(mxGraph graph)
	{
		this.graph = graph;
	}
	/**
	 * Returns the associated graph.
	 */
	public mxGraph getGraph()
	{
		return graph;
	}
	/**
	 * Returns the constraint for the given key and cell. This implementation
	 * always returns the value for the given key in the style of the given
	 * cell.
	 * 
	 * @param key Key of the constraint to be returned.
	 * @param cell Cell whose constraint should be returned.
	 */
	public Object getConstraint(Object key, Object cell)
	{
		return getConstraint(key, cell, null, false);
	}

	/**
	 * Returns the constraint for the given key and cell. The optional edge and
	 * source arguments are used to return inbound and outgoing routing-
	 * constraints for the given edge and vertex. This implementation always
	 * returns the value for the given key in the style of the given cell.
	 * 
	 * @param key Key of the constraint to be returned.
	 * @param cell Cell whose constraint should be returned.
	 * @param edge Optional cell that represents the connection whose constraint
	 * should be returned. Default is null.
	 * @param source Optional boolean that specifies if the connection is incoming
	 * or outgoing. Default is false.
	 */
	public Object getConstraint(Object key, Object cell, Object edge,
			boolean source)
	{
		mxCellState state = graph.getView().getState(cell);
		Map<String, Object> style = (state != null) ? state.getStyle() : graph
				.getCellStyle(cell);

		return (style != null) ? style.get(key) : null;
	}
	/**
	 * Returns true if the given vertex has no connected edges.
	 * 
	 * @param vertex Object that represents the vertex to be tested.
	 * @return Returns true if the vertex should be ignored.
	 */
	public boolean isVertexIgnored(Object vertex)
	{
		return !graph.getModel().isVertex(vertex)
				|| !graph.isCellVisible(vertex);
	}

	/**
	 * Returns true if the given edge has no source or target terminal.
	 * 
	 * @param edge Object that represents the edge to be tested.
	 * @return Returns true if the edge should be ignored.
	 */
	public boolean isEdgeIgnored(Object edge)
	{
		mxIGraphModel model = graph.getModel();

		return !model.isEdge(edge) || !graph.isCellVisible(edge)
				|| model.getTerminal(edge, true) == null
				|| model.getTerminal(edge, false) == null;
	}

}
