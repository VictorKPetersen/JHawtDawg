package org.jhotdraw.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import javax.swing.undo.UndoableEdit;

public class WhenUndoRedoAction extends Stage<WhenUndoRedoAction> {
    @ExpectedScenarioState
    private UndoRedoManager instance;

    @ExpectedScenarioState
    private UndoableEdit edit;

    public WhenUndoRedoAction the_edit_is_made() {
        instance.addEdit(edit);
        return this;
    }

    public WhenUndoRedoAction the_undo_action_is_executed() {
        instance.undo();
        return this;
    }

    public WhenUndoRedoAction the_redo_action_is_executed() {
        instance.redo();
        return this;
    }
}
