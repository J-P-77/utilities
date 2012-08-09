package utillib.table;

import java.util.Comparator;

/**
 * November 27, 2008 (Version 1.0.0)<br>
 *     -First Released<br>
 * <br>
 * @author Justin Palinkas<br>
 * <br>
 * Current Version 1.0.0
 */

//!!!!!!!!!!!!!!!IN TESTING PHASE!!!!!!!!!!!!!!!
public class Column {
    public static final int TYPE_INT = 0;
    public static final int TYPE_STR = 1;
    public static final int TYPE_OBJ = 2;
    public static final int TYPE_BOOL = 3;
    
    public String _Name = "";//You Must Set
    public int _Class = -1;//You Must Set
    public Comparator _Sort = null;//You Must Set
    
    public Object[] _Rows = null;
    public boolean[] _Editable = null;
    
    public Column(String name) {
        this(name,TYPE_STR,null);
    }

    public Column(String name, int classtype) {
        this(name,classtype,null);
    }

    public Column(String name, int classtype, Comparator comparator) {
        _Name = name;
        _Class = classtype;
        _Sort = comparator;
    }
}
