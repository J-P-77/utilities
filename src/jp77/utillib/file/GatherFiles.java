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

package jp77.utillib.file;

import java.io.File;

import jp77.beta.utillib.Queue;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * November 02, 2009 (Version 1.1.0)
 *     -Updated
 *         -Everything
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class GatherFiles extends Gather {

	/**
	 * Gets Files
	 * 
	 * @param crawldirectory
	 *            (File) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public GatherFiles(File crawldirectory) {
		super(crawldirectory, false, false);
	}

	/**
	 * Gets Files
	 * 
	 * @param crawl
	 *            (File) File or Directory to Crawl
	 * @param include
	 *            (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public GatherFiles(File crawldirectory, boolean include) {
		super(crawldirectory, include, false);
	}

	/**
	 * Gets Files
	 * 
	 * @param crawldirectory
	 *            (File) File or Directory to Crawl
	 * @param include
	 *            (boolean) Includes Sub Directories
	 * @param autorun
	 *            (boolean) Start Searching Immediately
	 * @param update
	 *            (IUpdate) When File Size Is Increased Will Call Method
	 *            updateFileSize() and updateTotalSize() When Total Size Is
	 *            Increased
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public GatherFiles(File crawldirectory, boolean include, boolean autorun) {
		super(crawldirectory, include, autorun);
	}

	@Override
	public void gather() {
		final Queue<File> DIRECTORIES = new Queue<File>(_CRAWL_DIRECTORY);

		while(!DIRECTORIES.isEmpty()) {
			final File DIRECTORY = DIRECTORIES.pop();
			_Directory_Count++;

			final File[] FILES = DIRECTORY.listFiles();
			for(int X = 0; X < FILES.length; X++) {
				if(FILES[X].isFile()) {
					if(isAcceptableFiles(FILES[X])) {
						final long FILESIZE = FILES[X].length();

						_TotalSize += FILESIZE;

						_File_Count++;
					}
				} else {
					if(_INCLUDE) {
						if(isAcceptableDirectory(FILES[X])) {
							DIRECTORIES.push(FILES[X]);//Pushes Directory Into Folder Stack
							_QUEUE.put(FILES[X]);
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
