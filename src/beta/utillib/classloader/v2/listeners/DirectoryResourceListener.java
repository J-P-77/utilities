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

package beta.utillib.classloader.v2.listeners;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;
import utillib.file.FileUtil;

import utillib.strings.MyStringBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.URL;

import java.util.jar.Manifest;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * August 1, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class DirectoryResourceListener implements IClassloaderListener {
	private static final DebugLogger _LOG = LogManager.getInstance().getLogger(DirectoryResourceListener.class);

	private Manifest _Manifest;

	private File _Directory;

	public DirectoryResourceListener(String directory) throws FileNotFoundException {

		this(new File(directory));
	}

	public DirectoryResourceListener(File directory) throws FileNotFoundException {

		if(directory == null) {
			throw new NullPointerException("Variable[file] - Is Null");
		}

		if(!directory.exists()) {
			throw new FileNotFoundException("Directory: " + directory.getPath() + " Does Not Exists");
		}

		if(!directory.isDirectory()) {
			throw new RuntimeException("Directory: " + directory.getPath() + " Is Not A Directory");
		}

		_Directory = directory;
	}

	@Override
	public String getName() {
		return _Directory.getAbsolutePath();
	}

	@Override
	public Manifest getManifest() {
		if(_Manifest == null) {
			final File MANIFEST_FILE = new File(_Directory, "META-INF" + FileUtil._S_ + "MANIFEST.MF");

			if(MANIFEST_FILE.exists()) {
				FileInputStream IStream = null;
				try {
					IStream = new FileInputStream(MANIFEST_FILE);

					return new Manifest(IStream);
				} catch(Exception e) {} finally {
					if(IStream != null) {
						try {
							IStream.close();
						} catch(Exception e) {}
						IStream = null;
					}
				}
			}
		}

		return _Manifest;
	}

	public File getFile() {
		return _Directory;
	}

	@Override
	public InputStream findClass(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			MyStringBuffer Buffer = new MyStringBuffer(name, 6);
			Buffer.replace('.', FileUtil._S_c);
			Buffer.append(".class");

			final String CLASS_NAME = Buffer.toString();

			final File FILE = new File(_Directory, CLASS_NAME);

			if(FILE.exists()) {
				try {
					return new FileInputStream(FILE);
				} catch(Exception e) {
					_LOG.printError(e);
				}
			}
		}

		return null;
	}

	//jar:file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
	@Override
	public URL findResource(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			final String NAME = name.replace('/', FileUtil._S_c);

			final File FILE = new File(_Directory, NAME);

			if(FILE.exists()) {
				try {
					return new URL("file:/" + FILE.toURI().toURL());
				} catch(Exception e) {
					_LOG.printError(e);
				}
			}
		}

		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			final String NAME = name.replace('/', FileUtil._S_c);

			final File FILE = new File(_Directory, NAME);

			if(FILE != null) {
				if(FILE.exists() && FILE.isFile() && FILE.canRead()) {
					try {
						return new FileInputStream(FILE);
					} catch(Exception e) {}
				}
			}
		}

		return null;
	}

	@Override
	public boolean resourceExists(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(!isClosed()) {
			final String NAME = name.replace('/', FileUtil._S_c);

			return new File(_Directory, NAME).exists();
		}

		return false;
	}

	@Override
	public boolean isClosed() {
		return _Directory == null || !_Directory.canRead();
	}

	@Override
	public void close() {
		_LOG.printInformation("Local: " + getName() + " - Closed");
		_Directory = null;
	}
}
