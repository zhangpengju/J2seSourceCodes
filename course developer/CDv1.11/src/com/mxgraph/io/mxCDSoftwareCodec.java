/**
 * $Id: mxCDSoftwareCodec.java,v 1.1 2011/06/23 06:41:33 administrator Exp $
 * Copyright (c) 2006, Gaudenz Alder
 */
package com.mxgraph.io;

import java.awt.Desktop;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.examples.swing.CDSoftWare;
import com.mxgraph.examples.swing.editor.BasicGraphEditor;
import com.sun.xml.internal.ws.api.pipe.Codec;

/**
 * Codec for mxGraphModels. This class is created and registered
 * dynamically at load time and used implicitely via mxCodec
 * and the mxCodecRegistry.
 */
public class mxCDSoftwareCodec extends mxObjectCodec
{

	/**
	 * Constructs a new model codec.
	 */
	public mxCDSoftwareCodec()
	{
		this(new CDSoftWare());
	}

	/**
	 * Constructs a new model codec for the given template.
	 */
	public mxCDSoftwareCodec(Object template)
	{
		this(template, null, null, null);
	}

	/**
	 * Constructs a new model codec for the given arguments.
	 */
	public mxCDSoftwareCodec(Object template, String[] exclude, String[] idrefs,
			Map<String, String> mapping)
	{
		super(template, exclude, idrefs, mapping);
	}

	/**
	 * Encode the given mxGraphModel by writing a (flat) XML sequence
	 * of cell nodes as produced by the mxCellCodec. The sequence is
	 * wrapped-up in a node with the name root.
	 */
	public Node encode(mxCodec enc, Object obj)
	{
		Node node = null;

		BasicGraphEditor basicGraphEditor;
		Node modelNode;
		if (obj instanceof CDSoftWare) {
			CDSoftWare cdSoftWare=(CDSoftWare)obj;
			JDesktopPane desktopPane=cdSoftWare.getDesktop();
			String name=mxCodecRegistry.getName(obj);
			
			node = enc.document.createElement(name);
		    Node rooNode = enc.document.createElement("object");
		    
			JInternalFrame[] frames = desktopPane.getAllFrames(); 
			for(int i=0;i<frames.length;i++){ 
				if(frames[i] instanceof BasicGraphEditor){

					basicGraphEditor=(BasicGraphEditor)frames[i];
					
					modelNode=enc.encode(basicGraphEditor.getGraphComponent().getGraph().getModel());
					rooNode.appendChild(modelNode);
				}
			}
			node.appendChild(rooNode);
		}

		return node;
	}

	/**
	 * Reads the cells into the graph model. All cells are children of the root
	 * element in the node.
	 */
	public Node beforeDecode(mxCodec dec, Node node, Object into)
	{
		if(node instanceof Element){
			Element elt = (Element) node;
			CDSoftWare cdSoftWare=null;
			
			if(into instanceof CDSoftWare){
				cdSoftWare=(CDSoftWare)into;
			}
			else {
				cdSoftWare=new CDSoftWare();
			}
			Node object = elt.getElementsByTagName("object").item(0);
			if (object != null)
			{
				Node tmp = object.getFirstChild();

				while (tmp != null)
				{
				
					if (tmp instanceof Element)
					{
						Element elt0 = (Element) tmp;
						mxGraphModel model = null;

						if (into instanceof mxGraphModel)
						{
							model = (mxGraphModel) into;
						}
						else
						{
							model = new mxGraphModel();
						}

						// Reads the cells into the graph model. All cells
						// are children of the root element in the node.
						Node root = elt0.getElementsByTagName("root").item(0);
						mxICell rootCell = null;

						if (root != null)
						{
							Node tmp0 = root.getFirstChild();

							while (tmp0 != null)
							{
								mxICell cell = dec.decodeCell(tmp0, true);

								if (cell != null && cell.getParent() == null)
								{
									rootCell = cell;
								}

								tmp0 = tmp0.getNextSibling();
							}

							root.getParentNode().removeChild(root);
						}

						// Sets the root on the model if one has been decoded
						if (rootCell != null)
						{
							model.setRoot(rootCell);
						}
					}
				}
			}
		}
		

		return node;
	}

}
