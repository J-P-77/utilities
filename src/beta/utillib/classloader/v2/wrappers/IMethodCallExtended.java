package beta.utillib.classloader.v2.wrappers;

/**
 * 
 * @author Dalton Dell
 */
public interface IMethodCallExtended {
	public String getMethodName();

	public Class<?>[] getArgumentClasses();

	public Class<?> getReturnType();
}
