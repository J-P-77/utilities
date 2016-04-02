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

package jp77.utillib.net.protocol.http.client;

import jp77.utillib.arrays.ResizingArray;

import jp77.utillib.collections.Collection;

import jp77.utillib.file.FileUtil.Line_Ending;

import jp77.utillib.strings.MyStringBuffer;

public class HttpEntry {
	public static enum Method {
		GET/*("GET")*/,
		HEAD/*("HEAD")*/,
		PUT/*("PUT")*/,
		POST/*("POST")*/,
		DELETE/*("DELETE")*/,
		TRACE/*("TRACE")*/,
		CONNECT/*("CONNECT")*/,
		OPTIONS/*("OPTIONS")*/;

//		private final String _NAME;
//		private Method(String name) {
//			_NAME = name;
//		}
//		public String getName() {return _NAME;}
	};

	private final ResizingArray<Header> _HEADERS = new ResizingArray<Header>();

	private String _Path;
	private Method _Method = Method.GET;
	private int _Status_Code = -1;
	private String _Status_Msg = null;

	public HttpEntry() {
		this(null);
	}

	public HttpEntry(String url) {
		this(Method.GET, url);
	}

	public HttpEntry(Method method, String path) {
		_Method = method;
		_Path = path;
	}

	public void setPath(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		_Path = path;
	}

	public String getPath() {
		return _Path;
	}

	public void setMethod(Method method) {
		if(method == null) {
			throw new RuntimeException("Variable[method] - Is Null");
		}

		if(!method.equals(Method.GET) && !method.equals(Method.HEAD)) {
			throw new RuntimeException("Variable[method] - Currently Only GET & HEAD Is Supported");
		}

		_Method = method;
	}

	public Method getMethod() {
		return _Method;
	}

	public void setStatusCode(int value) {
		if(!HttpCodes.validStatusCode(value)) {
			throw new RuntimeException("Variable[value] - Status Code Out Of Range" + ' ' + value);
		}

		_Status_Code = value;
	}

	public int getStatusCode() {
		return _Status_Code;
	}

	public void setStatusMsg(String value) {
		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		_Status_Msg = value;
	}

	public String getStatusMsg() {
		return _Status_Msg;
	}

	public void setKeepAlive(boolean value) {
		if(value) {
			setOnlyAllowOne("Connection", "keep-alive");
		} else {
			setOnlyAllowOne("Connection", "close");
		}
	}

