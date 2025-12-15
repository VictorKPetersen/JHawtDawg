package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Drawing;

public class GivenDrawingCanvas extends Stage<GivenDrawingCanvas> {
    @ProvidedScenarioState
    Drawing drawing;

    public GivenDrawingCanvas userIsOnDrawingCanvas(){
        drawing = new DefaultDrawing();
        return this;
    }
}
