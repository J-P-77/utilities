package utillib.security;

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
public class SecureCharArray {
	protected char[] _Password = null;

	public SecureCharArray() {}

	public SecureCharArray(StringBuffer password) {
		setPassword(password);
	}

	public SecureCharArray(String password) {
		setPassword(password);
	}

	public SecureCharArray(char[] password) {
		setPassword(password);
	}

	public SecureCharArray(byte[] password) {
		setPassword(password);
	}

	public SecureCharArray(int[] password) {
		setPassword(password);
	}

	public void setPassword(StringBuffer password) {
		clearPassword();

		_Password = new char[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = password.charAt(X);
		}
	}

	public void setPassword(String password) {
		clearPassword();

		_Password = new char[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = password.charAt(X);
		}
	}

	public void setPassword(char[] password) {
		clearPassword();

		_Password = password;
	}

	public void setPassword(byte[] password) {
		clearPassword();

		_Password = new char[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (char)password[X];
		}
	}

	public void setPassword(int[] password) {
		clearPassword();

		_Password = new char[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (char)password[X];
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
					_Password[X] = (char)((Integer.MAX_VALUE - Integer.MIN_VALUE + 1) * Math.random() + Integer.MIN_VALUE);
				} catch(Exception e) {
					_Password[X] = (char)77;
				}
			}
		}
	}

	private void eraseBuffer() {
		if(_Password != null) {
			for(int X = 0; X < _Password.length; X++) {
				_Password[X] = '\0';
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		clearPassword();

		super.finalize();
	}
}
