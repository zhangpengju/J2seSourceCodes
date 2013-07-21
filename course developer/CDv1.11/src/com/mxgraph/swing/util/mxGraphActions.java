/**
 * $Id: mxGraphActions.java,v 1.1 2011/06/23 06:41:20 administrator Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package com.mxgraph.swing.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import sun.security.provider.certpath.Vertex;

import com.mxgraph.examples.swing.CDSoftWare;
import com.mxgraph.examples.swing.GraphEditor;
import com.mxgraph.examples.swing.editor.BasicGraphEditor;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxCellVisitor;
import com.mxgraph.viewManage.mxGraphViewManage;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.File;
/**
 *
 */
public class mxGraphActions
{
	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final BasicGraphEditor getEditor(ActionEvent e)
	{
		if (e.getSource() instanceof Component)
		{
			Component component = (Component) e.getSource();

			while (component != null
					&& !(component instanceof BasicGraphEditor))
			{
				component = component.getParent();
			}

			return (BasicGraphEditor) component;
		}

		return null;
	}
	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final CDSoftWare getCDEditor(ActionEvent e)
	{
		if (e.getSource() instanceof Component)
		{
			Component component = (Component) e.getSource();

			while (component != null
					&& !(component instanceof CDSoftWare))
			{
				component = component.getParent();
			}

			return (CDSoftWare) component;
		}

		return null;
	}

	/**
	 * 
	 */
	static final Action deleteAction = new DeleteAction("delete");

	/**
	 * 
	 */
	static final Action editAction = new EditAction("edit");

	/**
	 * 
	 */
	static final Action groupAction = new GroupAction("group");

	/**
	 * 
	 */
	static final Action ungroupAction = new UngroupAction("ungroup");

	/**
	 * 
	 */
	static final Action removeFromParentAction = new RemoveFromParentAction(
			"removeFromParent");

	/**
	 * 
	 */
	static final Action updateGroupBoundsAction = new UpdateGroupBoundsAction(
			"updateGroupBounds");

	/**
	 * 
	 */
	static final Action selectAllAction = new SelectAction("selectAll");
	/**
	 * add a trigger
	 * */
	protected transient static EventObject trigger;
	/**
	 * 
	 */
	static final Action selectVerticesAction = new SelectAction("vertices");

	/**
	 * 
	 */
	static final Action selectEdgesAction = new SelectAction("edges");

	/**
	 * 
	 */
	static final Action selectNoneAction = new SelectAction("selectNone");

	/**
	 *
	 */
	static final Action selectNextAction = new SelectAction("selectNext");

	/**
	 * 
	 */
	static final Action selectPreviousAction = new SelectAction(
			"selectPrevious");

	/**
	 * 
	 */
	static final Action selectParentAction = new SelectAction("selectParent");

	/**
	 * 
	 */
	static final Action selectChildAction = new SelectAction("selectChild");

	/**
	 * 
	 */
	static final Action collapseAction = new FoldAction("collapse");

	/**
	 * 
	 */
	static final Action expandAction = new FoldAction("expand");

	/**
	 * 
	 */
	static final Action enterGroupAction = new DrillAction("enterGroup");

	/**
	 * 
	 */
	static final Action exitGroupAction = new DrillAction("exitGroup");

	/**
	 * 
	 */
	static final Action homeAction = new DrillAction("home");

	/**
	 * 
	 */
	static final Action zoomActualAction = new ZoomAction("actual");

	/**
	 * 
	 */
	static final Action zoomInAction = new ZoomAction("zoomIn");
     /**
      * 
      * */
	static final Action combinEquiAction=new CombinEquiAction("combinEqui");
	/**
	 * 
	 */
	static final Action hideRelationAction=new HideRelationAction("hideRelation");
	/**
	 * 
	 */
	static final Action renameRelationAction=new RenameRelationAction("renameRelation");
    /**
     * 
     * */
	static final Action transferUpperAction=new TransferUpperAction("transferUpper");
    /**
     * 
     * */
	static final Action hideSubcellAction=new HideSubcellAction("hideSubcell");
    /**
     * 
     * */
	static final Action createNewVertexAction=new CreateNewVertexAction("createNewVertex");
	 /**
     * 
     * */
	static final Action pruneOnceAction=new PruneOnceAction("pruneOnce");
	 /**
     * 
     * */
	static final Action pruneAllAction=new PruneAllAction("pruneAll");
	 /**
     * 
     * */
	static final Action unPruneAllAction=new UnPruneAllAction("unPruneAll");
	 /**
     * 
     * */
	static final Action unPruneOnceAction=new UnPruneOnceAction("unPruneOnce");
	 /**
     * 
     * */
	static final Action exportAction=new ExportAction("export");
	 /**
     * 
     * */
	static final Action importAction=new ImportAction("import");
	static final Action recoveryAction=new RecoveryAction("recovery"); 
	/**
	 * 
	 * */
	static final Action zoomOutAction = new ZoomAction("zoomOut");

	/**
	 * 
	 */
	static final Action toBackAction = new LayerAction("toBack");

	/**
	 * 
	 */
	static final Action toFrontAction = new LayerAction("toFront");

	/**
	 * 
	 * @return the delete action
	 */
	public static Action getDeleteAction()
	{
		return deleteAction;
	}

	/**
	 * 
	 * @return the edit action
	 */
	public static Action getEditAction()
	{
		return editAction;
	}

	/**
	 * 
	 * @return the edit action
	 */
	public static Action getGroupAction()
	{
		return groupAction;
	}

	/**
	 * 
	 * @return the edit action
	 */
	public static Action getUngroupAction()
	{
		return ungroupAction;
	}

	/**
	 * 
	 * @return the edit action
	 */
	public static Action getRemoveFromParentAction()
	{
		return removeFromParentAction;
	}

	/**
	 * 
	 * @return the edit action
	 */
	public static Action getUpdateGroupBoundsAction()
	{
		return updateGroupBoundsAction;
	}

	/**
	 * 
	 * @return the select all action
	 */
	public static Action getSelectAllAction()
	{
		return selectAllAction;
	}

	/**
	 * 
	 * @return the select vertices action
	 */
	public static Action getSelectVerticesAction()
	{
		return selectVerticesAction;
	}

	/**
	 * 
	 * @return the select edges action
	 */
	public static Action getSelectEdgesAction()
	{
		return selectEdgesAction;
	}

	/**
	 * 
	 * @return the select none action
	 */
	public static Action getSelectNoneAction()
	{
		return selectNoneAction;
	}

	/**
	 * 
	 * @return the select next action
	 */
	public static Action getSelectNextAction()
	{
		return selectNextAction;
	}

	/**
	 * 
	 * @return the select previous action
	 */
	public static Action getSelectPreviousAction()
	{
		return selectPreviousAction;
	}

	/**
	 * 
	 * @return the select parent action
	 */
	public static Action getSelectParentAction()
	{
		return selectParentAction;
	}

	/**
	 * 
	 * @return the select child action
	 */
	public static Action getSelectChildAction()
	{
		return selectChildAction;
	}

	/**
	 * 
	 * @return the go into action
	 */
	public static Action getEnterGroupAction()
	{
		return enterGroupAction;
	}

	/**
	 * 
	 * @return the go up action
	 */
	public static Action getExitGroupAction()
	{
		return exitGroupAction;
	}

	/**
	 * 
	 * @return the home action
	 */
	public static Action getHomeAction()
	{
		return homeAction;
	}

	/**
	 * 
	 * @return the collapse action
	 */
	public static Action getCollapseAction()
	{
		return collapseAction;
	}

	/**
	 * 
	 * @return the expand action
	 */
	public static Action getExpandAction()
	{
		return expandAction;
	}

	/**
	 * 
	 * @return the zoom actual action
	 */
	public static Action getZoomActualAction()
	{
		return zoomActualAction;
	}

	/**
	 * 
	 * @return the zoom in action
	 */
	public static Action getZoomInAction()
	{
		return zoomInAction;
	}
	/**
	 * 
	 * @return the CombinEqui action
	 */
	public static Action getCombinEquiAction()
	{
		return combinEquiAction;
	}
	/**
	 * 
	 * @return the HideRelaiton action
	 */
	public static Action getHideRelationAction()
	{
		return hideRelationAction;
	}
	/**
	 * 
	 * @return the renameRelaiton action
	 */
	public static Action getRenameRelationAction()
	{
		return renameRelationAction;
	}
	/**
	 * 
	 * @return the hidesubcell action
	 */
	public static Action getHideSubcellAction()
	{
		return hideSubcellAction;
	}
	/**
	 * 
	 * @return the hidesubcell action
	 */
	public static Action getCreateNewVertexAction()
	{
		return createNewVertexAction;
	}
	/**
	 * 
	 * @return the pruneOnce action
	 */
	public static Action getPruneOnceAction()
	{
		return pruneOnceAction;
	}
	/**
	 * 
	 * @return the pruneAll action
	 */
	public static Action getPruneAllAction()
	{
		return pruneAllAction;
	}
	/**
	 * 
	 * @return the unpruneAll action
	 */
	public static Action getUnPruneAllAction()
	{
		return unPruneAllAction;
	}
	/**
	 * 
	 * @return the unpruneOnce action
	 */
	public static Action getUnPruneOnceAction()
	{
		return unPruneOnceAction;
	}
	/**
	 * 
	 * @return the unpruneOnce action
	 */
	public static Action getExportAction()
	{
		return exportAction;
	}
	/**
	 * 
	 * @return the unpruneOnce action
	 */
	public static Action getImportAction()
	{
		return importAction;
	}
	/**
	 * 
	 * @return the unpruneOnce action
	 */
	public static Action getRecoveryAction()
	{
		return recoveryAction;
	}
	/**
	 * 
	 * @return the transferUpper action
	 */
	public static Action getTransferUpperAction()
	{
		return transferUpperAction;
	}
	/**
	 * 
	 * @return the zoom out action
	 */
	public static Action getZoomOutAction()
	{
		return zoomOutAction;
	}

	/**
	 * 
	 * @return the action that moves cell(s) to the backmost layer
	 */
	public static Action getToBackAction()
	{
		return toBackAction;
	}

	/**
	 * 
	 * @return the action that moves cell(s) to the frontmost layer
	 */
	public static Action getToFrontAction()
	{
		return toFrontAction;
	}

	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final mxGraph getGraph(ActionEvent e)
	{
		CDSoftWare editor=getCDEditor(e);
		if(null!=editor){
			BasicGraphEditor basicGraphEditor=editor.getBasicGraphEditor();
			if(null!=basicGraphEditor){
			   mxGraphComponent graphComponent=basicGraphEditor.getGraphComponent();
			   return graphComponent.getGraph();
			}
			return null;
		}
		return null;
	}

	/**
	 * 
	 */
	public static class EditAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4610112721356742702L;

