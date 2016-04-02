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

import jp77.utillib.arrays.ResizingArray;

import jp77.utillib.utilities.ImageUtil;
import jp77.utillib.utilities.PositionWindow;
import jp77.utillib.utilities.ColorUtil;
import jp77.utillib.utilities.ThreadUtil;

import jp77.utillib.interfaces.IProgress;
import jp77.utillib.interfaces.IText;

import java.awt.Color;

import java.awt.Font;
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
 * <b>Current Version 1.0.1</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 02, 2009 (version 1.0.1)
 *     -Updated
 *         -EveryThing
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
@Deprecated
public class FrmSplash extends JWindow implements IProgress, IText, WindowConstants {
	public static final String _DEFAULT_BACKGROUND_RESOURCE_ = "/resources/images/resources/blank.jpg";
	public static final int _BASE_HEIGHT_ = 236;
	public static final int _BASE_WIDTH_ = 400;

	private Image _Background_Image = null;
	private Image _Image = null;

	private static final ImageIcon _CLOSE_EXITED_ = new ImageIcon(ImageUtil.loadImageFromResource(FrmSplash.class, "/resources/images/resources/close exited.png"));
	private static final ImageIcon _CLOSE_ENTERED_ = new ImageIcon(ImageUtil.loadImageFromResource(FrmSplash.class, "/resources/images/resources/close entered.png"));

	private ResizingArray<Runnable> _Runnables = new ResizingArray<Runnable>(1);

	private SplashComponent _Painter = null;
	private IProgress _Progress = null;

	private int _CloseAction = DO_NOTHING_ON_CLOSE;

	/**
     *
     */
	public FrmSplash() {
		this(1, ImageUtil.loadImageFromResource(FrmSplash.class, _DEFAULT_BACKGROUND_RESOURCE_), null);
	}

	/**
	 * 
	 * @param max
	 */
	public FrmSplash(int max) {
		this(max, ImageUtil.loadImageFromResource(FrmSplash.class, _DEFAULT_BACKGROUND_RESOURCE_), null);
	}

	/**
	 * 
	 * @param max
	 */
	public FrmSplash(Image background) {
		this(1, background, null);
	}

	/**
	 * 
	 * @param max
	 */
	public FrmSplash(Image background, Image image) {
		this(1, background, image);
	}

	/**
	 * 
	 * @param max
	 * @param background
	 * @param image
	 */
	public FrmSplash(int max, Image background, Image image) {
		if(background == null) {
			throw new RuntimeException("Variable[background] - Is Null");
		}

		initComponents();

		setMaximum(max);
		setBackgroundImage(background);

		if(image != null) {
			setImage(image);
		}
	}

	private void initComponents() {
		_Painter = new SplashComponent(this);
		_Progress = _Painter;

		super.setSize(_BASE_WIDTH_, _BASE_HEIGHT_);
		super.getContentPane().add(_Painter);

		PositionWindow.quickToCenter(this);
	}

	public void runOnApplicationExit(Runnable runnable) {
		_Runnables.put(runnable);
	}

