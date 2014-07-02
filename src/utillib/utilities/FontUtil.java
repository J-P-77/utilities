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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * May 05, 2009 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FontUtil {
	private static final FontRenderContext _DEFAULT_FRC_ = new FontRenderContext(null, false, false);
	private static final GraphicsEnvironment _GE_ = GraphicsEnvironment.getLocalGraphicsEnvironment();

	public static FontMetrics getFontMetrics(Font font) {
//        font.getLineMetrics(chars, beginIndex, limit, DEFAULT_FRC);
		try {
			return Toolkit.getDefaultToolkit().getFontMetrics(font);
		} catch(Exception e1) {
			try {
				return sun.swing.SwingUtilities2.getFontMetrics(null, font);
			} catch(Exception e2) {
				try {
					return sun.font.FontDesignMetrics.getMetrics(font);
				} catch(Exception e3) {}
			}
		}

		return null;
	}

	public static int getStrWidth(Font font, String str) {
		return getStrWidth(font, _DEFAULT_FRC_, str);
	}

	public static int getStrWidth(Font font, FontRenderContext frc, String str) {
		try {
			return (int)font.getStringBounds(str, 0, str.length(), frc).getWidth();
		} catch(Exception e) {}

		return -1;
	}

	public static int getCharWidth(Font font, char c) {
		return getCharWidth(font, _DEFAULT_FRC_, c);
	}

	public static int getCharWidth(Font font, FontRenderContext frc, char c) {
		return getCharWidth(font, frc, new char[] {c}, 0, 1);
	}

	public static int getCharWidth(Font font, char[] chars) {
		return getCharWidth(font, _DEFAULT_FRC_, chars, 0, chars.length);
	}

	public static int getCharWidth(Font font, char[] chars, int offset, int length) {
		return getCharWidth(font, _DEFAULT_FRC_, chars, offset, length);
	}

	public static int getCharWidth(Font font, FontRenderContext frc, char[] chars, int offset, int length) {
		try {
			return (int)font.getStringBounds(chars, offset, length, frc).getWidth();
		} catch(Exception e) {}

		return -1;
	}

	public static Font[] getAllFonts() {
		return _GE_.getAllFonts();
	}

	public static String[] getFontFamilyNames() {
		return _GE_.getAvailableFontFamilyNames();
	}

/*
    public static void main(String[] args) {
        GraphicsEnvironment Ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice Gd = Ge.getDefaultScreenDevice();
        GraphicsConfiguration Gc = Gd.getDefaultConfiguration();
        
        final Font TextFont1 = new Font("Dialog", Font.PLAIN, 12);//Font
        char Char = 'W';
        //TextFont1.getTransform();

        StyleContext Sc = StyleContext.getDefaultStyleContext();

        FontMetrics Fm1 = sun.font.FontDesignMetrics.getMetrics(TextFont1);
        FontMetrics Fm2 = Sc.getFontMetrics(TextFont1);
        FontMetrics Fm3 = sun.swing.SwingUtilities2.getFontMetrics(null, TextFont1);
        
        System.out.println("Fm1= " + Fm1.charWidth(Char));
        System.out.println("Fm2= " + Fm2.charWidth(Char));
        System.out.println("Fm3= " + Fm3.charWidth(Char));        
        System.out.println("Fm4= " + getCharWidth(TextFont1, Char));
    }
*/
}
/*
    private static final StyleContext STYLE_CONTEXT;
    
    static {
        StyleContext Sc = null;
        try {
            Sc = StyleContext.getDefaultStyleContext();
        } catch (Exception e) {
            Sc = null;
            System.out.println("Class[FontUtil] - Method[static] - Variable[STYLE_CONTEXT] - Cause: " + e.getMessage());
        }
        
        STYLE_CONTEXT = Sc;
    }
*/