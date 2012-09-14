package utillib.io;

import utillib.file.FileUtil.Line_Ending;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.interfaces.IMyOutputStream;

import java.io.IOException;
import java.io.OutputStream;

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
 *         -Exported Everything To abstract class AMyOutputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyOutputStream extends AMyOutputStream implements IMyOutputStream {
	public static final MyOutputStream _SYSTEM_OUT_ = new MyOutputStream(System.out);

	private OutputStream _OStream = null;

	public MyOutputStream(OutputStream ostream) {
		this(ostream, Line_Ending.WINDOWS, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyOutputStream(OutputStream ostream, Line_Ending lineending) {
		this(ostream, lineending, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyOutputStream(OutputStream ostream, Byte_Ordering byteordering) {
		this(ostream, Line_Ending.WINDOWS, byteordering);
	}

	public MyOutputStream(OutputStream ostream, Line_Ending lineending, Byte_Ordering byteordering) {
		super(lineending, byteordering);

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		_OStream = ostream;
	}

	public OutputStream getInternalStream() {
		return _OStream;
	}

	public void write(int value) throws IOException {
		_OStream.write(value);
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		_OStream.write(buffer);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		_OStream.write(buffer, offset, length);
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	@Override
	public void close() throws IOException {
		if(_OStream != null) {
			_OStream.close();
			_OStream = null;
		}
	}
}
