package com.mxgraph.examples.swing.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import com.mxgraph.examples.swing.CDSoftWare;
import com.mxgraph.examples.swing.CDSoftWare.CustomGraph;
import com.mxgraph.examples.swing.CDSoftWare.CustomGraphComponent;
import com.mxgraph.examples.swing.editor.EditorActions.AlignCellsAction;
import com.mxgraph.examples.swing.editor.EditorActions.AutosizeAction;
import com.mxgraph.examples.swing.editor.EditorActions.BackgroundAction;
import com.mxgraph.examples.swing.editor.EditorActions.BackgroundImageAction;
import com.mxgraph.examples.swing.editor.EditorActions.ColorAction;
import com.mxgraph.examples.swing.editor.EditorActions.ExitAction;
import com.mxgraph.examples.swing.editor.EditorActions.GridColorAction;
import com.mxgraph.examples.swing.editor.EditorActions.GridStyleAction;
import com.mxgraph.examples.swing.editor.EditorActions.HistoryAction;
import com.mxgraph.examples.swing.editor.EditorActions.KeyValueAction;
//import com.mxgraph.examples.swing.editor.EditorActions.NewAction;
import com.mxgraph.examples.swing.editor.EditorActions.NewAction;
import com.mxgraph.examples.swing.editor.EditorActions.NewCDAction;
import com.mxgraph.examples.swing.editor.EditorActions.OpenAction;
import com.mxgraph.examples.swing.editor.EditorActions.OpenCDAction;
import com.mxgraph.examples.swing.editor.EditorActions.PageBackgroundAction;
import com.mxgraph.examples.swing.editor.EditorActions.PageSetupAction;
import com.mxgraph.examples.swing.editor.EditorActions.PrintAction;
import com.mxgraph.examples.swing.editor.EditorActions.PromptPropertyAction;
import com.mxgraph.examples.swing.editor.EditorActions.PromptValueAction;
import com.mxgraph.examples.swing.editor.EditorActions.SaveAction;
import com.mxgraph.examples.swing.editor.EditorActions.SaveCDAction;
import com.mxgraph.examples.swing.editor.EditorActions.ScaleAction;
import com.mxgraph.examples.swing.editor.EditorActions.SelectShortestPathAction;
import com.mxgraph.examples.swing.editor.EditorActions.SelectSpanningTreeAction;
import com.mxgraph.examples.swing.editor.EditorActions.SetLabelPositionAction;
import com.mxgraph.examples.swing.editor.EditorActions.SetStyleAction;
import com.mxgraph.examples.swing.editor.EditorActions.StyleAction;
import com.mxgraph.examples.swing.editor.EditorActions.StylesheetAction;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleAction;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleConnectModeAction;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleCreateTargetItem;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleDirtyAction;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleGridItem;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleOutlineItem;
import com.mxgraph.examples.swing.editor.EditorActions.TogglePropertyItem;
import com.mxgraph.examples.swing.editor.EditorActions.ToggleRulersItem;
import com.mxgraph.examples.swing.editor.EditorActions.WarningAction;
import com.mxgraph.examples.swing.editor.EditorActions.UnWarningAction;
import com.mxgraph.examples.swing.editor.EditorActions.ZoomPolicyAction;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
/**
 * 添加menubar
 */
public class EditorMenuBar extends JMenuBar
{

	/**
	 * 
	 */
	/**
	 * Adds required resources for i18n
	 */
//	static
//	{
//		mxResources.add("com/mxgraph/examples/swing/resources/modelingrules.xls");
//		//mxResources.add("com/mxgraph/examples/swing/resources/editorTestuft8");
//	}
	private static final long serialVersionUID = 4060203894740766714L;

