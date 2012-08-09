package beta.utillib.classloader;

import utillib.file.FileUtil;

public interface ClassConstants {
	public static final byte[] _CLASS_MAGIC_NUMBER_ = {-54, -2, -70, -66};
	
	public static final String _CLASSS_EXT_ = ".class";
	
	public static final String _MANIFEST_ = "META-INF" + FileUtil._LINUX_c +  "MANIFEST.MF";
}
