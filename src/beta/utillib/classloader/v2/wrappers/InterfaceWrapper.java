package beta.utillib.classloader.v2.wrappers;

import beta.utillib.classloader.ClassUtil;

import utillib.arrays.ResizingArray;

import utillib.collections.Collection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// 3780
/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * Maps Classes To A Interface
 * 
 * You Might Be Able To Use This, Cause This Class Does Not Have Access To Classes Interclasses
 * Must Dollar Sign For InterClasses ex. beta.utillib.classloader.betav1.ClassWrapperBetav1$TestClass
 * 
 * August 17, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class InterfaceWrapper {
	private static final int _PUBLIC_METHOD_ = 0x0001;

	private final Class<?> _INTERFACE;

	//Array Of All The Methods In The Interface Class
	private final InterfaceMethod[] _METHODS;

	private final boolean _EXACT_METHODS;

	//<Class Name, Class>
	private final Collection<String, Class<?>> _CLASSES = new Collection<String, Class<?>>(2, 2);

	public InterfaceWrapper(String interfaceclassname) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassLoader.getSystemClassLoader(), interfaceclassname);
	}

	public InterfaceWrapper(Class<?> interfaceclazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassLoader.getSystemClassLoader(), interfaceclazz, true);
	}

	public InterfaceWrapper(ClassLoader classloader, String interfaceclassname) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(classloader, ClassUtil.loadClassEx(classloader, interfaceclassname), true);
	}

	public InterfaceWrapper(ClassLoader classloader, Class<?> ainterface, boolean exactmethods) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}
		if(ainterface == null) {
			throw new RuntimeException("Variable[ainterface] - Is Null");
		}

		_INTERFACE = ainterface;
		_EXACT_METHODS = exactmethods;

