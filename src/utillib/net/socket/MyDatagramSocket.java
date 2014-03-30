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

package utillib.net.socket;

import utillib.lang.byref.IntByRef;
import utillib.lang.byref.TByRef;
import utillib.net.interfaces.ISocket;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
public class MyDatagramSocket implements ISocket {
	public static final int _OPTION_REUSEADDRESS_ = 3;
	public static final int _OPTION_BROADCAST_ = 4;

	private InetAddress _Local_Address;
	private int _Local_Port;

	private InetAddress _Remote_Address;
	private int _Remote_Port;

	private DatagramSocket _Socket = null;

	public MyDatagramSocket() throws IOException {
		_Socket = new DatagramSocket(null);
	}

	public MyDatagramSocket(int port) throws UnknownHostException, IOException {
		this(InetAddress.getLoopbackAddress(), port);
	}

	public MyDatagramSocket(String localaddress, int localport) throws UnknownHostException, IOException {
		this(new DatagramSocket(new InetSocketAddress(localaddress, localport)));
	}

	public MyDatagramSocket(InetAddress localaddress, int localport) throws UnknownHostException, IOException {
		this(new DatagramSocket(new InetSocketAddress(localaddress, localport)));
	}

	public MyDatagramSocket(DatagramSocket socket) throws IOException {
		if(socket == null) {
			throw new RuntimeException("Variable[socket] - Is Null");
		}

		_Socket = socket;
		_Local_Address = _Socket.getLocalAddress();
		_Local_Port = _Socket.getLocalPort();

		if(_Socket.getRemoteSocketAddress() != null) {
			_Remote_Address = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getAddress();
			_Remote_Port = ((InetSocketAddress)_Socket.getRemoteSocketAddress()).getPort();
		}
	}

	public DatagramSocket getSocket() {
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

			_Local_Address = _Socket.getLocalAddress();
			_Local_Port = _Socket.getLocalPort();
		}
	}

	@Override
	public boolean isBound() {
		return _Socket.isBound();
	}

	/**
	 * This Actually Locks A Datagram Socket to A Remote Address
	 * 
	 * @param address
	 * @param port
	 */
	@Override
	public void connect(String address, int port) throws IOException {
		connect(new InetSocketAddress(address, port));
	}

	/**
	 * This Actually Locks A Datagram Socket to A Remote Address
	 * 
	 * @param address
	 * @param port
	 */
	@Override
	public void connect(InetAddress address, int port) throws IOException {
		connect(new InetSocketAddress(address, port));
	}

	/**
	 * This Actually Locks A Datagram Socket to A Remote Address
	 * 
	 * @param address
	 */
	private void connect(InetSocketAddress address) throws IOException {
		if(_Remote_Address == null) {
			_Socket.connect(address);

			if(_Local_Address == null) {
				_Local_Address = _Socket.getLocalAddress();
				_Local_Port = _Socket.getLocalPort();
			}

			_Remote_Address = address.getAddress();
			_Remote_Port = address.getPort();
		}
	}

	@Override
	public boolean isConnected() {
		return _Socket.isConnected();
	}

	@Override
	public InetAddress getLocalAddress() {
		return _Local_Address;
	};

	@Override
	public int getLocalPort() {
		return _Local_Port;
	}

	@Override
	public InetAddress getRemoteAddress() {
		return _Remote_Address;
	};

	@Override
	public int getRemotePort() {
		return _Remote_Port;
	}

	public void send(byte[] buffer, int offset, int length, String toaddress, int toport) throws IOException {
		final DatagramPacket PACKET = new DatagramPacket(buffer, offset, length);

		PACKET.setAddress(_Remote_Address);
		PACKET.setPort(_Remote_Port);

		_Socket.send(PACKET);
	}

	public void send(byte[] buffer, int offset, int length, InetAddress toaddress, int toport) throws IOException {
		final DatagramPacket PACKET = new DatagramPacket(buffer, offset, length);

		PACKET.setAddress(toaddress);
		PACKET.setPort(toport);

		_Socket.send(PACKET);
	}

	public void send(DatagramPacket packet) throws IOException {
		_Socket.send(packet);
	}

	public int receive(byte[] buffer, int offset, int length, TByRef<InetAddress> fromaddress, IntByRef fromport) throws IOException {
		final DatagramPacket PACKET = new DatagramPacket(buffer, offset, length);

		_Socket.receive(PACKET);

		fromaddress.value = PACKET.getAddress();
		fromport.value = PACKET.getPort();

		return PACKET.getLength();
	}

	public int receive(DatagramPacket packet) throws IOException {
		_Socket.receive(packet);

		return packet.getLength();
	}

	@Override
	public void setOption(int option, Object value) {
		try {
			switch(option) {
				case _OPTION_SOTIMEOUT_:
					_Socket.setSoTimeout((Integer)value);
					break;
				case _OPTION_SENDBUFFERSIZE_:
					_Socket.setSendBufferSize((Integer)value);
					break;

				case _OPTION_RECEIVEBUFFERSIZE_:
					_Socket.setReceiveBufferSize((Integer)value);
					break;

				case _OPTION_REUSEADDRESS_:
					_Socket.setReuseAddress((Boolean)value);
					break;

				case _OPTION_BROADCAST_:
					_Socket.setBroadcast((Boolean)value);
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

				case _OPTION_SENDBUFFERSIZE_:
					return _Socket.getSendBufferSize();

				case _OPTION_RECEIVEBUFFERSIZE_:
					return _Socket.getReceiveBufferSize();

				case _OPTION_REUSEADDRESS_:
					return _Socket.getReuseAddress();

				case _OPTION_BROADCAST_:
					return _Socket.getBroadcast();
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
