package beta.utillib.classloader;

import beta.utillib.classloader.v2.wrappers.MethodCallError;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Dalton Dell
 */
public class ClassUtil {
	private static DebugLogger _LOG_ = LogManager.getInstance().getLogger(ClassUtil.class);

	public final static MethodCallError _CALL_ERROR_ = new MethodCallError();

//return (aclass == _PLUGIN_INTERFACE);
	public static boolean implementsInterface(Class<?> interfaceclass, Class<?> clazz) {
		final Method[] M_INTERFACE = interfaceclass.getMethods();

		if(!interfaceclass.isInterface()) {
			final Class<?> SUPER_CLASS = clazz.getSuperclass();

			if(!SUPER_CLASS.equals(interfaceclass)) {
				return false;
			}
		}

		for(int X = 0; X < M_INTERFACE.length; X++) {
			final Method METHOD;
			try {
				METHOD = clazz.getMethod(M_INTERFACE[X].getName(), M_INTERFACE[X].getParameterTypes());

				if(!(METHOD.getReturnType().equals(M_INTERFACE[X].getReturnType()))) {
					return false;
				}
			} catch(Exception e) {
				_LOG_.printError("Class: " + clazz.getCanonicalName() + " Method: " + M_INTERFACE[X].getName() + " - Does Not Exists");

				return false;
			}
		}

		return true;
	}

	public static Class<?> loadClassEx(ClassLoader classloader, String classname) throws ClassNotFoundException {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}
		if(classname == null) {
			throw new RuntimeException("Variable[classname] - Is Null");
		}

