/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.net.server;

import jp77.utillib.net.NetUtil;

import jp77.utillib.utilities.ThreadUtil;

import java.io.IOException;

import java.net.InetAddress;
import java.util.logging.Logger;

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
	protected static final Logger _LOG_ = Logger.getLogger(ASimpleServer.class.toString());
	
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
