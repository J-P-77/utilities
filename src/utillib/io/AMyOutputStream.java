package utillib.io;

import utillib.arrays.ArraysUtil;
import utillib.file.FileUtil.Line_Ending;

import utillib.strings.MyStringBuffer;

import utillib.utilities.BitOperations;
import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.interfaces.IMyOutputStream;

import utillib.lang.unsigned.UnsignedByte;
import utillib.lang.unsigned.UnsignedInt;
import utillib.lang.unsigned.UnsignedShort;

import java.io.IOException;
import java.io.OutputStream;

/**<pre>
 * <b>Current Version 1.3.0</b>
 *
 * October 14, 2009 (version 1.0.0)
 *	   -First Released
 * 
 * November 17, 2010 (version 1.1.0)
 * 	   -Fixed Bug
 *         -Incorrect Offset and Length Checks
 * 
 * December 30, 2010 (version 1.2.0)
 * 	   -Fixed Bug
 *         -writeInt Incorrect Buffer Size
 * 
 * January 2, 2011 (version 1.3.0)
 *     -Fixed Bug
 *         -Buffer Offset And Length Handling In Some Methods
 * 
 * <b>default ordering:</b> Little Endian
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public abstract class AMyOutputStream extends OutputStream implements IMyOutputStream {
	private Line_Ending _Default_Line_Ending;

	private Byte_Ordering _Default_Bit_Ordering;

	protected AMyOutputStream() {
		this(Line_Ending.WINDOWS, Byte_Ordering.LITTLE_ENDIAN);
	}

	protected AMyOutputStream(Byte_Ordering byteordering) {
		this(Line_Ending.WINDOWS, byteordering);
	}

	protected AMyOutputStream(Line_Ending lineending) {
		this(lineending, Byte_Ordering.LITTLE_ENDIAN);
	}

	protected AMyOutputStream(Line_Ending lineending, Byte_Ordering byteordering) {
		setLineEnding(lineending);
		setByteOrdering(byteordering);
	}

	public void setLineEnding(Line_Ending lineending) {
		if(lineending == null) {
			throw new RuntimeException("Variable[lineending] - Is Null");
		}

		_Default_Line_Ending = lineending;
	}

	public Line_Ending getLineEnding() {
		return _Default_Line_Ending;
	}

	public void setByteOrdering(Byte_Ordering byteordering) {
		BitOperations.checkNull(byteordering);

		_Default_Bit_Ordering = byteordering;
	}

	public Byte_Ordering getByteOrdering() {
		return _Default_Bit_Ordering;
	}

	public void writeByte(byte b) throws IOException {
		this.write(b);
	}

	@Override
	public void writeBytes(byte[] buffer) throws IOException {
		super.write(buffer);
	}

	@Override
	public void writeBytes(byte[] buffer, int off, int len) throws IOException {
		super.write(buffer, off, len);
	}

	public void writeBytes(char[] buffer) throws IOException {
		writeBytes(buffer, 0, buffer.length);
	}

	public void writeBytes(char[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

//		for(int X = 0; X < length; X++) {
//			write(buffer[offset + X]);
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer[offset + X];
		}

		this.write(BYTES, 0, length);
	}

	public void writeBytes(int[] buffer) throws IOException {
		writeBytes(buffer, 0, buffer.length);
	}

	public void writeBytes(int[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

//		for(int X = 0; X < length; X++) {
//			write(buffer[offset + X]);
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer[offset + X];
		}

		this.write(BYTES, 0, length);
	}

	public void writeInts(int[] buffer) throws IOException {
		writeInts(buffer, 0, buffer.length);
	}

	public void writeInts(int[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

//		for(int X = 0; X < length; X++) {
//			this.write(buffer[offset + X]);
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer[offset + X];
		}

		this.write(BYTES, 0, length);
	}

	public void writeChars(char[] buffer) throws IOException {
		writeChars(buffer, 0, buffer.length);
	}

	public void writeChars(char[] buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
//			this.write(buffer[offset + X]);

			BYTES[X] = (byte)buffer[offset + X];
		}

		this.write(BYTES, 0, length);
	}

	public void writeIntsW(int[] buffer) throws IOException {
		writeInts(buffer, 0, buffer.length);
	}

	public void writeIntsW(int[] buffer, Byte_Ordering bitordering) throws IOException {
		writeIntsW(buffer, 0, buffer.length, bitordering);
	}

	/**
	 * TODO: Needs Testing
	 */
	public void writeIntsW(int[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

//		for(int X = 0; X < length; X++) {
//			this.writeInt(buffer[offset + X], bitordering);
//		}

		final byte[] BYTES = new byte[length * 4];
		int Y = 0;
		for(int X = 0; X < length; X++, Y += 4) {
			BitOperations.intToBytes(buffer[offset + X], BYTES, Y, bitordering);
		}

		this.write(BYTES, 0, length);
	}

	public void writeCharsW(char[] buffer) throws IOException {
		writeChars(buffer, 0, buffer.length);
	}

	public void writeCharsW(char[] buffer, Byte_Ordering bitordering) throws IOException {
		writeCharsW(buffer, 0, buffer.length, bitordering);
	}

	/**
	 * TODO: Needs Testing
	 */
	public void writeCharsW(char[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length, offset, length);

//		for(int X = 0; X < length; X++) {
//			this.writeInt((int)buffer[offset + X]);
//		}

		final byte[] BYTES = new byte[length * 2];
		int Y = 0;
		for(int X = 0; X < length; X++, Y += 2) {
			BitOperations.shortToBytes((short)buffer[offset + X], BYTES, Y, bitordering);
		}

		this.write(BYTES, 0, length);
	}

	public void writeString(String buffer) throws IOException {
		writeString(buffer, 0, buffer.length());
	}

	public void writeString(String buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length(), offset, length);

//		for(int X = 0; X < length; X++) {
//			this.write((int)buffer.charAt(offset + X));
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer.charAt(offset + X);
		}

		this.write(BYTES, 0, length);
	}

	public void writeString(StringBuffer buffer) throws IOException {
		writeString(buffer, 0, buffer.length());
	}

	public void writeString(StringBuffer buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length(), offset, length);

