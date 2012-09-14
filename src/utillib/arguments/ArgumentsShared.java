package utillib.arguments;

import utillib.collections.MyStackAll;

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
public class ArgumentsShared extends ArgumentsUtil {
	protected String _CommentLineStart = "//";

	protected MyStackAll<Title> _Titles = new MyStackAll<Title>();

	public void setCommentLineStart(String commentlinestart) {
		_CommentLineStart = commentlinestart;
	}

	public String getCommentLineStart() {
		return _CommentLineStart;
	}

	public void addTitle(Title title) {
		if(title != null) {
			int TitleIndex = findTitle(title.getName());

			if(TitleIndex == -1) {
				_Titles.push(title);
			} else {
				for(int X = 0; X < title.length(); X++) {
					_Titles.getItemAt(TitleIndex).addArgument(title.getArgument(X));
				}
			}
		}
	}

	public void addTitle(Title[] titles) {
		for(int X = 0; X < titles.length; X++) {
			addTitle(titles[X]);
		}
	}

	public void addArgToTitle(String titlename, String argname, String argvariable) {
		final int INDEX = findTitle(titlename);

		if(INDEX == -1) {//Not Found
			Title TempTitle = new Title(titlename);

			TempTitle.addArgument(argname, argvariable);

			_Titles.push(TempTitle);
		} else {//Found
			_Titles.getItemAt(INDEX).addArgument(argname, argvariable);
		}
	}

	public int findTitle(String titlename) {
		for(int X = 0; X < _Titles.length(); X++) {
			if(_Titles.getItemAt(X).getName().equalsIgnoreCase(titlename)) {
				return X;
			}
		}

		return -1;
	}

	public Title getTitle(String titlename) {
		final int INDEX = findTitle(titlename);

		if(INDEX == -1) {
			return null;
		} else {
			return _Titles.getItemAt(INDEX);
		}
	}

	public int findArgument(String titlename, String argumentname) {
		for(int X = 0; X < _Titles.length(); X++) {
			if(_Titles.getItemAt(X).getName().equals(titlename)) {
				for(int Y = 0; Y < _Titles.getItemAt(X).length(); Y++) {
					if(_Titles.getItemAt(X).getArgument(Y).getName().equalsIgnoreCase(argumentname)) {
						return Y;
					}
				}
			}
		}

		return -1;
	}

	public boolean titleExists(String titlename) {
		for(int X = 0; X < _Titles.length(); X++) {
			if(_Titles.getItemAt(X).getName().equals(titlename)) {
				return true;
			}
		}

		return false;
	}

	public boolean argExists(String titlename, String argumentname) {
		for(int X = 0; X < _Titles.length(); X++) {
			if(_Titles.getItemAt(X).getName().equals(titlename)) {
				for(int Y = 0; Y < _Titles.getItemAt(X).length(); X++) {
					if(_Titles.getItemAt(X).getArgument(Y).getName().equalsIgnoreCase(argumentname)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public Argument getArgument(int titleindex, int argindex) {
		if(_Titles.validIndex(titleindex)) {
			final Title T_TITLE = _Titles.getItemAt(titleindex);

			if(T_TITLE != null) {
				return T_TITLE.getArgument(argindex);
			}
		}

		return null;
	}

	public Argument getArgument(String titlename, int argindex) {
		final int INDEX = findTitle(titlename);

		if(INDEX != -1) {
			final Title T_TITLE = _Titles.getItemAt(INDEX);

			if(T_TITLE != null) {
				return T_TITLE.getArgument(argindex);
			}
		}

		return null;
	}

	public Argument getArgument(String titlename, String argname) {
		for(int X = 0; X < _Titles.length(); X++) {
			if(titlename.equals(_Titles.getItemAt(X).getName())) {
				for(int Y = 0; Y < _Titles.getItemAt(X).length(); Y++) {
					if(argname.equalsIgnoreCase(_Titles.getItemAt(X).getArgument(Y).getName())) {
						return _Titles.getItemAt(X).getArgument(Y);
					}
				}
			}
		}

		return null;
	}

	public Title getTitle(int titleindex) {
		if(_Titles.validIndex(titleindex)) {
			return _Titles.getItemAt(titleindex);
		}

		return null;
	}

	public Title popTitle(String titlename) {
		final int INDEX = findTitle(titlename);

		return (INDEX == -1 ? null : _Titles.popItemAt(INDEX));
	}

	public int getTitleIndex(String titlename) {
		return findTitle(titlename);
	}

	public int length() {
		return _Titles.length();
	}
}