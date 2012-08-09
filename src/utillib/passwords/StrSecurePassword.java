package utillib.passwords;

/**
 * January 29, 2009 (Version 1.0.0)<br>
 *     -First Released<br>
 * <br>
 * @author Justin Palinkas<br>
 * <br>
 * Current Version 1.0.0
 */
public class StrSecurePassword {
	public StringBuffer _Password = null;
	
    public StrSecurePassword() {}
    
    public StrSecurePassword(StringBuffer password) {
        setPassword(password);
    }
    
    public StrSecurePassword(String password) {
        setPassword(password);
    }
    
    public StrSecurePassword(char[] password) {
        setPassword(password);
    }
    
    public StrSecurePassword(byte[] password) {
        setPassword(password);
    }
    
    public StrSecurePassword(int[] password) {
        setPassword(password);
    }
    
    public void setPassword(StringBuffer password) {
        clearPassword();
        
        _Password = password;
    }
    
    public void setPassword(String password) {
        clearPassword();
        
        _Password = new StringBuffer(password);
    }
    
    public void setPassword(char[] password) {
        clearPassword();
        
        _Password = new StringBuffer(password.length);
        
        for(int X = 0; X < password.length; X++) {
            _Password.append(password[X]);
        }
    }
    
    public void setPassword(byte[] password) {
        clearPassword();
        
        _Password = new StringBuffer(password.length);
        
        for(int X = 0; X < password.length; X++) {
            _Password.append((char)password[X]);
        }
    }
    
    public void setPassword(int[] password) {
        clearPassword();
        
        _Password = new StringBuffer(password.length);
        
        for(int X = 0; X < password.length; X++) {
            _Password.append((char)password[X]);
        }
    }
    
    //You Are Must Delete Bytes Array
    public byte[] getBytes() {
        byte[] Bytes = new byte[_Password.length()];
        
        for(int X = 0; X < _Password.length(); X++) {
            Bytes[X] = (byte)_Password.charAt(X);
        }
        
        return Bytes;
    }
    
    //You Are Must Delete Integer Array
    public int[] getInts() {
        int[] Ints = new int[_Password.length()];
        
        for(int X = 0; X < _Password.length(); X++) {
            Ints[X] = (int)_Password.charAt(X);
        }
        
        return Ints;  
    }
    
    //You Are Must Delete Character Array
    public char[] getChars() {
        char[] Chars = new char[_Password.length()];
        
        for(int X = 0; X < _Password.length(); X++) {
            Chars[X] = _Password.charAt(X);
        }
        
        return Chars;
    }
    
    //Does Not Erase Well Use With Caution
    public String getStr() {
        return _Password.toString();
    }
    
    //Do Not Erase StringBuffer Because It Will Erase Password From Class
    public StringBuffer getStrBuffer() {
        return _Password;
    }
    
    public static void erase(byte[] bytes) {
        for(int X = 0; X < bytes.length; X++) {
            bytes[X] = (byte)((Byte.MAX_VALUE - Byte.MIN_VALUE + 1) * Math.random() + Byte.MIN_VALUE);
            bytes[X] = 0;
        }
    }
    
    public static void erase(int[] ints) {
        for(int X = 0; X < ints.length; X++) {
            ints[X] = (int)((Integer.MAX_VALUE - Integer.MIN_VALUE + 1) * Math.random() + Integer.MIN_VALUE);
            ints[X] = 0;
        }
    }
    
    public static void erase(char[] chars) {
        for(int X = 0; X < chars.length; X++) {
            chars[X] = (char)((Integer.MAX_VALUE - Integer.MIN_VALUE + 1) * Math.random() + Integer.MIN_VALUE);
            chars[X] = '\0';
        }
    }
    
	public void clearPassword() {     
        if(_Password != null) {
            //Clear's Password Buffer
            randomBuffer();
            eraseBuffer();
            _Password = null;
        }
	}
	
    public boolean equals(String password) {
        if(_Password.length() != password.length()) {
            return false;
        }
        
        for(int X = 0; X < _Password.length(); X++) {
            if(_Password.charAt(X) != password.charAt(X)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean equalsIgnoreCase(String password) {
        if(_Password.length() != password.length()) {
            return false;
        }
        
        char C1 = '\0';
        char C2 = '\0';
        for(int X = 0; X < _Password.length(); X++) {
            C1 = Character.toLowerCase(_Password.charAt(X));
            C2 = Character.toLowerCase(password.charAt(X));
            
            if(C1 != C2) {
                return false;
            }
        }

        return true;
    }
    
	private void randomBuffer() {
		if(_Password != null) {
			for(int X = 0; X < _Password.length(); X++) {
				try {
					_Password.setCharAt(X, (char)((Integer.MAX_VALUE - Integer.MIN_VALUE + 1) * Math.random() + Integer.MIN_VALUE));
				} catch (Exception e) {
					_Password.setCharAt(X, (char)77);
				}
			}
		}
	}
	
	private void eraseBuffer() {
		if(_Password != null) {
			for(int X = 0; X < _Password.length(); X++) {
				_Password.setCharAt(X, '\0');
			}
            
            _Password.delete(0, _Password.length());
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
        clearPassword();

		super.finalize();
	}
}
