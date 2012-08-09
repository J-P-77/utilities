package beta.utillib.classloader.v2;

import java.io.Closeable;
import java.io.InputStream;

import java.net.URL;

import java.util.jar.Manifest;

/**
 *
 * @author Dalton Dell
 */
public interface IClassloaderListener extends Closeable {
	
	public String getName();
	
    /**
     * Search Local ClassLoader For Class
     * @param classname
     * @return If Class Does Not Exists return null
     */
    public InputStream findClass(String name);

    /**
     * Search Local ClassLoader For Resource
     * @param classname
     * @return If Resource Does Not Exists return null
     */
    public URL findResource(String name);

    /**
     * Search Local ClassLoader For Resource
     * @param classname
     * @return If Resource Stream Does Not Exists return null
     */
    public InputStream getResourceAsStream(String name);
    public boolean resourceExists(String name);

    public boolean isClosed();

    public Manifest getManifest();
}
