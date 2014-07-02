/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
