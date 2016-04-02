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

package jp77.utillib.utilities;

import java.io.InputStream;

import java.net.URL;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * March 19, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ClassResourceUtil {
	/**
	 * Use Method getResource(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static URL getResource(Class<?> aclass, String resource) {
		if(aclass == null) {
			throw new RuntimeException("Variable[aclass] - Is Null");
		}

		return getResource(aclass.getClassLoader(), resource);
	}

	public static URL getResource(String resource) {
		return getResource(ClassLoader.getSystemClassLoader(), resource);
	}

	public static URL getResource(ClassLoader classloader, String resource) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(resource == null) {
			throw new RuntimeException("CVariable[resource] - Is Null");
		}

		final URL L_URL = classloader.getResource(resource);

		return (L_URL == null ? getSystemResource(resource) : L_URL);
	}

	/**
	 * Use Method getResourceAsStream(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static InputStream getResourceAsStream(Class<?> clazz, String resource) {
		return getResourceAsStream(clazz.getClassLoader(), resource);
	}

	public static InputStream getResourceAsStream(String resource) {
		return getResourceAsStream(ClassLoader.getSystemClassLoader(), resource);
	}

	public static InputStream getResourceAsStream(ClassLoader classloader, String resource) {
		if(classloader == null) {
			throw new RuntimeException("Variable[classloader] - Is Null");
		}

		if(resource == null) {
			throw new RuntimeException("Variable[resource] - Is Null");
		}

		final InputStream ISTREAM = classloader.getResourceAsStream(resource);

		return (ISTREAM == null ? getSystemResourceAsStream(resource) : ISTREAM);
	}

	public static URL getSystemResource(String resource) {
		if(resource == null) {
			throw new RuntimeException("Variable[resource] - Is Null");
		}

		final String RESOURCE = (resource.startsWith("/") ? resource.substring(1) : resource);

		return ClassLoader.getSystemClassLoader().getResource(RESOURCE);
	}

	public static InputStream getSystemResourceAsStream(String resource) {
		if(resource == null) {
			throw new RuntimeException("Variable[resource] - Is Null");
		}

		final String RESOURCE = (resource.startsWith("/") ? resource.substring(1) : resource);

		return ClassLoader.getSystemClassLoader().getResourceAsStream(RESOURCE);
	}
}
/*
        if(resource == null) {
            throw new RuntimeException("Class[ImageUtil] - Method[getClassResource] - Variable[resource] - Is Null");
        }
        final URL RESOURCE_URL = aclass.getClassLoader().getResource(resource);
        if(RESOURCE_URL == null) {
            return getSystemResource(resource);
        } else {
            return RESOURCE_URL;
        }
*/
/*
    public static URL getResource(Class aclass, String resource) {
        if(aclass == null) {
            throw new RuntimeException("Class[ImageUtil] - Method[getClassResource] - Variable[aclass] - Is Null");
        }
        if(resource == null) {
            throw new RuntimeException("Class[ImageUtil] - Method[getClassResource] - Variable[resource] - Is Null");
        }
        final URL RESOURCE_URL = aclass.getResource(resource);
        if(RESOURCE_URL == null) {
			throw new RuntimeException("Resource: " + resource + " - Does Not Exist");
        } else {
            return RESOURCE_URL;
        }
    }
    public static URL getResource(String resource) {
        if(resource == null) {
            throw new RuntimeException("Class[ImageUtil] - Method[getClassResource] - Variable[resource] - Is Null");
        }
        final String RESOURCE = (resource.startsWith("/") ? resource.substring(1) : resource);
        return ClassLoader.getSystemClassLoader().getResource(RESOURCE);
    }
    public static InputStream getInputStreamResource(String resource) {
        if(resource == null) {
            throw new RuntimeException("Class[ImageUtil] - Method[getInputStreamResource] - Variable[resource] - Is Null");
        }
        final String RESOURCE = (resource.startsWith("/") ? resource.substring(1) : resource);
        return ClassLoader.getSystemResourceAsStream(RESOURCE);
    }
*/