	@SuppressWarnings("serial")
	public EditorMenuBar(final CDSoftWare editor)
	{
		final BasicGraphEditor basicGraphEditor=editor.getBasicGraphEditor();
		final mxGraphComponent graphComponent= basicGraphEditor.getGraphComponent();
		//final mxGraphComponent graphComponent = editor.getGraphComponent();
		final mxGraph graph = graphComponent.getGraph();
		JMenu menu = null;
		JMenu submenu = null;

		// Creates the file menu
		menu = add(new JMenu(mxResources.get("file")));

		menu.add(editor.bind(mxResources.get("new"), new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				editor.createFrame("new File"+editor.counters++, new CustomGraphComponent(new CustomGraph()));
				editor.setCDmodified(true);
			}
		},
				"/com/mxgraph/examples/swing/images/new.gif"));
		menu.add(editor.bind(mxResources.get("open"), new OpenAction(),
				"/com/mxgraph/examples/swing/images/open.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("save"), new SaveAction(false),
				"/com/mxgraph/examples/swing/images/save.gif"));
		menu.add(editor.bind(mxResources.get("saveAs"), new SaveAction(true),
				"/com/mxgraph/examples/swing/images/saveas.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("pageSetup"),
				new PageSetupAction(),
				"/com/mxgraph/examples/swing/images/pagesetup.gif"));
		menu.add(editor.bind(mxResources.get("printviewer"), new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		}));
		menu.add(editor.bind(mxResources.get("print"), new PrintAction(),
				"/com/mxgraph/examples/swing/images/print.gif"));

		menu.add(editor.bind(mxResources.get("property"), new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				
			}
		}));
		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("exit"), new ExitAction()));
//		menu.add(editor.bind("cellStyleTest", new AbstractAction() {
//			
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				Object v;
//				String string;
//				BasicGraphEditor basicGraphEditor=editor.getBasicGraphEditor();
//				mxGraphComponent component=basicGraphEditor.getGraphComponent();
//				mxGraph graph=component.getGraph();
//				Object[] cells=graph.getChildVertices(graph.getDefaultParent());
//				for(int i=0;i<cells.length;i++){
//					v=cells[i];
//					string=mxUtils.getString(graph.getCellStyle((mxCell)v), mxConstants.STYLE_SHAPE);
//					System.out.println(string);
//				}
//				
//			}
//		}));
		menu.add(editor.bind("cellLayerTest", new AbstractAction() {
		
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String root=System.getProperty("user.dir");
			JOptionPane.showMessageDialog(graphComponent,root);
			Class clazz = this.getClass(); 
			URLClassLoader loader = (URLClassLoader)clazz.getClassLoader(); 
			URL url = loader.getResource("relation_zh-CN.properties");
			System.out.println(url);
			//			Object v;
//			String path;
//			int layer;
//			BasicGraphEditor basicGraphEditor=editor.getBasicGraphEditor();
//			mxGraphComponent component=basicGraphEditor.getGraphComponent();
//			mxGraph graph=component.getGraph();
//			mxIGraphModel model=graph.getModel();
//			Object[] cells=graph.getChildVertices(graph.getDefaultParent());
//			Object[] edges=graph.getAllChildCells(graph.getDefaultParent(), false, true);
//			//Object[] vertexs=graph.getChildVertices(parent);//同上
//			Object[] vertexs=graph.getAllChildCells(graph.getDefaultParent(), true, false);
//			System.out.println(edges.length+"节点数是"+vertexs.length);
//			model.beginUpdate();
//			try{
//			for(int i=0;i<edges.length;i++){
//				((mxCell)edges[i]).setVisible(true);
//				System.out.println(((mxCell)edges[i]).getValue()+((mxCell)edges[i]).getId()+((mxCell)((mxCell)edges[i]).getSource()).getValue()+((mxCell)((mxCell)edges[i]).getTarget()).getValue());
//			}
//			for(int j=0;j<vertexs.length;j++){
//				((mxCell)vertexs[j]).setVisible(true);
//			}
//			}catch (Exception ep) {
//				ep.printStackTrace();
//				// TODO: handle exception
//			}finally{
//				model.endUpdate();
//				graph.refresh();
//			}
////			for(int i=0;i<cells.length;i++){
////				v=cells[i];
////				layer=graph.getCellLayer((mxCell)v);
////				//path=com.mxgraph.model.mxCellPath.create((mxCell)v);
////				System.out.println((((mxCell)v).getValue().toString())+"的层次是"+layer);
////			}
			
		}
	}));

		// Creates the edit menu
		menu = add(new JMenu(mxResources.get("edit")));

		menu.add(editor.bind(mxResources.get("undo"), new HistoryAction(true),
				"/com/mxgraph/examples/swing/images/undo.gif"));
		menu.add(editor.bind(mxResources.get("redo"), new HistoryAction(false),
				"/com/mxgraph/examples/swing/images/redo.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("cut"), TransferHandler
				.getCutAction(), "/com/mxgraph/examples/swing/images/cut.gif"));
		menu.add(editor
				.bind(mxResources.get("copy"), TransferHandler.getCopyAction(),
						"/com/mxgraph/examples/swing/images/copy.gif"));
		menu.add(editor.bind(mxResources.get("paste"), TransferHandler
				.getPasteAction(),
				"/com/mxgraph/examples/swing/images/paste.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("delete"), mxGraphActions
				.getDeleteAction(),
				"/com/mxgraph/examples/swing/images/delete.gif"));

		menu.addSeparator();
        menu.add(editor.bind(mxResources.get("findreplace"), new AbstractAction() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}));



		// Creates the format menu
		menu = add(new JMenu(mxResources.get("format")));

		submenu=(JMenu) menu.add(new JMenu(mxResources.get("cellset")));
		submenu=(JMenu) menu.add(new JMenu(mxResources.get("lineset")));
		submenu.add(editor.bind(mxResources.get("linecolor"), new ColorAction(
				"Linecolor", mxConstants.STYLE_STROKECOLOR),
				"/com/mxgraph/examples/swing/images/linecolor.gif"));
		submenu.add(editor.bind(mxResources.get("linewidth"),
				new PromptValueAction(mxConstants.STYLE_STROKEWIDTH,
						"Linewidth")));
		submenu.addSeparator();
		submenu.add(editor.bind(mxResources.get("straight"),
				new SetStyleAction("straight"),
				"/com/mxgraph/examples/swing/images/straight.gif"));

		submenu.add(editor.bind(mxResources.get("horizontal"),
				new SetStyleAction(""),
				"/com/mxgraph/examples/swing/images/connect.gif"));
		submenu.add(editor.bind(mxResources.get("vertical"),
				new SetStyleAction("vertical"),
				"/com/mxgraph/examples/swing/images/vertical.gif"));
		
