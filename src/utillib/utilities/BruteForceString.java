package utillib.utilities;

import utillib.arrays.ResizingArray;

/**
 *
 * @author Justin Palinkas
 */
public class BruteForceString {
    public static final char[] _NUMBER_POOL_ = BruteForceChar._NUMBER_POOL_;
    
    public static final char[] _LOWER_CASE_POOL_ = BruteForceChar._LOWER_CASE_POOL_;
    
    public static final char[] _UPPER_CASE_POOL_ = BruteForceChar._UPPER_CASE_POOL_;
    
    public static final char[] _UPPER_LOWER_CASE_POOL_ = BruteForceChar._UPPER_LOWER_CASE_POOL_;
    
    public static final char[] _BACKETS_POOL_ = BruteForceChar._BACKETS_POOL_;
	
    public static final char[] _SPECIAL_POOL_ = BruteForceChar._SPECIAL_POOL_;
    
    public static final char[] _BINARY_POOL_ = BruteForceChar._BINARY_POOL_;
	
	private ResizingArray<String> _String_Pool = new ResizingArray<String>();

	private final int _MIN;
	private final int _MAX;

    private final int[] _TRACKER;
    private int _CurrentSize = 0;

    private boolean _HasMore = true;

    private final boolean _DEBUGGING = true;

	public BruteForceString(int min, int max) {
        if(min <= 0) {
            throw new RuntimeException("Variable[min] - Must Be Greater Than Zero");
		}

        if(min > max) {
            throw new RuntimeException("Variable[min] - Min Must Be Less Than Max");
		}

		_MIN = min;
		_MAX = max;
        _CurrentSize = _MIN;
        
        _TRACKER = new int[_MAX];
    }

    public long getNumberOfPossibilities() {
        long NumOfPos = 0;
        for(int X = _MIN; X <= _MAX; X++) {
            int Count = _String_Pool.length();

            for(int Y = 1; Y < X; Y++) {
                Count *= _String_Pool.length();
            }

            NumOfPos += Count;
        }
        
        return NumOfPos;
    }

	public void addToPool(String str) {
        _String_Pool.put(str);
	}

	public void addToPool(String[] strs) {
		_String_Pool.puts(strs);
	}

    public void addToPool(char[] chars) {
        if(chars == null) throw new NullPointerException("(char[] chars) Is Null");

        for(int X = 0; X < chars.length; X++) {
            _String_Pool.put(Character.toString(chars[X]));
        }
    }

    public boolean hasMore() {
        return (_HasMore);
    }

    public String next() {
        StringBuffer Buffer = new StringBuffer(_CurrentSize);

        for(int X = 0; X < _CurrentSize; X++) {
            Buffer.append(_String_Pool.getAt(_TRACKER[X]));
        }

        if(_DEBUGGING) {//Debugging Information
            System.out.print("Tracker: ");
            for(int X = 0; X < _CurrentSize; X++) {
                System.out.print(_TRACKER[X]);

                if(X < (_CurrentSize - 1)) {System.out.print(", ");}
            }
            System.out.println();
        }

        int Count = 0;
        _TRACKER[_CurrentSize - 1]++;
        for(int Z = 1; Z <= _CurrentSize; Z++) {
            if(_TRACKER[_CurrentSize - Z] == _String_Pool.length()) {
                Count++;
                if(Z < _CurrentSize) {
                    _TRACKER[_CurrentSize - Z] = 0;
                    _TRACKER[_CurrentSize - Z - 1] += 1;
                }
            }
        }

        if((Count == _CurrentSize)) {
            if(_CurrentSize == _MAX) {
                _HasMore = false;
            } else {
                _CurrentSize++;

                for(int X = 0; X < _TRACKER.length; X++) {
                    _TRACKER[X] = 0;
                }
            }
        }

        return Buffer.toString();
    }
//1 = 0 1
//2 = 00 01 11 10
/*
    public static void main(String[] args) {
        BruteForceString Force = new BruteForceString(1, 2);
        
        Force.addToPool("Justin");
        Force.addToPool("Palinkas");

        System.out.println("Number Of Possibilities: " + Force.getNumberOfPossibilities());

        while(Force.hasMore()) {
            System.out.println(Force.next());
        }

        //System.out.println("Actual Number Of Possibilities: " + Force._NumberOfPossibilities);
    }
*/
}