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

import utillib.strings.MyStringBuffer;

import utillib.utilities.ZipJarConstants;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.exceptions.InvalidFileTypeException;

import utillib.file.FileUtil;

import utillib.arrays.ResizingArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.jar.Manifest;

import java.net.URL;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * August 16, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * January 18, 2011 (Version 1.1.0)
 *     -Bug Fix
 *         -Would Say The JarClassloader Was Closed When No InputStreams Were Open 
 *             (Jar Is Closed When Not Being Actively Being Used)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class JarResourceListener implements IClassloaderListener, ZipJarConstants {
	private static final DebugLogger _LOG = LogManager.getInstance().getLogger(JarResourceListener.class);

	private final File _JARPATH;

	private Jar _Jar = null;

	private Manifest _Manifest = null;

	public JarResourceListener(String filepath) throws FileNotFoundException, InvalidFileTypeException, IOException {
		this(new File(filepath));
	}

	public JarResourceListener(File file) throws FileNotFoundException, InvalidFileTypeException, IOException {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(!file.exists()) {
			throw new FileNotFoundException(file.getPath() + " Does Not Exists");
		}

		if(!FileUtil.isFileType(file, _JAR_MAGIC_NUMBER_)) {
			throw new InvalidFileTypeException(file.getPath() + " Is Not A Zip/Jar");
		}

		_JARPATH = file;

		JarFile JarFile = new JarFile(file);

		final ResizingArray<JarEntry> JARENTRIES = new ResizingArray<JarEntry>(JarFile.size());

		final Enumeration<JarEntry> ENTRIES = JarFile.entries();
		while(ENTRIES.hasMoreElements()) {
			final JarEntry ENTRY = ENTRIES.nextElement();

			JARENTRIES.put(ENTRY);
		}

		JarFile.close();
		JarFile = null;

		_Jar = new Jar(JARENTRIES.toArray(new JarEntry[JARENTRIES.length()]));
	}

//    public JarClassLoaderListener(InputStream istream) throws FileNotFoundException, InvalidFileTypeException, IOException {
//    	if(istream == null) {
//            throw new RuntimeException("Variable[istream] - Is Null");
//        }
//        	
//    	if(!FileUtil.isFileType(istream, _JAR_MAGIC_NUMBER_)) {
//            throw new InvalidFileTypeException("Is Not A Zip/Jar");
//        }
//
//    	_JARPATH = null;
//        
//    	JarInputStream IStream = new JarInputStream(istream);
//    	
//    	JarEntry Entry = null;
//    	
//    	final ResizingArray<JarEntry> JARENTRIES = new ResizingArray<JarEntry>();
//    	while((Entry = IStream.getNextJarEntry()) != null) {    	
//    		JARENTRIES.put(Entry);
//    	}
//        
//        _Jar = new Jar(JARENTRIES.toArray(new JarEntry[JARENTRIES.length()]));
//    }

	@Override
	public String getName() {
		return _JARPATH.getAbsolutePath();
	}

	public File getFile() {
		return _JARPATH;
	}

	public JarEntry[] getJarEntries() {
		return _Jar._ENTRIES;
	}

	public JarEntry getJarEntry(String name) {
		return _Jar.getJarEntry(name);
	}

	@Override
	public Manifest getManifest() {
		if(_Manifest == null) {
			_Manifest = loadManifest(_Jar);
		}

		return _Manifest;
	}

	@Override
	public InputStream findClass(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		MyStringBuffer Buffer = new MyStringBuffer(name, 6);
		Buffer.replace('.', '/');
		Buffer.append(".class");

		final String CLASS_NAME = Buffer.toString();

		final JarEntry ENTRY = _Jar.getJarEntry(CLASS_NAME);

		if(ENTRY != null) {
			return _Jar.getInputStream(ENTRY);
		}

		return null;
	}

	//jar:file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
	@Override
	public URL findResource(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		final String NAME = name;
		// final String NAME = (name.charAt(0) == '/' ? name.substring(1) : name);

		final JarEntry ENTRY = _Jar.getJarEntry(NAME);

		if(ENTRY != null) {
			try {
				return new URL("jar:file:/" + _JARPATH.toURI().toURL() + "!/" + NAME);
			} catch(Exception e) {
				_LOG.printError(e);
			}
		}

		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if(name == null || name.length() == 0) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		final JarEntry ENTRY = getJarEntry(name);

		if(ENTRY != null) {
			return _Jar.getInputStream(ENTRY);
		}

		return null;
	}

	@Override
	public boolean resourceExists(String name) {
		return _Jar.getJarEntry(name) != null;
	}

	@Override
	public boolean isClosed() {
		return _Jar == null;
	}

	@Override
	public void close() {
		if(_Jar != null) {
			try {
				_Jar.close(true);
			} catch(Exception e) {
				_LOG.printError(e);
			}
			_Jar = null;
		}

		_LOG.printInformation("Local: " + _JARPATH + " - Closed");
	}

	//STATIC
	private static Manifest loadManifest(Jar jar) {
		final JarEntry ENTRY = jar.getJarEntry(JarFile.MANIFEST_NAME);

		if(ENTRY != null) {
			InputStream IStream = jar.getInputStream(ENTRY);

			if(IStream != null) {
				try {
					return new Manifest(IStream);
				} catch(Exception e) {} finally {
					try {
						IStream.close();
					} catch(Exception e2) {}
					IStream = null;
				}
			}
		}

		return null;
	}

	//CLASSES
	//Just Used To Keep Track Of The Number Of Streams Open In A Given Jar
	private class WrapperInputStream extends InputStream {
		private InputStream _IStream;

		public WrapperInputStream(InputStream istream) {
			_IStream = istream;

			_Jar._OpenStreams++;
		}

		@Override
		public int read() throws IOException {
			return _IStream.read();
		}

		@Override
		public int read(byte[] buffer) throws IOException {
			return _IStream.read(buffer);
		}

		@Override
		public int read(byte[] buffer, int offset, int length) throws IOException {
			return _IStream.read(buffer, offset, length);
		}

		@Override
		public int available() throws IOException {
			return _IStream.available();
		}

		@Override
		public void close() throws IOException {
			if(_IStream != null) {
				_IStream.close();
				_IStream = null;

				_Jar._OpenStreams--;

				if(!_Jar.hasOpenStreams()) {
					_Jar.close(false);
				}
			}
		}
	}

	private class Jar {
		private final JarEntry[] _ENTRIES;

		private JarFile _JarFile;
		private int _OpenStreams = 0;

		private Jar(JarEntry[] entries) {
			_ENTRIES = entries;
		}

		private boolean open() {
			if(_JarFile == null) {
				try {
					_JarFile = new JarFile(_JARPATH);
				} catch(Exception e) {
					_LOG.printError(e);
					_JarFile = null;
				}
			}

			return !isClosed();
		}

		private void close(boolean forceclose) {
			if(!hasOpenStreams() || forceclose) {
				if(_JarFile != null) {
					try {
						_JarFile.close();
					} catch(Exception e) {}
					_JarFile = null;
				}
			}
		}

		public boolean hasOpenStreams() {
			return _OpenStreams > 0;
		}

		private boolean isClosed() {
			return _JarFile == null;
		}

		private JarEntry getJarEntry(String name) {
			for(int X = 0; X < _ENTRIES.length; X++) {
				if(_ENTRIES[X].getName().equals(name)) {
					return _ENTRIES[X];
				}
			}

			return null;
		}

		private InputStream getInputStream(JarEntry jarentry) {
			if(open()) {
				try {
					return new WrapperInputStream(_JarFile.getInputStream(jarentry));
				} catch(Exception e) {
					_LOG.printError(e);
				}
			}

			return null;
		}
	}
}

