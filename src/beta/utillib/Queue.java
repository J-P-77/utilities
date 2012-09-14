package beta.utillib;

/**
 * 
 * @author Dalton Dell
 */
public class Queue<T> {
	protected Node _First = null;
	protected Node _Last = null;

	protected int _Length = 0;

	public Queue() {}

	public Queue(T initial) {
		push(initial);
	}

	public void push(T i) {
		final Node TEMP = new Node();
		TEMP._Value = i;

		if(_Last == null) {
			_First = TEMP;
			_Last = TEMP;
		} else {
			_Last._Next = TEMP;
			_Last = TEMP;
		}
		_Length++;
	}

	public T pop() {
		final T TEMP = _First._Value;

		_First = _First._Next;
		_Length--;

		if(_First == null) {
			_Last = null;
		}

		return TEMP;
	}

	public boolean isEmpty() {
		return _Length == 0;
	}

	public int length() {
		return _Length;
	}

	protected class Node {
		public T _Value = null;
		public Node _Next = null;

		@Override
		public String toString() {
			return _Value.toString();
		}
	}
}