	public void setCloseAction(int closeaction) {
		if(closeaction >= 0 && closeaction <= 3) {
			_CloseAction = closeaction;
		} else {
			throw new RuntimeException("Variable[closeaction] - Choose: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
		}
	}

// - 100.0%
	@Override
	public void setText(String value) {
		_Painter.setText(value);
	}

	@Override
	public String getText() {
		return _Painter.getText();
	}

	public void setBackgroundImage(String path) {
		setBackGroundImage(new File(path));
	}

	public void setBackgroundImage(Image image) {
		_Background_Image = image;
		super.repaint();
	}

	public void setBackGroundImage(File path) {
		if(path == null) {
			throw new RuntimeException("Variable[filepath] - Is Null");
		} else if(!path.exists()) {
			throw new RuntimeException("Variable[filepath] - File: " + path.getPath() + " - Does Not Exists");
		}

		try {
			BufferedImage Temp = ImageIO.read(path);
			_Image = Temp.getScaledInstance(_BASE_WIDTH_, _BASE_HEIGHT_, Image.SCALE_DEFAULT);
			Temp = null;
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setBackGroundImage(URL path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		try {
			BufferedImage TempImage = ImageIO.read(path);
			_Image = TempImage.getScaledInstance(_BASE_WIDTH_, _BASE_HEIGHT_, Image.SCALE_DEFAULT);
			TempImage = null;
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setImage(Image image) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

		_Image = image.getScaledInstance(_BASE_WIDTH_, _BASE_HEIGHT_, Image.SCALE_DEFAULT);
	}

	public void setBackgroundColor(String color) {
		Color C = ColorUtil.convertStrToColor(color);

		if(C != null) {
			super.getContentPane().setBackground(C);
		}
	}

	@Override
	public void setValue(int value) {
		if(value < 0) {//ERROR
			throw new RuntimeException("Variable[value] - Invalid Value Number: " + value);
		}

		_Progress.setValue(value);
	}

	@Override
	public void increment() {
		_Progress.increment();
	}

	@Override
	public void increment(int value) {
		_Progress.increment(value);
	}

/*
	public void decrement() {
		decrement(1);
	}

	public void decrement(int value) {
        _Value -= value;

        this.repaint();
	}
*/
	public void stepAuto(int delaymillis) {
		increment(1);

		pause(delaymillis);
	}

	@Override
	public int getValue() {
		return _Progress.getValue();
	}

	public void stepAuto(int delaymillis, int value) {
		increment(value);

		pause(delaymillis);
	}

	private void pause(int millis) {
		if(millis > 0) {
			ThreadUtil.sleep(millis);
		}
	}

	@Override
	public void setMinimum(int value) {
		_Progress.setMinimum(value);
	}

	@Override
	public int getMinimum() {
		return _Progress.getMinimum();
	}

	@Override
	public void setMaximum(int value) {
		_Progress.setMaximum(value);
	}

	@Override
	public int getMaximum() {
		return _Progress.getMaximum();
	}

	public static URL getClassResource(Class aclass, String resource) {
		URL Url = aclass.getResource(resource);

		if(Url == null) {
			throw new NullPointerException("Resource: " + resource + " Does Not Exist");
		} else {
			return Url;
		}
	}

	public void reset() {
		PositionWindow PosFrm = new PositionWindow(this);
		PosFrm.toCenter();

		_Image = null;
	}

	public SplashComponent getSplashComponent() {
		return _Painter;
	}

	//CLASSES
	public class SplashComponent extends JComponent implements IProgress, IText {
		private final Window _WINDOW;

		public SplashComponent(Window window) {
			initComponents();

			_WINDOW = window;
		}

		private void initComponents() {
			super.setLayout(null);

			progressBar = new JProgressBar();
			progressBar.setBorderPainted(false);
			progressBar.setBackground(Color.WHITE);
			progressBar.setForeground(Color.BLUE);
			progressBar.setFont(new Font("", Font.PLAIN, 14));
			progressBar.setBorderPainted(false);
			progressBar.setBounds(22, _BASE_HEIGHT_ - 40, 356 - 40, 20);//40
			super.add(progressBar);

			lblPercent = new JLabel("0%");
			lblPercent.setForeground(Color.WHITE);
			//lblPercent.setBackground(Color.BLACK);
			//lblPercent.setOpaque(true);
			lblPercent.setHorizontalAlignment(JLabel.CENTER);
			lblPercent.setVerticalAlignment(JLabel.CENTER);
			lblPercent.setBounds(356 - 20 + 2, _BASE_HEIGHT_ - 40, 40, 20);//40
			super.add(lblPercent);

			lblInfo = new JLabel("Loading...");
			lblInfo.setForeground(Color.WHITE);
			//lblInfo.setBackground(Color.BLACK);
			//lblInfo.setOpaque(true);
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
		public void increment() {
			increment(1);
		}

		@Override
		public void increment(int value) {
			progressBar.setValue(progressBar.getValue() + value);

			updatePercent();
		}

		@Override
		public void setValue(int value) {
			progressBar.setValue(value);

			updatePercent();
		}

		@Override
		public int getValue() {
			return progressBar.getValue();
		}

		@Override
		public void setMaximum(int value) {
			progressBar.setMaximum(value);

			updatePercent();
		}

		@Override
		public int getMaximum() {
			return progressBar.getMaximum();
		}

		@Override
		public void setMinimum(int value) {
			progressBar.setMinimum(value);

			updatePercent();
		}

		@Override
		public int getMinimum() {
			return progressBar.getMinimum();
		}

		public void setTextColor(Color color) {
			lblInfo.setForeground(color);
			lblPercent.setForeground(color);
		}

		@Override
		public void setText(String text) {
			lblInfo.setText(text);
		}

		@Override
		public String getText() {
			return progressBar.getString();
		}

		public void setBarBackgroundColor(Color color) {
			progressBar.setBackground(color);
		}

		public void setBarColor(Color color) {
			progressBar.setForeground(color);
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(_Background_Image, 0, 0, this);

			if(_Image != null) {
				g.drawImage(_Image, 0, 0, this);
			}

			super.paintChildren(g);
		}

		private void updatePercent() {
			lblPercent.setText((long)(((double)progressBar.getValue() / progressBar.getMaximum()) * 100) + "%");
		}

		private JProgressBar progressBar;
		private JLabel lblClose;
		private JLabel lblInfo;
		private JLabel lblPercent;
	}

	//STATIC
	private static FrmSplash _Instance = null;

	public static FrmSplash getInstance() {
		if(_Instance == null) {
			_Instance = new FrmSplash();
		} else if(!_Instance.isDisplayable()) {
			_Instance.dispose();
			_Instance = null;
			_Instance = new FrmSplash();
		}

		return _Instance;
	}

	public static void showInstance() {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, 1, getClassResource(FrmSplash.class, _DEFAULT_BACKGROUND_RESOURCE_));
	}

	public static void showInstance(int max) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, max, getClassResource(FrmSplash.class, _DEFAULT_BACKGROUND_RESOURCE_));
	}

	public static void showInstance(int max, String imagefilepath) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, max, imagefilepath);
	}

