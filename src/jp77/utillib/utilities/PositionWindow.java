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

package jp77.utillib.utilities;

import jp77.utillib.lang.byref.IntByRef;
import jp77.utillib.strings.MyStringBuffer;
import jp77.utillib.strings.StringUtil;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * <pre>
 * <b>Current Version 1.0.12</b>
 * 
 * Version 1.0.0
 *     -First Released
 * 
 * May 05, 2008 (version 1.0.1)
 *     -Fixed Bug
 *         -Swap Method Name putToLeftOf(Window) With Name Method putToRightOf(Window)
 *             (Method putToLeftOf Puts Window To Right Of and Method putToRightOf Puts Windos To Left Of)
 * 
 * October 25, 2008 (version 1.0.2)
 *     -Added
 *         -Method putToLeftUpperOf(Window), putToLeftCenterOf(Window),
 *             putToLeftLowerOf(Window), putToRightUpperOf(Window),
 *             putToRightCenterOf(Window), and putToRightLowerOf(Window)
 * 
 * November 03, 2008 (version 1.0.3)
 *     -Added
 *         -Method getX() returns X Location On Screen),
 *             getY() returns Y Location On Screen
 * 
 * November 03, 2008 (version 1.0.4)
 *     -Added
 *         -Offset Location if Os is Windows
 * 
 * December 18, 2008 (version 1.0.5)
 *     -Added
 *         -Window Component Listener to See Window has Been Moved
 * 
 * April 01, 2009 (version 1.0.6)
 *     -Updated
 *         -EveryThing
 * 
 * April 01, 2009 (version 1.0.7)
 *     -Updated
 *         -Get This Screen Dimensions(Added Extra Layer of This)
 * 
 * May 01, 2009 (version 1.0.8)
 *     -Fixed Bug
 *         -putToLeftUpperOf(Window) Would Move Window To Incorrect Position
 *     -Updated
 *         -parse Method
 * 
 * June 21, 2009 (version 1.0.9)
 *     -Added
 *         -Method quickToCenter(Window) static method to move window to center
 * 
 * April 12, 2009 (version 1.0.10)
 *     -Added
 *         -Null Pointer Checks
 *         -Methods getWindow(), getHeight() and getWidth() and put's Get Window By Name
 * 
 * April 25, 2010 (version 1.0.11)
 *     -Added
 *         -Methods toPercentOfScreen(double, double) and
 *             toPercentOfScreen(int, double) and toPercentOfScreen(double, int)
 *             Set's The Window To A Percent Of Screen Size, The int Is For The
 *             Actual Size
 * 
 * May 05, 2009 (version 1.0.12)
 *     -Added
 *         -Methods bump(int, int), bumpX(int) and bumpY(int) Allows You To Bump
 *             The Window's Location
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */

//y = [Up and Down] or [Top and Bottom]
//x = [Right and Left]
public class PositionWindow {
	public static enum Position {
		OTHER("Other"),

		TO_CENTER("toCenter"),

		TO_LEFT_CENTER("toLeftCenter"),
		TO_RIGHT_CENTER("toRightCenter"),

		TO_UPPER_LEFT("toUpperLeft"),
		TO_UPPER_RIGHT("toUpperRight"),

		TO_LOWER_LEFT("toLowerLeft"),
		TO_LOWER_RIGHT("toLowerRight"),

		PUT_ON_TOP_CENTER_OF("putOnTopCenterOf"),
		PUT_ON_BOTTOM_CENTER_OF("putOnBottomCenterOf"),

		PUT_TO_OUT_LEFT_UPPER_OF("putToOutLeftUpperOf"),
		PUT_TO_OUT_LEFT_CENTER_OF("putToOutLeftCenterOf"),
		PUT_TO_OUT_LEFT_LOWER_OF("putToOutLeftLowerOf"),

		PUT_TO_OUT_RIGHT_UPPER_OF("putToOutRightUpperOf"),
		PUT_TO_OUT_RIGHT_CENTER_OF("putToOutRightCenterOf"),
		PUT_TO_OUT_RIGHT_LOWER_OF("putToOutRightLowerOf"),

		PUT_TO_IN_CENTER_OF("putToInCenterOf");

		private final String _Value;

		private Position(String value) {
			_Value = value;
		}

		public String getValue() {
			return _Value;
		}
	};

	private static final Dimension _THIS_SCREEN_;

