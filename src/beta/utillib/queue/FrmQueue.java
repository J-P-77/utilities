package beta.utillib.queue;

import beta.utillib.queue.v1.ThreadQueue;
import utillib.interfaces.IProgress;
import utillib.interfaces.IText;

// import utillib.arrays.ResizingArray;

import utillib.utilities.MsgUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

/**
 * 
 * @author Justin Palinkas
 */
public class FrmQueue extends JDialog {
//    private ResizingArray<Thread> _Threads = new ResizingArray<Thread>();

	private int _Current_Num_Of_Thread = 0;
	private int _Max_Num_Of_Threads = 1;

	private final Dimension _DEFAULT_QUEUE_DIM = new Dimension(300, 60);

	private final ThreadQueue _QUEUE;

	public FrmQueue() {
		initComponents();

		_QUEUE = new ThreadQueue(new IQueue() {

			@Override
			public boolean canAcceptTask() {
				return /*_Threads.length()*/_Current_Num_Of_Thread < _Max_Num_Of_Threads;
			}

			@Override
			public void taskAdded(QueueTask task) {
				updateNumOfTasks();
			}

			@Override
			public void taskRemoved(QueueTask task) {
				updateNumOfTasks();
			}

			@Override
			public void startTask(QueueTask task) {
				final Thread THREAD = new Thread(task, task.getName());

				final QueuePanel QP = new QueuePanel(task);
				//         QP.setPreferredSize(DIM);
				//         QP.setMinimumSize(DIM);
				//         QP.setMaximumSize(DIM);
				QP.setSize(_DEFAULT_QUEUE_DIM);

				if(task instanceof IPanel) {
					((IPanel)task).setPanel(QP);

					synchronized(listQueue.getTreeLock()) {
						listQueue.add(((IPanel)task).getPanel());
						//            LAYOUT.setRows(listQueue.getComponentCount() - 1);
						//            System.out.println(listQueue.getComponentCount()+ "");
						listQueue.validate();
						listQueue.repaint();
					}

					synchronized(scroll.getTreeLock()) {
						scroll.validate();
						scroll.repaint();
					}
				}

//                _Threads.put(T);

				_Current_Num_Of_Thread++;

				THREAD.setPriority(task.getPriority());
				THREAD.start();

				updateNumOfTasks();
			}

			@Override
			public void endTask(QueueTask task) {
//                for(int X =0; X < _Threads.length(); X++) {
//                    if(_Threads.getAt(X).getName().equals(task.getName())) {
//                        _Threads.removeAt(X);
//                        break;
//                    }
//                }

				_Current_Num_Of_Thread--;

				updateNumOfTasks();
			}

			@Override
			public void started() {}

			@Override
			public void shutdowned() {}
		});

		_QUEUE.startup();
	}

	private void initComponents() {
		super.setTitle("Queue");
		super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		super.setSize(300, 400);
		super.getContentPane().setLayout(new BorderLayout());

		listQueue = new JPanel();
		listQueue.setLayout(new MyLayout());

		scroll = new JScrollPane(listQueue, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				if(e.getID() == ComponentEvent.COMPONENT_RESIZED) {
					_DEFAULT_QUEUE_DIM.setSize(scroll.getWidth(), 60);
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentShown(ComponentEvent e) {}

			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		super.getContentPane().add(scroll, BorderLayout.CENTER);

		panelBottom = new JPanel();
		panelBottom.setPreferredSize(new Dimension(0, 40));

		butClear = new JButton("Clear Done");
		butClear.setMargin(new Insets(1, 1, 1, 1));
		butClear.setPreferredSize(new Dimension(80, 24));
		butClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent aevent) {
				butClearActionPerformed(aevent);
			}
		});
		panelBottom.add(butClear);

