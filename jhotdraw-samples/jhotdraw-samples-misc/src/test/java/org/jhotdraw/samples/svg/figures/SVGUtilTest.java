package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.Gradient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class SVGUtilTest {

    private Figure mockFigure;
    private Shape mockCachedShape;
    private Shape mockTransformedShape;

    @Before
    public void setUp() {
        mockFigure = mock(Figure.class);

        mockTransformedShape = new Rectangle2D.Double(0, 0, 100, 100);
        mockCachedShape = new Ellipse2D.Double(0, 0, 50, 50);

        setUpMocksBehavior();
    }

    private void setUpMocksBehavior() {
        when(mockFigure.get(AttributeKeys.STROKE_WIDTH)).thenReturn(2.0);
        when(mockFigure.get(AttributeKeys.STROKE_TYPE)).thenReturn(AttributeKeys.StrokeType.BASIC);
        when(mockFigure.get(AttributeKeys.STROKE_PLACEMENT)).thenReturn(AttributeKeys.StrokePlacement.CENTER);
        when(mockFigure.get(AttributeKeys.STROKE_MITER_LIMIT)).thenReturn(10.0);
        when(mockFigure.get(AttributeKeys.STROKE_JOIN)).thenReturn(BasicStroke.JOIN_MITER);
        when(mockFigure.get(AttributeKeys.STROKE_CAP)).thenReturn(BasicStroke.CAP_BUTT);
        when(mockFigure.get(AttributeKeys.IS_STROKE_MITER_LIMIT_FACTOR)).thenReturn(false);
    }


    @After
    public void tearDown() {
        mockFigure = null;
        mockTransformedShape = null;
        mockCachedShape = null;
    }

    @Test
    public void testGetHitShapeUtilReturnsCachedShape() {
        Color fillColor = Color.RED;
        Gradient fillGradient = mock(Gradient.class);

        HitShapeParam<Color, Gradient> param = new HitShapeParam<>(mockCachedShape, fillColor, fillGradient, mockTransformedShape);

        Shape result = SVGUtil.getHitShapeUtil(mockFigure, param);

        assertSame("Cached shape should not change", mockCachedShape, param.getCachedHitShape());
        assertSame("Cached shape should equal the result", mockCachedShape, result);

    }

    @Test
    public void testGetHitShapeUtilCalculatesWhenCacheEmpty() {
        Color fillColor = Color.RED;

        HitShapeParam<Color, Gradient> param = new HitShapeParam<>(null, fillColor, null, mockTransformedShape);

        Shape result = SVGUtil.getHitShapeUtil(mockFigure, param);

        assertNotNull("Should calculate and return a shape", result);
        assertNotNull("Should populate the cache", param.getCachedHitShape());
        assertSame("Returned shape should be the cached shape", result, param.getCachedHitShape());
    }
}