	//Will Only Offset Border Positions
	private static int _OffSetY = 0;
	private static int _OffSetX = 0;

	private Window _Window = null;

	static {
		Dimension Screen = null;
		try {
			Screen = Toolkit.getDefaultToolkit().getScreenSize();
//            System.out.println("PositionWindow Using: DefaultToolkit");
		} catch(Exception e1) {
			try {
				GraphicsEnvironment Ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice Gd = Ge.getDefaultScreenDevice();
				GraphicsConfiguration Gc = Gd.getDefaultConfiguration();
				Rectangle Bounds = Gc.getBounds();

				Screen = new Dimension((int)Bounds.getWidth(), (int)Bounds.getHeight());
//                System.out.println("PositionWindow Using: GraphicsConfiguration");
			} catch(Exception e2) {
				Screen = null;
				throw new RuntimeException("Variable[THIS_SRCEEN] - Cause: " + e1.getMessage() + " - " + e2.getMessage());
			}
		}

		if(Screen == null) {
			throw new RuntimeException("Failed To Get Screen Dimensions");
		}

		_THIS_SCREEN_ = Screen;

		//Set's Default Offset (Windows Start Menu)
		if(Os.isWindows()) {
			setOffsetY(30);
		} else {
			setOffsetY(0);
		}
	}

	public static void setOffsetX(int value) {
		_OffSetX = value;
	}

	public static int getOffsetX() {
		return _OffSetX;
	}

	/**
	 * Windows Default Start Menu Offset Is 30
	 * 
	 * @param value
	 */
	public static void setOffsetY(int value) {
		_OffSetY = value;
	}

	public static int getOffsetY() {
		return _OffSetY;
	}

	public PositionWindow(Window window) {
		if(window == null) {
			throw new RuntimeException("Varaiable[frame] - Is Null");
		}

		_Window = window;
	}

	/**
	 * Centers Window On Default Screen
	 */
	public void toCenter() {
		double X = (_THIS_SCREEN_.getWidth() - _Window.getWidth()) / 2;
		double Y = (_THIS_SCREEN_.getHeight() - _Window.getHeight()) / 2;

		setLocation(X, Y);
	}

	/**
	 * Centers Window On Left Side Of Default Screen
	 */
	public void toLeftCenter() {
		double Y = (_THIS_SCREEN_.getHeight() - _Window.getHeight()) / 2;

		setLocation(0, Y);
	}

	/**
	 * Centers Window On Right Side Of Default Screen
	 */
	public void toRightCenter() {
		double X = (_THIS_SCREEN_.getWidth() - _Window.getWidth());
		double Y = (_THIS_SCREEN_.getHeight() - _Window.getHeight()) / 2;

		setLocation(X, Y);
	}

	/**
	 * Puts Window In Upper Left Corner
	 */
	public void toUpperLeft() {
//        setLocation(0, 0);
		setLocation(0, 0 + _OffSetY);
	}

	/**
	 * Puts Window In Upper Right Corner
	 */
	public void toUpperRight() {
		double X = (_THIS_SCREEN_.getWidth() - _Window.getWidth());

		setLocation(X + _OffSetX, 0 + _OffSetY);
	}

	/**
	 * Puts Window In Lower Left Corner
	 */
	public void toLowerLeft() {
		double Y = (_THIS_SCREEN_.getHeight() - _Window.getHeight());

		setLocation(0 - _OffSetX, Y - _OffSetY);
	}

	/**
	 * Puts Window In Lower Right Corner
	 */
	public void toLowerRight() {
		double X = (_THIS_SCREEN_.getWidth() - _Window.getWidth());
		double Y = (_THIS_SCREEN_.getHeight() - _Window.getHeight());

		setLocation(X, Y - _OffSetY);
	}

	public void putOnOutSideTopCenterOf(String windowsname) {
		putOnOutSideTopCenterOf(getWindowByName(windowsname));
	}

	public void putOnOutSideTopCenterOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + (window.getWidth() / 2) - (_Window.getWidth() / 2);
		double Y = window.getY() - (_Window.getHeight());

		setLocation(X, Y);
	}

	public void putOnOutSideBottomCenterOf(String windowsname) {
		putOnOutSideBottomCenterOf(getWindowByName(windowsname));
	}

	public void putOnOutSideBottomCenterOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + (window.getWidth() / 2) - (_Window.getWidth() / 2);
		double Y = window.getY() + (window.getHeight());

		setLocation(X, Y);
	}

