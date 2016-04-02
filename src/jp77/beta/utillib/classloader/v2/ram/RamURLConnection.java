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

package jp77.beta.utillib.classloader.v2.ram;

import jp77.utillib.collections.Collection;

import jp77.utillib.io.ByteArrayInputStream;
import jp77.utillib.io.ByteArrayOutputStream;

import jp77.utillib.strings.MyStringBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

public class RamURLConnection extends URLConnection {
	private static final String _PACKAGE_ = "jp77.beta.utillib.classloader.v2";
//	private static final String _PACKAGE_ = RamURLConnection.class.getPackage().getName().substring(0, RamURLConnection.class.getPackage().getName().length() - 4);

	private static final Collection<String, Collection<String, byte[]>> _LISTENERS = new Collection<String, Collection<String, byte[]>>();

	private InputStream _IStream = null;
	private OutputStream _OStream = null;
	private long _Length = 0;

	public RamURLConnection(URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
		if(_IStream == null) {
			final Collection<String, byte[]> RAM = _LISTENERS.get(super.getURL().getHost());

			if(RAM == null) {
				throw new IOException("Unable Too Find Ram Id");
			} else {
				final MyStringBuffer BUFFER = new MyStringBuffer(super.getURL().getPath());

				if(BUFFER.length() > 0) {
					if(BUFFER.charAt(0) == '/') {
						BUFFER.remove(0);
					}
				}
				BUFFER.replace('/', '.');

				final byte[] BYTES = RAM.get((BUFFER.toString()));

				if(BYTES == null) {
					throw new IOException("Resource: " + BUFFER.toString() + " Not Found");
				} else {
					_IStream = new ByteArrayInputStream(BYTES);
					_OStream = new ByteArrayOutputStream(BYTES);
					_Length = _IStream.available();
				}
			}
		}
	}

	@Override
	public int getContentLength() {
		return (int)_Length;
	}

	@Override
	public long getContentLengthLong() {
		return _Length;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return _IStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return _OStream;
	}

	//STATIC
	/*
	 * <<RamId-${X})>, <Resource Id, Resource Bytes>>
	 */
	/*public static Collection<String, Collection<String, byte[]>> getListeners() {
		return _LISTENERS;
	}*/

	public static void registerPackage() {
		final MyStringBuffer BUFFER = new MyStringBuffer(System.getProperty("java.protocol.handler.pkgs", ""));

		final int INDEX = BUFFER.indexOf(_PACKAGE_);

		if(INDEX == -1) {
			BUFFER.append('|');
			BUFFER.append(_PACKAGE_);

			System.setProperty("java.protocol.handler.pkgs", BUFFER.toString());
		}
	}

	public static void unRegisterPackage() {
		final MyStringBuffer BUFFER = new MyStringBuffer(System.getProperty("java.protocol.handler.pkgs", ""));

		if(BUFFER.length() > 0) {
			final int INDEX = BUFFER.indexOf(_PACKAGE_);

			if(INDEX != -1) {
				BUFFER.remove(INDEX, _PACKAGE_.length());

				System.setProperty("java.protocol.handler.pkgs", BUFFER.toString());
			}
		}
	}
}
