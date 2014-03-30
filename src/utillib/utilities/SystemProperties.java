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

package utillib.utilities;

import utillib.collections.Collection;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * <pre>
 * <b>Current Version 2.0.0</b>
 * 
 * November 07, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * December 11, 2009 (Version 1.0.1)
 *     -Updated
 *         -Little Bit Of EveryThing
 * 
 * March 11, 2010 (Version 1.0.2)
 *     -Fix Bug
 *         -When Checking OS I Used The Wrong File Separator Character
 *     -Updated
 *         -The Checking Of The OS Version
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class SystemProperties implements User {
	public static enum Property {
		APP_PATH,

		JAVA_HOME,
		JAVA_VENDOR,
		JAVA_VENDOR_URL,
		JAVA_VERSION,
		JAVA_CLASS_PATH,
		JAVA_CLASS_VERSION,

		OS_ARCH,
		OS_NAME,
		OS_VERSION,

		USER_HOME,
		USER_NAME,
		USER_LANGUAGE,
		USER_COUNTRY,

		FILE_SEPARATOR,
		PATH_SEPARATOR,
		LINE_SEPARATOR,

		JAVA_SPECIFICATION_VERSION,
		JAVA_SPECIFICATION_VENDOR,
		JAVA_SPECIFICATION_NAME,

		JAVA_VM_SPECIFICATION_VERSION,
		JAVA_VM_SPECIFICATION_VENDOR,
		JAVA_VM_SPECIFICATION_NAME,
		JAVA_VM_VERSION,
		JAVA_VM_VENDOR,
		JAVA_VM_NAME,

		JAVA_CLASS_VERISON,
		JAVA_RUNTIME_VERSION,

		JAVA_TEMP_DIRECTORY,

		SUN_CPU_LIST,
		SUN_ARCH_DATA_MODEL,
		SUN_CPU_ENDIAN,
		SUN_OS_PATCH_LEVEL;
	};

	public static String getProperty(Property value) {
		switch(value) {
			case APP_PATH:
				return System.getProperty("user.dir");

			case JAVA_HOME:
				return System.getProperty("java.home");

			case JAVA_VENDOR:
				return System.getProperty("java.vendor");

			case JAVA_VENDOR_URL:
				return System.getProperty("java.vendor.url");

			case JAVA_VERSION:
				return System.getProperty("java.version");

			case JAVA_CLASS_PATH:
				return System.getProperty("java.class.path");

			case JAVA_CLASS_VERSION:
				return System.getProperty("java.class.version");

			case OS_ARCH:
				return System.getProperty("os.arch");

			case OS_NAME:
				return System.getProperty("os.name");

			case OS_VERSION:
				return System.getProperty("os.version");

			case USER_HOME:
				return System.getProperty("user.home");

			case USER_NAME:
				return System.getProperty("user.name");

			case USER_LANGUAGE:
				return System.getProperty("user.language");

			case USER_COUNTRY:
				return System.getProperty("user.country");

			case FILE_SEPARATOR:
				return System.getProperty("file.separator");

			case PATH_SEPARATOR:
				return System.getProperty("path.separator");

			case LINE_SEPARATOR:
				return System.getProperty("line.separator");

			case JAVA_SPECIFICATION_VERSION:
				return System.getProperty("java.specification.version");

			case JAVA_SPECIFICATION_VENDOR:
				return System.getProperty("java.specification.vendor");

			case JAVA_SPECIFICATION_NAME:
				return System.getProperty("java.specification.name");

			case JAVA_VM_SPECIFICATION_VERSION:
				return System.getProperty("java.vm.specification.version");

			case JAVA_VM_SPECIFICATION_VENDOR:
				return System.getProperty("java.vm.specification.vendor");

			case JAVA_VM_SPECIFICATION_NAME:
				return System.getProperty("java.vm.specification.name");

			case JAVA_VM_VERSION:
				return System.getProperty("java.vm.version");

			case JAVA_VM_VENDOR:
				return System.getProperty("java.vm.vendor");

			case JAVA_VM_NAME:
				return System.getProperty("java.vm.name");

			case JAVA_CLASS_VERISON:
				return System.getProperty("java.class.version");

			case JAVA_RUNTIME_VERSION:
				return System.getProperty("java.runtime.version");

			case JAVA_TEMP_DIRECTORY:
				return System.getProperty("java.io.tmpdir");

			case SUN_CPU_LIST:
				return System.getProperty("sun.cpu.isalist");

			case SUN_ARCH_DATA_MODEL:
				return System.getProperty("sun.arch.data.model");

			case SUN_CPU_ENDIAN:
				return System.getProperty("sun.cpu.endian");

			case SUN_OS_PATCH_LEVEL:
				return System.getProperty("sun.os.patch.level");

		}

		return null;
	}

	public static String getProperty(String value) {
		return System.getProperty(value);
	}

	public static Collection<String, String> getProperties() {
		final Collection<String, String> RESULTS = new Collection<String, String>();

		final Properties PROP = System.getProperties();

		final Set<?> SET = PROP.entrySet();
		final Iterator<?> I = SET.iterator();

		while(I.hasNext()) {
			final Entry<?, ?> E = ((Entry<?, ?>)I.next());

			RESULTS.add((String)E.getKey(), (String)E.getValue());
		}

		return RESULTS;
	}
}
