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

package jp77.utillib.net.connection;

import jp77.utillib.net.interfaces.ISocketFactory;

import jp77.utillib.net.NetUtil;
import jp77.utillib.net.factory.SocketFactories;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
 * November 5, 2011 (version 1.1.0)
 *     -Fixed bug
 *         -reconnect() method would reconnect to local address and port instead of remote address and port
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SimpleSocketConnection {
	private ISocketFactory _SocketFactory = SocketFactories.getPlainSocketFactory();

	private Socket _Socket = null;

	private InetAddress _Local_Address;
	private int _Local_Port;

	private InetAddress _Remote_Address;
	private int _Remote_Port;

	public SimpleSocketConnection() {
		this(SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnection(Socket socket) throws IOException {
		setSocket(socket);
	}

	public SimpleSocketConnection(ISocketFactory socketcreator) {
		setSocketFactory(socketcreator);
	}

	public SimpleSocketConnection(int port) throws UnknownHostException, IOException {
		this(NetUtil.getLoopbackAddress(), port, InetAddress.getLocalHost(), 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnection(String address, int port) throws UnknownHostException, IOException {
		this(address, port, null, 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnection(String address, int port, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		this(address, port, null, 0, socketcreator);
	}

	public SimpleSocketConnection(String address, int port, InetAddress localaddress, int localport, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		if(address == null) {
			throw new RuntimeException("Variable[address] - Is Null");
		}

		if(!NetUtil.validPort(port)) {
			throw new RuntimeException("Variable[port] - Invalid Port");
		}

		if(!NetUtil.validPort(localport)) {
			throw new RuntimeException("Variable[localport] - Invalid Port");
		}

		setSocketFactory(socketcreator);
		connect(address, port, localaddress, localport);
	}

	public SimpleSocketConnection(InetAddress address, int port) throws UnknownHostException, IOException {
		this(address, port, null, 0, SocketFactories.getPlainSocketFactory());
	}

	public SimpleSocketConnection(InetAddress address, int port, InetAddress localaddress, int localport, ISocketFactory socketcreator) throws UnknownHostException, IOException {
		if(address == null) {
			throw new RuntimeException("Variable[address] - Is Null");
		}

		if(!NetUtil.validPort(port)) {
			throw new RuntimeException("Variable[port] - Invalid Port");
		}

//    	if(localaddress == null) {
//            throw new RuntimeException("Variable[localaddress] - Is Null");
//      }

		if(!NetUtil.validPort(localport)) {
			throw new RuntimeException("Variable[localport] - Invalid Port");
		}

		setSocketFactory(socketcreator);
		connect(address, port, localaddress, localport);
	}

	public void setSocketFactory(ISocketFactory socketfactory) {
		if(socketfactory == null) {
			throw new RuntimeException("Variable[socketfactory] - Is Null");
		}

		_SocketFactory = socketfactory;
	}

	public ISocketFactory getSocketFactory() {
		return _SocketFactory;
	}

	public void canConnect() throws IOException {
		if(_Socket != null) {
			throw new IOException("Allready Connected");
		}

		if(_SocketFactory == null) {
			throw new IOException("No Socket Factory");
		}
	}

	public void connect(int port) throws UnknownHostException, IOException {
		connect(InetAddress.getLocalHost(), port);
	}

	public void connect(String address, int port) throws UnknownHostException, IOException {
		connect(address, port, null, 0);
	}

	public void connect(String address, int port, InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		canConnect();

		setSocket(_SocketFactory.createSocket(address, port, localaddress, localport));
	}

	public void connect(InetAddress address, int port) throws UnknownHostException, IOException {
		connect(address, port, null, 0);
	}

	public void connect(InetAddress address, int port, InetAddress localaddress) throws UnknownHostException, IOException {
		connect(address, port, localaddress, 0);
	}

	public void connect(InetAddress address, int port, InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		canConnect();

		setSocket(_SocketFactory.createSocket(address, port, localaddress, localport));
	}

	public void reconnect() throws UnknownHostException, IOException {
		reconnect(false);
	}

	public void reconnect(boolean reuselocal) throws UnknownHostException, IOException {
		if(_Local_Address == null || _Remote_Address == null) {
			throw new RuntimeException("Variable[_Local_Address | _Remote_Address] - Never Been Connected");
		}

		if(!isClosed()) {
			close();
		}

		if(reuselocal) {
			connect(_Remote_Address, _Remote_Port, _Local_Address, _Local_Port);
		} else {
			connect(_Remote_Address, _Remote_Port);
		}
	}

	public void setSocket(Socket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		if(!isClosed()) {
			throw new IOException("Allready Open");
		}

		_Socket = socket;

		_Local_Address = _Socket.getLocalAddress();
		_Local_Port = _Socket.getLocalPort();

		_Remote_Address = _Socket.getInetAddress();
		_Remote_Port = _Socket.getPort();
	}

	public InetAddress getLocalAddress() {
		return _Local_Address;
	};

	public int getLocalPort() {
		return _Local_Port;
	}

	public InetAddress getRemoteAddress() {
		return _Remote_Address;
	}

	public int getRemotePort() {
		return _Remote_Port;
	}

	public boolean isActivelyConnected() {
		if(_Socket == null) {
			return false;
		} else {
			return !_Socket.isInputShutdown() || !_Socket.isOutputShutdown();
		}
	}

	public boolean isClosed() {
		if(_Socket == null) {
			return true;
		} else {
			return _Socket.isClosed();
		}
	}

	public boolean isConnected() {
		if(_Socket == null) {
			return false;
		} else {
			return _Socket.isConnected();
		}
	}

	public InputStream getInputStream() throws IOException {
		if(_Socket == null) {
			throw new IOException("Socket InputStream Is Closed");
		} else {
			return _Socket.getInputStream();
		}
	}

	public OutputStream getOutputStream() throws IOException {
		if(_Socket == null) {
			throw new IOException("Socket OutputStream Is Closed");
		} else {
			return _Socket.getOutputStream();
		}
	}

	public Socket getSocket() {
		return _Socket;
	}

	public void flush() throws IOException {
		if(isClosed()) {
			throw new IOException("OutputStream Is Closed");
		} else {
			_Socket.getOutputStream().flush();
		}
	}

	/**
	 * Remove Socket And Addresses
	 * 
	 * Does Not Close Socket And You Can't Use the reconnect() Method
	 * 
	 */
	public synchronized void clear() {
		_Socket = null;
	}

	public void close() throws IOException {
		if(_Socket != null) {
			_Socket.close();
			_Socket = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}
}
