package utillib.debug;

/**
 * 
 * @author Justin Palinkas
 */
public class DebugMsgCount {
	private int _ErrorCount = 0;
	private int _WarningCount = 0;
	private int _InformationCount = 0;
	private int _OtherCount = 0;
	private int _UnknownCount = 0;

	public void addError() {
		_ErrorCount += 1;
	}

	public void addError(int value) {
		_ErrorCount += value;
	}

	public int getErrorCount() {
		return _ErrorCount;
	}

	public void addWarning() {
		_WarningCount += 1;
	}

	public void addWarning(int value) {
		_WarningCount += value;
	}

	public int getWarningCount() {
		return _WarningCount;
	}

	public void addInformation() {
		_InformationCount += 1;
	}

	public void addInformation(int value) {
		_InformationCount += value;
	}

	public int getInformationCount() {
		return _InformationCount;
	}

	public void addOther() {
		_OtherCount += 1;
	}

	public void addOther(int value) {
		_OtherCount += value;
	}

	public int getOtherCount() {
		return _OtherCount;
	}

	public void addUnknown() {
		_UnknownCount += 1;
	}

	public void addUnknown(int value) {
		_UnknownCount += value;
	}

	public int getUnknownCount() {
		return _UnknownCount;
	}
}
