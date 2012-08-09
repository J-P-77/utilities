package utillib.arrays;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ArraysUtil {
	public static <U> U[] add(U[] arr1, U[] arr2) {
		Object[] Obj = new Object[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return (U[])Obj;
	}
	
	public static byte[] add(byte[] arr1, byte[] arr2) {
		byte[] Obj = new byte[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}
	
	public static short[] add(short[] arr1, short[] arr2) {
		short[] Obj = new short[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}
	
	public static int[] add(int[] arr1, int[] arr2) {
		int[] Obj = new int[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}

	public static long[] add(long[] arr1, long[] arr2) {
		long[] Obj = new long[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}
	
	public static float[] add(float[] arr1, float[] arr2) {
		float[] Obj = new float[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}
	
	public static double[] add(double[] arr1, double[] arr2) {
		double[] Obj = new double[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, Obj, 0, arr1.length);
		System.arraycopy(arr2, 0, Obj,  arr1.length, arr2.length);
		
		return Obj;
	}

	public static <U> void add(U[] arr1, U[] arr2, U[] dest) {
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
	public static void add(byte[] arr1, byte[] arr2, byte[] dest) {	
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
	public static void add(short[] arr1, short[] arr2, short[] dest) {		
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
	public static void add(int[] arr1, int[] arr2, int[] dest) {
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}

	public static void add(long[] arr1, long[] arr2, long[] dest) {	
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
	public static void add(float[] arr1, float[] arr2, float[] dest) {
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
	public static void add(double[] arr1, double[] arr2, double[] dest) {		
		System.arraycopy(arr1, 0, dest, 0, arr1.length);
		System.arraycopy(arr2, 0, dest,  arr1.length, arr2.length);
	}
	
    public static void checkBufferBounds(int bufferlength, int offset, int length) {
    	if(bufferlength > 0) {//This Is Add Because If Buffer Length Is 0 The IndexOutOfBoundsException Is Thrown
	    	if(offset < 0 || offset >= bufferlength ||
	    			length < 0 || length > bufferlength ||
	    			(offset + length) > bufferlength) {
	
	    		throw new IndexOutOfBoundsException();
	    	}
    	}
    }
/*
	public static void main(String[] args) {
		Object[] I1 = {1,2,3,4,5};
		Object[] I2 = {6,7,8,9,0};
		Object[] I3 = new Object[10];
        
		add(I1,I2,I3);
	}
*/
}
//    //Resizing Array
//    final int IncreaseBy = 4;
//    Item[] Items = null;//Array To Hold Objects
//    int Top = 0;//Position In Array To Put Next Object

//    if(Items == null) {
//        Items = new Item[IncreaseBy];
//    } else {
//        if(Top == Items.length) {
//            Item[] TempItems = Items;
//            Items = null;
//            Items = new Item[Top + IncreaseBy];
//            System.arraycopy(TempItems, 0, Items, 0, TempItems.length);
//        }
//    }
//    Items[Top++] = Item;