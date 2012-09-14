package utillib.dialogs;

// import java.awt.event.MouseEvent;
import utillib.file.MyFileChooser;
import utillib.file.MyFileFilter;
import utillib.file.FileUtil.Line_Ending;

import utillib.popupmenus.SwingBasicPopup;

import utillib.arrays.ResizingArray;

import utillib.utilities.PositionWindow;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import java.awt.event.MouseListener;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FrmTextBox extends JFrame {
	private SwingBasicPopup _Popup = null;

	private static final int _BUFFER_LENGTH_ = 32;

	private PositionWindow _PosWin = null;

	private ResizingArray<MyFileFilter> _Filters = new ResizingArray<MyFileFilter>(1);

	private Line_Ending _LineEndings = Line_Ending.WINDOWS;

	public FrmTextBox() {
		this("TextBox", 396, 402, true);
	}

	public FrmTextBox(String title) {
		this(title, 396, 402, true);
	}

	public FrmTextBox(String title, int width, int height) {
		this(title, width, height, true);
	}

	public FrmTextBox(String title, int width, int height, boolean bulidfilemenu) {
		super.setTitle(title);
		super.setSize(width, height);

		initComponents();

		if(bulidfilemenu) {
			bulidFileMenu();
		}
	}

	private void initComponents() {
		super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		super.getContentPane().setLayout(new BorderLayout());

		_PosWin = new PositionWindow(this);

		txtInfo = new JTextArea();
		txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
		txtInfo.setWrapStyleWord(false);
		txtInfo.setTabSize(4);

		_Popup = new SwingBasicPopup(txtInfo);

		scrollInfo = new JScrollPane();
		scrollInfo.setBounds(txtInfo.getBounds());
		scrollInfo.setViewportView(txtInfo);
		super.getContentPane().add(scrollInfo, BorderLayout.CENTER);

		MainMenu = new JMenuBar();
		super.setJMenuBar(MainMenu);

		txtStatus = new JTextField();
		txtStatus.setEditable(false);
		super.getContentPane().add(txtStatus, BorderLayout.PAGE_END);
	}

	private void bulidFileMenu() {
		mnuFile = new JMenu("File");

		mnuFileSave = new JMenuItem("Save");
		mnuFileSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuFileSaveActionPerformed(evt);
			}
		});
		mnuFile.add(mnuFileSave);

		mnuFileOpen = new JMenuItem("Open");
		mnuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuFileOpenActionPerformed(evt);
			}
		});
		mnuFile.add(mnuFileOpen);

		sep1 = new JSeparator();
		mnuFile.add(sep1);

		mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuFileExitActionPerformed(evt);
			}
		});
		mnuFile.add(mnuFileExit);

		MainMenu.add(mnuFile);
	}

	private void mnuFileExitActionPerformed(ActionEvent evt) {
		//this.dispose();
	}

	private void mnuFileSaveActionPerformed(ActionEvent evt) {
		MyFileChooser FChooser = new MyFileChooser("Save File");

		for(int X = 0; X < _Filters.length(); X++) {
			FChooser.addFileFilter(_Filters.getAt(X));
		}

		FChooser.setFileSelectionMode(MyFileChooser.FILES_ONLY);

		if(FChooser.showDialog(this, "Save") != MyFileChooser.CANCEL_OPTION) {
			saveFile(FChooser.getPathPlusFilter());
		}
	}

	private void mnuFileOpenActionPerformed(ActionEvent evt) {
		MyFileChooser FChooser = new MyFileChooser("Open File");

		for(int X = 0; X < _Filters.length(); X++) {
			FChooser.addFileFilter(_Filters.getAt(X));
		}

		FChooser.setFileSelectionMode(MyFileChooser.FILES_ONLY);

		if(FChooser.showDialog(this, "Open") != MyFileChooser.CANCEL_OPTION) {
			openFile(FChooser.getSelectedFile());
		}
	}

	public void hideOnClose() {
		super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void exitOnClose() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void doNothingOnClose() {
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public JMenuBar getMainMenu() {
		return MainMenu;
	}

	public void addMenu(JMenu menu) {
		MainMenu.add(menu);
	}

	public void removeMenu(JMenu menu) {
		MainMenu.remove(menu);
	}

	public JMenu getFileMenu() {
		return mnuFile;
	}

	public void removeFileMenu() {
		if(mnuFile != null) {
			if(mnuFileSave != null) {
				mnuFile.remove(mnuFileSave);
			}

			if(mnuFileOpen != null) {
				mnuFile.remove(mnuFileOpen);
			}

			if(mnuFileExit != null) {
				mnuFile.remove(mnuFileExit);
			}

			if(sep1 != null) {
				mnuFile.remove(sep1);
			}

			MainMenu.remove(mnuFile);
		}
	}

	public void addFileFilter(String description, String filter) {
		addFileFilter(new MyFileFilter(description, filter));
	}

	public void addFileFilter(String description, String... filters) {
		addFileFilter(new MyFileFilter(description, filters));
	}

	public void addFileFilter(MyFileFilter filter) {
		_Filters.put(filter);
	}

	public void setLineWrap(boolean value) {
		txtInfo.setLineWrap(value);
	}

	public void setEditable(boolean value) {
		txtInfo.setEditable(value);
	}

	public void saveFile(char[] filename) {
		saveFile(filename, 0, filename.length);
	}

	public void saveFile(char[] filename, int offset, int length) {
		saveFile(new String(filename).substring(offset, length));
	}

	public void saveFile(String filename) {
		saveFile(new File(filename));
	}

	public void saveFile(File file) {
		FileOutputStream OStream = null;
		try {
			OStream = new FileOutputStream(file);

			Document Doc = txtInfo.getDocument();
			int DocLength = Doc.getLength();

			int Offset = 0;
			while(Offset < DocLength) {
				int ReadLength = ((Offset + _BUFFER_LENGTH_) > DocLength ? (DocLength - Offset) : _BUFFER_LENGTH_);

				final byte[] BLOCK = Doc.getText(Offset, ReadLength).getBytes();

				OStream.write(BLOCK, 0, BLOCK.length);

/*
                Segment Seg = new Segment();
                Doc.getText(Offset, ReadLength, Seg);
                
                for(int X = 0; X < Seg.length(); X++) {
                    OStream.write(Seg.charAt(X));
                }
*/
				Offset += _BUFFER_LENGTH_;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.toString(), super.getTitle(), JOptionPane.ERROR_MESSAGE);
			System.out.println("!!!ERROR!!! - TextBox Name: " + super.getTitle() + " - Message: " + e.getMessage());
		} finally {
			try {
				if(OStream != null) {
					OStream.close();
					OStream = null;
				}
			} catch(IOException e) {}
		}
	}

	public void openFile(char[] filename) {
		saveFile(filename, 0, filename.length);
	}

	public void openFile(char[] filename, int offset, int length) {
		openFile(new String(filename).substring(offset, offset + length));
	}

	public void openFile(String filename) {
		openFile(new File(filename));
	}

	public void openFile(File file) {
		if(file.isFile() && file.exists()) {
			FileInputStream IStream = null;
			try {
				IStream = new FileInputStream(file);
				final byte[] BUFFER = new byte[_BUFFER_LENGTH_];
				int ReadLength = 0;

				while((ReadLength = IStream.read(BUFFER, 0, BUFFER.length)) > 0) {
					txtInfo.append(new String(BUFFER, 0, ReadLength));
				}

/*
                int B = 0;
                while(B != -1) {
                    int Offset = 0;
                    //StringBuffer Buffer = new StringBuffer(_Buffer_Length);

                    while((Offset < _BUFFER_LENGTH_) && ((B = IStream.read()) != -1))  {
                        //Buffer.append((char)B);
                        //Offset++;
                        
                        BUFFER[Offset++] = (byte)B;
                    }
                    
//                    if(Buffer.length() > 0) {
//                        txtInfo.append(Buffer.toString());
//                    }
                    if(Offset > 0) {
                        txtInfo.append(new String(BUFFER, 0, Offset));
                    }
                }
*/
			} catch(Exception e) {
				JOptionPane.showMessageDialog(this, e.toString(), super.getTitle(), JOptionPane.ERROR_MESSAGE);
				System.out.println("!!!ERROR!!! - TextBox Name: " + super.getTitle() + " - Message: " + e.getMessage());
			} finally {
				try {
					if(IStream != null) {
						IStream.close();
						IStream = null;
					}
				} catch(IOException e) {}
			}
		} else {
			JOptionPane.showMessageDialog(this, "File: " + file.getPath() + "\nDoes Not Exists", this.getTitle(), JOptionPane.ERROR_MESSAGE);
			System.out.println("!!!ERROR!!! - File: " + file.getPath() + " Does Not Exists");
		}
	}

	public void print(char c) {
		print(c, false);
	}

	public void print(final char c, boolean invokelater) {
		if(invokelater) {
			Runnable Run = new Runnable() {
				public void run() {
					txtInfo.append(Character.toString((char)c));
				}
			};

			SwingUtilities.invokeLater(Run);
		} else {
			txtInfo.append(Character.toString((char)c));
		}
	}

	public void print(String text) {
		print(text, false);
	}

	public void print(final String text, boolean invokelater) {
		if(invokelater) {
			Runnable Run = new Runnable() {
				public void run() {
					txtInfo.append(text);
				}
			};

			SwingUtilities.invokeLater(Run);
		} else {
			txtInfo.append(text);
		}
	}

	public void println(String text) {
		println(text, false);
	}

	public void println(final String text, boolean invokelater) {
		if(invokelater) {
			Runnable Run = new Runnable() {
				public void run() {
					txtInfo.append(text);
					newline();
				}
			};

			SwingUtilities.invokeLater(Run);
		} else {
			txtInfo.append(text);
			newline();
		}
	}

	public void println() {
		println(false);
	}

	public void println(boolean invokelater) {
		if(invokelater) {
			Runnable Run = new Runnable() {
				public void run() {
					newline();
				}
			};

			SwingUtilities.invokeLater(Run);
		} else {
			newline();
		}
	}

	public void printlnOnly(String text) {
		clearText();

		txtInfo.append(text);
		newline();
	}

	public void newline() {
		txtInfo.append(_LineEndings.getValue());
	}

	public void setCaretPosition(int value) {
		txtInfo.setCaretPosition(value);
	}

	public int getTextLength() {
		return txtInfo.getDocument().getLength();
	}

	public void clearText() {
		txtInfo.setText(null);
	}

	public void setLineEnding(Line_Ending lineending) {
		_LineEndings = lineending;
	}

	public Line_Ending getLineEnding() {
		return _LineEndings;
	}

	public JTextField getStatusTextField() {
		return txtStatus;
	}

	public JTextArea getTextBox() {
		return txtInfo;
	}

	public PositionWindow moveWindow() {
		return _PosWin;
	}

	private JTextArea txtInfo;
	private JScrollPane scrollInfo;

	private JTextField txtStatus;

	private JMenuBar MainMenu;

	//File Menu
	private JMenu mnuFile;
	private JMenuItem mnuFileSave;
	private JMenuItem mnuFileOpen;
	private JSeparator sep1;
	private JMenuItem mnuFileExit;
/*
    public static void main(String[] args) {
        FrmTextBox Txt = new FrmTextBox("Test");
        Txt.exitOnClose();

        Txt.setVisible(true);
    }
*/
}
/*
        super.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\r' || e.getKeyChar() == '\n') {
                    System.out.println("New Line");
                }
            }

            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '\r' || e.getKeyChar() == '\n') {
                    System.out.println("New Line");
                }
            }

            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == '\r' || e.getKeyChar() == '\n') {
                    System.out.println("New Line");
                }
            }
        });
 */