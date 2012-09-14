package utillib.io;

import utillib.arrays.ArraysUtil;

import utillib.file.FileUtil;

import utillib.strings.MyStringBuffer;

import utillib.utilities.BitOperations;
import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.interfaces.IMyInputStream;

import utillib.lang.unsigned.UnsignedByte;
import utillib.lang.unsigned.UnsignedInt;
import utillib.lang.unsigned.UnsignedShort;

import java.io.IOException;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * October 14, 2009 (version 1.0.0)
 *     -First Released
 * 
 * January 10, 2010 (version 1.0.1)
 *     -Added
 *         -Bit Ordering
 * 
 * January 2, 2011 (version 1.1.0)
 *     -Fixed Bug
 *         -Buffer Offset And Length Handling In Some Methods
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public abstract class AMyInputStream extends AInputStream implements IMyInputStream {
	private boolean _PreviousCharCR = false;

	private Byte_Ordering _Default_Bit_Ordering;

	protected AMyInputStream() {
		this(Byte_Ordering.LITTLE_ENDIAN);
	}

	protected AMyInputStream(Byte_Ordering byteordering) {
		setByteOrdering(byteordering);
	}

	public void setByteOrdering(Byte_Ordering byteordering) {
		BitOperations.checkNull(byteordering);

		_Default_Bit_Ordering = byteordering;
	}

	public Byte_Ordering getByteOrdering() {
		return _Default_Bit_Ordering;
	}

	@Override
	public String readln() throws IOException {
		final MyStringBuffer LINE = new MyStringBuffer();

		if(readln(LINE)) {
			return LINE.toString();
		} else {
//			throw new IOException();
			return null;
		}
	}

	/**
	 * Dones Not Reset The Buffer Before Reading Characters
	 * 
	 * @param buffer
	 * @return true if a line has been read, false if not
	 * @throws IOException
	 */
	@Override
	public boolean readln(MyStringBuffer buffer) throws IOException {
		checkNull(buffer);

		if(!isClosed()) {
//        	try {
			int CurrentChar = -1;
			//r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
//                System.out.println("Bytes Available (" + this.available() + ")");

			while((CurrentChar = this.read()) != -1) {
				if(CurrentChar == FileUtil._LF_) {//Unix or Windows
					if(!_PreviousCharCR) {//Unix
						return true;
					} else {
						continue;//Windows (Skip Character[\n])
					}
				}

				if(CurrentChar == FileUtil._CR_) {//Mac
					_PreviousCharCR = true;
					return true;
				} else {
					_PreviousCharCR = false;
				}

				buffer.append((char)CurrentChar);//PreviousCharCR = false;

//                	System.out.println("Bytes Available (" + this.available() + ") " + line.toString());
			}

			return (buffer.length() > 0);
//			} catch (Exception e) {
//				System.out.println(e.getMessage() + " - " + (line == null ? "NULL" : line.toString()));
//			}
		}

		return false;
	}

	/**
	 * 
	 * @param line
	 * @return true if a line has been read, false if not
	 * @throws IOException
	 */
	@Override
	public boolean readln(StringBuffer buffer) throws IOException {
		checkNull(buffer);

		if(isOpen()) {
			int CurrentChar = -1;
			//r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
			while((CurrentChar = this.read()) != -1) {
				if(CurrentChar == FileUtil._LF_) {//Unix or Windows
					if(!_PreviousCharCR) {//Unix
						return true;
					} else {
						continue;//Windows (Skip Character[\n])
					}
				}

				if(CurrentChar == FileUtil._CR_) {//Mac
					_PreviousCharCR = true;
					return true;
				} else {
					_PreviousCharCR = false;
				}

				buffer.append((char)CurrentChar);//PreviousCharCR = false;
			}

			if(buffer.length() > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 0 = More Data 1 = End Of Line 2 = Error
	 * 
	 * @param buffer
	 * @param offset
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public int readln(char[] buffer, int offset, int length) throws IOException {
		checkNull(buffer);

		if(isOpen()) {
			int Counter = 0;
			int CurrentChar = -1;
			//r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
			while((CurrentChar = this.read()) != -1) {
				if(CurrentChar == FileUtil._LF_) {//Unix or Windows
					if(!_PreviousCharCR) {//Unix
						return 1;
					} else {
						continue;//Windows (Skip Character[\n])
					}
				}

				if(CurrentChar == FileUtil._CR_) {//Mac
					_PreviousCharCR = true;
					return 1;
				} else {
					_PreviousCharCR = false;
				}

				if(Counter < length) {
					buffer[offset + Counter] = (char)CurrentChar;
					Counter++;
				} else {
					break;
				}
			}

			return 0;
		}

		return 2;
	}

	@Override
	public String readString(int length) throws IOException {
		final MyStringBuffer BUFFER = new MyStringBuffer();

		readString(BUFFER, length);

		return BUFFER.toString();
	}

	@Override
	public int readString(StringBuffer buffer, int numtoread) throws IOException {
		checkNull(buffer);

		final byte[] BYTES = new byte[numtoread];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		for(int X = 0; X < LENGTH; X++) {
			buffer.append((char)BYTES[X]);
		}

		return LENGTH;
	}

	@Override
	public int readString(MyStringBuffer buffer, int numtoread) throws IOException {
		checkNull(buffer);

		final byte[] BYTES = new byte[numtoread];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		for(int X = 0; X < LENGTH; X++) {
			buffer.append((char)BYTES[X]);
		}

		return LENGTH;
	}

	@Override
	public int readBytes(byte[] buffer) throws IOException {
		return readBytes(buffer, 0, buffer.length);
	}

	@Override
	public int readBytes(byte[] buffer, int offset, int length) throws IOException {
		checkNull(buffer);
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		return super.read(buffer, offset, length);
	}

	@Override
	public int readBytes(char[] buffer) throws IOException {
		return readBytes(buffer, 0, buffer.length);
	}

	@Override
	public int readBytes(char[] buffer, int offset, int length) throws IOException {
		checkNull(buffer);
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		final byte[] BYTES = new byte[length];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		for(int X = 0; X < LENGTH; X++) {
			buffer[offset + X] = (char)BYTES[X];
		}

		return LENGTH;
	}

	@Override
	public int readBytes(int[] buffer) throws IOException {
		return readBytes(buffer, 0, buffer.length);
	}

	@Override
	public int readBytes(int[] buffer, int offset, int length) throws IOException {
		checkNull(buffer);
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		final byte[] BYTES = new byte[length];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		for(int X = 0; X < LENGTH; X++) {
			buffer[offset + X] = (int)BYTES[X];
		}

		return LENGTH;
	}

// TODO: (Fix Read) Character Is Unsigned 2 bytes (I Think), Warning This Might Break Things
	@Override
	public int readChars(char[] buffer) throws IOException {
		return readChars(buffer, 0, buffer.length);
	}

// TODO: (Fix Read) Character Is Unsigned 2 bytes (I Think), Warning This Might Break Things
	@Override
	public int readChars(char[] buffer, int offset, int length) throws IOException {
		return readBytes(buffer, offset, length);
	}

	@Override
	public int readInts(int[] buffer) throws IOException {
		return readInts(buffer, 0, buffer.length);
	}

	@Override
	public int readInts(int[] buffer, int offset, int length) throws IOException {
		return readBytes(buffer, offset, length);
	}

	public int readCharsW(char[] buffer) throws IOException {
		return readCharsW(buffer, 0, buffer.length, _Default_Bit_Ordering);
	}

	public int readCharsW(char[] buffer, Byte_Ordering byteordering) throws IOException {
		return readCharsW(buffer, 0, buffer.length, byteordering);
	}

	public int readCharsW(char[] buffer, int offset, int length) throws IOException {
		return readCharsW(buffer, offset, length, _Default_Bit_Ordering);
	}

	/**
	 * TODO: Needs Testing
	 */
	public int readCharsW(char[] buffer, int offset, int length, Byte_Ordering byteordering) throws IOException {
		checkNull(buffer);
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		final byte[] BYTES = new byte[length * 2];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		int Y = 0;
		for(int X = 0; X < LENGTH; X++, Y += 2) {
			buffer[offset + X] = (char)BitOperations.bytesToShort(BYTES, Y, byteordering);
		}

		return LENGTH;
	}

	public int readIntsW(int[] buffer) throws IOException {
		return readIntsW(buffer, 0, buffer.length, _Default_Bit_Ordering);
	}

	public int readIntsW(int[] buffer, Byte_Ordering byteordering) throws IOException {
		return readIntsW(buffer, 0, buffer.length, byteordering);
	}

	public int readIntsW(int[] buffer, int offset, int length) throws IOException {
		return readIntsW(buffer, offset, length, _Default_Bit_Ordering);
	}

	/**
	 * TODO: Needs Testing
	 */
	public int readIntsW(int[] buffer, int offset, int length, Byte_Ordering byteordering) throws IOException {
		checkNull(buffer);
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		final byte[] BYTES = new byte[length * 4];
		final int LENGTH = super.read(BYTES, 0, BYTES.length);

		int Y = 0;
		for(int X = 0; X < LENGTH; X++, Y += 4) {
			buffer[offset + X] = (char)BitOperations.bytesToShort(BYTES, Y, byteordering);
		}

		return LENGTH;
	}

	@Override
	public byte readByte() throws IOException {
		final byte[] BYTES = new byte[1];

		if(super.read(BYTES, 0, BYTES.length) == BYTES.length) {
			return BYTES[0];
		}

		return -1;
	}

	@Override
	public short readShort() throws IOException {
		return readShort(_Default_Bit_Ordering);
	}

	@Override
	public short readShort(Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[2];

		if(super.read(BYTES, 0, BYTES.length) == BYTES.length) {
			return BitOperations.bytesToShort(BYTES, bitordering);
		}

		return -1;
	}

	@Override
	public int readInt() throws IOException {
		return readInt(_Default_Bit_Ordering);
	}

	@Override
	public int readInt(Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[4];

		if(super.read(BYTES, 0, BYTES.length) == BYTES.length) {
			return BitOperations.bytesToInt(BYTES, bitordering);
		}

		return -1;
	}

	@Override
	public long readLong() throws IOException {
		return readLong(_Default_Bit_Ordering);
	}

	@Override
	public long readLong(Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[8];

		if(super.read(BYTES, 0, BYTES.length) == BYTES.length) {
			return BitOperations.bytesToLong(BYTES, bitordering);
		}

		return -1;
	}

	/**
	 * Needs Work
	 */
	@Override
	public int readUBytes(short[] buffer, int offset, int length) throws IOException {
		if(offset < 0 || (offset + length) > buffer.length) {
			throw new IndexOutOfBoundsException();
		}

		int Read = 0;
		short Value = -1;
		while(Read < length && (Value = readUByte()) != -1) {
			buffer[offset + Read] = Value;
			Read++;
		}

		return Read;
	}

	@Override
	public short readUByte() throws IOException {
		final byte BYTE = (byte)this.read();

		final short U = (short)(BYTE < 0 ? (((BYTE & 127)) | 128) : BYTE);

		UnsignedByte.checkRange(U);

		return U;
	}

	@Override
	public int readUShort() throws IOException {
		return readUShort(_Default_Bit_Ordering);
	}

	@Override
	public int readUShort(Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[4];

		if(super.read(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 2 : 0), 2) == 2) {
			final int U = BitOperations.bytesToInt(BYTES, bitordering);

			UnsignedShort.checkRange(U);

			return U;
		}

		return -1;
	}

	@Override
	public long readUInt() throws IOException {
		return readUInt(_Default_Bit_Ordering);
	}

	@Override
	public long readUInt(Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[8];

		if(super.read(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 4 : 0), 4) == 4) {
			final long U = BitOperations.bytesToLong(BYTES, bitordering);

			UnsignedInt.checkRange(U);

			return U;
		}

		return -1;
	}

	@Deprecated
	public boolean isOpen() {
		return !isClosed();
	}

	@Override
	public abstract int read() throws IOException;

//    @Override
//    public abstract int available() throws IOException;
	@Override
	public abstract boolean isClosed();

	@Override
	public abstract void close() throws IOException;

	//STATIC
	private static void checkNull(Object buffer) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}
	}
}