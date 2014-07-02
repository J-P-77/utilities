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