	public static void showInstance(int width, int height, int max, String imagefilepath) {
		showInstance(width, height, max, new File(imagefilepath));
	}

	public static void showInstance(int max, File imagefile) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, max, imagefile);
	}

	public static void showInstance(int width, int height, int max, File imagefile) {
		FrmSplash Instance = getInstance();

		Instance.reset();
		Instance.setSize(width, height);
		Instance.setMaximum(max);
		Instance.setBackGroundImage(imagefile);
		Instance.setVisible(true);
	}

	public static void showInstance(int max, URL imagepath) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, max, imagepath);
	}

	public static void showInstance(int width, int height, int max, URL imagepath) {
		FrmSplash Instance = getInstance();

		Instance.reset();
		Instance.setSize(width, height);

		if(max > 0) {
			Instance.setMaximum(max);
		} else {
			Instance.setMaximum(1);
		}

		Instance.setBackGroundImage(imagepath);
		Instance.setVisible(true);
	}

	public static void showInstance(int max, Image image) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, 1, image);
	}

	public static void showInstance(Image image) {
		showInstance(_BASE_WIDTH_, _BASE_HEIGHT_, 1, image);
	}

	public static void showInstance(int width, int height, int max, Image image) {
		FrmSplash Instance = getInstance();

		Instance.reset();
		Instance.setSize(width, height);

		if(max > 0) {
			Instance.setMaximum(max);
		} else {
			Instance.setMaximum(1);
		}

		Instance.setImage(image);
		Instance.setVisible(true);
	}

	public static void hideInstance() {
		if(_Instance.isDisplayable() && _Instance.isVisible()) {
			_Instance.setVisible(false);
		}
	}

	public static void showText(String str) {
		FrmSplash Instance = getInstance();

		Instance.setText(str);
	}

	public static void stepProgress() {
		FrmSplash Instance = getInstance();

		Instance.increment();

		if(Instance.getValue() > Instance.getMaximum()) {
			Instance.setVisible(false);
		}
	}

	public static void stepProgress(int value) {
		FrmSplash Instance = getInstance();

		Instance.increment(value);

		if(Instance.getValue() > Instance.getMaximum()) {
			Instance.setVisible(false);
		}
	}

	public static void destroyInstance() {
		if(_Instance != null) {
			_Instance.setVisible(false);
			_Instance.dispose();
			_Instance = null;
		}
	}
