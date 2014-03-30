/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.utilities;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import java.awt.Color;

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
public class ColorUtil {
	public static final int _COLOR_MIN_ = 0;
	public static final int _COLOR_MAX_ = 255;

	public static Color convertStrToColor(String color) {
		return convertStrToColor(color, ",");
	}

	public static Color convertStrToColor(String color, String spliter) {
		if(color == null) {
			return null;
		}

		color = color.toLowerCase();

		if(color.equals("")) {
			return null;
		} else if(color.equals("gray")) {
			return Color.GRAY;
		} else if(color.equals("white")) {
			return Color.WHITE;
		} else if(color.equals("orange")) {
			return Color.ORANGE;
		} else if(color.equals("lightgray")) {
			return Color.LIGHT_GRAY;
		} else if(color.equals("red")) {
			return Color.RED;
		} else if(color.equals("green")) {
			return Color.GREEN;
		} else if(color.equals("blue")) {
			return Color.BLUE;
		} else if(color.equals("pink")) {
			return Color.PINK;
		} else if(color.equals("darkgray")) {
			return Color.DARK_GRAY;
		} else if(color.equals("black")) {
			return Color.BLACK;
		} else if(color.equals("yellow")) {
			return Color.YELLOW;
		} else {
			String[] Colors = color.split(spliter, 3);

			int Red = -1;
			int Green = -1;
			int Blue = -1;
			try {
				Red = Integer.parseInt(Colors[0]);
				Green = Integer.parseInt(Colors[1]);
				Blue = Integer.parseInt(Colors[2]);

				Red = ((Red < _COLOR_MIN_ || Red > _COLOR_MAX_) ? -1 : Red);
				Green = ((Green < _COLOR_MIN_ || Green > _COLOR_MAX_) ? -1 : Green);
				Blue = ((Blue < _COLOR_MIN_ || Blue > _COLOR_MAX_) ? -1 : Blue);
			} catch(Exception e) {
				Red = -1;
				Green = -1;
				Blue = -1;
			}

			if(Red != -1 && Green != -1 && Blue != -1) {
				return new Color(Red, Green, Blue);
			} else {
				return null;
			}
		}
	}

	public static Color convertStrToColor2(String color) {
		if(color == null) {
			return null;
		}

		final String COLOR = color.toLowerCase();

		if(COLOR.equals("")) {
			return null;
		} else if(COLOR.equals("gray")) {
			return Color.GRAY;
		} else if(COLOR.equals("white")) {
			return Color.WHITE;
		} else if(COLOR.equals("orange")) {
			return Color.ORANGE;
		} else if(COLOR.equals("lightgray")) {
			return Color.LIGHT_GRAY;
		} else if(COLOR.equals("red")) {
			return Color.RED;
		} else if(COLOR.equals("green")) {
			return Color.GREEN;
		} else if(COLOR.equals("blue")) {
			return Color.BLUE;
		} else if(COLOR.equals("pink")) {
			return Color.PINK;
		} else if(COLOR.equals("darkgray")) {
			return Color.DARK_GRAY;
		} else if(COLOR.equals("black")) {
			return Color.BLACK;
		} else if(COLOR.equals("yellow")) {
			return Color.YELLOW;
		} else {
			int[] Colors = new int[3];

			int CurColor = 0;
			MyStringBuffer Buffer = new MyStringBuffer(Colors.length);

			for(int X = 0; X < COLOR.length() && CurColor < Colors.length; X++) {
				X = StringUtil.skipsWhiteSpaces(COLOR, X);

				final char CHAR = COLOR.charAt(X);

				if(Character.isDigit(CHAR)) {
					Buffer.append(CHAR);
				} else if(CHAR == ',' || CHAR == ' ') {
					Colors[CurColor++] = Integer.parseInt(Buffer.toString());

					Buffer.reset();
				} else {
					return null;
				}
			}

			if(Buffer.length() > 0) {
				Colors[CurColor] = Integer.parseInt(Buffer.toString());
			}

			for(int X = 0; X < Colors.length; X++) {
				if(Colors[X] < _COLOR_MIN_ || Colors[X] > _COLOR_MAX_) {
					return null;
				}
			}

			return new Color(Colors[0], Colors[1], Colors[2]);
		}
	}

//red, green, and blue
	public static String convertColorToStr(Color color) {
		if(color == null) {
			return "";
		}

		return color.getRed() + ',' + +color.getGreen() + ',' + color.getBlue() + "";
	}
}
