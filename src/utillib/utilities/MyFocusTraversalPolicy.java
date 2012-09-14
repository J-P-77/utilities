package utillib.utilities;

import utillib.arrays.ResizingArray;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

public class MyFocusTraversalPolicy extends FocusTraversalPolicy {
	private final ResizingArray<Component> _COMPONENTS;

	public MyFocusTraversalPolicy() {
		_COMPONENTS = new ResizingArray<Component>();
	}

	public MyFocusTraversalPolicy(ResizingArray<Component> components) {
		if(components == null) {
			throw new RuntimeException("Variable[components] - Is Null");
		}

		_COMPONENTS = components;
	}

	public ResizingArray<Component> getComponents() {
		return _COMPONENTS;
	}

	@Override
	public Component getComponentAfter(Container container, Component component) {
		if(_COMPONENTS.length() == 1) {
			return _COMPONENTS.getAt(0);
		} else {
			int Index = getComponentIndex(component);
			if(Index != -1) {
				while(Index < _COMPONENTS.length()) {
					Index++;

					if(_COMPONENTS.validIndex(Index)) {
						if(_COMPONENTS.getAt(Index).isEnabled()) {
							return _COMPONENTS.getAt(Index);
						}
					}
				}

				return getDefaultComponent(container);
			}

			return null;
		}
	}

	@Override
	public Component getComponentBefore(Container container, Component component) {
		if(_COMPONENTS.length() == 1) {
			return _COMPONENTS.getAt(0);
		} else {
			int Index = getComponentIndex(component);

			if(Index != -1) {
				while(Index < _COMPONENTS.length()) {
					Index--;

					if(_COMPONENTS.validIndex(Index)) {
						if(_COMPONENTS.getAt(Index).isEnabled()) {
							return _COMPONENTS.getAt(Index);
						}
					}
				}

				return getDefaultComponent(container);
			}

			return null;
		}
	}

	@Override
	public Component getDefaultComponent(Container container) {
		return getFirstComponent(container);
	}

	@Override
	public Component getFirstComponent(Container container) {
		if(_COMPONENTS.length() >= 1) {
			return _COMPONENTS.getAt(0);
		} else {
			return null;
		}
	}

	@Override
	public Component getLastComponent(Container container) {
		if(_COMPONENTS.length() == 0) {
			return null;
		} else {
			return _COMPONENTS.getAt(_COMPONENTS.length() - 1);
		}
	}

	private int getComponentIndex(Component component) {
		if(component == null) {
			throw new RuntimeException("Variable[component] - Is Null");
		}

		for(int X = 0; X < _COMPONENTS.length(); X++) {
			if(component.equals(_COMPONENTS.getAt(X))) {
				return X;
			}
		}

		return -1;
	}
}
