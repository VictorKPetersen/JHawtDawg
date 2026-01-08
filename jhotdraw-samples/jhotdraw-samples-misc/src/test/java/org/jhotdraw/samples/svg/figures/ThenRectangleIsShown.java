package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenRectangleIsShown extends Stage<ThenRectangleIsShown> {
    @ExpectedScenarioState
    SVGRectFigure createdRectangle;

    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    WhenDrawingRectangle.RectanglePoints rectanglePoints;

    public ThenRectangleIsShown createdRectShouldHaveThoseDimensions(){
        Point2D.Double startPoint = rectanglePoints.startPoint;
        Point2D.Double endPoint = rectanglePoints.endPoint;

        double expectedX = Math.min(startPoint.x, endPoint.x);
        double expectedY = Math.min(startPoint.y, endPoint.y);
        double expectedWidth = Math.abs(endPoint.x - startPoint.x);
        double expectedHeight = Math.abs(endPoint.y - startPoint.y);

        assertThat(createdRectangle).isNotNull();
        assertThat(createdRectangle.getX()).isEqualTo(expectedX);
        assertThat(createdRectangle.getY()).isEqualTo(expectedY);
        assertThat(createdRectangle.getWidth()).isEqualTo(expectedWidth);
        assertThat(createdRectangle.getHeight()).isEqualTo(expectedHeight);

        return this;
    }

    public ThenRectangleIsShown createdRectShouldBeOnCanvas(){
        assertThat(drawing.getChildren()).contains(createdRectangle);
        assertThat(createdRectangle.isEmpty()).isFalse();

        Rectangle2D.Double rectBounds = createdRectangle.getBounds();
        assertThat(rectBounds).isNotNull();
        assertThat(rectBounds.width).isGreaterThan(0);
        assertThat(rectBounds.height).isGreaterThan(0);

        return this;
    }


}
