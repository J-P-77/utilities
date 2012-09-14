package utillib.io;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.interfaces.IMyInputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * October 14, 2009 (version 1.0.0)
 *     -First Released
 * 
 * January 10, 2010 (version 1.0.1)
 *     -Added
 *         -Bit Ordering
 * 
 * March 29, 2010 (version 1.0.2)
 *     -Updated
 *         -Exported Everything To abstract class AMyInputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyInputStream extends AMyInputStream implements IMyInputStream {
	public static final MyInputStream _SYSTEM_IN_ = new MyInputStream(System.in);

	protected InputStream _IStream = null;

	public MyInputStream(InputStream istream) {
		this(istream, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyInputStream(InputStream istream, Byte_Ordering byteordering) {
		super(byteordering);

		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_IStream = istream;
	}

	public InputStream getInternalStream() {
		return _IStream;
	}

	@Override
	public int read() throws IOException {
		return _IStream.read();
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		return _IStream.read(buffer);
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return _IStream.read(buffer, offset, length);
	}

	@Override
	public int available() throws IOException {
		return _IStream.available();
	}

	@Override
	public boolean isClosed() {
		return _IStream == null;
	}

	@Override
	public synchronized void close() throws IOException {
		if(_IStream != null) {
			_IStream.close();
			_IStream = null;
		}
	}

//    public static void main(String[] args) {
//        java.io.FileInputStream FIStream = null;
//        MyInputStream IStream = null;
//
//        try {
//            FIStream = new java.io.FileInputStream("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Test.txt");
//            IStream = new MyInputStream(FIStream);
//
//            for(int X = 0; X < 6; X++) {
//                System.out.println(IStream.readString(6));
//            }
//
//            System.out.println(IStream.readString(6));
//            System.out.println(IStream.readString(6));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(IStream != null) {
//                    IStream.close();
//                    IStream = null;
//                }
//            } catch (Exception e) {}
//        }
//    }
}
