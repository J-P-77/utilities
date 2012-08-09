package beta.utillib.pluginmanager.v1;

import java.io.File;

/**
 *
 * @author Dalton Dell
 */
public interface IPluginInfo {
    /**
     * Jar File Path (Can Be Null)
     */
    public File getFile();

    /**
     * Jar File Name (Can Be Null)
     */
    public String getJarFileName();

    /**
     * Plugin Name
     */
    public String getPluginName();

    /**
     * Instance Of Plugin
     */
    public Object getPlugin();

    /**
     * Plugin Class
     */
    public Class<?> getPluginClass();
    
//    /**
//     * Creates New Instances Of The Plugin
//     */
//    public Object newInstance(Parameter... parameters);
    
    /**
     * Classloader for the Plugin (Can Be Null)
     */
    public ClassLoader getClassLoader();

    /**
     * Plugin Class Name - Period Formatted! - Ex. javax.swing.JButton
     */
    public String getPluginClassName();

    /**
     * Is Plugin Enable or Disabled (Used For Internal Use Only)
     */
    public boolean isEnabled();
}
