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

import jp77.utillib.file.MyFileInputStream;
import jp77.utillib.interfaces.IProgress;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;

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
//TODO 1: update this so the window could be resized to any valid size (only 236, 400 is currently allowed)
//TODO 1: allow only one progress bar (has two right now)
public class FrmSplashBetav2 extends JWindow implements WindowConstants {
	public static final String _DEFAULT_BACKGROUND_RESOURCE_ = "/resources/images/blank.jpg";
	public static final int _DEFAULT_HEIGHT_ = 236;
	public static final int _DEFAULT_WIDTH_ = 400;

	private final Window _WINDOW = this;

	private final Progress _TOTAL;
	private final Progress _CURRENT;

	private Image _Logo_Image = null;
	private Image _Background_Image = null;

	private static final ImageIcon _CLOSE_EXITED_ = new ImageIcon(ImageUtil.loadImageFromSystemResource("/resources/images/close exited.png"));
	private static final ImageIcon _CLOSE_ENTERED_ = new ImageIcon(ImageUtil.loadImageFromSystemResource("/resources/images/close entered.png"));

//    private ResizingArray<Runnable> _Runnables = new ResizingArray<Runnable>(0);

	private int _CloseAction = DO_NOTHING_ON_CLOSE;

	/**
     *
     */
	public FrmSplashBetav2() {
		this(ImageUtil.loadImageFromSystemResource(_DEFAULT_BACKGROUND_RESOURCE_));
	}

	/**
	 * 
	 * @param max
	 */
	public FrmSplashBetav2(Image background) {
		this(background, null);
	}

	/**
	 * 
	 * @param max
	 * @param background
	 * @param image
	 */
	public FrmSplashBetav2(Image background, Image image) {
//        if(background == null) {
//            throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[background] - Is Null");
//        }

		initComponents();

		setBackgroundImage(background, 0, 0);

		if(image != null) {
			setLogoImage(image);
		}

		_TOTAL = new TotalProgress(progressTotal);
		_CURRENT = new CurrentProgress(progressCurrent);
	}

	private void initComponents() {
		final SpringLayout LAYOUT = new SpringLayout();

		super.setSize(_DEFAULT_WIDTH_, _DEFAULT_HEIGHT_);
		super.getContentPane().setLayout(LAYOUT);

		lblInfo = new JLabel("Loading...");
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setBackground(Color.BLACK);
		lblInfo.setOpaque(true);
		lblInfo.setFont(new Font("Dialog", Font.PLAIN, 14));
		LAYOUT.putConstraint(SpringLayout.NORTH, lblInfo, -70, SpringLayout.SOUTH, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.WEST, lblInfo, 20, SpringLayout.WEST, super.getContentPane());
//        LAYOUT.putConstraint(SpringLayout.HEIGHT, lblInfo, 2, SpringLayout.HEIGHT, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.WIDTH, lblInfo, -40, SpringLayout.WIDTH, super.getContentPane());
		super.getContentPane().add(lblInfo);

		progressTotal = new JProgressBar();
		LAYOUT.putConstraint(SpringLayout.NORTH, progressTotal, 0, SpringLayout.SOUTH, lblInfo);
		LAYOUT.putConstraint(SpringLayout.WEST, progressTotal, 20, SpringLayout.WEST, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.HEIGHT, progressTotal, -220, SpringLayout.SOUTH, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.WIDTH, progressTotal, -70, SpringLayout.WIDTH, super.getContentPane());

		super.getContentPane().add(progressTotal);

		progressCurrent = new JProgressBar();
		LAYOUT.putConstraint(SpringLayout.NORTH, progressCurrent, 0, SpringLayout.SOUTH, progressTotal);
		LAYOUT.putConstraint(SpringLayout.WEST, progressCurrent, 20, SpringLayout.WEST, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.HEIGHT, progressCurrent, -220, SpringLayout.SOUTH, super.getContentPane());
		LAYOUT.putConstraint(SpringLayout.WIDTH, progressCurrent, -70, SpringLayout.WIDTH, super.getContentPane());

		super.getContentPane().add(progressCurrent);

		lblPercent = new JLabel("0%");
		lblPercent.setForeground(Color.WHITE);
		lblPercent.setBackground(Color.BLACK);
		lblPercent.setOpaque(true);
		lblPercent.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPercent.setHorizontalAlignment(JLabel.CENTER);
		lblPercent.setVerticalAlignment(JLabel.CENTER);

		LAYOUT.putConstraint(SpringLayout.NORTH, lblPercent, 0, SpringLayout.NORTH, progressTotal);
		LAYOUT.putConstraint(SpringLayout.WEST, lblPercent, 0, SpringLayout.EAST, progressTotal);
		LAYOUT.putConstraint(SpringLayout.HEIGHT, lblPercent, -220, SpringLayout.SOUTH, super.getContentPane());
//        LAYOUT.putConstraint(SpringLayout.WIDTH, lblPercent, 0, SpringLayout.WIDTH, progressCurrent);

		super.getContentPane().add(lblPercent);

		lblClose = new JLabel(_CLOSE_EXITED_);
		lblClose.setBounds(_DEFAULT_WIDTH_ - 36, 14, 20, 20);
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
//        super.getContentPane().add(lblClose);

		PositionWindow.quickToCenter(this);
	}

