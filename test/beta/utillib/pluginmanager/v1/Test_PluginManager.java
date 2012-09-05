package beta.utillib.pluginmanager.v1;

import beta.utillib.classloader.v2.wrappers.ClassWrapper;
import beta.utillib.classloader.v2.wrappers.IMethodCall;
import beta.utillib.classloader.v2.wrappers.InstanceWrapper;
import beta.utillib.pluginmanager.v1.PluginManager;

public class Test_PluginManager {

	public static class Function implements IFunction {

		@Override
		public Object call(Object... objs) {
			return null;
		}
	}

	public static interface IFunction {
		public Object call(Object... objs);
	}

	public static class Plugin implements IPlugin {
		@Override
		public Object handleMsg(int msg, Object obj1, Object obj2) {
			return null;
		}

		@Override
		public String getPluginName() {
			return null;
		}
	}

	public interface IPlugin {
		public Object handleMsg(int msg, Object obj1, Object obj2);

		public String getPluginName();
	}

	public static void main(String[] args) {
		final PluginManager P_MANAGER = new PluginManager(IPlugin.class, true);

		P_MANAGER.loadClass(Plugin.class);
		//P_MANAGER.loadLibrary("");
		final InstanceWrapper WRAPPER = new InstanceWrapper(P_MANAGER.getPluginInfoAt(0).getPlugin());
		
		final IMethodCall M_handleMsg_int_Object_Object = WRAPPER.getMethodRWPC("handleMsg", Object.class, int.class, Object.class, Object.class);
		M_handleMsg_int_Object_Object.call(1, "", null);
		
		final ClassWrapper C_WRAPPER = new ClassWrapper(P_MANAGER.getPluginInfoAt(0).getPluginClass());
		
		C_WRAPPER.newInstance();
	}
}
