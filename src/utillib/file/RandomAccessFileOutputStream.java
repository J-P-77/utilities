package utillib.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileOutputStream extends OutputStream {
	private RandomAccessFile _Raf;

	public RandomAccessFileOutputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file), FileUtil.Random_Access_Mode.READ_WRITE);
	}

	public RandomAccessFileOutputStream(String file, long seekto) throws FileNotFoundException, IOException {
		this(new File(file), seekto, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileOutputStream(String file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new File(file), 0, mode);
	}

	public RandomAccessFileOutputStream(File file) throws FileNotFoundException, IOException {
		this(file, FileUtil.Random_Access_Mode.READ_WRITE);
	}

	public RandomAccessFileOutputStream(File file, long seekto) throws FileNotFoundException, IOException {
		this(file, seekto, FileUtil.Random_Access_Mode.READ_WRITE);
	}

	public RandomAccessFileOutputStream(File file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(file, 0, mode);
	}

	public RandomAccessFileOutputStream(File file, long seekto, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode.getValue()));

		if(seekto < 0 /* || seekto >= _Raf.length() */) {
			throw new IOException("Invalid Seek Position: " + seekto);
		}

		if(seekto != 0) {
			_Raf.seek(seekto);
		}
	}

	public RandomAccessFileOutputStream(RandomAccessFile raf) {
		if(raf == null) {
			throw new RuntimeException("Variable[raf] - Is Null");
		}

		_Raf = raf;
	}

	@Override
	public void write(int b) throws IOException {
		_Raf.write(b);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		_Raf.write(buffer, offset, length);
	}

	@Override
	public synchronized void close() throws IOException {
		if(_Raf != null) {
			_Raf.close();
			_Raf = null;
		}
	}
}
