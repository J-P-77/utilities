package utillib.net;

//import java.net.Socket;

/**
 * Use before a connection is made
 * @author Dalton Dell
 *
 */
public class SocketAddressAndPort {
	private String _Address = null;
	
	private int _Port = 0;

	public SocketAddressAndPort() {
		this(NetUtil.getLoopbackAddress().getHostAddress(), 0);
	}
	
	public SocketAddressAndPort(String address) {
		this(address, 0);
	}
	
	public SocketAddressAndPort(String address, int port) {
		setAddress(address);
		setPort(port);
	}
	
//	public SocketAddressAndPort(Socket socket, boolean local) {
//		setAddress((local ? socket.getLocalAddress() : socket.getInetAddress()).getHostAddress());
//		setPort(local ? socket.getLocalPort() : socket.getPort());
//	}
	
	/**
	 * Can Be Anything IPv4 or IPv6 or URL
	 * @param value
	 */
	public void setAddress(String value) {
		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		_Address = value;
	}
	
	public String getAddress() {
		return _Address;
	}

	public void setPort(int value) {
		if(!NetUtil.validPort(value)) {
			throw new RuntimeException("Variable[value] - Invalid Port");
		}

		_Port = value;
	}

	public int getPort() {
		return _Port;
	}
	
	@Override
	public String toString() {
		return _Address.toString() + ':' + _Port;
		
//		int Index = _Address.indexOf('/');
//		
//		if(Index == -1) {
//			return _Address + ':' + _Port;
//		} else {
//			final MyStringBuffer BUFFER = new MyStringBuffer();
//			
//			int X = 0;
//			for(;X < Index && X < _Address.length(); X++) {
//				BUFFER.append(_Address.charAt(X));
//			}
//			
//			BUFFER.append(':');
//			BUFFER.append(_Port, true);
//			
//			for(;X < _Address.length(); X++) {
//				BUFFER.append(_Address.charAt(X));
//			}
//			
//			return BUFFER.toString();
//		}
	}
}
