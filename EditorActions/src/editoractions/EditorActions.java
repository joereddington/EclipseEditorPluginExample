package editoractions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;

import consoleActions.ConsoleActions;

/*
 * Useful to know about CurrentFileRealName:whoami.txt
 * RealFilePath:/Users/josephreddington/Documents/junit-workspace/testProject/whoami.txt
 * WorkingDirectory:/Users/josephreddington/Documents/junit-workspace/testProject In the sleep
 * function
 */

public class EditorActions {

  /**
   * This returns the contents of the current editor as a string.
   * 
   * @return contents of the current editor as a string.
   */
  public static String editorContents() {
    return editorContents((TextEditor) getEditor());
  }

  public static IEditorPart getEditor() {
    IWorkbenchWindow activeWorkbenchWindow = getWorkBenchWindow();
    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
    IEditorPart part = activePage.getActiveEditor();
    return part;
  }

  /**
   * This returns the contents of the current editor tab, as a string. if passed null, it throws a
   * null pointer exception implicitly
   * 
   * @return contents of the current editor as a string.
   */
  public static String editorContents(TextEditor editor) {
    IDocumentProvider prov = editor.getDocumentProvider();
    IDocument doc = prov.getDocument(editor.getEditorInput());
    return doc.get();
  }

  // TODO: this needs three test cases one where it's passed the default editor, one where it's
  // passed a nondefault editor, and one where there is no editors alive.


  protected static IWorkbenchWindow getWorkBenchWindow() {
    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (null == activeWorkbenchWindow) {
      System.err
          .println("Null Pointer error of the type that suggests this hasn't been executed from a the right thread");
      throw new NullPointerException();
    }
    return activeWorkbenchWindow;
  }



  /**
   * Returns the file current viewed in the editor as an IFile Object so it can be passed to other
   * calls. If there is NO such file it will throw an Assertion Error.
   * 
   * @return
   */
  public static IFile getFile() {
    IFile file = null;
    IWorkbenchWindow win = getWorkBenchWindow();
    IWorkbenchPage page = win.getActivePage();
    if (page != null) {
      IEditorPart editor = page.getActiveEditor();
      if (editor != null) {
        IEditorInput input = editor.getEditorInput();

        if (input instanceof IFileEditorInput) {
          file = ((IFileEditorInput) input).getFile();
        } else if (input instanceof FileStoreEditorInput) {
          // What goes here?
        }
      }
    }
    if (file == null) {
      throw new AssertionError("null file returned");
    }
    return file;
  }


  public static void setEditorContents(String in, TextEditor editor, final int newCursorPosition) {
    IDocumentProvider prov = editor.getDocumentProvider();
    IDocument doc = prov.getDocument(editor.getEditorInput());
    int currentCursorPosition = getcursorPosition(editor);
    ConsoleActions.debug("Current positions is:" + currentCursorPosition);
    doc.set(in);
    editor.getSelectionProvider().setSelection(
        new OurSelectionProvider(newCursorPosition, currentCursorPosition));
  }

  /**
   * Replaces the contents of an editor with the input string s *
   * 
   * @param in The replacement string to display in the editor
   */
  public static void setEditorContents(String in) {
    TextEditor editor = (TextEditor) getEditor();
    IDocumentProvider prov = editor.getDocumentProvider();
    IDocument doc = prov.getDocument(editor.getEditorInput());
    doc.set(in);
  }


  /**
   * Returns the cursor location for a given editor, cursor position is an int defined as the number
   * of times you would have to press the left arrow key to get to the start of the file
   * 
   * @param editor
   * @return
   */
  public static int getcursorPosition(TextEditor editor) {
    ISelection selection = editor.getSelectionProvider().getSelection();
    ITextSelection its = (ITextSelection) selection;
    return its.getOffset();
  }

  /**
   * Returns the cursor location for a given editor, cursor position is an int defined as the number
   * of times you would have to press the up arrow key to get to the start line of the file
   * 
   * @param editor
   * @return
   */
  public static int getcursorLine(TextEditor editor) {
    ISelection selection = editor.getSelectionProvider().getSelection();
    ITextSelection its = (ITextSelection) selection;
    return its.getStartLine();
  }

