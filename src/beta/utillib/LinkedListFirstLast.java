package beta.utillib;

public class LinkedListFirstLast<T> {//Don't Use
	private Node _First = null;
	private Node _Last = null;//_Top
	private int _Length = 0;

    public void pushFirst(T i) {
        final Node TEMP = new Node();
        TEMP._Value = i;

        if(isEmpty()) {
            _First = TEMP;
            _Last = TEMP;
        } else {
            _First._Previous = TEMP;
            TEMP._Next = _First;
            _First = TEMP;
        }
        _Length++;
    }

    public void pushLast(T i) {
        final Node TEMP = new Node();
        TEMP._Value = i;

        if(isEmpty()) {
            _First = TEMP;
            _Last = TEMP;
        } else {
            _Last._Next = TEMP;
            TEMP._Previous = _Last;
            _Last = TEMP;
        }
        _Length++;
    }

    public T popFirst() {
        final T TEMP = _First._Value;

        _First = _First._Next;
        _Length--;

        if(_First == null) {
        	_Last = null;
        }
        
        return TEMP;
    }

    public T popLast() {
        final T TEMP = _Last._Value;

        _Last = _Last._Previous;
        _Length--;

        if(_Last == null) {
        	_First = null;
        }
        
        return TEMP;
    }

//	public void push(T i) {
//		final Node TEMP = new Node();
//		TEMP._Value = i;
//		TEMP._Next = _Last;
//		
//		_Last = TEMP;
//		
//		_Length++;
//	}
//
//	public T pop() {
//		final T TEMP = _Last._Value;
//
//		_Last = _Last._Next;
//		_Length--;
//
//		return TEMP;
//	}

    public boolean isEmpty() {
        return _Last == null && _First == null;
    }

	private class Node {
		public T _Value = null;
		public Node _Next = null;
        public Node _Previous = null;
	}


}