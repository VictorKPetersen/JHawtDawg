package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ThenArrangeOutcomes extends Stage<ThenArrangeOutcomes> {
    @ProvidedScenarioState
    private DrawingView drawingView;

    @ProvidedScenarioState
    private List<Figure> selectionOrder;


    public ThenArrangeOutcomes selected_figures_are_on_top() {
        int i = 0;

        int childCount = drawingView.getDrawing().getChildCount() - 1;

        for (Figure figure : selectionOrder) {
            assertThat(figure).isSameAs(drawingView.getDrawing().getChild(childCount - i));
            i++;
        }

        return this;
    }

    public ThenArrangeOutcomes selected_figures_are_on_bottom() {
        int i = 0;

        Collections.reverse(selectionOrder);

        for (Figure figure : selectionOrder) {
            assertThat(figure).isSameAs(drawingView.getDrawing().getChild(i));
            i++;
        }

        return this;
    }
}
