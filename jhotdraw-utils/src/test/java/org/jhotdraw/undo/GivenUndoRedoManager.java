package org.jhotdraw.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;

import static org.mockito.Mockito.mock;


public class GivenUndoRedoManager extends Stage<GivenUndoRedoManager> {
    @ProvidedScenarioState
    private UndoRedoManager instance;

    @ProvidedScenarioState
    private UndoableEdit edit;

    public GivenUndoRedoManager an_empty_edit_history() {
        ResourceBundleUtil resourceBundleUtil = mock(ResourceBundleUtil.class);
        instance = new UndoRedoManager(resourceBundleUtil);
        return this;
    }

    public GivenUndoRedoManager a_meaningful_edit_was_made() {
        edit = new AbstractUndoableEdit() {
            @Override
            public boolean isSignificant() {
                return true;
            }
        };
        return this;
    }

    public GivenUndoRedoManager a_minor_edit_was_made() {
        edit = new AbstractUndoableEdit() {
            @Override
            public boolean isSignificant() {
                return false;
            }
        };
        return this;
    }

    public GivenUndoRedoManager an_edit_history_with_an_undone_edit() {
        ResourceBundleUtil resourceBundleUtil = mock(ResourceBundleUtil.class);
        instance = new UndoRedoManager(resourceBundleUtil);

        edit = new AbstractUndoableEdit() {
            @Override
            public boolean isSignificant() {
                return true;
            }
        };
        instance.addEdit(edit);
        instance.undo();
        return this;
    }
}
