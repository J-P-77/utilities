/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package utillib.table;

import java.util.Comparator;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 27, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
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
		this(name, TYPE_STR, null);
	}

	public Column(String name, int classtype) {
		this(name, classtype, null);
	}

	public Column(String name, int classtype, Comparator comparator) {
		_Name = name;
		_Class = classtype;
		_Sort = comparator;
	}
}
