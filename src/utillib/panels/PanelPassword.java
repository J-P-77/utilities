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

package utillib.panels;

import utillib.utilities.PasswordGenerator;
import utillib.textfield.MaxLengthTextField;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

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
public class PanelPassword extends JPanel {
	private PasswordGenerator _Password = null;

	public PanelPassword() {
		initComponents();

		_Password = new PasswordGenerator();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setBorder(new TitledBorder("Password Generator"));

		butGenerate = new JButton("Generate");
		butGenerate.setBounds(300 - 80 + 6, 100 - 24 - 6, 80, 24);
		butGenerate.setMargin(new Insets(1, 1, 1, 1));
		butGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aevent) {
				butGenerateactionPerformed(aevent);
			}
		});
		this.add(butGenerate);

		txtPassword = new JTextField();
		txtPassword.setBounds(6, 100, 300, 24);
		this.add(txtPassword);

		lblLength = new JLabel("Length");
		lblLength.setBounds(300 - 80 + 6, 100 - 24 - 6 - 24 - 2 - 24, 80, 24);
		this.add(lblLength);

		txtLength = new MaxLengthTextField(3);
		txtLength.setText("1");
		txtLength.setBounds(300 - 80 + 6, 100 - 24 - 6 - 24 - 2, 80, 24);
		txtLength.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {
				final int KEY = e.getKeyChar();

				switch(KEY) {
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
						break;
					default:
						e.consume();
				}
			}
		});
		this.add(txtLength);

		chkUpperCase = new JCheckBox("UpperCase");
		chkUpperCase.setBounds(6, 100 - 22 - 22 - 22, 100, 22);
		this.add(chkUpperCase);

		chkLowerCase = new JCheckBox("LowerCase");
		chkLowerCase.setBounds(6, 100 - 22 - 22, 100, 22);
		this.add(chkLowerCase);

		chkBrackets = new JCheckBox("Brackets");
		chkBrackets.setBounds(6, 100 - 22, 100, 22);
		this.add(chkBrackets);

		chkNumber = new JCheckBox("Number");
		chkNumber.setBounds(6 + 100, 100 - 22 - 22 - 22, 100, 22);
		this.add(chkNumber);

		chkPunctuation = new JCheckBox("Punctuation");
		chkPunctuation.setBounds(6 + 100, 100 - 22 - 22, 100, 22);
		this.add(chkPunctuation);
	}

	private void butGenerateactionPerformed(ActionEvent aevent) {
		_Password.setIncludeBrackets(chkBrackets.isSelected());
		_Password.setIncludeLower(chkLowerCase.isSelected());
		_Password.setIncludeUpper(chkUpperCase.isSelected());
		_Password.setIncludeNumber(chkNumber.isSelected());
		_Password.setIncludePunctuation(chkPunctuation.isSelected());

		if(chkBrackets.isSelected() || chkLowerCase.isSelected() || chkUpperCase.isSelected() || chkNumber.isSelected() || chkPunctuation.isSelected()) {

			try {
				_Password.setPassordLength(Integer.parseInt(txtLength.getText()));
				txtPassword.setText(_Password.generate());
			} catch(Exception e) {
				txtPassword.setText("!!!ERROR!!!");
			}
		} else {
			txtPassword.setText("!!!ERROR!!! - Check A Least One");
		}

		//System.out.println("Password: " + txtPassword.getText() + 
		//	" - Length: " + txtPassword.getText().length());
	}

	private JTextField txtPassword;
	private JLabel lblLength;
	private MaxLengthTextField txtLength;

	private JButton butGenerate;
	private JCheckBox chkUpperCase;
	private JCheckBox chkLowerCase;
	private JCheckBox chkBrackets;
	private JCheckBox chkPunctuation;
	private JCheckBox chkNumber;

	//private JSlider sliderLength;

//	public static void main(String[] args) {
//		javax.swing.JFrame Frm = new javax.swing.JFrame("Test Frame");
//
//		Frm.setSize(400,400);
//		Frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//		Frm.getContentPane().add(new PanelPassword());
//		Frm.setVisible(true);
//	}
}
