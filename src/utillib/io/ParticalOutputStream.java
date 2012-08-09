package utillib.io;

// import utillib.interfaces.ISeekable;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Dalton Dell
 */
public class ParticalOutputStream extends AOutputStream /* implements ISeekable */{
	private OutputStream _OStream = null;

	private final long _LENGTH;
	private long _Pos = 0;

	public ParticalOutputStream(OutputStream ostream, long length) {
		if(ostream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		_OStream = ostream;
		_LENGTH = length;
	}

	public long getCurrentPosition() throws IOException {
		return _Pos;
	}

	public long getLength() {
		return _LENGTH;
	}

//    public void seek(long pos) throws IOException {
//        if(pos < 0) {
//            _Pos = 0;
//        } else if(pos > _LENGTH) {
//            _Pos = _LENGTH;
//        } else {
//            _Pos = pos;
//        }
//    }
//
//    public void seek(Seek seek, long pos) throws IOException {
//        switch(seek) {
////SEEK_SET moves the pointer x bytes down from the beginning of the file (from byte 0 in the file).
//            case SEEK_SET://pos Must Be Positive
//                seek((pos < 0 ? 0 : pos));
//                break;
//
////SEEK_CUR moves the pointer x bytes down from the current pointer position.
//            case SEEK_CUR://pos Must Be Positive
//                seek(_Pos + (pos < 0 ? 0 : pos));
//                break;
//
////SEEK_END moves the pointer from the end of the file (so you must use negative offsets with this option).
//            case SEEK_END://pos Must Be Negative
//                seek(_LENGTH + (pos > 0 ? 0 : pos));
//                break;
//        }
//    }

	@Override
	public void flush() throws IOException {
		_OStream.flush();
	}

	@Override
	public void write(int i) throws IOException {
		if(_Pos < _LENGTH) {
			_OStream.write(i);
			_Pos++;
		} else {
			throw new IOException("Max Length Reached");
		}
	}

	@Override
	public boolean isClosed() {
		return _OStream == null;
	}

	@Override
	public void close() throws IOException {
		if(_OStream != null) {
			_OStream.flush();
			_OStream.close();
			_OStream = null;
		}
	}
}
