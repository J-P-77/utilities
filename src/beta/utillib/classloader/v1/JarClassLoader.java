package beta.utillib.classloader.v1;

import beta.utillib.classloader.ClassConstants;

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
 * @author Justin Palinkas
 *
 * </pre>
 */
public class JarClassLoader extends MyLocalClassloader implements ZipJarConstants, ClassConstants {    
    private final File _FILE;

    private JarFile _JarFile = null;
    
    public JarClassLoader(String file)
            throws FileNotFoundException, InvalidFileTypeException, IOException {
        
        this(new File(file), null, null);
    }
    public JarClassLoader(File file) throws FileNotFoundException, 
            InvalidFileTypeException, IOException {

        this(file, null, null);
    }

    public JarClassLoader(String file, ClassLoader parent)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), parent, null);
    }

    public JarClassLoader(String file, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), parent, manager);
    }

    public JarClassLoader(String file, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(new File(file), null, manager);
    }

    public JarClassLoader(File file, LoadedJarsManager manager) 
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(file, null, manager);
    }

    public JarClassLoader(File file, ClassLoader parent)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        this(file, parent, null);
    }

    public JarClassLoader(File file, ClassLoader parent, LoadedJarsManager manager)
            throws FileNotFoundException, InvalidFileTypeException, IOException {

        super(parent);

        if(file == null) {
            throw new RuntimeException("Variable[file] - Is Null");
        }
        	
    	if(!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " Does Not Exists");
        }
        	
    	if(!FileUtil.isFileType(file, _JAR_MAGIC_NUMBER_)) {
            throw new InvalidFileTypeException(file.getPath());
        }
        
        _FILE = file;
        _JarFile = new JarFile(file);

        super.setManifest(_JarFile.getManifest());
        
        if(manager != null) {///////////
            super.setLoadedJarsManager(manager);
            manager.add(_FILE.getName(), this);
        }///////////

        if(super.getManifest() == null) {
            _LOG.printInformation("Manifest In: " + (getName() == null ? "Unknown Name" : getName()) +  " Not Found");
        } else {
            _LOG.printInformation("Manifest Found In: " + (getName() == null ? "Unknown Name" : getName()));
            loadDepends(_FILE.getParentFile(), super.getManifest());
        }
    }
    
    public File getFile() {
        return _FILE;
    }

    public Enumeration<JarEntry> getJarEntries() {
        return _JarFile.entries();
    }

    public JarEntry getJarEntry(String name) {
        return (isClosed() ? null : _JarFile.getJarEntry(name));
    }

    @Override
    public String getName() {
        return _FILE.getName();
    }

    @Override
    protected Class<?> findLocalClass(String name) {
    	if(!isClosed()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', '/');
            Buffer.append(".class");

            final String CLASS_NAME = Buffer.toString();

            final JarEntry ENTRY = _JarFile.getJarEntry(CLASS_NAME);

            if(ENTRY != null) {
                InputStream IStream = null;
                try {
                    IStream = _JarFile.getInputStream(ENTRY);

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
							IStream = null;
						} catch(Exception e) {}
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

                final JarEntry ENTRY = _JarFile.getJarEntry(NAME);

                if(ENTRY != null) {
                    try {
                        return new URL("jar:file:/" + _FILE.toURI().toURL() + "!/" + NAME);
                    } catch (Exception e) {}//e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected InputStream getLocalResourceAsStream(String name) {
    	if(!isClosed()) {
            final JarEntry ENTRY = getJarEntry(name);

            if(ENTRY != null) {
                try {
                    return _JarFile.getInputStream(ENTRY);
                } catch (Exception e) {}
            }
        }

        return null;
    }

    @Override
    protected boolean localResourceExists(String name) {
        return getJarEntry(name) != null;
    }

    @Override
    public boolean isLocalClosed() {
        return _JarFile == null;
    }

    @Override
    public void localClose() {
        if(_JarFile != null) {
            try {
                _JarFile.close();
            } catch (Exception e) {}
            _JarFile = null;
            
            _LOG.printInformation("Local: " + _FILE.getName() + " - Closed");
        }
    }
/*
    public static File downloadJar(URL url, String jarname, String to, IProgress progress) throws IOException, InvalidFileTypeException {
        final File FILE;

        if(jarname == null) {
            throw new RuntimeException("Class[JarClassLoader] - Method[downloadJar] - Variable[jarname] - Is Null");
        }

        if(to == null) {
            FILE = File.createTempFile(jarname, (jarname.endsWith(".jar") ? "" : ".jar"));//Create A Temp Jar File
//            to = SystemProperties.getProperty(SystemProperties._TEMP_DIRECTORY_);
        } else {
            final File TO_DIR = new File(to).getAbsoluteFile();

            if(!TO_DIR.exists()) {
                TO_DIR.mkdirs();
            }

            FILE = new File(TO_DIR, (jarname.endsWith(".jar") ? jarname : jarname + ".jar"));
        }

        if(FILE.exists()) {
            return FILE;
        }

        final URLConnection CONNECTION = url.openConnection();

        if(CONNECTION != null) {
            InputStream IStream = null;
            FileOutputStream OStream = null;
            try {
                CONNECTION.connect();

                if(progress != null) {
                    progress.setValue(0);
                    progress.setMinimum(0);
                    progress.setMaximum(CONNECTION.getContentLength());
                }

                IStream = CONNECTION.getInputStream();
                OStream = new FileOutputStream(FILE);

                final byte[] HEADER = new byte[4];
                final int HEADERLEN = IStream.read(HEADER, 0, HEADER.length);
                if(HEADERLEN == 4) {
                    for(int X = 0; X < 4; X++) {
                        if(HEADER[X] != _ZIP_JAR_MAGIC_NUMBER_[X]) {
                            throw new InvalidFileTypeException(url.toString());
                        }
                    }
                    OStream.write(HEADER, 0, HEADERLEN);
                } else {
                    throw new InvalidFileTypeException(url.toString());
                }

                if(progress != null) {
                    progress.increment(4);
                }

                byte[] Buffer = new byte[32];
                int ReadLen = -1;
                while((ReadLen = IStream.read(Buffer, 0, Buffer.length)) > 0) {
                    OStream.write(Buffer, 0, ReadLen);

                    if(progress != null) {
                        progress.increment(ReadLen);
                    }
                }
                Buffer = null;
                
                progress.setValue(CONNECTION.getContentLength());
            } catch (InvalidFileTypeException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } finally {
                if(IStream != null) {
                    IStream.close();
                    IStream = null;
                }

                if(OStream != null) {
                    OStream.close();
                    OStream = null;
                }
            }
        }

        return FILE;
    }
 */
}

/*
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        _DEBUGHANDLER.printInformation("Find Class: " + name);

        //Search Local Loaded Classes
        for(int X = 0; X < _LOADEDCLASS.length(); X++) {
            final Class CLASS = _LOADEDCLASS.getAt(X);
            final String NAME = CLASS.getName();

            if(name.equals(NAME)) {
                _DEBUGHANDLER.printInformation("Loaded Classes Found Class: " + name);
                return CLASS;
            }
        }

        final JarEntry ENTRY;
        if(isOpen()) {
            MyStringBuffer Buffer = new MyStringBuffer(name, 6);
            Buffer.replace('.', '/');
            Buffer.append(".class");

            ENTRY = _JarFile.getJarEntry(Buffer.toString());
        } else {
            ENTRY = null;
        }

        if(ENTRY == null) {
            for(int X = 0; X < _DEPENDS.length(); X++) {
                final Class CLASS = _DEPENDS.getAt(X).findClass(name);

                if(CLASS != null) {
                    _DEBUGHANDLER.printInformation("Depends Found Class: " + name);
                    return CLASS;
                }
            }
        } else {
            InputStream IStream = null;
            Class Clazz = null;
            try {
                IStream = _JarFile.getInputStream(ENTRY);

                if(name.equalsIgnoreCase("utillib.arguments.CmdLineHandlerBeta$Cmd")) {
                    System.out.println("");
                }

                Clazz = readClass(name, IStream);
            } catch (Exception e) {
                _DEBUGHANDLER.printError(e);
            } finally {
                try {
                    if(IStream != null) {
                        IStream.close();
                        IStream = null;
                    }
                } catch (Exception e) {}
            }

            return Clazz;
//            InputStream IStream = null;
//            byte[] ReadBuffer = null;
//            Class Clazz = null;
//            try {
//                ReadBuffer = new byte[64];
//                IStream = _JarFile.getInputStream(ENTRY);
//
//                final ResizingByteArray BYTES = new ResizingByteArray(IStream.available());
//
//                BYTES.setIncreaseBy(64);
//
//                int BytesRead = -1;
//                while((BytesRead = IStream.read(ReadBuffer, 0, ReadBuffer.length)) != -1) {
//                    BYTES.put(ReadBuffer, 0, BytesRead);
//                }
//
////                stupidByteCorrection(Bytes, 0, Bytes.length);
//
//                final Class CLASS = defineClass(name, BYTES.toArray(), 0, BYTES.length());
//
//                if(CLASS != null) {
//                    _DEBUGHANDLER.printInformation(_FILE.getName() + " - Found Class: " + name);
//                    _LOADEDCLASS.put(CLASS);
//                }
//                ReadBuffer = null;
//
//                Clazz = CLASS;
//            } catch (Exception e) {
//                _DEBUGHANDLER.printError(e);
//            }
//
//            return Clazz;
        }

        return null;
    }
*/