//System.out.println("frame(" + frame.getName() + "): x=" + frame.getX() + ",y=" + frame.getY() + ",w=" + frame.getWidth() + ",h=" + frame.getHeight());
//System.out.println("_Frame(" + _Frame.getName() + "): x=" + _Frame.getX() + ",y=" + _Frame.getY() + ",w=" + _Frame.getWidth() + ",h=" + _Frame.getHeight());

	public void putToLeftUpperOf(String windowsname) {
		putToLeftUpperOf(getWindowByName(windowsname));
	}

	public void putToLeftUpperOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() - _Window.getWidth();
		double Y = window.getY();

		setLocation(X, Y);
	}

	public void putToOutSideLeftCenterOf(String windowsname) {
		putToOutSideLeftCenterOf(getWindowByName(windowsname));
	}

	public void putToOutSideLeftCenterOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() - _Window.getWidth();
		double Y = window.getY() + (window.getHeight() / 2) - _Window.getHeight() / 2;

		setLocation(X, Y);
	}

	public void putToLeftLowerOf(String windowsname) {
		putToLeftLowerOf(getWindowByName(windowsname));
	}

	public void putToLeftLowerOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() - _Window.getWidth();
		double Y = (window.getY() + window.getHeight()) - (_Window.getHeight());

		setLocation(X, Y);
	}

	public void putToOutSideRightUpperOf(String windowsname) {
		putToOutSideRightUpperOf(getWindowByName(windowsname));
	}

	public void putToOutSideRightUpperOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + window.getWidth();
		//double Y = frame.getY() + (frame.getHeight() / 2) - _Frame.getHeight() / 2;
		double Y = window.getY();
		setLocation(X, Y);
	}

	public void putToOutSideRightCenterOf(String windowsname) {
		putToOutSideRightCenterOf(getWindowByName(windowsname));
	}

	public void putToOutSideRightCenterOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + window.getWidth();
		double Y = window.getY() + (window.getHeight() / 2) - _Window.getHeight() / 2;
		setLocation(X, Y);
	}

	public void putToOutSideRightLowerOf(String windowsname) {
		putToOutSideRightLowerOf(getWindowByName(windowsname));
	}

	public void putToOutSideRightLowerOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + window.getWidth();
		double Y = (window.getY() + window.getHeight()) - (_Window.getHeight());

		setLocation(X, Y);
	}

	public void putToInCenterOf(String windowsname) {
		putToInCenterOf(getWindowByName(windowsname));
	}

	public void putToInCenterOf(Window window) {
		if(window == null) {
			throw new RuntimeException("Variable[window] - Is Null");
		}

		double X = window.getX() + window.getWidth() / 2 - _Window.getWidth() / 2;
		double Y = window.getY() + (window.getHeight() / 2) - _Window.getHeight() / 2;

		setLocation(X, Y);
	}

	public void setSize(int width, int height) {
		_Window.setSize(width, height);
	}

	public void setLocation(int x, int y) {
		_Window.setLocation(x, y);
	}

	public void setLocation(double x, double y) {
		_Window.setLocation((int)x, (int)y);
	}

	public void setLocation(int x, double y) {
		_Window.setLocation(x, (int)y);
	}

	public void setLocation(double x, int y) {
		_Window.setLocation((int)x, y);
	}

	public void bump(int bumpx, int bumpy) {
		_Window.setLocation(_Window.getX() + bumpx, _Window.getY() + bumpy);
	}

	public void bumpX(int bump) {
		_Window.setLocation(_Window.getX() + bump, _Window.getY());
	}

	public void bumpY(int bump) {
		_Window.setLocation(_Window.getX(), _Window.getY() + bump);
	}

	public int getX() {
		return _Window.getX();
	}

	public int getY() {
		return _Window.getY();
	}

	public int getHeight() {
		return _Window.getHeight();
	}

	public int getWidth() {
		return _Window.getWidth();
	}

	public Window getWindow() {
		return _Window;
	}

	public void setLocation(Position value) {
		setLocation(value, null);
	}

	public void setLocation(Position value, Window owerwindow) {
		if(owerwindow == null) {
			switch(value) {
				case TO_UPPER_LEFT:
					toUpperLeft();
					break;
				case TO_LOWER_LEFT:
					toLowerLeft();
					break;
				case TO_UPPER_RIGHT:
					toUpperRight();
					break;
				case TO_LOWER_RIGHT:
					toLowerRight();
					break;
				case TO_LEFT_CENTER:
					toLeftCenter();
					break;
				case TO_RIGHT_CENTER:
					toRightCenter();
					break;
				case TO_CENTER:
				default:
					toCenter();
					break;
			}
		} else {
			switch(value) {
				case PUT_ON_TOP_CENTER_OF:
					putOnOutSideTopCenterOf(owerwindow);
					break;
				case PUT_ON_BOTTOM_CENTER_OF:
					putOnOutSideBottomCenterOf(owerwindow);
					break;
				case PUT_TO_OUT_LEFT_UPPER_OF:
					putToLeftUpperOf(owerwindow);
					break;
				case PUT_TO_OUT_LEFT_CENTER_OF:
					putToOutSideLeftCenterOf(owerwindow);
					break;
				case PUT_TO_OUT_LEFT_LOWER_OF:
					putToLeftLowerOf(owerwindow);
					break;
				case PUT_TO_OUT_RIGHT_UPPER_OF:
					putToOutSideRightUpperOf(owerwindow);
					break;
				case PUT_TO_OUT_RIGHT_CENTER_OF:
					putToOutSideRightCenterOf(owerwindow);
					break;
				case PUT_TO_OUT_RIGHT_LOWER_OF:
					putToOutSideRightLowerOf(owerwindow);
					break;
				case PUT_TO_IN_CENTER_OF:
				default:
					putToInCenterOf(owerwindow);
			}
		}
	}

	public void toPercentOfScreen(int width, double heightpercent) {
		final int HEIGHT = (int)(_THIS_SCREEN_.getHeight() * (heightpercent > 1.0 ? 1.0 : (heightpercent < 0.0 ? 0.25 : heightpercent)));

		_Window.setSize(width, HEIGHT);
	}

	public void toPercentOfScreen(double widthpercent, int height) {
		final int WIDTH = (int)(_THIS_SCREEN_.getWidth() * (widthpercent > 1.0 ? 1.0 : (widthpercent < 0.0 ? 0.25 : widthpercent)));

		_Window.setSize(WIDTH, height);
	}

	public void toPercentOfScreen(double widthpercent, double heightpercent) {
		if(widthpercent > 1.0 || widthpercent < 0.0) {
			throw new RuntimeException("Variable[widthpercent] - Out of Range Must Be <= 1.0 And > 0.0)");
		}

		if(heightpercent > 1.0 || heightpercent < 0.0) {
			throw new RuntimeException("Variable[heightpercent] - Out of Range Must Be <= 1.0 And > 0.0)");
		}

		final int WIDTH = (int)(_THIS_SCREEN_.getWidth() * widthpercent);
		final int HEIGHT = (int)(_THIS_SCREEN_.getHeight() * heightpercent);

		_Window.setSize(WIDTH, HEIGHT);
	}

	public void toPercentOfScreen(int widthpercent, int heightpercent) {
		if(widthpercent > 100 || widthpercent < 0) {
			throw new RuntimeException("Variable[widthpercent] - Out of Range Must Be <= 1.0 And > 0.0)");
		}

		if(heightpercent > 100 || heightpercent < 0) {
			throw new RuntimeException("Variable[heightpercent] - Out of Range Must Be <= 1.0 And > 0.0)");
		}

		final int WIDTH = (int)((_THIS_SCREEN_.getWidth() * (widthpercent * 0.01)));
		final int HEIGHT = (int)((_THIS_SCREEN_.getHeight() * (heightpercent * 0.01)));

		_Window.setSize(WIDTH, HEIGHT);
	}

	/**
	 * Format Ex. toCenter, XY(0,0), putToInCenterOf(Window's Name);
	 */
	public boolean parsePosition(String position) {
		Position Location = null;
		for(Position Pos : Position.values()) {
			if(position.equalsIgnoreCase(Pos.getValue())) {
				Location = Pos;
				break;
			}
		}

		if(Location != null) {
			if(Location.getValue().startsWith("put")) {
				int IndexStart = position.indexOf('(');
				int IndexEnd = position.indexOf(')');
				final String WINDOW_NAME = position.substring(IndexStart + 1, IndexEnd);

				if(IndexStart != -1 && IndexEnd != -1) {
					final Window WINDOW = getWindowByName(WINDOW_NAME);

					if(WINDOW != null) {
						setLocation(Location, WINDOW);
					}
				}
				return false;
			} else {
				setLocation(Location);
				return true;
			}
		} else {
			return parseXY(position);
		}
	}

	private boolean parseXY(String position) {
		final int INDEX_START = position.indexOf('(');
		final int INDEX_END = position.indexOf(')');
		MyStringBuffer NumX = new MyStringBuffer(8);
		MyStringBuffer NumY = new MyStringBuffer(8);
		boolean XContainsPeriod = false;
		boolean YContainsPeriod = false;

		if(INDEX_START == -1 || INDEX_END == -1) {
			final int COMMA_INDEX = position.indexOf(',');

			if(COMMA_INDEX == -1) {//ERROR
				return false;
			} else {
				for(int X = (COMMA_INDEX - 1); X != -1; X--) {
					final char C = position.charAt(X);

					if(Character.isDigit(C)) {
						NumX.append(C);
					} else if(C == '.') {
						XContainsPeriod = true;
						NumX.append(C);
					}
				}

				for(int Y = (COMMA_INDEX + 1); Y < position.length(); Y++) {
					final char C = position.charAt(Y);

					if(Character.isDigit(C)) {
						NumY.append(C);
					} else if(C == '.') {
						YContainsPeriod = true;
						NumY.append(C);
					}
				}

				NumX.reverse();
			}
		} else {
			final String TSTRING = position.substring(INDEX_START + 1, INDEX_END);

			boolean XPos = true;
			for(int X = 0; X < TSTRING.length(); X++) {
				char C = TSTRING.charAt(X);

				if(C == ' ' || Character.isLetter(C)) {
					continue;
				} else if(C == ',') {
					XPos = false;
					continue;
				} else if(C == '.') {
					if(XPos) {
						XContainsPeriod = true;
					} else {
						YContainsPeriod = true;
					}
				}

				(XPos ? NumX : NumY).append(C);
			}
		}

		int X = 0;
		int Y = 0;
		try {
			X = (XContainsPeriod ? (int)Double.parseDouble(NumX.toString()) : Integer.parseInt(NumX.toString()));
			Y = (YContainsPeriod ? (int)Double.parseDouble(NumY.toString()) : Integer.parseInt(NumY.toString()));

			setLocation(X, Y);
			return true;
		} catch(Exception e) {}

		return false;
	}

	@Override
	public String toString() {
		StringBuffer Buffer = new StringBuffer();

		Buffer.append('(');
		Buffer.append((int)_Window.getLocation().getX());
		Buffer.append(',');
		Buffer.append((int)_Window.getLocation().getY());
		Buffer.append(')');

		return Buffer.toString();
	}

	public static Window getWindowByName(String name) {
		return getWindowByName(name, true);
	}

	public static Window getWindowByName(String name, boolean matchcase) {
		final Window[] ALLWINDOWS = Window.getWindows();

		final String NAME = (matchcase ? name : name.toLowerCase());

		for(int X = 0; X < ALLWINDOWS.length; X++) {
			final String W_NAME = (matchcase ? ALLWINDOWS[X].getName() : ALLWINDOWS[X].getName().toLowerCase());

			if(W_NAME.equals(NAME)) {
				return ALLWINDOWS[X];
			}
		}

		return null;
	}

	public static PositionWindow quickToCenter(Window window) {
		final PositionWindow P = new PositionWindow(window);

		P.toCenter();

		return P;
	}

	public static PositionWindow quickToCenterOf(Window owner, Window window) {
		final PositionWindow P = new PositionWindow(window);

		P.putToInCenterOf(owner);

		return P;
	}

	public static PositionWindow quickToPercentOfScreen(Window window, double widthpercent, double heightpercent) {
		final PositionWindow P = new PositionWindow(window);

		P.toPercentOfScreen(widthpercent, heightpercent);
		P.toCenter();

		return P;
	}

	public static PositionWindow quickToPercentOfScreen(Window window, int widthpercent, int heightpercent) {
		final PositionWindow P = new PositionWindow(window);

		P.toPercentOfScreen(widthpercent, heightpercent);
		P.toCenter();

		return P;
	}

	private boolean parsePositionBeta(String position) {
		Position Location = null;
		for(Position Pos : Position.values()) {
			if(position.equalsIgnoreCase(Pos.getValue())) {
				Location = Pos;
				break;
			}
		}

		if(Location == null) {
			return parseXYBeta(position);
		} else {
			if(Location.getValue().startsWith("put")) {
				final int I_START = position.indexOf('(');
				final int I_END = position.indexOf(')');

				if(I_START != -1 && I_END != -1) {
					final String WINDOW_NAME = position.substring(I_START + 1, I_END);
					final Window WINDOW = getWindowByName(WINDOW_NAME);

					if(WINDOW != null) {
						setLocation(Location, WINDOW);
					}
				}
				return false;
			} else {
				setLocation(Location);
				return true;
			}
		}
	}

	//must be only characters
	//must not contain spaces between characters 
	private static String readName(String str, IntByRef offset) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		for(; offset.value < str.length(); offset.value++) {
			if(Character.isLetter(str.charAt(offset.value))) {
				BUFFER.append(str.charAt(offset.value));
			} else if(str.charAt(offset.value) == ' ') {
				if(BUFFER.length() > 0) {
					StringUtil.skipsWhiteSpaces(str, offset);

					if(str.charAt(offset.value) == '(') {
						break;
					} else {
						throw new RuntimeException("Invalid Name");
					}
				}
			} else if(str.charAt(offset.value) == '(') {
				break;
			} else {
				throw new RuntimeException("Invalid Character: " + str.charAt(offset.value));
			}
		}

		if(BUFFER.length() == 0) {
			throw new RuntimeException("No Name Method");
		} else {
			return BUFFER.toString();
		}
	}

