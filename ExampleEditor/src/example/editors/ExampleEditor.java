package example.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.editors.text.TextEditor;


public class ExampleEditor extends TextEditor {

 
  public ExampleEditor() {
    super();
    setSourceViewerConfiguration(new ExampleSourceViewerConfig(this));

  }


  public void reformat() {
    SourceViewer sourceViewer = (SourceViewer) getSourceViewer();
    sourceViewer.doOperation(ISourceViewer.FORMAT);

  }



  public void refreshOutline() {
    // if (fOutlinePage != null) {
    // fOutlinePage.refresh();
    // } else {
    // throw new AssertionError();
    // }

  }
}
