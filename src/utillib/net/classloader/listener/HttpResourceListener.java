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

package utillib.net.classloader.listener;

import beta.utillib.classloader.v2.listeners.IClassloaderListener;
import utillib.debug.DebugLogger;
import utillib.debug.LogManager;
import utillib.file.FileUtil;
import utillib.net.NetUtil;
import utillib.net.factory.SocketFactories;
import utillib.net.protocol.http.client.HttpCodes;
import utillib.net.protocol.http.client.HttpEntry;
import utillib.net.protocol.http.client.MyHttpConnection;
import utillib.strings.MyStringBuffer;
import utillib.net.interfaces.ISocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * August 1, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class HttpResourceListener implements IClassloaderListener {
	private static final DebugLogger _LOG = LogManager.getInstance().getLogger(HttpResourceListener.class);

	private Manifest _Manifest;

	private MyHttpConnection _Connection;
	private final URL _URL;

	public HttpResourceListener(String address, int port) throws IOException {
		this(address, port, null, SocketFactories.getPlainSocketFactory());
	}

	public HttpResourceListener(String address, int port, String base) throws IOException {
		this(address, port, base, SocketFactories.getPlainSocketFactory());
	}

	public HttpResourceListener(String address, int port, String base, ISocketFactory socketcreator) throws IOException {
		if(address == null) {
			throw new RuntimeException("Variable[address] - Is Null");
		}

		if(!NetUtil.validPort(port)) {
			throw new RuntimeException("Variable[port] - Invalid Ip");
		}

		_Connection = new MyHttpConnection(InetAddress.getByName(address), port, socketcreator);

		if(base == null) {
			base = "";
		}

		_URL = new URL("http://", _Connection.getRemoteAddress().getHostName(), port, base);
	}

//    public HttpClassLoaderListener(URL url) throws IOException {
//    	this(url, NetUtil.getPlainSocketCreator());
//    }
//    
//    public HttpClassLoaderListener(URL url, ISocketCreator socketcreator) throws IOException {
//    	if(url == null) {
//			throw new RuntimeException("Variable[url] - Is Null");
//		}
//
//        _Connection = new MyHttpConnection(url, socketcreator);
//        
//        _URL = url;
//    }

	public String getRoot() {
		return _URL.getPath();
	}

	@Override
	public String getName() {
		return _Connection.getRemoteAddress().toString();
	}

	@Override
	public Manifest getManifest() {
		if(_Manifest == null) {
			final MyStringBuffer BUFFER = new MyStringBuffer(32);
			BUFFER.append(JarFile.MANIFEST_NAME);

			final String CLASS_NAME = BUFFER.toString();

			HttpEntry Entry = null;
			try {
				Entry = new HttpEntry(HttpEntry.Method.GET, combineTwoPaths(_URL.getPath(), CLASS_NAME));

				Entry.setHost(_URL.getHost());
				Entry.setContentLength(0);

				_LOG.printInformation("Sending HTTP Request For Manifest:" + ' ' + Entry.getPath());

				_Connection.request(Entry);

				_LOG.printInformation("Waiting For HTTP Reply For Manifest:" + ' ' + Entry.getPath());

				final InputStream ISTREAM = _Connection.reply(Entry);

				if(HttpCodes.statusSuccess(Entry)) {
					_LOG.printInformation("Revieving HTTP Reply For Manifest:" + ' ' + Entry.getPath());

					return (_Manifest = new Manifest(ISTREAM));
				} else {
					_LOG.printInformation(("HTTP Error:" + ' ') + Entry.getStatusCode() + (' ' + Entry.getStatusMsg()));

					return (_Manifest = null);
				}
			} catch(Exception e) {}
		}

		return _Manifest;
	}

	@Override
	public InputStream findClass(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			final MyStringBuffer BUFFER = new MyStringBuffer(32);
			BUFFER.append(name);
			BUFFER.replace('.', FileUtil._LINUX_c);
			BUFFER.append(".class");

			final String CLASS_NAME = BUFFER.toString();
			//Connect To Server And Fetch Class Stream
			HttpEntry Entry = null;
			try {
				Entry = new HttpEntry(HttpEntry.Method.GET, FileUtil.combineTwoPaths(_URL.getPath(), CLASS_NAME));

				Entry.setHost(_URL.getHost());
				Entry.setContentLength(0);

				_LOG.printInformation("Sending HTTP Request For Class:" + ' ' + Entry.getPath());

				_Connection.request(Entry);

				_LOG.printInformation("Waiting For HTTP Reply For Class:" + ' ' + Entry.getPath());

				final InputStream ISTREAM = _Connection.reply(Entry);

				_LOG.printInformation("Revieving HTTP Reply For Class:" + ' ' + Entry.getPath());

				if(HttpCodes.statusSuccess(Entry)) {
					return ISTREAM;
				} else {
					_LOG.printInformation(("HTTP Error:" + ' ') + Entry.getStatusCode() + (' ' + Entry.getStatusMsg()));

					return null;
				}
			} catch(Exception e) {}
		}

		return null;
	}

	//jar:file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
	@Override
	public URL findResource(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			// Make Sure URL Resource Exists If So Return URL
			try {
				return new URL("http://", _URL.getHost(), _URL.getPort(), FileUtil.combineTwoPaths(_URL.getPath(), name, "/"));
			} catch(Exception e) {}
		}

		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			//Connect To Server And Fetch Resource Stream
			HttpEntry Entry = null;
			try {
				Entry = new HttpEntry(HttpEntry.Method.GET, combineTwoPaths(_URL.getPath(), name));

				Entry.setHost(_URL.getHost());
				Entry.setContentLength(0);

				_LOG.printInformation("Sending HTTP Request For Resource:" + ' ' + Entry.getPath());

				_Connection.request(Entry);

				_LOG.printInformation("Waiting For HTTP Reply For Resource:" + ' ' + Entry.getPath());

				final InputStream ISTREAM = _Connection.reply(Entry);

				_LOG.printInformation("Revieving HTTP Reply For Resource:" + ' ' + Entry.getPath());

				if(HttpCodes.statusSuccess(Entry)) {
					return ISTREAM;
				} else {
					_LOG.printInformation(("HTTP Error:" + ' ') + Entry.getStatusCode() + (' ' + Entry.getStatusMsg()));

					return null;
				}
			} catch(Exception e) {}
		}

		return null;
	}

	@Override
	public boolean resourceExists(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			//Make Sure URL Resource Exists If So Return true
			HttpEntry Entry = null;
			try {
				Entry = new HttpEntry(HttpEntry.Method.HEAD, combineTwoPaths(_URL.getPath(), name));

				Entry.setHost(_URL.getHost());
				Entry.setContentLength(0);

				_Connection.request(Entry);

				_Connection.reply(Entry);

				if(HttpCodes.statusSuccess(Entry)) {
					return true;
				}
			} catch(Exception e) {}
		}

		return false;
	}

	@Override
	public boolean isClosed() {
		//Return true The Class Loader Is Still Able To Fetch Classes and Resources
		if(_Connection != null) {
			try {
				return _Connection.isClosed();
			} catch(Exception e) {}
			_Connection = null;
		}

		return true;
	}

	@Override
	public synchronized void close() {
		if(_Connection != null) {
			try {
				_Connection.close();
			} catch(Exception e) {}
			_Connection = null;
		}

		_LOG.printInformation("Local: " + getName() + " Closed");
	}

	public static String combineTwoPaths(String path1, String path2) {
		if(path1 == null || path1.length() == 0) {
			return null;
		}

		if(path2 == null || path2.length() == 0) {
			return null;
		}

		final MyStringBuffer BUFFER = new MyStringBuffer(path1.length() + path2.length() + (FileUtil._LINUX_.length() * 2));

		final boolean PATH1_ENDSWITH = path1.endsWith(FileUtil._LINUX_);
		final boolean PATH2_STARSWITH = path2.startsWith(FileUtil._LINUX_);
		final boolean PATH2_ENDSWITH = path2.endsWith(FileUtil._LINUX_);

		BUFFER.append(path1);

		if(!PATH1_ENDSWITH && !PATH2_STARSWITH) {
			BUFFER.append(FileUtil._LINUX_);
		}

		final int OFFSET = (PATH1_ENDSWITH && PATH2_STARSWITH ? 1 : 0);
		final int LEN = (PATH2_ENDSWITH ? (path2.length() - 1) : path2.length());
		for(int X = OFFSET; X < LEN; X++) {
			BUFFER.append(path2.charAt(X));
		}

		BUFFER.append(FileUtil._LINUX_);

		return BUFFER.toString();
	}
}
