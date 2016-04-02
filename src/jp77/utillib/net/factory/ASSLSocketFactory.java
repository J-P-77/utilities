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

package jp77.utillib.net.factory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

public abstract class ASSLSocketFactory extends SocketFactories {
	private SSLContext _Current_SSLContext = null;

	public void setSSLContext(SSLContext context) {
		if(context == null) {
			throw new RuntimeException("Variable[context] - Is Null");
		}

		_Current_SSLContext = context;
	}

	public SSLContext getSSLContext() {
		return _Current_SSLContext;
	}

	private void checkSSLContext() {
		if(_Current_SSLContext == null) {
			throw new RuntimeException("No SSLContext");
		}
	}

	//STATIC
	protected static Socket startHandshake(Socket socket) throws IOException {
		if(socket == null) {
			return null;
		} else {
			if(!(socket instanceof SSLSocket)) {
				return null;
			} else {
				((SSLSocket)socket).addHandshakeCompletedListener(new HandshakeCompletedListener() {

					@Override
					public void handshakeCompleted(HandshakeCompletedEvent event) {
						System.out.println(event.getSession().getCipherSuite());
						try {
							System.out.println(((X509Certificate)event.getPeerCertificates()[0]).getSubjectDN().getName());
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				});

				((SSLSocket)socket).startHandshake();

				return socket;
			}
		}
	}

	public static SSLContext getSSLContext(String protocol) {
		try {
			return SSLContext.getInstance(protocol);
		} catch(Exception e) {}

		return null;
	}
}
