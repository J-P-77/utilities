package utillib.textfield;

import utillib.arrays.ResizingCharArray;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class LimitedTextField extends JTextField {
	private final ResizingCharArray _EXCLUDE = new ResizingCharArray();
	private final ResizingCharArray _INCLUDE = new ResizingCharArray();

	private boolean _MatchCase = true;

	public LimitedTextField() {
		this(false);
	}

	public LimitedTextField(boolean matchcase) {
		super.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent kevent) {}

			@Override
			public void keyReleased(KeyEvent kevent) {}

			@Override
			public void keyTyped(KeyEvent kevent) {
				if(!isValidChar(kevent.getKeyChar())) {
					kevent.consume();
				}
			}
		});

		_MatchCase = matchcase;
	}

	public void setMatchCase(boolean value) {
		_MatchCase = value;
	}

	public boolean getMatchCase() {
		return _MatchCase;
	}

	@Override
	public void setText(String value) {
		if(value == null) {
			super.setText(null);
		} else {
			if(value.length() > 0) {
				StringBuffer Buffer = new StringBuffer(value.length());

				for(int X = 0; X < value.length(); X++) {
					if(isValidChar(value.charAt(X))) {
						Buffer.append(value.charAt(X));
					}
				}

				super.setText(Buffer.toString());
			}
		}
	}

	public ResizingCharArray getIncludeList() {
		return _INCLUDE;
	}

	public ResizingCharArray getExcludeList() {
		return _EXCLUDE;
	}

	public boolean isValidChar(char c) {
//		System.out.println((int)c);
		if(c == 8 || c == 10 || c == 13) {//Backspace | NewLine Characters
			return true;
		}

		final char C1 = (_MatchCase ? c : Character.toLowerCase(c));

		if(_INCLUDE.length() > 0) {
			for(int X = 0; X < _INCLUDE.length(); X++) {
				final char C2 = (_MatchCase ? _INCLUDE.getAt(X) : Character.toLowerCase(_INCLUDE.getAt(X)));

				if(C1 == C2) {
					return true;
				}
			}

			return false;
		}

		if(_EXCLUDE.length() > 0) {
			for(int X = 0; X < _EXCLUDE.length(); X++) {
				final char C2 = (_MatchCase ? _EXCLUDE.getAt(X) : Character.toLowerCase(_EXCLUDE.getAt(X)));

				if(C1 == C2) {
					return false;
				}
			}

			return true;
		}

		return true;
	}
}