//		menu.add(editor.bind(mxResources.get("cellset"), new AbstractAction() {
//			
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		}));
//		menu.add(editor.bind(mxResources.get("lineset"), new AbstractAction() {
//			
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		}));
		//populateFormatMenu(menu, editor.getBasicGraphEditor());

		// Creates the shape menu
		menu = add(new JMenu(mxResources.get("shape")));

		populateShapeMenu(menu, editor.getBasicGraphEditor());

		// Creates the view menu
		menu = add(new JMenu(mxResources.get("view")));

//		JMenuItem item = menu.add(new TogglePropertyItem(graphComponent,
//				mxResources.get("pageLayout"), "PageVisible", true,
//				new ActionListener()
//				{
//					/**
//					 * 
//					 */
//					public void actionPerformed(ActionEvent e)
//					{
//						if (graphComponent.isPageVisible()
//								&& graphComponent.isCenterPage())
//						{
//							graphComponent.zoomAndCenter();
//						}
//						else
//						{
//							graphComponent.getGraphControl()
//									.updatePreferredSize();
//						}
//					}
//				}));
//
//		item.addActionListener(new ActionListener()
//		{
//			/*
//			 * (non-Javadoc)
//			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//			 */
//			public void actionPerformed(ActionEvent e)
//			{
//				if (e.getSource() instanceof TogglePropertyItem)
//				{
//					final mxGraphComponent graphComponent = editor
//							.getGraphComponent();
//					TogglePropertyItem toggleItem = (TogglePropertyItem) e
//							.getSource();
//
//					if (toggleItem.isSelected())
//					{
//						// Scrolls the view to the center
//						SwingUtilities.invokeLater(new Runnable()
//						{
//							/*
//							 * (non-Javadoc)
//							 * @see java.lang.Runnable#run()
//							 */
//							public void run()
//							{
//								graphComponent.scrollToCenter(true);
//								graphComponent.scrollToCenter(false);
//							}
//						});
//					}
//					else
//					{
//						// Resets the translation of the view
//						mxPoint tr = graphComponent.getGraph().getView()
//								.getTranslate();
//
//						if (tr.getX() != 0 || tr.getY() != 0)
//						{
//							graphComponent.getGraph().getView().setTranslate(
//									new mxPoint());
//						}
//					}
//				}
//			}
//		});
//
//		menu.add(new TogglePropertyItem(graphComponent, mxResources
//				.get("antialias"), "AntiAlias", true));
//		menu.add(new ToggleOutlineItem(editor, mxResources.get("outline")));
//
//		menu.addSeparator();
//
//		menu.add(new ToggleGridItem(editor, mxResources.get("grid")));
//		menu.add(new ToggleRulersItem(editor, mxResources.get("rulers")));
//
//		menu.addSeparator();
		submenu=(JMenu) menu.add(new JMenu(mxResources.get("show/hide")));
		submenu.add(editor.bind(mxResources.get("celllist"), new AbstractAction() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}));
        submenu.add(new ToggleOutlineItem(editor, mxResources.get("outline")));
  		submenu.add(new ToggleGridItem(editor, mxResources.get("grid")));
  		submenu.add(new ToggleRulersItem(editor, mxResources.get("rulers")));
        
		submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));

		submenu.add(editor.bind("400%", new ScaleAction(4)));
		submenu.add(editor.bind("200%", new ScaleAction(2)));
		submenu.add(editor.bind("150%", new ScaleAction(1.5)));
		submenu.add(editor.bind("100%", new ScaleAction(1)));
		submenu.add(editor.bind("75%", new ScaleAction(0.75)));
		submenu.add(editor.bind("50%", new ScaleAction(0.5)));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("custom"), new ScaleAction(0)));