	public boolean getKeepAlive() {
		Header T_Header = getHeader("Connection");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				try {
					return Boolean.parseBoolean((String)ENTRY.getObject());
				} catch(Exception e) {}
			}
		}

		return true;
	}

	public void setContentLength(long value) {
		if(value < 0) {
			throw new RuntimeException("Variable[value] - Must Be Greater Than Zero");
		}

		setOnlyAllowOne("Content-Length", Long.toString(value));
	}

	public long getContentLength() {
		Header T_Header = getHeader("Content-Length");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				try {
					return Long.parseLong((String)ENTRY.getObject());
				} catch(Exception e) {}
			}
		}

		return 0;
	}

	public void setHost(String value) {
		setOnlyAllowOne("Host", value);
	}

	public String getHost() {
		Header T_Header = getHeader("Host");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}
		}

		return null;
	}

	public void setContentType(String value) {
		setOnlyAllowOne("Content-Type", value);
	}

	public String getContentType() {
		Header T_Header = getHeader("Content-Type");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}
		}

		return null;
	}

	public void setAccept(String value) {
		setOnlyAllowOne("Accept", value);
	}

	public String getAccept() {
		Header T_Header = getHeader("Accept");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}
		}

		return null;
	}

	public void setAcceptCharset(String value) {
		setOnlyAllowOne("Accept-Charset", value);
	}

	public String getAcceptCharset() {
		Header T_Header = getHeader("Accept-Charset");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}
		}

		return null;
	}

	public void setAcceptEncoding(String value) {
		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		setOnlyAllowOne("Accept-Encoding", value);
	}

	public String getAcceptEncoding() {
		Header T_Header = getHeader("Accept-Encoding");

		if(T_Header != null) {
			final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(0);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}
		}

		return null;
	}

	public void setCookie(String key, String value) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		Header T_Header = getHeader("set-cookie");

		if(T_Header == null) {
			T_Header = new Header(key);

			_HEADERS.put(T_Header);
		}

		T_Header.setKeyPair(key, value);
	}

	public Header getCookies() {
		return getHeader("set-cookie");
	}

	public Header createHeader(String name) {
		Header T_Header = getHeader(name);

		if(T_Header == null) {
			T_Header = new Header(name);

			_HEADERS.put(T_Header);
		}

		return T_Header;
	}

	public void setMulti(String headername, String value) {
		setMulti(headername, "", value);
	}

	public void setMulti(String headername, String key, String value) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		Header T_Header = new Header(headername);

		T_Header.setKeyPair(key, value);
	}

	public void setMulti(String headername, String[] keys, String[] values) {
		if(keys == null) {
			throw new RuntimeException("Variable[keys] - Is Null");
		}

		if(values == null) {
			throw new RuntimeException("Variable[values] - Is Null");
		}

		if(keys.length != values.length) {
			throw new RuntimeException("Variable[keys & values] - Must Be Equal Length");
		}

		for(int X = 0; X < keys.length; X++) {
			Header T_Header = new Header(headername);

			T_Header.setKeyPair(keys[X], values[X]);
		}
	}

	public void setOnlyAllowOne(String headername, String value) {
		setOnlyAllowOne(headername, "", value);
	}

	public void setOnlyAllowOne(String headername, String key, String value) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		if(value == null) {
			throw new RuntimeException("Variable[value] - Is Null");
		}

		Header T_Header = getHeader(headername);

		if(T_Header == null) {
			T_Header = new Header(headername);

			_HEADERS.put(T_Header);
		}

		T_Header.setKeyPair(key, value);
	}

	public void setOnlyAllowOne(String headername, String[] keys, String[] values) {
		if(keys == null) {
			throw new RuntimeException("Variable[keys] - Is Null");
		}

		if(values == null) {
			throw new RuntimeException("Variable[values] - Is Null");
		}

		if(keys.length != values.length) {
			throw new RuntimeException("Variable[keys & values] - Must Be Equal Length");
		}

		Header T_Header = getHeader(headername);

		if(T_Header == null) {
			T_Header = new Header(headername);

			_HEADERS.put(T_Header);
		}

		for(int X = 0; X < keys.length; X++) {
			T_Header.setKeyPair(keys[X], values[X]);
		}
	}

	public Header getHeader(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		for(int X = 0; X < _HEADERS.length(); X++) {
			if(_HEADERS.getAt(X).getName().equalsIgnoreCase(name)) {
				return _HEADERS.getAt(X);
			}
		}

		return null;
	}

	public Header[] getHeaders(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		final ResizingArray<Header> RETURN = new ResizingArray<Header>();

		for(int X = 0; X < _HEADERS.length(); X++) {
			if(_HEADERS.getAt(X).getName().equalsIgnoreCase(name)) {
				RETURN.put(_HEADERS.getAt(X));
			}
		}

		return RETURN.toArray(new Header[RETURN.length()]);
	}

	public String getHeaderValue(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		for(int X = 0; X < _HEADERS.length(); X++) {
			if(_HEADERS.getAt(X).getName().equalsIgnoreCase(name)) {
				return _HEADERS.getAt(X).getKeyPair(0).getObject();
			}
		}

		return null;
	}

	public Header getHeaderAt(int index) {
		return _HEADERS.getAt(index);
	}

	public int headerCount() {
		return _HEADERS.length();
	}

	@Override
	public String toString() {
		final MyStringBuffer BUFFER = new MyStringBuffer(100);

		BUFFER.append(getMethod().toString());//Just So It Looks Petty

		BUFFER.append(' ');
		if(getPath() == null) {
			BUFFER.append('/');
		} else {
			BUFFER.append(getPath());
		}

		BUFFER.append(' ');
		BUFFER.append("HTTP/1.1");
		BUFFER.append(Line_Ending.WINDOWS.getValue());

//		if(getHost() == null || getHost().length() == 0) { 
////			BUFFER.append("Host: " + _Connection.getRemoteInetAddress().getHostName());
////		} else {
//			BUFFER.append("Host: " + getHost());
//		}
//		BUFFER.append(Line_Ending.WIN.getValue());

		HttpEntry.Header T_Header = null;
		for(int X = 0; X < headerCount(); X++) {
			T_Header = getHeaderAt(X);

			BUFFER.append(T_Header.getName());
			BUFFER.append(':');
			BUFFER.append(' ');

			for(int Y = 0; Y < T_Header.keypairCount(); Y++) {
				final Collection<String, String>.Entry ENTRY = T_Header.getKeyPair(Y);

				if(ENTRY.getKey() != null && ENTRY.getKey().length() > 0) {
					BUFFER.append(ENTRY.getKey());

					BUFFER.append(';');
					BUFFER.append(' ');
				}

				BUFFER.append(ENTRY.getObject());
			}

			BUFFER.append(Line_Ending.WINDOWS.getValue());
		}

		return BUFFER.toString();
	}

	public class Header {
		private final String _NAME;

		private final Collection<String, String> _VALUE = new Collection<String, String>();

		public Header(String name) {
			if(name == null) {
				throw new RuntimeException("Variable[name] - Is Null");
			}

			_NAME = name;
		}

		public String getName() {
			return _NAME;
		}

		public void setKeyPair(String key, String value) {
			_VALUE.set(key, value);
		}

		public Collection<String, String>.Entry getKeyPair(int index) {
			return _VALUE.getEntryAt(index);
		}

		public Collection<String, String>.Entry getKeyPair(String name) {
			return _VALUE.getEntry(name);
		}

		public String getValue(String name) {
			final Collection<String, String>.Entry ENTRY = _VALUE.getEntry(name);

			if(ENTRY != null) {
				return ENTRY.getObject();
			}

			return null;
		}

		public int keypairCount() {
			return _VALUE.count();
		}

		@Override
		public String toString() {
			return _NAME;
		}
	}
}
