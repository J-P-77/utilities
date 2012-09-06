package utillib.net.interfaces;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public interface ISocketFactory {
//	public String getName();
	
    public Socket createSocket() throws UnknownHostException, IOException;
    public Socket createSocket(String address, int port) throws UnknownHostException, IOException;
    public Socket createSocket(InetAddress address, int port) throws UnknownHostException, IOException;
    public Socket createSocket(String address, int port, InetAddress localaddress, int localport) throws UnknownHostException, IOException;
    public Socket createSocket(InetAddress address, int port, InetAddress localaddress, int localport) throws UnknownHostException, IOException;
}
