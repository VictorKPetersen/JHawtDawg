= Vertification

== Assertions
Assertions are used to test that some code enver reaches a specefic state.
The idea is that if an assertion evaluates to false then the program crashes.
Hopefully the developers or QA personell can catch this before pushing the code.


#figure(```java
      private void executeUndoRedoOperation(UndoRedoOperation operation)
        throws CannotUndoException, CannotRedoException {
          assert !undoOrRedoInProgress : "An UndoRedoOperation is already in progress.";
          undoOrRedoInProgress = true;
          try {
              operation.execute();
          } finally {
              undoOrRedoInProgress = false;
              updateActions();
          }
    }
```, caption: [Asserting that no undo or redo is already in progress.]) <lst:assert_undo_redo>

@lst:assert_undo_redo shows an example of asserting something that must never be true.
The *undoOrRedoInProgress* flag must be false when we enter the function, else we might end up in a recursive scenario.
By adding the assertion, the likelyhood a bug which causes this to trigger is caught increases.

== Tests
Unit tests are small tests, which test the indiviudal parts of a class.
This will most often present itself as test for methods where each test,
ensures a specefic outcomes or results happens from said method.
A well implemented test suite can help further refactorings by providing a set of critera for how a piece of code should act or perform.

Testing the *UndoRedoManger* should focus primarily on each indiviudal method and what side effects it might introduce into the class.
It should test and validate the side effects of the methods, ensuring it is always left in a predicatble state.
Addtionally mocks are important for ensuring that only one particular part of the code is being tested.
This ensures that tests for *UndoRedoManger* does not fail because an error is introduced somewhere else.


#figure(```java
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
}
```, caption: [Instances and *setUp* method for *UndoRedoMangerTest*.]) <lst:test_undo_redo>

To allow for multiple test, a *setUp()* method which declares the necesarry variables and mocks are defined.
This makes the code easier to read, and follows the DRY principles.
@lst:test_undo_redo shows the code which mockes the necesarry classes and creates an instance of the system under test.
Addtionally the mocks for non relevant parts of the code can be seen, ensuring that only the *UndoRedoManger* is being tested.

#figure(```java
      @Test
    public void addSignificantEditUpdatesInstance() {
        instance.addEdit(significantEdit);

        assertTrue(instance.hasSignificantEdits());
    }
```, caption: [Test asserting that the *hasSignificantEdit* flag is set properly.]) <lst:test_sigedit_flag>

@lst:test_sigedit_flag shows a test which ensures that the instance has the correct flag set to true after adding a significant edit.
The test does not make any assumptions about how that flag is set, just that it must be set.

#figure(```java
      @Test
    public void discardAllEditsResetsState() {
        instance.addEdit(significantEdit);
        instance.discardAllEdits();

        assertFalse(instance.hasSignificantEdits());
        assertFalse(instance.canUndo());
    }
```, caption: [Test asserting that the internal state of the instance is correct after discarding edits.]) <lst:test_discard_state>

Another example of ensuring a specefic internal state after a method call can be seen in @lst:test_discard_state.
This checks for the logic that after having discard all edits, it should not be possible to either have a signifacnt edit, nor undo an edit.
Checking the side effects like this, does introduce an issue where how the object works is now defined.
If in the future the object was to be refactored to include less side effects, then the test would need to be refactored aswell.
This is generally acceptable as the test which use old functionallity should also be refactored or removed when refactoring code.
This plays into the idea that test are "first class citizens" i.e. it is as important as production code.

#figure(```java
      @Test
    public void addSignificantEditReturnsTrue() {
        boolean result = instance.addEdit(significantEdit);
        assertTrue(result);
    }
