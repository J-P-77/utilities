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

package jp77.utillib.popupmenus;

import jp77.utillib.strings.MyStringBuffer;

import java.awt.MenuItem;
import java.awt.PopupMenu;
// import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class AwtBasicPopup extends PopupMenu {
	private static final Clipboard _CLIPBOARD_ = Toolkit.getDefaultToolkit().getSystemClipboard();

	private final TextComponent _TCOMPONENT;

	public AwtBasicPopup(TextComponent comp) {
		_TCOMPONENT = comp;

		initComponents();
	}

	private void initComponents() {
		mnuCut = new MenuItem("Cut");
		mnuCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_CLIPBOARD_.setContents(new StringSelection(_TCOMPONENT.getSelectedText()), null);

				final MyStringBuffer BUFFER = new MyStringBuffer(_TCOMPONENT.getText());

				BUFFER.remove(_TCOMPONENT.getSelectionStart(), _TCOMPONENT.getSelectionEnd());

				_TCOMPONENT.setText(BUFFER.toString());
			}
		});
		this.add(mnuCut);

		mnuCopy = new MenuItem("Copy");
		mnuCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_CLIPBOARD_.setContents(new StringSelection(_TCOMPONENT.getSelectedText()), null);
			}
		});
		this.add(mnuCopy);

		mnuPaste = new MenuItem("Paste");
		mnuPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Transferable TRANSFER = _CLIPBOARD_.getContents(null);

				if(TRANSFER instanceof StringSelection) {
					try {
						final String TEXT = (String)TRANSFER.getTransferData(DataFlavor.stringFlavor);

						final StringBuffer BUFFER = new StringBuffer(_TCOMPONENT.getText());

						BUFFER.insert(_TCOMPONENT.getCaretPosition(), TEXT);

						_TCOMPONENT.setText(BUFFER.toString());
					} catch(Exception ex) {}
				}
			}
		});
		this.add(mnuPaste);

		this.addSeparator();

		mnuSelectAll = new MenuItem("Select All");
		mnuSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.selectAll();
			}
		});
		this.add(mnuSelectAll);

		mnuDeselectAll = new MenuItem("Deselect All");
		mnuDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.setSelectionStart(0);
				_TCOMPONENT.setSelectionEnd(0);
			}
		});
		this.add(mnuDeselectAll);
	}

	public void show(int x, int y) {
		super.show(_TCOMPONENT, x, y);
	}

	private MenuItem mnuCut;
	private MenuItem mnuCopy;
	private MenuItem mnuPaste;

	private MenuItem mnuSelectAll;
	private MenuItem mnuDeselectAll;
}