//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("zoomIn"), mxGraphActions
//				.getZoomInAction()));
//		menu.add(editor.bind(mxResources.get("zoomOut"), mxGraphActions
//				.getZoomOutAction()));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("page"), new ZoomPolicyAction(
//				mxGraphComponent.ZOOM_POLICY_PAGE)));
//		menu.add(editor.bind(mxResources.get("width"), new ZoomPolicyAction(
//				mxGraphComponent.ZOOM_POLICY_WIDTH)));
//
//		menu.addSeparator();
//
//		menu.add(editor.bind(mxResources.get("actualSize"), mxGraphActions
//				.getZoomActualAction()));
		menu.addSeparator();
		//menu.add(editor.bind(mxResources.get("pruneonce"), mxGraphActions.getPruneOnceAction()));
		submenu= (JMenu) menu.add(new JMenu(mxResources.get("prune")));
		submenu.add(editor.bind(mxResources.get("pruneonce"),  mxGraphActions.getPruneOnceAction()));
		submenu.add(editor.bind(mxResources.get("pruneall"), mxGraphActions.getPruneAllAction()));
		submenu.add(editor.bind("单次恢复", mxGraphActions.getUnPruneOnceAction()));
		submenu.add(editor.bind("完全恢复", mxGraphActions.getUnPruneAllAction()));
		submenu.add(editor.bind(mxResources.get("lock"), new WarningAction()));
		submenu.add(editor.bind(mxResources.get("unlock"), new UnWarningAction()));

//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("viewManage")));
//
//		submenu.add(editor.bind(mxResources.get("combinEqui"),  mxGraphActions.getCombinEquiAction()));
//		submenu.add(editor.bind(mxResources.get("hideRelation"),  mxGraphActions.getHideRelationAction()));
//		submenu.add(editor.bind(mxResources.get("renameRelation"),  mxGraphActions.getRenameRelationAction()));
//		submenu.add(editor.bind(mxResources.get("transferUpper"), mxGraphActions.getTransferUpperAction()));
//		submenu.add(editor.bind(mxResources.get("hideSubcell"), mxGraphActions.getHideSubcellAction()));
//		submenu.add(editor.bind(mxResources.get("createnewcell"), mxGraphActions.getCreateNewVertexAction()));
		
		
		// Creates the diagram menu
		menu = add(new JMenu(mxResources.get("tools")));
		menu.add(editor.bind("导出建模规则", mxGraphActions.getExportAction()));
		menu.add(editor.bind("导入建模规则", mxGraphActions.getImportAction()));
		menu.add(editor.bind("恢复系统默认规则", mxGraphActions.getRecoveryAction()));
