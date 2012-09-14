package beta.utillib.sections;

import utillib.strings.MyStringBuffer;

/**
 * 
 * @author Dalton Dell
 */
public class PropertyBuilder {
	private MyStringBuffer _BuildString = null;
	private final String _SPACER;

	public PropertyBuilder(String intialstring, String spacer) {
		if(spacer == null) {
			throw new RuntimeException("Variable[spacer] - Is Null");
		}

		_SPACER = spacer;

		_BuildString = new MyStringBuffer(intialstring, _SPACER.length() + 16);
		_BuildString.append(_SPACER);
	}

	public PropertyBuilder(String intialstring, char spacer) {
		_SPACER = Character.toString(spacer);

		_BuildString = new MyStringBuffer(intialstring, 16);
		_BuildString.append(_SPACER);
	}

	public void add(char c) {
		_BuildString.append(c);
		_BuildString.append(_SPACER);
	}

	public void add(String str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(StringBuffer str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(MyStringBuffer str) {
		_BuildString.append(str);
		_BuildString.append(_SPACER);
	}

	public void add(PropertyBuilder str) {
		_BuildString.append(str.getResult());
		_BuildString.append(_SPACER);
	}

	public void reset() {
		_BuildString.reset();
	}

	public String getResult() {
		return toString();
	}

	@Override
	public String toString() {
		if(_BuildString.endsWith(_SPACER)) {
			_BuildString.reset(_BuildString.length() - _SPACER.length());
		}

		return _BuildString.toString();
	}
}