/*
	public static void main(String[] args) {
        final int Max = 100;

        FrmSplash Splash = new FrmSplash(Max);

        new PositionWindow(Splash).toLowerLeft();

        Splash.setCloseAction(EXIT_ON_CLOSE);
        Splash.setText("Hello Me");
        Splash.getSplashComponent().setBarColor(Color.RED);
        Splash.getSplashComponent().setBarBackgroundColor(Color.BLUE);
        Splash.setVisible(true);
        for(int X = 0; X < Max; X++) {

//            Splash.setText(Integer.toString(X));

            Splash.increment();

            try {
                Thread.sleep(50);
            } catch (Exception e) {}
        }

        try {
            Thread.sleep(50);
        } catch (Exception e) {}
        Splash.dispose();

        FrmSplash.showInstance(Max);

//        for(int X = 0; X < Max; X++) {
//            int Red = utillib.utilities.RandomUtil.randomInt(0, 255);
//            int Green = utillib.utilities.RandomUtil.randomInt(0, 255);
//            int Blue = utillib.utilities.RandomUtil.randomInt(0, 255);
//            getInstance().setProgressColor(Red, Green, Blue);
//
//            FrmSplash.stepProgress();
//
//            StringBuffer Buffer = new StringBuffer();
//            int Len = utillib.utilities.RandomUtil.randomInt(20, 20);
//
//            int Letter = 65;
//            for(int Y = 0; Y < Len; Y++) {
//                boolean Coin = utillib.utilities.RandomUtil.random_50_50();
//
//                if(Coin) {
//                    Letter = utillib.utilities.RandomUtil.randomInt(65, 90);
//                } else {
//                    Letter = utillib.utilities.RandomUtil.randomInt(65 + 32, 90 + 32);
//                }
//                Buffer.append((char)Letter);
//            }
//
//            FrmSplash.showText(Buffer.toString());
//
//            try {
//                Thread.sleep(100);
//            } catch (Exception e) {}
//        }
//
//        try {
//            FrmSplash.stepProgress();
//            Thread.sleep(1000 * 1);
//        } catch (Exception e) {}
//
//        FrmSplash.destroyInstance();

//		FrmSplash Frm = new FrmSplash(1, getClassResource(FrmSplash.class, "/images/resources/splashjpg.jpg"));
//
//        Frm.setMaximum(9);
//        Frm.setText(DEFAULT_IMAGE);
//        Frm.setVisible(true);
//
//        for(int X = Frm.getMinimum(); X < Frm.getMaximum(); X++) {
//            Frm.step();
//
//            StringBuffer Buffer = new StringBuffer();
//            int Len = utillib.utilities.RandomUtil.randomInt(10, 20);
//
//            int Letter = 65;
//            for(int Y = 0; Y < Len; Y++) {
//                boolean Coin = utillib.utilities.RandomUtil.random_50_50();
//
//                if(Coin) {
//                    Letter = utillib.utilities.RandomUtil.randomInt(65, 90);
//                    Buffer.append((char)Letter);
//                } else {
//                    Letter = utillib.utilities.RandomUtil.randomInt(65 + 32, 90 + 32);
//                    Buffer.append((char)Letter);
//                }
//            }
//
//            Frm.setText(Buffer.toString());
//
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {}
//        }
//
//        try {
//            Frm.step();
//            Thread.sleep(1000);
//        } catch (Exception e) {}
//        Frm.dispose();

	}
*/
}

