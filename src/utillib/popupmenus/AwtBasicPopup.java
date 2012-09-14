package utillib.popupmenus;

import utillib.strings.MyStringBuffer;

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
