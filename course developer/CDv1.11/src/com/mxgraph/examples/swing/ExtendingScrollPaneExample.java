package com.mxgraph.examples.swing;

import java.awt.Adjustable;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ExtendingScrollPaneExample extends JFrame {

    private static final long serialVersionUID = 2632263620101793705L;

    mxGraphComponent graphComponent;

    public ExtendingScrollPaneExample() {

        super("ScrollPane Example");

        mxGraph graph = new mxGraph();
        Object graphParent = graph.getDefaultParent();

        graphParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object v1 = graph.insertVertex(graphParent, null, "Vertex 1", 50, 50, 80, 30);
            Object v2 = graph.insertVertex(graphParent, null, "Vertex 2", 800, 300, 80, 30);

            graph.insertEdge(graphParent, "", "", v1, v2);
        } finally {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        graphComponent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        graphComponent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        graphComponent.getHorizontalScrollBar().setUnitIncrement(40);
        graphComponent.getVerticalScrollBar().setUnitIncrement(40);

        graphComponent.setGridVisible(true);
        graphComponent.setGridStyle(mxGraphComponent.GRID_STYLE_LINE);

        AdjustmentListener listener = new MyAdjustmentListener();
        graphComponent.getHorizontalScrollBar().addAdjustmentListener(listener);
        graphComponent.getVerticalScrollBar().addAdjustmentListener(listener);

    }

    class MyAdjustmentListener implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent evt) {
            Adjustable source = evt.getAdjustable();

            if (evt.getValueIsAdjusting()) {
                return;
            }

            Rectangle rect = graphComponent.getGraphControl().getVisibleRect();

            int orientation = source.getOrientation();
            if (orientation == Adjustable.HORIZONTAL) {
                rect.x += 60;
            } else {
                rect.y += 60;
            }

            // we'd need to invoke the following, but since it isn't accessible
            // because of its protected state, we need to use reflection

            // 1. what we should do:
            // graphComponent.getGraphControl().extendComponent(rect);

            // 2. what we can do:
            try {

                // TODO: optimize or ask mxGraph devs nicely to make
                // extendComponent public
                Method protectedMethod = graphComponent.getGraphControl().getClass().getDeclaredMethod("extendComponent", new Class[] { Rectangle.class });
                protectedMethod.setAccessible(true);
                protectedMethod.invoke(graphComponent.getGraphControl(), rect);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ExtendingScrollPaneExample frame = new ExtendingScrollPaneExample();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
