package beta.utillib.pluginmanager.v2;

import beta.utillib.classloader.v1.DirectoryClassLoader;
import beta.utillib.classloader.v1.JarClassLoader;
import beta.utillib.classloader.v1.MyClassloader;

import beta.utillib.classloader.ClassUtil;

import beta.utillib.pluginmanager.v1.IPluginInfo;

import utillib.pluginmanager.global.Parameter;

import utillib.strings.MyStringBuffer;

import utillib.utilities.ZipJarConstants;

import utillib.exceptions.InvalidFileTypeException;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.interfaces.IPluginListener;
import utillib.interfaces.IPluginListener.Event;

import utillib.arrays.ResizingArray;

import utillib.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * February 1, 2011 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class PluginManager implements ZipJarConstants {
	private static final DebugLogger _LOG_ = LogManager.getInstance().getLogger(PluginManager.class);

	public static final String _PLUGIN_DIRECTORY_ = "plugins";
	public static final String _CLASSS_EXT_ = ".class";

	private final Class<?> _PLUGIN_INTERFACE;

	private final ResizingArray<IPluginListener> _PLUGINLISTENERS = new ResizingArray<IPluginListener>(1, 1);

	private ResizingArray<PluginInfo> _PluginInfo = new ResizingArray<PluginInfo>(1, 2);
	private ResizingArray<String> _BlackList = new ResizingArray<String>(0, 3);

	private final boolean _CREATE_INSTANCE;

	public PluginManager(Class<?> plugininterface) {
		this(plugininterface, true);
	}

	public PluginManager(Class<?> plugininterface, boolean createinstance) {
		if(plugininterface == null) {
			throw new RuntimeException("Variable[plugininterface] - Is Null");
		}

		_PLUGIN_INTERFACE = plugininterface;
		_CREATE_INSTANCE = createinstance;
	}

	public void addPluginListener(IPluginListener listener) {
		_PLUGINLISTENERS.put(listener);
	}

	public void removePluginListener(int index) {
		_PLUGINLISTENERS.removeAt(index);
	}

	public void removePluginListener(IPluginListener listener) {
		_PLUGINLISTENERS.removeAll(listener);
	}

	public int pluginListenerCount() {
		return _PLUGINLISTENERS.length();
	}

	/**
	 * If You Are Done With Object Use Should checkOut The Object
	 * 
	 * @param index
	 * @return Plugin Instance
	 */
	public Object getPlugin(int index) {
		final IPluginInfo P_INFO = getPluginInfoByIndex(index);

		return (P_INFO == null ? null : P_INFO.getPlugin());
	}

	/**
	 * If You Are Done With Object Use Should checkOut The Object
	 * 
	 * @param pluginname
	 * @return Plugin Instance
	 */
	public Object getPlugin(String pluginname) {
		final IPluginInfo P_INFO = getPluginInfoByName(pluginname);

		return (P_INFO == null ? null : P_INFO.getPlugin());
	}

	/**
	 * If You Are Done With Object Use Should checkOut The Object
	 * 
	 * @param index
	 * @return
	 */
	public IPluginInfo getPluginInfoAt(int index) {
		return getPluginInfoByIndex(index);
	}

	/**
	 * If You Are Done With Object Use Should checkOut The Object
	 * 
	 * @param pluginname
	 * @return
	 */
	public IPluginInfo getPluginInfo(String pluginname) {
		return getPluginInfoByName(pluginname);
	}

	public int getPluginInfoIndex(String pluginname) {
		for(int X = 0; X < _PluginInfo.length(); X++) {
			final PluginInfo P_INFO = _PluginInfo.getAt(X);

			if(P_INFO._PluginName.equalsIgnoreCase(pluginname)) {
				return X;
			}
		}

		return -1;
	}

	public boolean isPluginDisabled(int index) {
		return !isPluginEnabled(index);
	}

	public boolean isPluginDisabled(String pluginname) {
		return !isPluginEnabled(pluginname);
	}

	public boolean isPluginEnabled(int index) {
		final IPluginInfo P_INFO = getPluginInfoAt(index);

		return (P_INFO == null ? false : P_INFO.isEnabled());
	}

	public boolean isPluginEnabled(String pluginname) {
		final IPluginInfo P_INFO = getPluginInfo(pluginname);

		return (P_INFO == null ? false : P_INFO.isEnabled());
	}

	public int pluginCount() {
		return _PluginInfo.length();
	}

	public Class<?> getPluginInterface() {
		return _PLUGIN_INTERFACE;
	}

	// Load Lib Directory
	public void loadDirectory() {
		loadDirectory(FileUtil.getHomePath(_PLUGIN_DIRECTORY_));
	}

	// Load Lib Directory
	public void loadDirectory(String directory) {
		loadDirectory(new File(directory));
	}

	public void loadDirectory(File directory) {
		if(directory == null) {
			_LOG_.printError(new RuntimeException("Variable[directory] - Is Null"));
		} else if(!directory.exists()) {
			_LOG_.printError(new RuntimeException(directory.getPath() + " Does Not Exists"));
		} else if(!directory.isDirectory()) {
			_LOG_.printError(new RuntimeException("Variable[directory] - Is Not A Directory"));
		} else {
			final File[] FILES = directory.listFiles();
			for(int X = 0; X < FILES.length; X++) {
				if(FILES[X].isFile()) {
					if(FileUtil.isFileType(FILES[X], JarClassLoader._JAR_MAGIC_NUMBER_)) {
						loadLibrary(FILES[X]);
					}
				}
			}
		}
	}

	// Loads Library
	public void loadLibrary(String file) {
		loadLibrary(new File(file));
	}

	public void loadLibrary(String libname, ClassLoader ploader, String[] classes) {
		if(libname == null) {
			throw new RuntimeException("Variable[libname] - Is Null");
		}

		if(ploader == null) {
			throw new RuntimeException("Variable[ploader] - Is Null");
		}

		if(classes == null) {
			throw new RuntimeException("Variable[classes] - Is Null");
		}

		final PluginClassLoader PLUGIN_LOADER;
		try {
			PLUGIN_LOADER = new PluginClassLoader(ploader, classes);

			// Load Plugins
			for(int X = 0; X < PLUGIN_LOADER.pluginCount(); X++) {
				final String PLUGIN_CLASS = PLUGIN_LOADER.getPluginClassName(X);

				// Check to See if Plugin Is Allowed To Load
				if(isClassBlackListed(PLUGIN_CLASS)) {
					_LOG_.printInformation("Plugin[" + PLUGIN_CLASS + "] Is Blacklisted - Plugin Did Not Load");
				} else {
					final PluginInfo PINFO = new PluginInfo();

					PINFO._File = null;
					PINFO._LibName = libname;
					PINFO._ClassLoader = PLUGIN_LOADER.getClassLoader();

					PINFO._PluginClassName = PLUGIN_CLASS;
					PINFO._Plugin_Class = PLUGIN_LOADER.loadPluginClass(PINFO._PluginClassName);

					if(!ClassUtil.implementsInterface(_PLUGIN_INTERFACE, PINFO._Plugin_Class)) {
						_LOG_.printError(new RuntimeException("Variable[PINFO._Plugin_Class] - " + PINFO._Plugin_Class.getName() + " - Does Not Implement Interface"));

						PINFO._Plugin_Class = null;
						PINFO._Plugin_Instance = null;
						PINFO._Enabled = false;
					} else {
						if(_CREATE_INSTANCE) {
							PINFO._Enabled = newInstance(PINFO, new Parameter[0]);
						}
					}

					if(PINFO._Enabled/* && PINFO._Plugin_Instance != null */) {
						final String PNAME = getPluginName(PINFO._Plugin_Instance);

						if(PNAME == null) {
							PINFO._PluginName = PINFO._PluginClassName;
						} else {
							PINFO._PluginName = PNAME;
						}

						_PluginInfo.put(PINFO);
						// [Plugin Event] - Plugin Added
						sendEvent(Event.PLUGIN_ADDED, PINFO);
					}
				}
			}
		} catch(Exception e) {
			_LOG_.printError("While Loading Plugin");
			_LOG_.printError(e);
		}
	}

	public void loadLibrary(File ford) {
		if(ford == null) {
			throw new RuntimeException("Variable[ford] - Is Null");
		}

		final IPluginClassScanner SCANNER;

		if(ford.isFile()) {
			if(FileUtil.isFileType(ford, _JAR_MAGIC_NUMBER_)) {
				SCANNER = new JarScanner(ford);
			} else {
				throw new RuntimeException(ford.getPath() + ' ' + "Not A Zip/Jar File");
			}
		} else {
			SCANNER = new DirectoryScanner(ford, false);
		}

		final String[] P_CLASSES = SCANNER.getPluginClassesNames(_PLUGIN_INTERFACE);

		if(P_CLASSES != null && P_CLASSES.length > 0) {
			final PluginClassLoader PLUGIN_LOADER;
			try {
				final MyClassloader MYLOADER;
				if(ford.isFile()) {
					MYLOADER = new JarClassLoader(ford, null, null);
				} else {
					MYLOADER = new DirectoryClassLoader(ford, null, null);
				}

				PLUGIN_LOADER = new PluginClassLoader(MYLOADER, P_CLASSES);

				// Load Plugins
				for(int X = 0; X < PLUGIN_LOADER.pluginCount(); X++) {
					final String PLUGIN_CLASS = PLUGIN_LOADER.getPluginClassName(X);

					// Check to See if Plugin Is Allowed To Load
					if(isClassBlackListed(PLUGIN_CLASS)) {
						_LOG_.printInformation("Plugin[" + PLUGIN_CLASS + "] - Plugin Did Not Load Is Blacklisted ");
					} else {
						final PluginInfo PINFO = new PluginInfo();

						PINFO._File = ford;
						PINFO._LibName = ford.getName();
						PINFO._ClassLoader = PLUGIN_LOADER.getClassLoader();

						PINFO._PluginClassName = PLUGIN_CLASS;
						PINFO._Plugin_Class = PLUGIN_LOADER.loadPluginClass(PINFO._PluginClassName);

						if(!ClassUtil.implementsInterface(_PLUGIN_INTERFACE, PINFO._Plugin_Class)) {
							_LOG_.printError(new RuntimeException("Plugin Class: " + PINFO._PluginClassName + " - Does Not Implement Interface"));

							PINFO._Plugin_Class = null;
							PINFO._Plugin_Instance = null;
							PINFO._Enabled = false;
						} else {
							if(_CREATE_INSTANCE) {
								PINFO._Enabled = newInstance(PINFO, new Parameter[0]);
							} else {
								PINFO._Enabled = true;
							}
						}

						if(PINFO._Enabled) {
							final String PNAME = getPluginName(PINFO._Plugin_Instance);

							if(PNAME == null) {
								PINFO._PluginName = PINFO._PluginClassName;
							} else {
								PINFO._PluginName = PNAME;
							}

							_PluginInfo.put(PINFO);
							// [Plugin Event] - Plugin Added
							sendEvent(Event.PLUGIN_ADDED, PINFO);

							_LOG_.printInformation("Plugin[" + PINFO._PluginClassName + "] - Plugin Loaded");
						}
					}
				}
			} catch(FileNotFoundException e) {
				_LOG_.printError("Plugin: " + ford.getPath() + " Not Found");
			} catch(InvalidFileTypeException e) {
				_LOG_.printError("Plugin: " + ford.getPath() + " Not A Jar\\Zip File");
			} catch(Exception e) {
				_LOG_.printError("While Loading Plugin: " + ford.getPath());
				_LOG_.printError(e);
			}
		}
	}

	public void loadClass(Class<?> clazz, Parameter... parameters) {
		if(clazz == null) {
			_LOG_.printError(new RuntimeException("Variable[clazz] - Is Null"));
		} else {
			// Check to See if Plugin Is Allowed To Load
			if(isClassBlackListed(clazz.getName())) {
				_LOG_.printInformation("Plugin: " + clazz.getName() + " Is Blacklisted(Plugin Did Not Load)");
			} else {
				if(!ClassUtil.implementsInterface(_PLUGIN_INTERFACE, clazz)) {
					_LOG_.printError(new RuntimeException("Plugin Class: " + clazz.getName() + " - Does Not Implement Interface"));
				} else {
					try {
						final PluginInfo PINFO = new PluginInfo();

						PINFO._File = null;
						PINFO._LibName = null;
						PINFO._ClassLoader = clazz.getClassLoader();

						PINFO._PluginClassName = clazz.getName();
						PINFO._Plugin_Class = clazz;

						if(_CREATE_INSTANCE) {
							PINFO._Enabled = newInstance(PINFO, parameters);
						} else {
							PINFO._Enabled = true;
						}

						if(PINFO._Enabled/* && PINFO._Plugin_Instance != null */) {
							final String PNAME = getPluginName(PINFO._Plugin_Instance);

							if(PNAME == null) {
								PINFO._PluginName = PINFO._PluginClassName;
							} else {
								PINFO._PluginName = PNAME;
							}

							_PluginInfo.put(PINFO);
							// [Plugin Event] - Plugin Added
							sendEvent(Event.PLUGIN_ADDED, PINFO);

							_LOG_.printInformation("Plugin[" + PINFO._PluginClassName + "] - Plugin Loaded");
						}
					} catch(Exception e) {
						_LOG_.printError("While Loading Plugin: " + clazz.getName());
						_LOG_.printError(e);
					}
				}
			}
		}
	}

	public void unloadLib(String libname) {// Jar/Zip File Name (ex. Example.jar)
		for(int X = 0; X < _PluginInfo.length(); X++) {
			PluginInfo TempInfo = _PluginInfo.getAt(X);

			if(TempInfo._LibName != null) {
				if(libname.equals(TempInfo._LibName)) {
					unloadPlugin(X);
				}
			}
		}
	}

	// UnLoads Plugin
	public void unloadPlugin(String pluginname) {
		final int INDEX = getPluginInfoIndex(pluginname);

		if(INDEX != -1) {
			unloadPlugin(INDEX);
		}
	}

	public void unloadPlugin(int pluginindex) {
		if(_PluginInfo.validIndex(pluginindex)) {
			enablePlugin(pluginindex, false);

			final PluginInfo PINFO = _PluginInfo.getAt(pluginindex);

			PINFO._File = null;
			PINFO._LibName = null;
			PINFO._PluginName = null;
			PINFO._Plugin_Instance = null;
			PINFO._Plugin_Class = null;
			PINFO._ClassLoader = null;
			PINFO._PluginClassName = null;

			_PluginInfo.removeAt(pluginindex);
			// [Plugin Event] - Plugin Unload (Removed)
			sendEvent(Event.PLUGIN_REMOVED, PINFO);
		}
	}

	public void unloadAllPlugins() {
		for(int X = (_PluginInfo.length() - 1); X > -1; X--) {
			unloadPlugin(X);
		}
	}

	// Enable Lib Plugin
	public void enablePlugin(int pluginindex, boolean enable) {
		if(_PluginInfo.validIndex(pluginindex)) {
			final PluginInfo PINFO = _PluginInfo.getAt(pluginindex);

			if(PINFO != null) {
				PINFO._Enabled = enable;
				// [Plugin Event] - Plugin Enabled (Enabled or Disabled)
				if(enable) {
					sendEvent(Event.PLUGIN_ENABLE, PINFO);// Plugin Enabled
					_LOG_.printInformation("Plugin" + PINFO.getPluginName() + " -  Is Enabled");
				} else {
					sendEvent(Event.PLUGIN_DISABLED, PINFO);// Plugin Disabled
					_LOG_.printInformation("Plugin: " + PINFO.getPluginName() + " - Is Disabled");
				}
			}
		}
	}

	public void enablePlugin(String pluginname, boolean enable) {
		int Index = getPluginInfoIndex(pluginname);

		if(Index != -1) {
			enablePlugin(Index, enable);
		}
	}

	// Enable or Disables All Plugins
	public void enableAllPlugins(boolean enable) {
		for(int X = 0; X < _PluginInfo.length(); X++) {
			enablePlugin(X, enable);
		}
	}

	/**
	 * Ex. javax.swing.JButton
	 * 
	 * @param classname
	 */
	public void addBlackListClass(String classname) {
		if(classname != null && classname.length() > 0) {
			if(!isClassBlackListed(classname)) {
				_BlackList.put(classname);
			}
		}
	}

	/**
	 * Ex. javax.swing.JButton
	 * 
	 * @param classname
	 */
	public void removeBlackListClass(String classname) {
		if(classname != null && classname.length() > 0) {
			for(int X = 0; X < _BlackList.length();) {
				if(classname.equals(_BlackList.getAt(X))) {
					_BlackList.removeAt(X);
				} else {
					X++;
				}
			}
		}
	}

	/**
	 * Ex. javax.swing.JButton
	 * 
	 * @param classname
	 * @return
	 */
	public boolean isClassBlackListed(String classname) {
		if(classname == null || classname.length() == 0) {
			return false;
		} else {
			for(int X = 0; X < _BlackList.length(); X++) {
				if(_BlackList.getAt(X).endsWith(classname)) {
					return true;
				}
			}

			return false;
		}
	}

	private void sendEvent(Event event, IPluginInfo plugininfo) {
// final PluginEvent EVENT = new PluginEvent(event, plugininfo, obj);

		synchronized(_PLUGINLISTENERS) {
			for(int X = 0; X < _PLUGINLISTENERS.length(); X++) {
				final IPluginListener LISTENER = _PLUGINLISTENERS.getAt(X);

				if(LISTENER != null) {
					LISTENER.handleEvent(event, plugininfo);
					// LISTENER.handleEvent(EVENT);
				}
			}
		}
	}

	private PluginInfo getPluginInfoByIndex(int index) {
		if(_PluginInfo.validIndex(index)) {
			return _PluginInfo.getAt(index);
		}

		return null;
	}

	private IPluginInfo getPluginInfoByName(String pluginname) {
		for(int X = 0; X < _PluginInfo.length(); X++) {
			final PluginInfo P_INFO = _PluginInfo.getAt(X);

			if(P_INFO._PluginName.equalsIgnoreCase(pluginname)) {
				return P_INFO;
			}
		}

		return null;
	}

	private boolean isPluginInterface(Class<?> aclass) {
		return (aclass == _PLUGIN_INTERFACE);
	}

	// STATIC
	private static boolean newInstance(PluginInfo pinfo, Parameter... parameters) {
		try {
			if(parameters == null || parameters.length == 0) {
				pinfo._Plugin_Instance = pinfo.getClass().newInstance();
			} else {
				final Class<?>[] CLASSES = new Class[parameters.length];
				final Object[] ARGUMENTS = new Object[parameters.length];

				for(int X = 0; X < parameters.length; X++) {
					CLASSES[X] = parameters[X].getType();
					ARGUMENTS[X] = parameters[X].getArgument();
				}

				final Constructor<?> CONSTRUCTOR = pinfo.getClass().getConstructor(CLASSES);

				if(CONSTRUCTOR != null) {
					pinfo._Plugin_Instance = CONSTRUCTOR.newInstance(ARGUMENTS);
				}
			}

			return pinfo.getPlugin() != null;
		} catch(InstantiationException e) {
			_LOG_.printError("Failed To Create Instance For Plugin: " + pinfo.getPluginClassName());
		} catch(IllegalAccessException e) {
			_LOG_.printError("Does Not Have A Public Constructor For Plugin: " + pinfo.getPluginClassName());
		} catch(Exception e) {
			_LOG_.printError("While Loading Plugin: " + pinfo.getPluginClassName());
			_LOG_.printError(e);
		}

		return false;
	}

	private static String getPluginName(Object clazz) {
		Method M = null;
		try {
			M = clazz.getClass().getMethod("getPluginName");

			final Object OBJ = M.invoke(clazz);

			if(OBJ instanceof String) {
				return (String)OBJ;
			}
		} catch(Exception e) {}

		return null;
	}

	// CLASSES
	private class PluginInfo implements IPluginInfo {
		private File _File = null;
		private String _LibName = null;
		private String _PluginName = null;
		private Object _Plugin_Instance = null;
		private Class<?> _Plugin_Class = null;
		private ClassLoader _ClassLoader = null;
		private String _PluginClassName = null;
		private boolean _Enabled = false;

		@Override
		public File getFile() {
			return _File;
		}

		@Override
		public String getJarFileName() {
			return _LibName;
		}

		@Override
		public String getPluginName() {
			return _PluginName;
		}

		@Override
		public Object getPlugin() {
			return _Plugin_Instance;
		}

		@Override
		public Class<?> getPluginClass() {
			return _Plugin_Class;
		}

		@Override
		public ClassLoader getClassLoader() {
			return _ClassLoader;
		}

		@Override
		public String getPluginClassName() {
			return _PluginClassName;
		}

		@Override
		public boolean isEnabled() {
			return _Enabled;
		}
	}

	private interface IPluginClassScanner {
		public String[] getPluginClassesNames(Class<?> ainterface);
	}

	private class JarScanner implements IPluginClassScanner {
		private final File _FILE;

		public JarScanner(File jarfile) {
			_FILE = jarfile;
		}

		@Override
		public String[] getPluginClassesNames(Class<?> ainterface) {
			final ResizingArray<String> CLASSES = new ResizingArray<String>();

			JarFile JarFile = null;
			try {
				JarFile = new JarFile(_FILE);

				final MyStringBuffer FILENAME_BUFFER = new MyStringBuffer(32);

				final Enumeration<JarEntry> ENTRIES = JarFile.entries();
				while(ENTRIES.hasMoreElements()) {
					final JarEntry ENTRY = ENTRIES.nextElement();

					if(!ENTRY.isDirectory()) {
						FILENAME_BUFFER.append(ENTRY.getName());

						if(FILENAME_BUFFER.endsWith("Plugin.class")) {
							// Removes File Extension(*.class)
							final int INDEX = FILENAME_BUFFER.lastIndexOf('.');
							if(INDEX != -1) {
								FILENAME_BUFFER.reset(INDEX);

								// Reformat String Ex. From
								// [javax/swing/JButton] To [javax.swing.JButton]
								FILENAME_BUFFER.replace('/', '.');

								// MAYBE
								// Check to See if Plugin Is Allowed
//								if(isClassBlackListed(FILENAME_BUFFER.toString())) {
//							 		_DEBUGHANDLER.printInformation("Plugin: " + FILENAME_BUFFER.toString() + " Is Blacklisted");
//							 	} else {
								CLASSES.put(FILENAME_BUFFER.toString());
//							 	}
							}
						}

						FILENAME_BUFFER.reset();
					}
				}
			} catch(Exception e) {
				_LOG_.printError(e);
			} finally {
				if(JarFile != null) {
					try {
						JarFile.close();
					} catch(Exception e) {}
					JarFile = null;
				}
			}

			return (CLASSES.length() > 0 ? CLASSES.toArray(new String[CLASSES.length()]) : null);
		}
	}

	private class DirectoryScanner implements IPluginClassScanner {
		private final File _DIRECTORY;
		private final boolean _INCLUDE;

		public DirectoryScanner(File directory, boolean include) {
			_DIRECTORY = directory;
			_INCLUDE = include;
		}

		@Override
		public String[] getPluginClassesNames(Class<?> ainterface) {
			final ResizingArray<String> CLASSES = new ResizingArray<String>();

			scanDir(_DIRECTORY, CLASSES);

			return (CLASSES.length() > 0 ? CLASSES.toArray(new String[CLASSES.length()]) : null);
		}

		private void scanDir(File directroy, ResizingArray<String> output) {
			if(directroy.exists() && directroy.isDirectory()) {
				final MyStringBuffer FILENAME_BUFFER = new MyStringBuffer(32);

				final File[] FILES = directroy.listFiles();

				for(int X = 0; X < FILES.length; X++) {
					if(FILES[X].isDirectory()) {
						if(_INCLUDE) {
							scanDir(FILES[X], output);
						}
					} else {
						if(FILES[X].exists()) {
							FILENAME_BUFFER.append(FileUtil.removeDirectory(_DIRECTORY, FILES[X]));

							if(FILENAME_BUFFER.endsWith("Plugin.class")) {
								// Removes File Extension(*.class)
								final int INDEX = FILENAME_BUFFER.lastIndexOf('.');
								if(INDEX != -1) {
									FILENAME_BUFFER.reset(INDEX);

									// Reformat String Ex. From
									// [javax/swing/JButton] To [javax.swing.JButton]
									FILENAME_BUFFER.replace('/', '.');

									// MAYBE
									// Check to See if Plugin Is Allowed
//									if(isClassBlackListed(FILENAME_BUFFER.toString())) {
//								 		_DEBUGHANDLER.printInformation("Plugin: " + FILENAME_BUFFER.toString() + " Is Blacklisted");
//								 	} else {
									output.put(FILENAME_BUFFER.toString());
//								 	}
								}
							}

							FILENAME_BUFFER.reset();
						}
					}
				}
			}
		}
	}

	private class PluginClassLoader {
		private final ClassLoader _CLASSLOADER;

		private final String[] _PLUGIN_CLASSES;

		public PluginClassLoader(ClassLoader classloader, String[] pclasses) {
			if(classloader == null) {
				throw new RuntimeException("Variable[classloader] - Is Null");
			}

			if(pclasses == null) {
				throw new RuntimeException("Variable[pclasses] - Is Null");
			}

			_CLASSLOADER = classloader;
			_PLUGIN_CLASSES = pclasses;
		}

		public ClassLoader getClassLoader() {
			return _CLASSLOADER;
		}

		/**
		 * 
		 * @param index
		 * @return Class Name At Index
		 */
		public String getPluginClassName(int index) {
			if(validIndex(index)) {
				return _PLUGIN_CLASSES[index];
			}

			return null;
		}

		/**
		 * 
		 * @return The Number Of Available Plugin Class
		 */
		public int pluginCount() {
			return _PLUGIN_CLASSES.length;
		}

		private boolean pluginClassNameExists(String classname) {
			for(int X = 0; X < _PLUGIN_CLASSES.length; X++) {
				if(_PLUGIN_CLASSES[X].equals(classname)) {
					return true;
				}
			}

			return false;
		}

		/**
		 * Does Not Create New Instance Of Class
		 * 
		 * @param classname
		 * @return
		 */
		private Class<?> loadPluginClass(String classname) {// Ex. javax.swing.JButton
			if(pluginClassNameExists(classname)) {
				try {
					final Class<?> CLASS = _CLASSLOADER.loadClass(classname);

					if(CLASS != null) {
						if(!isPluginInterface(CLASS) && ClassUtil.implementsInterface(_PLUGIN_INTERFACE, CLASS)) {
							if(CLASS.isInterface()) {
								return null;
							} else {
								return CLASS;
							}
						}
					}
				} catch(Exception e) {
					_LOG_.printError("Plugin: " + classname + " Failed To Load");
					_LOG_.printError(e);
				}
			}

			return null;
		}

		private boolean validIndex(int index) {
			return (index >= 0 && index < _PLUGIN_CLASSES.length);
		}
	}
}
