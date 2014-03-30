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

package beta.utillib.classloader.v1.wrappers;

import utillib.exceptions.InvalidClassException;
import utillib.exceptions.InvalidNumberException;

import utillib.arrays.ResizingArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

// 3780
/**
 * Use Class beta.utillib.classloader.v2.wrappers.InstanceWrapper
 * 
 * @author Dalton Dell
 */
@Deprecated
public class InstanceWrapper {
	private static final int _PUBLIC_METHOD_ = 0x0001;

	private final Object _INSTANCE;

	private ResizingArray<MethodCall> _Methods;

	public InstanceWrapper(Object instance) {
		if(instance == null) {
			throw new RuntimeException("Variable[instance] - Is Null");
		}

//        if(Class.class.isInstance(instance)) {
//            throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[instance] - Is Not A Instance");
////            System.out.println("Is NOT Instance");
//        } //else {
////            System.out.println("Is Instance");
////        }

		_INSTANCE = instance;

		loadPublicMethods(_INSTANCE.getClass());
	}

	public InstanceWrapper(Class<?> clazz, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		if(clazz == null) {
			throw new RuntimeException("Variable[clazz] - Is Null");
		}

		_INSTANCE = newInstanceEx(clazz, args);

		if(_INSTANCE == null) {
			throw new InstantiationException("Failed To Create Instance For Class: " + clazz.getCanonicalName());
		}

		loadPublicMethods(_INSTANCE.getClass());
	}

	public InstanceWrapper(String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		this(ClassLoader.getSystemClassLoader(), classname, args);
	}

