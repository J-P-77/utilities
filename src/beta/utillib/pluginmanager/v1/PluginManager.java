package beta.utillib.pluginmanager.v1;

import beta.utillib.Listener;
import beta.utillib.classloader.v1.JarClassLoader;
import beta.utillib.classloader.ClassUtil;

import utillib.strings.MyStringBuffer;

import utillib.exceptions.InvalidFileTypeException;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.interfaces.IPluginListener;
import utillib.interfaces.IPluginListener.Event;

import utillib.pluginmanager.global.Parameter;

import utillib.arrays.ResizingArray;

import utillib.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.Enumeration;
import java.util.jar.JarEntry;

/**
 * <pre>
 * <b>Current Version 1.0.2</b>
 * 
 * September 24, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * March 12, 2009 (Version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * May 5, 2010 (Version 1.0.2)
 *     -Added
 *         -In Use (Tells If The Plugin Is In Use)
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class PluginManager {
	private static final DebugLogger _LOG_ = LogManager.getInstance().getLogger(PluginManager.class);

	public static final String _PLUGIN_DIRECTORY_ = "plugins";

	private final Class<?> _PLUGIN_INTERFACE;

	private final Listener<IPluginListener> _PLUGINLISTENERS = new Listener<IPluginListener>();

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

	public Listener<IPluginListener> getPluginListener() {
		return _PLUGINLISTENERS;
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
		loadDirectory(FileUtil.getAppPath(FileUtil._S_ + _PLUGIN_DIRECTORY_));
	}

	// Load Lib Directory
	public void loadDirectory(String directory) {
		loadDirectory(new File(directory));
	}

	public void loadDirectory(File directory) {
		if(directory == null) {
			_LOG_.printError(new RuntimeException("Variable[directory] - Is Null"));
		} else if(!directory.exists()) {
			_LOG_.printError(new RuntimeException("Variable[directory] - " + directory.getPath() + " Does Not Exists"));
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

	public void loadLibrary(File file) {
		PluginClassLoader PLoader = null;
		try {
			PLoader = new PluginClassLoader(file, _PLUGIN_INTERFACE);

			// Load Plugins
			for(int X = 0; X < PLoader.pluginCount(); X++) {
				final String PLUGIN_CLASS;
				try {
					PLUGIN_CLASS = PLoader.getPluginClassName(X);

					// Check to See if Plugin Is Allowed To Load
					if(isClassBlackListed(PLUGIN_CLASS)) {
						_LOG_.printInformation("Plugin[" + PLUGIN_CLASS + "] Is Blacklisted - Plugin Did Not Load");
					} else {
						final PluginInfo PINFO = new PluginInfo();

						PINFO._File = PLoader.getFile();
						PINFO._LibName = PLoader.getFile().getName();
						PINFO._ClassLoader = PLoader;

						PINFO._PluginClassName = PLUGIN_CLASS;
						PINFO._Plugin_Class = PLoader.loadPlugin(PINFO._PluginClassName);

						if(_CREATE_INSTANCE) {
							PINFO._Plugin_Instance = PINFO._Plugin_Class.newInstance();
						}

						PINFO._Enabled = (PINFO._Plugin_Instance == null ? false : true);

						if(PINFO._Enabled && PINFO._Plugin_Instance != null) {
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
				} catch(InstantiationException e) {
					_LOG_.printError("Failed To Create Instance For Plugin: " + file.getPath());
				} catch(IllegalAccessException e) {
					_LOG_.printError("Does Not Have A Public Constructor For Plugin: " + file.getPath());
				} catch(Exception e) {
					_LOG_.printError("While Loading Plugin: " + file.getPath());
					_LOG_.printError(e);
				}
			}
		} catch(FileNotFoundException e) {
			_LOG_.printError("Plugin: " + file.getPath() + " Not Found");
		} catch(InvalidFileTypeException e) {
			_LOG_.printError("Plugin: " + file.getPath() + " Not A Jar/Zip File");
/*} catch(IOException e) {*/
		} catch(Exception e) {
			_LOG_.printError("While Loading Plugin: " + file.getPath());
			_LOG_.printError(e);
		}
	}

	public void loadClass(Class<?> clazz, Parameter... parameters) {
		if(clazz == null) {
			_LOG_.printError(new RuntimeException("Variable[clazz] - Is Null"));
		} else {
			if(!ClassUtil.implementsInterface(_PLUGIN_INTERFACE, clazz)) {
				_LOG_.printError(new RuntimeException("Variable[clazz] - " + clazz.getName() + " Does Not Implement Interface"));
			} else {
				// Check to See if Plugin Is Allowed To Load
				if(isClassBlackListed(clazz.getName())) {
					_LOG_.printInformation("Plugin: " + clazz.getName() + " Is Blacklisted(Plugin Did Not Load)");
				} else {
					try {
						final PluginInfo PINFO = new PluginInfo();

						PINFO._File = null;
						PINFO._LibName = null;
						PINFO._ClassLoader = clazz.getClassLoader();
						PINFO._Plugin_Class = clazz;
						PINFO._PluginClassName = clazz.getName();

						if(parameters.length > 0) {
							final Class<?>[] CLASSES = new Class[parameters.length];
							final Object[] ARGUMENTS = new Object[parameters.length];

							for(int X = 0; X < parameters.length; X++) {
								CLASSES[X] = parameters[X].getType();
								ARGUMENTS[X] = parameters[X].getArgument();
							}

							final Constructor<?> CONSTRUCTOR = clazz.getConstructor(CLASSES);

							if(CONSTRUCTOR == null) {
								PINFO._Enabled = false;
							} else {
								if(_CREATE_INSTANCE) {
									PINFO._Plugin_Instance = CONSTRUCTOR.newInstance(ARGUMENTS);
								}
								PINFO._Enabled = true;
							}
						} else {
							if(_CREATE_INSTANCE) {
								PINFO._Plugin_Instance = clazz.newInstance();
							}
							PINFO._Enabled = true;
						}

						if(PINFO._Enabled && PINFO._Plugin_Instance != null) {
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

	public void unloadLib(String libname) {// Jar/Zip File Name (ex.
// Example.jar)
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
					_LOG_.printInformation("Plugin" + PINFO.getPluginName() + " Is Enabled");
				} else {
					sendEvent(Event.PLUGIN_DISABLED, PINFO);// Plugin Disabled
					_LOG_.printInformation("Plugin: " + PINFO.getPluginName() + " Is Disabled");
				}
			}
		}
	}

	public void enablePlugin(String pluginname, boolean enable) {
		final int INDEX = getPluginInfoIndex(pluginname);

		if(INDEX != -1) {
			enablePlugin(INDEX, enable);
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
				if(classname.equals(_BlackList.getAt(X))) {
					return true;
				}
			}

			return false;
		}
	}

	private void sendEvent(Event event, IPluginInfo plugininfo) {
		for(int X = 0; X < _PLUGINLISTENERS.listenerCount(); X++) {
			final IPluginListener LISTENER = _PLUGINLISTENERS.getListenerAt(X);

			if(LISTENER != null) {
				LISTENER.handleEvent(event, plugininfo);
			}
		}
	}

	private IPluginInfo getPluginInfoByIndex(int index) {
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

	// STATIC
	private static String getPluginName(Object instance) {
		Method M = null;
		try {
			M = instance.getClass().getMethod("getPluginName");

			final Object OBJ = M.invoke(instance);

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

// public void enabled(boolean value) {
// _Enabled = value;
// }

		@Override
		public boolean isEnabled() {
			return _Enabled;
		}
	}

	public class PluginClassLoader extends JarClassLoader {
		private ResizingArray<String> _PLUGIN_CLASSNAMES = new ResizingArray<String>();

		private final Class<?> _PLUGIN_INTERFACE;

		public PluginClassLoader(String path, Class<?> ainterface) throws IOException, FileNotFoundException, InvalidFileTypeException {
			this(new File(path), ainterface);
		}

		public PluginClassLoader(File jarfile, Class<?> ainterface) throws IOException, FileNotFoundException, InvalidFileTypeException {
			super(jarfile);

			if(ainterface == null) {
				throw new RuntimeException("Variable[ainterface] - Is Null");
			}

			_PLUGIN_INTERFACE = ainterface;

			final MyStringBuffer FILENAME_BUFFER = new MyStringBuffer(32);

			final Enumeration<JarEntry> ENTRIES = super.getJarEntries();
			while(ENTRIES.hasMoreElements()) {
				final JarEntry ENTRY = ENTRIES.nextElement();

				if(!ENTRY.isDirectory()) {
					FILENAME_BUFFER.append(ENTRY.getName());

					if(FILENAME_BUFFER.endsWith("Plugin.class")) {
						// Removes File Extension(*.class)
						final int INDEX = FILENAME_BUFFER.lastIndexOf('.');
						if(INDEX != -1) {
							FILENAME_BUFFER.reset(INDEX);
						}
						// Reformat String Ex. From [javax/swing/JButton] To
						// [javax.swing.JButton]
						FILENAME_BUFFER.replace('/', '.');

						if(!FILENAME_BUFFER.equals(ainterface.getCanonicalName())) {
							_PLUGIN_CLASSNAMES.put(FILENAME_BUFFER.toString());
						}
					}

					FILENAME_BUFFER.reset();
				}
			}
		}

		/**
		 * Does Not Create New Instance Of Class
		 * 
		 * @param index
		 * @return
		 */
		public Object loadPlugin(int index) {
			if(_PLUGIN_CLASSNAMES.validIndex(index)) {
				return loadPluginClass(_PLUGIN_CLASSNAMES.getAt(index));
			} else {
				return null;// Plugin Does Not Exists
			}
		}

		/**
		 * Does Not Create New Instance Of Class
		 * 
		 * @param classname
		 * @return
		 */
		public Class<?> loadPlugin(String classname) {
			final int INDEX = findPluginClass(classname);

			if(INDEX == -1) {
				return null;// Plugin Does Not Exists
			} else {
				return loadPluginClass(classname);
			}
		}

		/**
		 * 
		 * @param index
		 * @return Class Name At Index
		 */
		public String getPluginClassName(int index) {
			return _PLUGIN_CLASSNAMES.getAt(index);
		}

		/**
		 * 
		 * @return The Number Of Available Plugin Class
		 */
		public int pluginCount() {
			return _PLUGIN_CLASSNAMES.length();
		}

		private int findPluginClass(String classname) {
			for(int X = 0; X < _PLUGIN_CLASSNAMES.length(); X++) {
				if(classname.equals(_PLUGIN_CLASSNAMES.getAt(X))) {
					return X;
				}
			}

			return -1;
		}

		/**
		 * 
		 * Does Not Create New Instance Of Class
		 * 
		 * @param classname
		 * @return
		 */
		private Class<?> loadPluginClass(String classname) {// Ex javax.swing.JButton
			final Class<?> CLASS = super.loadClass(classname);

			if(CLASS != null) {
				if(!isPluginInterface(CLASS) && ClassUtil.implementsInterface(_PLUGIN_INTERFACE, CLASS)) {
					if(CLASS.isInterface()) {
						return null;
					} else {
						try {
							return CLASS;
						} catch(Exception e) {
							_LOG.printError("Plugin: " + classname + " Failed To Load");
							_LOG.printError(e);
						}
					}
				}
			}

			return null;
		}

		private boolean isPluginInterface(Class<?> aclass) {
			return (aclass == _PLUGIN_INTERFACE);
		}
	}
}
