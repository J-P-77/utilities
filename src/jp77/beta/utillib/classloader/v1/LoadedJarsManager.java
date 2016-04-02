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

package jp77.beta.utillib.classloader.v1;

import jp77.utillib.collections.Collection;

public class LoadedJarsManager {
	private Collection<String, MyClassloader> _Loaded = new Collection<String, MyClassloader>();

	public void add(String jarname, MyClassloader classloader) {
		_Loaded.add(jarname, classloader);
	}

	public void remove(String jarname) {
		_Loaded.remove(jarname);
	}

	public void removeAll() {
		_Loaded.removeAll();
	}

	public MyClassloader getLoader(int index) {
		return _Loaded.getAt(index);
	}

	public ClassLoader getLoader(String jarname) {
		for(int X = 0; X < _Loaded.length(); X++) {
			final Collection.Entry ENTRY = _Loaded.getEntryAt(X);

			if(ENTRY.getKey().equals(jarname)) {
				return (JarClassLoader)ENTRY.getObject();
			}
		}

		return null;
	}

	public int loaderCount() {
		return _Loaded.length();
	}

	public boolean isLoaded(String jarname) {
		for(int X = 0; X < _Loaded.length(); X++) {
			final Collection.Entry ENTRY = _Loaded.getEntryAt(X);

			if(ENTRY.getKey().equals(jarname)) {
				return true;
			}
		}

		return false;
	}

	public int isLoadedIndex(String jarname) {
		for(int X = 0; X < _Loaded.length(); X++) {
			final Collection.Entry ENTRY = _Loaded.getEntryAt(X);

			if(ENTRY.getKey().equals(jarname)) {
				return X;
			}
		}

		return -1;
	}
}
