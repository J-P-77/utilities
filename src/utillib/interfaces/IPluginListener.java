/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.interfaces;

import beta.utillib.pluginmanager.v1.IPluginInfo;

/**
 * 
 * @author Justin Palinkas
 */
public interface IPluginListener {
// public static final int _EVENT_PLUGIN_ADDED_ = 0;
// public static final int _EVENT_PLUGIN_REMOVED_ = 1;
// public static final int _EVENT_PLUGIN_ENABLE_ = 2;
// public static final int _EVENT_PLUGIN_DISABLED_ = 3;
// // public static final int _EVENT_PLUGIN_BLACKLISTED_CLASS_ADDED_ = 4;
// // public static final int _EVENT_PLUGIN_BLACKLISTED_CLASS_REMOVED = 5;

	public enum Event {
		PLUGIN_ADDED,
		PLUGIN_REMOVED,
		PLUGIN_ENABLE,
		PLUGIN_DISABLED;
// PLUGIN_ADDED_BLACKLISTED,
// PLUGIN_REMOVED_BLACKLISTED;

		// [Plugin Event] - Plugin Added
		// [Plugin Event] - Plugin Unloaded (Removed)
		// [Plugin Event] - Plugin Enabled (Enabled or Disabled)
		// [Plugin Event] - Plugin Added to BlackListed
		// [Plugin Event] - Plugin Removed from BlackListed
	};

	/**
	 * 
	 * @param event
	 *            Event Id That Has Happened
	 * @param plugininfo
	 *            Will Be PluginInfo
	 * @param obj
	 *            Additional Information (Optional)
	 * 
	 */
// @Deprecated
	public void handleEvent(Event event, IPluginInfo plugininfo);

// public void handleEvent(PluginEvent event);
}
