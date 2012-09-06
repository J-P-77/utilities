package utillib.net.factory;

import utillib.net.interfaces.IServerSocketFactory;
import utillib.net.interfaces.ISocketFactory;

public class SocketFactories {
	private static final ISocketFactory _PLAIN_ = new PlainSocketFactory();
	private static final IServerSocketFactory _PLAIN_SERVER_ = new PlainServerSocketFactory();

	private static final ISocketFactory _DEFAULT_SSL_ = new DefaultSSLSocketFactory();
	private static final IServerSocketFactory _DEFAULT_SSL_SERVER_ = new DefaultSSLServerSocketFactory();

	public static ISocketFactory getPlainSocketFactory() {
		return _PLAIN_;
	}

	public static IServerSocketFactory getPlainServerSocketFactory() {
		return _PLAIN_SERVER_;
	}

	public static ISocketFactory getDefaultSSLSocketFactory() {
		return _DEFAULT_SSL_;
	}

	public static IServerSocketFactory getDefaultSSLServerSocketFactory() {
		return _DEFAULT_SSL_SERVER_;
	}
}