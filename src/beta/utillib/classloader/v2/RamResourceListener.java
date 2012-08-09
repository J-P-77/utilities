package beta.utillib.classloader.v2;

import beta.utillib.classloader.ClassConstants;
import beta.utillib.classloader.v2.ram.RamURLConnection;

import utillib.collections.Collection;

import utillib.file.FileUtil;

import utillib.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.jar.Manifest;

public class RamResourceListener implements IClassloaderListener {
	private final Collection<String, byte[]> _CLASSES = new Collection<String, byte[]>();
	
	private static int _Counter_ = 0;
	
	private final int _ID;
	
	static {
		RamURLConnection.registerPackage();
	}
	
	public RamResourceListener() {
		_ID = _Counter_++;
		RamURLConnection.getListeners().add("RamId-" + _ID, _CLASSES);
	}
	
	public void addClass(String name, byte[] bytes) {
		if(FileUtil.isFileType(bytes, ClassConstants._CLASS_MAGIC_NUMBER_)) {
			_CLASSES.add(name + ClassConstants._CLASSS_EXT_, bytes);
		}
	}
	
	public void removeClass(String name) {
		_CLASSES.remove(name + ClassConstants._CLASSS_EXT_);
	}

	public void addResource(String name, byte[] bytes) {
		_CLASSES.add(name, bytes);
	}
	
	public void removeResource(String name) {
		_CLASSES.remove(name);
	}
	
	@Override
	public String getName() {
		return "RamId-" + _ID;
	}

	@Override
	public InputStream findClass(String name) {		
		final byte[] CLASS_BYTES = _CLASSES.get(name + ClassConstants._CLASSS_EXT_);
		
		if(CLASS_BYTES != null) {
			return new ByteArrayInputStream(CLASS_BYTES);
		}
		
		return null;
	}

	@Override
	public URL findResource(String name) {
		try {
			return new URL("ram", "RamId-" + _ID, "/" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		System.out.println(name);
		final byte[] CLASS_BYTES = _CLASSES.get(name);
		
		if(CLASS_BYTES != null) {
			return new ByteArrayInputStream(CLASS_BYTES);
		}
		
		return null;
	}

	@Override
	public boolean resourceExists(String name) {
		return (_CLASSES.get(name) != null);
	}
	
	@Override
	public Manifest getManifest() {
		final InputStream ISTREAM = getResourceAsStream(ClassConstants._MANIFEST_);
		
		if(ISTREAM != null) {
			try {
				return new Manifest(ISTREAM);
			} catch (Exception e) {}
		}
		
		return null;
	}
	
	@Override
	public void close() throws IOException {
		_CLASSES.removeAll();
	}
	
	@Override
	public boolean isClosed() {
		return false;
	}
	
	@Override
	protected void finalize() throws Throwable {
		RamURLConnection.getListeners().remove("RamId-" + _ID);
		
		super.finalize();
	}
}
