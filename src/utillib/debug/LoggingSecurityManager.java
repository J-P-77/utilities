package utillib.debug;

import java.io.FileDescriptor;

import java.net.InetAddress;
import java.security.Permission;

public class LoggingSecurityManager extends SecurityManager {
	private static final DebugLogger _LOG = LogManager.getInstance().getLogger(LoggingSecurityManager.class);
	
	public LoggingSecurityManager() {}
	
	public void checkCreateClassLoader() {
		_LOG.printInformation("Create ClassLoader");
	}
	
	@Override
	public void checkAccess(Thread g) {
		_LOG.printInformation("Thread Access: " + g.getName());
	}
	
	@Override
	public void checkAccess(ThreadGroup g) {
		_LOG.printInformation("ThreadGroup Access: " + g.getName());
	}
	
	@Override
	public void checkExit(int status) {
		_LOG.printInformation("Exit Status: " + status);
	}
	
	@Override
	public void checkExec(String cmd) {
		_LOG.printInformation("Exec: " + cmd);
	}
	
	@Override
	public void checkLink(String lib) {
		_LOG.printInformation("Link: " + lib);
	}
	
	@Override
	public void checkRead(FileDescriptor fd) {
		_LOG.printInformation("Read: " + fd);
	}
	
	@Override
	public void checkRead(String file) {
		_LOG.printInformation("Read File: " + file);
	}
	
	@Override
	public void checkRead(String file, Object context) {
		_LOG.printInformation("Read File: " + file + " - " + context.toString());
	}
	
	@Override
	public void checkWrite(FileDescriptor fd) {
		_LOG.printInformation("Write: " + fd);
	}
	
	@Override
	public void checkWrite(String file) {
		_LOG.printInformation("Write File: " + file);
	}
	
	@Override
	public void checkDelete(String file) {
		_LOG.printInformation("Delete File: " + file);
	}
	
	@Override
	public void checkConnect(String host, int port) {
		_LOG.printInformation("Connect: " + host + ':' + port);
	}
	
	@Override
	public void checkConnect(String host, int port, Object context) {
		_LOG.printInformation("Connect: " + host + ':' + port + " - " + context.toString());
	}
	
	@Override
	public void checkListen(int port) {
		_LOG.printInformation("Listen: " + port);
	}
	
	@Override
	public void checkAccept(String host, int port) {
		_LOG.printInformation("Accept: " + host + ':' + port);
	}
	
	@Override
	public void checkMulticast(InetAddress maddr) {
		_LOG.printInformation("Multicast: " + maddr.toString());
	}
	
	@Override
	public void checkMulticast(InetAddress maddr, byte ttl) {
		_LOG.printInformation("Multicast: " + maddr.toString() + " ttl: " + ttl);
	}
	
	@Override
	public void checkPropertiesAccess() {
		_LOG.printInformation("Properties Access");
	}
	
	@Override
	public void checkPropertyAccess(String key) {
		_LOG.printInformation("Property Access: " + key);
	}
	
//	public void checkPropertyAccess(String key, String def) {}
	
	@Override
	public boolean checkTopLevelWindow(Object window) {
		return true;
	}
	
	@Override
	public void checkPrintJobAccess() {
		_LOG.printInformation("Print Job Access");
	}
	
	@Override
	public void checkSystemClipboardAccess() {
		_LOG.printInformation("System Clipboard Access");
	}
	
	@Override
	public void checkAwtEventQueueAccess() {
		_LOG.printInformation("Awt Event Queue Access");
	}
	
	@Override
	public void checkPackageAccess(String pkg) {
		_LOG.printInformation("Package Access: " + pkg);
	}
	
	@Override
	public void checkPackageDefinition(String pkg) {
		_LOG.printInformation("Package Definition: " + pkg);
	}
	
	@Override
	public void checkSetFactory() {
		_LOG.printInformation("Set Factory");
	}
	
	@Override
	public void checkMemberAccess(Class<?> clazz, int which) {
		_LOG.printInformation("Member Access: " + clazz.getCanonicalName() + " - " + which);
	}
	
	@Override
	public void checkSecurityAccess(String provider) {
		_LOG.printInformation("Security Access: " + provider);
	}
	
	public void checkPermission(Permission perm) {
		_LOG.printInformation("Permission: " + perm.toString());
	}
}