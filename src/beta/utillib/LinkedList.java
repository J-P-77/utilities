package beta.utillib;

/**
 * Simple Push Pop
 * 
 * @author Dalton Dell
 */
public class LinkedList<T> {
	protected Node _Top = null;
//	protected int _Length = 0;
	
	public void push(T i) {
		final Node TEMP = new Node();
		TEMP._Value = i;
		TEMP._Next = _Top;
		
		_Top = TEMP;
		
//		_Length++;
	}
	
	public T pop() {
		final T TEMP = _Top._Value;
		
		_Top = _Top._Next;
//		_Length--;
		
		return TEMP;
	}

    public boolean isEmpty() {
        return _Top == null;
    }

//    public int length() {
//    	return _Length;
//    }
    
    protected class Node {
		public T _Value = null;
		public Node _Next = null;
		
		@Override
		public String toString() {
			return _Value.toString();
		}
	}

    public static void main(String[] args) {
        final LinkedList<Integer> LIST = new LinkedList<Integer>();

        for(int X = 0; X < 20; X++) {
            LIST.push(X);
        }

        while(!LIST.isEmpty()) {
            System.out.println(LIST.pop());
        }
    }
}