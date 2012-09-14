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