//setName(Justin, Palinkas) Valid
//setName(Justin  , Palinkas) Valid
//setName   (   Justin, Palinkas) Valid
//setName(  Justin   , Palinkas   ) Valid
//setName(  Just in   , Pa link as) Not Valid

	private static String[] readArguments(String str, IntByRef offset) {

		return null;
	}

	private static String readArgument(String str, IntByRef offset) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		return BUFFER.toString();
	}

	private boolean parseXYBeta(String position) {
		if(!position.startsWith("XY")) {
			//ERROR
		}

		int PosX = 0;
		int PosY = 0;

		final int INDEX_START = position.indexOf('(');
		final int INDEX_END = position.indexOf(')');
		MyStringBuffer NumX = new MyStringBuffer(8);
		MyStringBuffer NumY = new MyStringBuffer(8);
		boolean XContainsPeriod = false;
		boolean YContainsPeriod = false;

		if(INDEX_START == -1 || INDEX_END == -1) {
			final int COMMA_INDEX = position.indexOf(',');

			if(COMMA_INDEX == -1) {//ERROR
				return false;
			} else {
				for(int X = (COMMA_INDEX - 1); X != -1; X--) {
					final char C = position.charAt(X);

					if(Character.isDigit(C)) {
						NumX.append(C);
					} else if(C == '.') {
						XContainsPeriod = true;
						NumX.append(C);
					}
				}

				for(int Y = (COMMA_INDEX + 1); Y < position.length(); Y++) {
					final char C = position.charAt(Y);

					if(Character.isDigit(C)) {
						NumY.append(C);
					} else if(C == '.') {
						YContainsPeriod = true;
						NumY.append(C);
					}
				}

				NumX.reverse();
			}
		} else {
			final String TSTRING = position.substring(INDEX_START + 1, INDEX_END);

			boolean XPos = true;
			for(int X = 0; X < TSTRING.length(); X++) {
				char C = TSTRING.charAt(X);

				if(C == ' ' || Character.isLetter(C)) {
					continue;
				} else if(C == ',') {
					XPos = false;
					continue;
				} else if(C == '.') {
					if(XPos) {
						XContainsPeriod = true;
					} else {
						YContainsPeriod = true;
					}
				}

				(XPos ? NumX : NumY).append(C);
			}
		}

		try {
			PosX = (XContainsPeriod ? (int)Double.parseDouble(NumX.toString()) : Integer.parseInt(NumX.toString()));
			PosY = (YContainsPeriod ? (int)Double.parseDouble(NumY.toString()) : Integer.parseInt(NumY.toString()));

			setLocation(PosX, PosY);
			return true;
		} catch(Exception e) {}

		return false;
	}

	// Valid
	// Valid
	// Valid
	// Valid
	// Not Valid
	public static void main(String[] args) {
		System.out.println(readName("setName(Justin, Palinkas)", new IntByRef(0)));
		System.out.println(readName("             setName            (Justin, Palinkas)", new IntByRef(0)));
		System.out.println(readName("setName  (Justin, Palinkas)", new IntByRef(0)));
		System.out.println(readName("setName (Justin, Palinkas)", new IntByRef(0)));
		System.out.println(readName("set Name(Justin, Palinkas)", new IntByRef(0)));

//    	final String[] R1 = readArguments("setName(Justin, Palinkas)", new IntByRef(7));
//    	final String[] R2 = readArguments("setName(Justin  , Palinkas)", new IntByRef(7));
//    	final String[] R3 = readArguments("setName(   Justin, Palinkas)", new IntByRef(7));
//    	final String[] R4 = readArguments("setName(  Justin   , Palinkas   )", new IntByRef(7));
//    	final String[] R5 = readArguments("setName(  Just in   , Pa link as)", new IntByRef(7));

//        javax.swing.JFrame Frm = new javax.swing.JFrame();
//        Frm.setSize(200, 200);
//        Frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//
//        PositionWindow PosWin = new PositionWindow(Frm);
//
//        PosWin.parsePositionBeta("toCenter");
//        ThreadUtil.sleep(1000 * 2);
//        
//        PosWin.parsePositionBeta("putToInCenterOf(Main)");
//        ThreadUtil.sleep(1000 * 2);
//        
//        PosWin.parsePositionBeta("putXY(Main, 100,100");
//        ThreadUtil.sleep(1000 * 2);
//        
//        PosWin.parsePositionBeta("XY(100,100");
//        ThreadUtil.sleep(1000 * 2);
//
//        Frm.setVisible(true);
	}

