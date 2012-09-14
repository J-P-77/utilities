package utillib.interfaces;

import utillib.strings.MyStringBuffer;

import utillib.utilities.BitOperations.Byte_Ordering;

import java.io.IOException;

/**
 * 
 * @author Justin Palinkas
 */
public interface IMyInputStream extends IInputStream {
	public void setByteOrdering(Byte_Ordering bitordering);

	public Byte_Ordering getByteOrdering();

	public byte readByte() throws IOException;

	public int readBytes(byte[] buffer) throws IOException;

	public int readBytes(byte[] buffer, int offset, int length) throws IOException;

	public int readBytes(char[] buffer) throws IOException;

	public int readBytes(char[] buffer, int offset, int length) throws IOException;

	public int readBytes(int[] buffer) throws IOException;

	public int readBytes(int[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 1-byte per Read
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public int readChars(char[] buffer) throws IOException;

	@Deprecated
	public int readChars(char[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 1-bytes per Read
	 * 
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public int readInts(int[] buffer) throws IOException;

	@Deprecated
	public int readInts(int[] buffer, int offset, int length) throws IOException;

	public String readString(int length) throws IOException;

	public int readString(StringBuffer buffer, int numtoread) throws IOException;

	public int readString(MyStringBuffer buffer, int numtoread) throws IOException;

	public String readln() throws IOException;

	public boolean readln(StringBuffer line) throws IOException;

	public boolean readln(MyStringBuffer line) throws IOException;

	public int readln(char[] buffer, int offset, int length) throws IOException;

	/**
	 * Reads 4-Bytes per Read
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public int readIntsW(int[] buffer) throws IOException;

	public int readIntsW(int[] buffer, Byte_Ordering bitordering) throws IOException;

	public int readIntsW(int[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	/**
	 * Reads 2-Bytes per Read
	 * 
	 * @param ints
	 * @throws IOException
	 */
	public int readCharsW(char[] buffer) throws IOException;

	public int readCharsW(char[] buffer, Byte_Ordering bitordering) throws IOException;

	public int readCharsW(char[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException;

	public short readShort() throws IOException;

	public short readShort(Byte_Ordering bitordering) throws IOException;

	public int readInt() throws IOException;

	public int readInt(Byte_Ordering bitordering) throws IOException;

	public long readLong() throws IOException;

	public long readLong(Byte_Ordering bitordering) throws IOException;

	public int readUBytes(short[] buffer, int offset, int length) throws IOException;

	public short readUByte() throws IOException;

	public int readUShort() throws IOException;

	public int readUShort(Byte_Ordering bitordering) throws IOException;

	public long readUInt() throws IOException;

	public long readUInt(Byte_Ordering bitordering) throws IOException;
}
