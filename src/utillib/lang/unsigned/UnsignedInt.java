package utillib.lang.unsigned;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 08, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class UnsignedInt {
	public static final long _MIN_VALUE_ = 0;
	public static final long _MAX_VALUE_ = 4294967295l;

	private long _Value = 0;

	public UnsignedInt(int value) {
		_Value = (long)((value >> 32) & 0xff);
	}

	public UnsignedInt(long value) {
		setValue(value);
	}

	public void setValue(long value) {
		if(value < 0 || value > _MAX_VALUE_) {
			throw new RuntimeException("Variable[value] - Must Be >= 0 and <= 4294967295");
		}

		_Value = value;
	}

	public long getValue() {
		return _Value;
	}

	public void increment() {
		_Value++;

		checkRange(_Value);
	}

	public void increment(long value) {
		checkRange(value);

		_Value += value;
	}

	public void decrement() {
		_Value--;

		checkRange(_Value);
	}

	public void decrement(long value) {
		checkRange(value);

		_Value -= value;
	}

	public static void checkRange(long value) {
		if(value < _MIN_VALUE_ || value > _MAX_VALUE_) {
			throw new RuntimeException("Variable[value] - Must Be >= 0 and <= " + _MAX_VALUE_);
		}
	}
}
/*
Limits on Integer Constants
Constant  Meaning  Value
CHAR_BIT
 Number of bits in the smallest variable that is not a bit field.
 8

SCHAR_MIN
 Minimum value for a variable of type signed char.
 –128

SCHAR_MAX
 Maximum value for a variable of type signed char.
 127

UCHAR_MAX
 Maximum value for a variable of type unsigned char.
 255 (0xff)

CHAR_MIN
 Minimum value for a variable of type char.
 –128; 0 if /J option used

CHAR_MAX
 Maximum value for a variable of type char.
 127; 255 if /J option used

MB_LEN_MAX
 Maximum number of bytes in a multicharacter constant.
 5

SHRT_MIN
 Minimum value for a variable of type short.
 –32768

SHRT_MAX
 Maximum value for a variable of type short.
 32767

USHRT_MAX
 Maximum value for a variable of type unsigned short.
 65535 (0xffff)

INT_MIN
 Minimum value for a variable of type int.
 –2147483647 – 1

INT_MAX
 Maximum value for a variable of type int.
 2147483647

UINT_MAX
 Maximum value for a variable of type unsigned int.
 4294967295 (0xffffffff)

LONG_MIN
 Minimum value for a variable of type long.
 –2147483647 – 1

LONG_MAX
 Maximum value for a variable of type long.
 2147483647

ULONG_MAX
 Maximum value for a variable of type unsigned long.
 4294967295 (0xffffffff)
 
 */
