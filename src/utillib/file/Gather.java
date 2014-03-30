/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.file;

import utillib.arrays.ResizingArray;

import java.io.File;
import java.io.FileFilter;

public abstract class Gather extends Thread {
	private final ResizingArray<FileFilter> _ACCEPTABLE_FILES = new ResizingArray<FileFilter>(0, 1);
	private final ResizingArray<FileFilter> _ACCEPTABLE_DIRECTORIES = new ResizingArray<FileFilter>(0, 1);

	protected final File _CRAWL_DIRECTORY;
	protected final boolean _INCLUDE;

	protected long _TotalSize = 0;
	protected int _Directory_Count = 0;
	protected int _File_Count = 0;

	private boolean _IsRunning = false;
	private boolean _Cancel = false;
	private boolean _Paused = false;

	final ResizingArray<File> _QUEUE = new ResizingArray<File>();

	/**
	 * 
	 * @param crawldirectory
	 *            (File) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public Gather(File crawldirectory) {
		this(crawldirectory, false, false);
	}

	/**
	 * Gets Directories
	 * 
	 * @param crawldirectory
	 *            (File) File or Directory to Crawl
	 * @param include
	 *            (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public Gather(File crawldirectory, boolean include) {
		this(crawldirectory, include, false);
	}

	/**
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
	public Gather(File crawldirectory, boolean include, boolean autorun) {
		if(crawldirectory == null) {
			throw new NullPointerException("Variable[crawldirectory] - Is Null");
		}

		if(!crawldirectory.exists()) {
			throw new RuntimeException("Variable[crawldirectory] - Path: " + crawldirectory.getPath() + " - Does Not Exists");
		}

		if(crawldirectory.isFile()) {
			throw new RuntimeException("Variable[crawldirectory] - Path: " + crawldirectory.getPath() + " - Must Be A Direcoty");
		}

		_QUEUE.put(crawldirectory);

		_CRAWL_DIRECTORY = crawldirectory;
		_INCLUDE = (_CRAWL_DIRECTORY.isDirectory() ? include : false);
		_IsRunning = true;

		if(autorun) {
			super.start();
		}
	}

	public ResizingArray<FileFilter> getAcceptablesFiles() {
		return _ACCEPTABLE_FILES;
	}

	public ResizingArray<FileFilter> getAcceptablesDirectories() {
		return _ACCEPTABLE_DIRECTORIES;
	}

	public void cancel() {
		_Cancel = true;
	}

	public boolean isCanceled() {
		return _Cancel;
	}

	public void pause(boolean value) {
		_Paused = value;
	}

	public boolean isPaused() {
		return _Paused;
	}

	public boolean isRunning() {
		return _IsRunning || super.isAlive();
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

	public File getCrawlingDirectory() {
		return _CRAWL_DIRECTORY;
	}

	public ResizingArray<File> getDirectories() {
		if(isRunning()) {
			throw new RuntimeException("Variable[_DIRECTORIES] - You Must wait For Thread To Complete");
		}

		return _QUEUE;
	}

	protected boolean isAcceptableFiles(File file) {
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

	protected boolean isAcceptableDirectory(File directory) {
		if(_ACCEPTABLE_DIRECTORIES.length() == 0) {
			return true;
		}

		for(int X = 0; X < _ACCEPTABLE_FILES.length(); X++) {
			if(_ACCEPTABLE_DIRECTORIES.getAt(X).accept(directory)) {
				return true;
			}
		}

		return false;
	}

	protected boolean pauseCancelCheck() {
		while(_Paused) {
			try {
				if(_Cancel) {
					return true;
				}
				Thread.sleep(200);
			} catch(Exception e) {}
		}

		return _Cancel;
	}

	@Override
	public void run() {
		if(_CRAWL_DIRECTORY.isDirectory()) {
			gather();
		}

		_IsRunning = false;
	}

	public abstract void gather();
}
