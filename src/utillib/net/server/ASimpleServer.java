package utillib.net.server;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.net.NetUtil;

import utillib.utilities.ThreadUtil;

import java.io.IOException;

import java.net.InetAddress;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 13, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public abstract class ASimpleServer implements Runnable {
	protected static final DebugLogger _LOG_ = LogManager.getInstance().getLogger(ASimpleServer.class);

	public static final int _OPTION_REUSEADDRESS_ = 0;
	public static final int _OPTION_SOTIMEOUT_ = 1;
	public static final int _OPTION_RECEIVEBUFFERSIZE_ = 2;

	protected Exception _Last_Exception = null;

	protected Thread _Server_Thread = null;

	protected InetAddress _Local_Address = null;
	protected int _Local_Port = 0;

	protected static enum State {
		START,
		STARTED,

		STOP,
		STOPPED;
	};

	protected State _State = State.STOPPED;

	protected final String _NAME;

	public ASimpleServer(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;
	}

	public String getName() {
		return _NAME;
	}

	public void start() throws Exception {
		start(false);
	}

	public void start(boolean blocking) throws Exception {
		if(_State == State.STOP || _State == State.STOPPED) {
			if(!isBound()) {
				if(_Local_Address == null) {
					throw new Exception("No Address Set");
				}

				bind(_Local_Address, _Local_Port);
				setLocal(getLocalAddress(), getLocalPort());
			}

			_State = State.START;

			_Server_Thread = new Thread(this, getName() + '-' + NetUtil.toString(getLocalAddress(), getLocalPort()));
			_Server_Thread.start();

			if(blocking) {
				while(_State != State.STARTED) {
					ThreadUtil.sleep(10);
				}
			}
		}
	}

	public boolean hasStarted() {
		return _State == State.START;
	}

	public boolean isRunning() {
		return _State == State.START || _State == State.STARTED;
	}

	public boolean isListening() {
		return _State == State.STARTED;
	}

	public void stop() throws Exception {
		stop(false);
	}

	public void stop(boolean blocking) throws Exception {
		if(_State == State.START || _State == State.STARTED) {
			_State = State.STOP;

			close();

			if(blocking) {
				while(_State != State.STOPPED) {
					ThreadUtil.sleep(10);
				}
			}
		}
	}

	public boolean isShutdowning() {
		return _State == State.STOP;
	}

	public boolean isShutdown() {
		return _State == State.STOPPED;
	}

	public Exception getLastException() {
		return _Last_Exception;
	}

	/**
	 * Just Sets The Address And Port, Does Not Bind Address And Port
	 * 
	 * @param address
	 * @param port
	 */
	public void setLocal(InetAddress address, int port) {
		_Local_Address = address;
		_Local_Port = port;
	}

	/**
	 * Binds Address And Port
	 * 
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public abstract void bind(InetAddress address, int port) throws IOException;

	public abstract boolean isBound();

	/**
	 * Unbinds Address And Port
	 * 
	 * @throws IOException
	 */
	public abstract void close() throws IOException;

	public abstract boolean isClosed();

	public abstract InetAddress getLocalAddress();

	public abstract int getLocalPort();

	public abstract void setOption(int option, Object value);

	public abstract Object getOption(int option);
}
