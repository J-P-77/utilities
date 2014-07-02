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

package beta.utillib.classloader.v2.wrappers;

import beta.utillib.classloader.ClassUtil;

import utillib.arrays.ResizingArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * Wrapper For A Class
 * 
 * You Might Be Able To Use This, Cause This Class Does Not Have Access To Classes Interclasses
 * Must Dollar Sign For InterClasses ex. beta.utillib.classloader.betav1.ClassWrapperBetav1$TestClass
 * 
 * August 16, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * September 28, 2011 (Version 1.0.1)
 *     -Bug Fix
 *         -Constructor(Object) The Object Instead Of The Class Was Being Passed To loadMethods(ClassWrapper, Class)
 *         
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ClassWrapper {
	private static final int _PUBLIC_METHOD_ = 0x0001;

	private final Class<?> _CLASS;

	private final MethodCall[] _METHODS;

	public ClassWrapper(String classname) throws ClassNotFoundException {
		this(ClassLoader.getSystemClassLoader(), classname);
	}

	public ClassWrapper(Class<?> clazz) {
		this(clazz.getClassLoader(), clazz);
	}

	public ClassWrapper(ClassLoader classloader, String classname) throws ClassNotFoundException {
		this(classloader, ClassUtil.loadClassEx(classloader, classname));
	}

	public ClassWrapper(ClassLoader classloader, Class<?> clazz) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		_CLASS = clazz;
		_METHODS = loadMethods(this, _CLASS);
	}

	public ClassWrapper(Object clazz) {
		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		_CLASS = clazz.getClass();
		_METHODS = loadMethods(this, _CLASS.getClass());
	}

	public Class<?> getThisClass() {
		return _CLASS;
	}

	public String isInstanceOf() {
		return _CLASS.getCanonicalName();
	}

	public boolean instanceOf(Class<?> clazz) {
		if(_CLASS.equals(clazz)) {
			return true;
		} else {
			Class<?> Current = _CLASS;
			while((Current = Current.getSuperclass()) != null /*&& !Current.equals(Object.class)*/) {
				if(Current.equals(clazz)) {
					return true;
				}
			}
		}

		return false;
	}

	public Object callMethod(Object instance, int index, Object... args) {
		final IClassMethodCall M_CALL = getMethodByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return callMethod(instance, M_CALL, args);
	}

	public Object callMethod(Object instance, String name, Object... args) {
		final IClassMethodCall M_CALL = getMethod(name, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method: " + name + " Not Found");
		}

		return callMethod(instance, M_CALL, args);
	}

	private Object callMethod(Object instance, IClassMethodCall methodcall, Object... args) {
		if(instance == null) {
			throw new RuntimeException("Variable[instance] - Is Null");
		}

		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		return methodcall.call(instance, args);
	}

	public IClassMethodCall getMethodByIndex(int index) {
		if(index >= 0 && index < _METHODS.length) {
			return _METHODS[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	public int methodCount() {
		return _METHODS.length;
	}

	public IClassMethodCall[] getMethods(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}
		final ResizingArray<MethodCall> METHODS = new ResizingArray<MethodCall>();

		for(int X = 0; X < _METHODS.length; X++) {
			final MethodCall METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				METHODS.put(METHOD_CALL);
			}
		}

		return (METHODS.length() > 0 ? METHODS.toArray(new MethodCall[METHODS.length()]) : null);
	}

	/*public IClassMethodCall getMethodByName(String name, Object... args) {
		return getMethodByName(name, void.class, args);
	}*/

	public IClassMethodCall getMethod(String name, Object... args) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final MethodCall METHOD_CALL = _METHODS[X];

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

	public IClassMethodCall getMethodWPN(String name, String... parametertypes) {
		return getMethodRWPN(name, "void", parametertypes);
	}

	/**
	 * Get Method By Name With Parameters Names
	 * (getMethodByNameWParametersNames)
	 * 
	 * @param name
	 * @param returntype
	 * @param parametertypes
	 * @return
	 */
	public IClassMethodCall getMethodRWPN(String name, String returntype, String... parametertypes) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(returntype == null) {
			throw new RuntimeException("Variable[returntype] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final MethodCall METHOD_CALL = _METHODS[X];

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

	/**
	 * Get Method By NameWith Parameters Classes
	 * (getMethodByNameWParametersClasses)
	 * 
	 * @param name
	 * @param returntype
	 * @param parametertypes
	 * @return
	 */
	public IClassMethodCall getMethodRWPC(String name, Class<?> returntype, Class<?>... parametertypes) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		if(returntype == null) {
			throw new RuntimeException("Variable[returntype] - Is Null");
		}

		for(int X = 0; X < _METHODS.length; X++) {
			final MethodCall METHOD_CALL = _METHODS[X];

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

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param index
	 * @return
	 */
	public String[] getMethodArgClasses(int index) {
		final IMethodCallExtended M_CALL = (IMethodCallExtended)getMethodByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		final ResizingArray<String> ARGS = new ResizingArray<String>();
		final Class<?>[] CLASSES = M_CALL.getArgumentClasses();

		for(int Y = 0; Y < CLASSES.length; Y++) {
			ARGS.put(CLASSES[Y].getCanonicalName());
		}

		return ARGS.toArray(new String[ARGS.length()]);
	}

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param methodname
	 * @param args
	 * @return
	 */
	public String[][] getMethodArgClassNames(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		final MethodCall[] METHODS = (MethodCall[])getMethods(name);

		if(METHODS != null) {
			return getMethodArgClassNames(METHODS);
		}

		return null;
	}

	public String[][] getMethodArgClassNames(MethodCall[] methods) {
		if(methods == null) {
			throw new RuntimeException("Variable[methods] - Is Null");
		}

		final String[][] RETURN = new String[methods.length][];

		for(int X = 0; X < methods.length; X++) {
			final ResizingArray<String> ARGS = new ResizingArray<String>();

			final Class<?>[] CLASSES = methods[X].getArgumentClasses();

			for(int Y = 0; Y < CLASSES.length; Y++) {
				ARGS.put(CLASSES[Y].getCanonicalName());
			}

			RETURN[X] = ARGS.toArray(new String[ARGS.length()]);
		}

		return RETURN;
	}

	public Object newInstance() {
		try {
			return ClassUtil.newInstanceEx(_CLASS);
		} catch(Exception e) {}

		return null;
	}

	public Object newInstance(Object... args) {
		try {
			return ClassUtil.newInstanceEx(_CLASS, args);
		} catch(Exception e) {}

		return null;
	}

	public Object newInstanceEx() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		return ClassUtil.newInstanceEx(_CLASS);
	}

	public Object newInstanceEx(Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		return ClassUtil.newInstanceEx(_CLASS, args);
	}

	//CLASSES
	//This Class Is Used To Prevent Methods and Argument Classes From Being Cloned Every Time It Is Called
	private class MethodCall implements IClassMethodCall, IMethodCallExtended {
		private final Method _METHOD;
		private final Class<?>[] _PARAMETER_CLASSES;
		private final Class<?> _RETURN_TYPE;

		public MethodCall(Method method) {
			_METHOD = method;
			_PARAMETER_CLASSES = method.getParameterTypes();
			_RETURN_TYPE = method.getReturnType();
		}

		@Override
		public String getMethodName() {
			return _METHOD.getName();
		}

		@Override
		public Class<?>[] getArgumentClasses() {
			return _PARAMETER_CLASSES;
		}

		@Override
		public Class<?> getReturnType() {
			return _RETURN_TYPE;
		}

		@Override
		public Object call(Object instance, Object... args) {
			try {
				return _METHOD.invoke(instance, args);
			} catch(Exception e) {
				return new MethodCallError(e);
			}
		}

		@Override
		public String toString() {
			StringBuffer Buffer = new StringBuffer("Name: ");

			Buffer.append(_METHOD.getName());

			if(_PARAMETER_CLASSES.length > 0) {
				Buffer.append(", ");
				Buffer.append(_PARAMETER_CLASSES.length);
				Buffer.append(" Argument(s): ");

				for(int X = 0; X < _PARAMETER_CLASSES.length; X++) {
					Buffer.append(_PARAMETER_CLASSES[X].getCanonicalName());

					if(X < (_PARAMETER_CLASSES.length - 1)) {
						Buffer.append(", ");
					}
				}
			}

			return Buffer.toString();
		}
	}

	//STATIC
	private static MethodCall[] loadMethods(ClassWrapper cw, Class<?> clazz) {
		final Method[] METHODS = clazz.getMethods();

		if(METHODS.length == 0) {
			throw new RuntimeException("Variable[clazz] - No Methods");
		}

		final ResizingArray<MethodCall> T_METHODS = new ResizingArray<MethodCall>(METHODS.length);

		for(int X = 0; X < METHODS.length; X++) {
			if((METHODS[X].getModifiers() & _PUBLIC_METHOD_) != 0) {
				T_METHODS.put(cw.new MethodCall(METHODS[X]));

//                System.out.println("    Method Name: " + METHODS[X].getName());
			}
		}

		return T_METHODS.toArray(new MethodCall[T_METHODS.length()]);
	}

	public static boolean hasReturnType(IMethodCallExtended methodcall) {
		return hasReturnType(methodcall, void.class);
	}

	public static boolean hasReturnType(IMethodCallExtended methodcall, Class<?> clazz) {
		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		return clazz.equals(methodcall.getReturnType());
	}

	public static ClassWrapper create(String classname) {
		try {
			return new ClassWrapper(classname);
		} catch(Exception e) {}

		return null;
	}

	public static ClassWrapper create(Class<?> clazz) {
		try {
			return new ClassWrapper(clazz);
		} catch(Exception e) {}

		return null;
	}

	public static ClassWrapper create(ClassLoader classloader, String classname) {
		try {
			return new ClassWrapper(classloader, classname);
		} catch(Exception e) {}

		return null;
	}

	public static ClassWrapper create(ClassLoader classloader, Class<?> clazz) {
		try {
			return new ClassWrapper(classloader, clazz);
		} catch(Exception e) {}

		return null;
	}
}
//    public boolean instanceOf(Object instance) {
//        if(_CLASS.equals(instance.getClass())) {
//            return true;
//        } else {
//            Class Current = _CLASS;
//            while((Current = Current.getSuperclass()) != null && !Current.equals(Object.class)) {
//                if(Current.equals(instance.getClass())) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }