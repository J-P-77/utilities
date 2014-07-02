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

package utillib.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 13, 2008 (Version 1.0.0)
 *     -First Released<br>
 * 
 * @author Justin Palinkas<br>
 * 
 * </pre>
 */
//!!!!!!!!!!!!!!!TESTING VERSION ONLY!!!!!!!!!!!!!!!
public class Button implements TableCellRenderer {
	public static final int STATE_NONE = -1;
	public static final int STATE_UP = 0;
	public static final int STATE_DOWN = 1;

	private int _CurrentButton = -1;
	private int _State = STATE_NONE;

	public Button() {
		this("");
	}

	public Button(String text) {
		final Dimension Dim = new Dimension(100, 20);
		Insets Inset = new Insets(1, 1, 1, 1);

		butUp = new JButton(text);
		butUp.setMinimumSize(Dim);
		butUp.setMaximumSize(Dim);
		butUp.setPreferredSize(Dim);
		butUp.setMargin(Inset);
		butUp.getModel().setPressed(false);
		butUp.getModel().setArmed(false);

		butDown = new JButton(text);
		butDown.setMinimumSize(Dim);
		butDown.setMaximumSize(Dim);
		butDown.setPreferredSize(Dim);
		butDown.setMargin(Inset);
		butDown.getModel().setPressed(true);
		butDown.getModel().setArmed(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		boolean ButtonClicked = (_CurrentButton == column);

		JButton ButTemp = null;

		//System.out.println("Column: " + column + " - buttoncClicked: " + ButtonClicked);

		if(ButtonClicked) {
			if(_State == STATE_UP) {
				ButTemp = butUp;
			} else if(_State == STATE_DOWN) {
				ButTemp = butDown;
			}
		} else {
			ButTemp = butUp;
			ButTemp.getModel().setPressed(ButtonClicked);
			ButTemp.getModel().setArmed(ButtonClicked);
		}

		if(value != null) {
			ButTemp.setText(value.toString());
		}

		return ButTemp;
	}

	public void setCurrent(int button, int state) {
		_CurrentButton = button;
		_State = state;
	}

	private JButton butUp;
	private JButton butDown;
}
