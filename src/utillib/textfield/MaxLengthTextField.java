package utillib.textfield;

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
