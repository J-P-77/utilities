package utillib.interfaces;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Justin Palinkas
 */
public interface IFileSizeAccetable extends FileFilter {
    public boolean accept(File file);
}
