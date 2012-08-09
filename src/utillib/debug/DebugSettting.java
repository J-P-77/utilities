package utillib.debug;

/**
 *
 * @author Dalton Dell
 */
public class DebugSettting {
	private  boolean _Insert_Date_And_Time = false;
    private boolean _Write_To_File = false;
    private boolean _Print_To_Debug_Window = false;
    private boolean _Print_To_Output = false;
    private boolean _Show_Window = false;

    private int _DebugLevel = 0;

    public void setDebugLevel(int loglevel) {
        _DebugLevel = loglevel;
    }


    public int getDebugLevel() {
        return _DebugLevel;
    }

    public void enableInsertDateAndTime(boolean value) {
        _Insert_Date_And_Time = value;
    }

    public boolean isEnabledInsertDateAndTime() {
        return _Insert_Date_And_Time;
    }

    public void enableWriteToFile(boolean value) {
        _Write_To_File = value;
    }

    public boolean isEnabledWriteToFile() {
        return _Write_To_File;
    }

    public void enablePrintToDebugWindow(boolean value) {
        _Print_To_Debug_Window = value;
    }

    public boolean isEnabledPrintToDebugWindow() {
        return _Print_To_Debug_Window;
    }

    public void enablePrintToOutput(boolean value) {
        _Print_To_Output = value;
    }

    public boolean isEnabledPrintToOutput() {
        return _Print_To_Output;
    }

    public void enableShowWindow(boolean value) {
        _Show_Window = value;
    }

    public boolean isEnabledShowWindow() {
        return _Show_Window;
    }
}
