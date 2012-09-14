package utillib.debug;

import utillib.interfaces.IDebugLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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
public class DebugListener implements ActionListener, WindowStateListener, WindowListener {
	private final IDebugLogger _DEBUG_HANDLER;

	public DebugListener(IDebugLogger debug) {
		if(debug == null) {
			throw new RuntimeException("Variable[debug] - Is Null");
		}

		_DEBUG_HANDLER = debug;
	}

	public void actionPerformed(ActionEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowActivated(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowDeactivated(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowIconified(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowDeiconified(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowOpened(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowClosing(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowClosed(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}

	public void windowStateChanged(WindowEvent e) {
		_DEBUG_HANDLER.printInformation(e.toString());
	}
}
