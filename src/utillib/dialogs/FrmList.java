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

package utillib.dialogs;

import utillib.utilities.MsgUtil;
import utillib.utilities.PositionWindow;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * 
 * @author Justin Palinkas
 */
public class FrmList extends JDialog {
	public static final int _RETURN_INTEGER_ = 0;
	public static final int _RETURN_OBJECT_ = 1;

	private static final int _DEFAULT_WIDTH_ = 180;
	private static final int _DEFAULT_HEIGHT_ = 300;

	private Object _Return = null;

	public FrmList() {
		this(null, "List");
	}

	public FrmList(String title) {
		this(null, title);
	}

	public FrmList(Window owner, String title) {
		this(owner, _DEFAULT_WIDTH_, _DEFAULT_HEIGHT_, title, null);
	}

	public FrmList(String title, int width, int height) {
		this(null, width, height, title, null);
	}

	public FrmList(Window owner, String title, Object[] items) {
		this(owner, _DEFAULT_WIDTH_, _DEFAULT_HEIGHT_, title, items);
	}

	public FrmList(Window owner, int width, int height, String title, Object[] items) {
		super(owner, title);
		super.setSize(width, height);

		PositionWindow PosWin = new PositionWindow(this);

		if(owner == null) {
			PosWin.toCenter();
		} else {
			PosWin.putToInCenterOf(owner);
		}

		initComponents();

		addItems(items);
	}

	private void initComponents() {
		super.setModal(true);
		super.setMinimumSize(new Dimension(_DEFAULT_WIDTH_, _DEFAULT_HEIGHT_));
		super.setPreferredSize(new Dimension(_DEFAULT_WIDTH_, _DEFAULT_HEIGHT_));
		super.getContentPane().setLayout(new BoxLayout(super.getContentPane(), BoxLayout.PAGE_AXIS));
		super.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}

			public void windowClosing(WindowEvent e) {
				cancel();
			}

			public void windowClosed(WindowEvent e) {}

			public void windowIconified(WindowEvent e) {}

			public void windowDeiconified(WindowEvent e) {}

			public void windowActivated(WindowEvent e) {}

			public void windowDeactivated(WindowEvent e) {}
		});

		final Insets Buttons = new Insets(6, 4, 6, 4);

		listModal = new DefaultListModel();

		listInfo = new JList(listModal);
		//listInfo.setBounds(0,0,195,240);
		listInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollList = new JScrollPane(listInfo);
		scrollList.setBounds(listInfo.getBounds());
		super.getContentPane().add(scrollList);

		panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 14, 5));
		panelButtons.setMinimumSize(new Dimension(500, 36));
		panelButtons.setPreferredSize(new Dimension(500, 36));
		panelButtons.setMaximumSize(new Dimension(500, 36));
		super.getContentPane().add(panelButtons);

		butOk = new JButton("Ok");
		//butOk.setBounds(10+6,248,70,24);
		butOk.setMinimumSize(new Dimension(70, 24));
		butOk.setPreferredSize(new Dimension(70, 24));
		butOk.setMaximumSize(new Dimension(70, 24));
		butOk.setMargin(Buttons);
		butOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		panelButtons.add(butOk);

		butCancel = new JButton("Cancel");
		//butCancel.setBounds(200-70-10-8-6,248,70,24);
		butCancel.setMinimumSize(new Dimension(70, 24));
		butCancel.setPreferredSize(new Dimension(70, 24));
		butCancel.setMaximumSize(new Dimension(70, 24));
		butCancel.setMargin(Buttons);
		butCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		panelButtons.add(butCancel);
	}

	public Object showDialog() {
		return showDialog(_RETURN_OBJECT_);
	}

	public Object showDialog(int returntype) {
		if(returntype == _RETURN_INTEGER_) {
			return getSelectedIndex();
		} else {
			return showDialogObject();
		}
	}

	public Object showDialogObject() {
		this.setVisible(true);

		return _Return;
	}

	public int showDialogIndex() {
		this.setVisible(true);

		return getSelectedIndex();
	}

	public int getSelectedIndex() {
		return listInfo.getSelectedIndex();
	}

	public void addItem(Object items) {
		listModal.addElement(items);
	}

	public void addItems(Object[] items) {
		if(items != null) {
			for(int X = 0; X < items.length; X++) {
				listModal.addElement(items[X]);
			}
		}
	}

	public void removeItem(Object item) {
		listModal.removeElement(item);
	}

	public void removeItemAt(int index) {
		listModal.removeElementAt(index);
	}

	public void removeAllItems() {
		listModal.removeAllElements();
	}

	public int itemCount() {
		return listModal.getSize();
	}

	private void cancel() {
		_Return = null;
		this.setVisible(false);
	}

	private void ok() {
		int Selected = listInfo.getSelectedIndex();

		if(Selected == -1) {
			MsgUtil.msgboxInformation(this, "Nothing Selected", this.getTitle());
		} else {
			_Return = listModal.getElementAt(Selected);
			this.setVisible(false);
		}
	}

	private JList listInfo;
	private DefaultListModel listModal;
	private JScrollPane scrollList;

	private JButton butOk;
	private JButton butCancel;
	private JPanel panelButtons;

	public static void main(String[] args) {
		FrmList Frm = new FrmList("Test");

		for(int X = 0; X < 50; X++) {
			Frm.addItem(X);
		}

		Frm.showDialog();
	}
}
