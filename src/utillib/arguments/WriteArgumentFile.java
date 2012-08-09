package utillib.arguments;

import utillib.file.FileUtil.Line_Ending;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**<pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class WriteArgumentFile extends ArgumentOutput {
    protected final File _File;
    
    public WriteArgumentFile(String filename) throws FileNotFoundException {
        this(new File(filename));
    }
    
    public WriteArgumentFile(File file) throws FileNotFoundException {
        super(new FileOutputStream(file));

        _File = file;
		
		setLineEnding(Line_Ending.WINDOWS);
    }

    public File getFile() {
        return _File;
    }

    public static WriteArgumentFile create(String file) {
        return create(new File(file));
    }

    public static WriteArgumentFile create(File file) {
        try {
            return new WriteArgumentFile(file);
        } catch (Exception e) {}

        return null;
    }


//    public boolean write() {
//        return write(false);
//    }
//
//    //returns true if write was successful
//    public boolean write(boolean autocase) {
//        ArgumentFileOutputStream AOStream = null;
//        boolean NoErrors = true;
//        try {
//            AOStream = new ArgumentFileOutputStream(_File, _LineEndings);
//
//            for(int X = 0; X < _Titles.length(); X++) {
//                if(X != 0) {AOStream.newline();}
//
//                AOStream.writeTitle(_Titles.getItemAt(X), true, autocase);
//            }
//        } catch(Exception e) {
//			System.out.println("!!!ERROR!!!" + e.getMessage());
//            NoErrors = false;
//		} finally {
//			try {
//				if(AOStream != null) {
//                    AOStream.flush();
//					AOStream.close();
//					AOStream = null;
//				}
//			} catch (Exception i) {}
//		}
//
//        return NoErrors;
//    }
}