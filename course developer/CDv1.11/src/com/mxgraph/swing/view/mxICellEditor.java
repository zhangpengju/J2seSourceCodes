/**
 * $Id: mxICellEditor.java,v 1.1 2011/06/23 06:41:35 administrator Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package com.mxgraph.swing.view;

import java.util.EventObject;

/**
 *
 */
public interface mxICellEditor
{

	/**
	 * Returns the cell that is currently being edited.
	 */
	public Object getEditingCell();

	/**
	 * Starts editing the given cell.
	 */
	public void startEditing(Object cell, EventObject trigger);

	/**
	 * Stops the current editing.
	 */
	public void stopEditing(boolean cancel);

}
