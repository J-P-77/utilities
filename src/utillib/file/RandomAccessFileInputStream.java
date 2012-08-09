package utillib.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileInputStream extends InputStream {
	private RandomAccessFile _Raf;

	public RandomAccessFileInputStream(String file) throws FileNotFoundException, IOException {
		this(new File(file), FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(String file, long seekto) throws FileNotFoundException, IOException {
		this(new File(file), seekto, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(String file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new File(file), 0, mode);
	}

	public RandomAccessFileInputStream(File file) throws FileNotFoundException, IOException {
		this(file, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(File file, long seekto) throws FileNotFoundException, IOException {
		this(file, seekto, FileUtil.Random_Access_Mode.READ_ONLY);
	}

	public RandomAccessFileInputStream(File file, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(file, 0, mode);
	}

	public RandomAccessFileInputStream(File file, long seekto, FileUtil.Random_Access_Mode mode) throws FileNotFoundException, IOException {
		this(new RandomAccessFile(file, mode.getValue()));

		if(seekto < 0 /* || seekto >= _Raf.length() */) {
			throw new IOException("Invalid Seek Position: " + seekto);
		}

		if(seekto != 0) {
			_Raf.seek(seekto);
		}
	}

	public RandomAccessFileInputStream(RandomAccessFile raf) {
		if(raf == null) {
			throw new RuntimeException("Variable[raf] - Is Null");
		}

		_Raf = raf;
	}

	@Override
	public int read() throws IOException {
		return _Raf.read();
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return _Raf.read(buffer, offset, length);
	}

	@Override
	public synchronized void close() throws IOException {
		if(_Raf != null) {
			_Raf.close();
			_Raf = null;
		}
	}
}
