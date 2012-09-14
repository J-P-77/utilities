package utillib.file;

import beta.utillib.Queue;

import utillib.arrays.ResizingArray;

import java.io.File;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FileCrawler extends Thread {
	private final File _CRAWL;
	private final boolean _INCLUDE;

	private boolean _Cancel;
	private long _TotalSize;

	private ResizingArray<File> _FolderStack = new ResizingArray<File>();
	private ResizingArray<File> _FileStack = new ResizingArray<File>();
	private boolean _BuildList;
	private boolean _IsRunning;

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(String crawl) {
		this(new File(crawl), false, false, false);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(String crawl, boolean include) {
		this(new File(crawl), include, false, false);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
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
	public FileCrawler(String crawl, boolean include, boolean autorun) {
		this(new File(crawl), include, false, autorun);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (String) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param autorun
	 *            - (boolean) Start Searching Immediately
	 * @param buildlist
	 *            - (boolean) Adds File and\or Folders Into Stack
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(String crawl, boolean include, boolean autorun, boolean buildlist) {
		this(new File(crawl), include, buildlist, autorun);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(File crawl) {
		this(crawl, false, false, false);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(File crawl, boolean include) {
		this(crawl, include, false, false);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param buildlist
	 *            - (boolean) Adds File and\or Folders Into Stack
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(File crawl, boolean include, boolean buildlist) {
		this(crawl, include, buildlist, false);
	}

	/**
	 * Searches A File or Directory for Files and\or Directories
	 * 
	 * @param crawl
	 *            - (File) File or Directory to Crawl
	 * @param include
	 *            - (boolean) Includes Sub Directories
	 * @param buildlist
	 *            - (boolean) Adds File and\or Folders Into Stack
	 * @param autorun
	 *            - (boolean) Start Searching Immediately
	 * 
	 * @exception IllegalArgumentException
	 * @exception NullPointerException
	 */
	public FileCrawler(File crawl, boolean include, boolean buildlist, boolean autorun) {//,int threadpriority
		if(crawl != null) {
			_CRAWL = crawl;
			_BuildList = buildlist;
			//this.setPriority(MAX_PRIORITY);

			if(_CRAWL.isDirectory()) {
				_INCLUDE = include;
			} else if(_CRAWL.isFile()) {
				_INCLUDE = false;
			} else {
				throw new IllegalArgumentException("Crawl: " + _CRAWL.getPath() + " Is Not A Directory Or File");
			}

			if(autorun) {
				this.start();
			}
		} else {
			throw new NullPointerException("Crawl Is NULL");
		}
	}

	@Override
	public void run() {
		if(_CRAWL != null) {
			_IsRunning = true;
			if(_CRAWL.isFile()) {
				crawlFile();
			} else {
				crawlFolder();
			}
			_IsRunning = false;
		}
	}

	public boolean isRunning() {
		return _IsRunning;
	}

	public boolean buildList() {
		return _BuildList;
	}

	public void buildList(boolean value) {
		_BuildList = value;
	}

	public void cancel() {
		_Cancel = true;
	}

	public long getTotalSize() {
		return _TotalSize;
	}

	public File getFile(int index) {
		return _FileStack.getAt(index);
	}

	public int fileLength() {
		return _FileStack.length();
	}

	public File getFolder(int index) {
		return _FolderStack.getAt(index);
	}

	public int folderLength() {
		return _FolderStack.length();
	}

	public ResizingArray<File> getFiles() {
		return _FileStack;
	}

	public ResizingArray<File> getFolders() {
		return _FolderStack;
	}

	private void crawlFile() {
		if(_CRAWL == null) { //ERROR
			throw new NullPointerException("Source Is Null");
		} else {
			if(_CRAWL.isFile()) {
				_TotalSize += _CRAWL.length();

				if(_BuildList) {
					_FileStack.put(_CRAWL);
				}//PUSHES FILE INTO (_FileStack) STACK
			}
		}
	}

	private void crawlFolder() {
		if(_CRAWL == null) { //ERROR
			throw new NullPointerException("Source Is Null");
		} else {
			if(_CRAWL.isDirectory()) {
				final Queue<File> DIRECTORIES = new Queue<File>(_CRAWL);

				while(!DIRECTORIES.isEmpty()) {
					File SourceFolder = DIRECTORIES.pop();

					File[] Files = SourceFolder.listFiles();

					for(int X = 0; X < Files.length; X++) {
						if(Files[X].isFile()) {
							_TotalSize += Files[X].length();

							if(_BuildList) {
								_FileStack.put(Files[X]);//Pushes File Into (_FileStack) Stack
							}
						} else {
							if(_INCLUDE) {
								DIRECTORIES.push(Files[X]);//Pushes Folder Into Folder Stack

								if(_BuildList) {
									_FolderStack.put(Files[X]);//Pushes Folder Into (_FolderStack) Stack
								}
							}
						}
						if(_Cancel) {
							break;
						}
					}
					if(_Cancel) {
						break;
					}
				}
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		_Cancel = true;
	}
/*
    public static void main(String[] args) {
        String Filename = "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Justin's";
        
        File F = new File(Filename);
        
        System.out.println(F.length());
    }
*/
}
