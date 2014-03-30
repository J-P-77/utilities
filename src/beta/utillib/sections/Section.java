/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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