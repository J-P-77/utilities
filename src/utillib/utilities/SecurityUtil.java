package utillib.utilities;

import utillib.strings.StringUtil;

import java.util.Iterator;
import java.util.Set;

import java.security.AlgorithmParameters;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;

import javax.crypto.Cipher;

import beta.utillib.classloader.v1.JarClassLoader;
import beta.utillib.classloader.v1.MyClassloader;
import beta.utillib.classloader.v2.wrappers.ClassWrapper;

/**
 *
 * @author Dalton Dell
 */
public class SecurityUtil {
//	public static final String _ALGORITHM_SECURERANDOM_ = "SecureRandom";
//	public static final String _ALGORITHM_MESSAGEDIGEST_ = "MessageDigest";
//	public static final String _ALGORITHM_SIGNATURE_ = "Signature";
//	public static final String _ALGORITHM_CIPHER_ = "Cipher";
//	public static final String _ALGORITHM_ = "";
//	public static final String _ALGORITHM_KEYFACTORY_ = "KeyFactory";
//	public static final String _ALGORITHM_SECRETKEYFACTORY_ = "SecretKeyFactory";
//	public static final String _ALGORITHM_KEYPAIRGENERATOR_ = "KeyPairGenerator";
//	public static final String _ALGORITHM_KEYGENERATOR_ = "KeyGenerator";
//	public static final String _ALGORITHM_KEYAGREEMENT_ = "KeyAgreement";
//	public static final String _ALGORITHM_ALGORITHMPARAMETERS_ = "AlgorithmParameters";
//	public static final String _ALGORITHM_ALGORITHMPARAMETERGENERATOR_ = "AlgorithmParameterGenerator";
//	public static final String _ALGORITHM_KEYSTORE_ = "KeyStore";
//	public static final String _ALGORITHM_CERTIFICATEFACTORY_ = "CertificateFactory";
//	public static final String _ALGORITHM_CERTPATHBUILDER_ = "CertPathBuilder";
//	public static final String _ALGORITHM_CERTPATHVALIDATOR_ = "CertPathValidator";
//	public static final String _ALGORITHM_CERTSTORE_ = "CertStore";
	
//	Message Authentication Codes (MAC)

//MessageDigest, Cipher
    public static String[] getCipherAlgorithms() {
        return getAlgorithms("Cipher");
    }

    public static String[] getSupportedCipherModes(String ciphername) {
		return null;
    }
    
    public static String[] getDigestAlgorithms() {
        return getAlgorithms("MessageDigest");
    }
    
    public static String[] getAlgorithms(String servicename) {
    	if(servicename == null) {
			throw new RuntimeException("Variable[servicename] - Is Null");
		}
		
        final Set<String> A = Security.getAlgorithms(servicename);

        if(A == null) {
        	throw new RuntimeException("Variable[A] - Is Null");
        }
        
        return A.toArray(new String[A.size()]);
    }
    
    public static boolean cipherExists(String name) {
        return cipherExists(name, false);
    }

    public static boolean cipherExists(String name, boolean matchcase) {
        return algorithmExists(getCipherAlgorithms(), name, matchcase);
    }

    public static boolean digestExists(String name) {
        return digestExists(name, false);
    }

    public static boolean digestExists(String name, boolean matchcase) {
        return algorithmExists(getDigestAlgorithms(), name, matchcase);
    }
    
    public static boolean algorithmExists(String[] algorithms, String name, boolean matchcase) {
    	if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}
		
    	if(algorithms == null) {
			throw new RuntimeException("Variable[algorithms] - Is Null");
		}
    	
        final String NAME = (matchcase ? name : name.toLowerCase());

        for(int X = 0; X < algorithms.length; X++) {
            final String DIGEST = (matchcase ? algorithms[X] : algorithms[X].toLowerCase());

            if(NAME.equals(DIGEST)) {
                return true;
            }
        }

