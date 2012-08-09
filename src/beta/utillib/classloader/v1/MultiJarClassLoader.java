package beta.utillib.classloader.v1;

import utillib.arrays.ResizingArray;

import utillib.strings.MyStringBuffer;

import utillib.utilities.ZipJarConstants;

import utillib.exceptions.InvalidFileTypeException;

import utillib.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;

import java.net.URL;

/**<pre>
 * <b>Current Version 1.0.0</b>
 *
 * March 16, 2010 (Version 1.0.0)
 *     -First Released
 *
 * August 9, 2010 (Version 1.1.0)
 * 	   -Added
 *         -This Will Now On The Fly Open Jar Files
 *
 * @author Justin Palinkas
 *
 * </pre>
 */
public class MultiJarClassLoader extends MyLocalClassloader implements ZipJarConstants {
    private final ResizingArray<Jar> _JARFILES = new ResizingArray<Jar>(1, 2);

    private final String _NAME;

    public MultiJarClassLoader(String file)
            throws FileNotFoundException, InvalidFileTypeException, IOException {
        
        this(new File(file), null, null);
    }

    public MultiJarClassLoader(File file) throws FileNotFoundException,
            InvalidFileTypeException, IOException {

        this(file, null, null);
    }
    
    public MultiJarClassLoader(String file, ClassLoader parent)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), parent, null);
    }

    public MultiJarClassLoader(String file, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), parent, manager);
    }

    public MultiJarClassLoader(String file, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), null, manager);
    }

    public MultiJarClassLoader(File file, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(file, null, manager);
    }

    public MultiJarClassLoader(File file, ClassLoader parent)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(file, parent, null);
    }

    public MultiJarClassLoader(File file, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        super(parent);

        if(file == null) {
            throw new RuntimeException("Variable[file] - Is Null");
        }
        	
    	if(!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " Does Not Exists");
        }
        
        if(!FileUtil.isFileType(file, JarClassLoader._JAR_MAGIC_NUMBER_)) {
            throw new InvalidFileTypeException(file.getPath());
        }

        _NAME = file.getName();

        final JarFile JAR_FILE = new JarFile(file);

        super.setManifest(JAR_FILE.getManifest());
        
        if(manager != null) {///////////
            super.setLoadedJarsManager(manager);
            manager.add(file.getName(), this);
        }///////////

        if(super.getManifest() == null) {
            _LOG.printInformation("Manifest In: " + (getName() == null ? "Unknown Name" : getName()) +  " Not Found");
        } else {
            _LOG.printInformation("Manifest Found In: " + (getName() == null ? "Unknown Name" : getName()));
            loadDepends(file.getParentFile(), super.getManifest());
        }
    }

    public void addJar(File jarfile) {
        if(jarfile.exists() && jarfile.isFile() && jarfile.canRead()) {
            JarFile JarFile = null;
            try {
                JarFile = new JarFile(jarfile);

                final ResizingArray<JarEntry> JARENTRIES = new ResizingArray<JarEntry>(JarFile.size());

                final Enumeration<JarEntry> ENTRIES = JarFile.entries();

                while(ENTRIES.hasMoreElements()) {
                    final JarEntry ENTRY = ENTRIES.nextElement();

                    JARENTRIES.put(ENTRY);
                }

                if(JARENTRIES.length() > 0) {
                    _JARFILES.put(new Jar(jarfile, JARENTRIES.toArray(new JarEntry[JARENTRIES.length()])));

                    if(JarFile.getManifest() != null) {
                        loadDepends(jarfile.getParentFile(), JarFile.getManifest());
                    }
                }
            } catch (Exception e) {
            	_LOG.printError(e);
			} finally {
				if(JarFile != null) {
					try {
						JarFile.close();
					} catch(Exception e) {}
					JarFile = null;
				}
			}
        }
    }

    public void removeJarFileAt(int index) {
        if(_JARFILES.validIndex(index)) {
            _JARFILES.removeAt(index);
        }
    }
