package utillib.net.factory;

import utillib.net.interfaces.IServerSocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class DefaultSSLServerSocketFactory implements IServerSocketFactory {
	private final ServerSocketFactory _FACTORY = SSLServerSocketFactory.getDefault();

	public ServerSocket createServerSocket() throws UnknownHostException, IOException {
		return _FACTORY.createServerSocket();
	}

	public ServerSocket createServerSocket(int port) throws UnknownHostException, IOException {
		return _FACTORY.createServerSocket(port);
	}

	public ServerSocket createServerSocket(int port, int backlog) throws UnknownHostException, IOException {
		return _FACTORY.createServerSocket(port, backlog);
	}

	public ServerSocket createServerSocket(int port, int backlog, InetAddress localaddress) throws UnknownHostException, IOException {
		return _FACTORY.createServerSocket(port, backlog, localaddress);
	}
}
