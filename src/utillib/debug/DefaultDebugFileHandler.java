package utillib.debug;

import utillib.interfaces.IDebugLogger.Log_Level;
import utillib.strings.MyStringBuffer;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public class DefaultDebugFileHandler /*extends DebugLogger */{
    private DebugOutputStream _OStream = null;
    
    private final File _FILE;
    private final boolean _APPEND;
    
    public DefaultDebugFileHandler(String path) {
        this(new File(path), false);
    }

    public DefaultDebugFileHandler(String path, boolean append) {
        this(new File(path), append);
    }

    public DefaultDebugFileHandler(File file) {
        this(file, false);
    }

    public DefaultDebugFileHandler(File file, boolean append) {
//        super(file.getName());

        _FILE = file;
        _APPEND = append;
    }    
    
//    public void printLog(Log log, String msg) {
//        _OStream.write(log.toString() + " - ");
//        _OStream.writeln(msg);
//    }
//
//    public void printLog(LogEvent log, String msg) {
//        _OStream.write(log.getLogMsgStr());
//        _OStream.writeln(msg);
//    }
//
//    public void printLog(LogLevelEvent log, String msg) {
//        _OStream.write(log.getLogMsgStr());
//        _OStream.writeln(msg);
//    }

//    @Override
    public void printBlank(String msg) {
        _OStream.write(msg);
        _OStream.newline();
    }
    
//    @Override
    public void printCustom(String name, String logtype, String msg) {
    	_OStream.write("[" + name + "] ");
        _OStream.write(logtype);
        _OStream.write(msg);
        _OStream.newline();
        
    }

//    public void printPlain(String msg) {
//    	if(super.validLevel(Log_Levels.WARNING)) {
//    		_OStream.writeln(msg);
//    	}
//    }

//    public void printInformation(String msg) {
//    	if(super.validLevel(Log_Level.INFORMATION)) {
//    		_OStream.write(LogManager._LOG_EVENT_INFORMATION_);
//        	_OStream.writeln(msg);
//    	}
//    }
//
//    public void printWarning(String msg) {
//    	if(super.validLevel(Log_Level.WARNING)) {
//    		_OStream.write(LogManager._LOG_EVENT_WARNING_);
//    		_OStream.writeln(msg);
//    	}
//    }

//    public void printUnknown(String msg) {
//        _OStream.write(LogManager._LOG_EVENT_UNKNOWN_);
//        _OStream.writeln(msg);
//    }

//    public void printError(String msg) {
//    	if(super.validLevel(Log_Level.ERROR)) {
//    		_OStream.write(LogManager._LOG_EVENT_ERROR_);
//    		_OStream.writeln(msg);
//    	}
//    }
//
//    public void printError(Throwable e) {
//    	if(super.validLevel(Log_Level.ERROR)) {
//	        System.out.println(LogManager._LOG_EVENT_ERROR_ + " - Cause: " + e.getMessage());
//	
//	        StackTraceElement[] Trace =  e.getStackTrace();
//	
//	        MyStringBuffer Buffer = new MyStringBuffer(50);
//	
//	        for(int X = 0; X < Trace.length; X++) {
//	            Buffer.append("    ");
//	
//	            DebugUtil.createTrace(Trace[X], Buffer);
//	
//	            _OStream.writeln(Buffer.toString());
//	
//	            Buffer.reset();
//	        }
//    	}
//    }

//    public void setAppend(boolean value) {
//        if(isOpen()) {
//            if(_OStream.isAppending() != value) {
//                _IsAppending = value;
//                _OStream.reOpen(_OStream.getFile(), value);
//            }
//        } else {
//            _IsAppending = value;
//        }
//    }
//
//    public boolean isAppending() {
//        return _IsAppending;
//    }

//    @Override
    public void open() {
        if(isClosed()) {
            try {
            	_OStream = new DebugOutputStream(_FILE, _APPEND);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
    }

    public boolean isClosed() {
        if(_OStream == null) {
            return true;
        }
        
        return _OStream.isClosed();
    }

    public void close() {
        if(_OStream != null) {
        	try {
        		_OStream.close();
			} catch (Exception e) {}
			_OStream = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        
        super.finalize();
    }
}
