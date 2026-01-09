package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;

import java.util.ArrayList;
import java.util.List;

public class GivenDrawingState extends Stage<GivenDrawingState> {

    @ProvidedScenarioState
    private DrawingView drawingView;

    @ProvidedScenarioState
    private List<Figure> selectionOrder;

    public GivenDrawingState drawing_with_overlapping_figures() {
        drawingView = new DefaultDrawingView();
        drawingView.setDrawing(new QuadTreeDrawing());
        selectionOrder = new ArrayList<>();

        Drawing drawing = drawingView.getDrawing();

        drawing.add(new RectangleFigure(0,0,100,100));
        drawing.add(new RectangleFigure(10,10,100,100));
        drawing.add(new RectangleFigure(20,20,100,100));
        drawing.add(new RectangleFigure(30,30,100,100));

        return this;
    }

    public GivenDrawingState back_figure_selected() {
        drawingView.addToSelection(drawingView.getDrawing().getChild(0));
        selectionOrder.add(drawingView.getDrawing().getChild(0));
        return this;
    }

    public GivenDrawingState front_figure_selected() {
        drawingView.addToSelection(drawingView.getDrawing().getChild(3));
        selectionOrder.add(drawingView.getDrawing().getChild(3));
        return this;
    }

    public GivenDrawingState two_figures_selected() {
        drawingView.addToSelection(drawingView.getDrawing().getChild(1));
        drawingView.addToSelection(drawingView.getDrawing().getChild(2));

        selectionOrder.add(drawingView.getDrawing().getChild(2));
        selectionOrder.add(drawingView.getDrawing().getChild(1));

        return this;
    }
}
