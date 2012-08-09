package beta.utillib.classloader.v1;

import utillib.arrays.ResizingArray;

import utillib.file.FileUtil;

import utillib.strings.MyStringBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.jar.Manifest;

/**<pre>
 * <b>Current Version 1.0.0</b>
 *
 * March 16, 2010 (Version 1.0.0)
 *     -First Released
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public class MultiDirectoryClassLoader extends MyLocalClassloader {
//    private static final ADebugHandler _DEBUGHANDLER_ = DefaultDebugHandler.createLog(MyClassloader.class.getCanonicalName(), 5);

    private final ResizingArray<File> _DIRECTORIES = new ResizingArray<File>(1);

    private final String _NAME;
    
    public MultiDirectoryClassLoader(String directory)
            throws FileNotFoundException, IOException {
        
        this(new File(directory), null, null);
    }
    
    public MultiDirectoryClassLoader(File directory) throws FileNotFoundException, IOException {
        this(directory, null, null);
    }

    public MultiDirectoryClassLoader(String directory, ClassLoader parent)
            throws FileNotFoundException, IOException {

        this(new File(directory), parent, null);
    }

    public MultiDirectoryClassLoader(String directory, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(new File(directory), parent, manager);
    }

    public MultiDirectoryClassLoader(String directory, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(new File(directory), null, manager);
    }

    public MultiDirectoryClassLoader(File directory, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(directory, null, manager);
    }

    public MultiDirectoryClassLoader(File directory, ClassLoader parent)
            throws FileNotFoundException, IOException {

        this(directory, parent, null);
    }


    public MultiDirectoryClassLoader(File directory, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        super(parent);

        if(directory == null) {
            throw new NullPointerException("Variable[directory] - Is Null");
        }
        	
    	if(!directory.exists()) {
            throw new FileNotFoundException(directory.getPath() + " Does Not Exists");
        }
        	
    	if(!directory.isDirectory()) {
            throw new RuntimeException(directory.getPath() + " Is Not A Directory");
        }

        _NAME = directory.getName();
        _DIRECTORIES.put(directory);
        
        final File MANIFEST = new File(directory, "META-INF" + FileUtil._S_ +  "MANIFEST.MF");

        if(MANIFEST.exists()) {
            FileInputStream IStream = new FileInputStream(MANIFEST);

            super.setManifest(new Manifest(IStream));

            IStream.close();
            IStream = null;
        }

        if(manager != null) {///////////
            super.setLoadedJarsManager(manager);
            manager.add(directory.getName(), this);
        }///////////

        if(super.getManifest() == null) {
            _LOG.printInformation("Manifest In: " + (getName() == null ? "Unknown Name" : getName()) +  " Not Found");
        } else {
            _LOG.printInformation("Manifest Found In: " + (getName() == null ? "Unknown Name" : getName()));
            loadDepends(directory, super.getManifest());
        }
    }

    public void addDirectory(File directory) {
        if(directory.exists() && directory.isDirectory() && directory.canRead()) {
            _DIRECTORIES.put(directory);

            if(super.getLoadedJarsManager() != null) {///////////
                super.getLoadedJarsManager().add(directory.getName(), this);
            }///////////

            final File MANIFEST = new File(directory, "META-INF" + FileUtil._S_ +  "MANIFEST.MF");

            if(MANIFEST.exists() && MANIFEST.isFile()) {
                FileInputStream IStream = null;
                try {
                    IStream = new FileInputStream(MANIFEST);

                    loadDepends(directory, new Manifest(IStream));
                } catch (Exception e) {
                	_LOG.printError(e);
            	} finally {
					if(IStream != null) {
						try {
							IStream.close();
						} catch(Exception e) {}
						IStream = null;
					}
				}
            }
        }
    }

    public void removeDirectoryAt(int index) {
        if(_DIRECTORIES.validIndex(index)) {
            _DIRECTORIES.removeAt(index);
        }
    }

    public void removeDirectory(File directory) {
        _DIRECTORIES.removeAll(directory);
    }

    public void removeAllDirectories() {
        _DIRECTORIES.removeAll();
    }

    public int directoriesCount() {
        return _DIRECTORIES.length();
    }

    @Override
    public String getName() {
        return _NAME;
    }

    @Override
    protected Class<?> findLocalClass(String name) {
    	if(!isClosed()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', FileUtil._S_c);
            Buffer.append(".class");

            final String CLASS_NAME = Buffer.toString();

            for(int X = 0; X < _DIRECTORIES.length(); X++) {
                final File FILE = new File(_DIRECTORIES.getAt(X), CLASS_NAME);

                if(FILE.exists()) {
                    FileInputStream IStream = null;
                    try {
                        IStream = new FileInputStream(FILE);

                        return readClass(name, IStream);
                    } catch (Exception e) {
                        _LOG.printError(e);
                    } finally {
						if(IStream != null) {
							try {
								IStream.close();
							} catch(Exception e) {}
							IStream = null;
						}
                    }
                }
            }
        }

        return null;
    }

    //file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
    @Override
    protected URL findLocalResource(String name) {
        if(name.length() != 0) {
        	if(!isClosed()) {
                final String NAME = name.replace('/', FileUtil._S_c);
//                final String NAME = (name.charAt(0) == '/' ? name.substring(1) : name);

                for(int X = 0; X < _DIRECTORIES.length(); X++) {
                    final File FILE = new File(_DIRECTORIES.getAt(X), NAME);

                    if(FILE.exists()) {
                        try {
                            return new URL("file:/" + FILE.toURI().toURL());
                        } catch (Exception e) {}//e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected InputStream getLocalResourceAsStream(String name) {
    	if(!isClosed()) {
            final String NAME = name.replace('/', FileUtil._S_c);
            
            for(int X = 0; X < _DIRECTORIES.length(); X++) {
                final File FILE = new File(_DIRECTORIES.getAt(X), NAME);

                if(FILE.exists() && FILE.isFile() && FILE.canRead()) {
                    try {
                        return new FileInputStream(FILE);
                    } catch (Exception e) {}
                }
            }
        }

        return null;
    }

    @Override
    protected boolean localResourceExists(String name) {
    	if(!isClosed()) {
            final String NAME = name.replace('/', FileUtil._S_c);
            
            for(int X = 0; X < _DIRECTORIES.length(); X++) {
                final File FILE = new File(_DIRECTORIES.getAt(X), NAME);

                if(FILE.exists() && FILE.canRead()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isLocalClosed() {
        for(int X = 0; X < _DIRECTORIES.length(); X++) {
            final File FILE = _DIRECTORIES.getAt(X);

            if(FILE.canRead()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void localClose() {
        for(int X = 0; X < _DIRECTORIES.length();) {
            if(_DIRECTORIES.getAt(X) != null) {
                _LOG.printInformation("Local: " + _DIRECTORIES.getAt(X).getName() + " - Closed");
                _DIRECTORIES.removeAt(X);
            } else {
                X++;
            }
        }
    }

/*
    public boolean isOpen() {
        for(int X = 0; X < _DIRECTORIES.length(); X++) {
            final File FILE = _DIRECTORIES.getAt(X);

            if(FILE.canRead()) {
                return true;
            }
        }

        return false;
    }

   public void close() throws IOException {
        for(int X = 0; X < _DEPENDS.length(); X++) {
            if(_DEPENDS.getAt(X) != null) {
                _DEPENDS.getAt(X).close();
                _DEPENDS.removeAt(X);
            }
        }

        for(int X = 0; X < _DIRECTORIES.length(); X++) {
            if(_DIRECTORIES.getAt(X) != null) {
                _DIRECTORIES.removeAt(X);
            }
        }
    }
*/
}
