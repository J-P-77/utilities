/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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
