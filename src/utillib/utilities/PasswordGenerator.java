package utillib.utilities;

import static utillib.utilities.RandomUtil.*;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class PasswordGenerator {	
	private static final char[] SPECIAL_PUNCTUATION = {',','.','!',':',';','!','?'};
	private static final char[] SPECIAL_BRACKETS = {'(',')','[',']','{','}','<','>'};
	private static final char[] NUMBER = {'0','1','2','3','4','5','6','7','8','9'};
	private static final char[] LOWER_CASE = {'a','b','c','d','e','f','g','h','i','j','k',
		'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static final char[] UPPER_CASE = {'A','B','C','D','E','F','G','H','I','J','K',
		'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	private StringBuffer _Buffer;

	private int _PasswordLength;

	private boolean _Include_Lower;
	private boolean _Include_Upper;
	private boolean _Include_Number;
	private boolean _Include_Punctuation;
	private boolean _Include_Brackets;

	public PasswordGenerator() {
		this(16);
	}
	
	public PasswordGenerator(int passwordlength) {
		setPassordLength(passwordlength);
	}
	
	public void setPassordLength(int value) {
		if(value < 0) {
			throw new RuntimeException("Must Be A Positive Number");
		} else {
			_PasswordLength = value;

			if(_Buffer != null) {
				erasePassword();
			}

			_Buffer = new StringBuffer(_PasswordLength);
		}
	}
	
	public int getPassordLength() {
		return _PasswordLength;
	}
	
	public void setIncludeNumber(boolean value) {
		_Include_Number = value;
	}
	
	public boolean getIncludeNumber() {
		return _Include_Number;
	}
	
	public void setIncludeUpper(boolean value) {
		_Include_Upper = value;
	}
	
	public boolean getIncludeUpper() {
		return _Include_Upper;
	}
	
	public void setIncludeLower(boolean value) {
		_Include_Lower = value;
	}
	
	public boolean getIncludeLower() {
		return _Include_Lower;
	}
	
	public void setIncludeBrackets(boolean value) {
		_Include_Brackets = value;
	}
	
	public boolean getIncludeBrackets() {
		return _Include_Brackets;
	}
	
	public void setIncludePunctuation(boolean value) {
		_Include_Punctuation = value;
	}
	
	public boolean getIncludePunctuation() {
		return _Include_Punctuation;
	}
			
	private char add(StringBuffer buffer, char pattern) {
		switch(pattern) {
			case 'l':
				buffer.append(randLowerCase());
				return pattern;
			case 'u':
				buffer.append(randUpperCase());
				return pattern;
			case 'd':
				buffer.append(randNumber());
				return pattern;
			case 'b':
				buffer.append(randSpecialBarackets());
				return pattern;
			case 'p':
				buffer.append(randSpecialPunctuation());
				return pattern;
		}
		return '\0';//INVALID CHARACTER
	}
			
	public String generate() {	
		char[] CharPool = charaterPool();
		
		for(int X = 0; X < _PasswordLength; X++) {
			_Buffer.append(CharPool[secureRandomInt(0, CharPool.length - 1)]);
		}
		
		return _Buffer.toString();
	}
	
	public void generate(String pattern) {
		generate(pattern.toCharArray());
	}	
	
	public void generate(char[] pattern) {
		StringBuffer Buffer = new StringBuffer(pattern.length);
		
		char Cur = '\0';
		
		for(int X = 0; X < pattern.length; X++) {
			switch(pattern[X]) {
				case 'b':
				case 'p':
				case 'l':
				case 'u':
				case 'd':
					Cur = add(Buffer,pattern[X]);
					break;
					
				case '{':
					String StrNumber = "";
					X++;
					while(pattern[X] != '}') {
						StrNumber += pattern[X];
						X++;
						
						if(X >= pattern.length) {
							throw new RuntimeException("Missing Character: }");
						}
					}
					
					int Number = getNumber(StrNumber) - 1;
					
					for(int Y = 0; Y < Number; Y++) {
						add(Buffer,Cur);
					}
					
					break;
					
				case '\0':
				default:
					throw new RuntimeException("Illegal Character: " + pattern[X] + " in Pattern");
			}
		}
	}
	
	public String getPassword() {
		return _Buffer.toString();
	}
	
	public void erasePassword() {
		if(_Buffer != null) {
			for(int X = 0; X < _Buffer.length(); X++) {
				_Buffer.setCharAt(X, '\0');
			}
			_Buffer = null;
		}
	}
	
	private char[] charaterPool() {
		StringBuffer Buffer = new StringBuffer();
		
		if(_Include_Lower) {
			Buffer.append(LOWER_CASE);
		}
		
		if(_Include_Upper) {
			Buffer.append(UPPER_CASE);
		}
		
		if(_Include_Number) {
			Buffer.append(NUMBER);
		}
		
		if(_Include_Punctuation) {
			Buffer.append(SPECIAL_PUNCTUATION);
		}
		
		if(_Include_Brackets) {
			Buffer.append(SPECIAL_BRACKETS);
		}
		
		return Buffer.toString().toCharArray();
	}
	
	private static boolean isNumber(String str) {
		for(int X = 0; X < str.length(); X++) {
			if(!Character.isDigit(str.charAt(X))) {
				return false;
			}
		}
		
		return true;
	}
	
	private static int getNumber(String str) {
		if(isNumber(str)) {
			return Integer.parseInt(str);
		} else {
			throw new RuntimeException("Not A Number: " +  str);
		}
	}
		
	private static char randUpperCase() {
		return UPPER_CASE[randomInt(0,UPPER_CASE.length-1)];
	}
	
	private static char randLowerCase() {
		return LOWER_CASE[randomInt(0,LOWER_CASE.length-1)];
	}
	
	private static char randNumber() {
		return NUMBER[randomInt(0,NUMBER.length-1)];
	}
	
	private static char randSpecialPunctuation() {
		return SPECIAL_PUNCTUATION[randomInt(0,SPECIAL_PUNCTUATION.length-1)];
	}
	
	private static char randSpecialBarackets() {
		return SPECIAL_BRACKETS[randomInt(0,SPECIAL_BRACKETS.length-1)];
	}
	
	@Override
	protected void finalize() throws Throwable {
        erasePassword();

		super.finalize();
	}
/*
	public static void main(String[] args) {
		PasswordGenerator Pass = new PasswordGenerator();
		
		Pass.setIncludeUpper(true);
		Pass.setIncludeNumber(true);
		Pass.setIncludeLower(true);
		
		Pass.setIncludePunctuation(true);
		Pass.setIncludeBrackets(true);
		
		Pass.setPassordLength(20);
//		for(int X = 0; X < 100; X++) {
//			Pass.generate();
//			if(X < 10) {
//				System.out.println("Password 0" + X + ": " + Pass.getPassword());
//			} else {
//				System.out.println("Password " + X + ": " + Pass.getPassword());
//			}
//		}
		
		Pass.generate();
		System.out.println("Generate Password: " + Pass.getPassword());
		
		Pass.setPassordLength(21);
		//Pass.reGenerate();
		Pass.generate();
		System.out.println("Generate Password: " + Pass.getPassword());
		
		//Pass.generate("bd{2}u{2}l{2}b");
		//System.out.println("Generate Pattern Password:   " + Pass.getPassword());
	}
*/
}

/*
	Placeholder 	Type						Character Set
	-----------		------------------------	----------------------------------------
	a				Lower-Case Alphanumeric 	abcdefghijklmnopqrstuvwxyz 0123456789
	A				Mixed-Case Alphanumeric 	ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789
	U				Upper-Case Alphanumeric 	ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789
	c				Lower-Case Consonants		bcdfghjklmnpqrstvwxyz
	C				Mixed-Case Consonants		BCDFGHJKLMNPQRSTVWXYZ bcdfghjklmnpqrstvwxyz
	z				Upper-Case Consonants		BCDFGHJKLMNPQRSTVWXYZ
	d				Digit						0123456789
	h				Lower-Case Hex Character 	0123456789 abcdef
	H				Upper-Case Hex Character 	0123456789 ABCDEF
	l				Lower-Case Letters			abcdefghijklmnopqrstuvwxyz
	L				Mixed-Case Letters			ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz
	u				Upper-Case Letters			ABCDEFGHIJKLMNOPQRSTUVWXYZ
	p				Punctuation					,.;:
	b				Brackets					()[]{}<>
	S				Printable 7-Bit ASCII		A-Z, a-z, 0-9, !"#$%&'()*+,-./:;<=>?[\]^_{|}~
	v				Lower-Case Vowels			aeiou
	V				Mixed-Case Vowels			AEIOU aeiou
	Z				Upper-Case Vowels			AEIOU
	x				High ANSI					From '~' to U255 (excluding U255)
	\				Escape (Fixed Char)			Use following character as is
	{n}				Escape (Repeat)				Repeat the previous character n times
	[...]			Custom Char Set				Define a custom character set
*/
