package utillib.net.factory;

import utillib.net.interfaces.IServerSocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class PlainServerSocketFactory implements IServerSocketFactory {
	public ServerSocket createServerSocket() throws UnknownHostException, IOException {
		return new ServerSocket();
	}

	public ServerSocket createServerSocket(int port) throws UnknownHostException, IOException {
		return new ServerSocket(port);
	}

	public ServerSocket createServerSocket(int port, int backlog) throws UnknownHostException, IOException {
		return new ServerSocket(port, backlog);
	}

	public ServerSocket createServerSocket(int port, int backlog, InetAddress localaddress) throws UnknownHostException, IOException {
		return new ServerSocket(port, backlog, localaddress);
	}
}
