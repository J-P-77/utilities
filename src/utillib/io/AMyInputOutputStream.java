package utillib.io;

import utillib.file.FileUtil;
import utillib.file.FileUtil.Line_Ending;

import utillib.interfaces.IMyInputStream;
import utillib.interfaces.IMyOutputStream;

import utillib.lang.unsigned.UnsignedByte;
import utillib.lang.unsigned.UnsignedInt;
import utillib.lang.unsigned.UnsignedShort;

import utillib.strings.MyStringBuffer;

import utillib.utilities.BitOperations;
import utillib.utilities.BitOperations.Byte_Ordering;

import java.io.IOException;

/**<pre>
 * <b>Current Version 1.1.0
 *
 * January 12, 2011 (version 1.0.0)
 *     -First Released
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public abstract class AMyInputOutputStream implements IMyInputStream, IMyOutputStream {
    private boolean _PreviousCharCR = false;

    private Byte_Ordering _Default_Bit_Ordering;
    private Line_Ending _Default_Line_Ending;
        
    public AMyInputOutputStream(Line_Ending lineending, Byte_Ordering bitordering) {
        if(lineending == null) {
            throw new RuntimeException("Variable[lineending] - Is Null");
        }

        _Default_Line_Ending = (lineending == null ? Line_Ending.WINDOWS : lineending);
        _Default_Bit_Ordering= (bitordering == null ? Byte_Ordering.LITTLE_ENDIAN : bitordering);
    }

    public boolean isOpen() {
        return !isClosed();
    }

    public void setByteOrdering(Byte_Ordering byteordering) {
    	BitOperations.checkNull(byteordering);
    	
    	_Default_Bit_Ordering = byteordering;
    }
    
    public Byte_Ordering getByteOrdering() {
    	return _Default_Bit_Ordering;
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
    
    @Override
    public String readln() throws IOException {
        final MyStringBuffer LINE = new MyStringBuffer();

        if(readln(LINE)) {
            return LINE.toString();
        } else {
            return null;
        }
    }
    
    /**
     * Does Not Reset The Buffer Before Reading Characters
     */
    @Override
    public boolean readln(MyStringBuffer buffer) throws IOException {
    	checkNull(buffer);
    	
        if(isOpen()) {
            int CurrentChar = -1;
            //r = 13, n = 10 =  [\r and \n] or [\r] or [\n]
//         System.out.println("Bytes Available (" + this.available() + ")");
            
            while((CurrentChar = read()) != -1) {                	
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

//              System.out.println("Bytes Available (" + this.available() + ") " + line.toString());
            }

            return (buffer.length() > 0);
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
            
            while((CurrentChar = read()) != -1) {
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
	 * 0 = More Data
	 * 1 = End Of Line
	 * 2 = Error
	 * 
	 * @param buffer
	 * @param offset
	 * @param length
	 * @param timout
	 * @return
	 * @throws IOException
	 */
    @Override
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
    	
    	if(numtoread < 0) {
    		throw new RuntimeException("Variable[numtoread] - Must Be > Than 0");
    	}
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < numtoread && (Read = read()) != -1) {
            buffer.append((char)Read);
            Counter++;
        }
        
        return Counter;
    }
    
    @Override
    public int readString(MyStringBuffer buffer, int numtoread) throws IOException {
    	checkNull(buffer);
    	
    	if(numtoread < 0) {
    		throw new RuntimeException("Variable[numtoread] - Must Be > Than 0");
    	}
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < numtoread && (Read = read()) != -1) {
            buffer.append((char)Read);
            Counter++;
        }
        
        return Counter;
    }

    @Override
    public int readBytes(byte[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }

    @Override
    public int readBytes(byte[] buffer, int offset, int length) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < length && (Read = read()) != -1) {
            buffer[offset + Counter] = (byte)Read;
            Counter++;
        }

        return Counter;
    }

    @Override
    public int readBytes(char[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }

    @Override
    public int readBytes(char[] buffer, int offset, int length) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < length && (Read = read()) != -1) {
            buffer[offset + Counter] = (char)Read;
            Counter++;
        }

        return Counter;
    }

    @Override
    public int readBytes(int[] buffer) throws IOException {
        return readBytes(buffer, 0, buffer.length);
    }

    @Override
    public int readBytes(int[] buffer, int offset, int length) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < length && (Read = read()) != -1) {
            buffer[offset + Counter] = (byte)Read;
            Counter++;
        }

        return Counter;
    }

// TODO: (Fix Read) Character Is Unsigned 2 bytes (I Think), Warning This Might Break Things
    @Override
    public int readChars(char[] buffer) throws IOException {
        return readChars(buffer, 0, buffer.length);
    }
    
