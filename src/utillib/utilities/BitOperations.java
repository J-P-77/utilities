package utillib.utilities;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * April 07, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class BitOperations {
	public enum Byte_Ordering {
		LITTLE_ENDIAN,
		BIG_ENDIAN;
	};

	public static byte[] shortToBytes(short value) {
		return shortToBytes(value, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static byte[] shortToBytes(short value, Byte_Ordering byteordering) {
		checkNull(byteordering);

		final byte[] BYTES = new byte[2];

		shortToBytes(value, BYTES, /*0, 2,*/byteordering);

		return BYTES;
	}

	public static void shortToBytes(short value, byte[] bytes) {
		shortToBytes(value, bytes, 0, /*length,/*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void shortToBytes(short value, byte[] bytes, Byte_Ordering bitordering) {
		shortToBytes(value, bytes, 0, /*2,*/bitordering);
	}

	public static void shortToBytes(short value, byte[] bytes, int offset/*, int length*/) {
		shortToBytes(value, bytes, offset, /*length,/*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void shortToBytes(short value, byte[] bytes, int offset, /*int length,*/Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, 2);
//    	checkLength(length, 2);

		int Shift = 8;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/2; X++, Y++) {
				bytes[offset + X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		} else {
			for(byte X = 1, Y = 0; Shift >= 0 && Y < /*length*/2; X--, Y++) {
				bytes[offset + X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		}
	}

	public static short[] shortToUBytes(short value) {
		return shortToUBytes(value, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static short[] shortToUBytes(short value, Byte_Ordering byteordering) {
		checkNull(byteordering);

		final short[] BYTES = new short[2];

		shortToUBytes(value, BYTES, 0, 2, byteordering);

		return BYTES;
	}

	public static void shortToUBytes(short value, short[] bytes, Byte_Ordering bitordering) {
		shortToUBytes(value, bytes, 0, 2, bitordering);
	}

	public static void shortTouBytes(short value, short[] bytes, int offset, int length) {
		shortToUBytes(value, bytes, offset, length, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void shortToUBytes(short value, short[] bytes, int offset, int length, Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, length);
		checkLength(length, 2);

		int Shift = 8;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < length; X++, Y++) {
				bytes[offset + X] = (short)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		} else {
			for(byte X = 1, Y = 0; Shift >= 0 && Y < length; X--, Y++) {
				bytes[offset + X] = (short)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		}
	}

	public static short bytesToShort(byte[] bytes) {
		return bytesToShort(bytes, 0, /*2,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static short bytesToShort(byte[] bytes, Byte_Ordering byteordering) {
		return bytesToShort(bytes, 0, /*2,*/byteordering);
	}

	public static short bytesToShort(byte[] bytes, int offset/*, int length*/) {
		return bytesToShort(bytes, offset, /*length,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static short bytesToShort(byte[] bytes, int offset, /*int length,*/Byte_Ordering bitbyteorderingordering) {
		checkNull(bytes);
		checkNull(bitbyteorderingordering);
		checkBufferBounds(bytes.length, offset, 2);
//    	checkLength(length, 2);

		int Shift = 8;
		short Result = 0;
		if(bitbyteorderingordering == Byte_Ordering.BIG_ENDIAN) {
//            for(byte X = 0; X < 2; X++) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/2; X++, Y++) {
				Result |= (short)(bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		} else {
//            for(byte X = 1; X >= 0; X--) {
			for(byte X = 1, Y = 0; Shift >= 0 && Y < /*length*/2; X--, Y++) {
				Result |= (short)(bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		}

		return Result;
	}

	public static byte[] intToBytes(int value) {
		return intToBytes(value, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static byte[] intToBytes(int value, Byte_Ordering byteordering) {
		checkNull(byteordering);

		final byte[] BYTES = new byte[4];

		intToBytes(value, BYTES, 0, /*4,*/byteordering);

		return BYTES;
	}

	public static short[] intToUBytes(int value) {
		return intToUBytes(value, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static short[] intToUBytes(int value, Byte_Ordering byteordering) {
		checkNull(byteordering);

		final short[] BYTES = new short[4];

		intToUBytes(value, BYTES, 0, 4, byteordering);

		return BYTES;
	}

	public static void intToUBytes(int value, short[] bytes, Byte_Ordering byteordering) {
		intToUBytes(value, bytes, 0, 4, byteordering);
	}

	public static void intToUBytes(int value, short[] bytes, int offset, int length) {
		intToUBytes(value, bytes, offset, length, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void intToUBytes(int value, short[] bytes, int offset, int length, Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, length);
		checkLength(length, 4);

		int Shift = 24;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {//low to high
			for(byte X = 0, Y = 0; Shift >= 0 && Y < length; X++, Y++) {
				bytes[offset + X] = (short)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		} else {
			for(byte X = 3, Y = 0; Shift >= 0 && Y < length; X--, Y++) {//high to low
				bytes[offset + X] = (short)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		}
	}

	public static void intToBytes(int value, byte[] bytes/*, Byte_Ordering byteordering*/) {
		intToBytes(value, bytes, 0/*, 4, byteordering*/);
	}

	public static void intToBytes(int value, byte[] bytes, int offset/*, int length*/) {
		intToBytes(value, bytes, offset, /*length,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void intToBytes(int value, byte[] bytes, Byte_Ordering byteordering) {
		intToBytes(value, bytes, 0, /*length,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void intToBytes(int value, byte[] bytes, int offset, /*int length,*/Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, 4);
//    	checkLength(length, 4);

		int Shift = 24;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {//low to high
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/4; X++, Y++) {
				bytes[offset + X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		} else {
			for(byte X = 3, Y = 0; Shift >= 0 && Y < /*length*/4; X--, Y++) {//high to low
				bytes[offset + X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		}
	}

	public static int bytesToInt(byte[] bytes) {
		return bytesToInt(bytes, 0, /*4,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static int bytesToInt(byte[] bytes, Byte_Ordering byteordering) {
		return bytesToInt(bytes, 0, /*4,*/byteordering);
	}

	public static int bytesToInt(byte[] bytes, int offset) {
		return bytesToInt(bytes, offset, /*4,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static int bytesToInt(byte[] bytes, int offset, /*int length,*/Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, 4);
//		checkLength(length, 2);

		int Shift = 24;
		int Result = 0;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/4; X++, Y++) {
				Result |= (bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		} else {
			for(byte X = 3, Y = 0; Shift >= 0 && Y < /*length*/4; X--, Y++) {
				Result |= (bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		}

		return Result;
	}

	public static byte[] longToBytes(long value) {
		return longToBytes(value, Byte_Ordering.LITTLE_ENDIAN);
	}

	public static byte[] longToBytes(long value, Byte_Ordering byteordering) {
		checkNull(byteordering);

		final byte[] BYTES = new byte[8];

		longToBytes(value, BYTES, 0, /*8,*/byteordering);

		return BYTES;
	}

	public static void longToBytes(long value, byte[] bytes/*, Byte_Ordering byteordering*/) {
		longToBytes(value, bytes, 0/*, 8, byteordering*/);
	}

	public static void longToBytes(long value, byte[] bytes, int offset/*, int length*/) {
		longToBytes(value, bytes, offset, /*length,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void longToBytes(long value, byte[] bytes, Byte_Ordering byteordering) {
		longToBytes(value, bytes, 0, /*length,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static void longToBytes(long value, byte[] bytes, int offset, /*int length,*/Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, 8);
//		checkLength(length, 8);

		int Shift = 56;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/8; X++, Y++) {
				bytes[X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		} else {
			for(byte X = 7, Y = 0; Shift >= 0 && Y < /*length*/8; X--, Y++) {
				bytes[X] = (byte)((value >> Shift) & 0xff);
				Shift -= 8;
			}
		}
	}

	public static long bytesToLong(byte[] bytes) {
		return bytesToLong(bytes, 0, /*8,*/Byte_Ordering.LITTLE_ENDIAN);
	}

	public static long bytesToLong(byte[] bytes, Byte_Ordering byteordering) {
		return bytesToLong(bytes, 0, /*8,*/byteordering);
	}

	public static long bytesToLong(byte[] bytes, int offset, /*int length,*/Byte_Ordering byteordering) {
		checkNull(bytes);
		checkNull(byteordering);
		checkBufferBounds(bytes.length, offset, 8);
//    	checkLength(length, 8);

		int Shift = 56;
		long Result = 0;
		if(byteordering == Byte_Ordering.BIG_ENDIAN) {
			for(byte X = 0, Y = 0; Shift >= 0 && Y < /*length*/8; X++, Y++) {
				Result |= (long)(bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		} else {
			for(byte X = 7, Y = 0; Shift >= 0 && Y < /*length*/8; X--, Y++) {
				Result |= (long)(bytes[offset + X] & 0xff) << Shift;
				Shift -= 8;
			}
		}

		return Result;
	}

	public static short getUnsignedByte(byte value) {
		return (short)(value < 0 ? (((value & 0x7F)) | 0x80) : value);
	}

	public static void checkNull(byte[] bytes) {
		if(bytes == null) {
			throw new RuntimeException("Variable[bytes] - Is Null");
		}
	}

	public static void checkNull(short[] bytes) {
		if(bytes == null) {
			throw new RuntimeException("Variable[bytes] - Is Null");
		}
	}

	public static void checkNull(Byte_Ordering byteordering) {
		if(byteordering == null) {
			throw new RuntimeException("Variable[byteordering] - Is Null");
		}
	}

	public static void checkLength(int value, int length) {
		if(value > length) {
			throw new RuntimeException("Variable[length] - Max Length Can Only Be " + length);
		}
	}

	public static void checkBufferBounds(int bufferlength, int offset, int length) {
		if(offset < 0 || offset >= bufferlength || length < 0 || length > bufferlength || (offset + length) > bufferlength) {

			throw new IndexOutOfBoundsException("bufferlength: " + bufferlength + " offset: " + offset + " length: " + length);
		}
	}
	/*
	public static void main(String[] args) {
		final byte[] BUFFER = new byte[4];
		
		BitOperations.intToBytes(1025, BUFFER, BitOperations.Byte_Ordering.BIG_ENDIAN);
		
		for(int X = 0; X < BUFFER.length; X++) {
			System.out.println(X + " " +Integer.toBinaryString(BUFFER[X]));
		}
		
	}*/
/*
    public static void main(String[] args) {
        //Short To Bytes
        final short SHORT_VALUE_1 = 50;
        final short SHORT_VALUE_2 = 1504;

        if(Arrays.equals(Convert2.shortToBytes(SHORT_VALUE_1, Convert2.Byte_Ordering.BIG_ENDIAN), shortToBytes(SHORT_VALUE_1, _BIG_ENDIAN_))) {
            System.out.println("shortToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("shortToBytes _BIG_ENDIAN_ BAD");
        }

        if(Arrays.equals(Convert2.shortToBytes(SHORT_VALUE_2, Convert2.Byte_Ordering.LITTLE_ENDIAN), shortToBytes(SHORT_VALUE_2, _LITTLE_ENDIAN_))) {
            System.out.println("shortToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("shortToBytes _LITTLE_ENDIAN_ BAD");
        }

        //Short To Bytes With Buffer
        final byte[] SHORT_BUFFER_1 = new byte[2];
        final byte[] SHORT_BUFFER_2 = new byte[2];

        Convert2.shortToBytes(SHORT_VALUE_1, SHORT_BUFFER_1, Convert2.Byte_Ordering.BIG_ENDIAN);
        shortToBytes(SHORT_VALUE_1, SHORT_BUFFER_2, _BIG_ENDIAN_);

        if(Arrays.equals(SHORT_BUFFER_1, SHORT_BUFFER_2)) {
            System.out.println("shortToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("shortToBytes _BIG_ENDIAN_ BAD");
        }

        Arrays.fill(SHORT_BUFFER_1, (byte)0);
        Arrays.fill(SHORT_BUFFER_2, (byte)0);

        Convert2.shortToBytes(SHORT_VALUE_2, SHORT_BUFFER_1, Convert2.Byte_Ordering.LITTLE_ENDIAN);
        shortToBytes(SHORT_VALUE_2, SHORT_BUFFER_2, _LITTLE_ENDIAN_);

        if(Arrays.equals(SHORT_BUFFER_1, SHORT_BUFFER_2)) {
            System.out.println("shortToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("shortToBytes _LITTLE_ENDIAN_ BAD");
        }

        //Buffer To Short
        final byte[] SHORT_BUFFER = {3, 2};

        if(Convert2.bytesToShort(SHORT_BUFFER, Convert2.Byte_Ordering.BIG_ENDIAN) == bytesToShort(SHORT_BUFFER, _BIG_ENDIAN_)) {
            System.out.println("bytesToShort _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToShort _BIG_ENDIAN_ BAD");
        }

        if(Convert2.bytesToShort(SHORT_BUFFER, Convert2.Byte_Ordering.LITTLE_ENDIAN) == bytesToShort(SHORT_BUFFER, _LITTLE_ENDIAN_)) {
            System.out.println("bytesToShort _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToShort _LITTLE_ENDIAN_ BAD");
        }

        //Integer To Bytes
        final int INT_VALUE_1 = 50;
        final int INT_VALUE_2 = 1504;

        if(Arrays.equals(Convert2.intToBytes(INT_VALUE_1, Convert2.Byte_Ordering.BIG_ENDIAN), intToBytes(INT_VALUE_1, _BIG_ENDIAN_))) {
            System.out.println("intToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("intToBytes _BIG_ENDIAN_ BAD");
        }
        
        if(Arrays.equals(Convert2.intToBytes(INT_VALUE_2, Convert2.Byte_Ordering.LITTLE_ENDIAN), intToBytes(INT_VALUE_2, _LITTLE_ENDIAN_))) {
            System.out.println("intToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("intToBytes _LITTLE_ENDIAN_ BAD");
        }

        //Integer To Bytes With Buffer
        final byte[] INT_BUFFER_1 = new byte[4];
        final byte[] INT_BUFFER_2 = new byte[4];
        
        Convert2.intToBytes(INT_VALUE_1, INT_BUFFER_1, Convert2.Byte_Ordering.BIG_ENDIAN);
        intToBytes(INT_VALUE_1, INT_BUFFER_2, _BIG_ENDIAN_);

        if(Arrays.equals(INT_BUFFER_1, INT_BUFFER_2)) {
            System.out.println("intToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("intToBytes _BIG_ENDIAN_ BAD");
        }

        Arrays.fill(INT_BUFFER_1, (byte)0);
        Arrays.fill(INT_BUFFER_2, (byte)0);

        Convert2.intToBytes(INT_VALUE_2, INT_BUFFER_1, Convert2.Byte_Ordering.LITTLE_ENDIAN);
        intToBytes(INT_VALUE_2, INT_BUFFER_2, _LITTLE_ENDIAN_);

        if(Arrays.equals(INT_BUFFER_1, INT_BUFFER_2)) {
            System.out.println("intToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("intToBytes _LITTLE_ENDIAN_ BAD");
        }

        final byte[] INT_BUFFER = {3, 2, 1, 0};

        if(Convert2.bytesToInt(INT_BUFFER, Convert2.Byte_Ordering.BIG_ENDIAN) == bytesToInt(INT_BUFFER, _BIG_ENDIAN_)) {
            System.out.println("bytesToInt _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToInt _BIG_ENDIAN_ BAD");
        }

        if(Convert2.bytesToInt(INT_BUFFER, Convert2.Byte_Ordering.LITTLE_ENDIAN) == bytesToInt(INT_BUFFER, _LITTLE_ENDIAN_)) {
            System.out.println("bytesToInt _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToInt _LITTLE_ENDIAN_ BAD");
        }

//        final byte[] SHORT_BUFFER_LEN = {3, 2, 5, 5, 7};
//        if(Convert2.bytesToInt(new byte[] {0, 2, 5, 5}, Convert2.Byte_Ordering.BIG_ENDIAN) == bytesToInt(SHORT_BUFFER_LEN, 1, 3, _BIG_ENDIAN_)) {
//            System.out.println("bytesToInt length _BIG_ENDIAN_ GOOD");
//        } else {
//            System.out.println("bytesToInt length _BIG_ENDIAN_ BAD");
//        }
//
//        if(Convert2.bytesToInt(new byte[] {5, 5, 7, 0}, Convert2.Byte_Ordering.LITTLE_ENDIAN) == bytesToInt(SHORT_BUFFER_LEN, 2, 3, _LITTLE_ENDIAN_)) {
//            System.out.println("bytesToInt length _LITTLE_ENDIAN_ GOOD");
//        } else {
//            System.out.println("bytesToInt length _LITTLE_ENDIAN_ BAD");
//        }

        //Long To Bytes
        final int LONG_VALUE_1 = 50;
        final int LONG_VALUE_2 = 1504;

        if(Arrays.equals(Convert2.longToBytes(LONG_VALUE_1, Convert2.Byte_Ordering.BIG_ENDIAN), longToBytes(LONG_VALUE_1, _BIG_ENDIAN_))) {
            System.out.println("longToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("longToBytes _BIG_ENDIAN_ BAD");
        }

        if(Arrays.equals(Convert2.longToBytes(LONG_VALUE_2, Convert2.Byte_Ordering.LITTLE_ENDIAN), longToBytes(LONG_VALUE_2, _LITTLE_ENDIAN_))) {
            System.out.println("longToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("longToBytes _LITTLE_ENDIAN_ BAD");
        }

        //Long To Bytes With Buffer
        final byte[] LONG_BUFFER_1 = new byte[8];
        final byte[] LONG_BUFFER_2 = new byte[8];

        Convert2.longToBytes(LONG_VALUE_1, LONG_BUFFER_1, Convert2.Byte_Ordering.BIG_ENDIAN);
        longToBytes(LONG_VALUE_1, LONG_BUFFER_2, _BIG_ENDIAN_);

        if(Arrays.equals(LONG_BUFFER_1, LONG_BUFFER_2)) {
            System.out.println("longToBytes _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("longToBytes _BIG_ENDIAN_ BAD");
        }

        Arrays.fill(LONG_BUFFER_1, (byte)0);
        Arrays.fill(LONG_BUFFER_2, (byte)0);

        Convert2.longToBytes(LONG_VALUE_2, LONG_BUFFER_1, Convert2.Byte_Ordering.LITTLE_ENDIAN);
        longToBytes(LONG_VALUE_2, LONG_BUFFER_2, _LITTLE_ENDIAN_);

        if(Arrays.equals(LONG_BUFFER_1, LONG_BUFFER_2)) {
            System.out.println("longToBytes _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("longToBytes _LITTLE_ENDIAN_ BAD");
        }

        final byte[] LONG_BUFFER = {3, 2, 1, 0, 5, 5, 5, 4};

        if(Convert2.bytesToLong(LONG_BUFFER, Convert2.Byte_Ordering.BIG_ENDIAN) == bytesToLong(LONG_BUFFER, _BIG_ENDIAN_)) {
            System.out.println("bytesToLong _BIG_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToLong _BIG_ENDIAN_ BAD");
        }

        if(Convert2.bytesToLong(LONG_BUFFER, Convert2.Byte_Ordering.LITTLE_ENDIAN) == bytesToLong(LONG_BUFFER, _LITTLE_ENDIAN_)) {
            System.out.println("bytesToLong _LITTLE_ENDIAN_ GOOD");
        } else {
            System.out.println("bytesToLong _LITTLE_ENDIAN_ BAD");
        }
    }
*/
}
