/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/*
The MIT License (MIT)

Copyright (c) 2014 J-P-77

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.net.protocol.http.client;

import utillib.arrays.ResizingArray;
import utillib.arrays.buffer.FixedInputByteBuffer;
import utillib.arrays.buffer.FixedOutputByteBuffer;

import utillib.collections.Collection;

import utillib.file.FileUtil.Line_Ending;

import utillib.interfaces.IInputBuffer;
import utillib.interfaces.IOutputBuffer;

import utillib.net.interfaces.ISocketFactory;

import utillib.io.FixedByteBufferInputStream;
import utillib.io.FixedByteBufferOutputStream;
import utillib.io.MyInputStream;
import utillib.io.MyOutputStream;
import utillib.io.ParticalInputStream;
import utillib.io.ParticalOutputStream;

import utillib.lang.byref.IntByRef;

import utillib.net.connection.SimpleSocketConnection;
import utillib.net.factory.SocketFactories;
import utillib.net.protocol.http.client.HttpEntry.Method;

import utillib.strings.MyStringBuffer;
import utillib.strings.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.URL;

public class MyHttpConnection {
//	private final Object __READ_LOCK__ = new Object();
//	private final Object __WRITE_LOCK__ = new Object();

	private final static int _BUFFER_LENGTH_ = 512;

	private SimpleSocketConnection _Connection = null;
	private MyOutputStream _BOStream = null;
	private MyInputStream _BIStream = null;

	private OutputStream _Current_Output = null;
	private InputStream _Current_Input = null;

	private int _Max_Retries = 1;

	private FixedOutputByteBuffer _OUT_BUFFER = new FixedOutputByteBuffer(_BUFFER_LENGTH_, new IOutputBuffer() {
		@Override
		public void empty(byte[] buffer, int offset, int length) {
			try {
//				printBuffer("EMPTY", buffer, offset, length);
				_Connection.getOutputStream().write(buffer, offset, length);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	});

	private FixedInputByteBuffer _IN_BUFFER = new FixedInputByteBuffer(_BUFFER_LENGTH_, new IInputBuffer() {
		@Override
		public int fill(byte[] buffer, int offset, int length) {
			try {
				final int LEN = _Connection.getInputStream().read(buffer, offset, length);

//				printBuffer("FILL", buffer, offset, length);

				return LEN;
			} catch(Exception e) {
				e.printStackTrace();
			}

			return -1;
		}
	});

	public MyHttpConnection(String address) throws IOException {
		this(InetAddress.getByName(address), 80, SocketFactories.getPlainSocketFactory());
	}

	public MyHttpConnection(String address, int port) throws IOException {
		this(InetAddress.getByName(address), port, SocketFactories.getPlainSocketFactory());
	}

	public MyHttpConnection(String address, int port, ISocketFactory socketcreator) throws IOException {
		this(InetAddress.getByName(address), socketcreator);
	}

	public MyHttpConnection(URL url) throws IOException {
		if(url.getProtocol().equalsIgnoreCase("https")) {
			_Connection = new SimpleSocketConnection(url.getHost(), (url.getPort() <= 0 ? 443 : url.getPort()), SocketFactories.getDefaultSSLSocketFactory());
		} else {
			_Connection = new SimpleSocketConnection(url.getHost(), (url.getPort() <= 0 ? 80 : url.getPort()));
		}

		_BOStream = new MyOutputStream(new FixedByteBufferOutputStream(_OUT_BUFFER));
		_BIStream = new MyInputStream(new FixedByteBufferInputStream(_IN_BUFFER));
	}

	public MyHttpConnection(URL url, ISocketFactory socketcreator) throws IOException {
		_Connection = new SimpleSocketConnection(url.getHost(), (url.getPort() <= 0 ? 80 : url.getPort()), null, 0, socketcreator);

		_BOStream = new MyOutputStream(new FixedByteBufferOutputStream(_OUT_BUFFER));
		_BIStream = new MyInputStream(new FixedByteBufferInputStream(_IN_BUFFER));
	}

	public MyHttpConnection(InetAddress address) throws IOException {
		this(address, 80, SocketFactories.getPlainSocketFactory());
	}

	public MyHttpConnection(InetAddress address, ISocketFactory socketcreator) throws IOException {
		this(address, 80, socketcreator);
	}

	public MyHttpConnection(InetAddress address, int port, ISocketFactory socketcreator) throws IOException {
		_Connection = new SimpleSocketConnection(address, port, null, 0, socketcreator);

		_BOStream = new MyOutputStream(new FixedByteBufferOutputStream(_OUT_BUFFER));
		_BIStream = new MyInputStream(new FixedByteBufferInputStream(_IN_BUFFER));
	}

	public InetAddress getRemoteAddress() {
		return _Connection.getRemoteAddress();
	}

	public int getRemotePort() {
		return _Connection.getRemotePort();
	}

	public void setMaxRetries(int value) {
		if(value < 0) {
			throw new RuntimeException("Variable[value] - Must Be Greater Than Zero");
		}

		_Max_Retries = value;
	}

	public int getMaxRetries() {
		return _Max_Retries;
	}

	public OutputStream request(HttpEntry entry) throws IOException {
		return request(entry, false);
	}

	//GET, HEAD.....
	public synchronized OutputStream request(HttpEntry entry, boolean blockoutput) throws IOException {
		if(_Current_Input != null) {
			throw new RuntimeException("Variable[_Current_Input] - Did Not Finish Reading Input For Current Reply");
		}

		if(_Current_Output != null) {
			throw new RuntimeException("Variable[_Current_Output] - Did Not Finish Writing Output For Current Request");
		}

		if(entry == null) {
			throw new RuntimeException("Variable[entry] - Is Null");
		}

		if(entry.getMethod() == null) {
			throw new RuntimeException("Variable[entry.getMethod()] - Is Null");
		}

//		if(entry.getURI() == null) {
//			throw new RuntimeException("Variable[entry.getUrl()] - Is Null");
//		}

//		if(entry.getURI().getHost() == null && _Connection.getRemoteInetAddress().getHostName() == null) {
//			throw new RuntimeException("Variable[entry.getUrl().getHost()] - Is Null");
//		}

		if(!_Connection.isActivelyConnected()) {
			int Retry_Count = 0;
			for(; Retry_Count <= _Max_Retries; Retry_Count++) {
				try {
					_Connection.reconnect();
					break;
				} catch(Exception e2) {}
			}

			if(Retry_Count >= _Max_Retries) {
				throw new IOException("Unable To Reconnect To Server");
			}
		}

		_BOStream.writeString(entry.getMethod().toString());

		_BOStream.writeChar(' ');

		if(entry.getPath() == null || entry.getPath().length() == 0) {
			entry.setPath("/");
		}

		_BOStream.writeString(entry.getPath());

		_BOStream.writeChar(' ');
		_BOStream.writeString("HTTP/1.1");
		_BOStream.newline(Line_Ending.WINDOWS);

		if(entry.getHost() == null || entry.getHost().length() == 0) {
			entry.setHost(_Connection.getRemoteAddress().getHostName());
		}

		HttpEntry.Header T_Header = null;

		if((T_Header = entry.getHeader("Content-Length")) == null) {
			entry.setContentLength(0);
		}

		if((T_Header = entry.getHeader("Connection")) == null) {
			entry.setKeepAlive(true);
		}

		for(int X = 0; X < entry.headerCount(); X++) {
			T_Header = entry.getHeaderAt(X);

			if(T_Header != null) {
				writeHeader(T_Header);
			}
		}

		_BOStream.newline(Line_Ending.WINDOWS);
		_BOStream.flush();

		System.out.println();
		System.out.println("REQUEST");
		System.out.println(entry.toString());

		if(entry.getMethod() == Method.POST || entry.getMethod() == Method.PUT) {
			if(blockoutput) {
				return new HttpOutputBlockStream();
			} else {
				if(entry.getContentLength() <= 0) {
					throw new IOException("Negative Or Zero Content Length");
				}

				return new HttpOutputStream(entry.getContentLength());
			}
		} else {
			return null;
		}
	}

	private String[] getReplyAndParse() throws IOException {
		final String LINE = _BIStream.readln();

		final ResizingArray<String> RETURN = new ResizingArray<String>(3);
		final MyStringBuffer BUFFER = new MyStringBuffer(LINE.length());

		int X = 0;
		for(; X < LINE.length(); X++) {
			if(LINE.charAt(X) == ' ' && RETURN.length() < 2) {
				RETURN.put(BUFFER.toString(true));
			} else {
				BUFFER.append(LINE.charAt(X));
			}
		}

		if(BUFFER.length() > 0) {
			RETURN.put(BUFFER.toString(true));
		}

		return RETURN.toArray(new String[RETURN.length()]);
	}

	public synchronized InputStream reply(HttpEntry entry) throws IOException {
		if(_Current_Input != null) {
			throw new RuntimeException("Variable[_Current_Input] - Did Not Finish Reading Input For Current Reply");
		}

		if(_Current_Output != null) {
			throw new RuntimeException("Variable[_Current_Output] - Did Not Finish Writing Output For Current Request");
		}

		if(entry == null) {
			throw new RuntimeException("Variable[entry] - Is Null");
		}

		final String[] STATUS = getReplyAndParse()/* _BIStream.readln().split(" ") */;

		if(STATUS.length != 3) {
			throw new IOException("Not Enough HTTP Parameters");
		}

		if(STATUS[0].length() == 0 || !(STATUS[0].toUpperCase().startsWith("HTTP/1."))) {
			throw new IOException("Unknown HTTP Reply" + ' ' + STATUS[0]);
		}

		if(!StringUtil.isWholeNumber(STATUS[1])) {
			throw new IOException("Status Code Is Not A Number" + ' ' + STATUS[1]);
		}

		final int STATUS_CODE = Integer.parseInt(STATUS[1]);

		if(!HttpCodes.validStatusCode(STATUS_CODE)) {
			throw new IOException("Status Code Out Of Range" + ' ' + STATUS_CODE);
		}

		entry.setStatusCode(STATUS_CODE);
		entry.setStatusMsg(STATUS[2]);

		HttpEntry.Header T_Header = null;
		String Line = null;
		String Previous_Header_Name = null;
		while((Line = _BIStream.readln()) != null) {
			if(Line.length() == 0) {
				break;
			}

			System.out.println(Line);

			final IntByRef OFFSET = new IntByRef(0);
			if(Line.charAt(0) == ' ') {
				StringUtil.skipsWhiteSpaces(Line, OFFSET, Line.length());

				final String VALUE = Line.substring(OFFSET.value);

				if(VALUE.length() > 0) {
					final HttpEntry.Header P_VALUE = entry.getHeader(Previous_Header_Name);

					if(P_VALUE != null) {
						P_VALUE.setKeyPair("", P_VALUE.getKeyPair("") + VALUE);
					}
				}
			} else {
				final String NAME = readName(Line, OFFSET, Line.length());

				if(NAME.length() > 0) {
					final String VALUE = readValue(Line, OFFSET, Line.length());

					if(VALUE != null && VALUE.length() > 0) {
						T_Header = entry.getHeader(NAME);

						if(T_Header == null) {
							T_Header = entry.createHeader(NAME);
						}

						final String[] SPLIT = VALUE.split(";");

						//Has More Than 1 Parameter
						if(SPLIT.length > 1) {
							for(int X = 0; X < SPLIT.length; X++) {
								final String T_STR = SPLIT[X].trim();

								final int EQUALS_INDEX = T_STR.indexOf('=');

								if(EQUALS_INDEX != -1) {
									T_Header.setKeyPair(T_STR.substring(0, EQUALS_INDEX), T_STR.substring(EQUALS_INDEX + 1));
								}
							}
						} else {
							T_Header.setKeyPair("", VALUE);
						}
					}

					Previous_Header_Name = NAME;
				}
			}
		}

//		SystemHandler.getInstance().printInformation("REPLY");
//		SystemHandler.getInstance().printBlank(entry.toString());

		if((T_Header = entry.getHeader("Transfer-Encoding")) != null && T_Header.getValue("").equalsIgnoreCase("chunked")) {

			if(entry.getMethod() == Method.POST || entry.getMethod() == Method.PUT) {
				return null;
			} else {
				return new HttpInputBlockStream();
			}
		} else {
			if(entry.getContentLength() < 0) {
				throw new IOException("Negative Content Length");
			}

			if(entry.getMethod() == Method.POST || entry.getMethod() == Method.PUT) {
				return null;
			} else {
				return new HttpInputStream(entry.getContentLength());
			}
		}
	}

	private static String readName(String line, IntByRef offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		while(offset.value < length && line.charAt(offset.value) != ':') {
			BUFFER.append(line.charAt(offset.value));

			offset.value++;
		}
		offset.value++;

		return BUFFER.toString();
	}

	private static String readValue(String line, IntByRef offset, int length) {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		StringUtil.skipsWhiteSpaces(line, offset);

		for(; offset.value < length; offset.value++) {
			BUFFER.append(line.charAt(offset.value));
		}

		return BUFFER.toString();
	}

	private void writeHeader(HttpEntry.Header header) throws IOException {
		if(header == null) {
			throw new RuntimeException("Variable[header] - Is Null");
		}

		if(header.getName() == null || header.getName().length() == 0) {
			throw new RuntimeException("Variable[header.getName()] - Is Null");
		}

		_BOStream.writeString(header.getName());
		_BOStream.writeChar(':');
		_BOStream.writeChar(' ');

		for(int X = 0; X < header.keypairCount(); X++) {
			final Collection<String, String>.Entry ENTRY = header.getKeyPair(X);

			if(ENTRY.getKey() != null && ENTRY.getKey().length() > 0) {
				_BOStream.writeString(ENTRY.getKey());

				_BOStream.writeChar(';');
				_BOStream.writeChar(' ');
			}

			_BOStream.writeString(ENTRY.getObject());
		}

		_BOStream.newline(Line_Ending.WINDOWS);
	}

	public boolean isClosed() {
		if(_Connection != null) {
			return _Connection.isActivelyConnected();
		}

		return true;
	}

	public synchronized void close() throws IOException {
		if(_Current_Input != null) {
			_Current_Input.close();
			_Current_Input = null;
		}

		if(_Current_Output != null) {
			_Current_Output.flush();
			_Current_Output.close();
			_Current_Output = null;
		}

		if(_BIStream != null) {
			_BIStream.close();
			_BIStream = null;
		}

		if(_BOStream != null) {
			_BOStream.flush();
			_BOStream.close();
			_BOStream = null;
		}

		if(_Connection != null) {
			_Connection.close();
			_Connection = null;
		}
	}

	//CLASSES
	private class HttpOutputStream extends ParticalOutputStream {
		public HttpOutputStream(long limit) throws IOException {
			super(_BOStream, limit);

			_Current_Output = this;
		}

		@Override
		public synchronized void close() throws IOException {
			_Current_Output = null;

			super.close();
		}
	}

	private class HttpInputStream extends ParticalInputStream {
		public HttpInputStream(long limit) throws IOException {
			super(_BIStream, limit);

			_Current_Input = this;
		}

		@Override
		public synchronized void close() throws IOException {
			_Current_Input = null;

			super.close();
		}
	}

	public class HttpOutputBlockStream extends OutputStream {
		private long _Total_Length = 0;
		private int _Chunk_Length = 0;
		private int _Chunk_Pos = 0;

		public HttpOutputBlockStream() throws IOException {
			_Current_Output = this;
		}

		public void writeChunkLength(int value) throws IOException {
			if(_Chunk_Pos >= _Chunk_Length) {
				_Total_Length += (_Chunk_Length = value);
				_Chunk_Pos = 0;

				_BOStream.writeStringln(Integer.toHexString(value));
			}
		}

		public int getChunkLength() {
			return _Chunk_Length;
		}

		public long getTotalLength() {
			return _Total_Length;
		}

		@Override
		public void write(int value) throws IOException {
			if(_Chunk_Pos >= _Chunk_Length) {
				throw new IOException("Must Write Buffer Length Using writeChunkLength(int)");
			}

			_Chunk_Pos++;

			_BOStream.write(value);
		}

		@Override
		public void flush() throws IOException {
			_BOStream.flush();
		}

		public synchronized void close() throws IOException {
			_Current_Output = null;
		}
	}

	private class HttpInputBlockStream extends InputStream {
		private long _Total_Length = 0;
		private int _Chunk_Length = 0;
		private int _Chunk_Pos = 0;

		public HttpInputBlockStream() throws IOException {
			_Current_Input = this;
		}

		private void readChunkLength() throws IOException {
			while(true) {
				final String STR_LEN = _BIStream.readln();

				if(STR_LEN != null && STR_LEN.length() > 0) {
					_Total_Length += (_Chunk_Length = Integer.parseInt(STR_LEN.trim(), 16));
					_Chunk_Pos = 0;

//					System.out.println("Raw = " + STR_LEN + " - Chunk Length = " + _Chunk_Length);
					break;
				}
			}
		}

		@Override
		public int read() throws IOException {
			if(_Chunk_Length == -1) {
				throw new IOException("End Of Stream");
			}

			if(_Chunk_Pos > _Chunk_Length) {
				readChunkLength();

				if(_Chunk_Length <= 0) {
					return (_Chunk_Length = -1);
				}
			}

			_Chunk_Pos++;

			return _BIStream.read();
		}

		public synchronized void close() throws IOException {
			_Current_Input = null;
		}
	}
}
