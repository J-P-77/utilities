package beta.utillib;

import utillib.arrays.ResizingArray;

/**
 *
 * @author Dalton Dell
 */
public class Listener<T> {
	protected final Object __LOCK__ = new Object();

    protected final ResizingArray<T> _LISTENERS;
    
    public Listener() {
    	_LISTENERS = new ResizingArray<T>(0, 1);
    }

    public Listener(int initalsize) {
    	this(initalsize, 1);
    }
    
    public Listener(int initalsize, int increaseby) {
    	_LISTENERS = new ResizingArray<T>(initalsize, increaseby);
    }
    
    public void addListener(T listener) {
    	if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}		
    	
    	if(!listenerExists(listener)) {
    		synchronized(__LOCK__) {
    			_LISTENERS.put(listener);
    		}
    	}
    }

    public T getListenerAt(int index) {
    	synchronized(__LOCK__) {
	    	if(_LISTENERS.validIndex(index)) {
	    		return _LISTENERS.getAt(index);
	    	}
    	}
    	
    	return null;
    }
    
    public void removeListener(int index) {
        synchronized(__LOCK__) {
            if(_LISTENERS.validIndex(index)) {
            	_LISTENERS.removeAt(index);
            }
        }
    }
    
    public void removeListener(T listener) {
    	if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}
    	
        synchronized(__LOCK__) {
        	_LISTENERS.removeAll(listener);
        }
    }

    public int listenerCount() {
        synchronized(__LOCK__) {
            return _LISTENERS.length();
        }
    }
    
	public boolean listenerExists(T listener) {
    	if(listener == null) {
			throw new RuntimeException("Variable[listener] - Is Null");
		}
    	
		synchronized(__LOCK__) {
			for(int X = 0; X < _LISTENERS.length(); X++) {
				if(listener.equals(_LISTENERS.getAt(X))) {
					return true;
				}
			}
		}

		return false;
	}
}
