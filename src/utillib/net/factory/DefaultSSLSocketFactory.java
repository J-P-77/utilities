package utillib.net.factory;

import utillib.net.interfaces.ISocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class DefaultSSLSocketFactory implements ISocketFactory {
	private final SocketFactory _FACTORY = SSLSocketFactory.getDefault();

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
}
