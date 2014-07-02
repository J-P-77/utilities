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

package utillib.strings;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDropEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.JTextComponent;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 07, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class StringDropper extends DropTargetAdapter {
	private final JTextComponent _TextComponent;

	private ActionListener _Listener = null;
	private String _ActionCommand = null;

	public StringDropper(JTextComponent textcomponent) {
		if(textcomponent == null) {
			throw new RuntimeException("Variable[textcomponent] Is Null");
		}

		_TextComponent = textcomponent;
	}

	public void setActionCommand(String value) {
		_ActionCommand = value;
	}

	public void setActionCommand(byte value) {
		_ActionCommand = Byte.toString(value);
	}

	public void setActionCommand(short value) {
		_ActionCommand = Short.toString(value);
	}

	public void setActionCommand(int value) {
		_ActionCommand = Integer.toString(value);
	}

	public String getActionCommand() {
		return _ActionCommand;
	}

	public void setActionListener(ActionListener listener) {
		_Listener = listener;
	}

	public ActionListener getActionListener() {
		return _Listener;
	}

	public void drop(DropTargetDropEvent de) {
		DropTargetContext Context = de.getDropTargetContext();
		de.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable t = de.getTransferable();

		try {
			if(hasStringFlavor(t)) {
				Object data = t.getTransferData(DataFlavor.stringFlavor);
				if(data instanceof String) {
					String Str = (String)data;

					setText(Str);

					if(_Listener != null) {
						_Listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, _ActionCommand));
					}
				}
			}
			Context.dropComplete(true);
		} catch(Exception e) {
			System.out.println("!!!ERROR!!! - " + e.getMessage());
			Context.dropComplete(false);
		}
	}

	private void setText(String value) {
		String StrTemp = _TextComponent.getText();

		if(StrTemp == null) {
			_TextComponent.setText(value);
		} else {
			_TextComponent.setText(StrTemp + value);
		}
	}

	private boolean hasStringFlavor(Transferable t) {
		DataFlavor[] Flavors = t.getTransferDataFlavors();

		for(int X = 0; X < Flavors.length; X++) {
			if(Flavors[X].equals(DataFlavor.stringFlavor)) {
				return true;
			}
		}

		return false;
	}
}