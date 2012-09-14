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
public class SecureIntArray {
	protected int[] _Password = null;

	public SecureIntArray() {}

	public SecureIntArray(StringBuffer password) {
		setPassword(password);
	}

	public SecureIntArray(String password) {
		setPassword(password);
	}

	public SecureIntArray(char[] password) {
		setPassword(password);
	}

	public SecureIntArray(byte[] password) {
		setPassword(password);
	}

	public SecureIntArray(int[] password) {
		setPassword(password);
	}

	public void setPassword(StringBuffer password) {
		clearPassword();

		_Password = new int[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = (int)password.charAt(X);
		}
	}

	public void setPassword(String password) {
		clearPassword();

		_Password = new int[password.length()];

		for(int X = 0; X < password.length(); X++) {
			_Password[X] = (int)password.charAt(X);
		}
	}

	public void setPassword(char[] password) {
		clearPassword();

		_Password = new int[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (int)password[X];
		}
	}

	public void setPassword(byte[] password) {
		clearPassword();

		_Password = new int[password.length];

		for(int X = 0; X < password.length; X++) {
			_Password[X] = (int)password[X];
		}
	}

	public void setPassword(int[] password) {
		clearPassword();

		_Password = password;
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
					_Password[X] = (int)((Byte.MAX_VALUE - Byte.MIN_VALUE + 1) * Math.random() + Byte.MIN_VALUE);
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
