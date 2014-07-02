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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

import utillib.net.interfaces.IBothSocketFactory;

public class DefaultSSLBothSocketFactory implements IBothSocketFactory {
	private final SocketFactory _FACTORY = SSLSocketFactory.getDefault();
	private final ServerSocketFactory _SS_FACTORY = SSLServerSocketFactory.getDefault();

	public Socket createSocket() throws UnknownHostException, IOException {
		return _FACTORY.createSocket();
	}

	public Socket createSocket(String address, int port) throws UnknownHostException, IOException {
		return _FACTORY.createSocket(address, port);
	}

	public Socket createSocket(String address, int port, InetAddress localaddress, int localPort) throws UnknownHostException, IOException {
		return _FACTORY.createSocket(address, port, localaddress, localPort);
	}

	public Socket createSocket(InetAddress address, int port) throws UnknownHostException, IOException {
		return _FACTORY.createSocket(address, port);
	}

	public Socket createSocket(InetAddress address, int port, InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		return _FACTORY.createSocket(address, port, localaddress, localport);
	}

	public ServerSocket createServerSocket() throws UnknownHostException, IOException {
		return _SS_FACTORY.createServerSocket();
	}

	public ServerSocket createServerSocket(int port) throws UnknownHostException, IOException {
		return _SS_FACTORY.createServerSocket(port);
	}

	public ServerSocket createServerSocket(int port, int backlog) throws UnknownHostException, IOException {
		return _SS_FACTORY.createServerSocket(port, backlog);
	}

	public ServerSocket createServerSocket(int port, int backlog, InetAddress localaddress) throws UnknownHostException, IOException {
		return _SS_FACTORY.createServerSocket(port, backlog, localaddress);
	}
}
