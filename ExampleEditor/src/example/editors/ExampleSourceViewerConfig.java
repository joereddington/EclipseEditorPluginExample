package example.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;


public class ExampleSourceViewerConfig extends TextSourceViewerConfiguration {
  private ExampleRuleScanner scanner;
  private static Color DEFAULT_TAG_COLOR = new Color(Display.getCurrent(), new RGB(0, 0, 000));
  private ExampleEditor editor;

  public ExampleSourceViewerConfig(ExampleEditor editorIn) {
    editor = editorIn;
  }

  public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
    ContentFormatter formatter = new ContentFormatter();
    ExampleFormattingStrategy formattingStrategy = new ExampleFormattingStrategy(editor);
    formatter.setFormattingStrategy(formattingStrategy, IDocument.DEFAULT_CONTENT_TYPE);

    return formatter;
  }


  

  @Override
  public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
    return new DefaultAnnotationHover();
  }


  public ExampleRuleScanner getTagScanner() {
    if (scanner == null) {
      scanner = new ExampleRuleScanner();
      scanner.setDefaultReturnToken(new Token(new TextAttribute(DEFAULT_TAG_COLOR)));
    }
    return scanner;
  }


  /**
   * Define reconciler for MyEditor
   */
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();
    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTagScanner());
    reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
    return reconciler;
  }

  /** put in so that stuff activates on save **/



}
