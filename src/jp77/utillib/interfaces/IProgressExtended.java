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

package jp77.utillib.interfaces;

/**
 * 
 * @author Justin Palinkas
 */
public interface IProgressExtended extends IProgress {
/*
	public void decrement();
	public void decrement(int value);
*/
//
	@Deprecated
	public void setVisible(boolean value);

	@Deprecated
	public boolean isVisible();

//
	/**
	 * Destory's This Instance Of This Oject
	 */
	@Deprecated
	public void destroy();

	/**
	 * Tells Operation to Cancel
	 */
	public void cancel();

	/**
	 * Operation Is Attempting to Cancel Current Progress
	 * 
	 * @return
	 */
	public boolean isCanceling();

	/**
	 * Flag To Tell The Operation That It Has Canceled Current Progress
	 */
	public void canceled();

	/**
	 * Operation Has Canceled Current Progress
	 * 
	 * @return
	 */
	public boolean isCanceled();
}
