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

package utillib.debug;

/**
 * 
 * @author Justin Palinkas
 */
public class DebugSettting {
	private boolean _Insert_Date_And_Time = false;
	private boolean _Write_To_File = false;
	private boolean _Print_To_Debug_Window = false;
	private boolean _Print_To_Output = false;
	private boolean _Show_Window = false;

	private int _DebugLevel = 0;

	public void setDebugLevel(int loglevel) {
		_DebugLevel = loglevel;
	}

	public int getDebugLevel() {
		return _DebugLevel;
	}

	public void enableInsertDateAndTime(boolean value) {
		_Insert_Date_And_Time = value;
	}

	public boolean isEnabledInsertDateAndTime() {
		return _Insert_Date_And_Time;
	}

	public void enableWriteToFile(boolean value) {
		_Write_To_File = value;
	}

	public boolean isEnabledWriteToFile() {
		return _Write_To_File;
	}

	public void enablePrintToDebugWindow(boolean value) {
		_Print_To_Debug_Window = value;
	}

	public boolean isEnabledPrintToDebugWindow() {
		return _Print_To_Debug_Window;
	}

	public void enablePrintToOutput(boolean value) {
		_Print_To_Output = value;
	}

	public boolean isEnabledPrintToOutput() {
		return _Print_To_Output;
	}

	public void enableShowWindow(boolean value) {
		_Show_Window = value;
	}

	public boolean isEnabledShowWindow() {
		return _Show_Window;
	}
}
