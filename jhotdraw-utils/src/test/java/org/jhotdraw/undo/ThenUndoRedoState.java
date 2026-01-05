package org.jhotdraw.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import static org.assertj.core.api.Assertions.*;

public class ThenUndoRedoState extends Stage<ThenUndoRedoState> {
    @ExpectedScenarioState
    private UndoRedoManager instance;

    public ThenUndoRedoState the_edit_is_stored() {
        assertThat(instance.hasSignificantEdits()).isTrue();
        return this;
    }

    public ThenUndoRedoState the_edit_is_not_stored() {
        assertThat(instance.hasSignificantEdits()).isFalse();
        return this;
    }

    public ThenUndoRedoState the_undo_option_is_available() {
        assertThat(instance.getUndoAction().isEnabled()).isTrue();
        return this;
    }

    public ThenUndoRedoState the_undo_option_is_not_available() {
        assertThat(instance.getUndoAction().isEnabled()).isFalse();
        return this;
    }

    public ThenUndoRedoState the_redo_option_is_available() {
        assertThat(instance.getRedoAction().isEnabled()).isTrue();
        return this;
    }

    public ThenUndoRedoState the_redo_option_is_not_available() {
        assertThat(instance.getRedoAction().isEnabled()).isFalse();
        return this;
    }
}
