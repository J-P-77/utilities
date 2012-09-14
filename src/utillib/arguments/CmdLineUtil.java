package utillib.arguments;

import beta.utillib.classloader.v1.LookAndFeelLoader;

import utillib.interfaces.IDebugLogger;

import utillib.commandline.ACmdLineHandler;

import utillib.debug.LogManager;

import utillib.dialogs.FrmList;

import utillib.file.FileUtil;

import utillib.strings.StringUtil;

import utillib.utilities.LookAndFeels;
import utillib.utilities.User;
import utillib.utilities.MsgUtil;
import utillib.utilities.Os;

import javax.swing.UIManager;

import java.io.File;

/**
 * <pre>
 * <b>Current Version 1.1.0</b>
 * 
 * May 5, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * January 26, 2011 (Version 1.1.0)
 * 	   -Added
 * 	       -Method handleGlobalCommands(CmdLineHandler) To Handle HomeDirectory
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class CmdLineUtil implements LookAndFeels {
	public static void handleGlobalCommands(ACmdLineHandler cmdline) {
		ACmdLineHandler.Cmd Cmd = null;

		Cmd = cmdline.findCommand("-home", "-homedir", "-hd");
		if(Cmd != null) {
			if(Cmd.getVariable() == null || Cmd.getVariable().length() == 0) {
				System.out.println("Command[" + Cmd.getName() + "] Missing Argument");
			} else {
				final File DIR = FileUtil.getAbsoluteFile(Cmd.getVariable());

				if(DIR.isFile()) {
					System.out.println("Command[" + Cmd.getName() + "] " + DIR.getPath() + " Is Not A Directory");
				} else if(!DIR.exists()) {
					System.out.println("Command[" + Cmd.getName() + "] " + DIR.getPath() + " Does Not Exists");
				} else {
					FileUtil.setHomeDirectory(Cmd.getVariable());
				}
			}
		}

		Cmd = cmdline.findCommand("-debug-sysout");
		if(Cmd != null) {
			if(Cmd.getVariable() == null || Cmd.getVariable().length() == 0) {
				System.out.println("Command[" + Cmd.getName() + "] Missing Argument");
			} else {
				if(StringUtil.isWholeNumber(Cmd.getVariable())) {
					int Level = 0;
					try {
						Level = Integer.parseInt(Cmd.getVariable());

						for(IDebugLogger.Log_Level X : IDebugLogger.Log_Level.values()) {
							if(X.getLevel() == Level) {
								LogManager.getInstance().setLevel(X);
							}
						}
					} catch(Exception e) {
						System.out.println("Error Parsing Debug Level");
					}
				} else {
					IDebugLogger.Log_Level Level = null;
					try {
						Level = IDebugLogger.Log_Level.valueOf(Cmd.getVariable().toUpperCase());

						LogManager.getInstance().setLevel(Level);
					} catch(Exception e) {
						System.out.println("Debug Level Is Not A Whole Number Or Invalid Debug Level Name (0=None - 4=All)");
					}
				}
			}
		}
	}

	public static boolean simpleLookAndFeel(ACmdLineHandler cmdline) {
		final ACmdLineHandler.Cmd CMD = cmdline.findCommand("-LookAndFeel", "-LaF", "-L&F");

		if(CMD != null) {
			if(CMD.getVariable() == null || CMD.getVariable().length() == 0) {
				System.out.println("Command[" + CMD.getName() + "] Missing Argument");
			} else {
				final String LC_LAF = CMD.getVariable().toLowerCase();

				try {
					if(LC_LAF.equals(_WINDOWS_L_AND_F_)) {
						UIManager.setLookAndFeel(_WINDOWS_LOOK_AND_FEEL_);
						return true;
					} else if(LC_LAF.equals(_WINDOWSCLASSIC_L_A_F_)) {
						UIManager.setLookAndFeel(_WINDOWSCLASSIC_LOOK_AND_FEEL_);
						return true;
					} else if(LC_LAF.equals(_METAL_L_AND_F_)) {
						UIManager.setLookAndFeel(_METAL_LOOK_AND_FEEL_);
						return true;
					} else if(LC_LAF.equals(_MOTIF_L_A_F_)) {
						UIManager.setLookAndFeel(_MOTIF_LOOK_AND_FEEL_);
						return true;
					} else if(LC_LAF.equals(_NIMBUS_L_AND_F_)) {
						UIManager.setLookAndFeel(_NIMBUS_LOOK_AND_FEEL_);
						return true;
					}
				} catch(Exception e) {
					System.out.println("!!!ERROR!!! - Loading Look And Feel: " + LC_LAF);
				}
			}
		}

		return false;
	}

	public static void loadLookAndFeel(ACmdLineHandler cmdline) {
		final ACmdLineHandler.Cmd CMD = cmdline.findCommand("-LookAndFeel", "-LaF", "-L&F");

		if(CMD != null) {
			if(CMD.getVariable() == null || CMD.getVariable().length() == 0) {
				System.out.println("Command[" + CMD.getName() + "] Missing Argument");
			} else {
				final String LC_LAF = CMD.getVariable().toLowerCase();

				try {
					if(LC_LAF.equals(_WINDOWS_L_AND_F_)) {
						UIManager.setLookAndFeel(_WINDOWS_LOOK_AND_FEEL_);
					} else if(LC_LAF.equals(_WINDOWSCLASSIC_L_A_F_)) {
						UIManager.setLookAndFeel(_WINDOWSCLASSIC_LOOK_AND_FEEL_);
					} else if(LC_LAF.equals(_METAL_L_AND_F_)) {
						UIManager.setLookAndFeel(_METAL_LOOK_AND_FEEL_);
					} else if(LC_LAF.equals(_MOTIF_L_A_F_)) {
						UIManager.setLookAndFeel(_MOTIF_LOOK_AND_FEEL_);
					} else if(LC_LAF.equals(_NIMBUS_L_AND_F_)) {
						UIManager.setLookAndFeel(_NIMBUS_LOOK_AND_FEEL_);
					} else if(LC_LAF.equals(_GTK_L_AND_F_)) {
						if(Os.isWindows()) {
							final ACmdLineHandler.Cmd GTK_CMD = cmdline.findCommand("-GtkDir");

							if(GTK_CMD == null) {
								MsgUtil.msgboxError(null, "GTK Is Not Nativly Available On Windows!" + '\n' + "You Must Specifiy A Jar File That Contains GTK Look And Feel" + '\n' + "Use Command[-GtkDir] To Set Jar File Path.", User._USER_NAME_);
							} else if(GTK_CMD.getVariable() == null) {
								System.out.println("Command[-GtkDir] Missing Argument");
							} else {
								final File JAR_FILE = new File(GTK_CMD.getVariable()).getAbsoluteFile();

								if(JAR_FILE.exists()) {
									final LookAndFeelLoader GTKLoader = LookAndFeelLoader.getGTKLoader(JAR_FILE);

									if(GTKLoader.isLoaded()) {
										GTKLoader.setAsLookAndFeel();
									}
								}
							}
						} else if(Os.getOS() == Os.Os_Version.LINUX || Os.getOS() == Os.Os_Version.UNIX) {
							UIManager.setLookAndFeel(_GTK_LOOK_AND_FEEL_);
						} else {
							MsgUtil.msgboxError(null, "Look And Feel: GTK - Not Available", User._USER_NAME_);
						}
					} else {
						MsgUtil.msgboxError(null, "Look And Feel: " + LC_LAF + " - Does Not Exists", User._USER_NAME_);

						final FrmList LIST = new FrmList("Choose Look And Feel", 200, 300);

						final UIManager.LookAndFeelInfo[] L_A_F_INFO = UIManager.getInstalledLookAndFeels();

						for(int X = 0; X < L_A_F_INFO.length; X++) {
							LIST.addItem(L_A_F_INFO[X].getName());
						}

						UIManager.setLookAndFeel(L_A_F_INFO[LIST.showDialogIndex()].getClassName());
					}
				} catch(Exception e) {
					System.out.println("Unable To Load Look And Feel: " + LC_LAF);
					MsgUtil.msgboxError(null, "Unable To Load Look And Feel: " + LC_LAF, User._USER_NAME_);
				}
			}
		} else {
			final ACmdLineHandler.Cmd LAF_DIR_CMD = cmdline.findCommand("-LaF-Dir");
			final ACmdLineHandler.Cmd CLASS_NAME_CMD = cmdline.findCommand("-LaF-ClassName");

			if(LAF_DIR_CMD != null && CLASS_NAME_CMD != null) {
				if(LAF_DIR_CMD.getVariable() == null) {
					System.out.println("Command[" + LAF_DIR_CMD.getName() + "] Missing Argument");
				} else if(CLASS_NAME_CMD.getVariable() == null) {
					System.out.println("Command[" + CLASS_NAME_CMD.getName() + "] Missing Argument");
				} else {
					final File JAR_FILE = new File(LAF_DIR_CMD.getVariable()).getAbsoluteFile();

					if(JAR_FILE.exists()) {
						try {
							final LookAndFeelLoader LAF_LOADER = new LookAndFeelLoader(JAR_FILE, CLASS_NAME_CMD.getVariable());

							if(LAF_LOADER.isLoaded()) {
								LAF_LOADER.setAsLookAndFeel();
							}
						} catch(Exception e) {
							MsgUtil.msgboxError(null, "Unable To Load Look And Feel: " + CLASS_NAME_CMD.getVariable() + '\n' + e.getMessage(), User._USER_NAME_);
						}
					} else {
						System.out.println("Look And Feel File/Directory Does Not Exists");
						MsgUtil.msgboxError(null, "Look And Feel File: " + JAR_FILE.getPath() + " - Does Not Exists", User._USER_NAME_);
					}
				}
			}
		}
	}

/*
    public static void main(String[] args) {
        final URL URL = ClassLoader.getSystemClassLoader().getResource(User._WINDOWS_LOOK_AND_FEEL_);
        System.out.println("");
    }
*/
}