//		for(int X = 0; X < length; X++) {
//			this.write((int)buffer.charAt(offset + X));
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer.charAt(offset + X);
		}

		this.write(BYTES, 0, length);
	}

	public void writeString(MyStringBuffer buffer) throws IOException {
		writeString(buffer, 0, buffer.length());
	}

	public void writeString(MyStringBuffer buffer, int offset, int length) throws IOException {
		ArraysUtil.checkBufferBounds(buffer.length(), offset, length);

//		for(int X = 0; X < length; X++) {
//			this.write((int)buffer.charAt(offset + X));
//		}

		final byte[] BYTES = new byte[length];
		for(int X = 0; X < length; X++) {
			BYTES[X] = (byte)buffer.charAt(offset + X);
		}

		this.write(BYTES, 0, length);
	}

	public void writeBytesln(byte[] buffer) throws IOException {
		writeBytesln(buffer, 0, buffer.length);
	}

	public void writeBytesln(byte[] buffer, int offset, int length) throws IOException {
		super.write(buffer, offset, length);
		newline();
	}

	public void writeIntsln(int[] buffer) throws IOException {
		writeIntsln(buffer, 0, buffer.length);
	}

	public void writeIntsln(int[] buffer, int offset, int length) throws IOException {
		writeInts(buffer, offset, length);
		newline();
	}

	public void writeCharsln(char[] buffer) throws IOException {
		writeCharsln(buffer, 0, buffer.length);
	}

	public void writeCharsln(char[] buffer, int offset, int length) throws IOException {
		writeChars(buffer, offset, length);
		newline();
	}

	public void writeStringln(String str) throws IOException {
		writeStringln(str, 0, str.length());
	}

	public void writeStringln(String buffer, int offset, int length) throws IOException {
		writeString(buffer, offset, length);
		newline();
	}

	public void writeStringln(StringBuffer buffer) throws IOException {
		writeStringln(buffer, 0, buffer.length());
	}

	public void writeStringln(StringBuffer buffer, int offset, int length) throws IOException {
		writeString(buffer, offset, length);
		newline();
	}

	public void writeStringln(MyStringBuffer buffer) throws IOException {
		writeStringln(buffer, 0, buffer.length());
	}

	public void writeStringln(MyStringBuffer buffer, int offset, int length) throws IOException {
		writeString(buffer, offset, length);
		newline();
	}

	@Override
	public void writeln(String buffer) throws IOException {
		writeString(buffer, 0, buffer.length());
		newline();
	}

	public void newline() throws IOException {
		writeString(_Default_Line_Ending.getValue());
	}

	public void newline(Line_Ending lineending) throws IOException {
		writeString(lineending.getValue());
	}

	public void writeShort(short value) throws IOException {
		writeShort(value, _Default_Bit_Ordering);
	}

	public void writeShort(short value, Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[2];

		BitOperations.shortToBytes(value, BYTES, bitordering);

		super.write(BYTES, 0, BYTES.length);
	}

	public void writeInt(int value) throws IOException {
		writeInt(value, _Default_Bit_Ordering);
	}

	public void writeInt(int value, Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[4];

		BitOperations.intToBytes(value, BYTES, bitordering);

		super.write(BYTES, 0, BYTES.length);
	}

//	write((v >>> 8) & 0xFF);
//	write((v >>> 0) & 0xFF);
	public void writeChar(char value) throws IOException {
//        final byte[] BYTES = new byte[4];
//        BitOperations.intToBytes((int)value, BYTES, BitOperations.Byte_Ordering.BIG_ENDIAN);        
//        byte[] B = new byte[] {(byte)((value >>> 8) & 0xFF), (byte)((value >>> 0) & 0xFF)};
//        super.write(BYTES, 2, 2);

		write((int)value);
	}

	public void writeLong(long value) throws IOException {
		writeLong(value, _Default_Bit_Ordering);
	}

	public void writeLong(long value, Byte_Ordering bitordering) throws IOException {
		final byte[] BYTES = new byte[8];

		BitOperations.longToBytes(value, BYTES, bitordering);

		super.write(BYTES, 0, BYTES.length);
	}

	@Override
	public void writeUByte(short value) throws IOException {
		UnsignedByte.checkRange(value);

		super.write(new byte[] {(byte)value});
	}

	@Override
	public void writeUShort(int value) throws IOException {
		writeUShort(value, _Default_Bit_Ordering);
	}

	@Override
	public void writeUShort(int value, Byte_Ordering bitordering) throws IOException {
		UnsignedShort.checkRange(value);

		final byte[] BYTES = new byte[4];

		BitOperations.intToBytes(value, BYTES, bitordering);

		super.write(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 2 : 0), 2);
	}

	@Override
	public void writeUInt(long value) throws IOException {
		writeUInt(value, _Default_Bit_Ordering);
	}

	public void writeUInt(long value, Byte_Ordering bitordering) throws IOException {
		UnsignedInt.checkRange(value);

		final byte[] BYTES = new byte[8];

		BitOperations.longToBytes(value, BYTES, bitordering);

		super.write(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 4 : 0), 4);
	}

	//Writes Out Bytes To The Stream
	public abstract void write(int value) throws IOException;

	public abstract boolean isClosed();

	@Override
	public abstract void flush() throws IOException;

	@Override
	public abstract void close() throws IOException;
}
