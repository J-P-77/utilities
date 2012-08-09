package utillib.arguments;

import utillib.file.FileUtil.Line_Ending;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author Dalton Dell
 */
public class ArgumentFileOutputStream extends ArgumentOutputStream {
    private final File _FILE;

    public ArgumentFileOutputStream(String path)  throws FileNotFoundException {
        this(path, _DEFAULT_COMMENT_START_, Line_Ending.WINDOWS);
    }

    public ArgumentFileOutputStream(File file)  throws FileNotFoundException {
        this(file, _DEFAULT_COMMENT_START_, Line_Ending.WINDOWS);
    }

    public ArgumentFileOutputStream(String path,  Line_Ending lineending)  throws FileNotFoundException {
        this(path, _DEFAULT_COMMENT_START_, lineending);
    }

    public ArgumentFileOutputStream(File file,  Line_Ending lineending)  throws FileNotFoundException {
        this(file, _DEFAULT_COMMENT_START_, lineending);
    }

    public ArgumentFileOutputStream(String path, String commentstart, Line_Ending lineending) throws FileNotFoundException {
        this(new File(path), commentstart, lineending);
    }

    public ArgumentFileOutputStream(File file, String commentstart, Line_Ending lineending) throws FileNotFoundException {
        super(new FileOutputStream(file), commentstart, lineending);

        _FILE = file;
    }

    public File getFile() {
        return _FILE ;
    }
}
