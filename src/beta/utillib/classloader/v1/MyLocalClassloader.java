package beta.utillib.classloader.v1;

import beta.utillib.classloader.ClassConstants;

import utillib.file.FileUtil;

import java.io.File;

import java.util.jar.Attributes;
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
public abstract class MyLocalClassloader extends MyClassloader implements ClassConstants {
    private LoadedJarsManager _Manager;
    private Manifest _Manifest;   
    
    protected MyLocalClassloader() {
        super(getSystemClassLoader());
    }

    protected MyLocalClassloader(ClassLoader parent) {
        super(parent);
    }

    public void setManifest(Manifest manifest) {
        _Manifest = manifest;
    }

    public Manifest getManifest() {
        return _Manifest;
    }

    protected void setLoadedJarsManager(LoadedJarsManager manager) {
        _Manager = manager;
    }

    public LoadedJarsManager getLoadedJarsManager() {
        return _Manager;
    }

    public void callMainMethod(String[] args) {
        if(_Manifest == null) {
            _LOG.printWarning("No Manifest");
        } else {
            final Attributes MAIN = _Manifest.getMainAttributes();

            if(MAIN == null) {
                _LOG.printWarning("No Main Attributes");
            } else {
                final String MAIN_CLASS = MAIN.getValue("Main-Class");//Attributes.Name.CLASS_PATH

                if(MAIN_CLASS != null && MAIN_CLASS.length() > 0) {
                    callMainMethod(MAIN_CLASS, args);
                }
            }
        }
    }

    protected void loadDepends(File defaultlibdir, Manifest manifest) {
        if(defaultlibdir == null) {
            throw new RuntimeException("Variable[defaultlibdir] - Is Null");
        }

        if(!defaultlibdir.exists()) {
            throw new RuntimeException(defaultlibdir.getPath() + " Does Not Exists");
        }

        if(!defaultlibdir.isDirectory()) {
            throw new RuntimeException(defaultlibdir.getPath() + " Is Not A Directory");
        }
        
        if(manifest != null) {
            final Attributes MAIN = manifest.getMainAttributes();

            if(MAIN != null) {
                final String ATTRIBUTE = MAIN.getValue("Class-Path");

                if(ATTRIBUTE != null && ATTRIBUTE.length() > 0) {
                    final String[] SPLIT = ATTRIBUTE.split(" ");

                    if(SPLIT.length > 0) {
                        for(int X = 0; X < SPLIT.length; X++) {
                            if(SPLIT[X].length() > 0) {
                                File Lib_File = new File(SPLIT[X].replace('/', FileUtil._S_c)).getAbsoluteFile();

                                if(!Lib_File.exists()) {
                                    Lib_File = new File(defaultlibdir, SPLIT[X].replace('/', FileUtil._S_c));
                                }

                                if(!Lib_File.exists()) {
                                    _LOG.printError("Depends File: " + Lib_File.getPath() + " Not Found");
                                } else {
                                    MyClassloader Loader = null;
                                    try {
                                        if(_Manager != null) {///////////
                                            final int INDEX = _Manager.isLoadedIndex(Lib_File.getName());

                                            if(INDEX > -1) {
                                                _LOG.printInformation("Allready Loaded: " + Lib_File.getPath());
                                                continue;
                                            }
                                        }///////////

                                        if(Lib_File.isFile()) {
                                            if(FileUtil.isFileType(Lib_File, JarClassLoader._JAR_MAGIC_NUMBER_)) {
                                                Loader = new JarClassLoader(Lib_File, this, null);
                                            } else {
                                                _LOG.printWarning("Not A Zip/Jar File: " + Lib_File.getPath());
                                                continue;
                                            }
                                        } else {
                                            Loader = new DirectoryClassLoader(Lib_File, this, null);
                                        }
                                        
                                        _DEPENDS.put(Loader);

                                        if(_Manager != null) {///////////
                                            _Manager.add(Lib_File.getName(), Loader);
                                        }///////////

                                        _LOG.printInformation("Loaded Depend: " + Lib_File.getPath());
                                    } catch (Exception e) {
                                        _LOG.printError(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