		super.getContentPane().add(panelBottom, BorderLayout.PAGE_END);
	}

	public void butClearActionPerformed(ActionEvent aevent) {
		for(int X = listQueue.getComponentCount() - 1; X > -1; X--) {
			final Component COMP = listQueue.getComponent(X);

			if(COMP instanceof QueuePanel) {
				if(!((QueuePanel)COMP).getTask().isRunning()) {

					synchronized(listQueue.getTreeLock()) {
						listQueue.remove(X);
					}
				}
			}
		}

		synchronized(scroll.getTreeLock()) {
			scroll.validate();
			scroll.repaint();
		}

		synchronized(listQueue.getTreeLock()) {
			listQueue.validate();
			listQueue.repaint();
		}
	}

	public void addTask(QueueTask task) {
		_QUEUE.addTask(task);
	}

	public void removeTask(QueueTask task) {
		_QUEUE.removeTask(task);
	}

	public boolean isRunningTask() {
		return _Current_Num_Of_Thread/*_Threads.length()*/> 0;
	}

	public void shutdown() {
		_QUEUE.shutdown();
	}

	public void setMaxNumOfThreads(int value) {
		if(value <= 0) {
			throw new RuntimeException("Variable[value] - Must Be Greater Than Zero");
		}

		_Max_Num_Of_Threads = value;
	}

	public int getMaxNumOfThreads() {
		return _Max_Num_Of_Threads;
	}

	private void removePanel(QueuePanel panel) {
		synchronized(listQueue.getTreeLock()) {
			listQueue.remove(panel);
//            LAYOUT.setRows(listQueue.getComponentCount() - 1);
//            System.out.println(listQueue.getComponentCount()+ "");
			listQueue.validate();
			listQueue.repaint();
		}
	}

	private void updateNumOfTasks() {
		super.setTitle("Queue # of Tasks: " + _QUEUE.taskCount());
	}

	@Override
	@SuppressWarnings("FinalizeDeclaration")
	protected void finalize() throws Throwable {
		_QUEUE.shutdown();

		super.finalize();
	}

	private JPanel listQueue;
	private JScrollPane scroll;

	private JPanel panelBottom;
	private JButton butClear;

	//CLASSES
	public class QueuePanel extends JPanel implements IText, IProgress {
		private final QueueTask _TASK;

		public QueuePanel(QueueTask task) {
			super(new SpringLayout());

			if(task == null) {
				throw new RuntimeException("Variable[task] - Is Null");
			}

			_TASK = task;

			initComponents();
		}

		private void initComponents() {
			final SpringLayout LAYOUT;
			if(super.getLayout() instanceof SpringLayout) {
				LAYOUT = (SpringLayout)super.getLayout();
			} else {
				LAYOUT = null;
			}

			final JPanel THIS = this;
			final Color DEFAULT = THIS.getBackground();
			super.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
//                    THIS.setBackground(Color.ORANGE);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					THIS.setBackground(Color.ORANGE);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					THIS.setBackground(DEFAULT);
				}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}
			});
//            super.setLayout(LAYOUT);

			progressBar = new JProgressBar();
			if(LAYOUT != null) {
				LAYOUT.putConstraint(SpringLayout.WEST, progressBar, 4, SpringLayout.WEST, this);
				LAYOUT.putConstraint(SpringLayout.NORTH, progressBar, 4, SpringLayout.NORTH, this);
				LAYOUT.putConstraint(SpringLayout.HEIGHT, progressBar, -36, SpringLayout.HEIGHT, this);
				LAYOUT.putConstraint(SpringLayout.WIDTH, progressBar, -40, SpringLayout.WIDTH, this);
			}
			super.add(progressBar);

//            lblText = new JLabel();
//            lblText.setText("Justin");
//            lblText.setOpaque(true);
////            lblText.setHorizontalTextPosition(JLabel.LEFT);
//            lblText.setBackground(Color.red);
//
//            if(LAYOUT != null) {
//                LAYOUT.putConstraint(SpringLayout.NORTH, lblText, 4, SpringLayout.SOUTH, progressBar);
//                LAYOUT.putConstraint(SpringLayout.WEST, lblText, 4, SpringLayout.WEST, this);
////                LAYOUT.putConstraint(SpringLayout.HEIGHT, lblText, 0, SpringLayout.HEIGHT, this);
//                LAYOUT.putConstraint(SpringLayout.WIDTH, lblText, -6, SpringLayout.WIDTH, this);
//            }
//            super.add(lblText);

			txtText = new JTextField();
			txtText.setText("Justin");
			txtText.setOpaque(true);
			txtText.setAutoscrolls(false);
			txtText.setEditable(false);
			txtText.setBorder(null);
