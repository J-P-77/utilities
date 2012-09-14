package utillib.arguments;

import utillib.collections.MyStackAll;

import utillib.file.FileUtil.Line_Ending;

import java.io.File;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SettingShared extends ArgumentsUtil {
	protected final File _FILE;
	protected final Line_Ending _FILE_ENDING;// = Line_Ending.WIN

	protected MyStackAll<Argument> _Arguments = new MyStackAll<Argument>();

	public SettingShared(File file) {
		this(file, Line_Ending.WINDOWS);
	}

	public SettingShared(File file, Line_Ending lineending) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(lineending == null) {
			throw new RuntimeException("Variable[lineending] - Is Null");
		}

		_FILE = file;
		_FILE_ENDING = lineending;
	}

	public Line_Ending getLineEnding() {
		return _FILE_ENDING;
	}

	public int findArgIndex(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return X;
				}
			}

		}

		return -1;
	}

	public Argument getArg(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return _Arguments.getItemAt(X);
				}
			}
		}

		return null;
	}

	public String findArg(String argumentname) {
		if(_Arguments != null) {
			for(int X = 0; X < _Arguments.length(); X++) {
				if(_Arguments.getItemAt(X).getName().equals(argumentname)) {
					return _Arguments.getItemAt(X).getVariable();
				}
			}
		}

		return null;
	}
}