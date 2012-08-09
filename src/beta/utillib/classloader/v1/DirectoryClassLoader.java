package beta.utillib.classloader.v1;

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
public class DirectoryClassLoader extends MyLocalClassloader {
    private File _Directory;

    public DirectoryClassLoader(String directory) throws FileNotFoundException, IOException {
        this(new File(directory), null, null);
    }

    public DirectoryClassLoader(File directory) throws FileNotFoundException, IOException {
        this(directory, null, null);
    }
    
    public DirectoryClassLoader(String directory, ClassLoader parent)
            throws FileNotFoundException, IOException {

        this(new File(directory), parent, null);
    }

    public DirectoryClassLoader(String directory, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(new File(directory), parent, manager);
    }

    public DirectoryClassLoader(String directory, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(new File(directory), null, manager);
    }

    public DirectoryClassLoader(File directory, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        this(directory, null, manager);
    }

    public DirectoryClassLoader(File directory, ClassLoader parent)
            throws FileNotFoundException, IOException {

        this(directory, parent, null);
    }

    public DirectoryClassLoader(File directory, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, IOException {

        super(parent);

        if(directory == null) {
            throw new RuntimeException("Variable[file] - Is Null");
        }
        
    	if(!directory.exists()) {
            throw new FileNotFoundException(directory.getPath() + " Does Not Exists");
        }
        	
    	if(!directory.isDirectory()) {
            throw new RuntimeException(directory.getPath() + " Is Not A Directory");
        }

        _Directory = directory;

        final File MANIFEST = new File(_Directory, "META-INF" + FileUtil._S_ +  "MANIFEST.MF");

        if(MANIFEST.exists()) {
            FileInputStream IStream = new FileInputStream(MANIFEST);

            super.setManifest(new Manifest(IStream));

            IStream.close();
            IStream = null;
        }

        if(manager != null) {///////////
            super.setLoadedJarsManager(manager);
            manager.add(_Directory.getName(), this);
        }///////////

        if(super.getManifest() == null) {
            _LOG.printInformation("Manifest In: " + (getName() == null ? "Unknown Name" : getName()) +  " Not Found");
        } else {
            _LOG.printInformation("Manifest Found In: " + (getName() == null ? "Unknown Name" : getName()));
            loadDepends(_Directory, super.getManifest());
        }
    }

    public File getFile() {
        return _Directory;
    }

    @Override
    protected Class<?> findLocalClass(String name) {
        if(!isClosed()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', FileUtil._S_c);
            Buffer.append(".class");

            final String CLASS_NAME = Buffer.toString();

            final File FILE = new File(_Directory, CLASS_NAME);

            if(FILE.exists()) {
                FileInputStream IStream = null;
                try {
                    IStream = new FileInputStream(FILE);

//                    final byte[] CLASS_BYTES = readClassBytes(name, IStream);
//                    final Class CLASS = super.defineClass(name, CLASS_BYTES, 0, CLASS_BYTES.length);
//
//                    if(CLASS != null) {
//                        _DEBUGHANDLER.printInformation("Loaded Class: " + name);
//                        return CLASS;
//                    }

                    return readClass(name, IStream);
//                } catch (utillib.exceptions.InvalidClassException e) {
//                    _DEBUGHANDLER.printError(e.getMessage());
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

        return null;
    }

    //jar:file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
    @Override
    protected URL findLocalResource(String name) {
    	if(!isClosed()) {
            if(name.length() > 0) {
                final String NAME = name.replace('/', FileUtil._S_c);

                final File FILE = new File(_Directory, NAME);

                if(FILE.exists()) {
                    try {
                        return new URL("file:/" + FILE.toURI().toURL());
                    } catch (Exception e) {}//e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected InputStream getLocalResourceAsStream(String name) {
    	if(!isClosed()) {
            final String NAME = name.replace('/', FileUtil._S_c);

            final File FILE = new File(_Directory, NAME);

            if(FILE != null) {
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
            
            return new File(_Directory, NAME).exists();
        }

        return false;
    }

    @Override
    public String getName() {
        return _Directory.getName();
    }

    @Override
    public boolean isLocalClosed() {
        return _Directory == null || !_Directory.canRead();
    }

    @Override
    public void localClose() {
        _LOG.printInformation("Local: " + _Directory.getName() + " - Closed");
        _Directory = null;
    }
}
