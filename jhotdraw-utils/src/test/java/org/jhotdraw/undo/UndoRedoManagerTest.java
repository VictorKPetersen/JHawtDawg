package org.jhotdraw.undo;

import org.jhotdraw.util.ResourceBundleUtil;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UndoRedoManagerTest {
    private UndoRedoManager instance;
    private UndoableEdit significantEdit;
    private UndoableEdit nonSignificantEdit;

    @Before
    public void setUp() {
        ResourceBundleUtil resourceBundleUtil = mock(ResourceBundleUtil.class);
        instance = new UndoRedoManager(resourceBundleUtil);

        significantEdit = mock(UndoableEdit.class);
        when(significantEdit.isSignificant()).thenReturn(true);

        nonSignificantEdit = mock(UndoableEdit.class);
        when(nonSignificantEdit.isSignificant()).thenReturn(false);
    }

    @Test
    public void addSignificantEdit() {
        instance.addEdit(significantEdit);

        assertTrue(instance.hasSignificantEdits());
    }

    @Test
    public void addNonSignificantEdit() {
        instance.addEdit(nonSignificantEdit);

        assertFalse(instance.hasSignificantEdits());
    }

    @Test
    public void addSignificantEditEnablesUndoAction() {
        AbstractUndoableEdit realEdit = new AbstractUndoableEdit();
        Action undoAction = instance.getUndoAction();

        instance.addEdit(realEdit);

        assertTrue(undoAction.isEnabled());
    }

    @Test
    public void undoSignificantEditEnablesRedoAction() {
        AbstractUndoableEdit realEdit = new AbstractUndoableEdit();
        Action redoAction = instance.getRedoAction();

        instance.addEdit(realEdit);
        instance.undo();

        assertTrue(redoAction.isEnabled());
    }

    @Test
    public void addEditDuringUndoCallsDie() {
        UndoableEdit firstEdit = mock(UndoableEdit.class);
        when(firstEdit.isSignificant()).thenReturn(true);
        when(firstEdit.canUndo()).thenReturn(true);

        UndoableEdit secondEdit = mock(UndoableEdit.class);

        instance.addEdit(firstEdit);

        doAnswer(invocation -> {
            instance.addEdit(secondEdit);
            return null;
        }).when(firstEdit).undo();

        instance.undo();

        verify(secondEdit, times(1)).die();
    }

    @Test
    public void discardAllEditsResetsState() {
        instance.addEdit(significantEdit);
        instance.discardAllEdits();

        assertFalse(instance.hasSignificantEdits());
        assertFalse(instance.canUndo());
    }

    @Test
    public void discardAllEditsDisablesUndoAndRedeActions() {
        instance.discardAllEdits();
        Action undoAction = instance.getUndoAction();
        Action redoAction = instance.getRedoAction();

        assertFalse(undoAction.isEnabled());
        assertFalse(redoAction.isEnabled());
    }

    @Test(expected = CannotUndoException.class)
    public void undoWithEmptyStackThrowsException() {
        instance.undo();
    }

    @Test(expected = CannotRedoException.class)
    public void redoWithEmptyStackThrowsException() {
        instance.redo();
    }
}