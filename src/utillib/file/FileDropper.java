package utillib.file;

import utillib.interfaces.IFileDropper;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDropEvent;

import java.io.File;

import java.util.List;

import beta.utillib.Listener;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * January 08, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * March 11, 2010 (Version 1.0.1)
 *     -Updated
 *         -IFileDropper Interface added Methods[addFile(File), addDirectory(File)] and renamed
 * 
 * Example(s):
 * 
 *      javax.swing.JFrame Frm = new javax.swing.JFrame();
 *      Frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
 *      Frm.setSize(400, 58);
 * 
 *      javax.swing.JTextField Txt = new javax.swing.JTextField();
 *      Frm.getContentPane().add(Txt);
 * 
 *      FileDropperBeta DropFile = new FileDropperBeta(true, true, true);
 *      DropFile.getListeners().put(new IFileDropper() {
 *          public void setText(String value) {
 *              Txt.setText(value);
 *          }
 *      });
 * 
 *      Txt.setDropTarget(new DropTarget(Txt, DropFile));
 * 
 *      Frm.setVisible(true);
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FileDropper extends DropTargetAdapter {
	private Listener<IFileDropper> _LISTENERS = new Listener<IFileDropper>();

	private String _MultiPaths_Separator = File.pathSeparator;

	private boolean _Accept_Folders = false;
	private boolean _Accept_Files = false;
	private boolean _Accept_Multi = false;

	public FileDropper(boolean acceptfiles, boolean acceptfolders, boolean acceptmulti) {
		_Accept_Files = acceptfiles;
		_Accept_Folders = acceptfolders;
		_Accept_Multi = acceptmulti;
	}

	public Listener<IFileDropper> getListeners() {
		return _LISTENERS;
	}

	public void setAcceptFolders(boolean value) {
		_Accept_Folders = value;
	}

	public boolean getAcceptFolders() {
		return _Accept_Folders;
	}

	public void setAcceptFiles(boolean value) {
		_Accept_Files = value;
	}

	public boolean getAcceptFiles() {
		return _Accept_Files;
	}

	public void setAcceptMultiple(boolean value) {
		_Accept_Multi = value;
	}

	public boolean getAcceptMultiple() {
		return _Accept_Multi;
	}

	public void setMultiplePathsSeparator(String value) {
		_MultiPaths_Separator = value;
	}

	public String getMultiplePathsSeparator() {
		return _MultiPaths_Separator;
	}

	public void drop(DropTargetDropEvent de) {
		final DropTargetContext CONTEXT = de.getDropTargetContext();

		de.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

		final Transferable TRANSFER = de.getTransferable();

		try {
			if(hasFileFlavor(TRANSFER)) {
				final Object DATA = TRANSFER.getTransferData(DataFlavor.javaFileListFlavor);
				if(DATA instanceof List) {
					final List<?> FILES = (List<?>)DATA;

					if(_Accept_Multi) {
						final StringBuffer BUFFER = new StringBuffer(FILES.size() * 50);

						for(int X = 0; X < FILES.size(); X++) {
							final File TEMPFILE = (File)FILES.get(X);

							if(_Accept_Folders && TEMPFILE.isDirectory()) {
								fireAddDirectory(TEMPFILE);
								BUFFER.append(TEMPFILE.getPath());
							} else if(_Accept_Files && TEMPFILE.isFile()) {
								fireAddFile(TEMPFILE);
								BUFFER.append(TEMPFILE.getPath());
							}

							if(X != (FILES.size() - 1)) {
								BUFFER.append(_MultiPaths_Separator);
							}
						}

						fireAllFilesAndDirecties(BUFFER.toString());
					} else {
						if(FILES.size() > 0) {
							final File TEMPFILE = (File)FILES.get(0);

							if(TEMPFILE.isDirectory() && _Accept_Folders) {
								fireAddDirectory(TEMPFILE);
								fireAllFilesAndDirecties(TEMPFILE.getPath());
							} else if(TEMPFILE.isFile() && _Accept_Files) {
								fireAddFile(TEMPFILE);
								fireAllFilesAndDirecties(TEMPFILE.getPath());
							}
						}
					}
				}
			}
			CONTEXT.dropComplete(true);
		} catch(Exception e) {
			System.out.println("!!!FILE DROP ERROR!!! - " + e.getMessage());
			CONTEXT.dropComplete(false);
		}
	}

	private boolean hasFileFlavor(Transferable transfer) {
		final DataFlavor[] FLAVORS = transfer.getTransferDataFlavors();

		for(int X = 0; X < FLAVORS.length; X++) {
			if(FLAVORS[X].equals(DataFlavor.javaFileListFlavor)) {
				return true;
			}
		}

		return false;
	}

	private void fireAllFilesAndDirecties(String value) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			final IFileDropper LISTENER = _LISTENERS.getListenerAt(X);

			if(LISTENER != null) {
				LISTENER.allFilesAndDirecties(value);
			}
		}
	}

	private void fireAddFile(File file) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			final IFileDropper LISTENER = _LISTENERS.getListenerAt(X);

			if(LISTENER != null) {
				LISTENER.addFile(file);
			}
		}
	}

	private void fireAddDirectory(File directory) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			final IFileDropper LISTENER = _LISTENERS.getListenerAt(X);

			if(LISTENER != null) {
				LISTENER.addDirectory(directory);
			}
		}
	}

	/*public static void main(String[] args) {
	    javax.swing.JFrame Frm = new javax.swing.JFrame();
	    Frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	    Frm.setSize(400,24 + 34);
	    
	    final javax.swing.JTextField Txt = new javax.swing.JTextField();
	    Frm.getContentPane().add(Txt);
	    
	    FileDropperBeta DropFile = new FileDropperBeta(true, true, true);

	    DropFile.getListeners().put(new IFileDropper() {
	        public void setText(String value) {
	            Txt.setText(value);
	        }
	    });

	    Txt.setDropTarget(new DropTarget(Txt, DropFile));
	    
	    Frm.setVisible(true);
	}*/
}