/*
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        _DEBUGHANDLER.printInformation("Find Class: " + name);

        //Search Local Loaded Classes
        for(int X = 0; X < _LOADEDCLASS.length(); X++) {
            final Class CLASS = _LOADEDCLASS.getAt(X);
            final String NAME = CLASS.getName();

            if(name.equals(NAME)) {
                _DEBUGHANDLER.printInformation("Loaded Classes Found Class: " + name);
                return CLASS;
            }
        }

        final JarEntry ENTRY;
        if(isOpen()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', '/');
            Buffer.append(".class");

            ENTRY = _JarFile.getJarEntry(Buffer.toString());
        } else {
            ENTRY = null;
        }

        if(ENTRY == null) {
            for(int X = 0; X < _DEPENDS.length(); X++) {
                final Class CLASS = _DEPENDS.getAt(X).findClass(name);

                if(CLASS != null) {
                    _DEBUGHANDLER.printInformation("Depends Found Class: " + name);
                    return CLASS;
                }
            }
        } else {
            InputStream IStream = null;
            Class Clazz = null;
            try {
                IStream = _JarFile.getInputStream(ENTRY);

                if(name.equalsIgnoreCase("utillib.arguments.CmdLineHandlerBeta$Cmd")) {
                    System.out.println("");
                }

                Clazz = readClass(name, IStream);
            } catch (Exception e) {
                _DEBUGHANDLER.printError(e);
            } finally {
                try {
                    if(IStream != null) {
                        IStream.close();
                        IStream = null;
                    }
                } catch (Exception e) {}
            }

            return Clazz;
//            InputStream IStream = null;
//            byte[] ReadBuffer = null;
//            Class Clazz = null;
//            try {
//                ReadBuffer = new byte[64];
//                IStream = _JarFile.getInputStream(ENTRY);
//
//                final ResizingByteArray BYTES = new ResizingByteArray(IStream.available());
//
//                BYTES.setIncreaseBy(64);
//
//                int BytesRead = -1;
//                while((BytesRead = IStream.read(ReadBuffer, 0, ReadBuffer.length)) != -1) {
//                    BYTES.put(ReadBuffer, 0, BytesRead);
//                }
//
////                stupidByteCorrection(Bytes, 0, Bytes.length);
//
//                final Class CLASS = defineClass(name, BYTES.toArray(), 0, BYTES.length());
//
//                if(CLASS != null) {
//                    _DEBUGHANDLER.printInformation(_FILE.getName() + " - Found Class: " + name);
//                    _LOADEDCLASS.put(CLASS);
//                }
//                ReadBuffer = null;
//
//                Clazz = CLASS;
//            } catch (Exception e) {
//                _DEBUGHANDLER.printError(e);
//            }
//
//            return Clazz;
        }

        return null;
    }
*/