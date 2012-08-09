package utillib.arguments;

import utillib.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;

/**<pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 01, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ReadSettingFile extends SettingShared {
    private int _PreviousChar = -1;
    
    public ReadSettingFile(String path) {
        this(new File(path));
    }
    
    public ReadSettingFile(File file) {
        super(file);

		if(_FILE.isFile() && _FILE.exists()) {
			readFile();
		}
    }
    
    public int length() {
        return _Arguments.length();
    }
    
    private void readFile() {
		FileInputStream iStream = null;
		
        try {
			iStream = new FileInputStream(_FILE);

			String Str = "";
			while((Str = readln(iStream)) != null) {   
				if(isComment(Str)) {
					//DO NOTHING
				} else if(isBlankLine(Str)) {
					//DO NOTHING
				} else if(isArgument(Str)) {
					final String[] SPLIT = Str.split("=", 2);
					
					addArgument(new Argument(SPLIT[0], SPLIT[1]));
				}
			}
        } catch (Exception e) {
			System.out.println("!!!ERROR!!! - " + e.getMessage());
		} finally {
			if(iStream != null) {
				try {
					iStream.close();
				} catch(Exception i) {}
				iStream = null;
			}
		}
    }
            
    private void addArgument(Argument argument) {
        if(argument != null) {
            int ArgIndex = findArgIndex(argument.getName());
            
            if(ArgIndex == -1) {
                _Arguments.push(argument);
            } else {
				_Arguments.setItemAt(argument, ArgIndex);
            }
        }
    }
    
    private static boolean isComment(String str) {
        return str.startsWith("//");
    }
    
    private static boolean isBlankLine(String str) {
		return str.equals("");
    }
    
    private static boolean isArgument(String str) {
        return str.contains("=");
    }

    public String readln(FileInputStream istream) {
        StringBuffer Response = new StringBuffer();

        boolean EndofLine = false;
        boolean SkipChar = false;
        int CurrentChar = -1;
        //r = 13, n = 10
        //\r,\n
        //\r
        //\n
        try {
            while((CurrentChar = istream.read()) != -1) {
                if(CurrentChar == FileUtil._CR_) {
                    EndofLine = true;
                } else if(CurrentChar == FileUtil._LF_) {
                    if(_PreviousChar == FileUtil._CR_) {
                        SkipChar = true;
                    } else {
                        EndofLine = true;
                    }
                }

                _PreviousChar = CurrentChar;

                if(EndofLine) {
                    return Response.toString();
                } else if(SkipChar) {
                    SkipChar = false;
                } else {
                    Response.append((char)CurrentChar);
                }
            }
        } catch (Exception e) {}        
        
        return null;
    }
    /*
	public static void main(String[] args) {
		ReadSettingFile Setting = new ReadSettingFile("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Test 1.stf");
		
		System.out.println(Setting.findArg("First Name"));
		System.out.println(Setting.findArg("Last Name"));
	}
     */
}