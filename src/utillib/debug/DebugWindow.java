package utillib.debug;

import utillib.dialogs.FrmTextBox;

import utillib.interfaces.IGarbageUpdater;
import utillib.interfaces.IDebugLogger;
import utillib.interfaces.IDebugLogger.Log_Level;

import utillib.strings.MyStringBuffer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * February 26, 2010 (Version 1.0.1)
 *     -Updated
 *         -Everything
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DebugWindow /*extends DebugLogger implements IDebugLogger*/{
	private FrmTextBox _Frm = null;
	private GarbageCollectorUpdater _Status = null;
	private boolean _InsertDateAndTime = false;

	public DebugWindow() {
		this("Log");
	}

	public DebugWindow(String title) {
		this(title, Log_Level.ALL);
	}

	public DebugWindow(String title, Log_Level level) {
//    	super(title, level);

		_Frm = new FrmTextBox(title);

		initComponents();
	}

	private void initComponents() {
		_Status = new GarbageCollectorUpdater(false, false, new IGarbageUpdater() {
			public void updateAll(String value) {
				_Frm.getStatusTextField().setText(value);
			}

			public void updateFreeMemory(long value) {}

			public void updateMaxMemory(long value) {}

			public void updateTotalMemory(long value) {}
		});

		bulidOptionsMenu();

		_Frm.addFileFilter("Text File", "txt");

		_Status.start();
	}

	private void bulidOptionsMenu() {
		mnuOptions = new JMenu("Options");

		mnuOptionGc = new JMenuItem("GC");
		mnuOptionGc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				GarbageCollectorUpdater.collectTrash();
			}
		});
		mnuOptions.add(mnuOptionGc);

		_Frm.getMainMenu().add(mnuOptions);
	}

	public void setStatusText(String str) {
		_Frm.getStatusTextField().setText(str);
	}

	public String getStatusText() {
		return _Frm.getStatusTextField().getText();
	}

	public void setInsertDateAndTime(boolean value) {
		_InsertDateAndTime = value;
	}

	public boolean getInsertDateAndTime() {
		return _InsertDateAndTime;
	}

	public FrmTextBox getFrame() {
		return _Frm;
	}

//    @Override
	public void printBlank(String msg) {
		_Frm.println(msg);
	}

//    @Override
	public void printCustom(String name, String logtype, String msg) {
		if(_InsertDateAndTime) {
			_Frm.println("[" + name + "] " + DebugUtil.getDateAndTime() + msg);
		} else {
			_Frm.print("[" + name + "] ");
			_Frm.print(logtype);
			_Frm.println(msg);
		}
		_Frm.setCaretPosition(_Frm.getTextLength());
	}

//    public void printLog(Log log, String msg) {
//        if(_InsertDateAndTime) {
//            _Frm.println(DebugUtil.getDateAndTime() + ' ' + log.toString() + ' ' + msg);
//        } else {
//            _Frm.println(log.toString() + ' ' + msg);
//        }
//        _Frm.setCaretPosition(_Frm.getTextLength());
//    }
//
//    public void printLog(LogEvent log, String msg) {
//        if(_InsertDateAndTime) {
//            _Frm.println(DebugUtil.getDateAndTime() + ' ' + log.getLogMsgStr() + msg);
//        } else {
//            _Frm.println(log.getLogMsgStr() + ' ' + msg);
//        }
//        _Frm.setCaretPosition(_Frm.getTextLength());
//    }
//
//    public void printLog(LogLevelEvent log, String msg) {
//        if(_InsertDateAndTime) {
//            _Frm.println(DebugUtil.getDateAndTime() + ' ' + log.getLogMsgStr() + msg);
//        } else {
//            _Frm.println(log.getLogMsgStr() + ' ' + msg);
//        }
//        _Frm.setCaretPosition(_Frm.getTextLength());
//    }

//    public void printPlain(String msg) {
//        if(_InsertDateAndTime) {
//            _Frm.println(DebugUtil.getDateAndTime() + msg);
//        } else {
//            _Frm.println(msg);
//        }
//        _Frm.setCaretPosition(_Frm.getTextLength());
//    }

/*
    public void printInformation(String msg) {
    	if(super.validLevel(Log_Level.INFORMATION)) {
    		printLog(LogManager._LOG_EVENT_INFORMATION_ + msg);
    	}
    }

    public void printWarning(String msg) {
    	if(super.validLevel(Log_Level.WARNING)) {
    		printLog(LogManager._LOG_EVENT_WARNING_ + msg);
    	}
    }

//    public void printUnknown(String msg) {
//    	if(super.validLevel(Log_Levels.INFORMATION)) {
//    		printLog(LogManager._LOG_EVENT_UNKNOWN_ + msg);
//    	}
//    }

    public void printError(String msg) {
    	if(super.validLevel(Log_Level.ERROR)) {
        	printLog(LogManager._LOG_EVENT_ERROR_ + msg);
    	}
    }

    public void printError(Throwable e) {
    	if(super.validLevel(Log_Level.ERROR)) {
	        printError("Cause: " + e.getMessage());
	
	        StackTraceElement[] Trace =  e.getStackTrace();
	
	        MyStringBuffer Buffer = new MyStringBuffer(50);
	
	        for(int X = 0; X < Trace.length; X++) {
	            Buffer.append("    ");
	
	            DebugUtil.createTrace(Trace[X], Buffer);
	
	            _Frm.println(Buffer.toString(true));
	        }
	        _Frm.setCaretPosition(_Frm.getTextLength());
    	}
    }

    @Override
    public void open() {
        _Frm.setVisible(true);
    }

    @Override
    public boolean isClosed() {
        return !_Frm.isDisplayable() || !_Frm.isVisible();
    }

    @Override
    public void close() {
        _Frm.dispose();
    }
	
    private void printLog(String value) {
        if(_InsertDateAndTime) {
            _Frm.println(DebugUtil.getDateAndTime() + value);
        } else {
            _Frm.println(value);
        }
        _Frm.setCaretPosition(_Frm.getTextLength());
    }
    */
	//File Options
	private JMenu mnuOptions;
	//private JMenuItem mnuOptionDirectory;
	//private JSeparator sep2;
	private JMenuItem mnuOptionGc;
}
//        ProcessBuilder Pb = new ProcessBuilder("C:\\Documents and Settings\\Dalton Dell\\Desktop\\
//                                Justin's\\Visual Basic\\VB 2005 Projects\\Backup\\bin\\Debug\\Backup.exe");
//        try {Pb.start();} catch (Exception e) {}
