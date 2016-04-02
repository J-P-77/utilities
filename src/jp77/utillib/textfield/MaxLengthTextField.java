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

package jp77.utillib.textfield;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

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
public class MaxLengthTextField extends JTextField {
	private int _MaxLength = -1;

	public MaxLengthTextField() {
		this(-1);
	}

	public MaxLengthTextField(String text) {
		this(text, -1);
	}

	public MaxLengthTextField(int maxlength) {
		this("", maxlength);
	}

	public MaxLengthTextField(String text, int maxlength) {
		setMaxLength(maxlength);
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent kevent) {}

			public void keyReleased(KeyEvent kevent) {}

			public void keyTyped(KeyEvent kevent) {
				thiskeyTyped(kevent);
			}
		});
	}

	private void thiskeyTyped(KeyEvent kevent) {
		String Str = super.getSelectedText();

		if(Str == null && _MaxLength != -1) {
			if(super.getText().length() >= _MaxLength) {
				kevent.consume();
			}
		}
	}

	@Override
	public void setText(String str) {
		if(_MaxLength == -1) {
			super.setText(str);
		} else {
			if(str.length() > _MaxLength) {
				super.setText(str.substring(0, _MaxLength));
			} else {
				super.setText(str);
			}
		}
	}

	public void append(char c) {
		String OldText = super.getText();
		setText(OldText + c);
	}

	public void append(String str) {
		String OldText = super.getText();
		setText(OldText + str);
	}

	public void setMaxLength(int value) {
		_MaxLength = value;
	}

	public int getMaxLength() {
		return _MaxLength;
	}
}
