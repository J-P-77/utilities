package test.beta.utillib.classloader.v2.wrappers;

import utillib.strings.MyStringBuffer;

import beta.utillib.classloader.v2.wrappers.IClassMethodCall;
import beta.utillib.classloader.v2.wrappers.ClassWrapper;

public class Test_ClassWrapper {
	
	public static class TestClass {
		private String _Name = null;
		
		public TestClass() {
			this("");
		}
		
		public TestClass(String name) {
			setName(name);
		}
		
		public TestClass(int value) {
			setName(value);
		}
		
		public TestClass(String name, int value) {
			setName(name, value);
		}
		
		public void setName(String name) {
			_Name = name;
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
		ClassWrapper Wrapper = null;
		try {
			Wrapper = new ClassWrapper(TestClass.class);
			
			Object Instance1 = Wrapper.newInstance();
			Object Instance2 = Wrapper.newInstance("Justin");
			
			if(Instance1 == null) {
				System.out.println("Instance1 Is Null");
				return;
			}
			
			if(Instance2 == null) {
				System.out.println("Instance2 Is Null");
				return;
			}
			
			final IClassMethodCall _TESTCLASS_setName_String = Wrapper.getMethodWPC("setName", void.class, String.class);
			final IClassMethodCall _TESTCLASS_setName_int = Wrapper.getMethodWPC("setName", void.class, int.class);
			final IClassMethodCall _TESTCLASS_setName_String_int = Wrapper.getMethodWPC("setName", void.class, java.lang.String.class, int.class);
			final IClassMethodCall _TESTCLASS_getName = Wrapper.getMethodWPC("getName", String.class);
			
			System.out.println("=" + (String)_TESTCLASS_getName.call(Instance1));
			System.out.println("Justin=" + (String)_TESTCLASS_getName.call(Instance2));
			
////////////////////////////////////////////////////////////////
			_TESTCLASS_setName_String.call(Instance1, "Justin");
			_TESTCLASS_setName_String.call(Instance2, "Palinkas");
			
			System.out.println("Justin=" + (String)_TESTCLASS_getName.call(Instance1));
			System.out.println("Palinkas=" + (String)_TESTCLASS_getName.call(Instance2));
			
////////////////////////////////////////////////////////////////
			_TESTCLASS_setName_int.call(Instance1, 1);
			_TESTCLASS_setName_int.call(Instance2, 2);
			
			System.out.println("1=" + (String)_TESTCLASS_getName.call(Instance1));
			System.out.println("2=" + (String)_TESTCLASS_getName.call(Instance2));
			
////////////////////////////////////////////////////////////////
			_TESTCLASS_setName_String_int.call(Instance1, "Justin", 1);
			_TESTCLASS_setName_String_int.call(Instance2, "Palinkas", 2);
			
			System.out.println("Justin1=" + (String)_TESTCLASS_getName.call(Instance1));
			System.out.println("Palinkas2=" + (String)_TESTCLASS_getName.call(Instance2));
			
			final String[][] METHOD_CLASSES = Wrapper.getMethodArgClassNames("setName");
			
			System.out.println("Method: setName");
			for(final String[] X : METHOD_CLASSES) {
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