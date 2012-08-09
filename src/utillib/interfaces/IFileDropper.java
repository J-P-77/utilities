package utillib.interfaces;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public interface IFileDropper {
    public void allFilesAndDirecties(String fords);

    public void addFile(File file);
    public void addDirectory(File file);
}
