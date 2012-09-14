package utillib.debug;

import utillib.interfaces.IDebugLogger;

/**
 * 
 * @author Justin Palinkas
 */
public class DebugLogger implements IDebugLogger {
	private final Class<?> _CLASS;

	private Log_Level _Level = Log_Level.ALL;

	private final String _NAME;

//    private DebugLogger parent = null;

	public DebugLogger(String name) {
		this(name, Log_Level.ALL);
	}

	public DebugLogger(String name, Log_Level level) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;
		_CLASS = null;
	}

	public DebugLogger(Class<?> clazz) {
		this(clazz, Log_Level.ALL);
	}

	public DebugLogger(Class<?> clazz, Log_Level level) {
		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		_NAME = null;
		_CLASS = clazz;
	}

	@Override
	public void setLevel(Log_Level level) {
		_Level = level;
	}

	@Override
	public Log_Level getLevel() {
		return _Level;
	}

	public String getName() {
		return (_NAME == null ? _CLASS.getCanonicalName() : _NAME);
	}

	public Class<?> getClazz() {
		return _CLASS;
	}

	@Override
	public void printBlank(String msg) {
//        _COUNT.addOther();

		if(validLevel(Log_Level.NONE)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.NONE, msg), this);
		}
	}

	@Override
	public void printCustom(String name, String logtype, String msg) {
//        _COUNT.addOther();

		if(validLevel(Log_Level.NONE)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.NONE, logtype + msg), this);
		}
	}

	public void printInformation(String msg) {
//        _COUNT.addInformation();

		if(validLevel(Log_Level.INFORMATION)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.INFORMATION, msg), this);
		}
	}

	public void printWarning(String msg) {
//        _COUNT.addWarning();

		if(validLevel(Log_Level.WARNING)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.WARNING, msg), this);
		}
	}

	public void printError(String msg) {
//        _COUNT.addError();

		if(validLevel(Log_Level.ERROR)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.ERROR, msg), this);
		}
	}

	public void printError(Throwable e) {
//        _COUNT.addError();

		if(validLevel(Log_Level.ERROR)) {
			LogManager.getInstance().log(new LogEvent(Log_Level.ERROR, "", e), this);
		}
	}

	private boolean validLevel(Log_Level level) {
		if(level.getLevel() <= _Level.getLevel()) {
			return true;
		}

		return false;
	}

//    protected void print(Log_Level level, String premsg, String msg) {
//    	if(validLevel(level)) {
//    		System.out.println("["  + _NAME + "] " + premsg + msg);
//    	}
//    }
//    
//    protected void print(Log_Level level, String msg) {
//    	if(validLevel(level)) {
//    		System.out.println("["  + _NAME + "] " + msg);
//    	}
//    }
}
