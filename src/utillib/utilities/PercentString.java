package utillib.utilities;

import utillib.strings.MyStringBuffer;

/**
 *
 * @author Dalton Dell
 */
public class PercentString {
    final MyStringBuffer _BUFFER;

    public PercentString(String name) {
        this(name, 32);
    }

    public PercentString(String startstring, int intialsize) {
        _BUFFER = new MyStringBuffer(startstring.length() + intialsize);
        _BUFFER.append(startstring);
        _BUFFER.append(" - ");
        _BUFFER.storeLength();
        _BUFFER.append("0%");
    }

    public void setPercent(byte value) {
        _BUFFER.resetToStoredLength();
        _BUFFER.append(value, true);
        _BUFFER.append('%');
    }

    public void setPercent(int value) {
        _BUFFER.resetToStoredLength();
        _BUFFER.append(value, true);
        _BUFFER.append('%');
    }

    public void setPercent(long value) {
        _BUFFER.resetToStoredLength();
        _BUFFER.append(value, true);
        _BUFFER.append('%');
    }

    public String getString() {
        return _BUFFER.toString();
    }

    @Override
    public String toString() {
        return getString();
    }
}