		/**
		 * 
		 * @param name
		 */
		public EditAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof mxGraphComponent)
			{
				((mxGraphComponent) e.getSource()).startEditing();
			}
		}

	}

	/**
	 * 
	 */
	public static class DeleteAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8212339796803275529L;

		/**
		 * 
		 * @param name
		 */
		public DeleteAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				graph.removeCells();
			}
		}

	}

	/**
	 * 
	 */
	public static class GroupAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4718086600089409092L;

		/**
		 * 
		 * @param name
		 */
		public GroupAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		protected int getGroupBorder(mxGraph graph)
		{
			return 2 * graph.getGridSize();

		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				graph.setSelectionCell(graph.groupCells(null,
						getGroupBorder(graph)));
			}
		}

	}

	/**
	 * 
	 */
	public static class UngroupAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2247770767961318251L;

		/**
		 * 
		 * @param name
		 */
		public UngroupAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				graph.setSelectionCells(graph.ungroupCells());
			}
		}

	}

	/**
	 * 
	 */
	public static class RemoveFromParentAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7169443038859140811L;

		/**
		 * 
		 * @param name
		 */
		public RemoveFromParentAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				graph.removeCellsFromParent();
			}
		}

	}

	/**
	 * 
	 */
	public static class UpdateGroupBoundsAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4718086600089409092L;

		/**
		 * 
		 * @param name
		 */
		public UpdateGroupBoundsAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		protected int getGroupBorder(mxGraph graph)
		{
			return 2 * graph.getGridSize();
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				graph.updateGroupBounds(null, getGroupBorder(graph));
			}
		}

	}

	/**
	 * 
	 */
	public static class LayerAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 562519299806253741L;

		/**
		 * 
		 * @param name
		 */
		public LayerAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				boolean toBack = getValue(Action.NAME).toString()
						.equalsIgnoreCase("toBack");
				graph.orderCells(toBack);
			}
		}

	}

	/**
	 * 
	 */
	public static class FoldAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4078517503905239901L;

		/**
		 * 
		 * @param name
		 */
		public FoldAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				boolean collapse = getValue(Action.NAME).toString()
						.equalsIgnoreCase("collapse");
				graph.foldCells(collapse);
			}
		}

	}

	/**
	 * 
	 */
	public static class DrillAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5464382323663870291L;

		/**
		 * 
		 * @param name
		 */
		public DrillAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				String name = getValue(Action.NAME).toString();

				if (name.equalsIgnoreCase("enterGroup"))
				{
					graph.enterGroup();
				}
				else if (name.equalsIgnoreCase("exitGroup"))
				{
					graph.exitGroup();
				}
				else
				{
					graph.home();
				}
			}
		}

	}

	/**
	 * 
	 */
	public static class ZoomAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7500195051313272384L;

		/**
		 * 
		 * @param name
		 */
		public ZoomAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();

			if (source instanceof mxGraphComponent)
			{
				String name = getValue(Action.NAME).toString();
				mxGraphComponent graphComponent = (mxGraphComponent) source;

				if (name.equalsIgnoreCase("zoomIn"))
				{
					graphComponent.zoomIn();
				}
				else if (name.equalsIgnoreCase("zoomOut"))
				{
					graphComponent.zoomOut();
				}
				else
				{
					graphComponent.zoomActual();
				}
			}
		}

	}
	/**
	 * 
	 * */
	public static class CombinEquiAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public CombinEquiAction(String name){
			super(name);
		}
public void actionPerformed(ActionEvent e){
	System.out.println("激活合并！");
	Object sourcecomponent=e.getSource();
	if(sourcecomponent instanceof mxGraphComponent){
	Object[] edgeArray;
	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
	mxGraph graph=graphComponent.getGraph();
	graph.setLabelsVisible(true);
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
	Object value = model.getValue(edgeArray[j]);
	System.out.println("it's value is "+(String)value);

	if(value.equals("等价于")){

		Object source=graph.getSource((mxCell)edgeArray[j]);
		Object target=graph.getTarget((mxCell)edgeArray[j]);

		
		//remove edges of target to source
		model.remove(edgeArray[j]);
        //graph.convertApartEdges(source, target, edgeArray[j]);
        graph.convertEdges(source, target);
        model.remove(target);
        
		}
		
	}
	graph.refresh();
	}
	finally{
		model.endUpdate();
	}
}

}

}
		
	
/**
 * hide the edges who's value is 并列 or 是前提
 * */
	public static class HideRelationAction extends AbstractAction{


		/**
	 * 
	 */
	private static final long serialVersionUID = 653787049522150561L;

		public HideRelationAction(String name){
			super(name);
		}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object sourcecomponent=e.getSource();
		if(sourcecomponent instanceof mxGraphComponent){
		Object[] edgeArray;
		mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
		mxGraph graph=graphComponent.getGraph();
		graph.setLabelsVisible(true);
		mxIGraphModel model=graph.getModel();
		//TODO Object  parent=graph.getCurrentRoot()
		Object  parent=graph.getDefaultParent();
		// Finds the  edges for the graph
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
			for(int j=0;j<n;j++){
				Object value = model.getValue(edgeArray[j]);
				if(value.equals("并列")||value.equals("是前提")){
					model.remove(edgeArray[j]);
					graph.refresh();
				}
			}
			
		}catch(Exception e1){
			e1.printStackTrace();
		}
		finally{
			model.endUpdate();
		}
	}
		
	}
	}
	/**
	 * rename some relations 
	 * */
	public static class RenameRelationAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;

		public RenameRelationAction(String name){
			super(name);
		}
public void actionPerformed(ActionEvent e){

	Object sourcecomponent=e.getSource();
	if(sourcecomponent instanceof mxGraphComponent){
	Object[] edgeArray;
	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
	mxGraph graph=graphComponent.getGraph();
	graph.setLabelsVisible(true);
	mxIGraphModel model=graph.getModel();
	Object  parent=graph.getDefaultParent();
	// Finds the relevant edges for the layout
	Object[] edges=graph.getChildEdges(parent);
    
	mxCell edge;
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
	 edge=(mxCell)edgeArray[j];
	Object value = model.getValue(edge);
	System.out.println("it's value is "+(String)value);

	if(value.equals("具有特征")){        
		Object source=graph.getSource(edge);
		Object target=graph.getTarget(edge);
		
        
		edge.setTarget((mxCell)source);
        edge.setSource((mxCell)target);
        edge.setValue("包含");
		}
	if(value.equals("定义")){
		edge.setValue("包含");
	}
	if(value.equals("是一种")){
		Object sourceObject=edge.getSource();
		Object targetObject=edge.getTarget();
		if(!(mxUtils.getString(graph.getCellStyle((mxCell)sourceObject), mxConstants.STYLE_SHAPE)
				.equals(mxUtils.getString(graph.getCellStyle((mxCell)targetObject), mxConstants.STYLE_SHAPE)))){
			edge.setValue("相关");
		}
		
	}
	 graph.refresh();
	}
	}
	finally{
		model.endUpdate();
	}
}

}

}
/**
* transfer the relation between one cell with A to its upper cell B 
* */
		public static class TransferUpperAction extends AbstractAction{


			/**
		 * 
		 */
		private static final long serialVersionUID = 653787049522150561L;

			public TransferUpperAction(String name){
				super(name);
			}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object sourcecomponent=e.getSource();
			if(sourcecomponent instanceof mxGraphComponent){
			Object[] edgeArray;
			mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
			mxGraph graph=graphComponent.getGraph();
			graph.setLabelsVisible(true);
			mxIGraphModel model=graph.getModel();
			//TODO Object  parent=graph.getCurrentRoot()
			Object  parent=graph.getDefaultParent();
			// Finds the  edges for the graph
			Object[] edges=graph.getChildEdges(parent);
			//source of an edge
			
			List<Object> tmp = new ArrayList<Object>(edges.length);

			for (int i = 0; i < edges.length; i++)
			{					
					tmp.add(edges[i]);					
			}

			edgeArray = tmp.toArray();
			int n=edgeArray.length;
			int mark=0;//judge if the number of uppercells is more than 1 
			model.beginUpdate();
			try{
				for(int j=0;j<n;j++){
					mxCell edge=(mxCell)edgeArray[j];
					Object value = model.getValue(edge);
					if(value.equals("包含")||value.equals("支持")||value.equals("是工具")||value.equals("相关")){
						
						Object sub=graph.getTarget(edge); 
						Object source=graph.getSource(edge);
						Object[] upperObject=graph.getUpper(sub,graph);
						Object parentObject=edge.getParent();
						//edge.setValue("相关");
						
						if(upperObject.length!=0&&source!=null){
							for(int i=0;i<upperObject.length;i++){
								//vervalue=(String)(model.getValue(upperObject[i]));
								if(!(upperObject[i]==source)){
									if((graph.vertexsConnectionedges(upperObject[i], source))==null){
										graph.insertEdge(parentObject, null, "相关", source, upperObject[i]);
									}	
									mark++;
								}
								
							}
							//System.out.println((String)(model.getValue(upperObject)));
							if(mark>0){
								model.remove(edge);
								mark=0;
							}
							
							//graph.addEdge(edge, parentObject, source, upperObject, null);
						}
					}
				}
				
			}catch(Exception e1){
				e1.printStackTrace();
			}
			finally{
				model.endUpdate();
			}
		}
			
		}
		}
	
	
