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

package jp77.utillib.net.serversocket;

import jp77.utillib.net.NetUtil;

import jp77.utillib.net.interfaces.IServerSocket;
import jp77.utillib.net.interfaces.ISocket;

import jp77.utillib.net.socket.MySocket;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 13, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyServerSocket implements IServerSocket {
	public static final int _OPTION_KEEPALIVE_ = 3;
	public static final int _OPTION_SOLINGER_ = 4;
	public static final int _OPTION_TCPNODELAY_ = 5;
/*
	private InetAddress _Local_Address;
	private int _Local_Port;
*/
	private ServerSocket _Socket = null;

	public MyServerSocket() throws IOException {
		_Socket = new ServerSocket();
	}

	public MyServerSocket(int port) throws UnknownHostException, IOException {
		this(NetUtil.getLoopbackAddress(), port);
	}

	public MyServerSocket(String localaddress, int localport) throws UnknownHostException, IOException {
		this(new ServerSocket(localport, 50, InetAddress.getByName(localaddress)));
	}

	public MyServerSocket(InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		this(new ServerSocket(localport, 50, localaddress));
	}

	public MyServerSocket(ServerSocket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		if(!socket.isBound()) {
			throw new RuntimeException("Variable[socket] - Not Bound");
		}

		_Socket = socket;/*
		_Local_Address = _Socket.getInetAddress();
		_Local_Port = _Socket.getLocalPort();*/
	}

	public ServerSocket getSocket() {
		return _Socket;
	}

	@Override
	public void bind(String address, int port) throws IOException {
		bind(new InetSocketAddress(address, port));
	}

	@Override
	public void bind(InetAddress address, int port) throws IOException {
		bind(new InetSocketAddress(address, port));
	}

	private void bind(InetSocketAddress address) throws IOException {
		if(!isBound()) {
			_Socket.bind(address);

			/*_Local_Address = _Socket.getInetAddress();
			_Local_Port = _Socket.getLocalPort();*/
		}
	}

	@Override
	public boolean isBound() {
		if(_Socket == null) {
			return false;
		}
		
		return _Socket.isBound();
	}

	@Override
	public ISocket accept() throws IOException {
		if(_Socket == null) {
			throw new IOException("Socket not bound");
		}
		
		return new MySocket(_Socket.accept());
	}

	@Override
	public InetAddress getLocalAddress() {
		if(_Socket == null) {
			return null;
		}
		
		return _Socket.getInetAddress();
	};

	@Override
	public int getLocalPort() {
		if(_Socket == null) {
			return -1;
		}
		
		return _Socket.getLocalPort();
	}

	@Override
	public void setOption(int option, Object value) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					_Socket.setSoTimeout((Integer)value);
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					_Socket.setReceiveBufferSize((Integer)value);
					break;
			}
		} catch(Exception e) {}
	}

	@Override
	public Object getOption(int option) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					return _Socket.getSoTimeout();

				case _OPTION_RECEIVEBUFFERSIZE_:
					return _Socket.getReceiveBufferSize();

			}
		} catch(Exception e) {}

		return null;
	}

	@Override
	public boolean isClosed() {
		if(_Socket == null) {
			return true;
		}

		return _Socket.isClosed();
	}

	@Override
	public void close() throws IOException {
		if(_Socket != null) {
			_Socket.close();
			_Socket = null;
		}
	}
}
