package utillib.collections;

import utillib.interfaces.IProperty;

/**
 *
 * @author Dalton Dell
 */
public class DefaultProperty implements IProperty {
	private final String _Id;
	private Object _Variable = null;

	public DefaultProperty(String id) {
		this(id, null);
	}

	public DefaultProperty(String id, Object variable) {
		_Id = id;
		_Variable = variable;
	}

	@Override
	public String getId() {
		return _Id;
	}

	@Override
	public void setVariable(Object value) {
		if(_Variable == null || !_Variable.equals(value)) {
			_Variable = value;
		}
	}

	@Override
	public Object getVariable() {
		return _Variable;
	}
}
