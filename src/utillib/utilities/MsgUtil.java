package utillib.utilities;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * <pre>
 * <b>Current Version 1.0.1</b>
 * 
 * November 04, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * April 14, 2008 (Version 1.0.1)
 *     -Updated
 *         -Method msgboxYesNo(Component, msg, title) Now Returns A boolean Value, true For Yes And false For No
 * 
 * @author Justin Palinkas
 * 
 * <pre>
 */
public class MsgUtil {
	public static final int _YES_OPTION_ = JOptionPane.YES_OPTION;
	public static final int _NO_OPTION_ = JOptionPane.NO_OPTION;
	public static final int _CANCEL_OPTION_ = JOptionPane.CANCEL_OPTION;

	public static void msgboxError(Component owner, String msg, String title) {
		JOptionPane.showMessageDialog(owner, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void msgboxInformation(Component owner, String msg, String title) {
		JOptionPane.showMessageDialog(owner, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void msgboxWarning(Component owner, String msg, String title) {
		JOptionPane.showMessageDialog(owner, msg, title, JOptionPane.WARNING_MESSAGE);
	}

	public static int msgboxYesNoInt(Component owner, String msg, String title) {
		final int RESULT = JOptionPane.showConfirmDialog(owner, msg, title, JOptionPane.YES_NO_OPTION);

		return (RESULT == JOptionPane.CLOSED_OPTION ? _CANCEL_OPTION_ : RESULT);
	}

	public static boolean msgboxYesNo(Component owner, String msg, String title) {
		return JOptionPane.showConfirmDialog(owner, msg, title, JOptionPane.YES_NO_OPTION) == _YES_OPTION_;
	}

	public static String inputError(Component owner, String msg, String title) {
		return JOptionPane.showInputDialog(owner, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public static String inputInformation(Component owner, String msg, String title) {
		return JOptionPane.showInputDialog(owner, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static String inputWarning(Component owner, String msg, String title) {
		return JOptionPane.showInputDialog(owner, msg, title, JOptionPane.WARNING_MESSAGE);
	}

	public static String inputQuestion(Component owner, String msg, String title) {
		return JOptionPane.showInputDialog(owner, msg, title, JOptionPane.QUESTION_MESSAGE);
	}

	public static String inputPlain(Component owner, String msg, String title) {
		return JOptionPane.showInputDialog(owner, msg, title, JOptionPane.PLAIN_MESSAGE);
	}

	public static String inputPlain(Component owner, String msg, String title, String intialvalue) {
		final Object RESULT = JOptionPane.showInputDialog(owner, msg, title, JOptionPane.PLAIN_MESSAGE, null, null, intialvalue);

		return (RESULT == null ? null : (String)RESULT);
	}
}
