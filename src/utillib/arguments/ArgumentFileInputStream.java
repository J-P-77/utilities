package utillib.arguments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**<pre>
 *  Example Read:
 *
 *  ArgumentFileInputStream IStream = null;
 *  try {
 *      IStream = new ArgumentFileInputStream(_File);
 *
 *      Title CurTitle = null;
 *      while((CurTitle = IStream.readTitle()) != null) {
 *          Argument CurArgument = null;
 *          while((CurArgument = IStream.readArgument()) != null) {
 *              CurTitle.addArgument(CurArgument);
 *          }
 *
 *          //Add Title To A List Or Something
 *      }
 *  } catch (Exception ex) {
 *      System.out.println("!!!ERROR!!! - " + ex.getMessage());
 *  } finally {
 *      try {
 *          if(IStream != null) {
 *              IStream.close();
 *              IStream = null;
 *          }
 *      }catch (Exception i) {}
 *  }
 *
 * </pre>
 * @author Justin Palinkas
 */
public class ArgumentFileInputStream extends ArgumentInputStream {
    private final File _FILE;

    public ArgumentFileInputStream(String path)  throws FileNotFoundException {
        this(path, _DEFAULT_COMMENT_START_);
    }

    public ArgumentFileInputStream(File file)  throws FileNotFoundException {
        this(file, _DEFAULT_COMMENT_START_);
    }

    public ArgumentFileInputStream(String path, String commentstart) throws FileNotFoundException {
        this(new File(path), commentstart);
    }

    public ArgumentFileInputStream(File file, String commentstart) throws FileNotFoundException {
        super(new FileInputStream(file));

        _FILE = file;
    }

    public File getFile() {
        return _FILE ;
    }
}