package utillib.collections;

public class StringCollection<U> extends Collection<String, U> {
	private boolean _MatchCase = false;

	public StringCollection() {
		this(false);
	}

	public StringCollection(boolean matchcase) {
		_MatchCase = matchcase;
	}

	public void setMatchCase(boolean value) {
		_MatchCase = value;
	}

	public boolean getMatchCase() {
		return _MatchCase;
	}

	public String getKeyAt(int index) {
		final Entry E = super.getEntryAt(index);

		if(E != null) {
			return E.getKey();
		}

		return null;
	}

	public int keyPosition(String key) {
		if(key == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		for(int X = 0; X < super.count(); X++) {
			if(_MatchCase) {
				if(super.getEntryAt(X).getKey().equals(key)) {
					return X;
				}
			} else {
				if(super.getEntryAt(X).getKey().equalsIgnoreCase(key)) {
					return X;
				}
			}
		}

		return -1;
	}
}
