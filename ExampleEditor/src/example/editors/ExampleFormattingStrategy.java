package example.editors;

import org.eclipse.jface.text.formatter.IFormattingStrategy;




public class ExampleFormattingStrategy implements IFormattingStrategy {

  ExampleEditor editor;

  public ExampleFormattingStrategy(ExampleEditor editor) {
    this.editor = editor;
  }

  @Override
  public void formatterStarts(String initialIndentation) {

  }

  @Override
  public String format(String content, boolean isLineStart, String indentation, int[] positions) {

    // for each member of positions, get the responsible node in the tree, and then find it's new
    // position.
      return content;
   }

  @Override
  public void formatterStops() {

  }

}
