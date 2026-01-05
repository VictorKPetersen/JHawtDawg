= Concept Location
Concept location helps the developers find which part of the code implements a feature.
This is helpfull when adding a new feature as it is important to modify and extend the correct abstractions.
Additionally it helps find a set of starting classes, from which impact anylsis can be run,
giving a great understand of the scope of a particular change request.

@fig:concept_location_act_diag shows an activity diagram, of going from having a change requst.
To understanding the concept location and which parts of the code are relevant.

#figure(
  image("resources/conceptLocationActivity.svg", width: 85%),
  caption: [Activity diagram of concept location process.],
) <fig:concept_location_act_diag>

For finding the concepts involved in the Undo/Redo change request, 
an iterative approach was applied treating both parts of the feature as sepperate entities.
This means the process was repeated for both undo aswell as redo.
For each of these processes the general methodology focusses on finding some part of the code
where the feature might logically live, and then searching that immediate area for relevant code.

Finding the initial relevant class is achieved by searching the codebase for simmilar and relevant features.
Searching a codebase can be done through many means, a common tool found on most POSIX system would be grep.
Finding the relevant classes for undo and redo was relativly easy,
as there is a file called *UndoRedoManager* which is a great startpoint for concept location.

With the initial file found, I used the debugger to place stops around the code.
I then ran the program and executed a undo/redo action in an attempt to see what functions where called when.
This allowed me to branch out into other relevant files which were called as part of the undo/redo action.

This is effectivly an iterative process where I continued to find new break points for my debugger,
and continued running the relevant part of the application untill no more new files were being discovered.

@tab:concept_location shows the files visited during concept location,
along with what the responsibilty of each class is.
Generally the Undo/Redo feature goes through a couple of classes,
it has a manager which is responsible for calling the actions.
Both Undo and Redo feature has a relevant Action class which extends the abstact Action class.
Addtionally some classes related to the ui are present as they define the buttons for the both actions.

#figure(
table(
    columns: (auto, 1fr),
    align: (center, left),
    table.header[*Domain Class*][*Responsibility*],
    [UndoAction], [
    - Handle action (safely call *UndoRedoManager.undo()*). \
    - Configure button label (via *ResourceBundleUtil*).
    ],
    [RedoAction], [
    - Handle action (safely call *UndoRedoManager.redo()*). \
    - Configure button label (via *ResourceBundleUtil*).
    ],
    [UndoRedoManager], [
    - Manage undo/redo logic for edits. \
    - Control when undo/redo buttons light up and become pressable. \
    - Define behavior when undo/redo is executed (invoked by Action classes).
    ],
    [ActionsToolBar], [
    - Create buttons for ActionsToolBar. \
    - Determine which *UndoRedoManager* to use for buttons.
    ],
    [SVGDrawingPanel], [
    - Create the drawing panel. \
    - Instantiate the *UndoRedoManager* and pass it down through the chain.
    ],
    [ResourceBundleUtil], [
    - Provide resources associated with features/buttons (eg. undo and redo icons).
    ],
  ),
  caption: [Resulting classes from change request found during Concept Location.],
) <tab:concept_location>