/*
    public void removeJarFile(JarFile jarfile) {
        _JARFILES.removeAll(jarfile);
    }
*/
    public void removeAllJarFiles() {
        _JARFILES.removeAll();
    }

    public int jarFileCount() {
        return _JARFILES.length();
    }

    public JarEntry getJarEntry(String name) {
    	if(!isClosed()) {
            for(int X = 0; X < _JARFILES.length(); X++) {
                final Jar JAR = _JARFILES.getAt(X);
                final JarEntry ENTRY = JAR.getJarEntry(name);

                if(ENTRY != null) {
                    return ENTRY;
                }
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return _NAME;
    }

    @Override
    protected Class<?> findLocalClass(String name) {
    	if(!isClosed()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', '/');
            Buffer.append(".class");

            final String CLASS_NAME = Buffer.toString();

            for(int X = 0; X < _JARFILES.length(); X++) {
                final Jar JAR = _JARFILES.getAt(X);
                final JarEntry ENTRY = JAR.getJarEntry(CLASS_NAME);

                if(ENTRY != null) {
                    InputStream IStream = JAR.getInputStream(ENTRY);
                    
                    if(IStream != null) {
	                    try {
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
        }

        return null;
    }

    //jar:file:/C:/Documents%20and%20Settings/Dalton%20Dell/Desktop/Java%20Test/ClassBrowser.jar!/classbrowser/MyFilter.class
    @Override
    protected URL findLocalResource(String name) {
        if(name != null && name.length() > 0) {
        	if(!isClosed()) {
                final String NAME = name;
//                final String NAME = (name.charAt(0) == '/' ? name.substring(1) : name);

                for(int X = 0; X < _JARFILES.length(); X++) {
                    final Jar JAR = _JARFILES.getAt(X);
                    final JarEntry ENTRY = JAR.getJarEntry(name);

                    if(ENTRY != null) {
                        try {
                            return new URL("jar:file:/" + JAR._JARPATH.getAbsoluteFile().toURI().toURL() + "!/" + NAME);
                        } catch (Exception e) {}//e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected InputStream getLocalResourceAsStream(String name) {
        for(int X = 0; X < _JARFILES.length(); X++) {
            final Jar JAR = _JARFILES.getAt(X);
            final JarEntry ENTRY = JAR.getJarEntry(name);

            if(ENTRY != null) {
            	return JAR.getInputStream(ENTRY);
            }
        }

        return null;
    }

    @Override
    protected boolean localResourceExists(String name) {
        for(int X = 0; X < _JARFILES.length(); X++) {
            final JarEntry ENTRY = _JARFILES.getAt(X).getJarEntry(name);

            if(ENTRY != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isLocalClosed() {
        for(int X = 0; X < _JARFILES.length(); X++) {
            if(!_JARFILES.getAt(X).isClosed()) {
            	return false;
            }
        }

        return true;
    }

    @Override
    public void localClose() {
        close();

        _JARFILES.removeAll();
        _LOG.printInformation("Local: All Closed");
    }

    @Override
    public void close() {
        for(int X = 0; X < _JARFILES.length(); X++) {
            _JARFILES.getAt(X).close(true);
        }
    }

    private void closeAllNotUsedButThis(Jar jar) {
        for(int X = 0; X < _JARFILES.length(); X++) {
            if(!_JARFILES.getAt(X).equals(jar)) {
                _JARFILES.getAt(X).close(false);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    //CLASSES
    //Just Used To Keep Track Of The Number Of Streams Open In A Given Jar
    private class WrapperInputStream extends InputStream {
        private final Jar _JAR;
        private InputStream _IStream;

        public WrapperInputStream(InputStream istream, Jar jar) {
            _IStream = istream;
            _JAR = jar;

            _JAR._OpenStreams++;
        }

        @Override
        public int read() throws IOException {
            return _IStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return _IStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return _IStream.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            if(_IStream != null) {
                _IStream.close();
                _IStream = null;
                
                _JAR._OpenStreams--;

                if(!_JAR.hasOpenStreams()) {
                    _JAR.close(false);
                }
            }
        }
    }

    private class Jar {
        private final File _JARPATH;
        private final JarEntry[] _ENTRIES;

        private JarFile _JarFile;
        
        private int _OpenStreams = 0;

        private Jar(File jarpath, JarEntry[] entries) {
            _JARPATH = jarpath;
            _ENTRIES = entries;
        }

        private boolean open(){
            closeAllNotUsedButThis(this);
            
            if(_JarFile == null) {
                try {
                    _JarFile = new JarFile(_JARPATH);
                } catch (Exception e) {
                    _LOG.printError(e);
                    _JarFile = null;
                }
            }

            return !isClosed();
        }

        private void close(boolean force) {
            if(!hasOpenStreams() || force) {
                close();
            }
        }

		private void close() {
			if(_JarFile != null) {
				try {
					_JarFile.close();
				} catch(Exception e) {}
				_JarFile = null;
			}
		}

        public boolean hasOpenStreams() {
            return _OpenStreams > 0;
        }

        private boolean isClosed() {
            return _JarFile == null;
        }

        private JarEntry getJarEntry(String name) {
            for(int X = 0; X < _ENTRIES.length; X++) {
                if(_ENTRIES[X].getName().equals(name)) {
                    return _ENTRIES[X];
                }
            }

            return null;
        }
        
        private InputStream getInputStream(JarEntry jarentry) {
        	if(open()) {
        		try {
        			return new WrapperInputStream(_JarFile.getInputStream(jarentry), this);
        		} catch (Exception e) {
        			_LOG.printError(e);
        		}
        	}
        	
        	return null;
        }
    }
}
