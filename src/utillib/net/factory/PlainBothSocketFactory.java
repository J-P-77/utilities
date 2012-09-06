package utillib.net.factory;

import utillib.net.interfaces.IBothSocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlainBothSocketFactory implements IBothSocketFactory {

	public Socket createSocket() throws UnknownHostException, IOException {
		return new Socket();
	}

	public Socket createSocket(String address, int port) throws UnknownHostException, IOException {
		return new Socket(address, port);
	}

	public Socket createSocket(String address, int port, InetAddress localaddress, int localPort) throws UnknownHostException, IOException {
		return new Socket(address, port, localaddress, localPort);
	}

	public Socket createSocket(InetAddress address, int port) throws UnknownHostException, IOException {
		return new Socket(address, port);
	}

	public Socket createSocket(InetAddress address, int port, InetAddress localaddress, int localPort) throws UnknownHostException, IOException {
		return new Socket(address, port, localaddress, localPort);
	}

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