  /**
   * For a given editor sets the cursor position to input int. Cursor position is defined as the
   * number of times you would have to press the left arrow key to get to the start of the file
   * 
   * @param editor
   * @return
   */

  public static void setcursorPosition(TextEditor editor, int position) {
    editor.getSelectionProvider().setSelection(new OurSelectionProvider(position, 0));
  }


  /**
   * Returns the currently selected text in the currently open editors what happens if there is no
   * text selected.
   * 
   * @return the currently selected text in the currently open editors
   */
  public static String getCurrentSelection() {
    IEditorPart part =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (part instanceof ITextEditor) {
      ISelection sel = ((ITextEditor) part).getSelectionProvider().getSelection();
      if (sel instanceof TextSelection) {
        ITextSelection textSel = (ITextSelection) sel;
        return textSel.getText();
      }
    }
    return "No string";// was return null;
  }

  /**
   * Returns the name of the file currently in the editor, for example 'whoami.txt' or 'Hello.java'
   * 
   * @return the name of the file
   */
  public static String getCurrentFileRealName() {
    String name =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
            .getEditorInput().getName();
    ConsoleActions.debug(name);
    return name;

  }

  /**
   * Returns the working directory for the file currently in the editor, so if the file path is
   * '/Users/josephreddington/Documents/junit-workspace/testProject/whoami.txt ' then the working
   * directory is '/Users/josephreddington/Documents /junit-workspace/testProject' (no slash)
   * 
   * @return the working directory as a string
   */
  public static String getCurrentFileWorkingDirectory() {
    String temp = getCurrentFileRealPath();
    return temp.substring(0, temp.lastIndexOf("/"));
  }

  public static String getCurrentFilePathFromProject() {
    String fullPath = getCurrentFileRealPath();
    String projectName = getCurrentProject().getName();
    String pathFromProject =
        fullPath.substring(fullPath.lastIndexOf(projectName) + projectName.length(),
            fullPath.length());

    return pathFromProject;
  }

  public static String getCurrentFilePathToProject() {
    String fullPath = getCurrentFileRealPath();
    String projectName = getCurrentProject().getName();
    String pathToProject =
        fullPath.substring(0, fullPath.lastIndexOf(projectName) + projectName.length());

    return pathToProject;
  }

  public static IProject getCurrentProject() {
    // TODO add the (if file null) part from below, but test first
    return getFile().getProject();
  }

  /**
   * Returns the absolute location of the file currently in the editor. For example
   * '/Users/josephreddington/Documents/junit-workspace/testProject/whoami.txt
   * 
   * @return the absolute location of the file, as a string
   */
  public static String getCurrentFileRealPath() {
    IFile file = getFile();
    if (file != null) {

      return file.getLocation().toOSString();
    }
    return null;
  }

  /**
   * Creates a new file, with the contents given in the newContentsParameter, in the same location
   * as a givenfile, with the same name except for a different extension. Used to do such things as
   * create 'temp.vcg' from 'temp.str'
   * 
   * @param file
   * @param newContents
   * @param newExtension
   * @return
   */
  public static IFile dublicateFileWithNewExtension(IFile file, String newContents,
      String newExtension) {
    String projectName = getCurrentProject().getName();
    String fullPath = file.getLocation().toOSString();
    String pathFromProject =
        fullPath.substring(fullPath.lastIndexOf(projectName) + projectName.length(),
            fullPath.length());
    String newFilePathFromProject =
        pathFromProject.substring(0, pathFromProject.lastIndexOf(".")) + newExtension;
    return createFile(newContents, newFilePathFromProject, projectName);
  }

