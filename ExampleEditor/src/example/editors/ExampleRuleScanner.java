package example.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ExampleRuleScanner extends RuleBasedScanner {
  private static Color COMMENT_COLOR = new Color(Display.getCurrent(), new RGB(63, 127, 95));
  private static Color keyworldColor = new Color(Display.getCurrent(), new RGB(127, 00, 85));
  private static Color stringColor = new Color(Display.getCurrent(), new RGB(42, 00, 255));
  private static Color purpleStringColor = new Color(Display.getCurrent(), new RGB(134, 107, 255));
  private static Color backquoteColor = new Color(Display.getCurrent(), new RGB(255, 72, 30));


  public ExampleRuleScanner() {
    artRules();
  }

  public void artRules() {
    IToken commentToken = new Token(new TextAttribute(COMMENT_COLOR));
    IToken keyword = new Token(new TextAttribute(keyworldColor));
    IToken stringToken = new Token(new TextAttribute(stringColor));
    IToken darkStringToken = new Token(new TextAttribute(purpleStringColor));
    IToken backquoteToken = new Token(new TextAttribute(backquoteColor));


    List<IRule> rules = new ArrayList<IRule>();
    IWordDetector iWordDetector = new IWordDetector() {
      @Override
      public boolean isWordStart(char arg0) {
        // return false;
        return Character.isLetter(arg0);
      }

      @Override
      public boolean isWordPart(char arg0) {
        return Character.isLetter(arg0) || arg0 == '_';
      }
    };
    rules.add(new EndOfLineRule("//", commentToken));
    rules.add(new EndOfLineRule("#", commentToken));

    rules.add(new WordPatternRule(iWordDetector, "foo", "", keyword));
    rules.add(new WordPatternRule(iWordDetector, "bar", "", keyword));
    rules.add(new WordPatternRule(iWordDetector, "`", "", backquoteToken));

    rules.add(new SingleLineRule("'", "'", stringToken));
    rules.add(new SingleLineRule("\"", "\"", darkStringToken));

    rules.add(new SingleLineRule("(*", "*)", commentToken));

    setRules(rules.toArray(new IRule[rules.size()]));
    return;
  }
}
