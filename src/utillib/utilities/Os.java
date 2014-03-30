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

import utillib.file.FileUtil;

public class Os {
	public static enum Os_Version {
		UNKNOWN("Unknown"),
		WIN_98("Windows 98"),
		WIN_2000("Windows 2000"),
		WIN_XP("Windows XP"),
		WIN_VISTA("Windows Vista"),
		WIN_7("Windows 7"),
		UNIX("Unix-" + User._OS_NAME_),
		LINUX("Linux-" + User._OS_NAME_),
		MAC("Mac");

		private final String _Value;

		private Os_Version(String value) {
			_Value = value;
		}

		public String getValue() {
			return _Value;
		}
	}

	public static final boolean _IS_WINDOWS_ = Os.isMicrosoftWindows();

	public static final Os_Version _OS_;

	static {
		//Determine Operating System
		final String OS = User._OS_NAME_.toLowerCase();

		if(_IS_WINDOWS_) {
			if(OS.indexOf("98") != -1) {
				_OS_ = Os_Version.WIN_98;
			} else if(OS.indexOf("2000") != -1) {
				_OS_ = Os_Version.WIN_2000;
			} else if(OS.indexOf("xp") != -1) {
				_OS_ = Os_Version.WIN_XP;
			} else if(OS.indexOf("vista") != -1) {
				_OS_ = Os_Version.WIN_VISTA;
			} else if(OS.indexOf("7") != -1) {
				_OS_ = Os_Version.WIN_7;
			} else {
				_OS_ = Os_Version.UNKNOWN;
			}
		} else {
			if(OS.indexOf("mac") != -1) {
				_OS_ = Os_Version.MAC;
			} else if(OS.indexOf("unix") != -1) {
				_OS_ = Os_Version.UNIX;
			} else if(OS.indexOf("linux") != -1) {
				_OS_ = Os_Version.LINUX;
			} else if(FileUtil._S_c == '/') {
				_OS_ = Os_Version.LINUX;
			} else {
				_OS_ = Os_Version.UNKNOWN;
			}
		}
	}

	public static boolean isWindows() {
		return _IS_WINDOWS_;
	}

	public static Os_Version getOS() {
		return _OS_;
	}

	public static String getOSVersionStr() {
		return _OS_.getValue();
	}

	public static boolean isOs(String os) {
		final String OS = os.toLowerCase();
		final String OS_VER = _OS_.getValue().toLowerCase();

		if(OS_VER.indexOf(OS) == -1) {
			for(int X = 0; X < Os_Version.values().length; X++) {
				final String T_OS = Os_Version.values()[X].getValue();

				if(OS.equals(T_OS)) {
					return true;
				}
			}

			return false;
		} else {
			return true;
		}
	}

	private static boolean isMicrosoftWindows() {
		final String OS = User._OS_NAME_.toLowerCase();

		if(OS.indexOf("windows") != -1) {
			return true;
		}

		return false;
	}
}
