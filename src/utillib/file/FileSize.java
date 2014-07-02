/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package utillib.file;

import beta.utillib.Queue;

import utillib.arrays.ResizingArray;

import utillib.interfaces.IStatus;
import utillib.interfaces.IUpdateSize;
import utillib.utilities.Status;

import java.io.File;
import java.io.FileFilter;

/**
 * <pre>
 * <b>Current Version 1.2.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released<br>
 * 
 * April 08, 2009 (Version 1.0.1)
 *     -Added
 *         -Variable _Update(IUpdate) When File Size Is Increased Will Call Method
 *             updateFileSize() and updateTotalSize()
 * 
 * February 01, 2009 (Version 1.0.2)
 *     -Added
 *         -You Can Have Multiple FileFilters, Checks Whether File or Directory Is
 *             Accepted In Gathering Of The Directory Size
 * 
 * April 27, 2010 (Version 1.1.0)
 *     -Updated
 *         -Separated File and Directories FileFilters
 * 
 * January 10, 2011 (Version 1.2.0)
 *     -Fixed Bug
 *     	   -Method isAcceptableDirectory(File) Was Using The Wrong ResizingArray Length
 *     
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FileSize extends Thread {
	private final IUpdateSize _UPDATER;

	private final ResizingArray<FileFilter> _ACCEPTABLE_FILES = new ResizingArray<FileFilter>(0, 1);
	private final ResizingArray<FileFilter> _ACCEPTABLE_DIRECTORIES = new ResizingArray<FileFilter>(0, 1);

	private final File _CRAWL;
	private final boolean _INCLUDE;

	private long _TotalSize = 0;
	private int _Directory_Count = 0;
	private int _File_Count = 0;

	private final Status _STATUS = new Status();

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(String crawl) {
		this(new File(crawl), false, false, null);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(String crawl, boolean include) {
		this(new File(crawl), include, false, null);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param autorun
	 *            - (boolean) Start Searching Immediately
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(String crawl, boolean include, boolean autorun) {
		this(new File(crawl), include, autorun, null);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param autorun
	 *            - (boolean) Start Searching Immediately
	 * @param update
	 *            - (IUpdate) When File Size Is Increased Will Call Method
	 *            updateFileSize() and updateTotalSize() When Total Size Is
	 *            Increased
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(String crawl, boolean include, boolean autorun, IUpdateSize update) {
		this(new File(crawl), include, autorun, update);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(File crawl) {
		this(crawl, false, false, null);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(File crawl, boolean include) {
		this(crawl, include, false, null);
	}

	/**
	 * Gets File or Directory Size
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param autorun
	 *            - (boolean) Start Searching Immediately
	 * @param update
	 *            - (IUpdate) When File Size Is Increased Will Call Method
	 *            updateFileSize() and updateTotalSize() When Total Size Is
	 *            Increased
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileSize(File crawl, boolean include, boolean autorun, IUpdateSize update) {
		if(crawl == null) {
			throw new NullPointerException("Variable[crawl] - Is Null");
		}

		if(!crawl.exists()) {
			throw new RuntimeException(" Variable[crawl] - Path: " + crawl.getPath() + " - Does Not Exists");
		}

		_CRAWL = crawl;
		_UPDATER = (update == null ? new Updater() : update);
		_INCLUDE = (_CRAWL.isDirectory() ? include : false);
		_STATUS.start();

		if(autorun) {
			super.start();
		}
	}

	@Override
	public void run() {
		if(_CRAWL.isFile()) {
			crawlFile();
		} else if(_CRAWL.isDirectory()) {
			crawlDirectory();
		}

		if(_STATUS.isCanceling()) {
			_STATUS.canceled();
		} else {
			_STATUS.done();
		}
	}

	public ResizingArray<FileFilter> getAcceptablesFiles() {
		return _ACCEPTABLE_FILES;
	}

	public ResizingArray<FileFilter> getAcceptablesDirectories() {
		return _ACCEPTABLE_DIRECTORIES;
	}

	public IStatus getStatus() {
		return _STATUS;
	}

	public long getTotalSize() {
		return _TotalSize;
	}

	public int getFileCount() {
		return _File_Count;
	}

	public int getDirectoryCount() {
		return _Directory_Count;
	}

	public File getCrawlingFile() {
		return _CRAWL;
	}

	private void crawlFile() {
		if(_CRAWL.isFile()) {
			final long FILESIZE = _CRAWL.length();

			_UPDATER.fileSize(FILESIZE);

			_TotalSize += FILESIZE;

			_UPDATER.newTotalSize(_TotalSize);

			_File_Count++;
		} else {
			throw new RuntimeException(_CRAWL.getPath() + " - Is Not A File");
		}
	}

	private void crawlDirectory() {
		if(_CRAWL.isDirectory()) {
			final Queue<File> DIRECTORIES = new Queue<File>(_CRAWL);

			while(!DIRECTORIES.isEmpty()) {
				final File DIRECTORY = DIRECTORIES.pop();
				_Directory_Count++;

				final File[] FILES = DIRECTORY.listFiles();
				for(int X = 0; X < FILES.length; X++) {
					if(FILES[X].isFile()) {
						if(isAcceptableFiles(FILES[X])) {
							final long FILESIZE = FILES[X].length();

							_UPDATER.fileSize(FILESIZE);

							_TotalSize += FILESIZE;

							_UPDATER.newTotalSize(_TotalSize);

							_File_Count++;
						}
					} else {
						if(_INCLUDE) {
							if(isAcceptableDirectory(FILES[X])) {
								DIRECTORIES.push(FILES[X]);//Pushes Directory Into Folder Stack
							}
						}
					}
					if(pauseCancelCheck()) {
						return;
					}
				}
				if(pauseCancelCheck()) {
					return;
				}
			}
		} else {
			throw new RuntimeException(_CRAWL.getPath() + " - Is Not A Directory");
		}
	}

	private boolean isAcceptableFiles(File file) {
		if(_ACCEPTABLE_FILES.length() == 0) {
			return true;
		}

		for(int X = 0; X < _ACCEPTABLE_FILES.length(); X++) {
			if(_ACCEPTABLE_FILES.getAt(X).accept(file)) {
				return true;
			}
		}

		return false;
	}

	private boolean isAcceptableDirectory(File directory) {
		if(_ACCEPTABLE_DIRECTORIES.length() == 0) {
			return true;
		}

		for(int X = 0; X < _ACCEPTABLE_DIRECTORIES.length(); X++) {
			if(_ACCEPTABLE_DIRECTORIES.getAt(X).accept(directory)) {
				return true;
			}
		}

		return false;
	}

	private boolean pauseCancelCheck() {
		while(_STATUS.isPaused()) {
			try {
				if(_STATUS.isCanceling()) {
					return true;
				}
				Thread.sleep(200);
			} catch(Exception e) {}
		}

		return _STATUS.isCanceling();
	}

	//CLASSES
	private class Updater implements IUpdateSize {//Default Updater
		public void fileSize(long filesize) {}

		public void newTotalSize(long newtotalsize) {}
	}
/*
	public static void main(String[] args) {
        final File[] TEMP = {
            new File("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Temp")
        };
        
		for(int X = 0; X < TEMP.length; X++) {
			final FileSizeBeta FSIZE = new FileSizeBeta(TEMP[X], true);
            FSIZE.getAcceptables().put(new IFileSizeAccetable() {
                public boolean accept(File file) {
                    if(file.isHidden()) {
                        System.out.println("[IsHidden] Skipped File: " + file.getPath());
                        return false;
                    }

                    return true;
                }
            });
            FSIZE.getAcceptables().put(new IFileSizeAccetable() {
                private static final int _SIZE_ = 1024;

                public boolean accept(File file) {
                    if(file.length() > _SIZE_) {
                        System.out.println("[IsGreaterThan " + utillib.utilities.Convert.convertBytes(_SIZE_, 1) + "] Skipped File: " + file.getPath());
                        return false;
                    }

                    return true;
                }
            });

			FSIZE.start();
            while(FSIZE.isRunning()) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {}
            }

            System.out.println("Directory Size: " + utillib.utilities.Convert.convertBytes(FSIZE.getTotalSize()));
            System.out.println("# Of Directories: " + FSIZE.getDirectoryCount());
            System.out.println("# Of Files: " + FSIZE.getFileCount());
		}
	}
*/
}
