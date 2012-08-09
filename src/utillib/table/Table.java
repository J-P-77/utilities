package utillib.table;

//import utillib.strings.StringUtil;
//import utillib.collections.MyStackAll;
//import utillib.utilities.RandomUtil;
//
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 * November 27, 2008 (Version 1.0.0)<br>
 *     -First Released<br>
 * <br>
 * @author Justin Palinkas<br>
 * <br>
 * Current Version 1.0.0
 */

//!!!!!!!!!!!!!!!IN TESTING PHASE!!!!!!!!!!!!!!!
public class Table extends JTable implements MouseListener {
    private Button _Button = new Button();
    private boolean _SyncTable = false;
    
    public Table(AbstractTableModel tablemodel) {
        super(tablemodel);
    }

    public void createHeader() {
        createHeader(0);
    }
    
    public void setSyncTable(boolean value) {
        _SyncTable = value;
    }
    
    public boolean getSyncTable() {
        return _SyncTable;
    }
    
    public void createHeader(int offset) {
        TableColumnModel Temp = this.getColumnModel();

        for(int X = offset; X < this.getColumnCount(); X++) {
            Temp.getColumn(X).setHeaderRenderer(_Button);
        }

        this.getTableHeader().addMouseListener(this);
    }
    
    public void setHeaderColumnName(int column, String name) {
        AbstractTableModel ModelTemp = (AbstractTableModel)this.getModel();
        
        if(ModelTemp instanceof BlankModel) {
            ((BlankModel)ModelTemp).setColumnName(column,name);
        }
        
        this.getTableHeader().getColumnModel().getColumn(column).setHeaderValue(name);
        this.getTableHeader().repaint();
    }
    
    public void addRow(int numofrows) {
        AbstractTableModel ModelTemp = (AbstractTableModel)this.getModel();
        
        if(ModelTemp instanceof BlankModel) {
            BlankModel BlankTemp = (BlankModel)ModelTemp;
            BlankTemp.addRow(numofrows);
        } else {
            throw new RuntimeException("Incorrect Table Model Required[BlankModel_Beta]");
        }
    }
    
    public void addRow() {
        AbstractTableModel ModelTemp = (AbstractTableModel)this.getModel();
        
        if(ModelTemp instanceof BlankModel) {
            BlankModel BlankTemp = (BlankModel)ModelTemp;
            BlankTemp.addRow();
        } else {
            throw new RuntimeException("Incorrect Table Model Required[BlankModel_Beta]");
        }
    }
    
    public void addColumn() {
        addColumn(new Column("",Column.TYPE_STR));
    }
    
    public void addColumn(Column column) {
        AbstractTableModel ModelTemp = (AbstractTableModel)this.getModel();
        
        if(ModelTemp instanceof BlankModel) {
            BlankModel BlankTemp = (BlankModel)ModelTemp;
            BlankTemp.addColumn(column);
        } else {
            throw new RuntimeException("Incorrect Table Model Required[BlankModel_Beta]");
        }
        
        createHeader();
    }
    
    public void addColumns(Column[] column) {
        for(int X = 0; X < column.length; X++) {
            addColumn(column[X]);
        }
    }
    
    public void mouseClicked(MouseEvent e) {}
/* Column 1
Column: 0 - mousePressed
Column: 1 - buttoncClicked: false
Column: 0 - buttoncClicked: true
Column: 0 - mouseReleased
Column: 0 - buttoncClicked: true
Column: 1 - buttoncClicked: false
*/
/* Column
Column: 1 - mousePressed
Column: 1 - mouseReleased
Column: 0 - buttoncClicked: false
Column: 1 - buttoncClicked: true

*/
    public void mousePressed(MouseEvent e) {
        boolean Bad = false;
        
        int Column = this.getTableHeader().columnAtPoint(e.getPoint());
        AbstractTableModel ModelTemp = (AbstractTableModel)this.getModel();
        
        _Button.setCurrent(Column, Button.STATE_DOWN);        
        //System.out.println("Column: " + Column + " - mousePressed");
        
        if(ModelTemp instanceof BlankModel) {
            BlankModel BlankTemp = (BlankModel)ModelTemp;
            BlankTemp.sortColumn(Column, true,_SyncTable);
            
            //TEST ONLY
            switch(Column) {
                case 0:
                    for(int X = 0; X < _NumbersLength; X++) {
                        int IntTemp = (Integer)BlankTemp.getValueAt(X, 0);

                        if(IntTemp != X) {
                            Bad = true;
                            break ;
                        }
                    }
                    break;
                case 1:
                    for(int X = 0; X < _NumbersLength; X++) {
                        String StringTemp = (String)BlankTemp.getValueAt(X, 1);

                        if(StringTemp.length() > 0) {
                            if(StringTemp.charAt(0) != LOWER_CASE[X]) {
                                Bad = true;
                                break ;
                            }
                        }
                    }
                    break;
            }
        } else {
            throw new RuntimeException("Incorrect Table Model Required[BlankModel_Beta]");
        }
        
        if(Bad) {
            System.out.println("Column: " + Column + " - :( BAD ):");
        } else {
            System.out.println("Column: " + Column + " - :) GOOD (:");
        }
        
        this.getTableHeader().repaint();
        //System.out.println("mousePressed - repaint");
    }

