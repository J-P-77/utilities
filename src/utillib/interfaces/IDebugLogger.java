package utillib.interfaces;

/**
 * 
 * @author Justin Palinkas
 */
public interface IDebugLogger {
	public static final String _LOG_EVENT_INFORMATION_ = "I - ";
	public static final String _LOG_EVENT_WARNING_ = "W - ";
	public static final String _LOG_EVENT_ERROR_ = "E - ";
	public static final String _LOG_EVENT_UNKNOWN_ = "U - ";

	public enum Log_Level {
		NONE(0),
		ERROR(1),
		WARNING(2),
		INFORMATION(3),
		//PLAIN(4),
		ALL(4);

		private final int _Level;

		private Log_Level(int level) {
			_Level = level;
		}

		public int getLevel() {
			return _Level;
		}
	}

	public void setLevel(Log_Level level);

	public Log_Level getLevel();

	public void printInformation(String msg);

	public void printWarning(String msg);

	public void printError(String msg);

	public void printError(Throwable e);

	public void printBlank(String msg);

	public void printCustom(String name, String logtype, String msg);
}
