package utillib.security;

import java.lang.reflect.Field;

public class SecureString {
	private String _Str = null;

	public SecureString(String str) {
		_Str = str;
	}

	public String get() {
		return _Str;
	}

	public void erase() {
		try {
			final Field F_VALUE = String.class.getDeclaredField("value");
			F_VALUE.setAccessible(true);

			final char[] VALUE = (char[])F_VALUE.get(_Str);

			for(int X = 0; X < VALUE.length; X++) {
				VALUE[X] = '\0';
			}
		} catch(SecurityException e) {
			throw new RuntimeException("SecurityManager blocked erase");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return _Str;
	}

	@Override
	protected void finalize() throws Throwable {
		erase();
	}
}
