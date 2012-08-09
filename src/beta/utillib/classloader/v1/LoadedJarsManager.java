package beta.utillib.classloader.v1;

import utillib.collections.Collection;

/**
 *
 * @author Dalton Dell
 */
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
