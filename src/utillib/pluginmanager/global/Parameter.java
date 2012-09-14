package utillib.pluginmanager.global;

/**
 * 
 * @author Dalton Dell
 */
public class Parameter {
	public static final Class<?> _PRIMITIVE_VOID_ = void.class;
	public static final Class<?> _PRIMITIVE_BOOLEAN_ = boolean.class;
	public static final Class<?> _PRIMITIVE_BYTE_ = byte.class;
	public static final Class<?> _PRIMITIVE_SHORT_ = short.class;
	public static final Class<?> _PRIMITIVE_INT_ = int.class;
	public static final Class<?> _PRIMITIVE_LONG_ = long.class;
	public static final Class<?> _PRIMITIVE_CHAR_ = char.class;
	public static final Class<?> _PRIMITIVE_FLOAT_ = float.class;
	public static final Class<?> _PRIMITIVE_DOUBLE_ = double.class;

	private final Class<?> _CLASS_TYPE;
	private final Object _ARGUMENT;

	public Parameter(Class<?> classtype, Object argument) {
		_CLASS_TYPE = classtype;
		_ARGUMENT = argument;
	}

	public Class<?> getType() {
		return _CLASS_TYPE;
	}

	public Object getArgument() {
		return _ARGUMENT;
	}
}
