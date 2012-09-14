package utillib.utilities;

import utillib.utilities.SystemProperties.Property;

/**
 * 
 * @author Dalton Dell
 */
public interface User {
	public static final int _SYSTEM_EXIT_NORMAL_ = 0;
	public static final int _SYSTEM_EXIT_ERROR_ = 1;

	public static final String _APP_PATH_ = SystemProperties.getProperty(Property.APP_PATH);

	public static final String _JAVA_HOME_ = SystemProperties.getProperty(Property.JAVA_HOME);
	public static final String _JAVA_VENDOR_ = SystemProperties.getProperty(Property.JAVA_VENDOR);
	public static final String _JAVA_VENDOR_URL_ = SystemProperties.getProperty(Property.JAVA_VENDOR_URL);
	public static final String _JAVA_VERSION_ = SystemProperties.getProperty(Property.JAVA_VERSION);
	public static final String _JAVA_CLASS_PATH_ = SystemProperties.getProperty(Property.JAVA_CLASS_PATH);

	public static final String _OS_ARCH_ = SystemProperties.getProperty(Property.OS_ARCH);
	public static final String _OS_NAME_ = SystemProperties.getProperty(Property.OS_NAME);
	public static final String _OS_VERSION_ = SystemProperties.getProperty(Property.OS_VERSION);

	public static final String _USER_HOME_ = SystemProperties.getProperty(Property.USER_HOME);
	public static final String _USER_NAME_ = SystemProperties.getProperty(Property.USER_NAME);
}
