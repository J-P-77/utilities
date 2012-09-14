package utillib.interfaces;

import java.io.File;

/**
 * 
 * @author Justin Palinkas
 */
public interface IFileDropper {
	public void allFilesAndDirecties(String fords);

	public void addFile(File file);

	public void addDirectory(File file);
}