/*
    private class Painter extends JComponent implements IProgress {
        private int _Max = 0;
        private int _Value = 0;

        private MyStringBuffer _Buffer = new MyStringBuffer();

        public void setText(String text) {
            _Buffer.reset();
            _Buffer.append(text, 9);
            _Buffer.storeLength();
        }

        public String getText() {
            return _Buffer.toString();
        }

        public void increment() {increment(1);}

        public void increment(int value) {
            _Value += value;

            this.repaint();
        }

        public void setValue(int value) {_Value = value;}
        public int getValue() {return _Value;}
        public void setMaximum(int value) {_Max = value;}
        public int getMaximum() {return _Max;}
        public void setMinimum(int value) {}
        public int getMinimum() {return 0;}

        @Override
        public void update(Graphics g) {
            paint(g);
        }

        @Override
        public void paint(Graphics g) {
            Color TempColor = g.getColor();
            Font TempFont = g.getFont();

            double Percent = (_Value > 0 ? ((double)_Value / _Max) : 0.00);

            final int PROGRESSBARWIDTH = 355;

            if(_Image != null) {
                g.drawImage(_Image, 0, 0, this);
            }

            if(_Value > 0) {
                g.setColor(_ProgressColor);

                double FillPercent = PROGRESSBARWIDTH * Percent;

                g.fillRect(22, DEFAULT_HEIGHT - 40, (int)FillPercent, 20);
            }

            if(_Show_Percent) {
                _Buffer.append(" - ");
                _Buffer.append((int)(Percent * 100));
                _Buffer.append('%');
            }

            g.setColor(_TextColor);

            g.setFont(new Font("", Font.PLAIN, 14));
            String TempString = _Buffer.toString();

            int StrOffset = g.getFontMetrics().stringWidth(TempString) / 2;

            g.drawString(TempString, (PROGRESSBARWIDTH / 2) + 22 - StrOffset, DEFAULT_HEIGHT - 40 + 15);

            _Buffer.reset(_Buffer.getStoredLength());

            g.setFont(TempFont);
            g.setColor(TempColor);
        }
    }

    private class Painter2 extends JComponent implements IProgress, MouseListener, MouseMotionListener {
        private int _Max = 0;
        private int _Value = 0;

        private int _ProgressBarX = 22;
        private int _ProgressBarY = DEFAULT_HEIGHT - 40;
        private int _ProgressBarWidth = 355;//final int PROGRESSBARWIDTH = 355;
        private int _ProgressBarHeight = 20;

        private MyStringBuffer _Buffer = new MyStringBuffer();

        private boolean _HitX = false;
        private boolean _HitY = false;
        private boolean _IsPainting = false;

        public Painter2() {
            super.addMouseListener(this);
            super.addMouseMotionListener(this);
        }

        public void setText(String text) {
            _Buffer.reset();
            _Buffer.append(text, 9);
            _Buffer.storeLength();
        }

        public String getText() {
            return _Buffer.toString();
        }

        public void increment() {
            increment(1);
        }

        public void increment(int value) {
            _Value += value;

            if(!_IsPainting) {
                this.repaint();
            }
        }

        public void setValue(int value) {
            _Value = value;

            if(!_IsPainting) {
                this.repaint();
            }
        }

        public int getValue() {return _Value;}

        public void setMaximum(int value) {
            _Max = value;

            if(!_IsPainting) {
                this.repaint();
            }
        }

        public int getMaximum() {return _Max;}

        public void setMinimum(int value) {this.repaint();}

        public int getMinimum() {return 0;}

        public void mouseClicked(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            boolean HitX = false;
            boolean HitY = false;
            if(e.getX() > (DEFAULT_WIDTH - 36) && e.getX() < (DEFAULT_WIDTH - 36 + 20)) {
                HitX = true;
            }

            if(e.getY() > 14 && e.getY() < (14 + 20)) {
                HitY = true;
            }

            if((HitX && HitY) || (_HitX && _HitY)) {
                exitApplication();
            }
        }

        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}

        public void mouseDragged(MouseEvent e) {}

        public void mouseMoved(MouseEvent e) {
//            if(e.getX() > (DEFAULT_WIDTH - 36) && e.getX() < (DEFAULT_WIDTH - 16)) {
//                _HitX = true;
//
//                if(_HitX) {
//                    if(e.getY() > 14 && e.getY() < 34) {//(14 + 20)
//                        _HitY = true;
//                    } else {
//                        _HitY = false;
//                    }
//                }
//            } else {
//                _HitX = false;
//            }
//
//            if(_HitX && _HitY) {
//                _Close = ImageUtil.getImageFromResource(FrmSplash.class, "/images/resources/close entered.png");
//            } else {
//                _Close = ImageUtil.getImageFromResource(FrmSplash.class, "/images/resources/close exited.png");
//            }
//
//            if(!_IsPainting) {
//                this.repaint();
//            }
        }

        @Override
        public void update(Graphics g) {
            paint(g);
        }

        @Override
        public void paint(Graphics g) {
            _IsPainting = true;

            final Color TEMPCOLOR = g.getColor();
            final Font TEMPFONT = g.getFont();

            if(_Image != null) {
                g.drawImage(_Image, 0, 0, this);
            }

            if(_CLOSE_ENTERED_ != null) {
                //g.drawImage(_CLOSE_ENTERED_, DEFAULT_WIDTH - 36, 14, this);
            }

            double Percent = (_Value > 0 ? ((double)_Value / _Max) : 0.00);

            if(_Value > 0) {
                g.setColor(_ProgressColor);

                double FillPercent = _ProgressBarWidth * Percent;

                g.fillRect(_ProgressBarX, _ProgressBarY, (int)FillPercent, _ProgressBarHeight);
            }

            if(_Show_Percent) {
                if(_Buffer.length() > 0) {
                    _Buffer.append(" - ");
                }

                _Buffer.append((int)(Percent * 100));
                _Buffer.append('%');
            }

            if(_Buffer.length() > 0) {
                g.setColor(_TextColor);

                g.setFont(new Font("", Font.PLAIN, 14));
                String TempString = _Buffer.toString();

                int StrOffset = g.getFontMetrics().stringWidth(TempString) / 2;

                g.drawString(TempString, (_ProgressBarWidth / 2) + 22 - StrOffset, DEFAULT_HEIGHT - 40 + 15);

                try {
                    _Buffer.reset(_Buffer.getStoredLength());
                } catch (Exception e) {
                    e.printStackTrace();
                    _Buffer.reset(_Buffer.length());
                }
            }

            g.setFont(TEMPFONT);
            g.setColor(TEMPCOLOR);

            _IsPainting = false;
        }
    }
*/