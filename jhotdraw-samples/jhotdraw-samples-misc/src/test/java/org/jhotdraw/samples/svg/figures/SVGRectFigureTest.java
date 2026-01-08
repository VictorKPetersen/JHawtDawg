package org.jhotdraw.samples.svg.figures;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;

public class SVGRectFigureTest {

    private SVGRectFigure figure;

    @Before
    public void setUp() {
        figure = new SVGRectFigure();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("X should be 0", 0.0, figure.getX(), 0.0);
        assertEquals("Y should be 0", 0.0, figure.getY(), 0.0);
        assertEquals("Width should be 0", 0.0, figure.getWidth(), 0.0);
        assertEquals("Height should be 0", 0.0, figure.getHeight(), 0.0);
        assertEquals("Arc width should be 0", 0.0, figure.getArcWidth(), 0.0);
        assertEquals("Arc height should be 0", 0.0, figure.getArcHeight(), 0.0);
    }

    @Test
    public void testConstructorWithPosAndSize() {
        SVGRectFigure.Position pos = new SVGRectFigure.Position(10.0, 20.0);
        SVGRectFigure.Size size = new SVGRectFigure.Size(100.0, 50.0);

        SVGRectFigure rectFigure = new SVGRectFigure(pos, size);
        assertEquals("X should be 10.0", 10.0, rectFigure.getX(), 0.0);
        assertEquals("Y should be 20.0", 20.0, rectFigure.getY(), 0.0);
        assertEquals("Width should be 100.0", 100.0, rectFigure.getWidth(), 0.0);
        assertEquals("Height should be 50.0", 50.0, rectFigure.getHeight(), 0.0);
    }

    @Test
    public void testConstructorWithPosSizeAndRot() {
        SVGRectFigure.Position pos = new SVGRectFigure.Position(5.0, 15.0);
        SVGRectFigure.Size size = new SVGRectFigure.Size(80.0, 40.0);
        SVGRectFigure.Rotation rot = new SVGRectFigure.Rotation(10.0, 5.0);

        SVGRectFigure rectFigure = new SVGRectFigure(pos, size, rot);
        assertEquals("X should be 10.0", 5.0, rectFigure.getX(), 0.0);
        assertEquals("Y should be 20.0", 15.0, rectFigure.getY(), 0.0);
        assertEquals("Width should be 100.0", 80.0, rectFigure.getWidth(), 0.0);
        assertEquals("Height should be 50.0", 40.0, rectFigure.getHeight(), 0.0);
        assertEquals("Arc width should be 10.0", 10.0, rectFigure.getArcWidth(), 0.0);
        assertEquals("Arc height should be 5.0", 5.0, rectFigure.getArcHeight(), 0.0);
    }

    @Test
    public void testGetBounds() {
        figure.setBounds(new Point2D.Double(10.0, 20.0), new Point2D.Double(110.0, 120.0));

        Rectangle2D.Double bounds = figure.getBounds();

        assertEquals("Bounds X should be 10.0", 10.0, bounds.x, 0.0);
        assertEquals("Bounds Y should be 20.0", 20.0, bounds.y, 0.0);
        assertEquals("Bounds width should be 100.0", 100.0, bounds.width, 0.0);
        assertEquals("Bounds height should be 100.0", 100.0, bounds.height, 0.0);
    }

    @Test
    public void testPositiveDimensionInvariance() {
        figure.setBounds(new Point2D.Double(100.0, 100.0), new Point2D.Double(20.0, 30.0));

        assertTrue("Width must be positive.", figure.getWidth() > 0);
        assertTrue("Height must be positive.", figure.getHeight() > 0);
    }


    @After
    public void tearDown() {
        figure = null;
    }
}