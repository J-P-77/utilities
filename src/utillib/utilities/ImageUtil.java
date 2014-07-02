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

package utillib.utilities;

import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.Toolkit;

import java.net.URL;

import javax.imageio.ImageIO;

import java.io.File;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 03, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * September 11, 2009 (Version 1.0.1)
 *     -Tried To Increase Performance
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class ImageUtil {
	public static ImageIcon loadImageIconFromSystemResource(String resource) {
		return loadImageIcon(ClassResourceUtil.getSystemResource(resource));
	}

	public static Image loadImageFromSystemResource(String resource) {
		return loadImage(ClassResourceUtil.getSystemResource(resource));
	}

	/**
	 * Use Method loadImageIconFromResource(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static ImageIcon loadImageIconFromResource(Class<?> aclass, String resource) {
		return loadImageIcon(ClassResourceUtil.getResource(aclass, resource));
	}

	public static ImageIcon loadImageIconFromResource(ClassLoader loader, String resource) {
		return loadImageIcon(ClassResourceUtil.getResource(loader, resource));
	}

	/**
	 * Use Method loadImageFromResource(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static Image loadImageFromResource(Class<?> aclass, String resource) {
		return loadImage(ClassResourceUtil.getResource(aclass, resource));
	}

	public static Image loadImageFromResource(String resource) {
		return loadImage(ClassResourceUtil.getResource(ClassLoader.getSystemClassLoader(), resource));
	}

	public static Image loadImageFromResource(ClassLoader loader, String resource) {
		return loadImage(ClassResourceUtil.getResource(loader, resource));
	}

	public static ImageIcon loadImageIconFromSystemResourceEx(String resource) throws Exception {
		return loadImageIconEx(ClassResourceUtil.getSystemResource(resource));
	}

	public static Image loadImageFromSystemResourceEx(String resource) throws Exception {
		return loadImageEx(ClassResourceUtil.getSystemResource(resource));
	}

	/**
	 * Use Method loadImageIconFromResourceEx(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static ImageIcon loadImageIconFromResourceEx(Class<?> aclass, String resource) throws Exception {
		return loadImageIconEx(ClassResourceUtil.getResource(aclass, resource));
	}

	public static ImageIcon loadImageIconFromResourceEx(String resource) throws Exception {
		return loadImageIconEx(ClassResourceUtil.getResource(ClassLoader.getSystemClassLoader(), resource));
	}

	public static ImageIcon loadImageIconFromResourceEx(ClassLoader loader, String resource) throws Exception {
		return loadImageIconEx(ClassResourceUtil.getResource(loader, resource));
	}

	/**
	 * Use Method loadImageFromResourceEx(ClassLoader, String) Instead
	 * 
	 * @param aclass
	 * @param resource
	 * @return
	 */
	@Deprecated
	public static Image loadImageFromResourceEx(Class<?> aclass, String resource) throws Exception {
		return loadImageEx(ClassResourceUtil.getResource(aclass, resource));
	}

	public static Image loadImageFromResourceEx(String resource) throws Exception {
		return loadImageEx(ClassResourceUtil.getResource(ClassLoader.getSystemClassLoader(), resource));
	}

	public static Image loadImageFromResourceEx(ClassLoader loader, String resource) throws Exception {
		return loadImageEx(ClassResourceUtil.getResource(loader, resource));
	}

	public static ImageIcon loadImageIcon(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		return new ImageIcon(url);
	}

	public static ImageIcon loadImageIcon(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return new ImageIcon(path);
	}

	public static ImageIcon loadImageIcon(File path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return new ImageIcon(path.getAbsolutePath());
	}

	/**
	 * 
	 * @param url
	 * @return Image Object, null If Image Not Found
	 */
	public static Image loadImage(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		final Image IMG = Toolkit.getDefaultToolkit().getImage(url);

		return (IMG == null ? loadImageIO(url) : IMG);
	}

	/**
	 * 
	 * @param url
	 * @return Image Object, null If Image Not Found
	 */
	public static Image loadImageIO(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		try {
			return ImageIO.read(url);
		} catch(Exception e) {}

		return null;
	}

	/**
	 * 
	 * @param file
	 * @return Image Object, null If Image Not Found
	 */
	public static Image loadImageIO(String file) {
		return loadImageIO(new File(file));
	}

	/**
	 * 
	 * @param file
	 * @return Image Object, null If Image Not Found
	 */
	public static Image loadImageIO(File file) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		try {
			return ImageIO.read(file);
		} catch(Exception e) {}

		return null;
	}

	/**
	 * 
	 * @param path
	 *            (can be a String, File, URL
	 * @return
	 */
	public static Image loadImageObj(Object path) {
		try {
			if(path instanceof String) {
				String Temp = (String)path;

				return ImageIO.read(new File(Temp));
			} else if(path instanceof URL) {
				URL Temp = (URL)path;

				return ImageIO.read(Temp);
			} else if(path instanceof File) {
				File Temp = (File)path;

				return ImageIO.read(Temp);
			}
		} catch(Exception e) {
			System.out.println("Variable[path] - Loading Image Failed: " + path.toString() + ", Cause: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static ImageIcon loadImageIconEx(URL url) throws Exception {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		ImageIcon Icon = null;
		try {
			Icon = new ImageIcon(url);
		} catch(Exception e) {
			throw new Exception("Variable[url] -Image Icon Load Failed: " + url.toString() + ", Cause: " + e.getMessage());
		}

		return Icon;
	}

	public static Image imageEx(String path) throws Exception {
		return getImageEx(new File(path));
	}

	public static Image getImageEx(File file) throws Exception {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		try {
			return Toolkit.getDefaultToolkit().getImage(file.getPath());
		} catch(Exception e1) {
			try {
				return loadImageEx(file);
			} catch(Exception e2) {
				throw new Exception("Variable[file] - Image Resource: " + file.getPath() + ", Cause: " + e1.getMessage() + "-" + e2.getMessage());
			}
		}
	}

	public static Image getImageEx(URL url) throws Exception {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		try {
			return Toolkit.getDefaultToolkit().getImage(url);
		} catch(Exception e1) {
			try {
				return loadImageEx(url);
			} catch(Exception e2) {
				throw new Exception("Variable[url] - Image Resource: " + url.toString() + ", Cause: " + e1.getMessage() + "-" + e2.getMessage());
			}
		}
	}

	public static Image loadImageEx(String file) throws Exception {
		return loadImageEx(new File(file));
	}

	public static Image loadImageEx(File file) throws Exception {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		return ImageIO.read(file);
	}

	public static Image loadImageEx(URL url) throws Exception {
		if(url == null) {
			throw new RuntimeException("Variable[url] - Is Null");
		}

		return ImageIO.read(url);
	}

	public static Image loadImageObjEx(Object path) throws Exception {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		if(path instanceof String) {
			return ImageIO.read(new File((String)path));
		} else if(path instanceof URL) {
			return ImageIO.read((URL)path);
		} else if(path instanceof File) {
			return ImageIO.read((File)path);
		}

		return null;
	}

/*
    public static void main(String[] args) {
        final ClassLoader CLOADER = ClassLoader.getSystemClassLoader();

        if(CLOADER != null) {
            if(CLOADER instanceof URLClassLoader) {
                final URLClassLoader URLCLOADER = (URLClassLoader)CLOADER;

                final URL U1 = URLCLOADER.findResource("utillib/resources/close exited.png");
                final URL U2 = ClassResource.getResource("/images/resources/close exited.png");

                System.out.println((U1 == null ? "U1 Is Null" : U1.toString()));
                System.out.println((U2 == null ? "U2 Is Null" : U2.toString()));
            }
        }
    }
*/
}