//        System.out.println("Class Name: " + instance.getClass().getName());

		final Method[] METHODS = ainterface.getMethods();

		if(METHODS.length == 0) {
			throw new RuntimeException("Variable[METHODS] - No Public Methods");
		}

		//Maps All Method Into A Standard Interface Methos
		final ResizingArray<InterfaceMethod> T_METHODS = new ResizingArray<InterfaceMethod>(METHODS.length);
		for(int X = 0; X < METHODS.length; X++) {
			if((METHODS[X].getModifiers() & _PUBLIC_METHOD_) != 0) {
				T_METHODS.put(new InterfaceMethod(METHODS[X]));

//                System.out.println("    Method Name: " + METHODS[X].getName());
			}
		}
		_METHODS = T_METHODS.toArray(new InterfaceMethod[T_METHODS.length()]);
	}

	public boolean addClass(String classname) {
		return addClass(ClassLoader.getSystemClassLoader(), classname);
	}

	public boolean addClass(ClassLoader classloader, String classname) {
		Class<?> Class = null;
		try {
			Class = getClassByName(classname);

			if(Class == null) {
				Class = ClassUtil.loadClassEx(classloader, classname);

				if(Class != null) {
					return addClass(Class);
				}
			}
		} catch(Exception e) {}

		return false;
	}

	public boolean addClass(Class<?> clazz) {
		final Class<?> CLASS = getClassByName(clazz.getName());

		if(CLASS == null) {
			if(_EXACT_METHODS && ClassUtil.implementsInterface(_INTERFACE, clazz)) {
				if(registerMethods(clazz) == _METHODS.length) {
					_CLASSES.add(clazz.getName(), clazz);
					return true;
				} else {
					unregisterMethods(clazz);
				}
			} else {
				if(registerMethods(clazz) > 0) {
					_CLASSES.add(clazz.getName(), clazz);
					return true;
				}
			}
		}

		return false;
	}

	public void removeClass(String classname) {
		unregisterMethods(classname);
		_CLASSES.remove(classname);
	}

	public void removeClass(Class<?> clazz) {
		unregisterMethods(clazz);
		_CLASSES.remove(clazz.getName());
	}

	public void removeClass(int index) {
		if(_CLASSES.validIndex(index)) {
			unregisterMethods(_CLASSES.getAt(index));

			_CLASSES.removeAt(index);
		}
	}

	public int classCount() {
		return _CLASSES.count();
	}

	public Class<?> getInterface() {
		return _INTERFACE;
	}

	public String isInstanceOf() {
		return _INTERFACE.getName();
	}

	public boolean instanceOf(Class<?> clazz) {
		if(_INTERFACE.equals(clazz)) {
			return true;
		} else {
			Class<?> Current = _INTERFACE;
			while((Current = Current.getSuperclass()) != null && !Current.equals(Object.class)) {
				if(Current.equals(clazz)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean instanceOf(Object instance) {
		if(_INTERFACE.equals(instance.getClass())) {
			return true;
		} else {
			Class<?> Current = _INTERFACE;
			while((Current = Current.getSuperclass()) != null && !Current.equals(Object.class)) {
				if(Current.equals(instance.getClass())) {
					return true;
				}
			}
		}

		return false;
	}

	public Object callMethod(Object instance, int index, Object... args) {
		final IClassMethodCall M_CALL = getMethod/* ByIndex */(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - MethodIndex[" + index + "] - Not Found");
		}

		return callMethod(instance, M_CALL, args);
	}

	public Object callMethod(Object instance, String name, Object... args) {
		final IClassMethodCall M_CALL = getMethod/* ByName */(name, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method[" + name + "] - Not Found");
		}

		return callMethod(instance, M_CALL, args);
	}

	public Object callMethod(Object instance, IClassMethodCall methodcall, Object... args) {
		if(instance == null) {
			throw new RuntimeException("Variable[instance] - Is Null");
		}
		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		return methodcall.call(instance, args);
	}

	public IClassMethodCall getMethod(int index) {
		if(index >= 0 && index < _METHODS.length) {
			return _METHODS[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public IClassMethodCall getMethod(String name, Object... args) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final InterfaceMethod METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				final Class<?>[] CLASSES = METHOD_CALL.getArgumentClasses();

				if(args.length == CLASSES.length) {
					if(ClassUtil.compareClassesToArgs(CLASSES, args)) {
						return METHOD_CALL;
					} else {
						throw new RuntimeException("Method: " + name + " Error Invalid Classs Arguments");
					}
				}
			}
		}

		return null;
	}

	public IClassMethodCall[] getMethods(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}
		final ResizingArray<InterfaceMethod> METHODS = new ResizingArray<InterfaceMethod>();

		for(int X = 0; X < _METHODS.length; X++) {
			final InterfaceMethod METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				METHODS.put(METHOD_CALL);
			}
		}

		return (METHODS.length() > 0 ? METHODS.toArray(new InterfaceMethod[METHODS.length()]) : null);
	}

	public IClassMethodCall getMethodWPN(String name, String... parameterclassnames) {
		return getMethodRWPN(name, "void", parameterclassnames);
	}

	public IClassMethodCall getMethodRWPN(String name, String returntype, String... parametertypes) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(returntype == null) {
			throw new RuntimeException("Variable[returntype] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final InterfaceMethod METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				if(!returntype.equals(METHOD_CALL.getReturnType().getCanonicalName())) {
					continue;
				}

				final Class<?>[] CLASSES = METHOD_CALL.getArgumentClasses();

				if(parametertypes.length == 0 && CLASSES.length == 0) {
					return METHOD_CALL;
				} else {
					if(parametertypes.length == CLASSES.length) {
						boolean Found = true;
						for(int Y = 0; Y < CLASSES.length; Y++) {
							if(!parametertypes[Y].equals(CLASSES[Y].getName())) {
								Found = false;
								break;
							}
						}

						if(Found) {
							return METHOD_CALL;
						}
					}
				}
			}
		}

		return null;
	}

	public IClassMethodCall getMethodWPC(String name, Class<?>... parametertypes) {
		return getMethodRWPC(name, void.class, parametertypes);
	}

	public IClassMethodCall getMethodRWPC(String name, Class<?> returntype, Class<?>... parametertypes) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(returntype == null) {
			throw new RuntimeException("Variable[returntype] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final InterfaceMethod METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				if(!returntype.equals(METHOD_CALL.getReturnType())) {
					continue;
				}

				if(ClassUtil.compareClasses(METHOD_CALL.getArgumentClasses(), parametertypes)) {
					return METHOD_CALL;
				}
			}
		}

		return null;
	}

	public int methodCount() {
		return _METHODS.length;
	}

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param index
	 * @return
	 */
	public String[][] getMethodArgClassNames(int index) {
		final InterfaceMethod M_CALL = (InterfaceMethod)getMethod(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return getMethodArgClassNames(M_CALL.getMethodName());
	}

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param methodname
	 * @param args
	 * @return
	 */
	public String[][] getMethodArgClassNames(String methodname) {
		if(methodname != null) {
			final InterfaceMethod[] METHODS = (InterfaceMethod[])getMethods(methodname);

			if(METHODS != null) {
				final String[][] RETURN = new String[METHODS.length][];

				for(int X = 0; X < METHODS.length; X++) {
					final ResizingArray<String> ARGS = new ResizingArray<String>();

					final Class<?>[] CLASSES = METHODS[X].getArgumentClasses();

					for(int Y = 0; Y < CLASSES.length; Y++) {
						ARGS.put(CLASSES[Y].getCanonicalName());
					}

					RETURN[X] = ARGS.toArray(new String[ARGS.length()]);
				}

				return RETURN;
			}
		}

		return null;
	}

	public Class<?> getClassByName(String classname) {
		return _CLASSES.get(classname);
	}

//    public Object newInstance(String classname) {
//        try {
//            final Class<?> CLASS = getClassByName(classname);
//
//            if(CLASS != null) {
//                return ClassUtil.newInstanceEx(CLASS);
//            }
//        } catch (Exception e) {}
//
//        return null;
//    }

	public Object newInstance(String classname, Object... args) {
		try {
			final Class<?> CLASS = _CLASSES.get(classname);

			if(CLASS != null) {
				return ClassUtil.newInstanceEx(CLASS, args);
			}
		} catch(Exception e) {}

		return null;
	}

	/**
	 * 
	 * @param clazz
	 * @return The Number Of Methods That Were Added
	 */
	private int registerMethods(Class<?> clazz) {
		int Counter = 0;
		for(int X = 0; X < _METHODS.length; X++) {
			//Gets The Equivalent Method In The Given Class From The Interface Class Method 
			final Method METHOD = getMethod(_METHODS[X], clazz);

			if(METHOD != null) {
				_METHODS[X].addMethod(clazz, METHOD);
				Counter++;
			}
		}

		return Counter;
	}

	private void unregisterMethods(String classname) {
		for(int Y = 0; Y < _METHODS.length; Y++) {
			_METHODS[Y].removeMethod(classname);
		}
	}

	private void unregisterMethods(Class<?> clazz) {
		for(int Y = 0; Y < _METHODS.length; Y++) {
			_METHODS[Y].removeMethod(clazz);
		}
	}

	//CLASSES
	public class InterfaceMethod implements IClassMethodCall, IMethodCallExtended {
		private final String _METHOD_NAME;
		private final Class<?>[] _ARGUMENTS_CLASSES;
		private final Class<?> _RETURN_TYPE;

		private final Collection<Class<?>, MethodCall> _METHODS = new Collection<Class<?>, MethodCall>(1, 2);

		public InterfaceMethod(Method method) {
			_METHOD_NAME = method.getName();
			_ARGUMENTS_CLASSES = method.getParameterTypes();
			_RETURN_TYPE = method.getReturnType();
		}

		public void addMethod(Class<?> clazz, Method method) {
			_METHODS.add(clazz, new MethodCall(method));
		}

		public void removeMethod(String classname) {
			for(int X = 0; X < _METHODS.count(); X++) {
				final Collection<Class<?>, MethodCall>.Entry ENTRY = _METHODS.getEntryAt(X);

				if(((Class<?>)ENTRY.getKey()).getName().equals(classname)) {
					_METHODS.removeAt(X);
					break;
				}
			}
		}

		public void removeMethod(Class<?> clazz) {
			_METHODS.remove(clazz);
		}

		public int methodCount() {
			return _METHODS.count();
		}

		public IClassMethodCall getClassMethod(Class<?> clazz) {
			return _METHODS.get(clazz);
		}

		public IClassMethodCall getClassMethod(String classname) {
			for(int X = 0; X < _METHODS.count(); X++) {
				final Collection<Class<?>, MethodCall>.Entry ENTRY = _METHODS.getEntryAt(X);

				if(((Class<?>)ENTRY.getKey()).getName().equals(classname)) {
					return (IClassMethodCall)ENTRY.getObject();
				}
			}

			return null;
		}

		@Override
		public String getMethodName() {
			return _METHOD_NAME;
		}

		@Override
		public Object call(Object instance, Object... args) {
			if(instance == null) {
				throw new RuntimeException("Variable[instance] - Is Null");
			}

			try {
				final MethodCall MCALL = _METHODS.get(instance.getClass());

				if(MCALL == null) {
					throw new Exception("Class:" + ' ' + instance.getClass().getCanonicalName() + ' ' + "Not Found");
				}

				return _METHODS.get(instance.getClass()).call(instance, args);
			} catch(Exception e) {
				return new MethodCallError(e);
			}
		}

		@Override
		public Class<?>[] getArgumentClasses() {
			return _ARGUMENTS_CLASSES;
		}

		@Override
		public Class<?> getReturnType() {
			return _RETURN_TYPE;
		}

		@Override
		public String toString() {
			StringBuffer Buffer = new StringBuffer("Name: ");

			Buffer.append(_METHOD_NAME);

			if(_ARGUMENTS_CLASSES.length > 0) {
				Buffer.append(", ");
				Buffer.append(_ARGUMENTS_CLASSES.length);
				Buffer.append(" Argument(s): ");

				for(int X = 0; X < _ARGUMENTS_CLASSES.length; X++) {
					Buffer.append(_ARGUMENTS_CLASSES[X].getCanonicalName());

					if(X < (_ARGUMENTS_CLASSES.length - 1)) {
						Buffer.append(", ");
					}
				}
			}

			return Buffer.toString();
		}
	}

	public class MethodCall implements IClassMethodCall {
		private final Method _METHOD;

		public MethodCall(Method method) {
			if(method == null) {
				throw new RuntimeException("Variable[method] - Is Null");
			}

			_METHOD = method;
		}

		@Override
		public Object call(Object instance, Object... args) {
			try {
				return _METHOD.invoke(instance, args);
			} catch(Exception e) {
				return new MethodCallError(e);
			}
		}
	}

	//STATIC
	private static Method getMethod(IMethodCallExtended call, Class<?> clazz) {
		final Method METHOD;
		try {
			METHOD = clazz.getMethod(call.getMethodName(), call.getArgumentClasses());

			if((METHOD.getReturnType().equals(call.getReturnType()))) {
				return METHOD;
			}
		} catch(Exception e) {}

		return null;
	}

	public static InterfaceWrapper create(String interfaceclassname) {
		try {
			return new InterfaceWrapper(interfaceclassname);
		} catch(Exception e) {}

		return null;
	}

	public static InterfaceWrapper create(Class<?> clazz) {
		try {
			return new InterfaceWrapper(clazz);
		} catch(Exception e) {}

		return null;
	}

	public static InterfaceWrapper create(ClassLoader classloader, String interfaceclassname) {
		try {
			return new InterfaceWrapper(classloader, interfaceclassname);
		} catch(Exception e) {}

		return null;
	}

	public static InterfaceWrapper create(ClassLoader classloader, Class<?> clazz) {
		try {
			return new InterfaceWrapper(classloader, clazz, true);
		} catch(Exception e) {}

		return null;
	}

	public static InterfaceWrapper create(ClassLoader classloader, Class<?> clazz, boolean exactmethods) {
		try {
			return new InterfaceWrapper(classloader, clazz, exactmethods);
		} catch(Exception e) {}

		return null;
	}
}
