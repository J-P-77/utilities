package utillib.strings;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDropEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.JTextComponent;

/**<pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 07, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class StringDropper extends DropTargetAdapter {
    private final JTextComponent _TextComponent;
    
    private ActionListener _Listener = null;
    private String _ActionCommand = null;
        
    public StringDropper(JTextComponent textcomponent) {
        if(textcomponent == null) {
            throw new RuntimeException("Variable[textcomponent] Is Null");
        }
        
        _TextComponent = textcomponent;
    }
    
    public void setActionCommand(String value) {
        _ActionCommand = value;
    }
    
    public void setActionCommand(byte value) {
        _ActionCommand = Byte.toString(value);
    }
    
    public void setActionCommand(short value) {
        _ActionCommand = Short.toString(value);
    }
    
    public void setActionCommand(int value) {
        _ActionCommand = Integer.toString(value);
    }
    
    public String getActionCommand() {
        return _ActionCommand;
    }
    
    public void setActionListener(ActionListener listener) {
        _Listener = listener;
    }
    
    public ActionListener getActionListener() {
        return _Listener;
    }
    
    public void drop(DropTargetDropEvent de) {
        DropTargetContext Context = de.getDropTargetContext();
        de.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable t = de.getTransferable();

        try {
            if(hasStringFlavor(t)){
                Object data = t.getTransferData(DataFlavor.stringFlavor);
                if(data instanceof String) {
                    String Str = (String)data;

                    setText(Str);
  
                    if(_Listener != null) {
                        _Listener.actionPerformed(
                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, _ActionCommand));
                    }
                }
            }
            Context.dropComplete(true);
        }
        catch (Exception e) {
            System.out.println("!!!ERROR!!! - " + e.getMessage());
            Context.dropComplete(false);
        }	
    }
    
    private void setText(String value) {
        String StrTemp = _TextComponent.getText();
        
        if(StrTemp == null) {
            _TextComponent.setText(value);
        } else {
            _TextComponent.setText(StrTemp + value);
        }
    }

    private boolean hasStringFlavor(Transferable t) {
        DataFlavor[] Flavors = t.getTransferDataFlavors();

        for(int X =0; X < Flavors.length; X++) {
            if(Flavors[X].equals(DataFlavor.stringFlavor)) {
                return true;
            }
        }

        return false;
    }
}