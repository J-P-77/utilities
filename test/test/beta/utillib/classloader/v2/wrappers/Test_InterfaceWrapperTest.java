package test.beta.utillib.classloader.v2.wrappers;

import beta.utillib.classloader.v2.wrappers.IClassMethodCall;
import beta.utillib.classloader.v2.wrappers.InterfaceWrapper;

import utillib.strings.MyStringBuffer;

public class Test_InterfaceWrapperTest {
	private static interface TestInterface {
		public void setName(String value);
		public void setName(int value);
		public void setName(String name, int value);
		public String getName();
	}
	
	public static class InterfaceTest1 implements TestInterface {
		private String _Name = null;
		
		@Override
		public void setName(String value) {
			_Name = value;
		}
		
		@Override
		public void setName(int value) {
			_Name = Integer.toString(value);
		}
		
		@Override
		public void setName(String name, int value) {
			_Name = name + Integer.toString(value);
		}
		
		@Override
		public String getName() {
			return _Name;
		}
	}
	
	public static class InterfaceTest2 {
		private String _Name = null;
		
		public void setName(String value) {
			_Name = value;
		}
		
		public void setName(int value) {
			_Name = Integer.toString(value);
		}
		
		public void setName(String name, int value) {
			_Name = name + Integer.toString(value);
		}
		
		public String getName() {
			return _Name;
		}
	}
	
	public static void main(String[] args) {
		InterfaceWrapper Wrapper = null;
		try {
			Wrapper = new InterfaceWrapper(TestInterface.class);
			
			Wrapper.addClass(InterfaceTest1.class);
			Wrapper.addClass(InterfaceTest2.class.getName());
			
			Object Instance1 = Wrapper.newInstance(InterfaceTest1.class.getName());
			Object Instance2 = Wrapper.newInstance(InterfaceTest2.class.getName());
			
			if(Instance1 == null) {
				System.out.println("Instance1 Is Null");
				return;
			}
			
			if(Instance2 == null) {
				System.out.println("Instance2 Is Null");
				return;
			}
			
			final IClassMethodCall _TESTINTERFACE_setName_String = Wrapper.getMethodWPC("setName", void.class, String.class);
			final IClassMethodCall _TESTINTERFACE_setName_int = Wrapper.getMethodWPC("setName", void.class, int.class);
			final IClassMethodCall _TESTINTERFACE_setName_String_int = Wrapper.getMethodWPC("setName", void.class, java.lang.String.class, int.class);
			final IClassMethodCall _TESTINTERFACE_getName = Wrapper.getMethodWPC("getName", String.class);
			
////////////////////////////////////////////////////////////////
			_TESTINTERFACE_setName_String.call(Instance1, "Justin");
			_TESTINTERFACE_setName_String.call(Instance2, "Palinkas");
			
			System.out.println("Justin=" + (String)_TESTINTERFACE_getName.call(Instance1));
			System.out.println("Palinkas=" + (String)_TESTINTERFACE_getName.call(Instance2));
			
////////////////////////////////////////////////////////////////
			_TESTINTERFACE_setName_int.call(Instance1, 1);
			_TESTINTERFACE_setName_int.call(Instance2, 2);
			
			System.out.println("1=" + (String)_TESTINTERFACE_getName.call(Instance1));
			System.out.println("2=" + (String)_TESTINTERFACE_getName.call(Instance2));
			
////////////////////////////////////////////////////////////////
			_TESTINTERFACE_setName_String_int.call(Instance1, "Justin", 1);
			_TESTINTERFACE_setName_String_int.call(Instance2, "Palinkas", 2);
			
			System.out.println("Justin1=" + (String)_TESTINTERFACE_getName.call(Instance1));
			System.out.println("Palinkas2=" + (String)_TESTINTERFACE_getName.call(Instance2));
			
			final String[][] M_CLASSES_setName = Wrapper.getMethodArgClassNames("setName");
			
			System.out.println("Method: setName");
			for(final String[] X : M_CLASSES_setName) {
				final MyStringBuffer BUFFER = new MyStringBuffer("    " + "setName(");
				BUFFER.storeLength();
				for(final String Y : X) {
					BUFFER.append(Y);
					BUFFER.append(", ");
				}
				if(BUFFER.length() > BUFFER.getStoredLength()) {
					BUFFER.reset(BUFFER.length() - 2);
				}
				BUFFER.append(')');
				System.out.println(BUFFER.toString());
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
