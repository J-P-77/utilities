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

package utillib.net.connection;

import utillib.net.interfaces.ISocketFactory;

import utillib.io.MyInputStream;
import utillib.io.MyOutputStream;

import utillib.net.NetUtil;
import utillib.net.factory.SocketFactories;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <pre>
 * <b>Current Version 1.1.0
 * 
 * January 12, 2011 (version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SimpleSocketConnectionExtended extends SimpleSocketConnection {
	private MyInputStream _IStream;//Don't Add Null Too This
	private MyOutputStream _OStream;//Don't Add Null Too This

	public SimpleSocketConnectionExtended() {
		super(SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnectionExtended(Socket socket) throws IOException {
		super(socket);
	}

	public SimpleSocketConnectionExtended(ISocketFactory socketcreator) {
		super(socketcreator);
	}

	public SimpleSocketConnectionExtended(int port) throws UnknownHostException, IOException {
		super(NetUtil.getLoopbackAddress(), port, InetAddress.getLocalHost(), 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnectionExtended(String address, int port) throws UnknownHostException, IOException {
		super(address, port, null, 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnectionExtended(String address, int port, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		super(address, port, null, 0, socketcreator);
	}

	public SimpleSocketConnectionExtended(String address, int port, InetAddress localaddress, int localport, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		super(address, port, localaddress, localport, socketcreator);
	}

	public SimpleSocketConnectionExtended(InetAddress address, int port) throws UnknownHostException, IOException {
		super(address, port, null, 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnectionExtended(InetAddress address, int port, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		super(address, port, null, 0, socketcreator);
	}

	public SimpleSocketConnectionExtended(InetAddress address, int port, InetAddress localaddress, int localport, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		super(address, port, localaddress, localport, socketcreator);
	}

	@Override
	public void setSocket(Socket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		super.setSocket(socket);

		_IStream = new MyInputStream(super.getInputStream());
		_OStream = new MyOutputStream(super.getOutputStream());
	}

	public MyInputStream getMyInputStream() throws IOException {
		if(_IStream == null) {
			throw new IOException("Socket InputStream Is Closed");
		} else {
			return _IStream;
		}
	}

	public MyOutputStream getMyOutputStream() throws IOException {
		if(_OStream == null) {
			throw new IOException("Socket OutputStream Is Closed");
		} else {
			return _OStream;
		}
	}

	@Override
	public void flush() throws IOException {
		if(_OStream == null) {
			throw new IOException("OutputStream Is Closed");
		} else {
			_OStream.flush();
		}

		super.flush();
	}

	@Override
	public synchronized void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
			_IStream = null;
		}

		if(_OStream != null) {
			_OStream.flush();
			_OStream.close();
			_OStream = null;
		}

		super.close();
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}
}