/**
 *the class  hide the subvertexs
 * */
			public static class HideSubcellAction extends AbstractAction{
		     /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public HideSubcellAction(String name){
		    	 super(name);
		     }
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent e) {

				Object sourcecomponent=e.getSource();
				if(sourcecomponent instanceof mxGraphComponent){
				Object[] vertexArray;
				Object[] upperObjects;
				Object[] conedges;//the exc edges of the subcell
				Object[] bEdge;//edge between the upper and sub cells
				Object vertex;
				mxCell edge;
				mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
				mxGraph graph=graphComponent.getGraph();
				graph.setLabelsVisible(true);
				mxIGraphModel model=graph.getModel();
				Object  parent=graph.getDefaultParent();
				// Finds the relevant edges for the layout
				Object[] vertexts=graph.getChildVertices(parent);
				//System.out.println("the num is"+vertexts.length);
				//source of an vertex				
				List<Object> tvp = new ArrayList<Object>(vertexts.length);
				for(int i=0;i<vertexts.length;i++){
					tvp.add(vertexts[i]);
				}
				vertexArray=tvp.toArray();
				
				int n=vertexArray.length;
				
				model.beginUpdate();
				try{
					
				for(int j=0;j<n;j++)
				{
					
					
//					String string=((mxCell)vertexArray[j]).getStyle();
//					System.out.println(string);
////					Map<String, Object> style=new mxCellState().getStyle();
////					String name=mxUtils.getString(style, mxConstants.STYLE_SHAPE, null);
////					System.out.println("the shape is a"+name);
					//(graph.getCellStyle((mxCell)vertexArray[j])).get(arg0);
//					String name=mxUtils.getString(graph.getCellStyle((mxCell)vertexArray[j]), mxConstants.STYLE_SHAPE);
//
//					System.out.println(name);
					upperObjects=graph.getUpper(vertexArray[j],graph);
					if((upperObjects!=null)&&(upperObjects.length!=0)){
						
						//System.out.println("the upper is not null");
						if(upperObjects.length==1){
							bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[0], vertexArray[j], false);
							
							if(bEdge.length!=0){
								model.remove(bEdge[0]);
							}
							graph.convertEdges(upperObjects[0], vertexArray[j]);
							model.remove(vertexArray[j]);
						}
						else{
							for(int k=0;k<upperObjects.length;k++){
								bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[k], vertexArray[j], false);
								if(bEdge.length!=0){
									model.remove(bEdge[0]);
								}		
							}
							conedges=mxGraphModel.getEdges(model, vertexArray[j]);
							for(int i=0;i<conedges.length;i++){
								edge=(mxCell)conedges[i];
								if(edge.getTarget()==vertexArray[j]){
									vertex=edge.getSource();
									for(int p=0;p<upperObjects.length;p++){
										graph.insertEdge(edge.getParent(), null, edge.getValue(), vertex, upperObjects[p]);
									}
									model.remove(edge);
								}
								else 
								if(edge.getSource()==vertexArray[j]){
									vertex=edge.getTarget();
									for(int p=0;p<upperObjects.length;p++){
										graph.insertEdge(edge.getParent(), null, edge.getValue(), upperObjects[p],vertex );
									}
									model.remove(edge);
								}
							}
							for(int k=0;k<(upperObjects.length-1);k++){
								if(graph.vertexsConnectionedges(upperObjects[k], upperObjects[k+1])==null){
									graph.insertEdge(((mxCell)vertexArray[j]).getParent(), null, "相关", upperObjects[k], upperObjects[k+1]);
								}
							}
							model.remove(vertexArray[j]);
						}
					}
					
				}
				graph.refresh();
				}catch(Exception e1){
					e1.printStackTrace();
				}
				finally{
					model.endUpdate();
				}
			}
			
				
			}
			}	
/**
 *the class  hide the subvertexs
 * */
	public static class CreateNewVertexAction extends AbstractAction{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CreateNewVertexAction(String name){
    	 super(name);
     }
	@SuppressWarnings("null")
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		Object sourcecomponent=e.getSource();
		if(sourcecomponent instanceof mxGraphComponent){
		Object[] vertexArray;
		//Object vertex;
		Set<Object> visited=new HashSet<Object>();
		
		
		mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
		mxGraph graph=graphComponent.getGraph();
		graph.setLabelsVisible(true);
		mxIGraphModel model=graph.getModel();
		Object  parent=graph.getDefaultParent();
		// Finds the relevant edges for the layout
		Object[] vertexs=graph.getChildVertices(parent);
		//Object[] edges=graph.getChildEdges(parent);	    
		mxCell edge;
		Object vertex;
		mxCellVisitor visitor=graph.new mxCellVisitor();
		
		List<Object> tmp = new ArrayList<Object>(vertexs.length);
		for (int i = 0; i < vertexs.length; i++)
		{					
				tmp.add(vertexs[i]);					
		}

		vertexArray = tmp.toArray();
		
		int n=vertexArray.length;
		
		model.beginUpdate();
		try{
			for(int j=0;j<n;j++){
				vertex=vertexArray[j];
				graph.CDtraverse(vertex, true, visitor, null, visited, graph,graphComponent);
			}

			
		}catch(Exception e1){
			e1.printStackTrace();
		}
		finally{
			model.endUpdate();
			}
	}
		
	}
	}
	/**
	 * v1.2
	 * to prune the graph once
	 * */
	public static class PruneOnceAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public PruneOnceAction(String name){
			super(name);
		}
public void actionPerformed(ActionEvent e){
	System.out.println("开始一次压缩！");
	Object sourcecomponent=e.getSource();
	if(sourcecomponent instanceof mxGraphComponent){
    mxICellOverlay[] overlays;
	Object[] edgeArray;
	Object[] vertexArray;
	Object[] upperObjects;
	Object[] lowerObjects;
	Object[] bEdge;
	Object[] conedges;
	int prunednumvar;
	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
	mxGraph graph=graphComponent.getGraph();
	graph.setLabelsVisible(true);
	mxIGraphModel model=graph.getModel();
	Object  parent=graph.getDefaultParent();
	// Finds the relevant edges for the layout
	Object[] edges=graph.getChildEdges(parent);
	Object[] vertexs=graph.getChildVertices(parent);
	//source of an edge
	mxCell edge;
	boolean ispruned=false;
	boolean issubhided=false;
	boolean istransupered=false;
	boolean iscreatenew=false;
	int createnewnum=0;
	int mark=0;
	int prunednum=0;
	int cellprunable=0;
	boolean prunedall=false;
	boolean own=false;
	int graphMaxLayer=0;//图中节点的最大层数
	int recordGraphMaxLayer;
	int graphLayerVar=0;
	Object[] vertexArraym=null;
	int mm=0;
	List<Object> tmp = new ArrayList<Object>(edges.length);
	ArrayList<Object> vertexlist=new ArrayList<Object>();
	List<Object> vmp = new ArrayList<Object>(vertexs.length);
	Set<Object> visited=new HashSet<Object>();
	Object vertex;
	mxCell addedvertexCell;
	mxCellVisitor visitor=graph.new mxCellVisitor();
	
	for (int i = 0; i < edges.length; i++)
	{					
			tmp.add(edges[i]);					
	}

	edgeArray = tmp.toArray();
	
	int n=edgeArray.length;
	
	for (int i = 0; i < vertexs.length; i++)
	{					
			vmp.add(vertexs[i]);					
	}

	vertexArray = vmp.toArray();
	
	int m=vertexArray.length;
	boolean pruned=false;
	for(int la=0;la<m;la++){
		int celllayervar=graph.getCellLayer(vertexArray[la]);//此处程序停止响应
		((mxCell)vertexArray[la]).setLayer(celllayervar);
		if(celllayervar>graphMaxLayer){
			graphMaxLayer=celllayervar;
		}
	}//set the layer of all vertexs
	recordGraphMaxLayer=graphMaxLayer;
	model.beginUpdate();
	try{
	if(graph.prunedLayer.isEmpty()){
		graph.prunedLayer.addFirst(1);
		prunednumvar=1;
	}
	else{
		prunednumvar=graph.prunedLayer.getFirst();
		prunednumvar++;
		graph.prunedLayer.addFirst(prunednumvar);
	}
	ArrayList<Object> prunedLay=new ArrayList<Object>();// 在剪枝前，将初始结点保存到一个数组中，方便恢复时使用
	for(int ed=0;ed<edges.length;ed++){
		if(((mxCell)edges[ed]).isVisible()){
			prunedLay.add(edges[ed]);
		}
	}
	for(int ve=0;ve<vertexs.length;ve++){
		if(((mxCell)vertexs[ve]).isVisible()){
			prunedLay.add(vertexs[ve]);
		}
	}
	graph.getPrunedLayCell().add(prunedLay);
	for(int j=0;j<n;j++)
	{
		
	Object value = model.getValue(edgeArray[j]);

	if((((mxCell)edgeArray[j]).isVisible())&&(value!=null)&&value.equals("等价于")){

		Object source=graph.getSource((mxCell)edgeArray[j]);
		Object target=graph.getTarget((mxCell)edgeArray[j]);

		overlays=graphComponent.getCellOverlays(target);
		if(overlays!=null&&overlays.length>0){

			overlays=null;
			((mxCell)edgeArray[j]).setVisible(false);
	        graph.copeEdges(target, source, graph);//cope and set unvisible
       	    ((mxCell)source).setVisible(false);
       	    pruned=true;
		}
		else{
		((mxCell)edgeArray[j]).setVisible(false);
        graph.copeEdges(source, target, graph);

   	    ((mxCell)target).setVisible(false);
   	    pruned=true;
		}

        
		}
		
	}//预处理“等价合并”
	for(int k=0;k<n;k++){
		Object value = model.getValue(edgeArray[k]);
		if((value!=null)&&(value.equals("并列")||value.equals("是前提"))){
			((mxCell)edgeArray[k]).setVisible(false);
			pruned=true;
			graph.refresh();
		}
	}//预处理“关系隐藏”
	for(int j=0;j<n;j++)
	{ 
	 edge=(mxCell)edgeArray[j];
	 
	Object value = model.getValue(edge);

	if((value!=null)&&value.equals("具有特征")){
        
        addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null,"包含", edge.getTarget(), edge.getSource(), edge.getStyle());
        edge.setVisible(false);
        pruned=true;
		}
	if((value!=null)&&value.equals("定义")){
		Object edgess[]=graph.getEdgesBetween(edge.getSource(), edge.getTarget());
		for(int ed=0;ed<edgess.length;ed++){
			if((((mxCell)edgess[ed]).getValue().equals("包含"))){
			if(((mxCell)edgess[ed]).getSource()==edge.getSource()&&((mxCell)edgess[ed]).getTarget()==edge.getTarget())
			{
				edge.setVisible(false);
				((mxCell)edgess[ed]).setVisible(true);
				own=true;
			}}
		}
		if(!own){
		addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null,"包含", edge.getSource(), edge.getTarget(), edge.getStyle());
		edge.setVisible(false);
        }
        pruned=true;
        own=false;
	}
	if((value!=null)&&value.equals("是一种")){
		Object sourceObject=edge.getSource();
		Object targetObject=edge.getTarget();
		if(!(mxUtils.getString(graph.getCellStyle((mxCell)sourceObject), mxConstants.STYLE_SHAPE)
				.equals(mxUtils.getString(graph.getCellStyle((mxCell)targetObject), mxConstants.STYLE_SHAPE)))){
			Object edgess[]=graph.getEdgesBetween(edge.getSource(), edge.getTarget());
			for(int ed=0;ed<edgess.length;ed++){
				if((((mxCell)edgess[ed]).getValue().equals("相关"))){
				if(((mxCell)edgess[ed]).getSource()==edge.getSource()&&((mxCell)edgess[ed]).getTarget()==edge.getTarget())
				{
					edge.setVisible(false);
					((mxCell)edgess[ed]).setVisible(true);
					own=true;
				}}
			}
			if(!own){
			addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null, "相关", edge.getSource(), edge.getTarget(), edge.getStyle());
			edge.setVisible(false);
			}
			pruned=true;
			own=false;
			}
	}
	}//预处理“关系更名”
	
	
	while(!issubhided){
		issubhided=false;
		vertexArray=graph.getChildVertices(parent);
		m=vertexArray.length;
		for(int num=0;num<m;num++){
			((mxCell)vertexArray[num]).prunable=1;
		}//全部节点都设为可隐藏
		System.out.println("该图的graphmaxlayer是"+graphMaxLayer);//graphmaxlayer是可能被剪枝到的最外面的层数
		for(int fi=0;fi<m;fi++){
			if(((mxCell)vertexArray[fi]).getLayer()==graphMaxLayer){
				vertexlist.add(vertexArray[fi]);
			}
		}
		if(vertexlist.isEmpty()){
				System.out.println("第"+graphMaxLayer+"层的结点是空的！！！");
				graphMaxLayer--;
				continue;
			}else{
				vertexArraym=vertexlist.toArray();
				mm=vertexArraym.length;
			}
//		if(mm==0){continue;}
		for(int j=0;j<mm;j++){
			upperObjects=graph.getNoInVisibleUpper(vertexArraym[j],graph);
			lowerObjects=graph.getVisibleLower(vertexArraym[j], graph);
			overlays=graphComponent.getCellOverlays(vertexArraym[j]);
			if((((mxCell)vertexArraym[j]).prunable==1)&&(lowerObjects==null||lowerObjects.length==0)&&(upperObjects!=null)&&(upperObjects.length!=0&&(overlays==null||overlays.length==0))){
				overlays=null;
				
				System.out.println("开始剪枝啦");
				if(upperObjects.length==1){
					System.out.println(((mxCell)vertexArraym[j]).getValue());
					bEdge=graph.getEdgesBetweenCells(upperObjects[0], vertexArraym[j]);
					((mxCell)upperObjects[0]).prunable=0;	
					if(bEdge==null){
						System.out.println("该节点与其父节点的连接线条数为0");
					}
					if(bEdge.length!=0){
						System.out.println(bEdge.length);
						for(int bn=0;bn<bEdge.length;bn++){
							((mxCell)bEdge[bn]).setVisible(false);
						}
					}
					
					graph.copeEdges(upperObjects[0], vertexArraym[j], graph);
					((mxCell)vertexArraym[j]).setVisible(false);//convert delete to unvisible
					issubhided=true;
					pruned=true;
				}
				else{
					for(int k=0;k<upperObjects.length;k++){
						bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[k], vertexArraym[j], false);
						if(bEdge.length!=0){
							for(int bnum=0;bnum<bEdge.length;bnum++){
							((mxCell)bEdge[bnum]).setVisible(false);//convert delete to unvisible
							}
						}
						((mxCell)upperObjects[k]).prunable=0;
					}
					conedges=mxGraphModel.getEdges(model, vertexArraym[j]);
					for(int i=0;i<conedges.length;i++){
						edge=(mxCell)conedges[i];
						if(edge.isVisible()){
						if(edge.getTarget()==vertexArraym[j]){
							vertex=edge.getSource();
							for(int p=0;p<upperObjects.length;p++){
								addedvertexCell=(mxCell) graph.insertNoEdge(edge.getParent(), null, edge.getValue(), vertex, upperObjects[p],edge.getStyle());
							}
							edge.setVisible(false);
						}
						else 
						if(edge.getSource()==vertexArraym[j]){
							vertex=edge.getTarget();
							for(int p=0;p<upperObjects.length;p++){
								addedvertexCell=(mxCell) graph.insertNoEdge(edge.getParent(), null, edge.getValue(), upperObjects[p],vertex,edge.getStyle());
							}
							edge.setVisible(false);
						}
					}
					}
					for(int k=0;k<(upperObjects.length-1);k++){
						if(graph.vertexsConnectionedges(upperObjects[k], upperObjects[k+1])==null){
							addedvertexCell=(mxCell) graph.insertNoEdge(((mxCell)vertexArraym[j]).getParent(), null, "相关", upperObjects[k], upperObjects[k+1],null);
						}
					}
					((mxCell)vertexArraym[j]).setVisible(false);//convert delete to unvisible
					issubhided=true;
					pruned=true;
				}
				System.out.println("被剪枝的真假为"+issubhided);
			}
		}//隐藏下位
		
		if(issubhided){
			System.out.println("已经剪枝成功了");
			break;} //如果隐藏下位成功则完成一次压缩
		else{
			System.out.println("开始上位转移了");
			istransupered=false;
			edgeArray=graph.getChildEdges(parent);
			n=edgeArray.length;
			for(int j=0;j<n;j++){
				edge=(mxCell)edgeArray[j];
				Object value = model.getValue(edge);
				if(edge.isVisible()){
				   if((value!=null)&&(value.equals("包含")||value.equals("支持")||value.equals("是工具")||value.equals("相关"))){
					
					Object sub=graph.getTarget(edge); 
					Object source=graph.getSource(edge);
					Object[] upperObject=graph.getUpper(sub,graph);
					lowerObjects=graph.getVisibleLower(sub, graph);
					Object parentObject=edge.getParent();
					
					if(upperObject.length!=0&&source!=null&&(lowerObjects==null||lowerObjects.length==0)){
						for(int i=0;i<upperObject.length;i++){
							if(!(upperObject[i]==source)&&((mxCell)upperObject[i]).isVisible()){
								if((graph.vertexsConnectionedges(upperObject[i], source))==null){
									addedvertexCell=(mxCell) graph.insertNoEdge(parentObject, null, "相关", source, upperObject[i],null);
								}
								mark++;
							}
						}
						if(mark>0){
							System.out.println("插入的上位转移边数位"+mark);
							edge.setVisible(false);
							mark=0;
							istransupered=true;
							pruned=true;
						}
					}
				}
				   }
			}//遍历最外层结点，并上位转移
			if(istransupered){
				continue;}
			else if(graphMaxLayer>0){
				graphMaxLayer--;
				continue;
			}//如果没有上位转移成功，则graphMaxLayer--，继续尝试下一层的剪枝
			else{
				createnewnum=0;
				vertexArray=graph.getChildVertices(parent);
				m=vertexArray.length;
				for(int j=0;j<m;j++){
					vertex=vertexArray[j];
					iscreatenew=graph.CreateNewNum(vertex, true, visitor, null, visited, graph,graphComponent);
					if(iscreatenew){
						createnewnum++;
					}
				}//尝试创建新节点
				if(createnewnum>0){
					JOptionPane.showMessageDialog(graphComponent,"已不能继续剪枝！但图文件中可创建新节点");
					break;}
				else{
					JOptionPane.showMessageDialog(graphComponent,mxResources
							.get("connotprunedonce"));
					break;
				}//不能继续剪枝
			}
		}
	}//End while循环
	if(!pruned){
		if(!graph.prunedLayer.isEmpty()){
			graph.prunedLayer.removeFirst();
		}
		if(!graph.getPrunedLayCell().isEmpty()){
			graph.getPrunedLayCell().remove(graph.getPrunedLayCell().size()-1);
		}
	}//如果此次压缩没有对图文件做任何操作，则图文件中的操作记录数组中第一位出栈
	graph.refresh();
	}
	catch (Exception ep) {

		ep.printStackTrace();
		// TODO: handle exception
	}
	finally{
		model.endUpdate();
	}
}

}

}
	/**
	 * v1.2
	 * to prune the graph all
	 * */
	public static class PruneAllAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public PruneAllAction(String name){
			super(name);
		}
