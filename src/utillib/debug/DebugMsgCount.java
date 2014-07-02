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

/**
 * 
 * @author Justin Palinkas
 */
public class DebugMsgCount {
	private int _ErrorCount = 0;
	private int _WarningCount = 0;
	private int _InformationCount = 0;
	private int _OtherCount = 0;
	private int _UnknownCount = 0;

	public void addError() {
		_ErrorCount += 1;
	}

	public void addError(int value) {
		_ErrorCount += value;
	}

	public int getErrorCount() {
		return _ErrorCount;
	}

	public void addWarning() {
		_WarningCount += 1;
	}

	public void addWarning(int value) {
		_WarningCount += value;
	}

	public int getWarningCount() {
		return _WarningCount;
	}

	public void addInformation() {
		_InformationCount += 1;
	}

	public void addInformation(int value) {
		_InformationCount += value;
	}

	public int getInformationCount() {
		return _InformationCount;
	}

	public void addOther() {
		_OtherCount += 1;
	}

	public void addOther(int value) {
		_OtherCount += value;
	}

	public int getOtherCount() {
		return _OtherCount;
	}

	public void addUnknown() {
		_UnknownCount += 1;
	}

	public void addUnknown(int value) {
		_UnknownCount += value;
	}

	public int getUnknownCount() {
		return _UnknownCount;
	}
}
