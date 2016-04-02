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

package jp77.beta.utillib.classloader.v1;

import jp77.utillib.utilities.LookAndFeels;
import jp77.utillib.utilities.ZipJarConstants;

import jp77.utillib.exceptions.InvalidFileTypeException;

import jp77.utillib.file.FileUtil;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LookAndFeelLoader implements LookAndFeels, ZipJarConstants {
	private final ClassLoader _LAF_CLASS_LOADER;
	private final String _CLASSNAME;

	private LookAndFeel _LookAndFeelInstance = null;;

	/**
	 * 
	 * @param ford
	 *            Can Be A Jar/Zip File or Directory
	 * @param lookandfeelclassname
	 * @throws FileNotFoundException
	 * @throws NotAJarException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public LookAndFeelLoader(File ford, String lookandfeelclassname) throws FileNotFoundException, InvalidFileTypeException, IOException, ClassNotFoundException {
		if(ford == null) {
			throw new RuntimeException("Variable[ford] - Is Null");
		}

		if(lookandfeelclassname == null) {
			throw new RuntimeException("Variable[lookandfeelclassname] - Is Null");
		}

		if(ford.isFile()) {
			if(!FileUtil.isFileType(ford, _JAR_MAGIC_NUMBER_)) {
				throw new InvalidFileTypeException(ford.getPath() + " Is Not A Zip/Jar");
			}

			_LAF_CLASS_LOADER = new JarClassLoader(ford);
		} else {
			_LAF_CLASS_LOADER = new DirectoryClassLoader(ford);
		}

		_CLASSNAME = lookandfeelclassname;

		final MyClassloader MY_LOADER = (MyClassloader)_LAF_CLASS_LOADER;

		MY_LOADER.addCloseShutdownHook();

		if(!MY_LOADER.resourceExists(_CLASSNAME.replace('.', '/') + ".class")) {
			throw new ClassNotFoundException("Look And Feel Class: " + lookandfeelclassname + " Not Found");
		}
	}

	public LookAndFeelLoader(ClassLoader classloader, String lookandfeelclassname) throws ClassNotFoundException {
		if(classloader == null) {
			throw new RuntimeException("Variable[jarfile] - Is Null");
		}

		if(lookandfeelclassname == null) {
			throw new RuntimeException("Variable[lookandfeelclassname] - Is Null");
		}

		_LAF_CLASS_LOADER = classloader;
		_CLASSNAME = lookandfeelclassname;

		if(_LAF_CLASS_LOADER instanceof MyClassloader) {
			final MyClassloader MY_LOADER = (MyClassloader)_LAF_CLASS_LOADER;

			MY_LOADER.addCloseShutdownHook();

			if(!MY_LOADER.resourceExists(_CLASSNAME.replace('.', '/') + ".class")) {
				throw new ClassNotFoundException("Look And Feel Class: " + lookandfeelclassname + " Not Found");
			}
		} else {
			_LAF_CLASS_LOADER.loadClass(_CLASSNAME);
		}
	}

	public void revertToDefaultLookAndFeel() throws IllegalAccessException, InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	}

	public void setAsLookAndFeel() throws LookAndFeelNotFoundException, ClassLoaderNotFoundException, UnsupportedLookAndFeelException {

		final LookAndFeel L_A_F = getLookAndFeel();

		if(L_A_F == null) {//LookAndFeelNotFound
			throw new LookAndFeelNotFoundException("Look And Feel Not Found");
		} else {
			final ClassLoader CLASSLOADER = getClassLoader();

			if(CLASSLOADER == null) {
				throw new ClassLoaderNotFoundException("Look And Feel ClassLoader Not Found");
			} else {
				UIManager.getDefaults().put("ClassLoader", CLASSLOADER);
				UIManager.setLookAndFeel(getLookAndFeel());
			}
		}
	}

	public ClassLoader getClassLoader() {
		return _LAF_CLASS_LOADER;
	}

	public LookAndFeel getLookAndFeel() {
		if(_LookAndFeelInstance == null) {
			if(isLoaded()) {
				try {
					final Class<?> CLASS = _LAF_CLASS_LOADER.loadClass(_CLASSNAME);

					if(CLASS != null) {
						return (_LookAndFeelInstance = (LookAndFeel)CLASS.newInstance());
					}
				} catch(Exception e) {}
			}

			return null;
		} else {
			return _LookAndFeelInstance;
		}
	}

	public boolean isLoaded() {
		return (_LAF_CLASS_LOADER != null);
	}

	public static LookAndFeelLoader getGTKLoader(File jarfile) throws Exception {
		return new LookAndFeelLoader(jarfile, _GTK_LOOK_AND_FEEL_);
	}
}
