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

package jp77.beta.utillib.classloader;

import jp77.beta.utillib.classloader.v2.wrappers.ClassWrapper;
import jp77.beta.utillib.classloader.v2.wrappers.IClassMethodCall;

import jp77.utillib.arrays.ResizingArray;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PriorityClassLoader extends ClassLoader implements Closeable {
	private static final Logger _LOG_ = Logger.getLogger(PriorityClassLoader.class.toString());
	
	private final ResizingArray<Priority> _CLASSLOADERS = new ResizingArray<Priority>();

	private Thread _Close_On_VM_Shutdown = null;

	private final String _NAME;

	public PriorityClassLoader() {
		this("PriorityClassLoader", false);
	}

	public PriorityClassLoader(boolean addsystemclassloader) {
		this("PriorityClassLoader", addsystemclassloader);
	}

	public PriorityClassLoader(String name, boolean addsystemclassloader) {
		super(null);

		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;

		if(addsystemclassloader) {
			addClassLoader(10, ClassLoader.getSystemClassLoader());
		}
	}

	public PriorityClassLoader(String name, int priority, ClassLoader classloader) {
		super(null);

		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		_NAME = name;

		if(classloader != null) {
			addClassLoader(priority, classloader);
		}
	}

	private String getName() {
		return _NAME;
	}

	/**
	 * 
	 * @param priority
	 *            10 is the highest priority
	 * @param classloader
	 */
	public void addClassLoader(int priority, ClassLoader classloader) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(priority < 0 || priority > 10) {
			throw new RuntimeException("Variable[priority] Must be Between 0-10");
		}

		final Priority P = new Priority(priority, classloader);

		_CLASSLOADERS.put(P);

		sort();
	}

	public void changePriority(int priority, ClassLoader classloader) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(priority < 0 || priority > 10) {
			throw new RuntimeException("Variable[priority] Must be Between 0-10");
		}

		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			if(_CLASSLOADERS.getAt(X).equals(classloader)) {
				_CLASSLOADERS.getAt(X)._Priority = priority;
				break;
			}
		}
	}

	public ClassLoader getClassLoader(int index) {
		final Priority P = _CLASSLOADERS.getAt(index);

		if(P != null) {
			return P._ClassLoader;
		} else {
			return null;
		}
	}

	public void removeClassLoader(ClassLoader classloader) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			if(classloader.equals(_CLASSLOADERS.getAt(X)._ClassLoader)) {
				_CLASSLOADERS.removeAll(_CLASSLOADERS.getAt(X));
				break;
			}
		}

		sort();
	}

	public void removeClassLoader(int index) {
		_CLASSLOADERS.removeAt(index);

		sort();
	}

	public int classloaderCount() {
		return _CLASSLOADERS.length();
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
	 * @param name
	 * @param resolve
	 * @return
	 */
	@Override
	public synchronized Class<?> loadClass(String name, boolean resolve) {
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			try {
				Class<?> Clazz = _CLASSLOADERS.getAt(X)._ClassLoader.loadClass(name);

				if(Clazz == null) {
					final String CL_NAME = getClassLoaderName(_CLASSLOADERS.getAt(X)._ClassLoader);

					if(CL_NAME != null) {
						_LOG_.log(Level.INFO, "Class: " + name + " Not Found In " + CL_NAME);
					} else {
						_LOG_.log(Level.INFO, "Class: " + name + " Not Found");
					}
				} else {
					return Clazz;
				}
			} catch(Exception e) {
				_LOG_.log(Level.SEVERE, "", e);
			}
		}

		return null;
	}

	@Override
	public URL getResource(String name) {
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			try {
				URL Url = _CLASSLOADERS.getAt(X)._ClassLoader.getResource(name);

				if(Url == null) {
					final String CL_NAME = getClassLoaderName(_CLASSLOADERS.getAt(X)._ClassLoader);

					if(CL_NAME != null) {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found In " + CL_NAME);
					} else {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found");
					}
				} else {
					return Url;
				}
			} catch(Exception e) {
				_LOG_.log(Level.SEVERE, "", e);
			}
		}

		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			try {
				InputStream IStream = _CLASSLOADERS.getAt(X)._ClassLoader.getResourceAsStream(name);

				if(IStream == null) {
					final String CL_NAME = getClassLoaderName(_CLASSLOADERS.getAt(X)._ClassLoader);

					if(CL_NAME != null) {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found In " + CL_NAME);
					} else {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found");
					}
				} else {
					return IStream;
				}
			} catch(Exception e) {
				_LOG_.log(Level.SEVERE, "", e);
			}
		}

		return null;
	}

	public boolean resourceExists(String name) {
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			try {
				URL Url = _CLASSLOADERS.getAt(X)._ClassLoader.getResource(name);

				if(Url == null) {
					final String CL_NAME = getClassLoaderName(_CLASSLOADERS.getAt(X)._ClassLoader);

					if(CL_NAME != null) {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found In " + CL_NAME);
					} else {
						_LOG_.log(Level.INFO, "Resource: " + name + " Not Found");
					}
				} else {
					return true;
				}
			} catch(Exception e) {
				_LOG_.log(Level.SEVERE, "", e);
			}
		}

		return false;
	}

	public void addCloseShutdownHook() {
		if(_Close_On_VM_Shutdown == null) {
			_Close_On_VM_Shutdown = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						_LOG_.log(Level.INFO, "Closing Classloader: " + getName());
						if(!isClosed()) {
							close();
							_LOG_.log(Level.INFO, getName() + " - Classloader Closed");
						} else {
							_LOG_.log(Level.INFO, "Classloader: " + getName() + " Allready Closed");
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
	public synchronized void close() throws IOException {
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			final Priority P = _CLASSLOADERS.getAt(X);

			final ClassWrapper C_WRAPPER = new ClassWrapper(P._ClassLoader);

			final IClassMethodCall __close = C_WRAPPER.getMethod("close");

			if(__close != null) {
				__close.call(P._ClassLoader);
			}
		}
	}

	public boolean isClosed() {
		return false;
	}

	private void sort() {//High To Low
		for(int X = 0; X < _CLASSLOADERS.length(); X++) {
			for(int Y = 0; Y < X; Y++) {
				if(_CLASSLOADERS.getAt(X)._Priority > _CLASSLOADERS.getAt(Y)._Priority) {
					final Priority P = _CLASSLOADERS.getAt(X);

					_CLASSLOADERS.set(X, _CLASSLOADERS.getAt(Y));
					_CLASSLOADERS.set(Y, P);
				}
			}
		}
	}

	//STATIC
	private static String getClassLoaderName(ClassLoader classloader) {
		final ClassWrapper C_WRAPPER = new ClassWrapper(classloader);

		final IClassMethodCall __getName = C_WRAPPER.getMethod("getName");

		if(__getName != null) {
			return (String)__getName.call(classloader);
		} else {
			return null;
		}
	}

	//CLASSES
	private class Priority {
		private int _Priority = 0;
		private ClassLoader _ClassLoader = null;

		public Priority(int priority, ClassLoader classloader) {
			_Priority = priority;
			_ClassLoader = classloader;
		}
	}
//    private static void sortHighToLow(ResizingArray<Integer> ints) {
//    	for(int X = 0; X < ints.length(); X++) {
//    		for(int Y = 0; Y < X; Y++) {
//	    		if(ints.getAt(X) > ints.getAt(Y)) {
//	    			final Integer P = ints.getAt(X);
//	    			
//	    			ints.set(X, ints.getAt(Y));
//	    			ints.set(Y, P);
//	    		}
//    		}
//    	}
//    }
//    
//    private static void sortLowToHigh(ResizingArray<Integer> ints) {
//    	for(int X = 0; X < ints.length(); X++) {
//    		for(int Y = 0; Y < X; Y++) {
//	    		if(ints.getAt(X) < ints.getAt(Y)) {
//	    			final Integer P = ints.getAt(X);
//	    			
//	    			ints.set(X, ints.getAt(Y));
//	    			ints.set(Y, P);
//	    		}
//    		}
//    	}
//    }
}