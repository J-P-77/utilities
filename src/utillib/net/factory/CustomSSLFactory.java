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

package utillib.net.factory;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

public class CustomSSLFactory {
	private KeyStore _KeyStore = null;
	private KeyManagerFactory _KMF = null;
	private TrustManagerFactory _TMF = null;

	private File _File_Keystore = null;

	public CustomSSLFactory(String name) {}

	public boolean loadKeyStory(char[] password, File file, String algorithm, String provider) {
		if(password == null) {
			throw new RuntimeException("Variable[password] - Is Null");
		}

		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(algorithm == null) {
			throw new RuntimeException("Variable[algorithm] - Is Null");
		}

		_File_Keystore = file;

		FileInputStream IStream = null;
		try {
			IStream = new FileInputStream(file);

			if(provider == null) {
				_KeyStore = KeyStore.getInstance(algorithm);//"JKS"
			} else {
				_KeyStore = KeyStore.getInstance(algorithm, provider);
			}
			_KeyStore.load(IStream, password);

			return true;
		} catch(Exception e) {
			// TODO: handle exception
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception e) {}
				IStream = null;
			}
		}

		return false;
	}

	public boolean setKeyManagerFactory(char[] password, String algorithm, String provider) {
		if(algorithm == null) {
			throw new RuntimeException("Variable[algorithm] - Is Null");
		}

		try {
			if(provider == null) {
				_KMF = KeyManagerFactory.getInstance(algorithm);//"SunX509"
			} else {
				_KMF = KeyManagerFactory.getInstance(algorithm, provider);
			}
			_KMF.init(_KeyStore, password);
			return true;
		} catch(Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	public void setTrustManagerFactory(String algorithm, String provider) {
		if(algorithm == null) {
			throw new RuntimeException("Variable[algorithm] - Is Null");
		}

		if(_KeyStore == null) {
			throw new RuntimeException("Variable[_KeyStore] - Is Null");
		}

		try {
			if(provider == null) {
				_TMF = TrustManagerFactory.getInstance(algorithm);//"SunX509"
			} else {
				_TMF = TrustManagerFactory.getInstance(algorithm, provider);
			}
			_TMF.init(_KeyStore);
		} catch(Exception e) {
			// TODO: handle exception
		}
	}

	public void setSSLContext(String algorithm, String provider, boolean server) {
		if(algorithm == null) {
			throw new RuntimeException("Variable[algorithm] - Is Null");
		}

		if(_KMF == null) {
			throw new RuntimeException("Variable[KMF] - Is Null");
		}

		if(_TMF == null) {
			throw new RuntimeException("Variable[TMF] - Is Null");
		}

		SSLContext Context = null;
		try {
			if(provider == null) {
				Context = SSLContext.getInstance(algorithm);//TLS, SSL
			} else {
				Context = SSLContext.getInstance(algorithm, provider);
			}
			KeyManager[] KM = _KMF.getKeyManagers();
			TrustManager[] TM = _TMF.getTrustManagers();

			if(server) {
				Context.init(new KeyManager[] {new MyX509KeyManager("Justin", "paradime".toCharArray())}, _TMF.getTrustManagers(), null);
			} else {
				Context.init(KM, _TMF.getTrustManagers(), null);
			}
//			super.setSSLContext(Context);
		} catch(Exception e) {
			// TODO: handle exception
		}
	}

	private class MyX509KeyManager implements X509KeyManager {
		private String _Alias = null;
		private char[] _Password = null;

		public MyX509KeyManager(String alias, char[] password) {
			if(alias == null) {
				throw new RuntimeException("Variable[alias] - Is Null");
			}

			if(password == null) {
				throw new RuntimeException("Variable[password] - Is Null");
			}

			_Alias = alias;
			_Password = password;
		}

		@Override
		public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
			return _Alias;
		}

		@Override
		public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
			return _Alias;
		}

		@Override
		public X509Certificate[] getCertificateChain(String alias) {
			try {
				final Certificate[] CERTS = _KeyStore.getCertificateChain(alias);
				final X509Certificate[] RETURN = new X509Certificate[CERTS.length];

				for(int X = 0; X < CERTS.length; X++) {
					RETURN[X] = (X509Certificate)CERTS[X];
				}

				return RETURN;
			} catch(Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public String[] getClientAliases(String keyType, Principal[] issuers) {
			return new String[] {_Alias};
		}

		@Override
		public PrivateKey getPrivateKey(String alias) {
			try {
				return (PrivateKey)_KeyStore.getKey(alias, _Password);
			} catch(Exception e) {}

			return null;
		}

		@Override
		public String[] getServerAliases(String keyType, Principal[] issuers) {
			return new String[] {_Alias};
		}
	}
}
