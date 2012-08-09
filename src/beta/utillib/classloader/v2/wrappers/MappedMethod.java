package beta.utillib.classloader.v2.wrappers;

/**
 *
 * @author Dalton Dell
 */
public class MappedMethod implements IMethodCall {
    private Object _Instance = 0;
    private IClassMethodCall _Method = null;

    public MappedMethod(Object instance, IClassMethodCall method) {
        _Instance = instance;
        _Method = method;
    }

    @Override
    public Object call(Object... args) {
        return _Method.call(_Instance, args);
    }

    public void destroy() {
        _Instance = null;
    }
}