  /**
   * This creates a new file with the given contents and the particular path. This automatically
   * overwrites previous files.
   * 
   * @param contents the contents of the file.
   * @param path the path WITHIN THE PROJECT that the file should sit at
   * @param projectName the name of the containing project
   */
  // TODO this automatically overwrites previous files - should make that at
  // least optional.
  public static IFile createFile(String contents, String path, String projectName) {
    IWorkspace ws = ResourcesPlugin.getWorkspace();
    IProject project = ws.getRoot().getProject(projectName);
    System.out.println(project.getFullPath());
    IFile file = project.getFile(path);
    InputStream source = new ByteArrayInputStream(contents.getBytes());
    try {
      file.create(source, false, null);
    } catch (CoreException e) {
      try {
        file.setContents(source, false, false, null);
      } catch (CoreException e1) {
        e1.printStackTrace();
      }
    }
    return file;
  }

  public static IFolder createFolder(String path) {
    IWorkspace ws = ResourcesPlugin.getWorkspace();
    IProject project = ws.getRoot().getProject(getCurrentProject().getName());
    Path myPath = new Path(path);
    IFolder folder = project.getFolder(myPath);
    if (!folder.exists())
      try {
        folder.create(IResource.NONE, true, null);
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
      }
    return folder;
  }

  /**
   * Creates a 'red cross' flag on the current file, at the line number specified, with the
   * hover-message specificed.
   * 
   * Remember that linenumber starts at 1!
   */
  public static void createMarkerForResource(int linenumber, String message) throws CoreException {
    IResource resource = getFile();
    createMarkerForResource(resource, linenumber, message);
  }

  /**
   * Creates a 'red cross' flag on the given file, at the line number specified, with the
   * hover-message specificed.
   * 
   * Remember that linenumber starts at 1!
   */
  public static void createMarkerForResource(IResource resource, int linenumber, String message)
      throws CoreException {

    if (linenumber == 0) {
      linenumber = 1;
    }
    HashMap<String, Object> map = new HashMap<String, Object>();
    MarkerUtilities.setLineNumber(map, linenumber);
    MarkerUtilities.setMessage(map, message);
    map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    MarkerUtilities.createMarker(resource, map, IMarker.PROBLEM);
  }


  /**
   * Removes all markers (red crosses, ect) from resource.
   * 
   * @param resource
   * @throws CoreException
   */
  public static void removeAllMarkersForResource(IDocument resource) throws CoreException {
    IMarker[] markers = ((IResource) resource).findMarkers(null, true, IResource.DEPTH_INFINITE);
    for (IMarker marker : markers) {
      marker.delete();
    }
  }

  /**
   * Removes all markers (red crosses, ect) from resource.
   * 
   */
  public static void removeAllMarkersForResource() throws CoreException {
    IResource resource = getFile();
    IMarker[] markers = resource.findMarkers(null, true, IResource.DEPTH_INFINITE);
    for (IMarker marker : markers) {
      marker.delete();
    }
  }

  /**
   * Returns the line number, given a character number.
   * 
   * @param editorContents
   * @param target
   * @return
   */
  public static int getLineNumberForCharNumber(String editorContents, int target) {
    // TODO: if this http://www.plancomps.org/bug/bugzilla-4.4/show_bug.cgi?id=121 isn't a bug then
    // I have to make this function count newlines as two characters
    String[] lines = editorContents.split("\\n", -1);
    int charsLeft = target - 1;
    for (int i = 0; i < lines.length; i++) {
      if (charsLeft < lines[i].length()) {
        return i;
      }
      charsLeft = charsLeft - lines[i].length();
    }
    return -1;// because there are more chars than lines to hold them..
  }


  public static int linesInEditor(String editorContents) {

    String[] lines = editorContents.split("\\n", -1);
    // the -1 maintains empty elements (otherwise extra lines at end of file wouldn't be counted.

    return lines.length;
  }

  /**
   * 
   * Returns the line number, given a character number.
   * 
   * @param editor
   * @param target
   * @return
   */
  public static int getLineNumberForCharNumber(TextEditor editor, int target) {
    String editorContents = editorContents(editor);
    String[] lines = editorContents.split("\\n");
    int charsLeft = target;
    for (int i = 0; i < lines.length; i++) {
      if (charsLeft <= lines[i].length()) {
        return i;
      }
      charsLeft = charsLeft - lines[i].length();
    }
    return -1;// because there are more chars than lines to hold them..
  }

}
