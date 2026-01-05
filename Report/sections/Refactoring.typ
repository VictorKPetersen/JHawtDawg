= Refactoring

During anylisis of the *UndoRedoManager* multiple pieces of code which are no longer used within the program was discovered.
Unused code is a code smell as it adds no value to the actual program,
while still managing to confuse maintainers as they have to filter out the irrelevant code.
It is better to remove the code instead. If it is needed in the future
then it can still be found in version control, or a new implementation can be written.

#figure(```java
  public void setLocale(Locale l) {
    labels = ResourceBundleUtil.getBundle("org.jhotdraw.undo.Labels", l);
  }
```, caption: [Unused code for setting locale in *UndoRedoManager*.]) <lst:undo_redo_locale>

Fixing this issue is done by using the "Remove Dead Code" strategy, which simlpy entails removing the code.
@lst:undo_redo_locale shows the offending code before it was removed.

#figure(```java
    public static final UndoableEdit DISCARD_ALL_EDITS = new AbstractUndoableEdit() {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public boolean canRedo() {
            return false;
        }
    };
```, caption: [Unused code for sending an edit which cannot be undone.]) <lst:undo_redo_undoable>

Fixing the code shown in @lst:undo_redo_undoable, is also as simple as removing it.
It serves no functionallity within the program and is likely an artifact of a developer manually testing something at some point.

In *UndoRedoManger* there was also a set of 3 methods which where all structurede exactly the same,
and whose purpose is to function in a simmilar fashion.
This is a bad code smell where code is duplated which also violates the Don't Repeat Yourself Principles.
The duplicate code can be seen in @lst:undo_redo_duplicate.
The planned change is to refactor the logic of each method out into a singular abstraction, then each specefic method can call that abstraction.
This ensures that if any common changes are to be made in the future, it only has to change in one place.
An example could be if logging functionallity is required, currently that code would have to be written in 3 places, making it easy to forget one.
#figure(```java
   @Override
    public void undo() throws CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.undo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

   @Override
    public void redo()
            throws CannotUndoException {
        undoOrRedoInProgress = true;
        try {
            super.redo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

   @Override
    public void undoOrRedo()
            throws CannotUndoException, CannotRedoException {
        undoOrRedoInProgress = true;
        try {
            super.undoOrRedo();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }
```, caption: [Duplicate Code refactored into singular abstraction.]) <lst:undo_redo_duplicate>

@lst:undo_redo_duplicate_after shows the code after refactoring.
This is an example of Extract Method, where the functionallity from multiple methods are extracted into a singular one.
This specefic case is a little unique as it requires an addtional abstraction, instead of just statically extracting the code.

#figure(```java
    @FunctionalInterface
    private interface UndoRedoOperation {
        void execute() throws CannotUndoException, CannotRedoException;
    }

    private void executeUndoRedoOperation(UndoRedoOperation operation)
      throws CannotUndoException, CannotRedoException {
        undoOrRedoInProgress = true;
        try {
            operation.execute();
        } finally {
            undoOrRedoInProgress = false;
            updateActions();
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        executeUndoRedoOperation(super::undo);
    }

    @Override
    public void redo() throws CannotUndoException {
        executeUndoRedoOperation(super::redo);
    }

    @Override
    public void undoOrRedo() throws CannotUndoException, CannotRedoException {
        executeUndoRedoOperation(super::undoOrRedo);
    }
```, caption: [Duplicate code refactored into common interface.]) <lst:undo_redo_duplicate_after>

@lst:svgpanel_read shows a code snippet of the read method on *SVGDrawingPanel*.
The code is fairly long, has many layers of nesting and relies on comments to explain certain gaps in the code
(e.g "suppress silently" or "We get here if reading was succesful").
All of these problems leads to code which is hard to read, and prone to bugs.
The method *read* is also overloaded to do different things based on given arguments,
this implementation contains signifcant amounts of duplicated code.
Refactoring code which does not directly correlate to the undo redo feature at hand,
might seem like a waste of time, however it is important to consider how this code plays into the feature at large.
By refactoring it now, the change becomes easier to implement as we better understand how the code functions and flow.
This will in turn give us greater reasurance as to why it works or fails.

The planned refactoring is a "extract methods" where the common parts of the methods are extracted into its own seperate methods.
This increases reusability and makes both methods significantly easier to read and understand.
Looking at the method signature, the URI object is called f.
This is likely shorthand for something, however it is unreadable for a developer without insider knowledge.
It can be refactored by changing the name to something appropiate like uri.

#figure(```java
      public void read(URI f) throws IOException {
        Drawing newDrawing = createDrawing();
        if (newDrawing.getInputFormats().size() == 0) {
            throw new InternalError("Drawing object has no input formats.");
        }
        IOException firstIOException = null;
        for (InputFormat format : newDrawing.getInputFormats()) {
            try {
                format.read(f, newDrawing);
                final Drawing loadedDrawing = newDrawing;
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        setDrawing(loadedDrawing);
                    }
                };
                if (SwingUtilities.isEventDispatchThread()) {
                    r.run();
                } else {
                    try {
                        SwingUtilities.invokeAndWait(r);
                    } catch (InterruptedException ex) {
                        // suppress silently
                    } catch (InvocationTargetException ex) {
                        InternalError ie = new InternalError("Error setting drawing.");
                        ie.initCause(ex);
                        throw ie;
                    }
                }
                // We get here if reading was successful.
                return;
            } catch (IOException e) {
                // We get here if reading failed.
                // We only preserve the exception of the first input format,
                // because that's the one which is best suited for this drawing.
                if (firstIOException == null) {
                    firstIOException = e;
                }
            }
        }
        throw firstIOException;
    }

      /** Overloaded Method Signature **/
    public void read(URI f, InputFormat format) throws IOException {}
```, caption: [Example of a long method, which has too many responsibilities.]) <lst:svgpanel_read>

The final result of both of the *read* methods can be seen in @lst:svgpanel_read_after.
The factored out methods are not included, as one should ideally be capable of understanding the code by simply reading the refactored methods.

#figure(```java
 public void read(URI uri) throws IOException {
        Drawing newDrawing = createDrawing();
        validateDrawing(newDrawing);

        IOException firstIOException = null;
        for (InputFormat format : newDrawing.getInputFormats()) {
            try {
                format.read(uri, newDrawing);
                updateDrawingOnEDT(newDrawing);
                return;
            } catch (IOException e) {
                if (firstIOException == null) firstIOException = e;
            }
        }
        throw firstIOException;
    }

    public void read(URI uri, InputFormat format) throws IOException {
        if (format == null) {
            read(uri);
            return;
        }

        Drawing newDrawing = createDrawing();
        validateDrawing(newDrawing);

        format.read(uri, newDrawing);
        updateDrawingOnEDT(newDrawing);
    }
```, caption: [Read methods after refactoring.]) <lst:svgpanel_read_after>

Another simmilar problem is present in the same class, within the write methods.
These methods once again share large parts duplicate code, use bad names for arguments and do too much for a singular method.
This can be seen in @lst:svgpanel_write, once again there is an overloaded method accepting a format.

Simmilarly to before, the refactoring will consist of extracting duplicate parts of the code, and cleaning up the bad parameter names.
At the same time moving the guard clause into tis own method to make easier to understand the validation part of the logic.

#figure(```java
    public void write(URI uri) throws IOException {
        // Defensively clone the drawing object, so that we are not
        // affected by changes of the drawing while we write it into the file.
        final Drawing[] helper = new Drawing[1];
        Runnable r = new Runnable() {
            @Override
            public void run() {
                helper[0] = (Drawing) getDrawing().clone();
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (InterruptedException ex) {
                // suppress silently
            } catch (InvocationTargetException ex) {
                InternalError ie = new InternalError("Error getting drawing.");
                ie.initCause(ex);
                throw ie;
            }
        }
        Drawing saveDrawing = helper[0];
        if (saveDrawing.getOutputFormats().size() == 0) {
            throw new InternalError("Drawing object has no output formats.");
        }
        // Try out all output formats until we find one which accepts the
        // filename entered by the user.
        File f = new File(uri);
        for (OutputFormat format : saveDrawing.getOutputFormats()) {
            if (format.getFileFilter().accept(f)) {
                format.write(uri, saveDrawing);
                // We get here if writing was successful.
                // We can return since we are done.
                return;
            }
        }
        throw new IOException("No output format for " + f.getName());
    }
```, caption: [One of the write methods before refactoring.]) <lst:svgpanel_write>

An example of one of the refactored write methods can be seen in @lst:svgpanel_write_after.

#figure(```java
    public void write(URI uri) throws IOException {
        Drawing drawingSnapshot = getDrawingSnapshotFromEDT();
        validateOutputFormats(drawingSnapshot);

        File file = new File(uri);
        for (OutputFormat format : drawingSnapshot.getOutputFormats()) {
            if (format.getFileFilter().accept(file)) {
                format.write(uri, drawingSnapshot);
                return;
            }
        }
        throw new IOException("No output format for " + file.getName());
    }
```, caption: [One of the write methods after refactoring.]) <lst:svgpanel_write_after>


== Clean Code and Solid
Clean code is a set of principles which govern how to write code that is easy for other developers to understand.
When writing software we should strive to create code that others can easily read and understand,
this increases the likelyhood that they can maintain the software should that be needed.
A common set of princples often talked about within Clean code is the SOLID principles which are a set of principles for writing OO code.
The goal of SOLID is to ensure code remains readable, testable and extendable.

#figure(```java
 public void write(URI uri, OutputFormat format) throws IOException {
        if (format == null) {
            write(uri);
            return;
        }

        Drawing drawingSnapshot = getDrawingSnapshotFromEDT();
        format.write(uri, drawingSnapshot);
    }
```, caption: [Example of SOLID principles in refactored code.]) <lst:write_solid>

@lst:write_solid shows an example of solid principles in the previously refactored code,
in @tab:write_solid the explanations for how each principle is followed in this specefic example can be found.

#figure(
  table(
    columns: (auto, 1fr),
    align: (center, left),
    table.header[*Principle*][*Reason*],
    [*S*], [
    The method only has one reason to change, this being the high level calling of the format's write method.
    The implementation writing has been delegated to another more appropiate place.],
    [*O*], [
    The method relies upon the *OutputFormat* abstraction, this makes it easy to extend functionallity simply by implementing the *OutputFormat* class.],
    [*L*], [
    The method does not make a distinction between what implementation the *OutputFormat* has, it only asserts that if it is null then we use the simplier method.],
    [*I*], [
    Since the code does not define a interface, it can't really adhere to this principle.
    However it could be made more specefic by accepting in an abstraction for format which only features the *write* method, as that is the only one used.],
    [*D*], [
    As the method depends upon an abstraction and not a concrete class it follows this principle.
    This is part of how the method also adheares to the open close principle.
    ],
  ), caption: [SOLID explanations for @lst:write_solid]
) <tab:write_solid>

An overview of the *OutputFormat* abstraction ands the abstractons which extend it can be seen in @fig:interfaces_ocp.
This clearly shows how the open close principle comes to life by allowing any number of child classes or interfaces to derive from it.
Additionally it hightlights the use of Liskov substituion principle where *SVGZOutputFromat* can replace both *SVGOutputFormat* and *OutputFormat*
because itself follows the contracts defined within them.


#figure(
  image("resources/interfaceHierachy.svg", width: 85%),
  caption: [Interfaces in case study adhearing to both open close and Liskov substituion principles],
) <fig:interfaces_ocp>

== Clean architecture
Clean architecture is an architectual pattern that attempts to decouple the ui, database, external frameworks, etc. from the core buiness logic.
The idea is that the parts of the code which changes the most should be "plugged in to" the code that does not change.
This creates a system where developers can easily switch between differnt frameworks, without impacting the actual implementation of its buiness rules.
Additionally by not having the buiness rules depend on other external factors, it creates a system where testing can be done independent of the ui, databases or external factors.
All in all this helps ensure that the program is maintanable over time, even when frameworks or ui principles becomes abandoned.

JHotdraw follows some parts of clean architecture, with different parts of the application being split into their own seperate modules.
This does create a nice seperation, however it does not properly follow clean architecture as multiple parts of the code depend upon the ui framework swing.
An example of this can be seen in @lst:no_clean_arch where the *UndoRedoManger* depends upon the swing framework.
This violates the idea that the undo redo logic should be independent from a specefic ui framework.
This does also mean that, if the swing framework had to be removed from the application it would require large scale refactorings to large parts of the buiness logic.

#figure(```java
  public class UndoRedoManager extends UndoManager {
    //javax.swing.undo.UndoManager
    }
```, caption: [Swing dependency in non UI utillity class.]) <lst:no_clean_arch>
