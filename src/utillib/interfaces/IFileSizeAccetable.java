package utillib.interfaces;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Dalton Dell
 */
public interface IFileSizeAccetable extends FileFilter {
    public boolean accept(File file);
}
