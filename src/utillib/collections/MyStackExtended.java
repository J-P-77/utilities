package utillib.collections;

/**<pre>
 * <b>Current Version 1.0.4</b>
 * 
 * Version 1.0.0
 *     -First Released
 * 
 * Version 1.0.1
 *     -Fixed Bug
 *         -Method Flip (Would Not Flip Properly)
 * 
 * November 02, 2008 (version 1.0.2)
 *     -Added
 *         -Method insertItemAt(T i, int index)
 *         -Method insertItemAt(T[] i, int index)
 * 
 * November 07, 2008 (version 1.0.3)
 *     -Added
 *         -Method validIndex(int index)
 * 
 * November 29, 2008 (version 1.0.4)
 *     -Added
 *         -Method popItemAt(int index) (Gets And Removes Item)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@SuppressWarnings("unchecked")
public class MyStackExtended<T> extends MyStack<T> {
	/**
	  * Simple Stack\Collection
	  */
	public MyStackExtended() {
		super(1);
	}

	/**
	  * Simple Stack\Collection
	  * 
	  * @param initialobject - (T) Pushes Initial Object Into Stack
	  *
	  */
	public MyStackExtended(T initialobject) {
		super(initialobject, 1);
	}

	/**
	  * Simple Stack\Collection
	  * 
	  * @param initialcapacity - (int) Sets Initial Capacity for Stack
	  *
	  */
	public MyStackExtended(int initialcapacity) {
		super(initialcapacity);
	}

	/**
	  * Simple Stack\Collection
	  * 
	  * @param initialcapacity - (int) Sets Initial Capacity for Stack
	  * @param initialobject - (T) Pushes Initial Object Into Stack
	  *
	  */
	public MyStackExtended(T initialobject, int initialcapacity) {
		super(initialobject, initialcapacity);
	}

	/**
	  * Simple Stack\Collection (Used for Non-Growing Stack)
	  * 
	  * @param maxcapacity - (int) Sets Max Capacity for Stack
	  * @param grow - (boolean) Sets Whether The Stack Will Grow (Used false for non-growing stack)
	  *
	  */
	public MyStackExtended(int maxcapacity, boolean grow) {
		super(maxcapacity, grow);
	}

	public synchronized void flipStack() {
		T Temp = null;
		int Counter = 0;
		int X = (_Top - 1);

		for(; X > Counter;) {
			Temp = (T)_Arr[Counter];
			_Arr[Counter] = null;
			_Arr[Counter] = _Arr[X];
			_Arr[X] = null;
			_Arr[X] = Temp;
			X--;
			Counter++;
		}
	}

	public synchronized T getItemAt(int index) {
		if(validIndex(index)) {
			return (T)_Arr[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void setItemAt(T i, int index) {
		if(validIndex(index)) {
			_Arr[index] = i;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void removeItemAt(int index) {
		if(validIndex(index)) {
			erase(index);

			for(int X = (index + 1); X < (_Top); X++) {
				_Arr[index++] = _Arr[X];
			}

			_Arr[_Top - 1] = null;

			_Top = index;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void insertItemAt(T i, int index) {
		if(validIndex(index)) {
			if(_Grow) {
				if(_Top == _Capacity) {
					increaseCapacity();
				}

				for(int X = (_Top - 1); X >= index; X--) {
					T Temp = (T)_Arr[X];
					_Arr[X] = null;
					_Arr[X + 1] = Temp;
				}

				setItemAt(i, index);
				_Top++;
			}
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized void insertItemAt(T[] i, int index) {
		if(validIndex(index)) {
			if(_Grow) {
				if((_Top + i.length - 1) >= _Capacity) {
					increaseCapacity((_Top + i.length - 1));
				}

				for(int X = (_Top - 1); X >= index; X--) {
					T Temp = (T)_Arr[X];
					_Arr[X] = null;
					_Arr[X + i.length] = Temp;
				}

				_Top += i.length;

				for(int X = 0; X < i.length; X++, index++) {
					setItemAt(i[X], index);
				}
			}
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized T popItemAt(int index) {
		if(validIndex(index)) {
			T TTemp = getItemAt(index);

			removeItemAt(index);

			return TTemp;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public synchronized int length() {
		return (_Top);
	}

	@Override
	public synchronized boolean validIndex(int index) {
		return super.validIndex(index);
	}

	private synchronized void erase(int index) {
		if(validIndex(index)) {
			_Arr[index] = null;
		}
	}

	public synchronized void eraseAll() {
		_Top = 0;

		for(int X = 0; X < _Arr.length; X++) {
			_Arr[X] = null;
		}
	}
}
