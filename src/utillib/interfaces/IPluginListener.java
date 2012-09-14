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
