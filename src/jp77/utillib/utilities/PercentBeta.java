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

import jp77.beta.utillib.DefaultPropertyEvent;
import jp77.beta.utillib.IPropertyChanged;
import jp77.beta.utillib.Listener;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * May 02, 2010 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class PercentBeta {
	public static final int _PROPERTY_ID_TOTAL_ = 0;
	public static final int _PROPERTY_ID_COMPLETED_ = 1;
	public static final int _PROPERTY_ID_PERCENT_ = 2;

	private final Listener<IPropertyChanged> _LISTENERS = new Listener<IPropertyChanged>();

	private double _Previous = 0;

	private long _Total = 0l;
	private long _TotalCompleted = 0l;

	public PercentBeta() {
		this(0);
	}

	public PercentBeta(int total) {
		_Total = (long)total;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public PercentBeta(long total) {
		_Total = total;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public void resetAll() {
		_Total = 0;
		_TotalCompleted = 0;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, 0);
		fireChangedEvent(_PROPERTY_ID_COMPLETED_, 0);
	}

	public void resetTotal() {
		_Total = 0;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, 0);
	}

	public void resetCompleted() {
		_TotalCompleted = 0;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, 0);
	}

	public void setTotal(long value) {
		if(_Total != value) {
			fireChangedEvent(_PROPERTY_ID_TOTAL_, value);
		}

		_Total = value;
	}

	public long getTotal() {
		return _Total;
	}

	public void addTotal() {
		addTotal(1);
	}

	public void addTotal(int value) {
		_Total += value;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public void addTotal(long value) {
		_Total += value;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public void subTotal() {
		subTotal(1);
	}

	public void subTotal(int value) {
		_Total -= value;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public void subTotal(long value) {
		_Total -= value;

		fireChangedEvent(_PROPERTY_ID_TOTAL_, _Total);
	}

	public void setCompleted(long value) {
		_TotalCompleted = value;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, _TotalCompleted);
	}

	public long getCompleted() {
		return _TotalCompleted;
	}

	public void addCompleted() {
		addCompleted(1);
	}

	public void addCompleted(int value) {
		_TotalCompleted += value;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, _TotalCompleted);
	}

	public void addCompleted(long value) {
		_TotalCompleted += value;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, _TotalCompleted);
	}

	public void subCompleted() {
		subCompleted(1);
	}

	public void subCompleted(int value) {
		_TotalCompleted -= value;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, _TotalCompleted);
	}

	public void subCompleted(long value) {
		_TotalCompleted -= value;

		fireChangedEvent(_PROPERTY_ID_COMPLETED_, _TotalCompleted);
	}

	public String getStringPercent() {
		return getIntPercent() + "%";
	}

	public int getIntPercent() {
		final double PERCENT = getDblPercent();
		final double DBL_PERCENT = (PERCENT * 100);

		final int LNG_PERCENT = (int)DBL_PERCENT;

		return (LNG_PERCENT);
	}

	public double getDblPercent() {// * 100 To Get Percent
		if(_Total == 0 || _TotalCompleted == 0) {
			return 0;
		} else {
			final double TOTAL_COMPLETE = _TotalCompleted;
			final double PRECENT = (TOTAL_COMPLETE / _Total);

			return PRECENT;
		}
	}

	public Listener<IPropertyChanged> getPropertyListener() {
		return _LISTENERS;
	}

	private void fireChangedEvent(int propertyid, long value) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			_LISTENERS.getListenerAt(X).propertyChanged(DefaultPropertyEvent.eventChanged(this, propertyid, value));
		}

		final double CURRENT = getDblPercent();
		if(CURRENT != _Previous) {
			fireChangedEvent(_PROPERTY_ID_PERCENT_, CURRENT);
			_Previous = CURRENT;
		}
	}

	private void fireChangedEvent(int propertyid, double value) {
		for(int X = 0; X < _LISTENERS.listenerCount(); X++) {
			_LISTENERS.getListenerAt(X).propertyChanged(DefaultPropertyEvent.eventChanged(this, propertyid, value));
		}
	}
}
