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

package jp77.utillib.table;

import jp77.utillib.collections.MyStackAll;

import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

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
public class BlankModel extends AbstractTableModel {
	private MyStackAll<Column> _ColumnNames = null;//Column's

	private int _Row = 0;
	private int _Column = 0;

	public BlankModel(Column columns, int row) {
		this(new Column[] {columns}, row);
	}

	public BlankModel(Column[] columns, int row) {
		if(columns == null) {
			throw new NullPointerException("Variable[columns] Is Null");
		}

		_Row = row;
		_Column = columns.length;

		_ColumnNames = new MyStackAll<Column>();

		for(int X = 0; X < _Column; X++) {
			columns[X]._Rows = new Object[_Row];

			for(int Y = 0; Y < _Row; Y++) {
				/*try {
				    columns[X]._Rows[Y] = columns[X]._Class.newInstance();                    
				} catch (Exception e) {
				    e.printStackTrace();
				}*/

				switch(columns[X]._Class) {
					case Column.TYPE_BOOL:
						columns[X]._Rows[Y] = new Boolean(false);
						break;
					case Column.TYPE_INT:
						columns[X]._Rows[Y] = new Integer(0);
						break;
					case Column.TYPE_OBJ:
						columns[X]._Rows[Y] = new Object();
						break;
					case Column.TYPE_STR:
						columns[X]._Rows[Y] = new String("");
						break;
				}
			}

			_ColumnNames.push(columns[X]);
		}
	}

	public void setSortListener(int column, Comparator comparable) {
		if(_ColumnNames.validIndex(column)) {
			Column ColTemp = _ColumnNames.getItemAt(column);

			ColTemp._Sort = comparable;
		}
	}

	public int getColumnCount() {
		return _Column;
	}

	public int getRowCount() {
		return _Row;
	}

