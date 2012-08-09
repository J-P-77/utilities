package utillib.file;

import java.io.File;

import beta.utillib.Queue;

public class GatherDirectories extends Gather {
    
   /**
     * Gets Directories
     * 
     * @param crawldirectory (File) File or Directory to Crawl
     *
     * @exception IllegalArgumentException
     * @exception NullPointerException
     */
    public GatherDirectories(File crawldirectory)  {
        this(crawldirectory, false, false);
    }
    
   /**
     * Gets Directories
     * 
     * @param crawldirectory (File) File or Directory to Crawl
     * @param include (boolean) Includes Sub Directories
     *
     * @exception IllegalArgumentException
     * @exception NullPointerException
     */
    public GatherDirectories(File crawldirectory, boolean include)  {
        this(crawldirectory, include, false);
    }

   /**
     * Gets Directories
     * 
     * @param crawldirectory (File) File or Directory to Crawl
     * @param include (boolean) Includes Sub Directories
     * @param autorun (boolean) Start Searching Immediately
     * @param update (IUpdate) When File Size Is Increased Will Call Method updateFileSize() and updateTotalSize() When Total Size Is Increased
     * 
     * @exception IllegalArgumentException
     * @exception NullPointerException
     */
    public GatherDirectories(File crawldirectory, boolean include, boolean autorun) {
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
                if(pauseCancelCheck()) {return;}
            }
            if(pauseCancelCheck()) {return;}
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