		return classloader.loadClass(classname);
	}

	public static boolean isCallError(Object value) {
		if(value == null) {
			return false;
		}

		return (value instanceof MethodCallError);
	}

	/**
	 *
	 * @param clazz Class To Create New Instance From
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 */
	public static Object newInstance(Class<?> clazz, Object... args) {
		try {
			return newInstanceEx(clazz, args);
		} catch(Exception e) {}

		return null;
	}

	/**
	 *
	 * @param clazz Class To Create New Instance From
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(Class<?> clazz, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		if(args != null && args.length > 0) {
			final Class<?>[] CLASSES = new Class[args.length];
			for(int X = 0; X < args.length; X++) {
				CLASSES[X] = args[X].getClass();
			}

			Constructor<?> Constructor = null;
			try {
				Constructor = clazz.getConstructor(CLASSES);
			} catch(Exception e) {
				final Constructor<?>[] CONSTRUCTORS = clazz.getConstructors();

				for(int X = 0; X < CONSTRUCTORS.length; X++) {
					if(compareClassesToArgs(CONSTRUCTORS[X].getParameterTypes(), args)) {
						Constructor = CONSTRUCTORS[X];
						break;
					}
				}

				if(Constructor == null) {
					for(int X = 0; X < CONSTRUCTORS.length; X++) {
						try {
							return CONSTRUCTORS[X].newInstance(args);
						} catch(Exception e2) {}
					}
				}
			}

			if(Constructor == null) {
				throw new NoSuchMethodException();
			} else {
				return Constructor.newInstance(args);
			}
		} else {
			return clazz.newInstance();
		}
	}

	/**
	 *
	 * @param classname Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 */
	public static Object newInstance(String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return newInstance(ClassLoader.getSystemClassLoader(), classname, args);
	}

	/**
	 *
	 * @param classloader Classlodaer To Search For Class
	 * @param classname Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 */
	public static Object newInstance(ClassLoader classloader, String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		try {
			return newInstanceEx(classloader, classname, args);
		} catch(Exception e) {}

		return null;
	}

	/**
	 *
	 * @param classname Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return newInstanceEx(ClassLoader.getSystemClassLoader(), classname, new Object[] {});
	}

	/**
	 *
	 * @param classname Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return newInstanceEx(ClassLoader.getSystemClassLoader(), classname, args);
	}

	/**
	 *
	 * @param classloader Classlodaer To Search For Class
	 * @param classname Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(ClassLoader classloader, String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(classname == null) {
			throw new RuntimeException("Variable[classname] - Is Null");
		}

		if(args == null) {
			throw new RuntimeException("Variable[args] - Is Null");
		}

		final Class<?> CLASS = classloader.loadClass(classname);

		if(CLASS == null) {
			throw new ClassNotFoundException(classname);
		} else {
			final Object INSTANCE = newInstanceEx(CLASS, args);

			if(INSTANCE == null) {
				throw new InstantiationException("Failed To Create Instance" + (CLASS == null ? "" : " For Class: " + CLASS.getCanonicalName()));
			}

			return INSTANCE;
		}
	}

	public static Object call(Object instance, String methodname, Object... args) {
		return call(instance.getClass(), instance, methodname, args);
	}

	public static Object call(String classname, Object instance, String methodname, Object... args) {
		return call(ClassLoader.getSystemClassLoader(), classname, instance, methodname, args);
	}

	public static Object call(ClassLoader classloader, String classname, Object instance, String methodname, Object... args) {
		try {
			return call(loadClassEx(classloader, classname), instance, methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object call(Class<?> clazz, Object instance, String methodname, Object... args) {
		try {
			return callEx(clazz, instance, methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object staticCall(Object instance, String methodname, Object... args) {
		return staticCall(instance.getClass(), methodname, args);
	}

	public static Object staticCall(String classname, String methodname, Object... args) {
		return staticCall(ClassLoader.getSystemClassLoader(), classname, methodname, args);
	}

	public static Object staticCall(ClassLoader classloader, String classname, String methodname, Object... args) {
		try {
			return staticCall(loadClassEx(classloader, classname), methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object staticCall(Class<?> clazz, String methodname, Object... args) {
		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		if(methodname == null) {
			throw new RuntimeException("Variable[methodname] - Is Null");
		}

		try {
			return staticCallEx(clazz, methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object callEx(Object instance, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return callEx(instance.getClass(), instance, methodname, args);
	}

	public static Object callEx(String classname, Object instance, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return callEx(ClassLoader.getSystemClassLoader(), classname, instance, methodname, args);
	}

	public static Object callEx(ClassLoader classloader, String classname, Object instance, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			return callEx(loadClassEx(classloader, classname), instance, methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object callEx(Class<?> clazz, Object instance, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		if(methodname == null) {
			throw new RuntimeException("Variable[methodname] - Is Null");
		}

		final Method METHOD = getMethodWP(clazz, false, methodname, argsToClasses(args));

		if(METHOD != null) {
			return METHOD.invoke(instance, args);
		} else {
			throw new NoSuchMethodException("Missing Method: " + methodname);
		}
	}

	public static Object staticCallEx(Object instance, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return staticCallEx(instance.getClass(), methodname, args);
	}

	public static Object staticCallEx(String classname, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return staticCallEx(ClassLoader.getSystemClassLoader(), classname, methodname, args);
	}

	public static Object staticCallEx(ClassLoader classloader, String classname, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		try {
			return staticCallEx(loadClassEx(classloader, classname), methodname, args);
		} catch(Exception e) {
			return new MethodCallError(e);
		}
	}

	public static Object staticCallEx(Class<?> clazz, String methodname, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		if(methodname == null) {
			throw new RuntimeException("Variable[methodname] - Is Null");
		}

		final Method METHOD = getMethodWP(clazz, true, methodname, argsToClasses(args));

		if(METHOD != null) {
			return METHOD.invoke(null, args);
		} else {
			throw new NoSuchMethodException("Missing Method: " + methodname);
		}
	}

	public static Method getMethod(Class<?> clazz, String name) {
		return getMethod(clazz, false, name);
	}

	public static Method getMethod(Class<?> clazz, boolean isstatic, String name) {
		final Method[] METHODS = clazz.getMethods();

		for(int X = 0; X < METHODS.length; X++) {
			final Method METHOD = METHODS[X];

			if(METHOD.getName().equals(name)) {
				final Class<?>[] CLASSES = METHOD.getParameterTypes();

				if(CLASSES.length == 0) {
					final boolean IS_STATIC = (METHOD.getModifiers() & 0x0008) != 0;

					if(isstatic) {
						if(IS_STATIC) {
							return METHOD;
						}
					} else {
						if(!IS_STATIC) {
							return METHOD;
						}
					}
				}
			}
		}

		return null;
	}

	public Method getMethodWPN(Class<?> clazz, String name, String... argsclassname) {
		return getMethodWPN(clazz, false, name, argsclassname);
	}

	public Method getMethodWPN(Class<?> clazz, boolean isstatic, String name, String... argsclassname) {
		final Method[] METHODS = clazz.getMethods();

		for(int X = 0; X < METHODS.length; X++) {
			final Method METHOD_CALL = METHODS[X];

			if(METHOD_CALL.getName().equals(name)) {
				final Class<?>[] CLASSES = METHOD_CALL.getParameterTypes();

				if(argsclassname.length == 0 && CLASSES.length == 0) {
					return METHOD_CALL;
				} else {
					if(argsclassname.length == CLASSES.length) {
						for(int Y = 0; Y < CLASSES.length; Y++) {
							if(!argsclassname[Y].equals(CLASSES[Y].getName())) {
								continue;
							}
						}

						final boolean IS_STATIC = (METHOD_CALL.getModifiers() & 0x0008) != 0;

						if(isstatic) {
							if(IS_STATIC) {
								return METHOD_CALL;
							}
						} else {
							if(!IS_STATIC) {
								return METHOD_CALL;
							}
						}
					}
				}
			}
		}

		return null;
	}

	public static Method getMethodWP(Class<?> clazz, String name, Class<?>... parameters) {
		return getMethodWP(clazz, false, name, parameters);
	}

	public static Method getMethodWP(Class<?> clazz, boolean isstatic, String name, Class<?>... parameters) {
		final Method[] METHODS = clazz.getMethods();

		for(int X = 0; X < METHODS.length; X++) {
			final Method METHOD = METHODS[X];

//            final boolean IS_STATIC = (METHOD.getModifiers() & 0x0008) != 0;

//            if(isstatic && !IS_STATIC) {continue;}

			if(METHOD.getName().equals(name)) {
				final Class<?>[] CLASSES = METHOD.getParameterTypes();

				if(compareClasses(CLASSES, parameters)) {
					final boolean IS_STATIC = (METHOD.getModifiers() & 0x0008) != 0;

					if(isstatic) {
						if(IS_STATIC) {
							return METHOD;
						}
					} else {
						if(!IS_STATIC) {
							return METHOD;
						}
					}
				}
			}
		}

		return null;
	}

	public static boolean compareClassesToArgs(Class<?>[] classes, Object[] args) {
//        if(classes == null || args == null) {
//            return false;
//        }
		if(classes.length == 0 && args.length == 0) {
			return true;
		} else if(classes.length == args.length) {
			for(int X = 0; X < classes.length; X++) {
				final Class<?> C1 = classes[X];
				final Class<?> C2 = args[X].getClass();

				if(!compareClass(C1, C2)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public static boolean compareClasses(Class<?>[] classes1, Class<?>[] classes2) {
		if(classes1.length == 0 && classes2.length == 0) {
			return true;
		} else if(classes1.length == classes2.length) {
			for(int Y = 0; Y < classes1.length; Y++) {
				final Class<?> C1 = classes1[Y];
				final Class<?> C2 = classes2[Y];

				if(!compareClass(C1, C2)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private static boolean compareClass(Class<?> class1, Class<?> class2) {
		if(class1 == class2) {
			return true;
		} else {
			if(class1.isPrimitive()) {
				if(Byte.class.equals(class2)) {
					return true;
				} else if(Short.class.equals(class2)) {
					return true;
				} else if(Integer.class.equals(class2)) {
					return true;
				} else if(Character.class.equals(class2)) {
					return true;
				} else if(Long.class.equals(class2)) {
					return true;
				} else if(Float.class.equals(class2)) {
					return true;
				} else if(Double.class.equals(class2)) {
					return true;
				} else if(Boolean.class.equals(class2)) {
					return true;

				} else if(Byte[].class.equals(class2)) {
					return true;
				} else if(Short[].class.equals(class2)) {
					return true;
				} else if(Integer[].class.equals(class2)) {
					return true;
				} else if(Character[].class.equals(class2)) {
					return true;
				} else if(Long[].class.equals(class2)) {
					return true;
				} else if(Float[].class.equals(class2)) {
					return true;
				} else if(Double[].class.equals(class2)) {
					return true;
				} else if(Boolean[].class.equals(class2)) {
					return true;

				} else {
					return false;
				}
			} else if(class2.isPrimitive()) {
				if(Byte.class.equals(class1)) {
					return true;
				} else if(Short.class.equals(class1)) {
					return true;
				} else if(Integer.class.equals(class1)) {
					return true;
				} else if(Character.class.equals(class1)) {
					return true;
				} else if(Long.class.equals(class1)) {
					return true;
				} else if(Float.class.equals(class1)) {
					return true;
				} else if(Double.class.equals(class1)) {
					return true;
				} else if(Boolean.class.equals(class2)) {
					return true;

				} else if(Byte[].class.equals(class1)) {
					return true;
				} else if(Short[].class.equals(class1)) {
					return true;
				} else if(Integer[].class.equals(class1)) {
					return true;
				} else if(Character[].class.equals(class1)) {
					return true;
				} else if(Long[].class.equals(class1)) {
					return true;
				} else if(Float[].class.equals(class1)) {
					return true;
				} else if(Double[].class.equals(class1)) {
					return true;
				} else if(Boolean[].class.equals(class2)) {
					return true;

				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	private static Class<?>[] argsToClasses(Object... args) {
		if(args.length == 0) {
			return new Class[0];
		} else {
			final Class<?>[] CLASSES = new Class[args.length];

			for(int X = 0; X < args.length; X++) {
				CLASSES[X] = args[X].getClass();
			}

			return CLASSES;
		}
	}

//	private static Object bruteForceNewInstance(Constructor<?>[] constructors, Object[] args) {
//		for(int X = 0; X < constructors.length; X++) {
//			final Object INSTANCE = bruteForceNewInstance(constructors[X], args);
//
//			if(INSTANCE != null) {
//				return INSTANCE;
//			}
//		}
//
//		return null;
//	}
//
//	private static Object bruteForceNewInstance(Constructor<?> constructor, Object[] args) {
//		final Class<?>[] PARA_TYPES = constructor.getParameterTypes();
//
//		if(PARA_TYPES.length != args.length) {
//			return null;
//		}
//
//		final Object[] CLONE_ARGS = args.clone();
//
//		for(int X = 0; X < args.length; X++) {
//			try {
//				CLONE_ARGS[X] = PARA_TYPES[X].cast(CLONE_ARGS[X]);
//			} catch(Exception e2) {
//				return null;
//			}
//
//			try {
//				return constructor.newInstance(args);
//			} catch(Exception e) {}
//		}
//
//		return null;
//	}
}
