/**
 * $Id: mxIGraphViewManage.java,v 1.1 2011/06/23 06:41:27 administrator Exp $
 * Copyright (c) 2010, zhang pengju
 */
package com.mxgraph.viewManage;

/**
 * Defines the requirements for an object that implements a graph viewManage.
 */
public interface mxIGraphViewManage
{

	/**
	 * Executes the manage for the children of the specified parent.
	 * 
	 * @param parent Parent cell that contains the children to be managed .
	 */
	void execute(Object parent);

	/**
	 * Notified when a cell is being added in a parent 
	 * 
	 * @param cell Cell which is being added.
	 * @param x X-coordinate of the new cell location.
	 * @param y Y-coordinate of the new cell location.
	 */
//	void addCell(Object cell, double x, double y);
//	
//	/**
//	 * Notified when a cell is being removed in a parent
//	 * */
//    void removeCell(Object cell);
    
    
}