public void actionPerformed(ActionEvent e){
	System.out.println("开始一次压缩！");
	Object sourcecomponent=e.getSource();
	if(sourcecomponent instanceof mxGraphComponent){
    mxICellOverlay[] overlays;
	Object[] edgeArray;
	Object[] vertexArray;
	Object[] upperObjects; 
	Object[] lowerObjects;
	Object[] bEdge;
	Object[] conedges;
	int prunednumvar;
	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
	mxGraph graph=graphComponent.getGraph();
	graph.setLabelsVisible(true);
	mxIGraphModel model=graph.getModel();
	Object  parent=graph.getDefaultParent();
	// Finds the relevant edges for the layout
	Object[] edges=graph.getChildEdges(parent);
	Object[] vertexs=graph.getChildVertices(parent);
	//source of an edge
	mxCell edge;
	boolean own=false;
	boolean ispruned=false;
	boolean issubhided=false;
	boolean issubhidedall=false;
	boolean istransupered=false;
	boolean iscreatenew=false;
	int createnewnum=0;
	int mark=0;
	int prunednum=0;
	int cellprunable=0;
	boolean prunedall=false;
	int graphMaxLayer=0;//图中节点的最大层数
	int graphLayerVar=0;
	Object[] vertexArraym=null;
	int mm=0;
	List<Object> tmp = new ArrayList<Object>(edges.length);
	ArrayList<Object> vertexlist=new ArrayList<Object>();
	List<Object> vmp = new ArrayList<Object>(vertexs.length);
	Set<Object> visited=new HashSet<Object>();
	Object vertex;
	mxCell addedvertexCell;
	mxCellVisitor visitor=graph.new mxCellVisitor();
	
	for (int i = 0; i < edges.length; i++)
	{					
			tmp.add(edges[i]);					
	}

	edgeArray = tmp.toArray();
	
	int n=edgeArray.length;
	
	for (int i = 0; i < vertexs.length; i++)
	{					
			vmp.add(vertexs[i]);					
	}

	vertexArray = vmp.toArray();
	
	int m=vertexArray.length;
	boolean pruned=false;
	for(int la=0;la<m;la++){
		int celllayervar=graph.getCellLayer(vertexArray[la]);
		((mxCell)vertexArray[la]).setLayer(celllayervar);
		if(celllayervar>graphMaxLayer){
			graphMaxLayer=celllayervar;
		}
	}//set the layer of all vertexs
	model.beginUpdate();
	try{
	if(graph.prunedLayer.isEmpty()){
		graph.prunedLayer.addFirst(1);
		prunednumvar=1;
	}
	else{
		prunednumvar=graph.prunedLayer.getFirst();
		prunednumvar++;
		graph.prunedLayer.addFirst(prunednumvar);
	}
	ArrayList<Object> prunedLay=new ArrayList<Object>();
	for(int ed=0;ed<edges.length;ed++){
		if(((mxCell)edges[ed]).isVisible()){
			prunedLay.add(edges[ed]);
		}
	}
	for(int ve=0;ve<vertexs.length;ve++){
		if(((mxCell)vertexs[ve]).isVisible()){
			prunedLay.add(vertexs[ve]);
		}
	}
	graph.getPrunedLayCell().add(prunedLay);
	for(int j=0;j<n;j++)
	{
		
	Object value = model.getValue(edgeArray[j]);

	if((((mxCell)edgeArray[j]).isVisible())&&(value!=null)&&value.equals("等价于")){

		Object source=graph.getSource((mxCell)edgeArray[j]);
		Object target=graph.getTarget((mxCell)edgeArray[j]);

		overlays=graphComponent.getCellOverlays(target);
		if(overlays!=null&&overlays.length>0){

			overlays=null;
			((mxCell)edgeArray[j]).setVisible(false);
	        graph.copeEdges(target, source, graph);//cope and set unvisible
       	    ((mxCell)source).setVisible(false);
       	    pruned=true;
		}
		else{
		((mxCell)edgeArray[j]).setVisible(false);
        graph.copeEdges(source, target, graph);

   	    ((mxCell)target).setVisible(false);
   	    pruned=true;
		}

        
		}
		
	}//预处理“等价合并”
	for(int k=0;k<n;k++){
		Object value = model.getValue(edgeArray[k]);
		if((value!=null)&&(value.equals("并列")||value.equals("是前提"))){
			((mxCell)edgeArray[k]).setVisible(false);
			pruned=true;
			graph.refresh();
		}
	}//预处理“关系隐藏”
	for(int j=0;j<n;j++)
	{ 
	 edge=(mxCell)edgeArray[j];
	 
	Object value = model.getValue(edge);

	if((value!=null)&&value.equals("具有特征")){
        
        addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null,"包含", edge.getTarget(), edge.getSource(), edge.getStyle());
        edge.setVisible(false);
        pruned=true;
		}
	if((value!=null)&&value.equals("定义")){
		Object edgess[]=graph.getEdgesBetween(edge.getSource(), edge.getTarget());
		for(int ed=0;ed<edgess.length;ed++){
			if((((mxCell)edgess[ed]).getValue().equals("包含"))){
			if(((mxCell)edgess[ed]).getSource()==edge.getSource()&&((mxCell)edgess[ed]).getTarget()==edge.getTarget())
			{
				edge.setVisible(false);
				((mxCell)edgess[ed]).setVisible(true);
				own=true;
			}}
		}
		if(!own){
		addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null,"包含", edge.getSource(), edge.getTarget(), edge.getStyle());
		edge.setVisible(false);
        }
        pruned=true;
        own=false;
	}
	if((value!=null)&&value.equals("是一种")){
		Object sourceObject=edge.getSource();
		Object targetObject=edge.getTarget();
		if(!(mxUtils.getString(graph.getCellStyle((mxCell)sourceObject), mxConstants.STYLE_SHAPE)
				.equals(mxUtils.getString(graph.getCellStyle((mxCell)targetObject), mxConstants.STYLE_SHAPE)))){
			Object edgess[]=graph.getEdgesBetween(edge.getSource(), edge.getTarget());
			for(int ed=0;ed<edgess.length;ed++){
				if((((mxCell)edgess[ed]).getValue().equals("相关"))){
				if(((mxCell)edgess[ed]).getSource()==edge.getSource()&&((mxCell)edgess[ed]).getTarget()==edge.getTarget())
				{
					edge.setVisible(false);
					((mxCell)edgess[ed]).setVisible(true);
					own=true;
				}}
			}
			if(!own){
			addedvertexCell=(mxCell) graph.insertConvertEdge(edge.getParent(), null, "相关", edge.getSource(), edge.getTarget(), edge.getStyle());
			edge.setVisible(false);
			}
			pruned=true;
			own=false;
			}
	}
	}//预处理“关系更名”
	while(!prunedall)
	{
		issubhided=false;
		vertexArray=graph.getChildVertices(parent);
		m=vertexArray.length;
		for(int num=0;num<m;num++){
			((mxCell)vertexArray[num]).prunable=1;
		}//全部节点都设为可隐藏
		System.out.println("该图的graphmaxlayer是"+graphMaxLayer);//graphmaxlayer是可能被剪枝到的最外面的层数
		for(int fi=0;fi<m;fi++){
			if(((mxCell)vertexArray[fi]).getLayer()==graphMaxLayer){
				vertexlist.add(vertexArray[fi]);
			}
		}
		if(vertexlist.isEmpty()){
				System.out.println("第"+graphMaxLayer+"层的结点是空的！！！");
				graphMaxLayer--;
				continue;
			}else{
				vertexArraym=vertexlist.toArray();
				mm=vertexArraym.length;
			}
		for(int j=0;j<mm;j++){
			upperObjects=graph.getNoInVisibleUpper(vertexArraym[j],graph);
			lowerObjects=graph.getVisibleLower(vertexArraym[j], graph);
			overlays=graphComponent.getCellOverlays(vertexArraym[j]);
			if((((mxCell)vertexArraym[j]).prunable==1)&&(lowerObjects==null||lowerObjects.length==0)&&(upperObjects!=null)&&(upperObjects.length!=0&&(overlays==null||overlays.length==0))){
				overlays=null;
				
				System.out.println("开始剪枝啦");
				if(upperObjects.length==1){
					System.out.println(((mxCell)vertexArraym[j]).getValue());
					bEdge=graph.getEdgesBetweenCells(upperObjects[0], vertexArraym[j]);
					((mxCell)upperObjects[0]).prunable=0;	
					if(bEdge==null){
						System.out.println("该节点与其父节点的连接线条数为0");
					}
					if(bEdge.length!=0){
						System.out.println(bEdge.length);
						for(int bn=0;bn<bEdge.length;bn++){
							((mxCell)bEdge[bn]).setVisible(false);
						}
					}
					
					graph.copeEdges(upperObjects[0], vertexArraym[j], graph);
					((mxCell)vertexArraym[j]).setVisible(false);//convert delete to unvisible
					issubhided=true;
					pruned=true;
				}
				else{
					for(int k=0;k<upperObjects.length;k++){
						bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[k], vertexArraym[j], false);
						if(bEdge.length!=0){
							for(int bnum=0;bnum<bEdge.length;bnum++){
							((mxCell)bEdge[bnum]).setVisible(false);//convert delete to unvisible
							}
						}
						((mxCell)upperObjects[k]).prunable=0;
					}
					conedges=mxGraphModel.getEdges(model, vertexArraym[j]);
					for(int i=0;i<conedges.length;i++){
						edge=(mxCell)conedges[i];
						if(edge.isVisible()){
						if(edge.getTarget()==vertexArraym[j]){
							vertex=edge.getSource();
							for(int p=0;p<upperObjects.length;p++){
								addedvertexCell=(mxCell) graph.insertNoEdge(edge.getParent(), null, edge.getValue(), vertex, upperObjects[p],edge.getStyle());
							}
							edge.setVisible(false);
						}
						else 
						if(edge.getSource()==vertexArraym[j]){
							vertex=edge.getTarget();
							for(int p=0;p<upperObjects.length;p++){
								addedvertexCell=(mxCell) graph.insertNoEdge(edge.getParent(), null, edge.getValue(), upperObjects[p],vertex,edge.getStyle());
							}
							edge.setVisible(false);
						}
					}
					}
					for(int k=0;k<(upperObjects.length-1);k++){
						if(graph.vertexsConnectionedges(upperObjects[k], upperObjects[k+1])==null){
							addedvertexCell=(mxCell) graph.insertNoEdge(((mxCell)vertexArraym[j]).getParent(), null, "相关", upperObjects[k], upperObjects[k+1],null);
						}
					}
					((mxCell)vertexArraym[j]).setVisible(false);//convert delete to unvisible
					issubhided=true;
					pruned=true;
				}
				System.out.println("被剪枝的真假为"+issubhided);
			}
		}//隐藏下位
		
		if(issubhided){
			System.out.println("已经剪枝成功了");
			continue;
//			break;
			} //如果隐藏下位成功则完成一次压缩
		else{
			System.out.println("开始上位转移了");
			istransupered=false;
			edgeArray=graph.getChildEdges(parent);
			n=edgeArray.length;
			for(int j=0;j<n;j++){
				edge=(mxCell)edgeArray[j];
				Object value = model.getValue(edge);
				if(edge.isVisible()){
				   if((value!=null)&&(value.equals("包含")||value.equals("支持")||value.equals("是工具")||value.equals("相关"))){
					
					Object sub=graph.getTarget(edge); 
					Object source=graph.getSource(edge);
					Object[] upperObject=graph.getUpper(sub,graph);
					lowerObjects=graph.getVisibleLower(sub, graph);
					Object parentObject=edge.getParent();
					
					if(upperObject.length!=0&&source!=null&&(lowerObjects==null||lowerObjects.length==0)){
						for(int i=0;i<upperObject.length;i++){
							if(!(upperObject[i]==source)&&((mxCell)upperObject[i]).isVisible()){
								if((graph.vertexsConnectionedges(upperObject[i], source))==null){
									addedvertexCell=(mxCell) graph.insertNoEdge(parentObject, null, "相关", source, upperObject[i],null);
								}
								mark++;
							}
						}
						if(mark>0){
							System.out.println("插入的上位转移边数位"+mark);
							edge.setVisible(false);
							mark=0;
							istransupered=true;
							pruned=true;
						}
					}
				}
				   }
			}//遍历最外层结点，并上位转移
			if(istransupered){
				continue;
				}
			else if(graphMaxLayer>0){
				graphMaxLayer--;
				continue;
			}//如果没有上位转移成功，则graphMaxLayer--，继续尝试下一层的剪枝
			else{
				prunedall=true;
				createnewnum=0;
				vertexArray=graph.getChildVertices(parent);
				m=vertexArray.length;
				for(int j=0;j<m;j++){
					vertex=vertexArray[j];
					iscreatenew=graph.CreateNewNum(vertex, true, visitor, null, visited, graph,graphComponent);
					if(iscreatenew){
						createnewnum++;
					}
				}//尝试创建新节点
				if(createnewnum>0){
					JOptionPane.showMessageDialog(graphComponent,"已不能继续剪枝！但图文件中可创建新节点");
					break;}
				else{
					JOptionPane.showMessageDialog(graphComponent,mxResources
							.get("connotprunedonce"));
					break;
				}//不能继续剪枝
			}
		}
	

	}
	if(!pruned){
		if(!graph.prunedLayer.isEmpty()){
			graph.prunedLayer.removeFirst();
		}
		if(!graph.getPrunedLayCell().isEmpty()){
			graph.getPrunedLayCell().remove(graph.getPrunedLayCell().size()-1);
		}
	}//如果此次压缩没有对图文件做任何操作，则图文件中的操作记录数组中第一位出栈
	graph.refresh();
	}
	catch (Exception ep) {

		ep.printStackTrace();
		// TODO: handle exception
	}
	finally{
		model.endUpdate();
	}
}

}

}
//	public static class PruneAllAction extends AbstractAction{
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 8095949711677712569L;
//		protected Object[] edgeArray;
//		public PruneAllAction(String name){
//			super(name);
//		}
//public void actionPerformed(ActionEvent e){
//	System.out.println("开始一次压缩！");
//	Object sourcecomponent=e.getSource();
//	if(sourcecomponent instanceof mxGraphComponent){
//    mxICellOverlay[] overlays;
//	Object[] edgeArray;
//	Object[] vertexArray;
//	Object[] upperObjects;
//	Object[] lowerObjects;
//	Object[] bEdge;
//	Object[] conedges;
//	int prunednumvar;
//	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
//	mxGraph graph=graphComponent.getGraph();
//	graph.setLabelsVisible(true);
//	mxIGraphModel model=graph.getModel();
//	Object  parent=graph.getDefaultParent();
//	// Finds the relevant edges for the layout
//	//source of an edge
//	boolean issubhided=false;
//	mxCell edge;
//	//boolean ispruned=false;
//	boolean istransupered=false;
//	boolean iscreatenew=false;
//	int createnewnum=0;
//	int mark=0;
//	//int prunednum=0;
//	boolean prunedall=false;
//	while(!prunedall){
//	issubhided=false;
//	Object[] edges=graph.getChildEdges(parent);
//	Object[] vertexs=graph.getChildVertices(parent);
//	List<Object> tmp = new ArrayList<Object>(edges.length);
//
//	List<Object> vmp = new ArrayList<Object>(vertexs.length);
//	Set<Object> visited=new HashSet<Object>();
//	Object vertex;
//	mxCell addedvertexCell;
//	mxCellVisitor visitor=graph.new mxCellVisitor();
//	
//	for (int i = 0; i < edges.length; i++)
//	{					
//			tmp.add(edges[i]);					
//	}
//
//	edgeArray = tmp.toArray();
//	
//	int n=edgeArray.length;
//	
//	for (int i = 0; i < vertexs.length; i++)
//	{					
//			vmp.add(vertexs[i]);					
//	}
//
//	vertexArray = vmp.toArray();
//	
//	int m=vertexArray.length;
//	boolean pruned=false;
//	model.beginUpdate();
//	try{
//		
//	if(graph.prunedLayer.isEmpty()){
//		graph.prunedLayer.addFirst(1);
//		prunednumvar=1;
//	}
//	else{
//		prunednumvar=graph.prunedLayer.getFirst();
//		prunednumvar++;
//		graph.prunedLayer.addFirst(prunednumvar);
//	}
//	
//	for(int j=0;j<n;j++)
//	{ 
//	Object value = model.getValue(edgeArray[j]);
//
//	if(value.equals("等价于")){
//
//		Object source=graph.getSource((mxCell)edgeArray[j]);
//		Object target=graph.getTarget((mxCell)edgeArray[j]);
//
//		overlays=graphComponent.getCellOverlays(target);
//		if(overlays!=null&&overlays.length>0){
//
//			overlays=null;
//			//model.remove(edgeArray[j]);
//			((mxCell)edgeArray[j]).prunednum=prunednumvar;
//			((mxCell)edgeArray[j]).setVisible(false);
//			
//	        //graph.convertEdges(target, source);
//	        graph.copeEdges(target, source, graph);//cope and set unvisible
//       	    //model.remove(source);
//       	    ((mxCell)source).setPrunednum(prunednumvar);
//       	    ((mxCell)source).setVisible(false);
//       	    pruned=true;
//		}
//		else{
//		//remove edges of target to source
//		//model.remove(edgeArray[j]);
//		((mxCell)edgeArray[j]).prunednum=prunednumvar;
//		((mxCell)edgeArray[j]).setVisible(false);
//        //graph.convertEdges(source, target);
//        graph.copeEdges(source, target, graph);
//		//model.remove(target);
//        ((mxCell)target).setPrunednum(prunednumvar);
//   	    ((mxCell)target).setVisible(false);
//   	    pruned=true;
//		}
//
//        
//		}
//		
//	}//预处理“等价合并”
//	for(int k=0;k<n;k++){
//		Object value = model.getValue(edgeArray[k]);
//		if(value.equals("并列")||value.equals("是前提")){
//			//model.remove(edgeArray[k]);
//			((mxCell)edgeArray[k]).setPrunednum(prunednumvar);
//			((mxCell)edgeArray[k]).setVisible(false);
//			pruned=true;
//			graph.refresh();
//		}
//	}//预处理“关系隐藏”
//	for(int j=0;j<n;j++)
//	{ 
//	 edge=(mxCell)edgeArray[j];
//	 
//	Object value = model.getValue(edge);
//
//	if(value.equals("具有特征")){
////		Object source=graph.getSource(edge);
////		Object target=graph.getTarget(edge);
////		edge.setTarget((mxCell)source);
////        edge.setSource((mxCell)target);
////        edge.setValue("包含");
//        edge.setPrunednum(prunednumvar);
//        addedvertexCell=(mxCell) graph.insertEdge(parent, null,"包含", edge.getTarget(), edge.getSource(), edge.getStyle());
//        addedvertexCell.setPrunednum(prunednumvar);
//        pruned=true;
//        edge.setVisible(false);
//		}
//	if(value.equals("定义")){
//		//edge.setValue("包含");
//		edge.setPrunednum(prunednumvar);
//		addedvertexCell=(mxCell) graph.insertEdge(parent, null,"包含", edge.getSource(), edge.getTarget(), edge.getStyle());
//        addedvertexCell.setPrunednum(prunednumvar);
//        pruned=true;
//	}
//	if(value.equals("是一种")){
//		Object sourceObject=edge.getSource();
//		Object targetObject=edge.getTarget();
//		if(!(mxUtils.getString(graph.getCellStyle((mxCell)sourceObject), mxConstants.STYLE_SHAPE)
//				.equals(mxUtils.getString(graph.getCellStyle((mxCell)targetObject), mxConstants.STYLE_SHAPE)))){
//			//edge.setValue("相关");
//			edge.setPrunednum(prunednumvar);
//			addedvertexCell=(mxCell) graph.insertEdge(parent, null, "相关", edge.getSource(), edge.getTarget(), edge.getStyle());
//			addedvertexCell.setPrunednum(prunednumvar);
//			pruned=true;
//		}
//	}
//	}//预处理“关系更名”
//	while(!issubhided){
//		issubhided=false;
//		vertexArray=graph.getChildVertices(parent);
//		m=vertexArray.length;
//		for(int num=0;num<m;num++){
//			((mxCell)vertexArray[num]).prunable=1;
//		}
//		for(int pn=0;pn<m;pn++){
//			vertex=vertexArray[pn];
//			lowerObjects=graph.getVisibleLower(vertex, graph);
//			if(!(lowerObjects==null||lowerObjects.length==0)){
//				((mxCell)vertex).prunable=0;
//				Object[] samevertex=graph.getSimilarVertexs(vertex);
//				for(int sn=0;sn<samevertex.length;sn++){
//					((mxCell)samevertex[sn]).prunable=0;
//					//System.out.println("此相似节点无法剪切");
//				}
//			}
//		}
//		for(int j=0;j<m;j++){
//			upperObjects=graph.getNoInVisibleUpper(vertexArray[j],graph);
//			lowerObjects=graph.getVisibleLower(vertexArray[j], graph);
//			overlays=graphComponent.getCellOverlays(vertexArray[j]);
//
//			if((((mxCell)vertexArray[j]).prunable==1)&&(lowerObjects==null||lowerObjects.length==0)&&(upperObjects!=null)&&(upperObjects.length!=0&&(overlays==null||overlays.length==0))){
//				overlays=null;
//				
//				System.out.println("开始剪枝啦");
//				if(upperObjects.length==1){
//					bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[0], vertexArray[j], false);
//					((mxCell)upperObjects[0]).prunable=0;
//					if(bEdge.length!=0){
//						for(int bn=0;bn<bEdge.length;bn++){
//							((mxCell)bEdge[bn]).setVisible(false);
//							((mxCell)bEdge[bn]).setPrunednum(prunednumvar);
//						}
//					}
//					//graph.convertEdges(upperObjects[0], vertexArray[j]);
//					graph.copeEdges(upperObjects[0], vertexArray[j], graph);
//					//model.remove(vertexArray[j]);//delete
//					((mxCell)vertexArray[j]).setVisible(false);//convert delete to unvisible
//					((mxCell)vertexArray[j]).setPrunednum(prunednumvar);
//					issubhided=true;
//					pruned=true;
//				}
//				else{
//					for(int k=0;k<upperObjects.length;k++){
//						bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[k], vertexArray[j], false);
//						if(bEdge.length!=0){
//							for(int bnum=0;bnum<bEdge.length;bnum++){
//							//model.remove(bEdge[0]);//delete
//							((mxCell)bEdge[bnum]).setPrunednum(prunednumvar);
//							((mxCell)bEdge[bnum]).setVisible(false);//convert delete to unvisible
//							}
//						}
//						((mxCell)upperObjects[k]).prunable=0;
//					}
//					conedges=mxGraphModel.getEdges(model, vertexArray[j]);
//					for(int i=0;i<conedges.length;i++){
//						edge=(mxCell)conedges[i];
//						if(edge.isVisible()){
//						if(edge.getTarget()==vertexArray[j]){
//							vertex=edge.getSource();
//							for(int p=0;p<upperObjects.length;p++){
//								addedvertexCell=(mxCell) graph.insertEdge(edge.getParent(), null, edge.getValue(), vertex, upperObjects[p],edge.getStyle());
//								addedvertexCell.setPrunednum(prunednumvar);
//							}
//							//model.remove(edge);
//							edge.setPrunednum(prunednumvar);
//							edge.setVisible(false);
//						}
//						else 
//						if(edge.getSource()==vertexArray[j]){
//							vertex=edge.getTarget();
//							for(int p=0;p<upperObjects.length;p++){
//								addedvertexCell=(mxCell) graph.insertEdge(edge.getParent(), null, edge.getValue(), upperObjects[p],vertex,edge.getStyle());
//								addedvertexCell.setPrunednum(prunednumvar);
//							}
//							//model.remove(edge);
//							edge.setPrunednum(prunednumvar);
//							edge.setVisible(false);
//						}
//					}
//					}
//					for(int k=0;k<(upperObjects.length-1);k++){
//						if(graph.vertexsConnectionedges(upperObjects[k], upperObjects[k+1])==null){
//							addedvertexCell=(mxCell) graph.insertEdge(((mxCell)vertexArray[j]).getParent(), null, "相关", upperObjects[k], upperObjects[k+1]);
//							addedvertexCell.setPrunednum(prunednumvar);
//						}
//					}
//					//model.remove(vertexArray[j]);
//					((mxCell)vertexArray[j]).setVisible(false);//convert delete to unvisible
//					((mxCell)vertexArray[j]).setPrunednum(prunednumvar);
//					issubhided=true;
//					pruned=true;
//				}
//			}
//		}//隐藏下位
//		if(issubhided){
////			JOptionPane.showMessageDialog(graphComponent,mxResources
////					.get("prunedonce"));
//			break;} //如果隐藏下位成功则完成一次压缩
//		else{
//			istransupered=false;
//			edgeArray=graph.getChildEdges(parent);
//			n=edgeArray.length;
//			for(int j=0;j<n;j++){
//				edge=(mxCell)edgeArray[j];
//				Object value = model.getValue(edge);
//				if(edge.isVisible()){
//				   if(value.equals("包含")||value.equals("支持")||value.equals("是工具")||value.equals("相关")){
//					
//					Object sub=graph.getTarget(edge); 
//					Object source=graph.getSource(edge);
//					Object[] upperObject=graph.getUpper(sub,graph);
//					lowerObjects=graph.getVisibleLower(sub, graph);
//					Object parentObject=edge.getParent();
//					//edge.setValue("相关");
//					
//					if(upperObject.length!=0&&source!=null&&(lowerObjects==null||lowerObjects.length==0)){
//						for(int i=0;i<upperObject.length;i++){
//							//vervalue=(String)(model.getValue(upperObject[i]));
//							if(!(upperObject[i]==source)&&((mxCell)upperObject[i]).isVisible()){
//								if((graph.vertexsConnectionedges(upperObject[i], source))==null){
//									addedvertexCell=(mxCell) graph.insertEdge(parentObject, null, "相关", source, upperObject[i]);
//									addedvertexCell.setPrunednum(prunednumvar);
//								}
//								mark++;
//							}
//						}
//						//System.out.println((String)(model.getValue(upperObject)));
//						if(mark>0){
//							//model.remove(edge);
//							edge.setPrunednum(prunednumvar);
//							edge.setVisible(false);
//							mark=0;
//							istransupered=true;
//							pruned=true;
//						}
//						
//						//graph.addEdge(edge, parentObject, source, upperObject, null);
//					}
//				}
//				   }
//			}//上位转移
//			if(istransupered){continue;}
//			else{
//				createnewnum=0;
//				vertexArray=graph.getChildVertices(parent);
//				m=vertexArray.length;
//				for(int j=0;j<m;j++){
//					vertex=vertexArray[j];
//					//System.out.println("the value of the vertex is"+(String)(((mxCell)vertex).getValue()));
//					iscreatenew=graph.CreateNewNum(vertex, true, visitor, null, visited, graph,graphComponent);
//					if(iscreatenew){
//						createnewnum++;
//					}
//				}//尝试创建新节点
//				if(createnewnum>0){
//					//JOptionPane.showMessageDialog(graphComponent,"已不能继续剪枝！但图文件中有"+createnewnum+"处可创建新节点");
//					JOptionPane.showMessageDialog(graphComponent,"已完成全部剪枝！但图文件中可创建新节点！");
//					prunedall=true;
//					break;}
//				else{
////					JOptionPane.showMessageDialog(graphComponent,mxResources
////							.get("connotprunedonce"));
//					JOptionPane.showMessageDialog(graphComponent,"已完成全部剪枝！");
//					prunedall=true;
//					//System.out.println("hahha you cannot prune the graph again");
//					break;
//				}//不能继续剪枝
//			}
//		}
//	}
//	graph.refresh();
//	if(!pruned){
//		if(!graph.prunedLayer.isEmpty()){
//			graph.prunedLayer.removeFirst();
//		}
//		
//	}//如果此次压缩没有对图文件做任何操作，则图文件中的操作记录数组中第一位出栈
//	}
//	catch (Exception ep) {
//
//		ep.printStackTrace();
//		// TODO: handle exception
//	}
//	finally{
//		model.endUpdate();
//	}
//	}
//}
//
//}

