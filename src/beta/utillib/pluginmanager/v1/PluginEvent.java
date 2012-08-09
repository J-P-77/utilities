package beta.utillib.pluginmanager.v1;

/**
 *
 * @author Dalton Dell
 */
public class PluginEvent {
    private final int _EVENT;
    private final IPluginInfo _PLUGININFO;

    public PluginEvent(int event, IPluginInfo plugininfo) {
        _EVENT = event;
        _PLUGININFO = plugininfo;
    }

    public int getEvent() {
        return _EVENT;
    }

    public IPluginInfo getPluginInfo() {
        return _PLUGININFO;
    }
}
