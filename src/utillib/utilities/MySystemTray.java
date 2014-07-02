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

package utillib.utilities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import java.io.File;

import java.net.URL;

import javax.imageio.ImageIO;

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * November 03, 2008 (Version 1.0.1)
 *     -Added
 *         -Method hasPopupMenu
 *         -Method hasTrayIcon
 * 
 * April 05, 2009 (Version 1.0.2)
 *     -Updated
 *         -Everything
 * 
 * @author Justin Palinkas
 * 
 * <pre>
 */
public class MySystemTray {
	private SystemTray _Tray = null;
	private PopupMenu _MainPopupMenu = null;
	private TrayIcon _TrayIcon = null;

	public MySystemTray(Image image) {
		this(image, "", null);
	}

	public MySystemTray(Image image, String tooltip) {
		this(image, tooltip, null);
	}

	public MySystemTray(Image image, String tooltip, MouseListener listener) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

		if(tooltip == null) {
			throw new RuntimeException("Variable[tooltip] - Is Null");
		}

		if(!SystemTray.isSupported()) {
			throw new RuntimeException("Variable[_Tray] - System Tray Is Not Supported");
		}

		_Tray = SystemTray.getSystemTray();

		_TrayIcon = new TrayIcon(image, tooltip);
		_TrayIcon.setImageAutoSize(true);

		if(listener != null) {
			_TrayIcon.addMouseListener(listener);
		}

		try {
			_Tray.add(_TrayIcon);
		} catch(Exception e) {
			throw new RuntimeException("Variable[_Tray] - " + e.toString());
		}
	}

	public void displayInfoMsg(String title, String msg) {
		displayMessage(title, msg, TrayIcon.MessageType.INFO);
	}

	public void displayErrorMsg(String title, String msg) {
		displayMessage(title, msg, TrayIcon.MessageType.ERROR);
	}

	public void displayWarningMsg(String title, String msg) {
		displayMessage(title, msg, TrayIcon.MessageType.WARNING);
	}

	public void displayMessage(String title, String msg) {
		displayMessage(title, msg, TrayIcon.MessageType.NONE);
	}

	private void checkTrayIcon() {
		if(_TrayIcon == null) {
			throw new RuntimeException("Variable[_TrayIcon] - Is Null");
		}
	}

	public void displayMessage(String title, String msg, TrayIcon.MessageType messagetype) {
		checkTrayIcon();

		_TrayIcon.displayMessage(title, msg, messagetype);
	}

	public void addTrayMouseListener(MouseListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.addMouseListener(listener);
	}

	public void addTrayMouseListener(MouseInputListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.addMouseListener(listener);
	}

	public void addTrayMouseListener(MouseMotionListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.addMouseMotionListener(listener);
	}

	public void removeTrayMouseListener(MouseListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.removeMouseListener(listener);
	}

	public void removeTrayMouseListener(MouseInputListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.removeMouseListener(listener);
	}

	public void removeTrayMouseListener(MouseMotionListener listener) {
		if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.removeMouseMotionListener(listener);
	}

	public void setImage(File file) {
		if(file != null && file.exists()) {
			setImage(Toolkit.getDefaultToolkit().getImage(file.getPath()));
		}
	}

	public void setImage(String resource) {
		URL Url = ClassLoader.getSystemClassLoader().getResource(resource);

		if(Url == null) {
			System.out.println("Resource: " + Url + " Does Not Exist");
		} else {
			Image Img = null;//new ImageIcon(filepath).getImage();

			try {
				//Img = Toolkit.getDefaultToolkit().getImage(filepath);
				Img = ImageIO.read(Url);
			} catch(Exception e) {
				System.out.println(e.toString());
			}

			setImage(Img);
		}
	}

	public void setImage(Image image) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

		checkTrayIcon();

		_TrayIcon.setImage(image);
	}

	public void setToolTip(String tip) {
		checkTrayIcon();

		_TrayIcon.setToolTip(tip);
	}

	public MenuItem addMenuItem(String label, ActionListener action) {
		return addMenuItem(label, null, action);
	}

	public MenuItem addMenuItem(String label, int actioncommand, ActionListener action) {
		return addMenuItem(label, Integer.toString(actioncommand), action);
	}

	public MenuItem addMenuItem(String label, String actioncommand, ActionListener action) {
		checkTrayIcon();

		if(_MainPopupMenu == null) {
			return null;
		} else {
			MenuItem Item = new MenuItem();

			Item.setLabel(label);

			if(actioncommand != null) {
				Item.setActionCommand(actioncommand);
			}

			if(action != null) {
				Item.addActionListener(action);
			}

			_MainPopupMenu.add(Item);

			return Item;
		}
	}

	public MenuItem addMenuItem(MenuItem menuitem) {
		checkTrayIcon();

		if(_MainPopupMenu == null) {
			return null;
		} else {
			_MainPopupMenu.add(menuitem);

			return menuitem;
		}
	}

	public void removeMenuItem(MenuItem menuitem) {
		checkTrayIcon();

		if(_MainPopupMenu != null) {
			_MainPopupMenu.remove(menuitem);
		}
	}

	public PopupMenu addPopupMenu(PopupMenu popupmenu) {
		checkTrayIcon();

		_MainPopupMenu.add(popupmenu);

		return popupmenu;
	}

	public void createDefaultPopMenu() {
		checkTrayIcon();

		if(_MainPopupMenu == null) {
			_MainPopupMenu = new PopupMenu();
			_TrayIcon.setPopupMenu(_MainPopupMenu);
		}
	}

	public boolean hasPopupMenu() {
		return (_MainPopupMenu != null);
	}

	public boolean hasTrayIcon() {
		return (_Tray != null);
	}

	public void setMainPopupMenu(PopupMenu popupmenu) {
		if(popupmenu == null) {
			throw new RuntimeException("Variable[popupmenu] - Is Null");
		}

		checkTrayIcon();

		if(_MainPopupMenu != null) {
			removeMenus();
		}

		_MainPopupMenu = popupmenu;
		_TrayIcon.setPopupMenu(_MainPopupMenu);
	}

	public PopupMenu getMainPopMenu() {
		return _MainPopupMenu;
	}

	public void removeTray() {
		if(_Tray != null && _TrayIcon != null) {
			try {
				//XXX Not Returning Correctly
				_Tray.remove(_TrayIcon);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeMenus() {
		checkTrayIcon();

		_TrayIcon.getPopupMenu().removeAll();
		_MainPopupMenu = null;
		_TrayIcon.setPopupMenu(null);
	}

	public Image resizeImage(Image image) {
		Dimension Size = _Tray.getTrayIconSize();

		return image.getScaledInstance((int)Size.getWidth(), (int)Size.getWidth(), Image.SCALE_DEFAULT);
	}

	public static boolean isSupported() {
		return SystemTray.isSupported();
	}

	@Override
	protected void finalize() throws Throwable {
		removeMenus();
		removeTray();

		super.finalize();
	}
}
