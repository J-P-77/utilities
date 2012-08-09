package utillib.debug;

import utillib.interfaces.IDebugLogger.Log_Level;

/**
 *  Log Start Message String (ex. "!!!ERROR!!! - ") - (LogEvent) + (Log Message)
 * @author Dalton Dell
 */
public class LogEvent {
	private final long _TIME;
	private final Log_Level _LEVEL;
    private final String _MSG;
    
//    private final Class<?> _CLASS;
    private final Throwable _THROWABLE;
    
    public LogEvent(Log_Level level, String msg) {
    	this(level, msg, null/*, null*/);
    }
    
//    public LogEvent(Log_Level level, String msg, Class<?> clazz) {
//    	this(level, msg, clazz, null);
//    }
    
    public LogEvent(Log_Level level, String msg,/* Class<?> clazz,*/ Throwable throwable) {
//    	_CLASS = clazz;
    	_TIME = System.currentTimeMillis();
    	_MSG = msg;
    	_LEVEL = level;
    	_THROWABLE = throwable;
    }

    public long getTime() {return _TIME;}
    public Log_Level getLevel() {return _LEVEL;}
    public String getMsg() {return _MSG;}
    public Throwable getThrowable() {return _THROWABLE;}
//    public Class<?> getClazz() {return _CLASS;}
}