        return false;
    }
    
    //Cipher
    //	SupportedKeyFormats, SupportedModes, SupportedPaddings
    public static String[] getServiceTypeAttribute(String servicetype, String algorithm, String attributename) {
		for(int X = 0; X < Security.getProviders().length; X++) {
			final Provider P = Security.getProviders()[X];
			final Set<Service> S = P.getServices();
			final Iterator<Service> I = S.iterator();

			while(I.hasNext()) {
				final Service SERVICE = I.next();

				if(servicetype.equalsIgnoreCase(SERVICE.getType())) {
					if(algorithm.equalsIgnoreCase(SERVICE.getAlgorithm())) {
						final String A = SERVICE.getAttribute(attributename);
						
						if(A != null) {
							return StringUtil.split(A, '|');
						}
					}
				}
			}
		}
		
		return null;
    }
    
//	"algorithm/mode/padding" or
//	"algorithm"
    public static void main(String[] args) {
//        final Set<String> A = Security.getAlgorithms("KeyPairGenerator");
//        
//        final String[] ARRAY = A.toArray(new String[A.size()]);
//    	
//    	for(String X : ARRAY) {
//    		System.out.println(X);
//    	}
//    	
//    	try {
//    		java.security.KeyPairGenerator KP = java.security.KeyPairGenerator.getInstance("RSA");
//    		
//    		KP.initialize(1024 * 8);
//    		
//    		java.security.KeyPair KEY = KP.genKeyPair();   		
//    		
//    		System.out.println("Private: " + StringUtil.getString(KEY.getPrivate().getEncoded()));
//    		System.out.println("Public:  " + StringUtil.getString(KEY.getPublic().getEncoded()));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	if(true) return;
    	
		for(int X = 0; X < Security.getProviders().length; X++) {
			final Provider P = Security.getProviders()[X];

			System.out.println(P.getName());
			Set<Service> S = P.getServices();

			Iterator<Service> I = S.iterator();

			while(I.hasNext()) {
				final Service SERVICE = I.next();
				
				System.out.println(SERVICE.toString());
			}

			System.out.println("================================================================");
		}

		if(true) return;
		
//		try {
//			final String[] A = SecurityUtil.getAlgorithms("Paddings");
//
//			for(String X : A) {
//				System.out.println(X);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
    	
//    	final String[] A = getAlgorithms("Cipher");
//    	for(String X : A) {
//    		System.out.println(X);
//    	}
//		
    	final String[] MODES = getServiceTypeAttribute("Cipher", "AES", "SupportedModes");
    	for(String X : MODES) {
    		System.out.println(X);
    	}
    	final String[] PADDINGS = getServiceTypeAttribute("Cipher", "AES", "SupportedPaddings");
    	for(String X : PADDINGS) {
    		System.out.println(X);
    	}
    	
//    	if(true) return;
    	
    	Cipher C = null;
    	
    	try {
			C = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			
			AlgorithmParameters AP = AlgorithmParameters.getInstance("AES");
			javax.crypto.KeyGenerator KG = javax.crypto.KeyGenerator.getInstance("AES");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	if(true) return;
    	
    	MyClassloader Loader;
    	ClassWrapper Clazz = null;
    	try {
    		Loader = new JarClassLoader("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Justin's\\Programming\\Java\\Not My Projects\\bouncycastle\\146\\bcprov-ext-jdk16-146.jar");
    		
    		Loader.addCloseShutdownHook();
    		
    		Clazz = new ClassWrapper(Loader, "org.bouncycastle.jce.provider.BouncyCastleProvider");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		if(Clazz != null) {
			Security.addProvider((Provider)Clazz.newInstance());
			System.out.println("Provider Added");
		}
    	
		final Provider[] PS = Security.getProviders();
	
		for(final Provider P : PS) {
			System.out.println(P.getName());
		}
		
//    	final Provider P = Security.getProvider("SunJCE");
//    	
//    	System.out.println(P.getName());
//    	Set<Service> S = P.getServices();
//    	
//    	Iterator<Service> I = S.iterator();
//    	
//    	while(I.hasNext()) {
//    		final Service SI = I.next();
//    		
//    		System.out.println(SI.toString());
//    	}    	
//    	
//		for(final String NAME : getCipherAlgorithms()) {
//			System.out.println(NAME);
//		}
//		
//		try {
//			Cipher C = Cipher.getInstance("");
//		} catch (Exception e) {}
	}
}
