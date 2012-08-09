package utillib.utilities;

import utillib.utilities.AConsole;

public abstract class ADefaultConsole extends AConsole {

	@Override
	public void processLine(String line) {
		if(line.length() == 0) {
			return;
		}

		final String CMD;
		final String PARAMETER;

		final int INDEX = line.indexOf(' ');
		if(INDEX == -1) {
			CMD = line.toLowerCase();
			PARAMETER = null;
		} else {
			CMD = line.substring(0, INDEX).toLowerCase();
			PARAMETER = line.substring(INDEX + 1);
		}

		if("help".equals(CMD) || "?".equals(CMD) || "listcmds".equals(CMD)) {
			helpCmd();
		} else if("exit".equals(CMD) || "quit".equals(CMD)) {
			exitCmd();
			super.close();
		} else {
			if(!processCommand(CMD, PARAMETER)) {
				System.out.println("Command: " + CMD + " Does Not Exists");
			}
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param parameters
	 * @return true if command exists, false if not
	 */
	public abstract boolean processCommand(String cmd, String parameters);

	public abstract void helpCmd();

	public abstract void exitCmd();
}
