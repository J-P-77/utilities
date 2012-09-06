package utillib.net.factory;

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
