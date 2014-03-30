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

package beta.utillib;

/**
 * 
 * @author Dalton Dell
 */
public interface IPropertyEvent {
	/**
	 * You Can Not Consume This Event Value Change
	 */
	public static final int _EVENT_ADDED_ = 0;

	/**
	 * You Can Not Consume This Event Value Change
	 */
	public static final int _EVENT_REMOVED_ = 1;

	/**
	 * You Can Not Consume This Event Value Change
	 */
	public static final int _EVENT_CHANGED_ = 2;

	/**
	 * <pre>
	 * You Can Consume This Event Value Change
	 *      (Use This Event To Test If New Value Is Valid or Not)
	 * </pre>
	 */
	public static final int _EVENT_CHANGING_ = 3;

	public int getEventId();

	public int getPropertyId();

	public Object getSource();

	public Object getNewValue();

//    public Object getOldValue();

	public void consume();

	public boolean hasBeenConsumed();
}
