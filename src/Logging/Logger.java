package Logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import constants.ConstantHandler;
import constants.ConstantHandler.LOG_TYPES;

public class Logger {

	private static Logger log;

	private PrintWriter out;
	private boolean can;

	private Logger() {
		can = true;
		try {
			out = new PrintWriter("Log.txt");
		} catch (FileNotFoundException e) {
			can = false;
		}
	}

	public static Logger getLogger() {
		if (log == null)
			log = new Logger();
		return log;
	}

	public void log(String text, LOG_TYPES type) {

		if (!can)
			return;

		switch (type) {
		case DEBUG:
			if (ConstantHandler.LOG_DEBUG)
				out.println("DEBUG: " + System.nanoTime() + " " + text);
			break;
		case EVENT:
			if (ConstantHandler.LOG_EVENT)
				out.println("EVENT: " + System.nanoTime() + " " + text);
			break;
		case WORNING:
			if (ConstantHandler.LOG_WORNING)
				out.println("WORNING: " + System.nanoTime() + " " + text);
			break;
		case ERROR:
			if (ConstantHandler.LOG_DEBUG)
				out.println("ERROR: " + System.nanoTime() + " " + text);
			break;
		default:
			out.println("UNKNOWN: " + System.nanoTime() + " " + text);
			break;
		}
	}
}