//            txtText.setBackground(Color.red);

			if(LAYOUT != null) {
				LAYOUT.putConstraint(SpringLayout.NORTH, txtText, 4, SpringLayout.SOUTH, progressBar);
				LAYOUT.putConstraint(SpringLayout.WEST, txtText, 4, SpringLayout.WEST, this);
//                LAYOUT.putConstraint(SpringLayout.HEIGHT, txtText, 0, SpringLayout.HEIGHT, this);
				LAYOUT.putConstraint(SpringLayout.WIDTH, txtText, -6, SpringLayout.WIDTH, this);
			}
			super.add(txtText);

			lblPause = new JLabel("||");
			lblPause.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(_TASK.isRunning()) {
						if(_TASK.isPaused()) {
							lblPause.setText("||");
							lblPause.setToolTipText("Pause");
							_TASK.pause(false);
						} else {
							lblPause.setText("P");
							lblPause.setToolTipText("Unpause");
							_TASK.pause(true);
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}
			});
			super.add(lblPause);

			if(LAYOUT != null) {
				LAYOUT.putConstraint(SpringLayout.WEST, lblPause, 4, SpringLayout.EAST, progressBar);
				LAYOUT.putConstraint(SpringLayout.NORTH, lblPause, 0, SpringLayout.NORTH, progressBar);
			}

			lblCancel = new JLabel("O");
			lblCancel.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(cancelQuestion(true)) {
						_TASK.cancel();//removePanel(/*(QueuePanel)*/this);
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {
					if(_TASK.isRunning()) {
						lblCancel.setText("X");

						if(_TASK.isDone() || _TASK.isCanceled()) {
							lblCancel.setToolTipText("Close");
						} else {
							lblCancel.setToolTipText("Cancel");
						}
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if(_TASK.isRunning()) {
						lblCancel.setText("O");
						lblCancel.setToolTipText(null);
					}
				}
			});
			super.add(lblCancel);

			if(LAYOUT != null) {
				LAYOUT.putConstraint(SpringLayout.WEST, lblCancel, 4 + 14, SpringLayout.EAST, progressBar);
				LAYOUT.putConstraint(SpringLayout.NORTH, lblCancel, 0, SpringLayout.NORTH, progressBar);
			}

			super.setBorder(new LineBorder(Color.yellow));
		}

		public QueueTask getTask() {
			return _TASK;
		}

		public boolean cancelQuestion(boolean pause) {
			if(_TASK == null || _TASK.isDone()) {
				return false;
			} else {
				if(!_TASK.isCanceling() && !_TASK.isCanceled()) {
					if(pause) {
						_TASK.pause(true);
					}

					if(!_TASK.isCanceling() && !_TASK.isCanceled()) {
						if(MsgUtil.msgboxYesNo(this, "Cancel Process?", "Queue")) {
							return true;
						} else {
							if(pause) {
								_TASK.pause(false);
							}

							return false;
						}
					} else {
						if(pause) {
							_TASK.pause(false);
						}

						return true;
					}
				} else {
					return true;
				}
			}
		}

		@Override
		public void increment() {
			progressBar.setValue(progressBar.getValue() + 1);
		}

		@Override
		public void increment(int value) {
			progressBar.setValue(progressBar.getValue() + value);
		}

		@Override
		public void setValue(int value) {
			progressBar.setValue(value);
		}

		@Override
		public int getValue() {
			return progressBar.getValue();
		}

		@Override
		public void setMaximum(int value) {
			progressBar.setMaximum(value);
		}

		@Override
		public int getMaximum() {
			return progressBar.getMaximum();
		}

		@Override
		public void setMinimum(int value) {
			progressBar.setMinimum(value);
		}

		@Override
		public int getMinimum() {
			return progressBar.getMinimum();
		}

		@Override
		public void setText(String value) {
			if(lblText == null) {
				txtText.setText(value);
			} else {
				lblText.setText(value);
			}
		}

		@Override
		public String getText() {
			if(lblText == null) {
				return txtText.getText();
			} else {
				return lblText.getText();
			}
		}

		private JLabel lblText;
		private JTextField txtText;

		private JLabel lblCancel;
		private JLabel lblPause;

		private JProgressBar progressBar;
	}

	private class MyLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component comp) {}

		@Override
		public void removeLayoutComponent(Component comp) {}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
//            System.out.println("preferredLayoutSize");
			final Component[] CHILDERN = parent.getComponents();

			int Y = 0;
			for(int Z = 0; Z < CHILDERN.length; Z++) {
				final int HEIGHT = CHILDERN[Z].getHeight();

				Y += HEIGHT;
			}

			return new Dimension(0, Y);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
//            System.out.println("minimumLayoutSize");

			return new Dimension(0, 0);
		}

		@Override
		public void layoutContainer(Container parent) {
//            System.out.println("layoutContainer");
			final Component[] CHILDERN = parent.getComponents();

			int X = 0;
			int Y = 0;
			for(int Z = 0; Z < CHILDERN.length; Z++) {
				final int HEIGHT = CHILDERN[Z].getHeight();

				CHILDERN[Z].setLocation(X, Y);
				CHILDERN[Z].setSize(parent.getWidth(), CHILDERN[Z].getHeight());

				Y += HEIGHT;
			}
		}
	}

	public interface IPanel {
		public void setPanel(FrmQueue.QueuePanel panel);

		public FrmQueue.QueuePanel getPanel();
	}

	public static void main(String[] args) {
		final FrmQueue FRM = new FrmQueue();

		FRM.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

//        QueueListCellRenderer Q = new QueueListCellRenderer();

//        Q.lblText.setText("Justin");
//        FRM.getContentPane().add(Q);

		FRM.setSize(300, 500);
		FRM.setVisible(true);
//        FRM.setMaxNumOfThreads(4);
//
//        for(int X = 0; X < FRM.getMaxNumOfThreads() * 5; X++) {
//            FRM.addTask(new DownloadSimulationQueueTask("Test " + X));
//        }

		FRM._QUEUE.shutdown();
	}
}