    public void mouseReleased(MouseEvent e) {
        int Column = this.getTableHeader().columnAtPoint(e.getPoint());

        _Button.setCurrent(Column, Button.STATE_UP);
        //System.out.println("Column: " + Column + " - mouseReleased");

        this.getTableHeader().repaint();
        //System.out.println("mouseReleased - repaint");
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


	private static final char[] LOWER_CASE = {'a','b','c','d','e','f','g','h','i','j','k',
		'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};


    private static int _NumbersLength = 0;
/*
    public static void main(String[] args) {
        final JFrame Frm = new JFrame();
        Frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frm.setSize(456, 510);
        //Frm.setLayout(null);
        Frm.getContentPane().setLayout(new BorderLayout());
        
        JPanel Panel = new JPanel();
        //Panel.setLayout(null);
        Panel.setLayout(new GridLayout(2,2));
        Panel.setBounds(10, 10, 450, 300);

        Column ColTemp1 = new Column("Integer", Column.TYPE_INT,new SortInteger());
        Column ColTemp2 = new Column("String", Column.TYPE_STR,new SortString());
        Column[] ArrTemp = {ColTemp1, ColTemp2};
        
        final BlankModel Model = new BlankModel(ArrTemp,1);
        
        final Table Table = new Table(Model);
        Table.setBounds(0, 0, 450, 200);
        Table.createHeader();
        Table.setSyncTable(true);
        
        int Min = 25;
        int Max = 25;
        int RndLength = RandomUtil.randomInt(Min, Max);
        Table.addRow(RndLength);
        
        MyStackAll<Integer> TempNumbers = new MyStackAll<Integer>();
        for(int X = 0; X < (RndLength + 1); X++) {
            TempNumbers.push(new Integer(X));
        }
        
        for(int X = 0; X < (RndLength + 1); X++) {
            int TempLen = TempNumbers.length() - 1;
            int Rnd = (RandomUtil.randomInt(0, TempLen));
            //int IntTemp = TempNumbers.getItemAt(Rnd);
            //TempNumbers.removeItemAt(Rnd);
            int IntTemp = TempNumbers.popItemAt(Rnd);
            
            Model.setValueAt(new Integer(IntTemp), X, 0);
        }
        _NumbersLength = (RndLength + 1);

        MyStackAll<Character> TempChar = new MyStackAll<Character>();
        for(int X = 0; X < LOWER_CASE.length; X++) {
            TempChar.push(new Character(LOWER_CASE[X]));
        }
        
        for(int X = 0; X < LOWER_CASE.length; X++) {
            int TempLen = TempChar.length() - 1;
            int Rnd = (RandomUtil.randomInt(0, TempLen));
            //char CharTemp = TempChar.getItemAt(Rnd);
            //TempChar.removeItemAt(Rnd);
            char CharTemp = TempChar.popItemAt(Rnd);
                
            Model.setValueAt(new String(Character.toString(CharTemp)), X, 1);
        }
        
//        Model.setValueAt(new String("Justin"), 0, 1);
//        Model.setValueAt(new String("Palinkas"), 1, 1);
//        Model.setValueAt(new String("House"), 2, 1);
//        Model.setValueAt(new String("Dog"), 3, 1);
//        Model.setValueAt(new String("Crazy"), 4, 1);
//        Model.setValueAt(new String("Cat"), 5, 1);
       
        JScrollPane ScrollPane = new JScrollPane(Table);
        ScrollPane.setBounds(Table.getBounds()); 
        //Panel.add(ScrollPane);
        Frm.getContentPane().add(ScrollPane, BorderLayout.NORTH);
        
        JButton AddRowButton = new JButton("Add Row");
        AddRowButton.setBounds(120, 214, 80, 22);
        AddRowButton.setMargin(new Insets(1, 1, 1, 1));
        AddRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aevent) {
                while(true) {
                    String StrTemp = JOptionPane.showInputDialog("Enter Number of Rows:","1");

                    if(StrTemp == null) {
                        break;
                    } else if(!StringUtil.isNumber(StrTemp)) {
                        continue;
                    } else {
                        try {
                            int NumOfRows = Integer.parseInt(StrTemp);

                            Table.addRow(NumOfRows);
                            break;
                        } catch (Exception e) {}
                    }
                }
            }
        });
        Panel.add(AddRowButton);
        
        JButton AddColButton = new JButton("Add Column");
        AddColButton.setBounds(120, 240, 80, 22);
        AddColButton.setMargin(new Insets(1, 1, 1, 1));
        AddColButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aevent) {
                Table.addColumn();
            }
        });
        Panel.add(AddColButton);

        JButton InfoButton = new JButton("Info");
        InfoButton.setBounds(210, 240, 90, 22);
        InfoButton.setMargin(new Insets(1, 1, 1, 1));
        InfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aevent) {
                int Col = Table.getSelectedColumn();
                int Row = Table.getSelectedRow();
                Object ObjTemp = Model.getValueAt(Row, Col);
                
                JOptionPane.showMessageDialog(Frm, ObjTemp);
                
                //int[] Col = Table.getSelectedColumns();
                //int[] Row = Table.getSelectedRows();
            }
        });
        Panel.add(InfoButton);

        JButton SetColNameButton = new JButton("Column Name");
        SetColNameButton.setBounds(210, 214, 90, 22);
        SetColNameButton.setMargin(new Insets(1, 1, 1, 1));
        SetColNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aevent) {
                try {
                    int Col = Integer.parseInt(
                        JOptionPane.showInputDialog("Select Column"));
                    String StrTemp = JOptionPane.showInputDialog("Set Column Name");
                    
                    Table.setHeaderColumnName(Col, StrTemp);
                } catch (Exception e) {}
            }
        });
        Panel.add(SetColNameButton);
        
        Frm.getContentPane().add(Panel,BorderLayout.SOUTH);
        Frm.setVisible(true);
    }
*/
}