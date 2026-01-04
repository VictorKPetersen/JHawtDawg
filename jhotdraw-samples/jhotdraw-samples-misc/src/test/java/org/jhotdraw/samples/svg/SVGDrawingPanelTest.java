package org.jhotdraw.samples.svg;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.undo.UndoRedoManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.swing.undo.AbstractUndoableEdit;



import static org.junit.Assert.*;

public class SVGDrawingPanelTest {
    private SVGDrawingPanel instance;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        instance = new SVGDrawingPanel();
    }

    @Test
    public void setDrawingDiscardsOldEdits() {
        Drawing drawing = instance.createDrawing();
        UndoRedoManager undoManager = instance.getUndoRedoManager();

        undoManager.addEdit(new AbstractUndoableEdit());
        assertTrue(undoManager.canUndo());

        instance.setDrawing(drawing);
        assertFalse(undoManager.canUndo());
    }

    @Test
    public void createDrawingDiscardOldEdits() {
        UndoRedoManager undoManager = instance.getUndoRedoManager();

        undoManager.addEdit(new AbstractUndoableEdit());
        assertTrue(undoManager.canUndo());

        instance.setDrawing(instance.createDrawing());

        assertFalse(undoManager.canUndo());
    }

    @Test
    public void readNullURIThrowsException() {
        assertThrows(NullPointerException.class, () -> instance.read(null));
    }

    @Test
    public void writeNullURIThrowsException() {
        assertThrows(NullPointerException.class, () -> instance.write(null));
    }
}