package utillib.debug;

import utillib.interfaces.IDebugLogger;

public class NullDebugLogger {/*implements IDebugLogger {/*

@Override
public void setLevel(Log_Level level) {}

@Override
public Log_Level getLevel() {
return Log_Level.NONE;
}

@Override
public void printInformation(String msg) {}

@Override
public void printWarning(String msg) {}

@Override
public void printError(String msg) {}

@Override
public void printError(Throwable e) {}

@Override
public void printBlank(String msg) {}

@Override
public void printCustom(String name, String logtype, String msg) {}

@Override
public void open() {}

@Override
public boolean isClosed() {
return false;
}

@Override
public void close() {}


private static NullDebugLogger _Instance = null;

public static IDebugLogger getInstance() {
if(_Instance == null) {
_Instance = new NullDebugLogger();
}

return _Instance;
}*/
}
