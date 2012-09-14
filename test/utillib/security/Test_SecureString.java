package utillib.security;

public class Test_SecureString {
	public static void main(String[] args) {
		final SecureString STR = new SecureString("Justin");

		System.out.println(STR.get());
		STR.erase();
		System.out.println(STR.get());
	}
}
