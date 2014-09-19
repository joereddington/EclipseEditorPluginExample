package consoleActions;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


public class ConsoleActions {

	private static final String defaultConsoleName = "Joe's Console";
	private static final boolean debug = true;

	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public static String getConsoleContents() {
		return getConsoleContents(defaultConsoleName);
	}

	public static String getConsoleContents(String name) {
		MessageConsole myConsole = ConsoleActions.findConsole(name);
		IDocument doc = myConsole.getDocument();
		String consoleContents = doc.get();
		return consoleContents;
	}

	public static void writeToConsole(String string) {
		MessageConsoleStream out = getConsoleStream();
		out.println(string);
		return;
	}

	public static void out(String string) {
		if (debug) {
			System.out.println(string);
		}
		writeToConsole(string);
	}

	public static void debug(String string) {
		if (debug) {
			System.out.println(string);
			writeToConsole(string);
		}
	}

	private static MessageConsoleStream getConsoleStream() {
		return getConsoleStream(defaultConsoleName);
	}

	private static MessageConsoleStream getConsoleStream(String name) {
		MessageConsole myConsole = ConsoleActions.findConsole(name);
		MessageConsoleStream out = myConsole.newMessageStream();
		return out;
	}

}
