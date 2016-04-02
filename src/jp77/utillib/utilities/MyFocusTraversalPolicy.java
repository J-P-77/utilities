/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.utilities;

import jp77.utillib.arrays.ResizingArray;

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
