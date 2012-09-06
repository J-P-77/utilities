package utillib.net;

import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
import utillib.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStore.Entry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
// import java.security.Security;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
// import java.security.Provider;

import java.util.Arrays;

public class MyKeyStore {
	private static final char[] _DEFAULT_KS_PASSWORD_ = "1234567890".toCharArray();
	private static final File _DEFAULT_KEYSTORE_ = new File(FileUtil._APP_PATH_, "Default.ks");

	public static final String _DEFAULT_KEYSTORE_ALGORITHM_ = "JKS";

	public static final String _DEFAULT_CERTIFICATE_TYPE_ = "SunX509";

	public static final String _DEFAULT_KEYMANAGERFACTORY_ALGORITHM_ = _DEFAULT_CERTIFICATE_TYPE_;
	public static final String _DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM_ = _DEFAULT_CERTIFICATE_TYPE_;

	private final char[] _KEYSTORE_PASSWORD;

	private KeyStore _KeyStore = null;

	private final String _KEYSTORE_ALGORITHM;

	private final String _KEYSTORE_PROVIDER;

	private final File _FILE;

	public MyKeyStore(File file, char[] keystorepassword, String keystore_algorithm) {
		this(file, keystorepassword, keystore_algorithm, null);
	}

//    public MyKeyStore(File file, char[] keystorepassword, String keystore_algorithm, String keystore_provider) {
//    	this(file, keystorepassword, keystore_algorithm, Security.getProvider(keystore_provider));
//    }

	public MyKeyStore(File file, char[] keystorepassword, String keystore_algorithm, String keystore_provider) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(keystorepassword == null || keystorepassword.length == 0) {
			throw new RuntimeException("Variable[keystorepassword] - Is Null");
		}

		if(keystore_algorithm == null) {
			throw new RuntimeException("Variable[keystore_algorithm] - Is Null");
		}

