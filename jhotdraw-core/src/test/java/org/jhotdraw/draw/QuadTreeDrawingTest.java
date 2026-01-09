package org.jhotdraw.draw;

import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class QuadTreeDrawingTest {

    private QuadTreeDrawing drawing;

    @Before
    public void setUp() {
        drawing = new QuadTreeDrawing();
        addMockFigures();
    }

    private void addMockFigures() {
        for (int i = 0; i < 5; i++) {
            drawing.add(getMockFigure());
        }
    }

    private Figure getMockFigure() {
        Figure figure = mock(Figure.class);
        when(figure.getDrawingArea()).thenReturn(new Rectangle2D.Double(0.0, 0.0, 100, 100.0));
        return figure;
    }

    @Test
    public void testBringToFront() {
        Figure figure = getMockFigure();
        //Add figure to back of figures
        drawing.add(0, figure);

        drawing.bringToFront(figure);

        int childCount = drawing.getChildren().size();
        assertEquals(drawing.getChildren().get(childCount - 1), figure);
    }

    @Test
    public void testSendToBack() {
        Figure figure = getMockFigure();
        //Add figure to front of figures
        drawing.add(figure);

        drawing.sendToBack(figure);

        assertEquals(drawing.getChildren().get(0), figure);
    }
}