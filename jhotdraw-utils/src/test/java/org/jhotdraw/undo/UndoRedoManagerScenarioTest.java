package org.jhotdraw.undo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class UndoRedoManagerScenarioTest
        extends ScenarioTest<GivenUndoRedoManager, WhenUndoRedoAction, ThenUndoRedoState> {
    @Test
    public void meaningful_changes_should_be_reversible() {
        given().an_empty_edit_history()
                .and().a_meaningful_edit_was_made();

        when().the_edit_is_made();

        then().the_edit_is_stored()
                .and().the_undo_option_is_available();
    }

    @Test
    public void minor_changes_should_not_be_reversible() {
        given().an_empty_edit_history()
                .and().a_minor_edit_was_made();

        when().the_edit_is_made();

        then().the_edit_is_not_stored()
                .and().the_undo_option_is_not_available();
    }

    @Test
    public void undo_is_not_possible_without_making_any_changes() {
        given().an_empty_edit_history();

        then().the_undo_option_is_not_available();
    }

    @Test
    public void redo_is_not_possible_without_undoing_first() {
        given().an_empty_edit_history();

        then().the_redo_option_is_not_available();
    }

    @Test
    public void undoing_a_meaningful_edit_should_enable_redo() {
        given().an_empty_edit_history()
                .and().a_meaningful_edit_was_made();

        when().the_edit_is_made()
                .and().the_undo_action_is_executed();

        then().the_edit_is_stored()
                .and().the_undo_option_is_not_available()
                .and().the_redo_option_is_available();
    }

    @Test
    public void redo_should_restore_the_previous_state() {
        given().an_edit_history_with_an_undone_edit();

        when().the_redo_action_is_executed();

        then().the_redo_option_is_not_available();
    }
}
