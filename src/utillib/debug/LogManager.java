package utillib.debug;

import utillib.collections.Collection;

import utillib.interfaces.IDebugHandler;
import utillib.interfaces.IDebugLogger.Log_Level;

/**
 *
 * @author Dalton Dell
 */
public class LogManager implements IDebugHandler {
    //Log Events
//    public static final String _LOG_EVENT_INFORMATION_ = "!!!INFORMATION!!! - ";
//    public static final String _LOG_EVENT_WARNING_ = "!!!WARNING!!! - ";
//    public static final String _LOG_EVENT_ERROR_ = "!!!ERROR!!! - ";
//    public static final String _LOG_EVENT_UNKNOWN_ = "!!!UNKNOWN!!! - ";
    
// The Higher The Number The More Logging
//    Level 5. PLAIN, INFORMATION, WARNING, ERROR and UNKNOWN
//    Level 4. INFORMATION, WARNING, ERROR and UNKNOWN
//    Level 3. WARNING, ERROR and UNKNOWN
//    Level 2. ERROR and UNKNOWN
//    Level 1. UNKNOWN
//    Level 0. NONE(Only Used For No Debugging)

    private Collection<String, IDebugHandler> _HANDLERS = new Collection<String, IDebugHandler>(1);
    private Collection<String, DebugLogger> _LOGGERS = new Collection<String, DebugLogger>(1);
    
    private DebugMsgCount _COUNT = new DebugMsgCount();

    private Log_Level _Global_Level = Log_Level.ALL;

    private LogManager() {}

    public void setLevel(Log_Level level) {
    	_Global_Level = level;
        
//        for(int X = 0; X < _HANDLERS.length(); X++) {
//        	_HANDLERS.getAt(X).setLevel(level);
//        }
    }

    public void setLevel(int level) {
    	final Log_Level[] LEVELS = Log_Level.values();
    	
        for(int X = 0; X < LEVELS.length; X++) {
            if(LEVELS[X].getLevel() == level) {
            	setLevel(LEVELS[X]);
                break;
            }
        }
    }

    private boolean validLevel(Log_Level level) {
    	if(level.getLevel() <= _Global_Level.getLevel()) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Log_Level getLevel() {
        return _Global_Level;
    }
    
    public void addLogHander(String name, IDebugHandler handler) {
        final IDebugHandler HANDLER = _HANDLERS.get(name);

        if(HANDLER == null) {
        	_HANDLERS.add(name, handler);
        }
    }
    
    public DebugLogger getLogger(Class<?> clazz) {
    	DebugLogger Log = _LOGGERS.get(clazz.getCanonicalName());
    	
    	if(Log == null) {
    		Log = new DebugLogger(clazz);
    		
    		_LOGGERS.add(clazz.getCanonicalName(), Log);
    	}
    	
    	return Log;
    }
    
    public DebugLogger getLogger(String name) {
    	DebugLogger Log = _LOGGERS.get(name);
    	
    	if(Log == null) {
    		Log = new DebugLogger(name);
    		
    		_LOGGERS.add(name, Log);
    	}
    	
    	return Log;
    }
    
    
    
    public DebugMsgCount getDebugMsgCount() {
        return _COUNT;
    }

//    @Override
//    public String getName() {
//    	return "Global";
//    }
    
    @Override
    public void log(LogEvent log, DebugLogger callinglogger) {
    	if(validLevel(log.getLevel())) {
    		for(int X = 0; X < _HANDLERS.count(); X++) {
    			_HANDLERS.getAt(X).log(/*nameoflogger, */log, callinglogger);
    		}
    	}
    }
    
	//STATIC INSTANCE
	private static LogManager _Instance = null;

	public static void addSystemHandler() {
		getInstance().addLogHander("System", SystemOutDebugHandler.getInstance());
	}
	
	public static LogManager getInstance() {
		if(_Instance == null) {
			_Instance = new LogManager();
			_Instance.addLogHander("System", SystemOutDebugHandler.getInstance());
        }
        
		return _Instance;
    }
/*
	public static ADebugHandler createInstance() {
        final LogManager INSTANCE = getInstance();

		if(INSTANCE != null) {
			
        }

		return null;
    }
*/
    /*public static void main(String[] args) {
        final LogManager INSTANCE = LogManager.getInstance();

        INSTANCE.addLog(new DefaultDebugHandler("System.out_1", 5));
        INSTANCE.addLog(new DefaultDebugHandler("System.out_2", 3));

        INSTANCE.addLog(new DefaultDebugFileHandler("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Java Tests\\Debug 1.txt", false, 5));
        INSTANCE.addLog(new DefaultDebugFileHandler("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Java Tests\\Debug 2.txt", false, 3));

        INSTANCE.addLog(new DebugWindow("Test1", 5));
        INSTANCE.addLog(new DebugWindow("Test1", 3));

        INSTANCE.open();

        INSTANCE.printInformation("This Is Just A Test");
        INSTANCE.printError("This Is Just A Test");

        utillib.utilities.ThreadUtil.sleep(1000 * 5);

        INSTANCE.close();
    }*/
}
