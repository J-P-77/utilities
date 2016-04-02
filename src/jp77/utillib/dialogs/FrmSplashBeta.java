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

package jp77.utillib.dialogs;

// import jp77.utillib.arrays.ResizingArray;

import jp77.utillib.utilities.ImageUtil;
import jp77.utillib.utilities.PositionWindow;
import jp77.utillib.utilities.ColorUtil;

import jp77.utillib.interfaces.IProgress;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import javax.imageio.ImageIO;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FrmSplashBeta extends JWindow implements WindowConstants {
	public static final String _DEFAULT_BACKGROUND_RESOURCE_ = "/resources/images/blank.jpg";
	public static final int _BASE_HEIGHT_ = 236;
	public static final int _BASE_WIDTH_ = 400;

	private Image _Background_Image = null;
	private Image _Image = null;

	private static final ImageIcon _CLOSE_EXITED_ = new ImageIcon(ImageUtil.loadImageFromSystemResource("/resources/images/close exited.png"));
	private static final ImageIcon _CLOSE_ENTERED_ = new ImageIcon(ImageUtil.loadImageFromSystemResource("/resources/images/close entered.png"));

//    private ResizingArray<Runnable> _Runnables = new ResizingArray<Runnable>(0);

	private SplashComponent _Painter = null;

	private int _CloseAction = DO_NOTHING_ON_CLOSE;

	/**
     *
     */
	public FrmSplashBeta() {
		this(ImageUtil.loadImageFromSystemResource(_DEFAULT_BACKGROUND_RESOURCE_));
	}

	/**
	 * 
	 * @param max
	 */
	public FrmSplashBeta(Image background) {
		this(background, null);
	}

	/**
	 * 
	 * @param max
	 * @param background
	 * @param image
	 */
	public FrmSplashBeta(Image background, Image image) {
//        if(background == null) {
//            throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[background] - Is Null");
//        }

		initComponents();

		setBackgroundImage(background);

		if(image != null) {
			setImage(image);
		}
	}

	private void initComponents() {
		_Painter = new SplashComponent(this);

		super.setSize(_BASE_WIDTH_, _BASE_HEIGHT_);
		super.getContentPane().add(_Painter);

		PositionWindow.quickToCenter(this);
	}

//    public void runOnApplicationExit(Runnable runnable) {
//        _Runnables.put(runnable);
//    }

	public void setCloseAction(int closeaction) {
		if(closeaction >= 0 && closeaction <= 3) {
			_CloseAction = closeaction;
		} else {
			throw new RuntimeException("Variable[closeaction] - Choose: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
		}
	}

	public void setBackgroundImage(Image image) {
		_Background_Image = image;
		super.repaint();
	}

	public void setBackgroundImage(String path) {
		setBackGroundImage(new File(path));
	}

	public void setBackGroundImage(File path) {
		if(path == null) {
			throw new RuntimeException("Variable[filepath] - Is Null");
		} else if(!path.exists()) {
			throw new RuntimeException("Variable[filepath] - File: " + path.getPath() + " - Does Not Exists");
		}

		try {
			BufferedImage TempImage = ImageIO.read(path);
			_Image = TempImage.getScaledInstance(_BASE_WIDTH_, _BASE_HEIGHT_, Image.SCALE_DEFAULT);
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setBackGroundImage(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		try {
			BufferedImage TempImage = ImageIO.read(url);
			_Image = TempImage.getScaledInstance(_BASE_WIDTH_, _BASE_HEIGHT_, Image.SCALE_DEFAULT);
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setImage(Image image) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

		_Image = image;
		super.repaint();
	}

	public void setImage(String path) {
		setImage(new File(path));
	}

	public void setImage(File path) {
		if(path == null) {
			throw new RuntimeException("Variable[filepath] - Is Null");
		} else if(!path.exists()) {
			throw new RuntimeException("Variable[filepath] - File: " + path.getPath() + " - Does Not Exists");
		}

		try {
			BufferedImage TempImage = ImageIO.read(path);
			_Image = null;
			_Image = TempImage;
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setImage(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		try {
			BufferedImage TempImage = ImageIO.read(url);
			_Image = null;
			_Image = TempImage;
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setBackgroundColor(String color) {
		final Color COLOR = ColorUtil.convertStrToColor(color);

		if(COLOR != null) {
			super.getContentPane().setBackground(COLOR);
		}
	}

	public void setText(String text) {
		_Painter.getTotal().setText(text);
	}

	public String getText() {
		return _Painter.getTotal().getText();
	}

	public SplashComponent.Progress getCurrent() {
		return _Painter.getCurrent();
	}

	public SplashComponent.Progress getTotal() {
		return _Painter.getTotal();
	}

	public void reset() {
		PositionWindow.quickToCenter(this);

		_Image = null;
	}

	public SplashComponent getSplashComponent() {
		return _Painter;
	}

	//CLASSES
	public class SplashComponent extends JComponent {
		private final Window _WINDOW;

		private final Progress _TOTAL;
		private final Progress _CURRENT;

		public SplashComponent(Window window) {
			initComponents();

			_WINDOW = window;

			_TOTAL = new TotalProgress(progressTotal);
			_CURRENT = new CurrentProgress(progressCurrent);
		}

		private void initComponents() {
			super.setLayout(null);

			progressCurrent = new JProgressBar();
//XXX (Possible Internal Bug - When JProgressBar Height Is Set To 10 [setBounds(22, 196, 316, 10)]
			progressCurrent.setBounds(22, _BASE_HEIGHT_ - 40, 316, 12);
			super.add(progressCurrent);

			progressTotal = new JProgressBar();
			progressTotal.setBounds(22, _BASE_HEIGHT_ - 40 + 10 + 2, 356 - 40, 12);
			super.add(progressTotal);

			lblPercent = new JLabel("0%");
			lblPercent.setForeground(Color.WHITE);
			lblPercent.setHorizontalAlignment(JLabel.CENTER);
			lblPercent.setVerticalAlignment(JLabel.CENTER);
			lblPercent.setBounds(356 - 20 + 2, _BASE_HEIGHT_ - 40, 40, 20);//40
			super.add(lblPercent);

			lblInfo = new JLabel("Loading...");
			lblInfo.setForeground(Color.WHITE);
			lblInfo.setBounds(22, _BASE_HEIGHT_ - 60, 356, 20);//40
			super.add(lblInfo);

			lblClose = new JLabel(_CLOSE_EXITED_);
			lblClose.setBounds(_BASE_WIDTH_ - 36, 14, 20, 20);
			lblClose.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					switch(_CloseAction) {
						case DISPOSE_ON_CLOSE:
							_WINDOW.dispose();
							break;

						case HIDE_ON_CLOSE:
							_WINDOW.setVisible(false);
							break;

						case EXIT_ON_CLOSE:
							System.exit(0);
							break;

						case DO_NOTHING_ON_CLOSE:
						default:
							break;
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {
					lblClose.setIcon(_CLOSE_ENTERED_);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					lblClose.setIcon(_CLOSE_EXITED_);
				}
			});
			super.add(lblClose);
		}

		@Override
		public void paint(Graphics g) {
			if(_Background_Image != null) {
				g.drawImage(_Background_Image, 0, 0, this);
			}

			if(_Image != null) {
				g.drawImage(_Image, 0, 0, this);
			}

			super.paintChildren(g);
		}

		public void setTextColor(Color color) {
			lblInfo.setForeground(color);
			lblPercent.setForeground(color);
		}

		public Progress getTotal() {
			return _TOTAL;
		}

		public Progress getCurrent() {
			return _CURRENT;
		}

		private void updatePercent() {
			lblPercent.setText((long)(((double)progressTotal.getValue() / progressTotal.getMaximum()) * 100) + "%");
		}

		private JProgressBar progressCurrent;
		private JProgressBar progressTotal;

		private JLabel lblClose;
		private JLabel lblInfo;
		private JLabel lblPercent;

		//CLASSES
		public class TotalProgress extends Progress {
			public TotalProgress(JProgressBar progressbar) {
				super(progressbar);
			}

			@Override
			public void increment(int value) {
				_PROGRESS_BAR.setValue(_PROGRESS_BAR.getValue() + value);

				updatePercent();
			}

			@Override
			public void setValue(int value) {
				_PROGRESS_BAR.setValue(value);

				updatePercent();
			}

			@Override
			public void setMinimum(int value) {
				_PROGRESS_BAR.setMinimum(value);

				updatePercent();
			}

			@Override
			public void setMaximum(int value) {
				_PROGRESS_BAR.setMaximum(value);

				updatePercent();
			}
		}

		public class CurrentProgress extends Progress {
			public CurrentProgress(JProgressBar progressbar) {
				super(progressbar);
			}

			@Override
			public void increment(int value) {
				_PROGRESS_BAR.setValue(_PROGRESS_BAR.getValue() + value);
			}

			@Override
			public void setValue(int value) {
				_PROGRESS_BAR.setValue(value);
			}

			@Override
			public void setMinimum(int value) {
				_PROGRESS_BAR.setMinimum(value);
			}

			@Override
			public void setMaximum(int value) {
				_PROGRESS_BAR.setMaximum(value);
			}
		}

		public abstract class Progress implements IProgress {
			protected final JProgressBar _PROGRESS_BAR;

			public Progress(JProgressBar progressbar) {
				if(progressbar == null) {
					throw new RuntimeException("Variable[progressbar] - Is Null");
				}

				_PROGRESS_BAR = progressbar;
			}

			@Override
			public void increment() {
				increment(1);
			}

			@Override
			public int getValue() {
				return _PROGRESS_BAR.getValue();
			}

			@Override
			public int getMaximum() {
				return _PROGRESS_BAR.getMaximum();
			}

			@Override
			public int getMinimum() {
				return _PROGRESS_BAR.getMinimum();
			}

			@Override
			public void setText(String text) {
				lblInfo.setText(text);
			}

			@Override
			public String getText() {
				return lblInfo.getText();
			}

			public void setBarBackgroundColor(Color color) {
				_PROGRESS_BAR.setOpaque(true);
				_PROGRESS_BAR.setBackground(color);
			}

			public void setBarColor(Color color) {
				_PROGRESS_BAR.setForeground(color);
			}
		}
	}
/*
	public static void main(String[] args) {
        final int Max1 = 10;
        final int Max2 = 20;

        final String LCLAF = utillib.utilities.LookAndFeels._WINDOWS_L_AND_F_;
        try {
            if(LCLAF.equals(utillib.utilities.LookAndFeels._WINDOWS_L_AND_F_)) {
                javax.swing.UIManager.setLookAndFeel(utillib.utilities.LookAndFeels._WINDOWS_LOOK_AND_FEEL_);
            } else if(LCLAF.equals(utillib.utilities.LookAndFeels._WINDOWSCLASSIC_L_A_F_)) {
                javax.swing.UIManager.setLookAndFeel(utillib.utilities.LookAndFeels._WINDOWSCLASSIC_LOOK_AND_FEEL_);
            } else if(LCLAF.equals(utillib.utilities.LookAndFeels._METAL_L_AND_F_)) {
                javax.swing.UIManager.setLookAndFeel(utillib.utilities.LookAndFeels._METAL_LOOK_AND_FEEL_);
            } else if(LCLAF.equals(utillib.utilities.LookAndFeels._MOTIF_L_A_F_)) {
                javax.swing.UIManager.setLookAndFeel(utillib.utilities.LookAndFeels._MOTIF_LOOK_AND_FEEL_);
            } else if(LCLAF.equals(utillib.utilities.LookAndFeels._NIMBUS_L_AND_F_)) {
                javax.swing.UIManager.setLookAndFeel(utillib.utilities.LookAndFeels._NIMBUS_LOOK_AND_FEEL_);
            } else {
                System.out.println("No Look And Feel");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FrmSplashBeta Splash = new FrmSplashBeta();

        new PositionWindow(Splash).toLowerLeft();

        Splash.setCloseAction(EXIT_ON_CLOSE);

        Splash.setText("Hello Me");
//        Splash.getTotal().setBarColor(Color.RED);
//        Splash.getCurrent().setBarColor(Color.YELLOW);

        Splash.getTotal().setBarBackgroundColor(Color.YELLOW);
        Splash.getCurrent().setBarBackgroundColor(Color.YELLOW);

        Splash.getSplashComponent().setTextColor(Color.WHITE);
        Splash.setVisible(true);

        Splash.getTotal().setMinimum(0);
        Splash.getTotal().setMaximum(Max1);

        Splash.getCurrent().setMinimum(0);
        for(int X = 0; X < Max1; X++) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {}

            Splash.getCurrent().setValue(0);
            Splash.getCurrent().setMaximum(Max2);
            for(int Y = 0; Y < Max2; Y++) {
                Splash.getCurrent().increment();
                try {
                    Thread.sleep(20);
                } catch (Exception e) {}
            }

            Splash.getTotal().increment();
        }

        try {
            Thread.sleep(50);
        } catch (Exception e) {}
        Splash.dispose();
	}
*/
}
