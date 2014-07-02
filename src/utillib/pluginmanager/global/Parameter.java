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

package utillib.pluginmanager.global;

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
