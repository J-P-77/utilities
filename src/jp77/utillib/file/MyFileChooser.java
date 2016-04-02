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

package jp77.utillib.file;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import jp77.utillib.strings.MyStringBuffer;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * February 18, 2009 (Version 1.0.1)
 *     -Error Was Caused Will User SelectedAll FileFilter. Unable To Cast
 *          Filter To MyFileFilter Class
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyFileChooser extends JFileChooser {
	private File _Previous_Directory = null;

	public MyFileChooser() {
		this("File\\Folder Chooser", null, true);
	}

	public MyFileChooser(String title) {
		this(title, null, true);
	}

	public MyFileChooser(File file) {
		this("File\\Folder Chooser", file, true);
	}

	public MyFileChooser(String title, String file) {
		this(title, new File(file), true);
	}

	public MyFileChooser(String title, File file) {
		this(title, file, true);
	}

	public MyFileChooser(String title, File file, boolean acceptallfilter) {
		this(title, file, acceptallfilter, new MyFileFilter[0]);
	}

	public MyFileChooser(String title, String description, String... filters) {
		this(title, null, false, new MyFileFilter(description, filters));
	}

	public MyFileChooser(String title, File file, boolean acceptallfilter, String description, String... filters) {
		this(title, file, acceptallfilter, new MyFileFilter(description, filters));
	}

	public MyFileChooser(String title, File file, boolean acceptallfilter, MyFileFilter... filters) {
		super.setDialogTitle(title);
		super.setAcceptAllFileFilterUsed(acceptallfilter);

		if(file != null && file.exists()) {
			super.setCurrentDirectory(file);
		}

		for(int X = 0; X < filters.length; X++) {
			super.addChoosableFileFilter(filters[X]);
		}
	}

	/**
	 * 
	 * @param parent
	 * @param approveButtonText
	 * @return true If User Approved, false If User Canceled
	 * @throws HeadlessException
	 */
	public boolean showFiles(Component parent, String approveButtonText) throws HeadlessException {
		super.setFileSelectionMode(MyFileChooser.FILES_ONLY);

		return (super.showDialog(parent, approveButtonText) == JFileChooser.APPROVE_OPTION);
	}

	/**
	 * 
	 * @param parent
	 * @param approveButtonText
	 * @return true If User Approved, false If User Canceled
	 * @throws HeadlessException
	 */
	public boolean showDirectories(Component parent, String approveButtonText) throws HeadlessException {
		super.setFileSelectionMode(MyFileChooser.DIRECTORIES_ONLY);

		return (super.showDialog(parent, approveButtonText) == JFileChooser.APPROVE_OPTION);
	}

	/**
	 * 
	 * @param parent
	 * @param approveButtonText
	 * @return true If User Approved, false If User Canceled
	 * @throws HeadlessException
	 */
	public boolean showAll(Component parent, String approveButtonText) throws HeadlessException {
		super.setFileSelectionMode(MyFileChooser.FILES_AND_DIRECTORIES);

		return (super.showDialog(parent, approveButtonText) == JFileChooser.APPROVE_OPTION);
	}

	public void multiSelect(boolean value) {
		super.setMultiSelectionEnabled(value);
	}

	public void storePreviousDirectory() {
		_Previous_Directory = super.getSelectedFile();
	}

	public void removePreviousDirectory() {
		_Previous_Directory = null;
	}

	public void restorePreviousDirectory() {
		if(_Previous_Directory != null) {
			super.setSelectedFile(_Previous_Directory);
		}
	}

	public File getPreviousDirectory() {
		return _Previous_Directory;
	}

	public String getSelectedFilter() {
		final FileFilter ALL = super.getAcceptAllFileFilter();
		final FileFilter CURRENT = super.getFileFilter();

		if(ALL == null || CURRENT == null || ALL.equals(CURRENT)) {
			return "";
		} else {
			if(CURRENT instanceof MyFileFilter) {
				MyFileFilter MYTFF = (MyFileFilter)CURRENT;

				return MYTFF.getExtension();
			} else {
				return "";
			}
		}
	}

	public String getPathPlusFilter() {
		final String FILEPATH = this.getSelectedFile().getPath();
		final MyStringBuffer RESULT = new MyStringBuffer(FILEPATH.length() + 5);

		final String EXT = getSelectedFilter();

		if(EXT != null && EXT.length() > 0) {
			if(!FILEPATH.endsWith(EXT)) {
				RESULT.append(EXT);
			}
		}

		return RESULT.toString();
	}

	public void addFileFilter(String description, String filter) {
		super.addChoosableFileFilter(new MyFileFilter(description, filter));
	}

	public void addFileFilter(String description, String... filters) {
		super.addChoosableFileFilter(new MyFileFilter(description, filters));
	}

	public void addFileFilter(FileFilter filter) {
		super.addChoosableFileFilter(filter);
	}
}
