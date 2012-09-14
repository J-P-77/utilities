package beta.utillib;

/**
 * 
 * @author Dalton Dell
 */
public class ByteQueue<T> {
	private Node _First = null;
	private Node _Last = null;

	private int _Length = 0;

	public ByteQueue() {}

	public ByteQueue(byte initial) {
		push(initial);
	}

	public void push(byte i) {
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

	public byte pop() {
		final byte TEMP = _First._Value;

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

	private class Node {
		public byte _Value = 0;
		public Node _Next = null;
	}
}
