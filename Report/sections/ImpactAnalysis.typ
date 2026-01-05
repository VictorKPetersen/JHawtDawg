= Impact Analysis
Impact analysis takes the initial set of classes found during concept location
and expands them to the "real" estimated impact set.
This can be done by creating an interaction diagram from the initial impact set,
and then iterativly running through said diagram marking classes as either next, inspected, propagating or changed.
The goal is to identify specefically which classes will have to be changed for the change request to be implemented.

#figure(
  image("resources/InteractionDiagram.png", width: 100%),
  caption: [Interaction Diagram showing process of marking classes (green = next, red = change, grey = unchanged.],
) <fig:impact_anlysis_diagram>

@fig:impact_anlysis_diagram shows the process of iterativly marking classes which interact with the previously identified *UndoRedoManager*.
Starting with only having that one class marked as red/change and ending with multiple classes that have been inspect and marked as either change or no change.
Addtionally the complete list of packages and the amount of classes visted can be seen in @tab:ia_visited_classes,
along with some genereal information about each package and what their responsibilities are.
This helps keep an overview of the impact along with what is relevant withing each package.

#figure(
table(
    columns: (auto, auto, 1fr),
    align: (center, left, left),
    table.header[*Package Name*][*\# of classes*][*Comments*],
    [org.jhotdraw.undo], [4], [
    This package contains the main logic for undo/redo along with the manger that controls them. \
    As such this is the crux of this feature, this layer is effectively the layer between java/javax implementation and ours.
    ],
    [org.jhotdraw.action], [1], [
      This package contains the basis of which a action is. Undo/Redo counts as a action,
      therefore this contains the necesarry implementation and contractual obligations for our new actions to work. \
      This class is unlikely to be changed because of our new feature as it is the basic abstaction multiple other features are build upon.
    ],
    [org.jhotdraw.samples.svg], [3], [
    This package contains alot of the logic required specefically for the SVG application. \
    This records the edits that have been made, and sets the specefic instance of Undo/Redo to use.
    ],
    [org.jhotdraw.samples.svg.gui], [1], [
    This package contains the logic for the ui bar, where a user can click on buttons to execute an action. \
    This is relevant as there needs to be implemented a button for the user to click.
    ]
  ),
  caption: [List of packages visited during impact analysis.],
) <tab:ia_visited_classes>

Focusing on packages means we only see the top level details,
this helps with keeping the focus on what parts of the codebase are impacted and not specefically what file/function is impacted.
