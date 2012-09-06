package utillib.net.factory;

import utillib.net.interfaces.ISocketFactory;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlainSocketFactory implements ISocketFactory {

//	public PlainSocketFactory() {
//		super("Plain");
//	}

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
}