//}
	/**
	 * 
	 * to prune the graph once
	 * */
	public static class UnPruneOnceAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public UnPruneOnceAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent e){
			Object sourcecomponent=e.getSource();
			if(sourcecomponent instanceof mxGraphComponent){
				mxICellOverlay[] overlays;
				Object[] edgeArray;
				Object[] vertexArray;
				Object[] upperObjects;
				Object[] lowerObjects;
				Object[] bEdge;
				Object[] conedges;
				int prunednumvar;
				mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
				mxGraph graph=graphComponent.getGraph();
				graph.setLabelsVisible(true);
				mxIGraphModel model=graph.getModel();
				Object  parent=graph.getDefaultParent();
				// Finds the relevant edges for the layout
				//source of an edge
				boolean issubhided=false;
				mxCell edge;
				//boolean ispruned=false;
				boolean istransupered=false;
				boolean iscreatenew=false;
				int createnewnum=0;
				int mark=0;
				//int prunednum=0;
				boolean prunedall=false;
				issubhided=false;
				Object[] edges=graph.getAllChildCells(parent, false, true);
				Object[] vertexs=graph.getAllChildCells(parent, true, false);
				List<Object> tmp = new ArrayList<Object>(edges.length);
				List<Object> vmp = new ArrayList<Object>(vertexs.length);
				Set<Object> visited=new HashSet<Object>();
				Object vertex;
				mxCell addedvertexCell;
				mxCellVisitor visitor=graph.new mxCellVisitor();
				
				for (int i = 0; i < edges.length; i++)
				{					
						tmp.add(edges[i]);					
				}

				edgeArray = tmp.toArray();
				
				int n=edgeArray.length;
				
				for (int i = 0; i < vertexs.length; i++)
				{					
						vmp.add(vertexs[i]);					
				}

				vertexArray = vmp.toArray();
				
				int m=vertexArray.length;
				boolean pruned=false;
				model.beginUpdate();
				try{
					
				if(graph.getPrunedLayCell().isEmpty()){
					JOptionPane.showMessageDialog(graphComponent,"已完成恢复！");
//					graph.prunedLayer.addFirst(1);
//					prunednumvar=1;
				}
				else{
					System.out.println("数据开始恢复");
					graph.prunedLayer.clear();
					ArrayList parray=graph.getPrunedLayCell();
					ArrayList array=(ArrayList) parray.get(parray.size()-1);
					for(int ed=0;ed<edges.length;ed++){
						((mxCell)edges[ed]).setVisible(false);
					}
					for(int ve=0;ve<vertexs.length;ve++){
						((mxCell)vertexs[ve]).setVisible(false);
					}
					for(int a=0;a<array.size();a++){
						((mxCell)(array.get(a))).setVisible(true);
					}
					parray.remove(parray.size()-1);
				}
				}catch (Exception ep) {
					ep.printStackTrace();
					// TODO: handle exception
				}finally{
					model.endUpdate();
					graph.refresh();
				}
			
		}
}}
	/**
	 * 
	 * to unprune the graph all
	 * */
	public static class UnPruneAllAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public UnPruneAllAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent e){
			Object sourcecomponent=e.getSource();
			if(sourcecomponent instanceof mxGraphComponent){
				mxICellOverlay[] overlays;
				Object[] edgeArray;
				Object[] vertexArray;
				Object[] upperObjects;
				Object[] lowerObjects;
				Object[] bEdge;
				Object[] conedges;
				int prunednumvar;
				mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
				mxGraph graph=graphComponent.getGraph();
				graph.setLabelsVisible(true);
				mxIGraphModel model=graph.getModel();
				Object  parent=graph.getDefaultParent();
				// Finds the relevant edges for the layout
				//source of an edge
				boolean issubhided=false;
				mxCell edge;
				//boolean ispruned=false;
				boolean istransupered=false;
				boolean iscreatenew=false;
				int createnewnum=0;
				int mark=0;
				//int prunednum=0;
				boolean prunedall=false;
				issubhided=false;
				//Object[] edges=graph.getChildEdges(parent);//这儿的问题，应该取所有的结点
				Object[] edges=graph.getAllChildCells(parent, false, true);
				//Object[] vertexs=graph.getChildVertices(parent);//同上
				Object[] vertexs=graph.getAllChildCells(parent, true, false);
				List<Object> tmp = new ArrayList<Object>(edges.length);
				List<Object> vmp = new ArrayList<Object>(vertexs.length);
				Set<Object> visited=new HashSet<Object>();
				Object vertex;
				mxCell addedvertexCell;
				mxCellVisitor visitor=graph.new mxCellVisitor();
				
				for (int i = 0; i < edges.length; i++)
				{					
						tmp.add(edges[i]);					
				}

				edgeArray = tmp.toArray();
				
				int n=edgeArray.length;
				
				for (int i = 0; i < vertexs.length; i++)
				{					
						vmp.add(vertexs[i]);					
				}

				vertexArray = vmp.toArray();
				
				int m=vertexArray.length;
				boolean pruned=false;
				model.beginUpdate();
				try{
					
				if(graph.getPrunedLayCell().isEmpty()){
					JOptionPane.showMessageDialog(graphComponent,"已完成恢复！");
				}
				else{
					System.out.println("数据开始恢复");
					graph.prunedLayer.clear();
					ArrayList array=graph.getPrunedLayCell().get(0);
					
					for(int ed=0;ed<edges.length;ed++){
						((mxCell)edges[ed]).setVisible(false);
					}
					for(int ve=0;ve<vertexs.length;ve++){
						((mxCell)vertexs[ve]).setVisible(false);
					}
					for(int a=0;a<array.size();a++){
						((mxCell)(array.get(a))).setVisible(true);
					}
					graph.getPrunedLayCell().clear();
				}
				}catch (Exception ep) {
					ep.printStackTrace();
					// TODO: handle exception
				}finally{
					model.endUpdate();
					graph.refresh();
				}
			
		}
}}
	
	/**
	 * 
	 * to prune the graph once
	 * */
	public static class PruneAllSecondAction extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8095949711677712569L;
		protected Object[] edgeArray;
		public PruneAllSecondAction(String name){
			super(name);
		}