		_FILE = file;
		_KEYSTORE_ALGORITHM = keystore_algorithm;
		_KEYSTORE_PROVIDER = keystore_provider;
		_KEYSTORE_PASSWORD = keystorepassword;
	}

	public boolean isOpen() {
		return _KeyStore != null;
	}

	public KeyStore getKeyStore() {
		return _KeyStore;
	}

	public boolean addCertificate(String alias, Certificate cert, boolean overwrite) {
		if(alias == null) {
			throw new RuntimeException("Variable[alias] - Is Null");
		}

		if(cert == null) {
			throw new RuntimeException("Variable[certfile] - Is Null");
		}

		try {
			if(overwrite) {
				_KeyStore.setCertificateEntry(alias, cert);
			} else {
				if(_KeyStore.getCertificate(alias) == null) {
					_KeyStore.setCertificateEntry(alias, cert);
				}
			}
			return true;
		} catch(Exception e) {}

		return false;
	}

	public boolean addCertificate(String alias, File certfile, boolean overwrite) {
		return addCertificate(alias, certfile, overwrite, _DEFAULT_CERTIFICATE_TYPE_);
	}

	public boolean addCertificate(String alias, File certfile, boolean overwrite, String certificate_type) {
		return addCertificate(alias, certfile, overwrite, certificate_type, null);
	}

	public boolean addCertificate(String alias, File certfile, boolean overwrite, String certificate_type, String certificate_provider) {
		if(certfile == null) {
			throw new RuntimeException("Variable[certfile] - Is Null");
		}

		if(!certfile.exists()) {
			throw new RuntimeException(certfile.getPath() + " Does Not Exists");
		}

		if(certfile.isDirectory()) {
			throw new RuntimeException(certfile.getPath() + " Must Not Be A Directory");
		}

		FileInputStream IStream = null;
		try {
			IStream = new FileInputStream(certfile);

			return addCertificate(alias, IStream, overwrite, certificate_type, certificate_provider);
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

	public boolean addCertificate(String alias, InputStream istreamcert, boolean overwrite) {
		return addCertificate(alias, istreamcert, overwrite, _DEFAULT_CERTIFICATE_TYPE_);
	}

	public boolean addCertificate(String alias, InputStream istreamcert, boolean overwrite, String certificate_type) {
		return addCertificate(alias, istreamcert, overwrite, certificate_type, null);
	}

	public boolean addCertificate(String alias, InputStream istreamcert, boolean overwrite, String certificate_type, String certificate_provider) {
		if(alias == null) {
			throw new RuntimeException("Variable[alias] - Is Null");
		}

		if(istreamcert == null) {
			throw new RuntimeException("Variable[istreamcert] - Is Null");
		}

		try {
			final CertificateFactory FACTORY;

			if(certificate_provider == null) {
				FACTORY = CertificateFactory.getInstance(certificate_type);
			} else {
				FACTORY = CertificateFactory.getInstance(certificate_type, certificate_provider);
			}

			return addCertificate(alias, FACTORY.generateCertificate(istreamcert), overwrite);
		} catch(Exception e) {}

		return false;
	}

	public boolean addPublicKeyEntry(String alias, PublicKey key) {
		try {
			_KeyStore.setKeyEntry(alias, key, _KEYSTORE_PASSWORD, null);
			return true;
		} catch(Exception e) {}

		return false;
	}

	public boolean addPrivateKeyEntry(String alias, PrivateKey key) {
		try {
			_KeyStore.setKeyEntry(alias, key, _KEYSTORE_PASSWORD, null);
			return true;
		} catch(Exception e) {}

		return false;
	}

	public void open() {
		FileInputStream IStream = null;
		try {
			IStream = new FileInputStream(_FILE);

			if(_KEYSTORE_PROVIDER == null) {
				_KeyStore = KeyStore.getInstance(_KEYSTORE_ALGORITHM);
			} else {
				_KeyStore = KeyStore.getInstance(_KEYSTORE_ALGORITHM, _KEYSTORE_PROVIDER);
			}
			_KeyStore.load(IStream, _KEYSTORE_PASSWORD);
		} catch(Exception e) {
			_KeyStore = null;
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception e) {}
				IStream = null;
			}
		}
	}

	public void save() {
		FileOutputStream OStream = null;
		try {
			OStream = new FileOutputStream(_FILE);

			_KeyStore.store(OStream, _KEYSTORE_PASSWORD);
		} catch(Exception e) {
			_KeyStore = null;
		} finally {
			if(OStream != null) {
				try {
					OStream.close();
				} catch(Exception e) {}
				OStream = null;
			}
		}
	}

	public void close() {
		_KeyStore = null;
		Arrays.fill(_KEYSTORE_PASSWORD, ' ');
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	private static MyKeyStore _Default_KeyStore = null;

	public static MyKeyStore getInstance() {
		if(_Default_KeyStore == null) {
			_Default_KeyStore = new MyKeyStore(_DEFAULT_KEYSTORE_, _DEFAULT_KS_PASSWORD_, _DEFAULT_KEYSTORE_ALGORITHM_, null);
			_Default_KeyStore.open();
		}

		return _Default_KeyStore;
	}

	//STATIC
	public static KeyStore loadKeyStore(String password, File keystorefile, String keystore_algorithm) throws KeyStoreException {
		return loadKeyStore(KeyStore.getInstance(KeyStore.getDefaultType()), password, keystorefile, keystore_algorithm);
	}

	public static KeyStore loadKeyStore(String password, File keystorefile, String keystore_algorithm, String keystore_provider) throws KeyStoreException {
		return loadKeyStore(KeyStore.getInstance(KeyStore.getDefaultType()), password, keystorefile, keystore_algorithm);
	}

	public static KeyStore loadKeyStore(KeyStore keystore, String password, File keystorefile, String keystore_algorithm) {
		return loadKeyStore(keystore, password, keystorefile, keystore_algorithm);
	}

	public static KeyStore loadKeyStore(KeyStore keystore, String password, File keystorefile, String keystore_algorithm, String keystore_provider) {
		final char[] PASSWORD = password.toCharArray();

		try {
			if(keystore_provider == null) {
				keystore = KeyStore.getInstance(keystore_algorithm);//"JKS"
			} else {
				keystore = KeyStore.getInstance(keystore_algorithm, keystore_provider);
			}
			keystore.load(new java.io.FileInputStream(keystorefile), PASSWORD);

			return keystore;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	public static void main(String[] args) {
//		try {
//			KeyPairGenerator KEY_GEN = KeyPairGenerator.getInstance("RSA");
//			
//			KEY_GEN.initialize(4096);
//			
//			final KeyPair PAIR = KEY_GEN.generateKeyPair();
//
//			CertificateFactory CF = CertificateFactory.getInstance("");
//
//			final X509CertImpl C_CERT = new X509CertImpl(PAIR.getPrivate().getEncoded());
////			X509CertInfo C_INFO = (X509CertInfo)C_CERT;
////			
////			X509CertInfo INFO = new X509CertInfo(PAIR.getPrivate().getEncoded());
////			INFO.set(arg0, arg1);
//
//			X509CertImpl certImpl = new X509CertImpl(PAIR.getPrivate().getEncoded());
//			X509CertInfo certInfo = (X509CertInfo)certImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
//
//			certInfo.set(X509CertInfo., arg1)
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//	}
}

/*
 * final File DIR = new
 * File("C:\\Documents and Settings\\Dalton Dell\\Desktop\\firefox's certs");
 * final File KS = new
 * File("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Default.ks");
 * 
 * final File[] FILES = DIR.listFiles(); for(int X = 0; X < FILES.length; X++) {
 * final int INDEX = FILES[X].getName().indexOf('_');
 * 
 * final String ALIAS; if(INDEX != -1) { ALIAS =
 * FileExtended.getNameWithOutExtension(FILES[X].getName().substring(INDEX +
 * 1)); } else { ALIAS =
 * FileExtended.getNameWithOutExtension(FILES[X].getName()); }
 * 
 * if(addCertToKeystore(FILES[X], ALIAS, KS, "1234567890")) {
 * System.out.println(ALIAS + " Done"); } else { System.out.println(ALIAS +
 * " Error"); // break; }
 * 
 * utillib.utilities.ThreadUtil.sleep(100); }
 * 
 * //keytool -importcert -file $(CERT_FILE) -keystore $(KETSTORE_FILE)
 * -storepass $(PASSWORD) public static boolean addCertToKeystore(String
 * certfile, String certalias, String keystore, String password) { return
 * addCertToKeystore(new File(certfile), certalias, new File(keystore),
 * password); }
 * 
 * public static boolean addCertToKeystore(File certfile, String certalias, File
 * keystore, String password) { if(!Os.isWindows()) { throw new
 * RuntimeException("Currently Only Windows Is Supported"); }
 * 
 * if(certfile == null) { throw new
 * RuntimeException("Variable[certfile] - Is Null"); }
 * 
 * if(keystore == null) { throw new
 * RuntimeException("Variable[keystore] - Is Null"); }
 * 
 * if(password == null) { throw new
 * RuntimeException("Variable[password] - Is Null"); }
 * 
 * final File KEYTOOL_EXE = new
 * File(SystemProperties.getProperty(SystemProperties._JAVA_HOME_), FileUtil._S_
 * + "bin" + FileUtil._S_ + "keytool.exe");
 * 
 * if(KEYTOOL_EXE.exists() && certfile.exists()) { final String STR = '"' +
 * KEYTOOL_EXE.getPath() + "\" -importcert -file " + '"' + certfile.getPath() +
 * "\" -keystore \"" + keystore.getPath() + "\" -storepass " + password +
 * " -alias \"" + (certalias == null ?
 * FileExtended.getNameWithOutExtension(certfile.getName()) : certalias) +
 * "\" -noprompt";
 * 
 * try { final Process P = Runtime.getRuntime().exec(STR);
 * 
 * return (P.waitFor() == 0); } catch (Exception e) {} }
 * 
 * return false; }
 */