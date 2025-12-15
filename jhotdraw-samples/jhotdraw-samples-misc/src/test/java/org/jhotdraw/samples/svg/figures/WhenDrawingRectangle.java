package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;

import java.awt.geom.Point2D;

public class WhenDrawingRectangle extends Stage<WhenDrawingRectangle> {
    @ExpectedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    SVGRectFigure createdRectangle;

    @ProvidedScenarioState
    RectanglePoints rectanglePoints;

    public static class RectanglePoints{
        public Point2D.Double startPoint;
        public Point2D.Double endPoint;

        public RectanglePoints(Point2D.Double startPoint, Point2D.Double endPoint){
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }
    }

    public WhenDrawingRectangle userSelectsRectangleTool(){
        return this;
    }


    public WhenDrawingRectangle userDragsRectFromStartPointToEndPoint(int x1, int y1, int x2, int y2){
        rectanglePoints = new RectanglePoints(new Point2D.Double(x1,y1), new Point2D.Double(x2,y2));

        createdRectangle = new SVGRectFigure();
        createdRectangle.setBounds(rectanglePoints.startPoint, rectanglePoints.endPoint);

        drawing.add(createdRectangle);

        return this;
    }
}
