/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package utillib.textfield;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

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
public class FormattedTextField extends JFormattedTextField {
	public FormattedTextField(String text) {
		this(text, text.length());
	}

	public FormattedTextField(int maxlength) {
		this("", maxlength);
	}

	public FormattedTextField(String text, int maxlength) {
		this.setText(text);
		createFormat(maxlength);
	}

	public void setMaxLength(int value) {
		createFormat(value);
	}

	public int getLength() {
		return this.getText().length();
	}

	public String getTextTrim() {
		return this.getText().trim();
	}

	private void createFormat(int maxlength) {
		MaskFormatter formatter = null;
		StringBuffer Format = new StringBuffer();

		for(int X = 0; X < maxlength; X++) {
			Format.append('#');
		}

		try {
			formatter = new MaskFormatter(Format.toString());
			formatter.setPlaceholderCharacter('0');

			this.setFormatter(formatter);
			this.setText(null);
		} catch(java.text.ParseException exc) {
			System.err.println("!!ERROR!!! - Formatter Is Bad: " + exc.getMessage());
		}
	}
}
