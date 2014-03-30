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

package utillib.net.server;

import utillib.net.NetUtil;
import utillib.net.interfaces.IServerSocketFactory;
import utillib.net.interfaces.ISocket;

import utillib.net.factory.SocketFactories;
import utillib.net.socket.MySocket;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
public abstract class ASimpleSocketServer extends ASimpleServer {
	private IServerSocketFactory _ServerSocketFactory = SocketFactories.getPlainServerSocketFactory();

	private ServerSocket _ServerSocket = null;

	private boolean _ReuseAddress = false;
	private int _SoTimeout = -1;
	private int _ReceiveBufferSize = -1;

	private int _Max_Connections = 0;

	private int _Shutdown_After_N = 0;

	private int _ConnectionCount = 0;

	public ASimpleSocketServer() {
		this("TCP-Server");
	}

	public ASimpleSocketServer(String name) {
		super(name);
	}

	public ASimpleSocketServer(int port) throws IOException {
		this("TCP-Server", null, port, 0);
	}

	public ASimpleSocketServer(int port, int maxconnection) throws IOException {
		this("TCP-Server", null, port, maxconnection);
	}

	public ASimpleSocketServer(String address, int port) throws UnknownHostException, IOException {
		this("TCP-Server", InetAddress.getByName(address), port, 0);
	}

	public ASimpleSocketServer(InetAddress address, int port) throws IOException {
		this("TCP-Server", address, port, 0);
	}

	public ASimpleSocketServer(String name, InetAddress address, int port, int maxconnection) throws IOException {
		super(name);

		_ServerSocket = _ServerSocketFactory.createServerSocket(port, 50, address);
	}

	public ASimpleSocketServer(String name, ServerSocket serversocket, int maxconnection) throws IOException {
		super(name);

		setServerSocket(serversocket);
	}

	public ASimpleSocketServer(String name, IServerSocketFactory serversocketfactory, int maxconnection) throws IOException {
		super(name);

		setSocketFactory(serversocketfactory);
	}

	/**
	 * 
	 * 
	 * @param socket
	 *            reserved for future use
	 */
	public void releaseClient(ISocket socket) {
		_Max_Connections--;
	}

	public void setMaxConnections(int value) {
		if(value < 0) {
			throw new RuntimeException("Variable[value] - Must Be Equal Too or Greater Than Zero");
		}

		_Max_Connections = value;
	}

	public int getMaxConnections() {
		return _Max_Connections;
	}

	public int getConnectionsCount() {
		return _ConnectionCount;
	}

	public void resetConnectionCount() {
		_ConnectionCount = 0;
	}

	public void setShutDownAfterN(int value) {
		if(value < 0) {
			throw new RuntimeException("Variable[value] - Must Be Equal Too or Greater Than Zero");
		}

		_Shutdown_After_N = value;
	}

	public int getShutDownAfterN() {
		return _Shutdown_After_N;
	}

	public void setServerSocket(ServerSocket serversocket) {
		if(!super.isShutdown()) {
			throw new RuntimeException(getName() + " Is Not Shutdown");
		}

		if(serversocket == null) {
			throw new RuntimeException("Variable[serversocket] - Is Null");
		}

		_ServerSocket = serversocket;
	}

	public ServerSocket getServerSocket() {
		return _ServerSocket;
	}

	public void setSocketFactory(IServerSocketFactory socketfactory) {
		if(socketfactory == null) {
			throw new RuntimeException("Variable[socketfactory] - Is Null");
		}

		if(!super.isShutdown()) {
			throw new RuntimeException(getName() + " Is Not Shutdown");
		}

		_ServerSocketFactory = socketfactory;
	}

	public IServerSocketFactory getSocketFactory() {
		return _ServerSocketFactory;
	}

	@Override
	public void bind(InetAddress address, int port) throws IOException {
		if(!isClosed()) {
			throw new IOException("Is Not Closed");
		}

		if(isBound()) {
			throw new IOException("Allready Bound");
		}

		_ServerSocket = _ServerSocketFactory.createServerSocket(port, 50, address);

		_ServerSocket.setReuseAddress(_ReuseAddress);

		if(_SoTimeout != -1) {
			_ServerSocket.setSoTimeout(_SoTimeout);
		}

		if(_ReceiveBufferSize != -1) {
			_ServerSocket.setReceiveBufferSize(_ReceiveBufferSize);
		}
	}

	@Override
	public boolean isBound() {
		if(_ServerSocket != null) {
			return _ServerSocket.isBound();
		}

		return false;
	}

	@Override
	public InetAddress getLocalAddress() {
		if(_ServerSocket != null) {
			return _ServerSocket.getInetAddress();
		}

		return null;
	}