public void actionPerformed(ActionEvent e){
	//System.out.println("激活合并！");
	Object sourcecomponent=e.getSource();
	if(sourcecomponent instanceof mxGraphComponent){
	mxICellOverlay[] overlays;
	Object[] edgeArray;
	Object[] vertexArray;
	Object[] upperObjects;
	Object[] bEdge;
	Object[] conedges;
	mxGraphComponent graphComponent = (mxGraphComponent) sourcecomponent;
	mxGraph graph=graphComponent.getGraph();
	graph.setLabelsVisible(true);
	mxIGraphModel model=graph.getModel();
	Object  parent=graph.getDefaultParent();
	// Finds the relevant edges for the layout
	Object[] edges=graph.getChildEdges(parent);
	Object[] vertexs=graph.getChildVertices(parent);
	//source of an edge
	mxCell edge;
	boolean canpruned=true;
	boolean issubhided=false;
	boolean istransupered=false;
	boolean iscreatenew=false;
	int createnewnum=0;
	int mark=0;
	int prunednum=0;
	List<Object> tmp = new ArrayList<Object>(edges.length);

	List<Object> vmp = new ArrayList<Object>(edges.length);
	Set<Object> visited=new HashSet<Object>();
	Object vertex;
	mxCellVisitor visitor=graph.new mxCellVisitor();
	
	for (int i = 0; i < edges.length; i++)
	{					
			tmp.add(edges[i]);					
	}

	edgeArray = tmp.toArray();
	
	int n=edgeArray.length;
	
	for (int i = 0; i < vertexs.length; i++)
	{					
			vmp.add(vertexs[i]);					
	}

	vertexArray = vmp.toArray();
	
	int m=vertexArray.length;
	model.beginUpdate();
	try{
		
	for(int j=0;j<n;j++)
	{ 
	Object value = model.getValue(edgeArray[j]);
	System.out.println("it's value is "+(String)value);

	if(value.equals("等价于")){

		Object source=graph.getSource((mxCell)edgeArray[j]);
		Object target=graph.getTarget((mxCell)edgeArray[j]);

		overlays=graphComponent.getCellOverlays(target);
		if(overlays!=null&&overlays.length>0){

			overlays=null;
			model.remove(edgeArray[j]);
	        graph.convertEdges(target, source);
       	    model.remove(source);
		}
		else{
		//remove edges of target to source
		model.remove(edgeArray[j]);
        //graph.convertApartEdges(source, target, edgeArray[j]);
        graph.convertEdges(source, target);
        	 model.remove(target);
		}
		}
	}
	for(int k=0;k<n;k++){
		Object value = model.getValue(edgeArray[k]);
		if(value.equals("并列")||value.equals("是前提")){
			model.remove(edgeArray[k]);
			graph.refresh();
		}
	}
	for(int j=0;j<n;j++)
	{ 
	 edge=(mxCell)edgeArray[j];
	Object value = model.getValue(edge);
	System.out.println("it's value is "+(String)value);

	if(value.equals("具有特征")){        
		Object source=graph.getSource(edge);
		Object target=graph.getTarget(edge);
		
        
		edge.setTarget((mxCell)source);
        edge.setSource((mxCell)target);
        edge.setValue("包含");
		}
	if(value.equals("定义")){
		edge.setValue("包含");
	}
	if(value.equals("是一种")){
		Object sourceObject=edge.getSource();
		Object targetObject=edge.getTarget();
		if(!(mxUtils.getString(graph.getCellStyle((mxCell)sourceObject), mxConstants.STYLE_SHAPE)
				.equals(mxUtils.getString(graph.getCellStyle((mxCell)targetObject), mxConstants.STYLE_SHAPE)))){
			edge.setValue("相关");
		}
	}
	}
//	while(canpruned){
		//canpruned=true;
	while(true){
		issubhided=false;
		vertexArray=graph.getChildVertices(parent);
		m=vertexArray.length;
		for(int j=0;j<m;j++){
			upperObjects=graph.getUpper(vertexArray[j],graph);
			overlays=graphComponent.getCellOverlays(vertexArray[j]);
			if((upperObjects!=null)&&(upperObjects.length!=0&&(overlays==null||overlays.length==0))){
				overlays=null;
				//System.out.println("the upper is not null");
				if(upperObjects.length==1){
					bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[0], vertexArray[j], false);
					
					if(bEdge.length!=0){
						model.remove(bEdge[0]);
					}
					graph.convertEdges(upperObjects[0], vertexArray[j]);
					model.remove(vertexArray[j]);
					issubhided=true;
				}
				else{
					for(int k=0;k<upperObjects.length;k++){
						bEdge=mxGraphModel.getEdgesBetween(model, upperObjects[k], vertexArray[j], false);
						if(bEdge.length!=0){
							model.remove(bEdge[0]);
						}		
					}
					conedges=mxGraphModel.getEdges(model, vertexArray[j]);
					for(int i=0;i<conedges.length;i++){
						edge=(mxCell)conedges[i];
						if(edge.getTarget()==vertexArray[j]){
							vertex=edge.getSource();
							for(int p=0;p<upperObjects.length;p++){
								graph.insertEdge(edge.getParent(), null, edge.getValue(), vertex, upperObjects[p]);
							}
							model.remove(edge);
						}
						else 
						if(edge.getSource()==vertexArray[j]){
							vertex=edge.getTarget();
							for(int p=0;p<upperObjects.length;p++){
								graph.insertEdge(edge.getParent(), null, edge.getValue(), upperObjects[p],vertex );
							}
							model.remove(edge);
						}
					}
					for(int k=0;k<(upperObjects.length-1);k++){
						if(graph.vertexsConnectionedges(upperObjects[k], upperObjects[k+1])==null){
							graph.insertEdge(((mxCell)vertexArray[j]).getParent(), null, "相关", upperObjects[k], upperObjects[k+1]);
						}
					}
					model.remove(vertexArray[j]);
					issubhided=true;
				}
			}
		}
		if(issubhided){continue;}
		else{
			istransupered=false;
			edgeArray=graph.getChildEdges(parent);
			n=edgeArray.length;
			for(int j=0;j<n;j++){
				edge=(mxCell)edgeArray[j];
				Object value = model.getValue(edge);
				if(value.equals("包含")||value.equals("支持")||value.equals("是工具")||value.equals("相关")){
					
					Object sub=graph.getTarget(edge); 
					Object source=graph.getSource(edge);
					Object[] upperObject=graph.getUpper(sub,graph);
					Object parentObject=edge.getParent();
					//edge.setValue("相关");
					
					if(upperObject.length!=0&&source!=null){
						for(int i=0;i<upperObject.length;i++){
							//vervalue=(String)(model.getValue(upperObject[i]));
							if(!(upperObject[i]==source)){
								if((graph.vertexsConnectionedges(upperObject[i], source))==null){
									graph.insertEdge(parentObject, null, "相关", source, upperObject[i]);
								}	
								mark++;
							}
							
						}
						//System.out.println((String)(model.getValue(upperObject)));
						if(mark>0){
							model.remove(edge);
							mark=0;
							istransupered=true;
						}
						
						//graph.addEdge(edge, parentObject, source, upperObject, null);
					}
				}
			}
			if(istransupered){continue;}
			else{
				createnewnum=0;
				vertexArray=graph.getChildVertices(parent);
				m=vertexArray.length;
				for(int j=0;j<m;j++){
					vertex=vertexArray[j];
					iscreatenew=graph.CreateNewNum(vertex, true, visitor, null, visited, graph,graphComponent);
					if(iscreatenew){
						createnewnum++;
					}
				}
				if(createnewnum>0){
					JOptionPane.showMessageDialog(graphComponent,"已不能继续剪枝！但图文件中有"+createnewnum+"处可创建新节点");
					break;
				}
				else{
					canpruned=false;
					System.out.println("you have end the prune");
					JOptionPane.showMessageDialog(graphComponent,mxResources
							.get("prunedall"));
					break;
				}
			}
		}
	}
//	if(!canpruned){
//		break;
//	}
//	}
	graph.refresh();
	}
	catch (Exception ep) {

		ep.printStackTrace();
		// TODO: handle exception
	}
	finally{
		model.endUpdate();
	}
}

}

}
	/**
	 * 
	 */
	public static class SelectAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6501585024845668187L;

		/**
		 * 
		 * @param name
		 */
		public SelectAction(String name)
		{
			super(name);
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = getGraph(e);

			if (graph != null)
			{
				String name = getValue(Action.NAME).toString();

				if (name.equalsIgnoreCase("selectAll"))
				{
					graph.selectAll();
				}
				else if (name.equalsIgnoreCase("selectNone"))
				{
					graph.clearSelection();
				}
				else if (name.equalsIgnoreCase("selectNext"))
				{
					graph.selectNextCell();
				}
				else if (name.equalsIgnoreCase("selectPrevious"))
				{
					graph.selectPreviousCell();
				}
				else if (name.equalsIgnoreCase("selectParent"))
				{
					graph.selectParentCell();
				}
				else if (name.equalsIgnoreCase("vertices"))
				{
					graph.selectVertices();
				}
				else if (name.equalsIgnoreCase("edges"))
				{
					graph.selectEdges();
				}
				else
				{
					graph.selectChildCell();
				}
			}
		}

	}
	public static class ExportAction extends AbstractAction{

		public ExportAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object sourcecomponent=e.getSource();
			JFileChooser jfc = new javax.swing.JFileChooser();
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileFilter(new FileFilter() {
				
				@Override
				public String getDescription() {
					// TODO Auto-generated method stub
					return "excel 文件 (*.xls)";
				}
				
				@Override
				public boolean accept(File f) {
					// TODO Auto-generated method stub
					if (f.isDirectory()) return true;
				    String fileName = f.getName();
				    if (fileName.toUpperCase().endsWith(".xls".toUpperCase())) return true;
				    return false;
				}
			});
			jfc.showSaveDialog((JComponent)sourcecomponent);
			File file=jfc.getSelectedFile();
			File newFile = null;
			 if(file!=null){
			  if (file.getAbsolutePath().toUpperCase().endsWith(".xls".toUpperCase())) {
			    // 如果文件是以选定扩展名结束的，则使用原名
			    newFile = file;
			  } else {
			    // 否则加上选定的扩展名
			    newFile = new File(file.getAbsolutePath() + ".xls");
			  }
			 }
			String root  = System.getProperty("user.dir");
			if(!root.contains("CDv1.11")){
				root=root+"/CDv1.11";
			}
			File file2=new File(root+"/src/com/mxgraph/examples/swing/resources/modelingrules.xls");
			try {
				mxUtils.CopyFile(file2, newFile);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(newFile.exists()){
				JOptionPane.showMessageDialog(null, "文件导出成功");
			}
		}
		
	}
	public static class ImportAction extends AbstractAction{

		public ImportAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object sourcecomponent=e.getSource();
			JFileChooser jfc = new javax.swing.JFileChooser();
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileFilter(new FileFilter() {
				
				@Override
				public String getDescription() {
					// TODO Auto-generated method stub
					return "excel 文件 (*.xls)";
					//return ".xls";
				}
				
				@Override
				public boolean accept(File f) {
					// TODO Auto-generated method stub
					if (f.isDirectory()) return true;
				    String fileName = f.getName();
				    if (fileName.toUpperCase().endsWith(".xls".toUpperCase())) return true;
				    return false;

					//return f.getName().endsWith(".xls");
				}
			});
			jfc.showOpenDialog((JComponent)sourcecomponent);
			File file=jfc.getSelectedFile();
			
			File newFile = null;
			if(file!=null){
			  if (file.getAbsolutePath().toUpperCase().endsWith(".xls".toUpperCase())) {
			    // 如果文件是以选定扩展名结束的，则使用原名
			    newFile = file;
			  } else {
			    // 否则加上选定的扩展名
			    newFile = new File(file.getAbsolutePath() + ".xls");
			  }
			}
			String root  = System.getProperty("user.dir");
			if(!root.contains("CDv1.11")){
				root=root+"/CDv1.11";
			}
			try {
				mxUtils.ExcelToShapeProperties(file.getAbsolutePath(),root+"/src/com/mxgraph/examples/swing/resources/shape_zh-CN.properties");
				mxUtils.ExcelToRelationProperties(file.getAbsolutePath(), root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties");
				//				mxUtils.CopyFile(newFile, file2);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	public static class RecoveryAction extends AbstractAction{

		public RecoveryAction(String name){
			super(name);
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			String root  = System.getProperty("user.dir");
			if(!root.contains("CDv1.11")){
				root=root+"/CDv1.11";
			}
			try {
				mxUtils.ExcelToShapeProperties(root+"/src/com/mxgraph/examples/swing/resources/modelingrules.xls",root+"/src/com/mxgraph/examples/swing/resources/shape_zh-CN.properties");
				mxUtils.ExcelToRelationProperties(root+"/src/com/mxgraph/examples/swing/resources/modelingrules.xls", root+"/src/com/mxgraph/examples/swing/resources/relation_zh-CN.properties");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
}