	public void sortColumn(int column, boolean lowertogreater, boolean sync) {
		if(_ColumnNames.validIndex(column)) {
			Column ColTemp = _ColumnNames.getItemAt(column);

			if(ColTemp._Sort != null) {
				Object[] ObjTemp = ColTemp._Rows;
				Comparator Sort = ColTemp._Sort;

				if(lowertogreater) {
					for(int X = 0; X < _Row; X++) {
						for(int Y = 0; (X + Y) < _Row; Y++) {
							Object ObjTemp1 = ObjTemp[X];
							Object ObjTemp2 = ObjTemp[X + Y];

							if(Sort.compare(ObjTemp1, ObjTemp2) > 0) {
								if(sync) {
									for(int Z = 0; Z < _Column; Z++) {
										Column SyncColTemp = _ColumnNames.getItemAt(Z);
										Object[] SyncObjTemp = SyncColTemp._Rows;
										swap(SyncObjTemp, X, X + Y);
									}
								} else {
									swap(ObjTemp, X, X + Y);
								}
							}
						}
					}
				} else {
					for(int X = 0; X < _Row; X++) {
						for(int Y = 0; (X + Y) < _Row; Y++) {
							Object ObjTemp1 = ObjTemp[X];
							Object ObjTemp2 = ObjTemp[X + Y];

							if(Sort.compare(ObjTemp1, ObjTemp2) < 0) {
								if(sync) {
									for(int Z = 0; Z < _Column; Z++) {
										Column SyncColTemp = _ColumnNames.getItemAt(Z);
										Object[] SyncObjTemp = SyncColTemp._Rows;
										swap(SyncObjTemp, X, X + Y);
									}
								} else {
									swap(ObjTemp, X, X + Y);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void swap(Object[] obj, int a, int b) {
		Object ObjTemp = obj[a];
		obj[a] = obj[b];
		obj[b] = null;
		obj[b] = ObjTemp;
	}

/*
TableModelEvent(source);              //  The data, ie. all rows changed 
TableModelEvent(source, HEADER_ROW);  //  Structure change, reallocate TableColumns
TableModelEvent(source, 1);           //  Row 1 changed
TableModelEvent(source, 3, 6);        //  Rows 3 to 6 inclusive changed
TableModelEvent(source, 2, 2, 6);     //  Cell at (2, 6) changed
TableModelEvent(source, 3, 6, ALL_COLUMNS, INSERT); // Rows (3, 6) were inserted
TableModelEvent(source, 3, 6, ALL_COLUMNS, DELETE); // Rows (3, 6) were deleted
 */
	public void setColumnName(int column, String name) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		ColTemp._Name = name;
	}

	public void addColumn(Column column) {
		_Column++;

		column._Rows = new Object[_Row];
		for(int X = 0; X < _Row; X++) {
			/*try {
			     column._Rows[X] = column._Class.newInstance();
			} catch (Exception e) {
			    e.printStackTrace();
			}*/

			switch(column._Class) {
				case Column.TYPE_BOOL:
					column._Rows[X] = new Boolean(false);
					break;
				case Column.TYPE_INT:
					column._Rows[X] = new Integer(0);
					break;
				case Column.TYPE_OBJ:
					column._Rows[X] = new Object();
					break;
				case Column.TYPE_STR:
					column._Rows[X] = new String("");
					break;
			}
		}

		_ColumnNames.push(column);

		this.fireTableStructureChanged();
	}

	@Override
	public String getColumnName(int column) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		return ColTemp._Name;
	}

	public Object getValueAt(int row, int column) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		return ColTemp._Rows[row];
	}

	public void addRow() {
		addRow(1);
	}

	public void addRow(int numofrows) {
		_Row += numofrows;

		for(int X = 0; X < _Column; X++) {
			Column ColTemp = _ColumnNames.getItemAt(X);

			Object[] OldObjTemp = ColTemp._Rows;
			Object[] NewObjTemp = new Object[_Row];

			for(int Y = 0; Y < NewObjTemp.length; Y++) {
				if(Y < OldObjTemp.length) {
					NewObjTemp[Y] = OldObjTemp[Y];
				} else {
					/*try {
					    NewObjTemp[Y] = ColTemp._Class.newInstance();                    
					} catch (Exception e) {
					    e.printStackTrace();
					}*/

					switch(ColTemp._Class) {
						case Column.TYPE_BOOL:
							NewObjTemp[Y] = new Boolean(false);
							break;
						case Column.TYPE_INT:
							NewObjTemp[Y] = new Integer(0);
							break;
						case Column.TYPE_OBJ:
							NewObjTemp[Y] = new Object();
							break;
						case Column.TYPE_STR:
							NewObjTemp[Y] = new String("");
							break;
					}
				}
			}

			ColTemp._Rows = null;
			ColTemp._Rows = NewObjTemp;
			NewObjTemp = null;
		}

		this.fireTableRowsInserted(0, _Row);
	}

	@Override
	public Class getColumnClass(int c) {
		Object ObjTemp = getValueAt(0, c);

		if(ObjTemp == null) {
			return null;
		}

		return getValueAt(0, c).getClass();
	}

	public void setCellEditable(boolean value, int row, int column) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		if(ColTemp._Editable != null) {
			if(row < ColTemp._Editable.length) {
				ColTemp._Editable[row] = value;
			}
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		if(ColTemp._Editable == null) {
			return true;
		} else {
			if(row < ColTemp._Editable.length) {
				return ColTemp._Editable[row];
			} else {
				return true;
			}
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Column ColTemp = _ColumnNames.getItemAt(column);

		ColTemp._Rows[row] = value;

		fireTableCellUpdated(row, column);
	}
}

/*
for(int X = (_Row - 1); X >= 0; X--) {
    for(int Y = 0; (X - Y) >= 0; Y++) {
        Object ObjTemp1 = ObjTemp[X];
        Object ObjTemp2 = ObjTemp[X - Y];

        if(Sort.compare(ObjTemp1, ObjTemp2) < 0){
            swap(ObjTemp, X, X - Y);
        }
    }
}
*/