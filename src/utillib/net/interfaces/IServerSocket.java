package utillib.net.interfaces;

import utillib.interfaces.IClose;

import java.io.IOException;

import java.net.InetAddress;

public interface IServerSocket extends IClose {
	public static final int _OPTION_SOTIMEOUT_ = 0;
	public static final int _OPTION_RECEIVEBUFFERSIZE_ = 1;

	public InetAddress getLocalAddress();

	public int getLocalPort();

	public void bind(String address, int port) throws IOException;

	public void bind(InetAddress address, int port) throws IOException;

	public boolean isBound();

	public void setOption(int option, Object value);

	public Object getOption(int option);

	public boolean isClosed();

	public void close() throws IOException;

	public ISocket accept() throws IOException;
}
