package utillib.panels;

import utillib.interfaces.IProgress;
import utillib.interfaces.IText;

import utillib.utilities.MsgUtil;

import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.awt.Dimension;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import javax.swing.SpringLayout;

/**
 *
 * @author Dalton Dell
 */
public class PanelProgress extends JPanel implements IProgress, IText {
    private static final int _CANCELING_ = 0;
    private static final int _CANCELED_ = 1;
    private static final int _DONE_ = 2;

    private boolean _Paused = false;

    private int _Status = -1;

    public PanelProgress() {
        initComponents();
    }

    private void initComponents() {
//        if(compInfo != null) {
//            super.setPreferredSize(new Dimension(340, 60));
//        } else {
//            super.setPreferredSize(new Dimension(340, 90));
//        }
//        super.setSize(348 - 8, 100 - 10);

        super.setMinimumSize(new Dimension(300, 60));
        
        final SpringLayout LAYOUT = new SpringLayout();

        super.setLayout(LAYOUT);

        proProgress = new JProgressBar();
        proProgress.setPreferredSize(new Dimension(180, 22));
        proProgress.setStringPainted(true);
        super.add(proProgress);

        LAYOUT.putConstraint(SpringLayout.WEST, proProgress, 2, SpringLayout.WEST, this);
        LAYOUT.putConstraint(SpringLayout.NORTH, proProgress, 2, SpringLayout.NORTH, this);
//        LAYOUT.putConstraint(SpringLayout.HEIGHT, proProgress, -36, SpringLayout.HEIGHT, super.getContentPane());
        LAYOUT.putConstraint(SpringLayout.WIDTH, proProgress, -128, SpringLayout.WIDTH, this);
        
        final Insets BUTTONINSETS = new Insets(1, 1, 1, 1);

        butPause = new JButton("Pause");
        butPause.setPreferredSize(new Dimension(60, 22));
        butPause.setMargin(BUTTONINSETS);
        butPause.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aevent) {
                butPauseActionPerformed(aevent);
            }
        });
        super.add(butPause);

        LAYOUT.putConstraint(SpringLayout.WEST, butPause, 2, SpringLayout.EAST, proProgress);
        LAYOUT.putConstraint(SpringLayout.NORTH, butPause, 2, SpringLayout.NORTH, this);
//        LAYOUT.putConstraint(SpringLayout.HEIGHT, proProgress, -36, SpringLayout.HEIGHT, super.getContentPane());
//        LAYOUT.putConstraint(SpringLayout.WIDTH, butPause, -120, SpringLayout.WIDTH, super.getContentPane());

        butCancel = new JButton("Cancel");
        butCancel.setPreferredSize(new Dimension(60, 22));
        butCancel.setMargin(BUTTONINSETS);
        butCancel.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aevent) {
                butCancelActionPerformed(aevent);
            }
        });
        super.add(butCancel);
        
        LAYOUT.putConstraint(SpringLayout.WEST, butCancel, 2, SpringLayout.EAST, butPause);
        LAYOUT.putConstraint(SpringLayout.NORTH, butCancel, 2, SpringLayout.NORTH, this);
//        LAYOUT.putConstraint(SpringLayout.HEIGHT, proProgress, -36, SpringLayout.HEIGHT, super.getContentPane());
//        LAYOUT.putConstraint(SpringLayout.WIDTH, butPause, -120, SpringLayout.WIDTH, super.getContentPane());
    }

    private void butCancelActionPerformed(ActionEvent aevent) {
        if(cancelQuestion(false)) {
            cancel();
        }
    }

    private void butPauseActionPerformed(ActionEvent aevent) {
        _Paused = !_Paused;
    }

    public boolean cancelQuestion(boolean pause) {
        if(_Status == _DONE_) {
            return false;
        } else {
            if(_Status != _CANCELING_ &&  _Status != _CANCELED_) {
                if(pause) {pause(true);}

                if(_Status != _CANCELING_ &&  _Status != _CANCELED_) {
                    if(MsgUtil.msgboxYesNo(this, "Cancel Process?", "")) {

                        return true;
                    } else {
                        if(pause) {pause(false);}

                        return false;
                    }
                } else {
                    if(pause) {pause(false);}

                    return true;
                }
            } else {
                return true;
            }
        }
    }

    @SuppressWarnings("SleepWhileHoldingLock")
    public boolean pauseCancelCheck() {
        while(_Paused) {
            try {
                if(_Status == _CANCELING_ || _Status == _CANCELED_) {
                    return true;
                }
                Thread.sleep(200);
            } catch (Exception e) {}
        }

        return (_Status == _CANCELING_ || _Status == _CANCELED_);
    }

	public void cancel() {
        _Status = _CANCELING_;
		_Paused = false;
	}

    public boolean isCanceling() {
        return _Status == _CANCELING_;
    }

    public void canceled() {
        _Status = _CANCELED_;
    }

    public boolean isCanceled() {
        return _Status == _CANCELED_;
    }

    public void done() {
        _Status = _DONE_;
		_Paused = false;
    }

    public boolean isDone() {
        return _Status == _DONE_;
    }

    public void pause(boolean value) {
        _Paused = value;
    }

    @Override @Deprecated
    public void setText(String text) {
        proProgress.setString(text);
    }

    @Override @Deprecated
    public String getText() {
        return proProgress.getString();
    }

    @Override
    public void setMinimum(int value) {
        proProgress.setMinimum(value);
    }

    @Override
    public int getMinimum() {
        return proProgress.getMinimum();
    }

    @Override
    public void setMaximum(int value) {
        proProgress.setMaximum(value);
    }

    @Override
    public int getMaximum() {
        return proProgress.getMaximum();
    }

    @Override
    public void setValue(int value) {
        proProgress.setValue(value);
    }

    @Override
    public int getValue() {
        return proProgress.getValue();
    }

    public boolean getVisible() {
        return this.isVisible();
    }

    public void setIndeterminate(boolean value) {
        proProgress.setIndeterminate(value);  
    }

    public boolean isIndeterminate() {
        return proProgress.isIndeterminate();  
    }

    @Override
    public void increment() {
        proProgress.setValue(proProgress.getValue() + 1);
    }

    @Override
    public void increment(int value) {
        proProgress.setValue(proProgress.getValue() + value);
    }

    public String getPercent() {
        double Value = proProgress.getValue();
        double Max = proProgress.getMaximum();
        double Total = (Value / Max) * 100;

        return Integer.toString((int)Total) + "%";
    }

    private JProgressBar proProgress;
    private JButton butCancel;
    private JButton butPause;

    public static void main(String[] args) {
        final javax.swing.JDialog FRM = new javax.swing.JDialog();

        final PanelProgress PANEL = new PanelProgress();

//        FRM.setSize(300, 80);
        FRM.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        FRM.getContentPane().add(PANEL);
        FRM.setMinimumSize(new Dimension(300, 40));

        PANEL.setText("This Is Just A Test");
//        PANEL.getJComponent().setEditable(false);
//        PANEL.getJTextComponent().setEnabled(false);
        FRM.setVisible(true);
    }
}