```, caption: [Test ensuring that adding an edit runs correctly.]) <lst:test_edit_return_true>


Because code that tests side effect may be refactored in the future,
it is important to also test how the code itself behaves.
@lst:test_edit_return_true shows how that is tested for the code present in @lst:test_sigedit_flag.
By doing this both the side effects and the code excetion is tested,
this will also lead to better test logs for troubleshooting if necesarry.


#figure(```java
      @Test(expected = CannotUndoException.class)
    public void undoWithEmptyStackThrowsException() {
        instance.undo();
    }

      @Test(expected = CannotRedoException.class)
    public void redoWithEmptyStackThrowsException() {
        instance.redo();
    }
  ```, caption: [Test asserting that the correct exceptions are thrown with bad input.]) <lst:test_bad_input>

Simply testing the best case scenario is not enought, a test suite should also contain tests for the worst case / bad input scenario.
This often comes down to testing that the right exception or errors are thrown when something goes wrong.
In @lst:test_bad_input is an example of tests that cover what exceptions should be thrown when undo/redo is called on an non existing edit.

All of the unit tests together ensure that the code which is being tested runs in a predetermined and predicatble way.

== Behavior Driven Development scenarios
Beahvior Driver Development is a development process where the user wan'ts and their behaivor are put first.
It is done by translating user stories into Given-When-Then scenariosm which can then be tested in code.
These test care more about how the systems functions from the user perspective than the internal affairs of each class.

As an example the initial user story of the undo redo change request is as follows:

#align(center)[
  #block(
    fill: luma(230),
    inset: 8pt,
    radius: 4pt,
    [As a user, i want the ability to undo and redo my actions so that i can recover from mistakes
    and return to a previous working state.]
  )
]

This can be turned into multiple given-when-then scenarios

#figure([
#columns(2, gutter: 8pt)[
#block(
    radius: 4pt,
    stroke: gray,
    inset: (x: 15pt, y: 10pt)
  )[
    #text(weight: "bold", size: 1.2em)[Scenario 1]
    #block(inset: (x: 0pt, y: 8pt), below: 0.5em)[
      #text(fill: red, [Given]) the user has a new drawing, \
      and the user performs and edit. \
      #text(fill: red, [When]) the user selects undo. \
      #text(fill: red, [Then]) the edit should be undone, \
      and the redo option should become available.
    ]
  ]

  #colbreak()
  #block(
    radius: 4pt,
    stroke: gray,
    inset: (x: 15pt, y: 10pt)
  )[
    #text(weight: "bold", size: 1.2em)[Scenario 2]
    #block(inset: (x: 0pt, y: 8pt), below: 0.5em)[
      #text(fill: red, [Given]) the user has a drawing with an undone edit. \
      #text(fill: red, [When]) the user selects redo. \
      #text(fill: red, [Then]) the redo should be done, \
    ]
  ]
]], caption: [Examples of Given-When-Then Scenario translated from a user story.]) <fig:given_when_then_scene>

@fig:given_when_then_scene shows examples of two scenarios that are derived from the change requests user story.
It is possible to translate these scenarios into code, which can than automatically be ran along with the other tests.
This is another test type, which helps ensure that the parts of the code which users interact with are running optimally.
Additionally bdd scenarios are often more readable for non techinal people, thus these scenarios can serve as a form of documentation of the code.
Ideally these scenarios should only ever change, if the buiness goals/rules change.
Outside of that scenario they should remain static, as they describe the users path through a system.

#figure(```java
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
```, caption: [Scenario 1 translated into JGiven test.]) <lst:test_bdd_scene1>

Shows in @lst:test_bdd_scene1 is the code translated version of scenario 1.
This helps assert that when some edit is made and undone, then the redo action becomes aviable.
That is a core part of the undo redo functionallity and is something which should always happen.

#figure(```java
   public GivenUndoRedoManager an_empty_edit_history() {
        ResourceBundleUtil resourceBundleUtil = mock(ResourceBundleUtil.class);
        instance = new UndoRedoManager(resourceBundleUtil);
        return this;
    }
```, caption: [Method used to setup a given state.]) <lst:test_bdd_given_setup>

Each part of the given, when and then methods are defined in a seperate class.
This promotes code reuse, as it is defined once and then used in other classes.
An example of how *an_empty_edit_history* is setup can be seen in @lst:test_bdd_given_setup.
