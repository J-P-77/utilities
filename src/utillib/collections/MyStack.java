package utillib.collections;

/**
 * <pre>
 * <b>Current Version 1.0.3</b>
 * 
 * Version 1.0.0
 *     -First Released
 * 
 * Version 1.0.1
 *     -Fixed
 *         -Method Push (Non-Growing Stack Only)
 *              (Pushing Object When Stack Is Full Index Out Of Bounds Error Would Occur)
 * 
 * November 02, 2008 (version 1.0.2)
 *     -Added
 *         -increaseCapacity(int increaseby)
 * 
 * November 07, 2008 (version 1.0.3)
 *     -Added
 *         -Method Push(T[] i) (Pushes An Array of Objects)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@SuppressWarnings("unchecked")
public class MyStack<T> {
	protected Object[] _Arr = null;;
	protected int _Capacity = 1;
	protected int _Top = 0;
	protected int _IncreaseBy = 5;
	protected boolean _Grow = true;

	/**
	 * Simple Stack
	 */
	public MyStack() {
		this(1);
	}

	/**
	 * Simple Stack
	 * 
	 * @param initialobject
	 *            - (T) Pushes Initial Object Into Stack
	 * 
	 */
	public MyStack(T initialobject) {
		this(initialobject, 1);
	}

	/**
	 * Simple Stack
	 * 
	 * @param initialcapacity
	 *            - (int) Sets Initial Capacity for Stack
	 * 
	 */
	public MyStack(int initialcapacity) {
		if(initialcapacity <= 0) {
			_Capacity = 1;
		} else {
			_Capacity = initialcapacity;
		}

		_Top = 0;
		_IncreaseBy = 5;
		_Grow = true;

		_Arr = new Object[_Capacity];
	}

	/**
	 * Simple Stack
	 * 
	 * @param initialcapacity
	 *            - (int) Sets Initial Capacity for Stack
	 * @param initialobject
	 *            - (T) Pushes Initial Object Into Stack
	 * 
	 */
	public MyStack(T initialobject, int initialcapacity) {
		this(initialcapacity);
		push(initialobject);
	}

	/**
	 * Simple Stack (Used for Non-Growing Stack)
	 * 
	 * @param maxcapacity
	 *            - (int) Sets Max Capacity for Stack
	 * @param grow
	 *            - (boolean) Sets Whether The Stack Will Grow (Used false for
	 *            non-growing stack)
	 * 
	 */
	public MyStack(int maxcapacity, boolean grow) {
		this(maxcapacity);
		_Grow = grow;
	}

	public synchronized void push(T i) {
		if(_Top == _Capacity) {
			if(_Grow) {
				increaseCapacity();
				_Arr[_Top++] = i;
			}
		} else {
			_Arr[_Top++] = i;
		}
	}

	public synchronized void push(T[] i) {
		for(int X = 0; X < i.length; X++) {
			push(i[X]);
		}
	}

	public synchronized T pop() {
		if(!isEmpty()) {
			T temp = (T)_Arr[--_Top];
			_Arr[_Top] = null;

			return temp;
		} else {
			return null;
		}
	}

	public synchronized boolean isEmpty() {
		if(_Arr == null) {
			return true;
		}

		if(_Top == 0) {
			return true;
		}

		return false;
	}

	public synchronized boolean isFull() {
		if(_Arr == null) {
			return true;
		} else {
			if(_Top == _Capacity) {
				return true;
			} else {
				return false;
			}
		}
	}

	public synchronized void setIncreaseBy(int value) {
		if(value <= 0) {
			_IncreaseBy = 1;
		} else {
			_IncreaseBy = value;
		}
	}

	public synchronized int getIncreaseBy() {
		return _IncreaseBy;
	}

	public Object[] toArray() {
		Object[] Temp = new Object[_Top];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Arr[X];
		}

		return Temp;
	}

	public void toArray(T[] array) {
		if(array == null) {
			throw new NullPointerException("Variable[array] - Is Null");
		} else {
			int Counter = 0;
			int Length = _Top;

			while(Counter < array.length && Counter < Length) {
				array[Counter] = (T)_Arr[Counter];
				Counter++;
			}

		}
	}

	protected synchronized void increaseCapacity() {
		increaseCapacity(_IncreaseBy);
	}

	protected synchronized void increaseCapacity(int increaseby) {
		_Capacity += increaseby;

		Object[] Temp = new Object[_Capacity];

		for(int X = 0; X < _Top; X++) {
			Temp[X] = _Arr[X];
		}
		_Arr = null;
		_Arr = Temp;
	}

	protected synchronized boolean validIndex(int index) {
		if(index >= 0 && index < _Top) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * public static void main(String[] args) { MyStack<Integer> TempInt = new
	 * MyStack<Integer>();
	 * 
	 * TempInt.push(0); TempInt.push(1); TempInt.push(2); TempInt.push(3);
	 * TempInt.push(4); TempInt.push(5); TempInt.push(6); TempInt.push(7);
	 * 
	 * TempInt.toArray(); }
	 */
}

/*
 * public synchronized void PushtoBeginning(T i) { Object[] Temp = CopyArray();
 * int End = _Top;
 * 
 * Erase(); Push(i);
 * 
 * for(int X = 0; X < End; X++) { Push((T)Temp[X]); } }
 */

/*
 * public synchronized void PushtoEnd(T i) { push(i); }
 * 
 * public synchronized T PopfromBeginning() { if(!isEmpty()) { T temp =
 * (T)_Arr[0]; removeAt(0);
 * 
 * return temp; } else { return null; } }
 * 
 * public synchronized T PopfromEnd() { return pop(); }
 */

/*
 * private synchronized void IncreaseCapacity() { Object[] Temp = CopyArray();
 * 
 * _Capacity += _IncreaseBy;
 * 
 * _Arr = new Object[_Capacity];
 * 
 * for(int X = 0; X < _Top; X++) { _Arr[X] = Temp[X]; } }
 * 
 * private synchronized Object[] CopyArray() { Object[] Temp = new
 * Object[_Arr.length];
 * 
 * for(int X = 0; X < _Arr.length; X++) { Temp[X] = _Arr[X]; }
 * 
 * return Temp; }
 */

/*
 * private synchronized void Move(int removeindex) { _Arr[removeindex] = null;
 * 
 * for(int X = (removeindex + 1);X < (_Top); X++) { _Arr[removeindex] = _Arr[X];
 * removeindex++; }
 * 
 * for(int Y = removeindex; Y < (_Top); Y++) { _Arr[Y] = null; }
 * 
 * _Top = removeindex; }
 */