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

package jp77.utillib.security;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * January 29, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SecureByteArray {
	public byte[] _Password = null;

	public SecureByteArray() {}

	public SecureByteArray(StringBuffer password) {
		setPassword(password);
	}

	public SecureByteArray(String password) {
		setPassword(password);
	}

	public SecureByteArray(char[] password) {
		setPassword(password);
	}

	public SecureByteArray(byte[] password) {
		setPassword(password);
	}

	public SecureByteArray(int[] password) {
		setPassword(password);
	}

	public void setPassword(StringBuffer password) {
		clearPassword();

		_Password = new byte[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = (byte)password.charAt(X);
		}
	}

	public void setPassword(String password) {
		clearPassword();

		_Password = new byte[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = (byte)password.charAt(X);
		}
	}

	public void setPassword(char[] password) {
		clearPassword();

		_Password = new byte[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (byte)password[X];
		}
	}

	public void setPassword(byte[] password) {
		clearPassword();

		_Password = password;
	}

	public void setPassword(int[] password) {
		clearPassword();

		_Password = new byte[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (byte)password[X];
		}
	}

	public void clearPassword() {
		//Clear's Password Buffer
		randomBuffer();
		eraseBuffer();
		_Password = null;
	}

	private void randomBuffer() {
		if(_Password != null) {
			for(int X = 0; X < _Password.length; X++) {
				try {
					_Password[X] = (byte)(int)((Byte.MAX_VALUE - Byte.MIN_VALUE + 1) * Math.random() + Byte.MIN_VALUE);
				} catch(Exception e) {
					_Password[X] = 77;
				}
			}
		}
	}

	private void eraseBuffer() {
		if(_Password != null) {
			for(int X = 0; X < _Password.length; X++) {
				_Password[X] = 0;
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		clearPassword();

		super.finalize();
	}
}
