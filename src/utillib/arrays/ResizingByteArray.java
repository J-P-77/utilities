package utillib.arrays;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 16, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ResizingByteArray {
	private byte[] _Buffer = null;
	private int _Top = 0;
	private int _IncreaseBy = 5;

	public ResizingByteArray() {
		this(5);
	}

	public ResizingByteArray(int initalsize) {
		this(initalsize, 5);
	}

	public ResizingByteArray(int initalsize, byte initalobject) {
		this(initalsize, 5, initalobject);
	}

	public ResizingByteArray(int initalsize, int increaseby, byte initalobject) {
		this(initalsize, increaseby);

		put(initalobject);
	}

	public ResizingByteArray(int initalsize, int increaseby) {
		_Buffer = (initalsize < 1 ? new byte[1] : new byte[initalsize]);
		_IncreaseBy = (increaseby > 0 ? increaseby : 5);
	}

	public synchronized void put(byte object) {
		if(_Top == _Buffer.length) {
			resize();
		}

		_Buffer[_Top++] = object;
	}

	public synchronized void put(byte[] buffer) {
		put(buffer, 0, buffer.length);
	}

	public synchronized void put(byte[] buffer, int offset, int length) {
		if((_Top + (length - offset)) >= _Buffer.length) {
			resize(((length - offset) - (_Buffer.length - _Top)) + _IncreaseBy);
		}

		//len - offset = Number Of Objects To Copy
		//minus
		//_Objects.length - _Top = Number Of Current Free Objects
		//plus _IncreaseBy (Bumper Size)

		for(int X = 0; X < length; X++) {
			_Buffer[_Top++] = buffer[X + offset];
		}
	}

	public synchronized byte get(int index) {
		if(validIndex(index)) {
			return _Buffer[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized int get(int startindex, byte[] dstbuffer, int dstoffset, int dstlength) {
		if(!validIndex(startindex)) {
			throw new IndexOutOfBoundsException("Index Number: " + startindex);
		}

		if(dstoffset + dstlength > dstbuffer.length) {
			throw new RuntimeException("Variable[dstbuffer] - Not Enough Room In Buffer");
		}

		int X = 0;
		while(X < _Top && X < dstlength) {
			dstbuffer[dstoffset + X] = _Buffer[startindex + X];
		}

		return X;
	}

	public synchronized void set(int index, byte object) {
		if(validIndex(index)) {
			_Buffer[index] = object;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public synchronized void remove(int index) {
		if(validIndex(index)) {
			for(int X = (index + 1); X < (_Top); X++) {
				_Buffer[index++] = _Buffer[X];
			}

			_Buffer[_Top - 1] = -1;

			_Top = index;
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index + " Out Of Bounds");
		}
	}

	public void reset() {
		_Top = 0;
	}

	public void reset(int value) {
		if(value >= 0 && value < _Buffer.length) {
			_Top = value;
		}
	}

	public synchronized boolean validIndex(int index) {
		if(index >= 0 && index < _Top) {
			return true;
		} else {
			return false;
		}
	}

	public void setIncreaseBy(int value) {
		_IncreaseBy = value;
	}

	public int getIncreaseBy() {
		return _IncreaseBy;
	}

	public int length() {
		return _Top;
	}

	public int getCapacity() {
		return _Buffer.length;
	}

	public boolean isEmpty() {
		return _Top == 0;
	}

	public byte[] getArray() {
		return _Buffer;
	}

	public byte[] toArray() {
		final byte[] TEMP = new byte[_Top];

		System.arraycopy(_Buffer, 0, TEMP, 0, _Top);

		return TEMP;
	}

	private synchronized void resize() {
		resize(_IncreaseBy);
	}

	private synchronized void resize(int increaseby) {
		byte[] ObjTemp = _Buffer;
		_Buffer = null;
		_Buffer = new byte[ObjTemp.length + increaseby];

		System.arraycopy(ObjTemp, 0, _Buffer, 0, ObjTemp.length);
	}
/*
    public static void main(String[] args) {
        ResizingIntArray Bytes = new ResizingIntArray(1);
        
        int[] Arr = {0,1,2,3,4,5,6,7,8,9};

        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
        Bytes.put(Arr, 0, Arr.length);
    }
*/
}
////Resizing Array
//Item[] Items = null;
//int Top = 0;
//if(Items == null) {
//    Items = new Item[4];
//} else {
//    if(Top == Items.length) {
//        Item[] TempItems = Items;
//        Items = null;
//        Items = new Item[Top + 4];
//        System.arraycopy(TempItems, 0, Items, 0, TempItems.length);
//    }
//}
//Items[Top++] = Item;