//		submenu = (JMenu) menu.add(new JMenu(mxResources.get("background")));
//
//		submenu.add(editor.bind(mxResources.get("backgroundColor"),
//				new BackgroundAction()));
//		submenu.add(editor.bind(mxResources.get("backgroundImage"),
//				new BackgroundImageAction()));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.bind(mxResources.get("pageBackground"),
//				new PageBackgroundAction()));

		

		//Creates the project menu
		menu = add(new JMenu(mxResources.get("project")));
		menu.add(editor.bind(mxResources.get("new"), new NewCDAction(),
		"/com/mxgraph/examples/swing/images/new.gif"));
		menu.add(editor.bind(mxResources.get("open"), new OpenCDAction(),
		"/com/mxgraph/examples/swing/images/open.gif"));
		menu.add(editor.bind(mxResources.get("save"), new SaveCDAction(false),
		"/com/mxgraph/examples/swing/images/save.gif"));
		menu.add(editor.bind(mxResources.get("saveAs"), new SaveCDAction(true),
		"/com/mxgraph/examples/swing/images/saveas.gif"));
		// Creates the help menu
		menu = add(new JMenu(mxResources.get("help")));

		JMenuItem item = menu.add(new JMenuItem(mxResources.get("aboutGraphEditor")));
		item.addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				editor.getBasicGraphEditor().about();
			}
		});
	}

	/**
	 * Adds menu items to the given shape menu. This is factored out because
	 * the shape menu appears in the menubar and also in the popupmenu.
	 */
	public static void populateShapeMenu(JMenu menu, BasicGraphEditor editor)
	{
//		menu.add(editor.bind(mxResources.get("home"), mxGraphActions
//				.getHomeAction(),
//				"/com/mxgraph/examples/swing/images/house.gif"));

		JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("group")));
		submenu.add(editor.bind(mxResources.get("group"), mxGraphActions
				.getGroupAction(),
		"/com/mxgraph/examples/swing/images/group.gif"));
		submenu.add(editor.bind(mxResources.get("ungroup"), mxGraphActions
				.getUngroupAction(),
		"/com/mxgraph/examples/swing/images/ungroup.gif"));
		submenu.add(editor.bind(mxResources.get("collapse"), mxGraphActions
				.getCollapseAction(),
		"/com/mxgraph/examples/swing/images/collapse.gif"));
		submenu.add(editor.bind(mxResources.get("expand"), mxGraphActions
				.getExpandAction(),
		"/com/mxgraph/examples/swing/images/expand.gif"));
//		menu.add(editor.bind(mxResources.get("exitGroup"), mxGraphActions
//				.getExitGroupAction(),
//				"/com/mxgraph/examples/swing/images/up.gif"));
//		menu.add(editor.bind(mxResources.get("enterGroup"), mxGraphActions
//				.getEnterGroupAction(),
//				"/com/mxgraph/examples/swing/images/down.gif"));

//		menu.add(editor.bind(mxResources.get("group"), mxGraphActions
//				.getGroupAction(),
//				"/com/mxgraph/examples/swing/images/group.gif"));
//		menu.add(editor.bind(mxResources.get("ungroup"), mxGraphActions
//				.getUngroupAction(),
//				"/com/mxgraph/examples/swing/images/ungroup.gif"));
//
//		menu.addSeparator();

