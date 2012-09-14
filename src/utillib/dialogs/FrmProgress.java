package utillib.dialogs;

import utillib.panels.PanelProgress;

import utillib.interfaces.IProgress;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;

/**
 * 
 * @author Justin Palinkas
 */

public class FrmProgress extends JDialog implements IProgress {
	private int _Min = 0;
	private int _Max = 0;
	private int _Value = 0;
	private String _Text = null;

	public FrmProgress() {
		this(null, "Progress"/*, new JTextField()*/);
	}

	public FrmProgress(String title) {
		this(null, title/*, new JTextField()*/);
	}

//    public FrmProgress(JComponent component) {
//        this(null, "Progress", component);
//    }

	public FrmProgress(Window owner) {
		this(owner, "Progress"/*, new JTextField()*/);
	}

	public FrmProgress(Window owner, String title/*, JComponent component*/) {
		super(owner, title);

		/*if(component == null) {
		    throw new RuntimeException("Class[" + getClass().getName() + "] - Method[Constructor] - Variable[component] - Is Null");
		}*/

		initComponents();
	}

	private void initComponents() {
//        super.setSize(348 - 8, 100 - 10);
		super.setMinimumSize(new Dimension(300, 60));

		panelProgress = new PanelProgress();

		super.add(panelProgress);
	}

	public void store() {
		_Min = panelProgress.getMinimum();
		_Max = panelProgress.getMaximum();
		_Value = panelProgress.getValue();
		_Text = panelProgress.getText();
	}

	public void restore() {
		panelProgress.setMinimum(_Min);
		panelProgress.setMaximum(_Max);
		panelProgress.setValue(_Value);
		panelProgress.setText(_Text);
	}

	/*public JComponent getJComponent() {
	    return null;
	}*/

	public boolean cancelQuestion(boolean pause) {
		return panelProgress.cancelQuestion(pause);
	}

	public boolean pauseCancelCheck() {
		return panelProgress.pauseCancelCheck();
	}

	public void cancel() {
		panelProgress.cancel();
	}

	public boolean isCanceling() {
		return panelProgress.isCanceling();
	}

	public void canceled() {
		panelProgress.canceled();
	}

	public boolean isCanceled() {
		return panelProgress.isCanceled();
	}

	public void done() {
		panelProgress.done();
	}

	public boolean isDone() {
		return panelProgress.isDone();
	}

	public void pause(boolean value) {
		panelProgress.pause(value);
	}

	@Deprecated
	public void setText(String text) {
		panelProgress.setText(text);
	}

	@Deprecated
	public String getText() {
		return panelProgress.getText();
	}

	public void setMinimum(int value) {
		panelProgress.setMinimum(value);
	}

	public int getMinimum() {
		return panelProgress.getMinimum();
	}

	public void setMaximum(int value) {
		panelProgress.setMaximum(value);
	}

	public int getMaximum() {
		return panelProgress.getMaximum();
	}

	public void setValue(int value) {
		panelProgress.setValue(value);
	}

	public int getValue() {
		return panelProgress.getValue();
	}

	public boolean getVisible() {
		return this.isVisible();
	}

	public void setIndeterminate(boolean value) {
		panelProgress.setIndeterminate(value);
	}

	public boolean isIndeterminate() {
		return panelProgress.isIndeterminate();
	}

	public void increment() {
		panelProgress.setValue(panelProgress.getValue() + 1);
	}

	public void increment(int value) {
		panelProgress.setValue(panelProgress.getValue() + value);
	}

	public String getPercent() {
		double Value = panelProgress.getValue();
		double Max = panelProgress.getMaximum();
		double Total = (Value / Max) * 100;

		return Integer.toString((int)Total) + "%";
	}

	private PanelProgress panelProgress;

	public static void main(String[] args) {
		final FrmProgress FRM = new FrmProgress();
//        FRM.setSize(460, 460);
//        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        FRM.getContentPane().add(new ProgressPane());
//
		FRM.setVisible(true);
	}
}