/*
    public static void main(String[] args) {
        final javax.swing.JFrame Parent = new javax.swing.JFrame("Parent");
        Parent.setSize(100,100);

        PositionWindow PosWin = new PositionWindow(Parent);
        PosWin.parsePosition("100,100");

        Parent.setVisible(true);
        
        //final javax.swing.JFrame Child = new javax.swing.JFrame("Child"); 
        
        Parent.setSize(400, 400);
        //Child.setSize(200, 200);
        
        final PositionWindow ParentPos = new PositionWindow(Parent);
        //final PositionWindow ChildPos = new PositionWindow(Child);
        
        Parent.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        Parent.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {}
            public void componentMoved(ComponentEvent e) {
                //ChildPos.putToOutRightUpperOf(Parent);
            }
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });
        
        Parent.setVisible(true);
        
        Timer MoveWin = new Timer(2* 1000, new ActionListener() {
            int Pos = -1;
            public void actionPerformed(ActionEvent e) {
                switch(++Pos) {
                    case 0:
                        ParentPos.toUpperLeft();
                        break;
                    case 1:
                        ParentPos.toUpperRight();
                        break;
                    case 2:
                        ParentPos.toLowerRight();
                        break;
                    case 3:
                        ParentPos.toLowerLeft();
                        break;
                        
                    default:
                       Pos = -1;
                }
            }
        });
        MoveWin.start();
        
        
        Child.setVisible(true);
    }*/
}
//        System.out.println("TkH=" + THIS_SRCEEN.getHeight());
//        System.out.println("TkW=" + THIS_SRCEEN.getWidth());
//        
//        System.out.println("GcH=" + Gc.getBounds().getHeight());
//        System.out.println("GcW=" + Gc.getBounds().getWidth());

/*
  	    	while(str.charAt(offset.value) != ')' || offset.value < str.length()) {
	    		StringUtil.skipsWhiteSpaces(str, offset);
	    		
	    		final MyStringBuffer BUFFER = new MyStringBuffer();
	    		
	    		while(str.charAt(offset.value) != ',' || offset.value < str.length()) {
	    			if(BUFFER.length() == 0) {
	    				StringUtil.skipsWhiteSpaces(str, offset);
	    			}
	    			
	    			if(str.charAt(offset.value) == ' ') {
	    				StringUtil.skipsWhiteSpaces(str, offset);
	    				
	    				if(str.charAt(offset.value) != ',') {
	    					throw new RuntimeException();
	    				} else {
	    					break;
	    				}
	    			} else {
	    				BUFFER.append(str.charAt(offset.value));
	    				offset.value++;
	    			}
	    		}
	    		
	    		offset.value++;
	    		RETURN.put(BUFFER.toString(true));
	    	}
*/
