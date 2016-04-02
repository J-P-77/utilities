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

import jp77.utillib.utilities.SystemProperties.Property;

public interface User {
	public static final int _SYSTEM_EXIT_NORMAL_ = 0;
	public static final int _SYSTEM_EXIT_ERROR_ = 1;

	public static final String _APP_PATH_ = SystemProperties.getProperty(Property.APP_PATH);

	public static final String _JAVA_HOME_ = SystemProperties.getProperty(Property.JAVA_HOME);
	public static final String _JAVA_VENDOR_ = SystemProperties.getProperty(Property.JAVA_VENDOR);
	public static final String _JAVA_VENDOR_URL_ = SystemProperties.getProperty(Property.JAVA_VENDOR_URL);
	public static final String _JAVA_VERSION_ = SystemProperties.getProperty(Property.JAVA_VERSION);
	public static final String _JAVA_CLASS_PATH_ = SystemProperties.getProperty(Property.JAVA_CLASS_PATH);

	public static final String _OS_ARCH_ = SystemProperties.getProperty(Property.OS_ARCH);
	public static final String _OS_NAME_ = SystemProperties.getProperty(Property.OS_NAME);
	public static final String _OS_VERSION_ = SystemProperties.getProperty(Property.OS_VERSION);

	public static final String _USER_HOME_ = SystemProperties.getProperty(Property.USER_HOME);
	public static final String _USER_NAME_ = SystemProperties.getProperty(Property.USER_NAME);
}
