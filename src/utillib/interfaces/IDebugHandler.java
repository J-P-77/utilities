package utillib.interfaces;

import utillib.debug.DebugLogger;
import utillib.debug.LogEvent;

public interface IDebugHandler {
//	public String getName();
	
	public void log(LogEvent log, DebugLogger callinglogger);
}