// TODO: (Fix Read) Character Is Unsigned 2 bytes (I Think), Warning This Might Break Things
    @Override
    public int readChars(char[] buffer, int offset, int length) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < length && (Read = read()) != -1) {        	
            buffer[offset + Counter] = (char)Read;
            Counter++;            
        }

        return Counter;
    }    
    
 // TODO: (Fix Read) Integer Is 4 bytes, Warning This Might Break Things
    @Override
    public int readInts(int[] buffer) throws IOException {
        return readInts(buffer, 0, buffer.length);
    }

// TODO: (Fix Read) Integer Is 4 bytes, Warning This Might Break Things
    public int readInts(int[] buffer, int offset, int length) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
        int Counter = 0;
        int Read = -1;
        while(Counter < length && (Read = read()) != -1) {        	
            buffer[offset + Counter] = Read;
            Counter++;            
        }

        return Counter;
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
    
    public int readCharsW(char[] buffer, int offset, int length, Byte_Ordering byteordering) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);

    	final byte[] BYTES_BUFFER = new byte[2];

        int Counter = 0;
        while(Counter < length) {        	
        	final int NUM_READ = readBytes(BYTES_BUFFER, 0, 2);
        	
        	if(NUM_READ == 4) {
                buffer[offset + Counter] = (char)BitOperations.bytesToInt(BYTES_BUFFER, /*0, 2,*/ byteordering);
                Counter++;
        	} else {
        		throw new IOException();
        	}
        }

        return Counter;
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
    
    public int readIntsW(int[] buffer, int offset, int length, Byte_Ordering byteordering) throws IOException {
    	checkNull(buffer);
    	checkBufferBounds(buffer.length, offset, length);
    	
    	final byte[] BYTES_BUFFER = new byte[4];

        int Counter = 0;
        while(Counter < length) {        	
        	final int NUM_READ = readBytes(BYTES_BUFFER, 0, 4);
        	
        	if(NUM_READ == 4) {
                buffer[offset + Counter] = BitOperations.bytesToInt(BYTES_BUFFER, 0, /*4,*/ byteordering);
                Counter++;
        	} else {
        		throw new IOException();
        	}
        }

        return Counter;
    }
    
    public byte readByte() throws IOException {   	
        final byte[] BYTES = new byte[1];

        if(this.readBytes(BYTES, 0, BYTES.length) == BYTES.length) {
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

        if(readBytes(BYTES, 0, BYTES.length) == BYTES.length) {
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

        if(readBytes(BYTES, 0, BYTES.length) == BYTES.length) {
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

        if(readBytes(BYTES, 0, BYTES.length) == BYTES.length) {
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
        byte Value = -1;
        while(Read < length && (Value = (byte)read()) != -1) {
        	final short VALUE = (short)(Value < 0 ? (((Value & 127)) | 128) : Value);
        	
        	UnsignedByte.checkRange(VALUE);
        	
            buffer[offset + Read] = VALUE;
            
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

        if(readBytes(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 2 : 0), 2) == 2) {
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
        
        if(readBytes(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 4 : 0), 4) == 4) {        	
        	final long U =  BitOperations.bytesToLong(BYTES, bitordering);
        	
        	UnsignedInt.checkRange(U);
        	
        	return U;
        }

        return -1;
    }
    
    public void writeByte(byte b) throws IOException {
    	write(b);
    }
    
    @Override
    public void writeBytes(byte[] buffer) throws IOException {
    	writeBytes(buffer, 0, buffer.length);
    }
    
    @Override
    public void writeBytes(byte[] buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
    	writeBytes(buffer, offset, length);
    }

    public void writeBytes(char[] buffer) throws IOException {
        writeBytes(buffer, 0, buffer.length);
    }

    public void writeBytes(char[] buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
        for(int X = 0; X < length; X++) {
            write(buffer[offset + X]);
        }
    }

    public void writeBytes(int[] buffer) throws IOException {
        writeBytes(buffer, 0, buffer.length);
    }

    public void writeBytes(int[] buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
        for(int X = 0; X < length; X++) {
        	write(buffer[offset + X]);
        }
    }
    
    public void writeInts(int[] buffer) throws IOException {
        writeInts(buffer, 0, buffer.length);
    }

    public void writeInts(int[] buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
        for(int X = 0; X < length; X++) {
        	write(buffer[offset + X]);
        }
    }

    public void writeChars(char[] buffer) throws IOException {
        writeChars(buffer, 0, buffer.length);
    }

    public void writeChars(char[] buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
	
        for(int X = offset; X < length; X++) {
        	write(buffer[X]);
        }
    }

    public void writeIntsW(int[] buffer) throws IOException {
        writeInts(buffer, 0, buffer.length);
    }

    public void writeIntsW(int[] buffer, Byte_Ordering bitordering) throws IOException {
        writeIntsW(buffer, 0, buffer.length, bitordering);
    }

    public void writeIntsW(int[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
        for(int X = 0; X < length; X++) {
            writeInt(buffer[offset + X], bitordering);
        }
    }
    
    public void writeCharsW(char[] buffer) throws IOException {
        writeChars(buffer, 0, buffer.length);
    }

    public void writeCharsW(char[] buffer, Byte_Ordering bitordering) throws IOException {
        writeCharsW(buffer, 0, buffer.length, bitordering);
    }

    public void writeCharsW(char[] buffer, int offset, int length, Byte_Ordering bitordering) throws IOException {
    	checkBufferBounds(buffer.length, offset, length);
    	
        for(int X = 0; X < length; X++) {
            writeInt((int)buffer[offset + X]);
        }
    }
    public void writeString(String buffer) throws IOException {
        writeString(buffer, 0, buffer.length());
    }

    public void writeString(String buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length(), offset, length);
    	
        for(int X = 0; X < length; X++) {
        	write(buffer.charAt(offset + X));
        }
    }

    public void writeString(StringBuffer buffer) throws IOException {
        writeString(buffer, 0, buffer.length());
    }

    public void writeString(StringBuffer buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length(), offset, length);
    	
        for(int X = 0; X < length; X++) {
        	write(buffer.charAt(offset + X));
        }
    }

    public void writeString(MyStringBuffer buffer) throws IOException {
        writeString(buffer, 0, buffer.length());
    }

    public void writeString(MyStringBuffer buffer, int offset, int length) throws IOException {
    	checkBufferBounds(buffer.length(), offset, length);
    	
        for(int X = 0; X < length; X++) {
        	write(buffer.charAt(offset + X));
        }
    }

    public void writeBytesln(byte[] buffer) throws IOException {
        writeBytesln(buffer, 0, buffer.length);
    }

    public void writeBytesln(byte[] buffer, int offset, int length) throws IOException {
    	writeBytes(buffer, offset, length);
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
    	newline(_Default_Line_Ending);
    }
    
    public void newline(Line_Ending lineending) throws IOException {
    	writeString(lineending.getValue(), 0, lineending.getValue().length());
    }
    
    public void writeShort(short value) throws IOException {
    	writeShort(value, _Default_Bit_Ordering);
    }
    
    public void writeShort(short value, Byte_Ordering bitordering) throws IOException {
        final byte[] BYTES = new byte[2];

        BitOperations.shortToBytes(value, BYTES, bitordering);

        writeBytes(BYTES, 0, BYTES.length);
    }

    public void writeInt(int value) throws IOException {
    	writeInt(value, _Default_Bit_Ordering);
    }
    
    public void writeInt(int value, Byte_Ordering bitordering) throws IOException {
        final byte[] BYTES = new byte[4];

        BitOperations.intToBytes(value, BYTES, bitordering);

        writeBytes(BYTES, 0, BYTES.length);
    }
    
//	write((v >>> 8) & 0xFF);
//	write((v >>> 0) & 0xFF);
    public void writeChar(char value) throws IOException {    	
    	write(value);
    }
    
    public void writeLong(long value) throws IOException {
        writeLong(value, _Default_Bit_Ordering);
    }
    
    public void writeLong(long value, Byte_Ordering bitordering) throws IOException {
        final byte[] BYTES = new byte[8];

        BitOperations.longToBytes(value, BYTES, bitordering);

        writeBytes(BYTES, 0, BYTES.length);
    }
    
    @Override
    public void writeUByte(short value) throws IOException {
    	UnsignedByte.checkRange(value);
    	
    	writeBytes(new byte[] {(byte)value});
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

        writeBytes(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 2 : 0), 2);
    }

    @Override
    public void writeUInt(long value) throws IOException {
    	writeUInt(value, _Default_Bit_Ordering);
    }

    public void writeUInt(long value, Byte_Ordering bitordering) throws IOException {
    	UnsignedInt.checkRange(value);
    	
        final byte[] BYTES = new byte[8];

        BitOperations.longToBytes(value, BYTES, bitordering);
        
        writeBytes(BYTES, (bitordering == Byte_Ordering.BIG_ENDIAN ? 4 : 0), 4);
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }
    
    public abstract int read() throws IOException;
    public abstract void write(int b) throws IOException;
    public abstract void flush()  throws IOException;
    public abstract int available() throws IOException;
    public abstract boolean isClosed();
    public abstract void close() throws IOException;
    
    //STATIC
    private static void checkNull(Object buffer) {
        if(buffer == null) {
            throw new RuntimeException("Variable[buffer] - Is Null");
        }
    }
    
    private static void checkBufferBounds(int bufferlength, int offset, int length) {
    	if(offset < 0 || offset >= bufferlength ||
			length < 0 || length > bufferlength ||
			(offset + length) > bufferlength) {

    		throw new IndexOutOfBoundsException();
    	}
    }
}
