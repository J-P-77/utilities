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

package jp77.utillib.net;

// import java.net.Socket;

/**
 * Use before a connection is made
 * 
 * @author Justin Palinkas
 * 
 */
public class SocketAddressAndPort {
	private String _Address = null;

	private int _Port = 0;

	public SocketAddressAndPort() {
		this(NetUtil.getLoopbackAddress().getHostAddress(), 0);
	}

	public SocketAddressAndPort(String address) {
		this(address, 0);
	}

	public SocketAddressAndPort(String address, int port) {
		setAddress(address);
		setPort(port);
	}

//	public SocketAddressAndPort(Socket socket, boolean local) {
//		setAddress((local ? socket.getLocalAddress() : socket.getInetAddress()).getHostAddress());
//		setPort(local ? socket.getLocalPort() : socket.getPort());
//	}

	/**
	 * Can Be Anything IPv4 or IPv6 or URL
	 * 
	 * @param value
	 */
	public void setAddress(String value) {
		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		_Address = value;
	}

	public String getAddress() {
		return _Address;
	}

	public void setPort(int value) {
		if(!NetUtil.validPort(value)) {
			throw new RuntimeException("Variable[value] - Invalid Port");
		}

		_Port = value;
	}

	public int getPort() {
		return _Port;
	}

	@Override
	public String toString() {
		return _Address.toString() + ':' + _Port;

//		int Index = _Address.indexOf('/');
//		
//		if(Index == -1) {
//			return _Address + ':' + _Port;
//		} else {
//			final MyStringBuffer BUFFER = new MyStringBuffer();
//			
//			int X = 0;
//			for(;X < Index && X < _Address.length(); X++) {
//				BUFFER.append(_Address.charAt(X));
//			}
//			
//			BUFFER.append(':');
//			BUFFER.append(_Port, true);
//			
//			for(;X < _Address.length(); X++) {
//				BUFFER.append(_Address.charAt(X));
//			}
//			
//			return BUFFER.toString();
//		}
	}
}
