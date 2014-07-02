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

package beta.utillib.classloader.v1;

import beta.utillib.classloader.ClassConstants;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.arrays.ResizingArray;
import utillib.arrays.ResizingByteArray;

import utillib.exceptions.InvalidClassException;
import utillib.file.FileUtil;

import java.lang.reflect.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;

import java.net.URL;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * March 16, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public abstract class MyClassloader extends ClassLoader implements Closeable, ClassConstants {
	protected final DebugLogger _LOG = LogManager.getInstance().getLogger(MyClassloader.class);

	private static final int _CLASS_READ_BUFFER_SIZE = 64;

	protected final ResizingArray<MyClassloader> _DEPENDS = new ResizingArray<MyClassloader>(0);

	private Thread _Close_On_VM_Shutdown = null;

	protected MyClassloader() {
		this(getSystemClassLoader());
	}

	protected MyClassloader(ClassLoader parent) {
		super(parent);
	}

	public void callMainMethod(String mainclassname, String[] args) {
		try {
			final Class<?> MAIN_CLASS = loadClass(mainclassname);

			if(MAIN_CLASS == null) {
				_LOG.printWarning("Main Class: " + mainclassname + " - Not Found");
			} else {
				final Method[] METHODS = MAIN_CLASS.getMethods();
				Method Main_Method = null;
				for(int X = 0; X < METHODS.length; X++) {
					final int MOD = METHODS[X].getModifiers();

					//public static void main(String[] args)
					if((MOD & 0x0001) != 0 && (MOD & 0x0008) != 0) {
						if(METHODS[X].getName().equals("main")) {
							if(METHODS[X].getReturnType().equals(void.class)) {
								final Class<?>[] TYPES = METHODS[X].getParameterTypes();

								if(TYPES.length == 1) {
									if(TYPES[0].equals(String[].class)) {
										Main_Method = METHODS[X];
										break;
									}
								}
							}
						}
					}
				}

				if(Main_Method == null) {
					_LOG.printWarning("Main Method In Class: " + mainclassname + " - Not Found");
				} else {
					_LOG.printInformation("Calling Main Method In Class: " + mainclassname);
					Main_Method.invoke(MAIN_CLASS, new Object[] {args});
				}
			}
		} catch(Exception e) {
			_LOG.printError(e);
		}
	}

	protected Class<?> readClass(String classname, InputStream istream) throws IOException, InvalidClassException {
		byte[] ReadBuffer = new byte[_CLASS_READ_BUFFER_SIZE];
		ResizingByteArray ClassBytes = new ResizingByteArray((istream.available() > 0 ? istream.available() : _CLASS_READ_BUFFER_SIZE), _CLASS_READ_BUFFER_SIZE);

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

		final String PACKAGE_NAME = getPackageNameFromClassName(classname);
		if(PACKAGE_NAME != null) {
			final Package PACKAGE = super.getPackage(PACKAGE_NAME);
			if(PACKAGE != null) {
				super.definePackage(PACKAGE_NAME, null, null, null, null, null, null, null);
			}
		}

		final Class<?> CLASS = super.defineClass(classname, ClassBytes.toArray(), 0, ClassBytes.length());

		if(CLASS != null) {
			_LOG.printInformation("Loaded Class: " + classname);
		}

		ReadBuffer = null;
		ClassBytes = null;

		return CLASS;
	}

	@Override
	public Class<?> loadClass(String name) {
		return this.loadClass(name, false);
	}

//    //Load/Search Order
//    //1. Search System Classloader
//    //2. Search Parent Classloader
//    //3. Search This Classloader
//    @Override
//    public synchronized Class<?> loadClass(String name, boolean resolve) {
//    	Class Clazz = null;
//        try {
//            _DEBUGHANDLER.printInformation("Searching System ClassLoader For: " + name);
//            Clazz = getSystemClassLoader().loadClass(name);
//
//            if(Clazz != null) {
//                _DEBUGHANDLER.printInformation("System ClassLoader Found Class: " + name);
//                return resolveClass(Clazz, resolve);
//            }
//        } catch (Exception e) {}
//        _DEBUGHANDLER.printInformation("Class: " + name + " Not Found In System ClassLoader");
//    	
//        //Try To Load Class From Parent
//        final ClassLoader PARENT = getParent();
//
//        if(PARENT == null) {
//            _DEBUGHANDLER.printInformation("No Parent ClassLoader To Load Class");
//        } else {
//            try {
//                _DEBUGHANDLER.printInformation("Searching Parent ClassLoader For: " + name);
//                Clazz = PARENT.loadClass(name);
//
//                if(Clazz != null) {
//                    _DEBUGHANDLER.printInformation("Parent ClassLoader Found Class: " + name);
//                    return resolveClass(Clazz, resolve);
//                }
//            } catch (Exception e) {}
//            _DEBUGHANDLER.printInformation("Class: " + name + " Not Found In Parent ClassLoader");
//        }
//        
//        Clazz = this.findClass(name);
//        
//        if(Clazz != null) {
//        	return resolveClass(Clazz, resolve);
//        }
//        
//        return null;
//    }
	//Load/Search Order
	//1. Search This Classloader
	//2. Search Parent Classloader
	//3. Search System Classloader
	/**
	 * Use This To Find And Load Classes
	 * 
	 * Search Order: 1. VM Loaded Classes 2. This Classloader 3. All Depends 4.
	 * Parent Classloader 5. System Classloader
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
			return null;
		}

		return resolveClass(Clazz, resolve);
	}

	/**
	 * Does Not Search Parent or System ClassLoader Use Method loadClass(String)
	 * or loadClass(String, boolean)
	 * 
	 * Search Order: 1. VM Loaded Classes 2. This Classloader 3. All Depends
	 * 
	 * @param name
	 *            name of class
	 * @return
	 */
	@Override
	public Class<?> findClass(String name) {
		final String CLASS_NAME = name;//Buffer.toString() name

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
		Clazz = findLocalClass(CLASS_NAME);

		if(Clazz != null) {
			_LOG.printInformation("Local Classes Found Class: " + CLASS_NAME);
			return Clazz;
		}
		_LOG.printInformation("Class: " + CLASS_NAME + " Not Found In Local Classes");

		//Search All Depends
		for(int X = 0; X < _DEPENDS.length(); X++) {
			final MyClassloader C_LOADER = _DEPENDS.getAt(X);
			_LOG.printInformation("Searching Depends: " + C_LOADER.getName() + " For Class: " + CLASS_NAME);

			Clazz = C_LOADER.findClass(CLASS_NAME);

			if(Clazz != null) {
				_LOG.printInformation("Depends Found Class: " + CLASS_NAME);
				return Clazz;
			}
		}
		_LOG.printInformation("Class: " + CLASS_NAME + " Not Found In Depends");

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

			Url = getSystemClassLoader().getResource(name);
			if(Url != null) {
				_LOG.printInformation("System ClassLoader Found Resource: " + name);
				return Url;
			}

			_LOG.printInformation("Class: " + name + " Not Found In System ClassLoader");
			return null;
		} else {
			return Url;
		}
	}

	/**
	 * Does Not Search Parent or System ClassLoader Use Method
	 * getResource(String)
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public URL findResource(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		URL Url = findLocalResource(name);

		if(Url == null) {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");
			//Search All Depends
			for(int X = 0; X < _DEPENDS.length(); X++) {
				final MyClassloader C_LOADER = _DEPENDS.getAt(X);
				_LOG.printInformation("Searching Depends: " + C_LOADER.getName() + " For Resource: " + name);

				Url = C_LOADER.findResource(name);
				if(Url != null) {
					_LOG.printInformation("Depends Found Resource: " + name);
					return Url;
				}
			}

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

			IStream = getSystemClassLoader().getResourceAsStream(name);
			if(IStream != null) {
				_LOG.printInformation("System ClassLoader Found Resource: " + name);
				return IStream;
			}

			_LOG.printInformation("Class: " + name + " Not Found In System ClassLoader");
			return null;
		} else {
			return IStream;
		}
	}

	/**
	 * Does Not Search Parent or System ClassLoader Use Method
	 * getResourceAsStream(String)
	 * 
	 * @param name
	 * @return
	 */
	public InputStream findResourceStream(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		InputStream IStream = getLocalResourceAsStream(name);

		if(IStream == null) {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");
			//Search All Depends
			for(int X = 0; X < _DEPENDS.length(); X++) {
				final MyClassloader C_LOADER = _DEPENDS.getAt(X);
				_LOG.printInformation("Searching Depends: " + C_LOADER.getName() + " For Resource: " + name);

				IStream = C_LOADER.getResourceAsStream(name);//getLocalResourceAsStream
				if(IStream != null) {
					_LOG.printInformation("Depends Found Resource: " + name);
					return IStream;
				}
			}

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
					//
				}
			}
			_LOG.printInformation("Resource: " + name + " Not Found In Parent ClassLoader");

			if(ClassLoader.getSystemResource(name) != null) {
				_LOG.printInformation("Resource Exists In System ClassLoader: " + name);
				return true;
			}
			_LOG.printInformation("Resource: " + name + " Does Not Exists In System ClassLoader");

			return false;
		}
	}

	/**
	 * Does Not Search Parent or System ClassLoader Use Method
	 * resourceExists(String)
	 * 
	 * @param name
	 * @return
	 */
	public boolean findIfResourceExists(String name) {
		//Search This Classloader
		_LOG.printInformation("Searching Local For Resource: " + name);
		boolean Exists = localResourceExists(name);

		if(Exists) {
			_LOG.printInformation("Local Found Resource: " + name);
			return true;
		} else {
			_LOG.printInformation("Resource: " + name + " Not Found In Local");
			//Search All Depends
			for(int X = 0; X < _DEPENDS.length(); X++) {
				final MyClassloader C_LOADER = _DEPENDS.getAt(X);
				_LOG.printInformation("Searching Depends: " + C_LOADER.getName() + " For Resource: " + name);

				Exists = C_LOADER.resourceExists(name);
				if(Exists) {
					_LOG.printInformation("Depends Found Resource: " + name);
					return true;
				}
			}

			_LOG.printInformation("Resource: " + name + " Not Found In Depends");
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

	private Class<?> resolveClass(Class<?> clazz, boolean resolve) {
		if(resolve) {
			super.resolveClass(clazz);
		}

		return clazz;
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	@Override
	public void close() {
		for(int X = 0; X < _DEPENDS.length();) {
			if(_DEPENDS.getAt(X) != null) {
				_DEPENDS.getAt(X).close();
				_DEPENDS.removeAt(X);
			} else {
				X++;
			}
		}

		localClose();
	}

	public boolean isClosed() {
		if(!isLocalClosed()) {
			return false;
		}

		for(int X = 0; X < _DEPENDS.length(); X++) {
			if(_DEPENDS.getAt(X) != null) {
				if(!_DEPENDS.getAt(X).isClosed()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Search Local ClassLoader For Class
	 * 
	 * @param classname
	 * @return If Class Does Not Exists return null
	 */
	protected abstract Class<?> findLocalClass(String name);

	/**
	 * Search Local ClassLoader For Resource
	 * 
	 * @param classname
	 * @return If Resource Does Not Exists return null
	 */
	protected abstract URL findLocalResource(String name);

	/**
	 * Search Local ClassLoader For Resource
	 * 
	 * @param classname
	 * @return If Resource Stream Does Not Exists return null
	 */
	protected abstract InputStream getLocalResourceAsStream(String name);

	protected abstract boolean localResourceExists(String name);

	public abstract boolean isLocalClosed();

	public abstract void localClose();

	public abstract String getName();

	//STATIC
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

	private static String getPackageNameFromClassName(String classname) {
		final int INDEX = classname.lastIndexOf('.');

		if(INDEX != -1) {
			return classname.substring(0, INDEX);
		}

		return null;
	}

	protected static byte[] readClassBytes(String name, InputStream istream) throws IOException, InvalidClassException {
		byte[] ReadBuffer = new byte[_CLASS_READ_BUFFER_SIZE];
		ResizingByteArray ClassBytes = new ResizingByteArray((istream.available() > 0 ? istream.available() : _CLASS_READ_BUFFER_SIZE));

		//Verify That It Is A Class File
		final byte[] HEADER = new byte[4];
		final int HEADERLEN = istream.read(HEADER, 0, HEADER.length);
		if(HEADERLEN == 4) {
			for(int X = 0; X < 4; X++) {
				if(HEADER[X] != _CLASS_MAGIC_NUMBER_[X]) {
					throw new InvalidClassException("Class: " + name + " Is Not A Vaild Class File");
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

		ReadBuffer = null;

		return ClassBytes.toArray();
	}
}
