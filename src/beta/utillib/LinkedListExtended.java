package beta.utillib;

import java.util.Iterator;

public class LinkedListExtended<T> extends LinkedList<T> {

	public boolean exists(T i) {
		Node Top = _Top;
		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
				return true;
			}

			Top = Top._Next;
		}

		return false;
	}

	public T pop(T i) {
		Node Top = _Top;
		Node Pre = null;

		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
				final T TEMP = Top._Value;

//				System.out.println("equals");
				if(Pre == null) {
					_Top = Top._Next;
				} else {
					Pre._Next = Top._Next;
				}
//				_Length--;

				return TEMP;
			}

			Pre = Top;
			Top = Top._Next;
		}

		return null;
	}

	public void remove(T i) {
		Node Top = _Top;
		Node Pre = null;

		while(Top != null) {
//			System.out.println(i.toString() + " - " + Top._Value.toString());

			if(i.equals(Top._Value)) {
//				System.out.println("equals");
				if(Pre == null) {
					_Top = Top._Next;
				} else {
					Pre._Next = Top._Next;
				}
//				_Length--;
				break;
			}

			Pre = Top;
			Top = Top._Next;
		}
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node _T = _Top;

			@Override
			public boolean hasNext() {
				return _T != null;
			}

			@Override
			public T next() {
				final T TEMP = _T._Value;

				_T = _T._Next;

				return TEMP;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
