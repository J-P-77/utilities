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
