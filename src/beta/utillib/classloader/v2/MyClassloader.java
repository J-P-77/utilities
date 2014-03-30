/*
The MIT License (MIT)

Copyright (c) 2014 J-P-77

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package beta.utillib.classloader.v2;

import beta.utillib.classloader.ClassConstants;
import beta.utillib.classloader.v2.listeners.DirectoryResourceListener;
import beta.utillib.classloader.v2.listeners.IClassloaderListener;
import beta.utillib.classloader.v2.listeners.JarResourceListener;
import utillib.debug.DebugLogger;
import utillib.debug.LogManager;
import utillib.arrays.ResizingArray;
import utillib.arrays.ResizingByteArray;
import utillib.utilities.ZipJarConstants;
import utillib.exceptions.InvalidClassException;
import utillib.file.FileUtil;

import java.lang.reflect.Method;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * July 19, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * June 10, 2011 (Version 1.0.1)
 *     -Added
 *         -The Option To Disable The Searching Of The SystemClassLoader
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class MyClassloader extends ClassLoader implements Closeable, ZipJarConstants, ClassConstants {
	private final DebugLogger _LOG = LogManager.getInstance().getLogger(MyClassloader.class);

	private static final int _CLASS_READ_BUFFER_SIZE = 64;

	private final ResizingArray<IClassloaderListener> _LISTENERS = new ResizingArray<IClassloaderListener>();

	private Thread _Close_On_VM_Shutdown = null;

	private final String _NAME;

	private boolean _Allow_System_ClassLoader = true;

	public MyClassloader() {
		this("MyClassLoader", getSystemClassLoader());
	}

	public MyClassloader(String name) {
		this(name, getSystemClassLoader());
	}

	public MyClassloader(String name, ClassLoader parent) {
		super(parent);

		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;
	}

	public void setAllowSystemClassLoader(boolean value) {
		_Allow_System_ClassLoader = value;
	}

	public boolean getAllowSystemClassLoader() {
		return _Allow_System_ClassLoader;
	}

	public boolean callMainMethod(String mainclassname, String[] args) {
		final Class<?> MAIN_CLASS = loadClass(mainclassname);

		if(MAIN_CLASS == null) {
			_LOG.printWarning("Main Class: " + mainclassname + " Not Found");
		} else {
			final Method[] METHODS = MAIN_CLASS.getMethods();
			for(int X = 0; X < METHODS.length; X++) {
				final int MOD = METHODS[X].getModifiers();

				//public static void main(String[] args)
				if((MOD & 0x0001) != 0 && (MOD & 0x0008) != 0) {
					if(METHODS[X].getName().equals("main")) {
						if(METHODS[X].getReturnType().equals(void.class)) {
							final Class<?>[] TYPES = METHODS[X].getParameterTypes();

							if(TYPES.length == 1) {
								if(TYPES[0].equals(String[].class)) {
									_LOG.printInformation("Calling Main Method In Class: " + mainclassname);

									try {
										METHODS[X].invoke(MAIN_CLASS, new Object[] {args});

										return true;
									} catch(Exception e) {
										_LOG.printError(e);
									}
									break;
								}
							}
						}
					}
				}
			}
		}

		_LOG.printWarning("Main Method In Class: " + mainclassname + " Not Found");
		return false;
	}

	public void addListener(IClassloaderListener listener) {
		addListener(listener, false);
	}

	public void addListener(IClassloaderListener listener, boolean loadmanifest) {
		if(!classloaderListenerExists(listener)) {
			_LISTENERS.put(listener);

			if(loadmanifest) {
				loadManifest(listener.getManifest());
			}
		}
	}

	public void removeListener(IClassloaderListener listener) {
		_LISTENERS.removeAll(listener);
	}

	public void removeListener(int index) {
		_LISTENERS.removeAt(index);
	}

	public int listenerCount() {
		return _LISTENERS.length();
	}

	private void loadManifest(Manifest manifest) {
		if(manifest != null) {
			final Attributes MAIN = manifest.getMainAttributes();

			if(MAIN != null) {
				final String ATTRIBUTE = MAIN.getValue("Class-Path");

				if(ATTRIBUTE != null && ATTRIBUTE.length() > 0) {
					final String[] SPLIT = ATTRIBUTE.split(" ");

					if(SPLIT.length > 0) {
						for(int X = 0; X < SPLIT.length; X++) {
							if(SPLIT[X].length() > 0) {
								File Lib_File = new File(SPLIT[X].replace(FileUtil._LINUX_c, FileUtil._S_c)).getAbsoluteFile();

								if(!Lib_File.exists()) {
									Lib_File = new File(FileUtil._APP_PATH_, SPLIT[X].replace(FileUtil._LINUX_c, FileUtil._S_c));
								}

								if(!Lib_File.exists()) {
									_LOG.printError("Depends File: " + Lib_File.getPath() + " - Not Found");
								} else {
									if(classloaderListenerExists(Lib_File.getPath())) {
										_LOG.printInformation("Depends Allready Loaded: " + Lib_File.getPath());
									} else {
										IClassloaderListener Loader = null;
										try {
											if(Lib_File.isFile()) {
												if(FileUtil.isFileType(Lib_File, _JAR_MAGIC_NUMBER_)) {
													Loader = new JarResourceListener(Lib_File);
												} else {
													_LOG.printWarning("Not A Zip/Jar File: " + Lib_File.getPath());
													continue;
												}
											} else {
												Loader = new DirectoryResourceListener(Lib_File);
											}

//											if(Loader instanceof IDebugHandler) {
//												((IDebugHandler)Loader).setDebugLogHandler(_LOG);
//											}

											addListener(Loader, true);

											_LOG.printInformation("Loaded Depend: " + Lib_File.getPath());
										} catch(Exception e) {
											_LOG.printError(e);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Use This To Find And Load Classes
	 * 
	 * @param name
	 * @param resolve
	 * @return
	 */
	@Override
	public Class<?> loadClass(String name) {
		return this.loadClass(name, false);
	}

	/**
	 * Use This To Find And Load Classes
	 * 
	 * Search Order: 1. VM Loaded Classes 2. This Classloader 3. Parent
	 * Classloader 4. System Classloader
	 * 
	 * @param name
	 *            name of class
	 * @param resolve
	 * @return
	 */
	@Override
	public synchronized Class<?> loadClass(String name, boolean resolve) {
		//Try To Load Class From This Classloader
		Class<?> Clazz = this.findClass(name);

		if(Clazz == null) {
			//Try To Load Class From Parent Classloader
			final ClassLoader PARENT = getParent();

			if(PARENT == null) {
				_LOG.printInformation("No Parent ClassLoader To Load Class");
			} else {
				try {
					_LOG.printInformation("Searching Parent ClassLoader For: " + name);
					Clazz = PARENT.loadClass(name);

					if(Clazz != null) {
						_LOG.printInformation("Parent ClassLoader Found Class: " + name);
						return resolveClass(Clazz, resolve);
					}
				} catch(Exception e) {}
				_LOG.printInformation("Class: " + name + " Not Found In Parent ClassLoader");
			}

			if(_Allow_System_ClassLoader) {
				try {
					_LOG.printInformation("Searching System ClassLoader For: " + name);
					//Try To Load Class From System Classloader
					Clazz = getSystemClassLoader().loadClass(name);

					if(Clazz != null) {
						_LOG.printInformation("System ClassLoader Found Class: " + name);
						return resolveClass(Clazz, resolve);
					}
				} catch(Exception e) {}
				_LOG.printInformation("Class: " + name + " Not Found In System ClassLoader");
			}

			return null;
		}

		return resolveClass(Clazz, resolve);
	}

	/**
	 * Does Not Search Parent/System ClassLoader Use Method loadClass(String) or
	 * loadClass(String, boolean)
	 * 
	 * Search Order: 1. VM Loaded Classes 2. This Classloader
	 * 
	 * @param name
	 *            name of class
	 * @return
	 */
	@Override
	public Class<?> findClass(String name) {
		final String CLASS_NAME = name;

		//Search VM Loaded Classes
		_LOG.printInformation("Searching VM Loaded Classes For: " + CLASS_NAME);
		Class<?> Clazz = super.findLoadedClass(name);

		if(Clazz != null) {
			_LOG.printInformation("VM Loaded Classes Found Class: " + CLASS_NAME);
			return Clazz;
		}
		_LOG.printInformation("Class: " + CLASS_NAME + " Not Found In VM Loaded Classes");

		//Search This Classloader
		_LOG.printInformation("Searching Local Classes For Class: " + CLASS_NAME);

		final InputStream CLASS_ISTREAM = fireFindClass(CLASS_NAME);

		if(CLASS_ISTREAM != null) {
			_LOG.printInformation("Local Classes Found Class: " + CLASS_NAME);

			return readClass(name, CLASS_ISTREAM);
		}
		_LOG.printInformation("Class: " + CLASS_NAME + " Not Found In Local Classes");

		return null;
	}

	@Override
	public URL getResource(String name) {
		URL Url = this.findResource(name);

		if(Url == null) {
			//Try To Find Class In Parent ClassLoader
			final ClassLoader PARENT = getParent();

			if(PARENT == null) {
				_LOG.printInformation("No Parent ClassLoader To Find Resource");
			} else {
				Url = PARENT.getResource(name);

				if(Url != null) {
					_LOG.printInformation("Parent ClassLoader Found Resource: " + name);
					return Url;
				}
			}
			_LOG.printInformation("Resource: " + name + " Not Found In Parent ClassLoader");

			if(_Allow_System_ClassLoader) {
				Url = getSystemClassLoader().getResource(name);

				if(Url != null) {
					_LOG.printInformation("System ClassLoader Found Resource: " + name);
					return Url;
				}
				_LOG.printInformation("Class: " + name + " Not Found In System ClassLoader");
			}

			return null;
		} else {
			return Url;
		}
	}

	/**
	 * Does Not Search Parent/System ClassLoader or System ClassLoader Use
	 * Method getResource(String)
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public URL findResource(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		URL Url = fireFindResource(name);

		if(Url == null) {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");

			return null;
		} else {
			_LOG.printInformation("Local Found Resource: " + name);
			return Url;
		}
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		InputStream IStream = this.findResourceStream(name);

		if(IStream == null) {
			//Try To Find Class From Parent
			final ClassLoader PARENT = getParent();

			if(PARENT == null) {
				_LOG.printInformation("No Parent ClassLoader To Find Resource");
			} else {
				IStream = PARENT.getResourceAsStream(name);

				if(IStream != null) {
					_LOG.printInformation("Parent ClassLoader Found Resource: " + name);
					return IStream;
				}
			}
			_LOG.printInformation("Resource: " + name + " Not Found In Parent ClassLoader");

			if(_Allow_System_ClassLoader) {
				IStream = getSystemClassLoader().getResourceAsStream(name);

				if(IStream != null) {
					_LOG.printInformation("System ClassLoader Found Resource: " + name);
					return IStream;
				}

				_LOG.printInformation("Class: " + name + " Not Found In System ClassLoader");
			}

			return null;
		} else {
			return IStream;
		}
	}

	/**
	 * Does Not Search Parent/System ClassLoader or System ClassLoader Use
	 * Method getResourceAsStream(String)
	 * 
	 * @param name
	 * @return
	 */
	public InputStream findResourceStream(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		InputStream IStream = fireGetResourceAsStream(name);

		if(IStream == null) {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");

			return null;
		} else {
			_LOG.printInformation("Local Found Resource: " + name);
			return IStream;
		}
	}

	public boolean resourceExists(String name) {
		boolean Exists = this.findIfResourceExists(name);

		if(Exists) {
			return true;
		} else {
			//Try To Find Class From Parent
			final ClassLoader PARENT = getParent();

			if(PARENT == null) {
				_LOG.printInformation("No Parent ClassLoader To Find Resource");
			} else {
				if(PARENT instanceof MyClassloader) {
					Exists = ((MyClassloader)PARENT).resourceExists(name);

					if(Exists) {
						_LOG.printInformation("Resource Exists In Parent ClassLoader: " + name);
						return true;
					}
				} else {
					_LOG.printInformation("Not Using MyClassloader.class Calling getResouce(String) Instead");

					if(getResource(name) != null) {
						_LOG.printInformation("Resource Exists In Parent ClassLoader: " + name);
						return true;
					}
				}
			}
			_LOG.printInformation("Resource: " + name + " Not Found In Parent ClassLoader");

			if(_Allow_System_ClassLoader) {
				if(ClassLoader.getSystemResource(name) != null) {
					_LOG.printInformation("Resource Exists In System ClassLoader: " + name);
					return true;
				}
				_LOG.printInformation("Resource: " + name + " Does Not Exists In System ClassLoader");
			}

			return false;
		}
	}

	/**
	 * Does Not Search Parent/System ClassLoader or System ClassLoader Use
	 * Method resourceExists(String)
	 * 
	 * @param name
	 * @return
	 */
	public boolean findIfResourceExists(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		boolean Exists = fireResourceExists(name);

		if(Exists) {
			_LOG.printInformation("Local Found Resource: " + name);
			return true;
		} else {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");
			return false;
		}
	}

	public void addCloseShutdownHook() {
		if(_Close_On_VM_Shutdown == null) {
			_Close_On_VM_Shutdown = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						_LOG.printInformation("Closing Classloader: " + getName());
						if(!isClosed()) {
							close();
							_LOG.printInformation(getName() + " - Classloader Closed");
						} else {
							_LOG.printInformation("Classloader: " + getName() + " - Allready Closed");
						}
					} catch(Exception e) {}
				}
			});
		}

		Runtime.getRuntime().addShutdownHook(_Close_On_VM_Shutdown);
	}

	public void removeCloseShutdownHook() {
		if(_Close_On_VM_Shutdown != null) {
			Runtime.getRuntime().removeShutdownHook(_Close_On_VM_Shutdown);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	@Override
	public void close() throws IOException {
		fireClose();
	}

	public boolean isClosed() {
		return fireIsClosed();
	}

	private Class<?> resolveClass(Class<?> clazz, boolean resolve) {
		if(resolve) {
			super.resolveClass(clazz);
		}

		return clazz;
	}

	public String getName() {
		return _NAME;
	}

	private Class<?> readClass(String classname, InputStream istream) {
		if(classname == null) {
			throw new RuntimeException("Class[" + getClass().getName() + "] - Method[readClass] - Variable[classname] - Is Null");
		}

		if(istream == null) {
			throw new RuntimeException("Class[" + getClass().getName() + "] - Method[ConstrreadClassuctor] - Variable[istream] - Is Null");
		}

		try {
			byte[] ReadBuffer = new byte[_CLASS_READ_BUFFER_SIZE];
			ResizingByteArray ClassBytes = new ResizingByteArray((istream.available() > 0 ? istream.available() : _CLASS_READ_BUFFER_SIZE));

			//Verify That It Is A Class File
			final byte[] HEADER = new byte[4];
			final int HEADERLEN = istream.read(HEADER, 0, HEADER.length);
			if(HEADERLEN == 4) {
				for(int X = 0; X < 4; X++) {
					if(HEADER[X] != _CLASS_MAGIC_NUMBER_[X]) {
						_LOG.printError("Class: " + classname + " Is Not A Vaild Class File");
						return null;
					}
				}
				ClassBytes.put(HEADER, 0, HEADERLEN);
			} else {
				return null;
			}

			ClassBytes.setIncreaseBy(_CLASS_READ_BUFFER_SIZE);

			int ReadLength = -1;
			while((ReadLength = istream.read(ReadBuffer, 0, ReadBuffer.length)) > 0) {
				ClassBytes.put(ReadBuffer, 0, ReadLength);
			}

			final String PACKAGE_NAME = getPackageNameFromClassName(classname);
			if(PACKAGE_NAME != null) {
				final Package PACKAGE = super.getPackage(PACKAGE_NAME);
				if(PACKAGE != null) {
					super.definePackage(PACKAGE_NAME, null, null, null, null, null, null, null);
				}
			}

			final Class<?> CLASS = super.defineClass(classname, ClassBytes.toArray(), 0, ClassBytes.length());
			//Exception in thread "main" java.lang.NoClassDefFoundError: main/MainX (wrong name: main/Main)

			if(CLASS != null) {
				_LOG.printInformation("Loaded Class: " + classname);
			}

			ReadBuffer = null;
			ClassBytes = null;

			return CLASS;
		} catch(Exception e) {} finally {
			try {
				istream.close();
				istream = null;
			} catch(Exception e) {}
		}

		return null;
	}

	//STATIC
	private static String getPackageNameFromClassName(String classname) {
		final int INDEX = classname.lastIndexOf('.');

		if(INDEX != -1) {
			return classname.substring(0, INDEX);
		}

		return null;
	}

	public static Class<?> getClass(String classname) {
		try {
			return ClassLoader.getSystemClassLoader().loadClass(classname);
		} catch(Exception e) {}

		return null;
	}

	public static Class<?> getClass(String classname, MyClassloader classloader) {
		try {
			return classloader.loadClass(classname);
		} catch(Exception e) {}

		return null;
	}

	public static byte[] readClassBytes(String classname, InputStream istream) throws IOException, InvalidClassException {
		byte[] ReadBuffer = new byte[_CLASS_READ_BUFFER_SIZE];
		ResizingByteArray ClassBytes = new ResizingByteArray((istream.available() > 0 ? istream.available() : _CLASS_READ_BUFFER_SIZE));

		if(!FileUtil.isFileType(istream, _CLASS_MAGIC_NUMBER_)) {
			throw new InvalidClassException("Class: " + classname + " Is Not A Vaild Class File");
		}
		ClassBytes.put(_CLASS_MAGIC_NUMBER_);

//        //Verify That It Is A Class File
//        final byte[] HEADER = new byte[4];
//        final int HEADERLEN = istream.read(HEADER, 0, HEADER.length);
//        if(HEADERLEN == 4) {
//            for(int X = 0; X < 4; X++) {
//                if(HEADER[X] != _CLASS_MAGIC_NUMBER_[X]) {
//                    throw new InvalidClassException("Class: " + name + " - Is Not A Vaild Class File");
//                }
//            }
//            ClassBytes.put(HEADER, 0, HEADERLEN);
//        } else {
//            return null;
//        }

		ClassBytes.setIncreaseBy(_CLASS_READ_BUFFER_SIZE);

		int ReadLength = -1;
		while((ReadLength = istream.read(ReadBuffer, 0, ReadBuffer.length)) > 0) {
			ClassBytes.put(ReadBuffer, 0, ReadLength);
		}

		ReadBuffer = null;

		return ClassBytes.toArray();
	}

	private boolean classloaderListenerExists(IClassloaderListener listener) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(_LISTENERS.getAt(X).equals(listener)) {
				return true;
			}
		}

		return false;
	}

	private boolean classloaderListenerExists(String name) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(_LISTENERS.getAt(X).getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	private InputStream fireFindClass(String name) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			final InputStream CLASS_STREAM = _LISTENERS.getAt(X).findClass(name);

			if(CLASS_STREAM != null) {
				return CLASS_STREAM;
			}
		}

		return null;
	}

	private Manifest[] fireGetManifests() {
		final ResizingArray<Manifest> RESULTS = new ResizingArray<Manifest>();

		for(int X = 0; X < _LISTENERS.length(); X++) {
			final Manifest MANIFEST = _LISTENERS.getAt(X).getManifest();

			if(MANIFEST != null) {
				RESULTS.put(MANIFEST);
			}
		}

		return (RESULTS.length() > 0 ? RESULTS.toArray(new Manifest[RESULTS.length()]) : null);
	}

	private URL fireFindResource(String name) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			final URL URL = _LISTENERS.getAt(X).findResource(name);

			if(URL != null) {
				return URL;
			}
		}

		return null;
	}

	private InputStream fireGetResourceAsStream(String name) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			final InputStream ISTREAM = _LISTENERS.getAt(X).getResourceAsStream(name);

			if(ISTREAM != null) {
				return ISTREAM;
			}
		}

		return null;
	}

	private boolean fireResourceExists(String name) {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(_LISTENERS.getAt(X).resourceExists(name)) {
				return true;
			}
		}

		return false;
	}

	private boolean fireIsClosed() {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			if(!_LISTENERS.getAt(X).isClosed()) {
				return false;
			}
		}

		return true;
	}

	private void fireClose() throws IOException {
		for(int X = 0; X < _LISTENERS.length(); X++) {
			_LISTENERS.getAt(X).close();
		}
	}
}