	@Override
	public int getLocalPort() {
		if(_ServerSocket != null) {
			return _ServerSocket.getLocalPort();
		}

		return 0;
	}

//  _ServerSocket.setReceiveBufferSize(size);
//  _ServerSocket.setSoTimeout(timeout);
//  _ServerSocket.setReuseAddress(on);	
	public void setOption(int option, Object value) {
		if(isClosed()) {
			throw new RuntimeException(getName() + " Server Is Not Open Call open(InetAddress, int) First");
		}

		try {
			switch(option) {
				case _OPTION_REUSEADDRESS_:
					if(_ServerSocket != null) {
						_ServerSocket.setReuseAddress((Boolean)value);
					}
					_ReuseAddress = (boolean)(Boolean)value;
					break;

				case _OPTION_SOTIMEOUT_:
					if(_ServerSocket != null) {
						_ServerSocket.setSoTimeout((Integer)value);
					}
					_SoTimeout = (int)(Integer)value;
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					if(_ServerSocket != null) {
						_ServerSocket.setReceiveBufferSize((Integer)value);
					}
					_ReceiveBufferSize = (int)(Integer)value;
					break;
			}
		} catch(Exception e) {}
	}

	public Object getOption(int option) {
		if(isClosed()) {
			throw new RuntimeException(getName() + " Server Is Not Open Call open(InetAddress, int) First");
		}

		try {
			switch(option) {
				case _OPTION_REUSEADDRESS_:
					return _ReuseAddress;

				case _OPTION_SOTIMEOUT_:
					return _SoTimeout;

				case _OPTION_RECEIVEBUFFERSIZE_:
					return _ReceiveBufferSize;

			}
		} catch(Exception e) {}

		return null;
	}

	public MySocket accept() throws IOException, SocketTimeoutException {
		final Socket SOCKET = _ServerSocket.accept();

		return new MySocket(SOCKET);
	}

	public void run() {
		if(!isBound()) {
			throw new RuntimeException("Variable[] - Is Not Bound");
		}

		_State = State.STARTED;

		_LOG_.printInformation("Started: " + Thread.currentThread().getName());

		final String SERVER_ADDRESS_PORT = NetUtil.toString(getLocalAddress(), getLocalPort());

		_LOG_.printInformation("Listening On: " + SERVER_ADDRESS_PORT);

		while(_State != State.STOP/* || !isClosed() */) {
			try {
				final MySocket CLIENT_SOCKET = accept();

				final String REMOTE_ADDRESS_PORT = NetUtil.toString(CLIENT_SOCKET.getRemoteAddress(), CLIENT_SOCKET.getRemotePort());
				//Connection
				_LOG_.printInformation("Incoming From: " + REMOTE_ADDRESS_PORT);

				if(_Max_Connections > 0 && _ConnectionCount >= _Max_Connections) {
					_LOG_.printInformation("Not Accepting Any More On: " + SERVER_ADDRESS_PORT);

					try {
						CLIENT_SOCKET.close();
					} catch(Exception e) {}
				} else {
					if(acceptConnection(CLIENT_SOCKET)) {
						_LOG_.printInformation("Accepted From: " + REMOTE_ADDRESS_PORT);

						handleConnection(CLIENT_SOCKET);

						_ConnectionCount++;

						if(_Shutdown_After_N > 0 && _ConnectionCount >= _Shutdown_After_N) {
							_LOG_.printInformation("Not Accepting Any More On " + SERVER_ADDRESS_PORT + " Server Shutting Down");
							break;
						}
					} else {
						_LOG_.printInformation("Rejected From: " + REMOTE_ADDRESS_PORT);
					}
				}

			} catch(SocketTimeoutException e) {

			} catch(Exception e) {
				if(_State != State.STOP) {
					_LOG_.printError(e);

					_Last_Exception = e;
					_State = State.STOP;
				}
			}
		}

//			try {
//				close();
//			} catch(Exception e) {
//				_Last_Exception = e;
//			}

//			_LOG_.printInformation("Closed On " + NetUtil.toStringAddressNPort(getLocalAddress(), getLocalPort()));

		_LOG_.printInformation("Stopped: " + Thread.currentThread().getName());
		_State = State.STOPPED;
	}

	@Override
	public boolean isClosed() {
		return _ServerSocket == null;
	}

	@Override
	public void close() {
		if(_ServerSocket != null/* && isRunning() */) {
			try {
				_ServerSocket.close();
			} catch(Exception e) {}

//			_LOG_.printInformation("Closed On " + NetUtil.toStringAddressNPort(getLocalAddress(), getLocalPort()));

			_ServerSocket = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	public abstract boolean acceptConnection(MySocket socket);

	public abstract void handleConnection(MySocket socket);
}
