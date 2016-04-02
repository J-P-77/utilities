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

import jp77.utillib.strings.StringUtil;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 27, 2011 (Version 1.1.0)
 *     -Updated
 *         -Everything
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class NumberTextField extends JTextField {
	private int _MaxLength = 0;

	private boolean _OnlyWhole = false;
	private boolean _PositiveOnly = false;

	public NumberTextField(byte defaultnumber, int maxlength) {
		this(Byte.toString(defaultnumber), false, maxlength, false);
	}

	public NumberTextField(short defaultnumber, int maxlength) {
		this(Short.toString(defaultnumber), false, maxlength, false);
	}

	public NumberTextField(int defaultnumber, int maxlength) {
		this(Integer.toString(defaultnumber), false, maxlength, false);
	}

	public NumberTextField(long defaultnumber, int maxlength) {
		this(Long.toString(defaultnumber), false, maxlength, false);
	}

	public NumberTextField(float defaultnumber, int maxlength) {
		this(Float.toString(defaultnumber), true, maxlength, false);
	}

	public NumberTextField(double defaultnumber, int maxlength) {
		this(Double.toString(defaultnumber), true, maxlength, false);
	}

	public NumberTextField(String defaultnumber) {
		this(defaultnumber, true, 0, false);
	}

	public NumberTextField(int maxlength) {
		this(null, true, maxlength, false);
	}

	public NumberTextField(boolean allowperiod) {
		this(null, allowperiod, 0, false);
	}

	public NumberTextField(String defaultnumber, int maxlength) {
		this(defaultnumber, true, maxlength, false);
	}

	public NumberTextField(String defaultnumber, boolean allowperiod, int maxlength, boolean positiveonly) {
		setMaxLength(maxlength);

		_OnlyWhole = allowperiod;

		setText(defaultnumber);

		super.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent kevent) {}

			public void keyReleased(KeyEvent kevent) {}

			public void keyTyped(KeyEvent kevent) {
				if(!isValidKey(kevent)) {
					kevent.consume();
				}
			}
		});
	}

	public void allowPeriod(boolean value) {
		_OnlyWhole = value;
	}

	public boolean isPeriodAllowed() {
		return _OnlyWhole;
	}

	public void setText(byte number) {
		final String NUMBER = Byte.toString(number);

		this.setText(NUMBER);
	}

	public void setText(short number) {
		final String NUMBER = Short.toString(number);

		this.setText(NUMBER);
	}

	public void setText(int number) {
		final String NUMBER = Integer.toString(number);

		this.setText(NUMBER);
	}

	public void setText(long number) {
		final String NUMBER = Long.toString(number);

		this.setText(NUMBER);
	}

	@Override
	public void setText(String number) {
		if(number == null || number.length() == 0) {
			super.setText(null);
		} else {
			if(_OnlyWhole) {
				if(StringUtil.isWholeNumber(number)) {
					if(_PositiveOnly && !StringUtil.isPositiveNumber(number)) {
						throw new RuntimeException(("Variable[number]" + ' ') + number + (' ' + "Must Be Positive Number"));
					}

					internalSetText(number);
				} else {
					throw new RuntimeException(("Variable[number]" + ' ') + number + (' ' + "Is Not A Number"));
				}
			} else {
				if(StringUtil.isNumber(number)) {
					if(_PositiveOnly && !StringUtil.isPositiveNumber(number)) {
						throw new RuntimeException(("Variable[number]" + ' ') + number + (' ' + "Must Be Positive Number"));
					}

					internalSetText(number);
				} else {
					throw new RuntimeException(("Variable[number]" + ' ') + number + (' ' + "Is Not A Number"));
				}
			}
		}
	}

	private void internalSetText(String number) {
		if(_MaxLength == 0) {
			super.setText(number);
		} else {
			if(number.length() > _MaxLength) {
				super.setText(number.substring(0, _MaxLength));
			} else {
				super.setText(number);
			}
		}
	}

	public byte getByte() {
		return Byte.parseByte(super.getText());
	}

	public int getShort() {
		return Short.parseShort(super.getText());
	}

	public int getInt() {
		return Integer.parseInt(super.getText());
	}

	public long getLong() {
		return Long.parseLong(super.getText());
	}

	public float getFloat() {
		return Float.parseFloat(super.getText());
	}

	public double getDouble() {
		return Double.parseDouble(super.getText());
	}

	public void setMaxLength(int value) {
		if(value < 0) {
			throw new RuntimeException("Variable[value] - Must Be Equal Too Or Greater Than 0");
		}

		_MaxLength = value;

		if(value > 0) {
			if(getText().length() > value) {
				super.setText(getText().substring(0, _MaxLength));
			}
		}
	}

	public int getMaxLength() {
		return _MaxLength;
	}

	private boolean isValidKey(KeyEvent kevent) {
		return (isValid(kevent) && isValidChar(kevent));
	}

	private boolean isValid(KeyEvent kevent) {
		final String SELETED_STR = super.getSelectedText();

		if(SELETED_STR == null) {
			if(_MaxLength > 0 && super.getText().length() >= _MaxLength) {
				return false;
			}
		}

		return true;
	}

	private boolean isValidChar(KeyEvent kevent) {
		if(kevent.getKeyChar() == '-') {
			if(super.getCaretPosition() == 0) {
				if(super.getDocument().getLength() > 0) {
					try {
						final String STR = super.getDocument().getText(0, 1);

						if(STR.length() > 0) {
							if(STR.charAt(0) == '-') {
								return false;
							}
						}

						return true;
					} catch(Exception e) {}
				}
			}
		}

		switch(kevent.getKeyChar()) {
			case '.':
				final String STR = super.getText();

				if(STR != null && STR.length() > 0) {
					return _OnlyWhole && !containsPeriod(STR);
				}

				/*case '-': case '+':*/
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return true;
		}

		return false;
	}

	private static boolean containsPeriod(String str) {
		for(int X = 0; X < str.length(); X++) {
			if(str.charAt(X) == '.') {
				return true;
			}
		}

		return false;
	}

//    public static void main(String[] args) {
//        javax.swing.JFrame Frm = new javax.swing.JFrame();
//        Frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//        Frm.setSize(400, 24 + 34);
//
//        final NumberTextField TXT = new NumberTextField(false);
//        Frm.getContentPane().add(TXT);
//
//        Frm.setVisible(true);
//    }
}
