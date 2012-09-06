package utillib.net.interfaces;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public interface IServerSocketFactory {
//	public String getName();

	public ServerSocket createServerSocket() throws UnknownHostException, IOException;
	public ServerSocket createServerSocket(int port) throws UnknownHostException, IOException;
	public ServerSocket createServerSocket(int port, int backlog) throws UnknownHostException, IOException;
	public ServerSocket createServerSocket(int port, int backlog, InetAddress localaddress) throws UnknownHostException, IOException;
}
