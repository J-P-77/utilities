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

package utillib.net.factory;

import utillib.net.interfaces.IBothSocketFactory;
import utillib.net.interfaces.IServerSocketFactory;
import utillib.net.interfaces.ISocketFactory;

public class SocketFactories {
	private static final ISocketFactory _PLAIN_ = new PlainSocketFactory();
	private static final IServerSocketFactory _PLAIN_SERVER_ = new PlainServerSocketFactory();

	private static final ISocketFactory _DEFAULT_SSL_ = new DefaultSSLSocketFactory();
	private static final IServerSocketFactory _DEFAULT_SSL_SERVER_ = new DefaultSSLServerSocketFactory();

	private static final IBothSocketFactory _BOTH_PLAIN_ = new PlainBothSocketFactory();
	private static final IBothSocketFactory _BOTH_DEFAULT_SSL_ = new DefaultSSLBothSocketFactory();

	public static ISocketFactory getPlainSocketFactory() {
		return _PLAIN_;
	}

	public static IServerSocketFactory getPlainServerSocketFactory() {
		return _PLAIN_SERVER_;
	}

	public static ISocketFactory getDefaultSSLSocketFactory() {
		return _DEFAULT_SSL_;
	}

	public static IServerSocketFactory getDefaultSSLServerSocketFactory() {
		return _DEFAULT_SSL_SERVER_;
	}

	public static IBothSocketFactory getPlainBothSocketFactory() {
		return _BOTH_PLAIN_;
	}

	public static IBothSocketFactory getDefaultSSLBothSocketFactory() {
		return _BOTH_DEFAULT_SSL_;
	}
}
