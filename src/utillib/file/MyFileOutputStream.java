package utillib.file;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.file.FileUtil.Line_Ending;

import utillib.io.MyOutputStream;

import utillib.interfaces.IMyOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * April 01, 2009 (version 1.0.0)
 *     -Updated
 *         -EveryThing
 * 
 * March 29, 2010 (version 1.0.1)
 *     -Updated
 *         -Exported Everything To abstract class AMyOutputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyFileOutputStream extends MyOutputStream implements IMyOutputStream {
//    private final Object _LOCK = new Object();

	private final File _FILE;

	public MyFileOutputStream(String path) throws FileNotFoundException {
		this(path, Line_Ending.WINDOWS);
	}

	public MyFileOutputStream(File file) throws FileNotFoundException {
		this(file, Line_Ending.WINDOWS);
	}

	public MyFileOutputStream(String path, Line_Ending lineending) throws FileNotFoundException {
		this(new File(path), lineending, false, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileOutputStream(String path, boolean append) throws FileNotFoundException {
		this(new File(path), Line_Ending.WINDOWS, append, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileOutputStream(String path, Line_Ending lineending, boolean append) throws FileNotFoundException {
		this(new File(path), lineending, append, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileOutputStream(String path, Byte_Ordering ordering) throws FileNotFoundException {
		this(new File(path), Line_Ending.WINDOWS, false, ordering);
	}

	public MyFileOutputStream(File file, Line_Ending lineending) throws FileNotFoundException {
		this(file, lineending, false, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileOutputStream(File file, boolean append) throws FileNotFoundException {
		this(file, Line_Ending.WINDOWS, append, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileOutputStream(File file, Byte_Ordering ordering) throws FileNotFoundException {
		this(file, Line_Ending.WINDOWS, false, ordering);
	}

	public MyFileOutputStream(File file, Line_Ending lineending, boolean append, Byte_Ordering ordering) throws FileNotFoundException {
		super(new FileOutputStream(file, append), lineending, ordering);

		_FILE = file;
	}

	public long length() {
		return _FILE.length();
	}

	public File getFile() {
		return _FILE;
	}
	/*
	public static void main(String[] args) {
	    String FileName = "Test 1.txt";

	    MyFileOutputStream OStream = null;
	    try {
	        OStream = new MyFileOutputStream("C:\\Documents and Settings\\Dalton Dell\\Desktop\\" + FileName);

	    } catch (Exception e) {
	        System.out.println("!!ERROR!!! - Cause, " + e.getMessage());
	    } finally {
	        try {
	            if(OStream != null) {
	                OStream.close();
	                OStream = null;
	            }
	        } catch (Exception e) {}
	    }
	}*/
}
