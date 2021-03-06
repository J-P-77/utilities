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

package jp77.utillib.net.interfaces;

import jp77.utillib.interfaces.IClose;
import jp77.utillib.lang.byref.IntByRef;
import jp77.utillib.lang.byref.TByRef;

import java.io.IOException;
import java.net.InetAddress;

public interface ISocket extends IClose {
	public static final int _OPTION_SOTIMEOUT_ = 0;
	public static final int _OPTION_SENDBUFFERSIZE_ = 1;
	public static final int _OPTION_RECEIVEBUFFERSIZE_ = 2;

	public InetAddress getLocalAddress();

	public int getLocalPort();

	public InetAddress getRemoteAddress();

	public int getRemotePort();

	public void bind(String address, int port) throws IOException;

	public void bind(InetAddress address, int port) throws IOException;

	public boolean isBound();

	public void connect(String address, int port) throws IOException;

	public void connect(InetAddress address, int port) throws IOException;

	public boolean isConnected();

	public void setOption(int option, Object value);

	public Object getOption(int option);

	public void close() throws IOException;

	public boolean isClosed();

	//=================================================================================
//	public void send(byte[] buffer, int offset, int length) throws IOException;
//	public void send(byte[] buffer, int offset, int length, InetAddress toaddress, int toport) throws IOException;
//	
//	public int receive(byte[] buffer, int offset, int length) throws IOException;
//	public int receive(byte[] buffer, int offset, int length, TByRef<InetAddress> fromaddress, IntByRef fromport) throws IOException;
}