	public void setCloseAction(int closeaction) {
		if(closeaction >= 0 && closeaction <= 3) {
			_CloseAction = closeaction;
		} else {
			throw new RuntimeException("Variable[closeaction] - Choose: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
		}
	}

	public void setBackgroundImage(String path, boolean scaletowindowsize) {
		setBackgroundImage(new File(path), scaletowindowsize);
	}

	public void setBackgroundImage(File path, boolean scaletowindowsize) {
		if(path == null) {
			throw new RuntimeException("Variable[filepath] - Is Null");
		} else if(!path.exists()) {
			throw new RuntimeException("Variable[filepath] - File: " + path.getPath() + " - Does Not Exists");
		}

		try {
			BufferedImage T_Img = ImageIO.read(path);

			if(scaletowindowsize) {
				setBackgroundImage(T_Img, super.getHeight(), super.getWidth());
			} else {
				setBackgroundImage(T_Img, 0, 0);
			}
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setBackgroundImage(URL url, boolean scaletowindowsize) {
		if(url == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		try {
			BufferedImage T_Img = ImageIO.read(url);

			if(scaletowindowsize) {
				setBackgroundImage(T_Img, super.getHeight(), super.getWidth());
			} else {
				setBackgroundImage(T_Img, 0, 0);
			}
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setBackgroundImage(Image image, int newheight, int newwidth) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

//        Image T_Img = null;
//        if(newheight > 0 || newheight > 0) {
//        	T_Img = image.getScaledInstance(newheight, newheight, Image.SCALE_DEFAULT);
//        	_Background_Image = T_Img;
//        	T_Img = null;
//        } else {
//        	_Background_Image = image;
//        }

		_Background_Image = getScaledInstance(image, newheight, newwidth);

		super.repaint();
	}

	private static Image getScaledInstance(Image image, int newheight, int newwidth) {
		int H = image.getHeight(null);
		int W = image.getWidth(null);

		if(newheight > 0) {
			H = newheight;
		}

		if(newwidth > 0) {
			W = newwidth;
		}

		if(H != image.getHeight(null) || W != image.getWidth(null)) {
			return image.getScaledInstance(H, W, Image.SCALE_DEFAULT);
		}

		return image;
	}

	public void setImage(String path) {
		setLogoImage(new File(path));
	}

	public void setLogoImage(File path) {
		if(path == null) {
			throw new RuntimeException("Variable[filepath] - Is Null");
		} else if(!path.exists()) {
			throw new RuntimeException("Variable[filepath] - File: " + path.getPath() + " - Does Not Exists");
		}

		try {
			BufferedImage TempImage = ImageIO.read(path);
			_Logo_Image = null;
			_Logo_Image = TempImage;
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setLogoImage(URL url) {
		if(url == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		try {
			BufferedImage TempImage = ImageIO.read(url);
			_Logo_Image = null;
			_Logo_Image = TempImage;
			TempImage = null;

			super.repaint();
		} catch(IOException e) {
			setBackgroundColor("white");
		}
	}

	public void setLogoImage(Image image) {
		setLogoImage(image, 0, 0);
	}

	public void setLogoImage(Image image, int newheight, int newwidth) {
		if(image == null) {
			throw new RuntimeException("Variable[image] - Is Null");
		}

		Image T_Img = null;
		if(newheight > 0 || newheight > 0) {
			T_Img = image.getScaledInstance(newheight, newheight, Image.SCALE_DEFAULT);
			_Logo_Image = T_Img;
			T_Img = null;
		} else {
			_Logo_Image = image;
		}

		super.repaint();
	}

	public void setBackgroundColor(String color) {
		final Color COLOR = ColorUtil.convertStrToColor(color);

		if(COLOR != null) {
			super.getContentPane().setBackground(COLOR);
		}
	}

	public void setText(String text) {
		getTotal().setText(text);
	}

	public String getText() {
		return getTotal().getText();
	}

	public Progress getCurrent() {
		return _CURRENT;
	}

	public Progress getTotal() {
		return _TOTAL;
	}

	public void reset() {
		PositionWindow.quickToCenter(this);

		_Logo_Image = null;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);

		if(_Background_Image != null) {
			graphics.drawImage(_Background_Image, 0, 0, this);
		}

		if(_Logo_Image != null) {
			graphics.drawImage(_Logo_Image, 0, 0, this);
		}
	}

	public void setTextColor(Color color) {
		lblInfo.setForeground(color);
		lblPercent.setForeground(color);
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

	public static void main(String[] args) {
		final int Max1 = 10;
		final int Max2 = 20;

		final String LCLAF = jp77.utillib.utilities.LookAndFeels._WINDOWS_L_AND_F_;
		try {
			if(LCLAF.equals(jp77.utillib.utilities.LookAndFeels._WINDOWS_L_AND_F_)) {
				javax.swing.UIManager.setLookAndFeel(jp77.utillib.utilities.LookAndFeels._WINDOWS_LOOK_AND_FEEL_);
			} else if(LCLAF.equals(jp77.utillib.utilities.LookAndFeels._WINDOWSCLASSIC_L_A_F_)) {
				javax.swing.UIManager.setLookAndFeel(jp77.utillib.utilities.LookAndFeels._WINDOWSCLASSIC_LOOK_AND_FEEL_);
			} else if(LCLAF.equals(jp77.utillib.utilities.LookAndFeels._METAL_L_AND_F_)) {
				javax.swing.UIManager.setLookAndFeel(jp77.utillib.utilities.LookAndFeels._METAL_LOOK_AND_FEEL_);
			} else if(LCLAF.equals(jp77.utillib.utilities.LookAndFeels._MOTIF_L_A_F_)) {
				javax.swing.UIManager.setLookAndFeel(jp77.utillib.utilities.LookAndFeels._MOTIF_LOOK_AND_FEEL_);
			} else if(LCLAF.equals(jp77.utillib.utilities.LookAndFeels._NIMBUS_L_AND_F_)) {
				javax.swing.UIManager.setLookAndFeel(jp77.utillib.utilities.LookAndFeels._NIMBUS_LOOK_AND_FEEL_);
			} else {
				System.out.println("No Look And Feel");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		FrmSplashBetav2 Splash = new FrmSplashBetav2();

		new PositionWindow(Splash).toLowerLeft();

		Splash.setCloseAction(EXIT_ON_CLOSE);

		MyFileInputStream IStream = null;
		try {
			IStream = new MyFileInputStream("C:\\Program Files\\Mozilla Firefox\\dictionaries\\en-US.dic");
		} catch(Exception e) {}

		Splash.setText("Hello Me");
//        Splash.getTotal().setBarColor(Color.RED);
//        Splash.getCurrent().setBarColor(Color.YELLOW);

		Splash.getTotal().setBarBackgroundColor(Color.YELLOW);
		Splash.getCurrent().setBarBackgroundColor(Color.YELLOW);

//        Splash.setTextColor(Color.WHITE);
		Splash.setVisible(true);

		Splash.getTotal().setMinimum(0);
		Splash.getTotal().setMaximum(Max1);

		Splash.getCurrent().setMinimum(0);
		for(int X = 0; X < Max1; X++) {
			try {
				Thread.sleep(200);
			} catch(Exception e) {}

			Splash.getCurrent().setValue(0);
			Splash.getCurrent().setMaximum(Max2);

			String Line = "Hello Me";
			for(int Y = 0; Y < Max2; Y++) {

				if(IStream != null && Line != null) {
					try {
						Line = IStream.readln();

						if(Line != null) {
							final int INDEX = Line.indexOf('/');

							if(INDEX != -1) {
								Line = Line.substring(0, INDEX);
							}

							Splash.setText(Line);
						}
					} catch(Exception e) {}
				}

				Splash.getCurrent().increment();
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			}

			Splash.getTotal().increment();
		}

		if(IStream != null) {
			try {
				IStream.close();
			} catch(Exception e) {}
			IStream = null;
		}

		try {
			Thread.sleep(50);
		} catch(Exception e) {}
		Splash.dispose();
	}

}