//		menu.add(editor.bind(mxResources.get("removeFromGroup"), mxGraphActions
//				.getRemoveFromParentAction()));
//
//		menu.add(editor.bind(mxResources.get("updateGroupBounds"), mxGraphActions
//				.getUpdateGroupBoundsAction()));

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("layerchange")));
		submenu.add(editor.bind(mxResources.get("toBack"), mxGraphActions
				.getToBackAction(),
				"/com/mxgraph/examples/swing/images/toback.gif"));
		submenu.add(editor.bind(mxResources.get("toFront"), mxGraphActions
				.getToFrontAction(),
				"/com/mxgraph/examples/swing/images/tofront.gif"));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("align")));

		submenu.add(editor.bind(mxResources.get("left"), new AlignCellsAction(
				mxConstants.ALIGN_LEFT),
				"/com/mxgraph/examples/swing/images/alignleft.gif"));
		submenu.add(editor.bind(mxResources.get("center"),
				new AlignCellsAction(mxConstants.ALIGN_CENTER),
				"/com/mxgraph/examples/swing/images/aligncenter.gif"));
		submenu.add(editor.bind(mxResources.get("right"), new AlignCellsAction(
				mxConstants.ALIGN_RIGHT),
				"/com/mxgraph/examples/swing/images/alignright.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("top"), new AlignCellsAction(
				mxConstants.ALIGN_TOP),
				"/com/mxgraph/examples/swing/images/aligntop.gif"));
		submenu.add(editor.bind(mxResources.get("middle"),
				new AlignCellsAction(mxConstants.ALIGN_MIDDLE),
				"/com/mxgraph/examples/swing/images/alignmiddle.gif"));
		submenu.add(editor.bind(mxResources.get("bottom"),
				new AlignCellsAction(mxConstants.ALIGN_BOTTOM),
				"/com/mxgraph/examples/swing/images/alignbottom.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("autosize"),new AutosizeAction()));
		submenu = (JMenu) menu.add(new JMenu(mxResources.get("layout")));

		submenu.add(editor.graphLayout("verticalHierarchical"));
		submenu.add(editor.graphLayout("horizontalHierarchical"));
		submenu.add(editor.graphLayout("organicLayout"));

//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalPartition"));
//		submenu.add(editor.graphLayout("horizontalPartition"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalStack"));
//		submenu.add(editor.graphLayout("horizontalStack"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("verticalTree"));
//		submenu.add(editor.graphLayout("horizontalTree"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("placeEdgeLabels"));
//		submenu.add(editor.graphLayout("parallelEdges"));
//
//		submenu.addSeparator();
//
//		submenu.add(editor.graphLayout("organicLayout"));
//		submenu.add(editor.graphLayout("circleLayout"));		


	}

	/**
	 * Adds menu items to the given format menu. This is factored out because
	 * the format menu appears in the menubar and also in the popupmenu.
	 */
	public static void populateFormatMenu(JMenu menu, BasicGraphEditor editor)
	{

		JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("line")));

		submenu.add(editor.bind(mxResources.get("linecolor"), new ColorAction(
				"Linecolor", mxConstants.STYLE_STROKECOLOR),
				"/com/mxgraph/examples/swing/images/linecolor.gif"));

		submenu.addSeparator();

//		submenu.add(editor.bind(mxResources.get("dashed"), new ToggleAction(
//				mxConstants.STYLE_DASHED)));
		submenu.add(editor.bind(mxResources.get("linewidth"),
				new PromptValueAction(mxConstants.STYLE_STROKEWIDTH,
						"Linewidth")));

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("connector")));

		submenu.add(editor.bind(mxResources.get("straight"),
				new SetStyleAction("straight"),
				"/com/mxgraph/examples/swing/images/straight.gif"));

		submenu.add(editor.bind(mxResources.get("horizontal"),
				new SetStyleAction(""),
				"/com/mxgraph/examples/swing/images/connect.gif"));
		submenu.add(editor.bind(mxResources.get("vertical"),
				new SetStyleAction("vertical"),
				"/com/mxgraph/examples/swing/images/vertical.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("entityRelation"),
				new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"),
				"/com/mxgraph/examples/swing/images/entity.gif"));
		submenu.add(editor.bind(mxResources.get("arrow"), new SetStyleAction(
				"arrow"), "/com/mxgraph/examples/swing/images/arrow.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("plain"), new ToggleAction(
				mxConstants.STYLE_NOEDGESTYLE)));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("linestart")));

		submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN),
				"/com/mxgraph/examples/swing/images/open_start.gif"));
		submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC),
				"/com/mxgraph/examples/swing/images/classic_start.gif"));
		submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK),
				"/com/mxgraph/examples/swing/images/block_start.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND),
				"/com/mxgraph/examples/swing/images/diamond_start.gif"));
		submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL),
				"/com/mxgraph/examples/swing/images/oval_start.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
				mxConstants.STYLE_STARTARROW, mxConstants.NONE)));
		submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
				mxConstants.STYLE_STARTSIZE, "Linestart Size")));

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("lineend")));

		submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN),
				"/com/mxgraph/examples/swing/images/open_end.gif"));
		submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC),
				"/com/mxgraph/examples/swing/images/classic_end.gif"));
		submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK),
				"/com/mxgraph/examples/swing/images/block_end.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND),
				"/com/mxgraph/examples/swing/images/diamond_end.gif"));
		submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL),
				"/com/mxgraph/examples/swing/images/oval_end.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
				mxConstants.STYLE_ENDARROW, mxConstants.NONE)));
		submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
				mxConstants.STYLE_ENDSIZE, "Linestart Size")));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("rounded"), new ToggleAction(
				mxConstants.STYLE_ROUNDED)));

		menu.add(editor.bind(mxResources.get("style"), new StyleAction()));
	}



}
