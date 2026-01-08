package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;



public class DrawSVGRectBDDTest extends ScenarioTest<GivenDrawingCanvas, WhenDrawingRectangle, ThenRectangleIsShown> {

    @Test
    public void userDrawsRectangle(){
        given().userIsOnDrawingCanvas();

        when().userSelectsRectangleTool()
                .and().userDragsRectFromStartPointToEndPoint(10, 10, 50, 50);

        then().createdRectShouldHaveThoseDimensions()
                .and().createdRectShouldBeOnCanvas();
    }

}


















