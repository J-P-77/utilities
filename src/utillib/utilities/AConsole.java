package utillib.utilities;

import utillib.io.MyInputStream;
import utillib.io.MyOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

/**<pre>
 * <b>Current Version 1.0.0<b/>
 *
 * December 05, 2009 (Version 1.0.0)
 *     -First Released
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public abstract class AConsole implements Runnable {	
    private MyOutputStream _OStream = null;
    private MyInputStream _IStream = null;
    
    public AConsole() {
        this(System.in, System.out);
    }

    public AConsole(InputStream istream, OutputStream ostream) {
    	if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}
    	
    	if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}    	
    	
        _IStream = new MyInputStream(istream);
        _OStream = new MyOutputStream(ostream);
    }

    public void run() {
        try {
        	while(!isClosed()) {
        		String Line = null;
	            while((Line = _IStream.readln()) != null) {
            		processLine(Line);
	            }
        	}
        	
        	close();
        } catch (Exception e) {
        	if(!isClosed()) {
        		e.printStackTrace();
        	}
        }
    }
    
    public void close() {
		if(_IStream != null) {
			try {
				_IStream.close();
			} catch(Exception e) {}
			_IStream = null;
		}
		if(_OStream != null) {
			try {
				_OStream.close();
			} catch(Exception e) {}
			_OStream = null;
		}
    }

    public boolean isClosed() {
        return (_IStream == null && _OStream == null);
    }

    public void write(String value) {
        try {
            _OStream.writeString(value);
        } catch (Exception e) {}
    }

    public void writeln(String value) {
        try {
            _OStream.writeStringln(value);
        } catch (Exception e) {}
    }

    public MyOutputStream getMyOutputStream() {
        return _OStream;
    }

    @Override
    protected void finalize() throws Throwable {
    	close();

        super.finalize();
    }

    public abstract void processLine(String line);

    public static void main(String[] args) {
    	final AConsole CONSOLE = new AConsole() {
            @Override
            public void processLine(String line) {
                if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                    super.close();
                } else {
                    System.out.println("Echo " + line);
                }
            }
        };
        CONSOLE.run();
    }

}
