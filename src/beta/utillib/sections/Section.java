package beta.utillib.sections;

import utillib.collections.MyStackAll;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class Section implements SectionConstants {
	private final String _NAME;
	private final boolean _IS_COMMENT;

	private MyStackAll<SectionProperty> _Properties = new MyStackAll<SectionProperty>();

	public Section(String name) {
		this(name, false);
	}

	public Section(String name, boolean iscomment) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;
		_IS_COMMENT = iscomment;
	}

	public String getName() {
		return _NAME;
	}

	public boolean isComment() {
		return _IS_COMMENT;
	}

	public void addProperty(String property) {
		if(property.contains("=")) {
			String[] StrSplit = property.split("=", 2);
			addProperty(StrSplit[0], StrSplit[1]);
		} else {
			addProperty(property, "");
		}
	}

	public void addProperty(String name, boolean variable) {
		addProperty(name, Boolean.toString(variable));
	}

	public void addProperty(String name, byte variable) {
		addProperty(name, Byte.toString(variable));
	}

	public void addProperty(String name, short variable) {
		addProperty(name, Short.toString(variable));
	}

	public void addProperty(String name, int variable) {
		addProperty(name, Integer.toString(variable));
	}

	public void addProperty(String name, long variable) {
		addProperty(name, Long.toString(variable));
	}

	public void addProperty(String name, float variable) {
		addProperty(name, Float.toString(variable));
	}

	public void addProperty(String name, double variable) {
		addProperty(name, Double.toString(variable));
	}

	public void addProperty(String name, String variable) {
		addProperty(new SectionProperty(name, variable));
	}

	public void addProperty(SectionProperty a) {
		if(isComment()) {
			throw new RuntimeException("Variable[a] - Cannot Add Argument To Comment Title");
		}

		final SectionProperty PROP = getProperty(a.getName());

		if(PROP == null) {
			_Properties.push(a);
		} else {
			PROP.setVariable(a.getVariable());
		}
	}

	public void addComment(String commentstart, String comment) {
		_Properties.push(new SectionProperty(commentstart, comment));
	}

	public SectionProperty getProperty(int index) {
		if(_Properties.validIndex(index)) {
			return _Properties.getItemAt(index);
		} else {
			return null;
		}
	}

	public SectionProperty getProperty(String name) {
		for(int X = 0; X < _Properties.length(); X++) {
			if(name.equalsIgnoreCase(_Properties.getItemAt(X).getName())) {
				return _Properties.getItemAt(X);
			}
		}

		return null;
	}

	public int length() {
		return _Properties.length();
	}

	public MyStackAll<SectionProperty> getProperties() {
		return _Properties;
	}
}