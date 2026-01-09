package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrangeFigureScenarioTests extends ScenarioTest<GivenDrawingState, WhenArrangeAction, ThenArrangeOutcomes> {

    @Test
    public void bring_to_front_should_be_successful() {
        given().drawing_with_overlapping_figures()
                .and().back_figure_selected();

        when().clicks_bring_to_front();

        then().selected_figures_are_on_top();
    }

    @Test
    public void send_to_back_should_be_successful() {
        given().drawing_with_overlapping_figures()
                .and().front_figure_selected();

        when().clicks_send_to_back();

        then().selected_figures_are_on_bottom();
    }

    @Test
    public void send_multiple_to_front_should_retain_internal_order() {
        given().drawing_with_overlapping_figures()
                .and().two_figures_selected();

        when().clicks_bring_to_front();

        then().selected_figures_are_on_top();
    }

    @Test
    public void send_multiple_to_back_should_retain_internal_order() {
        given().drawing_with_overlapping_figures()
                .and().two_figures_selected();

        when().clicks_send_to_back();

        then().selected_figures_are_on_bottom();
    }
}