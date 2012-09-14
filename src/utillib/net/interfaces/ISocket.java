package utillib.net.interfaces;

import utillib.interfaces.IClose;

import java.io.IOException;

import java.net.InetAddress;

public interface ISocket extends IClose {
	public static final int _OPTION_SOTIMEOUT_ = 0;
	public static final int _OPTION_SENDBUFFERSIZE_ = 1;
	public static final int _OPTION_RECEIVEBUFFERSIZE_ = 2;

	public InetAddress getLocalAddress();

	public int getLocalPort();

	public InetAddress getRemoteAddress();

	public int getRemotePort();

	public void bind(String address, int port) throws IOException;

	public void bind(InetAddress address, int port) throws IOException;

	public boolean isBound();

	public void connect(String address, int port) throws IOException;

	public void connect(InetAddress address, int port) throws IOException;

	public boolean isConnected();

	public void setOption(int option, Object value);

	public Object getOption(int option);

	public boolean isClosed();

	public void close() throws IOException;
}
