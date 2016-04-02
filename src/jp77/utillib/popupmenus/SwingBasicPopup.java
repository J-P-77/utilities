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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;

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
public class SwingBasicPopup extends JPopupMenu {
	//private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();

	private final JTextComponent _TCOMPONENT;

	public SwingBasicPopup(JTextComponent jcomponent) {
		_TCOMPONENT = jcomponent;

		initComponents();
	}

	private void initComponents() {
		super.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent popupevent) {
				if(_TCOMPONENT.getSelectionStart() == _TCOMPONENT.getSelectionEnd()) {
					mnuCut.setEnabled(false);
					mnuCopy.setEnabled(false);
					mnuDeselectAll.setEnabled(false);
				} else {
					mnuCut.setEnabled(true);
					mnuCopy.setEnabled(true);
					mnuDeselectAll.setEnabled(true);
				}
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent popupevent) {}

			@Override
			public void popupMenuCanceled(PopupMenuEvent popupevent) {}
		});

		mnuCut = new JMenuItem("Cut");
		mnuCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.cut();
			}
		});
		super.add(mnuCut);

		mnuCopy = new JMenuItem("Copy");
		mnuCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.copy();
			}
		});
		super.add(mnuCopy);

		mnuPaste = new JMenuItem("Paste");
		mnuPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.paste();
			}
		});
		super.add(mnuPaste);

		sep1 = new Separator();
		super.add(sep1);

		mnuSelectAll = new JMenuItem("Select All");
		mnuSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.selectAll();
			}
		});
		super.add(mnuSelectAll);

		mnuDeselectAll = new JMenuItem("Deselect All");
		mnuDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_TCOMPONENT.setSelectionStart(0);
				_TCOMPONENT.setSelectionEnd(0);
			}
		});
		super.add(mnuDeselectAll);
	}

	public void showPopUp(int x, int y) {
		if(_TCOMPONENT.getSelectionStart() == _TCOMPONENT.getSelectionEnd()) {
			mnuCut.setEnabled(false);
			mnuCopy.setEnabled(false);
			mnuDeselectAll.setEnabled(false);
		} else {
			mnuCut.setEnabled(true);
			mnuCopy.setEnabled(true);
			mnuDeselectAll.setEnabled(true);
		}

		System.out.println(_TCOMPONENT.getSelectionStart());
		System.out.println(_TCOMPONENT.getSelectionEnd());

		super.show(_TCOMPONENT, x, y);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	private JMenuItem mnuCut;
	private JMenuItem mnuCopy;
	private JMenuItem mnuPaste;

	private JSeparator sep1;

	private JMenuItem mnuSelectAll;
	private JMenuItem mnuDeselectAll;
}
