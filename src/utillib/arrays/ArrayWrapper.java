package utillib.arrays;

public class ArrayWrapper<T> {
	private T[] _Array = null;

	public ArrayWrapper(T[] array) {
		if(array == null) {
			throw new RuntimeException("Variable[array] - Is Null");
		}

		_Array = array;
	}

	public void set(int index, T obj) {
		_Array[index] = obj;
	}

	public T get(int index) {
		return _Array[index];
	}

	public int length() {
		return _Array.length;
	}

	public boolean contains(T obj) {
		return (indexOf(0, obj) != -1);
	}

	public int indexOf(T obj) {
		return indexOf(0, obj);
	}

	public int indexOf(int offset, T obj) {
		for(int X = offset; X < _Array.length; X++) {
			if(_Array[X] != null && _Array[X].equals(obj)) {
				return X;
			}
		}

		return -1;
	}
}
