package utillib.file;

import utillib.interfaces.IMyInputStream;

import utillib.utilities.BitOperations.Byte_Ordering;

import utillib.io.MyInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 * <b>Current Version 1.0.4</b>
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * May 06, 2009 (version 1.0.2)
 *     -Updated
 *         -Method readln (Would Read Line Incorrectly)
 * 
 * May 06, 2009 (version 1.0.3)
 *     -Updated
 *         -Internal Stuff
 *     -Fixed Bug
 *         -One Of The write Method Would Actually Write A New Line
 * 
 * March 29, 2010 (version 1.0.4)
 *     -Updated
 *         -Exported Everything To abstract class AMyInputStream
 * 
 * <b>default ordering:</b> Little Endian
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyFileInputStream extends MyInputStream implements IMyInputStream {
//    private final Object _LOCK = new Object();

	private final File _FILE;

	public MyFileInputStream(String path) throws FileNotFoundException {
		this(path, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileInputStream(String filepath, Byte_Ordering ordering) throws FileNotFoundException {
		this(new File(filepath), ordering);
	}

	public MyFileInputStream(File file) throws FileNotFoundException {
		this(file, Byte_Ordering.LITTLE_ENDIAN);
	}

	public MyFileInputStream(File file, Byte_Ordering ordering) throws FileNotFoundException {
		super(new FileInputStream(file), ordering);

		_FILE = file;
	}

	public long length() {
		return _FILE.length();
	}

	public File getFile() {
		return _FILE;
	}

//    public static void main(String[] args) {
//        final String[] PATHS = {
//            "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Temp.txt"
//        };
//
//        MyFileInputStream IStream = null;
//        try {
//            IStream = new MyFileInputStream(PATHS[0]);
//            
//        } catch (Exception e) {
//            System.out.println("!!ERROR!!! - Cause, " + e.getMessage());
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
