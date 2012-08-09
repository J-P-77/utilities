package utillib.debug;

import java.text.DecimalFormat;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 *     
 * September 12, 2011 (Version 1.0.0)
 *     -Added
 *        -Counter (Keeps Track of Past Time)
 *     
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class StopWatch {    
    public enum Mode {
        SECONDS,
        MINUTES,
        HOURS;
    };
    
    private long _Start = 0;
    private long _Stop = 0;
    
    private long _Counter = 0;
    
    private boolean _IsRunning = false;
    
    public StopWatch() {
        this(false);
    }
    
    public StopWatch(boolean autostart) {
        if(autostart) {
            start();
        }
    }
    
    public void start() {
        if(!_IsRunning) {
            _IsRunning = true;
            _Stop = 0;
            _Start = System.currentTimeMillis();
        }
    }
    
    public void stop() {
        if(_IsRunning) {
            _Stop = System.currentTimeMillis();
            _Counter += _Stop;
            _IsRunning = false;
        }
    }
    
    public void reset() {
        _Start = 0;
        _Stop = 0;
        _Counter = 0;
        _IsRunning = false;
    }
    
    public String resultStr() {
        if(_IsRunning) {stop();}
        return (Double.toString(_Stop - _Start));
    }
    
    public boolean isRunning() {
        return _IsRunning;
    }
    
    public long getCounter() {
    	return _Counter;
    }
    
	public long result() {
		if(_IsRunning) {
            stop();
        }

		return _Stop - _Start;
	}
    
    public double resultInSeconds() {
        if(_IsRunning) {
            stop();
        }
        
        final long RESULTS = _Stop - _Start;
//        double Seconds = RESULTS * 0.001;//1000.0;
        
        return (RESULTS * 0.001);
    }
    
    public double resultInMinutes() {
        if(_IsRunning) {
            stop();
        }
        
        return resultInSeconds() / 60;
    }
    
    public double resultInHours() {
        if(_IsRunning) {
            stop();
        }

        return resultInMinutes() / 60;
    }
	
    public String resultsln(Mode mode) {
        final DecimalFormat FORMAT = new DecimalFormat("######.###");

        switch(mode) {
            case MINUTES:
                return FORMAT.format(resultInMinutes()) + " Minute(s)";
            case HOURS:
                return FORMAT.format(resultInHours()) + " Hour(s)";
             case SECONDS:
            default:
                return FORMAT.format(resultInSeconds()) + " Second(s)";
        }
    }
        
    @Override
    public String toString() {
        if(_IsRunning) {
            return "Start: " + _Start;
        } else {
            return "Start: " + _Start + " Stop: " + _Stop;
        }
    }
/*
	public static void main(String[] args) {
		StopWatch Watch = new StopWatch();
		
		Watch.start();
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
		
		System.out.println(Watch.resultInSeconds());
	}
*/
}