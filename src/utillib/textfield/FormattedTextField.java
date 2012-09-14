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
