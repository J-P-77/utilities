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

package jp77.beta.utillib.classloader.v2.wrappers;

import jp77.beta.utillib.classloader.ClassUtil;

import jp77.utillib.arrays.ResizingArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceWrapper {
	public static class MethodCallErrorObject extends Object {}

	private final static MethodCallErrorObject _CALL_ERROR_ = new MethodCallErrorObject();

	private static final int _PUBLIC_METHOD_ = 0x0001;

	private final Object _INSTANCE;

	private final MethodCall[] _METHODS;

	public InstanceWrapper(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(classname, new Object[] {});
	}

	public InstanceWrapper(String classname, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassLoader.getSystemClassLoader(), classname, args);
	}

	public InstanceWrapper(ClassLoader classloader, String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassUtil.loadClassEx(classloader, classname), null);
	}

	public InstanceWrapper(ClassLoader classloader, String classname, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassUtil.loadClassEx(classloader, classname), args);
	}

	public InstanceWrapper(Class<?> clazz, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassUtil.newInstanceEx(clazz, args));
	}

	public InstanceWrapper(Object instance) {
		if(instance == null) {
			throw new RuntimeException("Variable[instance] - Is Null");
		}

		_INSTANCE = instance;

//        System.out.println("Class Name: " + instance.getClass().getName());

		final Method[] METHODS = _INSTANCE.getClass().getMethods();
		final ResizingArray<MethodCall> T_METHODS = new ResizingArray<MethodCall>(METHODS.length);

		for(int X = 0; X < METHODS.length; X++) {
			if((METHODS[X].getModifiers() & _PUBLIC_METHOD_) != 0) {
				T_METHODS.put(new MethodCall(METHODS[X]));

//                System.out.println("    Method Name: " + METHODS[X].getName());
			}
		}
		_METHODS = T_METHODS.toArray(new MethodCall[T_METHODS.length()]);
	}

	public Object getInstance() {
		return _INSTANCE;
	}

	public Class<?> getThisClass() {
		return _INSTANCE.getClass();
	}

	public boolean instanceOf(Class<?> clazz) {
		if(_INSTANCE.getClass().equals(clazz)) {
			return true;
		} else {
			Class<?> Current = _INSTANCE.getClass();
			while((Current = Current.getSuperclass()) != null && !Current.equals(Object.class)) {
				if(Current.equals(clazz)) {
					return true;
				}
			}
		}

		return false;
	}

	public Object callMethod(int index, Object... args) {
		final IMethodCall M_CALL = getMethodByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return callMethod(M_CALL, args);
	}

	public Object callMethod(String name, Object... args) {
		final IMethodCall M_CALL = getMethod(name, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method: " + name + " Not Found");
		}

		return callMethod(M_CALL, args);
	}

	public static Object callMethod(IMethodCall methodcall, Object... args) {
		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		return methodcall.call(args);
	}

	public IMethodCall getMethodByIndex(int index) {
		if(index >= 0 && index < _METHODS.length) {
			return _METHODS[index];
		} else {
			throw new IndexOutOfBoundsException("Index Number: " + index);
		}
	}

	/**
	 * 
	 * @param name
	 *            name of method
	 * @param args
	 * @return
	 */
	public IMethodCall getMethod(String name, Object... args) {
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

	/**
	 * 
	 * @param name
	 *            name of method
	 * @return
	 */
	public IMethodCall[] getMethods(String name) {
		if(name == null) {
			throw new RuntimeException("Variable[name] - Is Null");
		}

		final ResizingArray<IMethodCall> METHODS = new ResizingArray<IMethodCall>();

		for(int X = 0; X < _METHODS.length; X++) {
			final MethodCall METHOD_CALL = _METHODS[X];

			if(METHOD_CALL.getMethodName().equals(name)) {
				METHODS.put(METHOD_CALL);
			}
		}

		return (METHODS.length() > 0 ? METHODS.toArray(new IMethodCall[METHODS.length()]) : null);
	}

	public IMethodCall getMethodWPN(String name, String... parameterclassnames) {
		return getMethodWPN("void", name, parameterclassnames);
	}

	/**
	 * 
	 * @param name
	 *            name of method
	 * @param returntype
	 *            return type by class name
	 * @param parameterclassnames
	 *            class names
	 * @return
	 */
	public IMethodCall getMethodWPN(String returntype, String name, String... parametertypes) {
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
						for(int Y = 0; Y < CLASSES.length; Y++) {
							if(!parametertypes[Y].equals(CLASSES[Y].getName())) {
								continue;
							}
						}

						return METHOD_CALL;
					}
				}
			}
		}

		return null;
	}

	public IMethodCall getMethodWPC(String name, Class<?>... parametertypes) {
		return getMethodWPC(void.class, name, parametertypes);
	}

	/**
	 * 
	 * @param name
	 *            name of method
	 * @param returntype
	 *            return type by class
	 * @param parametertypes
	 *            Classes
	 * @return
	 */
	public IMethodCall getMethodWPC(Class<?> returntype, String name, Class<?>... parametertypes) {
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

	public int methodCount() {
		return _METHODS.length;
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

	//STATIC
	public static InstanceWrapper create(Class<?> clazz, Object[] args) {
		try {
			return new InstanceWrapper(clazz, args);
		} catch(Exception e) {}

		return null;
	}

	public static InstanceWrapper create(String classname, Object[] args) {
		try {
			return new InstanceWrapper(classname, args);
		} catch(Exception e) {}

		return null;
	}

	public static InstanceWrapper create(ClassLoader classloader, String classname, Object[] args) {
		try {
			return new InstanceWrapper(classloader, classname, args);
		} catch(Exception e) {}

		return null;
	}

	public static InstanceWrapper create(Object instance) {
		try {
			return new InstanceWrapper(instance);
		} catch(Exception e) {}

		return null;
	}

	//CLASSES
	//This Class Is Used To Prevent Argument Classes From Being Cloned Every Time It Is Called
	public class MethodCall implements IMethodCall, IMethodCallExtended {
		private final Method _METHOD;
		private final Class<?>[] _PARAMETERS_CLASSES;
		private final Class<?> _RETURN_TYPE;

		public MethodCall(Method method) {
			_METHOD = method;
			_PARAMETERS_CLASSES = method.getParameterTypes();
			_RETURN_TYPE = method.getReturnType();
		}

		@Override
		public String getMethodName() {
			return _METHOD.getName();
		}

		@Override
		public Object call(Object... args) {
			try {
				return _METHOD.invoke(_INSTANCE, args);
			} catch(Exception e) {}

			return _CALL_ERROR_;
		}

		@Override
		public Class<?>[] getArgumentClasses() {
			return _PARAMETERS_CLASSES;
		}

		@Override
		public Class<?> getReturnType() {
			return _RETURN_TYPE;
		}

		@Override
		public String toString() {
			StringBuffer Buffer = new StringBuffer("Name: ");

			Buffer.append(_METHOD.getName());

			if(_PARAMETERS_CLASSES.length > 0) {
				Buffer.append(", ");
				Buffer.append(_PARAMETERS_CLASSES.length);
				Buffer.append(" Argument(s): ");

				for(int X = 0; X < _PARAMETERS_CLASSES.length; X++) {
					Buffer.append(_PARAMETERS_CLASSES[X].getCanonicalName());

					if(X < (_PARAMETERS_CLASSES.length - 1)) {
						Buffer.append(", ");
					}
				}
			}

			return Buffer.toString();
		}
	}

//    public static void main(String[] args) {
//        final InstanceWrapper IW = InstanceWrapper.create(java.lang.String.class, new Object[] {"Justin"});
//
//        final String[] STRINGS = IW.getMethodArgClasses(0);
//        for(int X = 0; X < STRINGS.length; X++) {
//            System.out.println(STRINGS[X]);
//        }
//    }
}
//    public static void main(String[] args) {
//        /*
//        System.out.println("INSTANCE_1");
//        final InstanceWrapperBeta1 INSTANCE_1 = InstanceWrapperBeta1.create("java.lang.String", new Object[] {"Justin Palinkas"});
//
//        if(INSTANCE_1 == null) {
//            System.out.println("!!!ERROR!!! - INSTANCE_1");
//        } else {
//            System.out.println("INSTANCE_1.instanceOf(Class)=" + INSTANCE_1.instanceOf(java.lang.String.class));
//            System.out.println("toString()=" + (String)INSTANCE_1.callMethod("toString"));
//            System.out.println("charAt()=" + (Character)INSTANCE_1.callMethod("charAt", 0));
//        }
//
//        final InstanceWrapperBeta1 INSTANCE_2;
//        try {
//            System.out.println("INSTANCE_2");
//            INSTANCE_2 = new InstanceWrapperBeta1("java.lang.StringBuffer", new Object[] {"This    Just A Test"});
//
//            if(INSTANCE_2 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_2");
//            } else {
//                System.out.println("toString()=" + (String)INSTANCE_2.callMethod("toString"));
//                INSTANCE_2.callMethod("setCharAt", 5, 'I');
//                INSTANCE_2.callMethod("setCharAt", 6, 's');
//                System.out.println("toString()=" + (String)INSTANCE_2.callMethod("toString"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final InstanceWrapperBeta1 INSTANCE_3;
//        try {
//            System.out.println("INSTANCE_3");
//            INSTANCE_3 = new InstanceWrapperBeta1("java.lang.StringBuffer", new Object[] {"This    Just A Test"});
//
//            if(INSTANCE_3 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_3");
//            } else {
//                final IMethodCall __toString = INSTANCE_3.getMethodByNameWParametersClasses("toString");
//                final IMethodCall __setCharAt = INSTANCE_3.getMethodByNameWParametersClasses("setCharAt", int.class, char.class);
//
//                System.out.println("toString()=" + (String)__toString.call());
//                __setCharAt.call(5, 'I');
//                __setCharAt.call(6, 's');
//                System.out.println("toString()=" + (String)__toString.call());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final InstanceWrapperBeta1 INSTANCE_4;
//        try {
//            System.out.println("INSTANCE_4");
//            INSTANCE_4 = new InstanceWrapperBeta1("javax.swing.JFrame", new Object[] {"Test Window"});
//
//            if(INSTANCE_4 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_4");
//            } else {
//                final IMethodCall __setTitle = INSTANCE_4.getMethodByNameWParametersNames("setTitle", "java.lang.String");
//                final IMethodCall __getTitle = INSTANCE_4.getMethodByNameWParametersNames("getTitle");
//                final IMethodCall __setVisible = INSTANCE_4.getMethodByNameWParametersClasses("setVisible", java.lang.Boolean.class);
//                final IMethodCall __dispose = INSTANCE_4.getMethodByNameWParametersClasses("dispose");
//
//                System.out.println("INSTANCE_4.instanceOf(Class)=" + INSTANCE_4.instanceOf(java.awt.Window.class));
//                System.out.println("getTitle()=" + (String)__getTitle.call());
//                __setTitle.call("Title Changed");
//                System.out.println("getTitle()=" + (String)__getTitle.call());
//                __setVisible.call(true);
//                __dispose.call();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final InstanceWrapperBeta1 INSTANCE_5;
//        try {
//            System.out.println("INSTANCE_5");
//            INSTANCE_5 = new InstanceWrapperBeta1("test.jp77.beta.utillib.classloader.betav1.TestClass", new Object[] {});
//
//            if(INSTANCE_5 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_5");
//            } else {
//                final IMethodCall __test1  = INSTANCE_5.getMethodByNameWParametersClasses("test1");
//                final IMethodCall __test1__int = INSTANCE_5.getMethodByNameWParametersClasses("test1", Integer.class);
//                final IMethodCall __test1__String = INSTANCE_5.getMethodByNameWParametersClasses("test1", String.class);
//                final IMethodCall __test1__int_String = INSTANCE_5.getMethodByNameWParametersClasses("test1", Integer.class, String.class);
//
//                __test1.call();
//                __test1__int.call(1);
//                __test1__String.call("Hello");
//                __test1__int_String.call(1, "Hello");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final InstanceWrapperBeta1 INSTANCE_6;
//        try {
//            System.out.println("INSTANCE_6");
//            INSTANCE_6 = new InstanceWrapperBeta1("test.jp77.beta.utillib.classloader.betav1.TestClass", new Object[] {});
//
//            if(INSTANCE_6 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_6");
//            } else {
//                final IMethodCall __test1  = INSTANCE_6.getMethodByNameWParametersNames("test1");
//                final IMethodCall __test1__int = INSTANCE_6.getMethodByNameWParametersNames("test1", "int");
//                final IMethodCall __test1__String = INSTANCE_6.getMethodByNameWParametersNames("test1", "java.lang.String");
//                final IMethodCall __test1__int_String = INSTANCE_6.getMethodByNameWParametersNames("test1", "int", "java.lang.String");
//
//                __test1.call();
//                __test1__int.call(1);
//                __test1__String.call("Hello");
//                __test1__int_String.call(1, "Hello");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }*/
//
//        final InstanceWrapper INSTANCE_7;
//        try {
//            System.out.println("INSTANCE_7");
//            INSTANCE_7 = new InstanceWrapper("javax.swing.JFrame");
//
//            if(INSTANCE_7 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_2");
//            } else {
//                System.out.println("INSTANCE_7.instanceOf(Class)=" + INSTANCE_7.instanceOf(java.awt.Window.class));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final InstanceWrapper INSTANCE_8;
//        try {
//            System.out.println("INSTANCE_7");
//            INSTANCE_8 = new InstanceWrapper("javax.swing.JFrame");
//
//            if(INSTANCE_8 == null) {
//                System.out.println("!!!ERROR!!! - INSTANCE_2");
//            } else {
//                System.out.println("INSTANCE_8.instanceOf(Class)=" + INSTANCE_8.instanceOf(java.awt.AWTError.class));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static boolean compareExact(Class[] classes, Object[] args) {
//        for(int Y = 0; Y < classes.length; Y++) {
//            final Class C1 = classes[Y];
//            final Class C2 = args[Y].getClass();
//
//            if(C1 != C2) {
//                return false;
//            }
//        }
//
//        return true;
//    }

//            if(C1 != C2) {
//                if(C1.isPrimitive()) {
//                    if(Byte.class.equals(C2)) {
//                        continue;
//                    } else if(Short.class.equals(C2)) {
//                        continue;
//                    } else if(Integer.class.equals(C2)) {
//                        continue;
//                    } else if(Character.class.equals(C2)) {
//                        continue;
//                    } else if(Long.class.equals(C2)) {
//                        continue;
//                    } else if(Float.class.equals(C2)) {
//                        continue;
//                    } else if(Double.class.equals(C2)) {
//                        continue;
//                    } else {
//                        return false;
//                    }
//                } else if(C2.isPrimitive()) {
//                    if(Byte.class.equals(C1)) {
//                        continue;
//                    } else if(Short.class.equals(C1)) {
//                        continue;
//                    } else if(Integer.class.equals(C1)) {
//                        continue;
//                    } else if(Character.class.equals(C1)) {
//                        continue;
//                    } else if(Long.class.equals(C1)) {
//                        continue;
//                    } else if(Float.class.equals(C1)) {
//                        continue;
//                    } else if(Double.class.equals(C1)) {
//                        continue;
//                    } else {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            }
/*
 * @Override public String toString() { return "InstanceOf " +
 * _INSTANCE.getClass().getCanonicalName(); // return
 * _INSTANCE.getClass().getCanonicalName() + "@" +
 * Integer.toHexString(hashCode()); }
 */

//    public static Object invokeMethod(String methodname, Object... args) {
//        return invokeMethod(null, true, methodname, args);
//    }
//
//    public static Object invokeMethod(Object instance, boolean staticmethod, String methodname, Object... args) {
//        if(methodname == null) {
//            throw new RuntimeException("Class[] - Method[callMethod(MethodCall, Object[])] - Variable[methodcall] - Is Null");
//        }
//
//        final Class[] CLASSES = new Class[args.length];
//        try {
//            for(int X = 0; X < args.length; X++) {
//                CLASSES[X] = args[X].getClass();
//            }
//
//            final Method METHOD = getMethodWParameters(instance.getClass(), staticmethod, methodname, CLASSES);
//
//            if(METHOD != null) {
//                return METHOD.invoke(instance, args);
//            }
//        } catch (Exception e) {e.printStackTrace();}
//
//        return null;
//    }