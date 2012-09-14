package utillib.debug;

import utillib.file.FileUtil;
import utillib.interfaces.IDebugHandler;
import utillib.interfaces.IDebugLogger;

import utillib.strings.MyStringBuffer;

/**
 * 
 * @author Justin Palinkas
 */
public class SystemOutDebugHandler implements IDebugHandler {
//	private final String _NAME;
//	
//    public SystemDebugLogger(String name) {
//        if(name == null) {
//			throw new RuntimeException("Variable[name] - Is Null");
//		}
//        
//        _NAME = name;
//    }
//    
//    @Override
//    public String getName() {
//    	return _NAME;
//    }

	public void log(/* String nameoflogger, */LogEvent log, DebugLogger callinglogger) {
		final MyStringBuffer BUFFER = new MyStringBuffer(50);

		BUFFER.append('[');
		BUFFER.append(callinglogger.getName());
		BUFFER.append(']');
		BUFFER.append(' ');

		switch(log.getLevel()) {
			case INFORMATION:
				BUFFER.append(IDebugLogger._LOG_EVENT_INFORMATION_);
				break;

			case ERROR:
				BUFFER.append(IDebugLogger._LOG_EVENT_ERROR_);
				break;

			case WARNING:
				BUFFER.append(IDebugLogger._LOG_EVENT_UNKNOWN_);
				break;

			case NONE:
			case ALL:
		}

		BUFFER.append(log.getMsg());

		if(log.getThrowable() != null) {
			if(log.getThrowable().getMessage() != null) {
				BUFFER.append(log.getThrowable().getMessage());
				BUFFER.append(FileUtil.Line_Ending.getDefaultLineEnding().getValue());
			}

			final StackTraceElement[] TRACE = log.getThrowable().getStackTrace();

			for(int X = 0; X < TRACE.length; X++) {
				BUFFER.append("    ");

				DebugUtil.createTrace(TRACE[X], BUFFER);
				BUFFER.append(FileUtil.Line_Ending.getDefaultLineEnding().getValue());
			}
		}

		System.out.println(BUFFER.toString());
	};

	//STATIC INSTANCE
	private static IDebugHandler _Instance = null;

	public static IDebugHandler getInstance() {
		if(_Instance == null) {
			_Instance = new SystemOutDebugHandler(/* "System" */);
		}

		return _Instance;
	}

}
