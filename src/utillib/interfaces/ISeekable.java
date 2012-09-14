package utillib.interfaces;

import java.io.IOException;

/**
 * 
 * @author Dalton Dell
 */
public interface ISeekable {
	public static enum Seek {
		SEEK_SET, //From Beginning Of The File
		SEEK_CUR, //From File Pointer Of The File
		SEEK_END;//From End Of The File
	};

	public long getCurrentPosition() throws IOException;

	public void seek(long pos) throws IOException;

	public void seek(Seek seek, long pos) throws IOException;

//SEEK_SET moves the pointer x bytes down from the  beginning of the file (from byte 0 in the file).
//SEEK_CUR moves the pointer x bytes down from the current pointer position.
//SEEK_END moves the pointer from the end of the file (so you must use negative offsets with this option).
}