	public InstanceWrapper(ClassLoader classloader, String classname, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

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
// <editor-fold defaultstate="collapsed" desc="Debugging Information">
//            System.out.print(CLASS.getName() + " " + CLASS.getModifiers() + " ");
//            if((CLASS.getModifiers() & 0x0400) != 0) {
//                System.out.print("ABSTRACT, ");
//            }
//            if((CLASS.getModifiers() & 0x0010) != 0) {
//                System.out.print("FINAL, ");
//            }
//            if((CLASS.getModifiers() & 0x0200) != 0) {
//                System.out.print("INTERFACE, ");
//            }
//            if((CLASS.getModifiers() & 0x0001) != 0) {
//                System.out.print("PUBLIC, ");
//            }
//            if((CLASS.getModifiers() & 0x0020) != 0) {
//                System.out.print("SUPER, ");
//            }
//            System.out.println();
//
//            final Method[] D_METHODS = CLASS.getMethods();
//            for(int X = 0; X < D_METHODS.length; X++) {
//                System.out.print(D_METHODS[X].getName() + " " + D_METHODS[X].getModifiers() + " " );
//                if((D_METHODS[X].getModifiers() & 0x0400) != 0) {
//                    System.out.print("ABSTRACT, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0010) != 0) {
//                    System.out.print("FINAL, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0100) != 0) {
//                    System.out.print("NATIVE, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0002) != 0) {
//                    System.out.print("PRIVATE, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0004) != 0) {
//                    System.out.print("PROTECTED, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0001) != 0) {
//                    System.out.print("PUBLIC, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0008) != 0) {
//                    System.out.print("STATIC, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0800) != 0) {
//                    System.out.print("STRICT, ");
//                }
//                if((D_METHODS[X].getModifiers() & 0x0020) != 0) {
//                    System.out.print("SYNCHRONIZED, ");
//                }
//                System.out.println();
//            }
// </editor-fold>
			_INSTANCE = newInstanceEx(CLASS, args);

			if(_INSTANCE == null) {
				throw new InstantiationException("Failed To Create Instance" + (CLASS == null ? "" : " For Class: " + CLASS.getCanonicalName()));
			}

			loadPublicMethods(_INSTANCE.getClass());
		}
	}

	private void loadPublicMethods(Class<?> clazz) {
		final Method[] METHODS = clazz.getMethods();

		_Methods = new ResizingArray<MethodCall>(METHODS.length);

		for(int X = 0; X < METHODS.length; X++) {
			if((METHODS[X].getModifiers() & _PUBLIC_METHOD_) != 0) {
				final MethodCall MC = new MethodCall(METHODS[X], METHODS[X].getParameterTypes());

				_Methods.put(MC);
//                System.out.println(MC.toString());
			}
		}
	}

	public Object getInstance() {
		return _INSTANCE;
	}

	public String instanceOf() {
		return _INSTANCE.getClass().getCanonicalName();
	}

	public ResizingArray<MethodCall> getMethodCalls() {
		return _Methods;
	}

	public Object callMethod(int index, Object... args) {
		final MethodCall M_CALL = getMethodCallByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return callMethod(M_CALL, args);
	}

	public Object callMethod(String name, Object... args) {
		final MethodCall M_CALL = getMethodCallByName(name, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method: " + name + " Not Found");
		}

		return callMethod(M_CALL, args);
	}

	public Object callMethod(MethodCall methodcall, Object... args) {
		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		try {
			return methodcall.getMethod().invoke(_INSTANCE, args);
		} catch(Exception e) {}

		return null;
	}

	public Object callMethodEx(int index, Object... args) throws InvalidNumberException, InvalidClassException, IllegalAccessException, InvocationTargetException {

		final MethodCall M_CALL = getMethodCallByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return callMethodEx(M_CALL, args);
	}

	public Object callMethodEx(String name, Object... args) throws InvalidNumberException, InvalidClassException, IllegalAccessException, InvocationTargetException {

		final MethodCall M_CALL = getMethodCallByName(name, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method: " + name + " - Not Found");
		}

		return callMethodEx(M_CALL, args);
	}

	public Object callMethodEx(MethodCall methodcall, Object... args) throws InvalidNumberException, InvalidClassException, IllegalAccessException, InvocationTargetException {

		if(methodcall == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		return methodcall.getMethod().invoke(_INSTANCE, args);
	}

	public String getMethodName(int index) {
		return _Methods.getAt(index).getMethod().getName();
	}

	public MethodCall getMethodCallByIndex(int index) {
		if(_Methods.validIndex(index)) {
			return _Methods.getAt(index);
		}

		return null;
	}

	public MethodCall getMethodCallByName(String name, Object... args) {
		for(int X = 0; X < _Methods.length(); X++) {
			final MethodCall METHOD_CALL = _Methods.getAt(X);

			if(METHOD_CALL.getMethod().getName().equals(name)) {
				final Class<?>[] CLASSES = METHOD_CALL.getArgumentClasses();

				if(args.length == CLASSES.length) {
					boolean Equals = compareExact(CLASSES, args);

					if(Equals) {
						return METHOD_CALL;
					} else {
						Equals = compare(CLASSES, args);

						if(Equals) {
							return METHOD_CALL;
						} else {
							throw new RuntimeException("Method: " + name + " Error Invalid Classs Arguments");
						}
					}
				}
			}
		}

		return null;
	}

	public MethodCall getMethodCallByNameWParameters(String name, Class<?>... methodparameters) {
		for(int X = 0; X < _Methods.length(); X++) {
			final MethodCall METHOD_CALL = _Methods.getAt(X);

			if(METHOD_CALL.getMethod().getName().equals(name)) {
				final Class<?>[] CLASSES = METHOD_CALL.getArgumentClasses();

				if(methodparameters.length == CLASSES.length) {
					for(int Y = 0; Y < CLASSES.length; Y++) {
						final Class<?> C1 = CLASSES[Y];
						final Class<?> C2 = methodparameters[Y];

						if(!C1.equals(C2)) {
							return null;
						}
					}

					return METHOD_CALL;
				}
			}
		}

		return null;
	}

	public int methodCount() {
		return _Methods.length();
	}

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param index
	 * @return
	 */
	public String[] getMethodCallArgClasses(int index) {
		final MethodCall M_CALL = getMethodCallByIndex(index);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method Not Found");
		}

		return getMethodArgClassNames(M_CALL);
	}

	/**
	 * Gets Method Arguments Class Names
	 * 
	 * @param methodname
	 * @param args
	 * @return
	 */
	public String[] getMethodCallArgClasses(String methodname, Object... args) {
		final MethodCall M_CALL = getMethodCallByName(methodname, args);

		if(M_CALL == null) {
			throw new RuntimeException("Variable[M_CALL] - Method: " + methodname + " Not Found");
		}

		return getMethodArgClassNames(M_CALL);
	}

	public String[] getMethodArgClassNames(MethodCall methodcall) {
		if(methodcall != null) {
			ResizingArray<String> Results = new ResizingArray<String>();

			final Class<?>[] CLASSES = methodcall.getArgumentClasses();

			for(int X = 0; X < CLASSES.length; X++) {
				Results.put(CLASSES[X].getCanonicalName());
			}

			return Results.toArray(new String[Results.length()]);
		}

		return null;
	}

	/**
	 * 
	 * @param clazz
	 *            Class To Create New Instance From
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
	 * @param clazz
	 *            Class To Create New Instance From
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(Class<?> clazz, Object... args) throws /*Exception {*/
	ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		if(args.length > 0) {
			Constructor<?> Constructor = null;
			try {
				final Class<?>[] CLASSES = new Class[args.length];
				for(int X = 0; X < args.length; X++) {
					CLASSES[X] = args[X].getClass();
				}

				Constructor = clazz.getConstructor(CLASSES);
			} catch(Exception e) {
				final Constructor<?>[] CONSTRUCTORS = clazz.getConstructors();

				for(int X = 0; X < CONSTRUCTORS.length; X++) {
					if(compare(CONSTRUCTORS[X].getParameterTypes(), args)) {
						Constructor = CONSTRUCTORS[X];
						break;
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
	 * @param classname
	 *            Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 */
	public static Object newInstance(String classname, Object... args) throws /*Exception {*/
	ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		return newInstance(ClassLoader.getSystemClassLoader(), classname, args);
	}

	/**
	 * 
	 * @param classloader
	 *            Classlodaer To Search For Class
	 * @param classname
	 *            Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class, null If Failed To Create New Instance
	 */
	public static Object newInstance(ClassLoader classloader, String classname, Object... args) throws /*Exception {*/
	ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		try {
			return newInstanceEx(classloader, classname, args);
		} catch(Exception e) {}

		return null;
	}

	/**
	 * 
	 * @param classname
	 *            Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(String classname, Object... args) throws /*Exception {*/
	ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

		return newInstanceEx(ClassLoader.getSystemClassLoader(), classname, args);
	}

	/**
	 * 
	 * @param classloader
	 *            Classlodaer To Search For Class
	 * @param classname
	 *            Class Name (ex. java.swing.JButton)
	 * @param parameters
	 * @return New Instance Of The Class
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object newInstanceEx(ClassLoader classloader, String classname, Object... args) throws /*Exception {*/
	ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

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

/*
    @Override
    public String toString() {
        return "InstanceOf " + _INSTANCE.getClass().getCanonicalName();
//        return _INSTANCE.getClass().getCanonicalName() + "@" + Integer.toHexString(hashCode());
    }
*/
	//CLASSES
	//This Class Is Used To Prevent Argument Classes From Being Clone Every Time It Is Called
	public class MethodCall {
		private final Method _METHOD;
		private final Class<?>[] _ARGUMENTS_CLASSES;

		public MethodCall(Method method, Class<?>[] argumentclass) {
			_METHOD = method;
			_ARGUMENTS_CLASSES = argumentclass;
		}

		public Method getMethod() {
			return _METHOD;
		}

		public Class<?>[] getArgumentClasses() {
			return _ARGUMENTS_CLASSES;
		}

		@Override
		public String toString() {
//            return "Name: " + _METHOD.getName() + ", # of Arguments: " + _ARGUMENTS_CLASSES.length;

			StringBuffer Buffer = new StringBuffer("Name: ");

			Buffer.append(_METHOD.getName());

			if(_ARGUMENTS_CLASSES.length > 0) {
				Buffer.append(", " + _ARGUMENTS_CLASSES.length);
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

	public static Object excuteMethod(String methodname, Object... args) {
		return excuteMethod(null, true, methodname, args);
	}

	public static Object excuteMethod(Object instance, boolean staticmethod, String methodname, Object... args) {
		if(methodname == null) {
			throw new RuntimeException("Variable[methodcall] - Is Null");
		}

		final Class<?>[] CLASSES = new Class[args.length];
		try {
			for(int X = 0; X < args.length; X++) {
				CLASSES[X] = args[X].getClass();
			}

			final Method METHOD = getMethodWParameters(instance.getClass(), staticmethod, methodname, CLASSES);

			if(METHOD != null) {
				return METHOD.invoke(instance, args);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Method getMethodWParameters(Class<?> clazz, boolean staticmethod, String methodname, Class<?>... methodparameters) {
		final Method[] METHODS = ((Class<?>)clazz).getMethods();

		for(int X = 0; X < METHODS.length; X++) {
			final Method METHOD = METHODS[X];

			if(METHOD.getName().equals(methodname)) {
				if(staticmethod && ((METHOD.getModifiers() & 0x0008) == 0)) {
					continue;
				}

				final Class<?>[] CLASSES = METHOD.getParameterTypes();
				if(methodparameters.length == CLASSES.length) {
					for(int Y = 0; Y < CLASSES.length; Y++) {
						final Class<?> C1 = CLASSES[Y];
						final Class<?> C2 = methodparameters[Y];

						if(!C1.equals(C2)) {
							return null;
						}
					}

					return METHOD;
				}
			}
		}

		return null;
	}

	public static boolean compareExact(Class<?>[] classes, Object[] args) {
		for(int Y = 0; Y < classes.length; Y++) {
			final Class<?> C1 = classes[Y];
			final Class<?> C2 = args[Y].getClass();

			if(C1 != C2) {
				return false;
			}
		}

		return true;
	}

	public static boolean compare(Class<?>[] classes, Object[] args) {
		if(classes == null || args == null) {
			return false;
		}

		if(classes.length != args.length) {
			return false;
		}

		for(int X = 0; X < classes.length; X++) {
			final Class<?> C1 = classes[X];
			final Class<?> C2 = args[X].getClass();

			if(C1 != C2) {
				if(C1.isPrimitive()) {
					if(Byte.class.equals(C2)) {
						continue;
					} else if(Short.class.equals(C2)) {
						continue;
					} else if(Integer.class.equals(C2)) {
						continue;
					} else if(Character.class.equals(C2)) {
						continue;
					} else if(Long.class.equals(C2)) {
						continue;
					} else if(Float.class.equals(C2)) {
						continue;
					} else if(Double.class.equals(C2)) {
						continue;
					} else {
						return false;
					}
				} else if(C2.isPrimitive()) {
					if(Byte.class.equals(C1)) {
						continue;
					} else if(Short.class.equals(C1)) {
						continue;
					} else if(Integer.class.equals(C1)) {
						continue;
					} else if(Character.class.equals(C1)) {
						continue;
					} else if(Long.class.equals(C1)) {
						continue;
					} else if(Float.class.equals(C1)) {
						continue;
					} else if(Double.class.equals(C1)) {
						continue;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return true;
	}
}

/*
    public static boolean compare(Class[] classes, Object[] args) {
        return compareBeta2(classes, args);
    }
*/
/*
    public static boolean compareBeta1(Class[] classes, Object[] args) {
        if(classes == null || args == null) {
            return false;
        }

        if(classes.length != args.length) {
            return false;
        }

        for(int X = 0; X < classes.length; X++) {
            final Class C1 = classes[X];
            final Class C2 = args[X].getClass();

            if(C1 != C2) {
                if(C1.isPrimitive()) {
                    if(C1.equals(byte.class)) {
                        if(!Byte.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(short.class)) {
                        if(!Short.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(int.class)) {
                        if(!Integer.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(char.class)) {
                        if(!Character.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(long.class)) {
                        if(!Long.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(float.class)) {
                        if(!Float.class.equals(C2)) {
                            return false;
                        }
                    } else if(C1.equals(double.class)) {
                        if(!Double.class.equals(C2)) {
                            return false;
                        }
                    }
                } else if(C2.isPrimitive()) {
                    if(C2.equals(byte.class)) {
                        if(!Byte.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(short.class)) {
                        if(!Short.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(int.class)) {
                        if(!Integer.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(char.class)) {
                        if(!Character.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(long.class)) {
                        if(!Long.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(float.class)) {
                        if(!Float.class.equals(C1)) {
                            return false;
                        }
                    } else if(C2.equals(double.class)) {
                        if(!Double.class.equals(C1)) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }
*/
/*
    public static void main(String[] args) {
        InstanceWrapper Wrapper = null;
        try {
            Wrapper = new InstanceWrapper(ClassLoader.getSystemClassLoader(), "utillib.collections.MyStackExtended");

            Wrapper.callMethod("pop");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
