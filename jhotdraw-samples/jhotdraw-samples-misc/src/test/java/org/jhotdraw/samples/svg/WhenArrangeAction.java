package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.action.SendToBackAction;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;

public class WhenArrangeAction extends Stage<WhenArrangeAction> {
    @ProvidedScenarioState
    private DrawingView drawingView;

    @ProvidedScenarioState
    private List<Figure> selectionOrder;

    public WhenArrangeAction clicks_bring_to_front() {
        BringToFrontAction.bringToFront(drawingView, drawingView.getSelectedFigures());
        return this;
    }

    public WhenArrangeAction clicks_send_to_back() {
        SendToBackAction.sendToBack(drawingView, drawingView.getSelectedFigures());
        return this;
    }
}
