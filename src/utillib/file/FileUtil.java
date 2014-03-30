/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package utillib.file;

import utillib.utilities.Java;
import utillib.utilities.Os;
import utillib.utilities.User;
import utillib.utilities.SystemProperties;

import utillib.arrays.ResizingArray;

import utillib.debug.DebugLogger;
import utillib.debug.LogManager;

import utillib.strings.MyStringBuffer;

import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <pre>
 * <b>Current Version 1.0.0</b>
 * 
 * November 02, 2008 (Version 1.0.0)
 *     -First Released
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class FileUtil implements User {
	private static final DebugLogger _LOG_ = LogManager.getInstance().getLogger(FileUtil.class);

	public static enum Access {
		READ,
		WRITE,
		READ_WRITE;
	};

	public static enum Drive_FileSystem {
		FAT,
		FAT32,
		NTFS,
		HFS,
		EXT3,
		EXT4,
		UNKNOWN;
	};

	public static enum Random_Access_Mode {
		/*
		 * Open for reading only. Invoking any of the write methods of the
		 * resulting object will cause an IOException to be thrown.
		 */
		READ_ONLY("r"),

		/*
		 * Open for reading and writing. If the file does not already exist then
		 * an attempt will be made to create it.
		 */
		READ_WRITE("rw"),

		/*
		 * Open for reading and writing, as with \"rw\", and also require that
		 * every update to the file's content or metadata be written
		 * synchronously to the underlying storage device.
		 */
		READ_WRITE_S("rws"),

		/*
		 * Open for reading and writing, as with \"rw\", and also require that
		 * every update to the file's content be written synchronously to the
		 * underlying storage device.
		 */
		READ_WRITE_D("rwd");

		private final String _Value;

//        private final String _Meaning;
		private Random_Access_Mode(String value/* , String meaning */) {
			_Value = value;
//            _Meaning = meaning;
		}

		public String getValue() {
			return _Value;
		}
//        public String getMeaning() {return _Meaning;}
	};

	public static enum Line_Ending {
		LINUX(_LINUX_NEWLINE_),
		MAC(_MAC_NEWLINE_),
		WINDOWS(_WINDOWS_NEWLINE_);

		private final String _Value;

		private Line_Ending(String value) {
			_Value = value;
		}

		public String getValue() {
			return _Value;
		}

		public byte[] getBytes() {
			return _Value.getBytes();
		}

		private static Line_Ending _DefaultLineEnding = null;

		public static Line_Ending getDefaultLineEnding() {
			if(_DefaultLineEnding == null) {
				switch(Os.getOS()) {
					case WIN_98:
					case WIN_2000:
					case WIN_XP:
					case WIN_VISTA:
					case WIN_7:
						_DefaultLineEnding = WINDOWS;
						break;

					case UNIX:
					case LINUX:
					case MAC:
					case UNKNOWN:
					default:
						_DefaultLineEnding = LINUX;
						break;
				}
			}

			return _DefaultLineEnding;
		}
	};

	public static enum File_Separator {
		LINUX(_LINUX_),
		MAC(_MAC_),
		WIN(_WIN_);

		private final String _Value;

		private File_Separator(String value) {
			_Value = value;
		}

		public String getValue() {
			return _Value;
		}

		public byte[] getBytes() {
			return _Value.getBytes();
		}

		private static File_Separator _FileSystemFileSeparator = null;

		public static File_Separator getFileSystemFileSeparator() {
			if(_FileSystemFileSeparator == null) {
				for(File_Separator sep : File_Separator.values()) {
					if(_S_.equals(sep.getValue())) {
						_FileSystemFileSeparator = sep;
						break;
					}
				}
			}

			return _FileSystemFileSeparator;
		}
	}

	public static final String _S_ = SystemProperties.getProperty(SystemProperties.Property.FILE_SEPARATOR);

	public static final char _S_c = File.separatorChar;

	public static final char _LINUX_c = '/';
	public static final char _MAC_c = _LINUX_c;
	public static final char _WIN_c = '\\';

	public static final String _LINUX_ = "/";
	public static final String _MAC_ = _LINUX_;
	public static final String _WIN_ = "\\";

	public static final char _CR_ = '\r';
	public static final char _LF_ = '\n';

	public static final String _LINUX_NEWLINE_ = "\n";
	public static final String _MAC_NEWLINE_ = "\r";
	public static final String _WINDOWS_NEWLINE_ = "\r\n";

	public static final int _WIN_MAX_PATH_LENGTH_ = 256;

	private static String _Home_Directory = _APP_PATH_;

	public static String getHomeDirectory() {
		return _Home_Directory;
	}

	public static void setHomeDirectory(String dirpath) {
		setHomeDirectory(new File(dirpath));
	}

	public static void setHomeDirectory(File directory) {
		if(directory == null) {
			throw new RuntimeException("Variable[directory] - Is Null");
		}

		final File FILE = directory.getAbsoluteFile();

		if(FILE.isFile()) {
			throw new RuntimeException(FILE.getPath() + " Is Not A Directory");
		}

		if(!FILE.exists()) {
			throw new RuntimeException(FILE.getPath() + " Does Not Exists");
		}

		_Home_Directory = FILE.getPath();
	}

	public static File getHomeFile(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return new File(combineTwoPaths(getHomeDirectory(), path));
	}

	public static String getHomePath(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return combineTwoPaths(getHomeDirectory(), path);
	}

	/**
	 * 
	 * @param If
	 *            path2 Endswith File Separator It Will Be Removed
	 * @return path1 Plus path2 Combined
	 */
	public static String combineTwoPaths(String path1, String path2) {
		return combineTwoPaths(path1, path2, _S_);
	}

	/**
	 * 
	 * @param If
	 *            path2 Endswith File Separator It Will Be Removed
	 * @return path1 Plus path2 Combined
	 */
	public static String combineTwoPaths(String path1, String path2, String separator) {
		if(path1 == null || path1.length() == 0 || path2 == null || path2.length() == 0 || separator == null) {
			return null;
		}

		final MyStringBuffer BUFFER = new MyStringBuffer(path1.length() + path2.length() + +(separator.length() * 2));

		final boolean PATH1_ENDSWITH = path1.endsWith(separator);
		final boolean PATH2_STARTSWITH = path2.startsWith(separator);
		final boolean PATH2_ENDSWITH = path2.endsWith(separator);

		BUFFER.append(path1);

		if(!PATH1_ENDSWITH && !PATH2_STARTSWITH) {
			BUFFER.append(separator);
		}

		final int OFFSET = (PATH1_ENDSWITH && PATH2_STARTSWITH ? 1 : 0);
		final int LEN = (PATH2_ENDSWITH ? (path2.length() - 1) : path2.length());
		for(int X = OFFSET; X < LEN; X++) {
			BUFFER.append(path2.charAt(X));
		}

		return BUFFER.toString();
	}

	public static String combinePaths(String... paths) {
		return combinePaths(File_Separator.getFileSystemFileSeparator(), paths);
	}

	public static String combinePaths(File_Separator separator, String... paths) {
		if(separator == null) {
			throw new RuntimeException("Variable[separator] - Is Null");
		}

		if(paths == null) {
			throw new RuntimeException("Variable[paths] - Is Null");
		}

		final MyStringBuffer BUFFER = new MyStringBuffer(256);

		for(int X = 0; X < paths.length; X++) {
			if(paths[X] == null || paths[X].length() == 0) {
				continue;
			}

			final boolean PATH1_ENDSWITH = BUFFER.endsWith(FileUtil._WIN_) || BUFFER.endsWith(FileUtil._LINUX_);
			final boolean PATH2_STARTSWITH = paths[X].startsWith(FileUtil._WIN_) || paths[X].startsWith(FileUtil._LINUX_);
			final boolean PATH2_ENDSWITH = paths[X].endsWith(FileUtil._WIN_) || paths[X].endsWith(FileUtil._LINUX_);

			if(!PATH1_ENDSWITH && !PATH2_STARTSWITH) {
//	        	if(X == paths.length - 1) {
//	        		if(!paths[X].startsWith(".")) {
//	        			BUFFER.append(separator.getValue());
//	        		}
//	        	} else {
				BUFFER.append(separator.getValue());
//	        	}
			}

			final int OFFSET = (PATH1_ENDSWITH && PATH2_STARTSWITH ? 1 : 0);
			final int LEN = (PATH2_ENDSWITH ? (paths[X].length() - 1) : paths[X].length());
			for(int Y = OFFSET; Y < LEN; Y++) {
				if(paths[X].charAt(Y) == _WIN_c || paths[X].charAt(Y) == _LINUX_c) {
					BUFFER.append(separator.getValue());
				} else {
					BUFFER.append(paths[X].charAt(Y));
				}
			}
		}

		if(BUFFER.length() > 0) {
			if(BUFFER.charAt(0) == separator.getValue().charAt(0)) {//Only Applies To Windows, Linux Starts With File Separator (Ignore This Comment For Now)
				return BUFFER.getSubString(1);
			}
		}

		return BUFFER.toString();
	}

	public static String combineTwoPaths(String path1, String path2, File_Separator separator) {
		if(path1 == null || path1.length() == 0 || path2 == null || path2.length() == 0 || separator == null) {
			return null;
		}

		final MyStringBuffer BUFFER = new MyStringBuffer(path1.length() + path2.length() + +(separator.getValue().length() * 2));

		final boolean PATH1_ENDSWITH = path1.endsWith(File_Separator.LINUX.getValue()) || path1.endsWith(File_Separator.WIN.getValue());
		final boolean PATH2_STARTSWITH = path2.startsWith(File_Separator.LINUX.getValue()) || path2.startsWith(File_Separator.WIN.getValue());
		final boolean PATH2_ENDSWITH = path2.endsWith(File_Separator.LINUX.getValue()) || path2.endsWith(File_Separator.WIN.getValue());

		appendPath(path1, 0, path1.length(), separator, BUFFER);

		if(!PATH1_ENDSWITH && !PATH2_STARTSWITH) {
			BUFFER.append(separator.getValue());
		}

		final int OFFSET = (PATH1_ENDSWITH && PATH2_STARTSWITH ? 1 : 0);
		final int LEN = (PATH2_ENDSWITH ? (path2.length() - 1) : path2.length());

		appendPath(path2, OFFSET, LEN, separator, BUFFER);

		return BUFFER.toString();
	}

	private static void appendPath(String path, int offset, int length, File_Separator separator, MyStringBuffer outbuffer) {
		boolean SkipSeparator = false;
		for(int X = offset; X < length; X++) {
			if(separator == File_Separator.LINUX || separator == File_Separator.MAC) {
				if(path.charAt(X) == _WIN_c) {
					if(!SkipSeparator) {
						outbuffer.append(_LINUX_c);
						SkipSeparator = true;
						continue;
					}
				}
			} else {
				if(path.charAt(X) == _LINUX_c) {
					if(!SkipSeparator) {
						outbuffer.append(_WIN_c);
						SkipSeparator = true;
						continue;
					}
				}
			}

			outbuffer.append(path.charAt(X));
			SkipSeparator = false;
		}
	}

	/**
	 * 
	 * @param drive
	 *            If Endswith File Separator It Will Be Removed
	 * @return Application Path Plus path Combined
	 */
	@Deprecated
	public static String createAppPath(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return combineTwoPaths(_APP_PATH_, path);
	}

	public static String removeFileSepAtEnd(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		if(path.endsWith(FileUtil._S_)) {
			return path.substring(0, path.length() - FileUtil._S_.length());
		} else {
			return path;
		}
	}

	public static File getDriveBySystemDisplayName(String name) {
		return getDriveBySystemDisplayName(name, true);
	}

	public static File getDriveBySystemDisplayName(String name, boolean matchcase) {
		final String NAME = (matchcase ? name : name.toLowerCase());

		final FileSystemView FSV = FileSystemView.getFileSystemView();

		final File[] DRIVES = File.listRoots();
		for(int X = 0; X < DRIVES.length; X++) {
			final String T_NAME = (matchcase ? FSV.getSystemDisplayName(DRIVES[X]) : FSV.getSystemDisplayName(DRIVES[X]).toLowerCase());

			if(T_NAME.startsWith(NAME)) {
				return DRIVES[X];
			}
		}

		return null;
	}

	public static String[] getSystemDisplayNames() {
		final File[] DRIVES = File.listRoots();

		final ResizingArray<String> RESULTS = new ResizingArray<String>(DRIVES.length);

		for(int X = 0; X < DRIVES.length; X++) {
			if(DRIVES[X].exists()) {
				RESULTS.put(getSystemDisplayName(DRIVES[X]));
			}
		}

		return RESULTS.toArray(new String[RESULTS.length()]);
	}

	public static String getSystemDisplayName(String drive) {
		return getSystemDisplayName(new File(drive));
	}

	public static String getSystemDisplayName(File drive) {
		return FileSystemView.getFileSystemView().getSystemDisplayName(drive);
	}

	public static String getSystemTypeDescription(String path) {
		return getSystemTypeDescription(new File(path));
	}

	public static String getSystemTypeDescription(File file) {
		return FileSystemView.getFileSystemView().getSystemTypeDescription(file);
	}

	public static boolean isDrive(File drive) {
		return isDrive(drive.getPath());
	}

	public static boolean isDrive(String drive) {
		if(drive == null) {
			throw new RuntimeException("Variable[drive] - Is Null");
		}

		if(drive.length() > 0) {
			final File[] DRIVES = File.listRoots();

			for(int X = 0; X < DRIVES.length; X++) {
				if(drive.equals(DRIVES[X].getPath())) {
					return true;
				}
			}
		}

		return false;
	}

	public static File getDriveFromPath(File file) {
		return getDriveFromPath(file.getAbsolutePath());
	}

	public static File getDriveFromPath(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		final String PATH = (Os.isWindows() ? path.toLowerCase() : path);

		if(path.length() > 0) {
			final File[] DRIVES = File.listRoots();

			for(int X = 0; X < DRIVES.length; X++) {
				final String DRIVE = (Os.isWindows() ? DRIVES[X].getPath().toLowerCase() : DRIVES[X].getPath());

				if(PATH.startsWith(DRIVE)) {
					return DRIVES[X];
				}
			}
		}

		return null;
	}

	//java7:
	public static Drive_FileSystem getDriveFileSystemType(File drive) {
		if(Java._IS_JAVA7_) {
			try {
				final FileStore FSTORE = Files.getFileStore(Paths.get(drive.getPath()));

				if(FSTORE.type().equalsIgnoreCase("fat32")) {
					return Drive_FileSystem.FAT32;
				} else if(FSTORE.type().equalsIgnoreCase("fat")) {
					return Drive_FileSystem.FAT;
				} else if(FSTORE.type().equalsIgnoreCase("ntfs")) {
					return Drive_FileSystem.NTFS;
				} else if(FSTORE.type().equalsIgnoreCase("ntfs")) {
					return Drive_FileSystem.NTFS;
				} else if(FSTORE.type().equalsIgnoreCase("hfs")) {
					return Drive_FileSystem.HFS;
				} else if(FSTORE.type().equalsIgnoreCase("ext3")) {
					return Drive_FileSystem.EXT3;
				} else if(FSTORE.type().equalsIgnoreCase("ext4")) {
					return Drive_FileSystem.EXT4;
				} else {
					return Drive_FileSystem.UNKNOWN;
				}
			} catch(Exception e) {}
		} else {
			return Drive_FileSystem.UNKNOWN;
		}

		return null;
	}

	public static String removeDirectory(String remove, String path) {
		return removeDirectory(remove.length(), path);
	}

	public static String removeDirectory(int offset, String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		if(offset <= 0) {
			return path;
		}

//        final MyStringBuffer BUFFER = new MyStringBuffer(path.length() - offset);
//
//        for(int X = offset; X < path.length(); X++) {
//            BUFFER.append(path.charAt(X));
//        }

		return path.substring(offset);
	}

	public static String removeDirectory(File remove, File path) {
		return removeDirectory(remove.getAbsolutePath().length(), path);
	}

	public static String removeDirectory(int offset, File path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		if(offset <= 0) {
			return path.getAbsolutePath();
		}

		final String PATH = path.getAbsolutePath();

//        MyStringBuffer BUFFER = new MyStringBuffer(PATH.length() - offset);
//
//        for(int X = offset; X < PATH.length(); X++) {
//            BUFFER.append(PATH.charAt(X));
//        }

		return PATH.substring(offset);
	}

	public static File getAppPath(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return new File(createAppPath(path));
	}

	public static File getAbsoluteFile(String path) {
		if(path == null) {
			throw new RuntimeException("Variable[path] - Is Null");
		}

		return new File(path).getAbsoluteFile();
	}

	public static File getAbsoluteFile(File file) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		return file.getAbsoluteFile();
	}

	public static boolean pathExists(String path) {
		if(Java._IS_JAVA7_) {
			return Files.exists(Paths.get(path));
		} else {
			return pathExists(new File(path));
		}
	}

	public static boolean pathExists(File file) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		return file.exists();
	}

	public static boolean deletePath(String path) {
		return deletePath(new File(path));
	}

	public static boolean deletePath(File file) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(file.exists()) {
			return file.delete();
		}

		return false;
	}

	public static void deletePathOnExit(File file) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(file.exists()) {
			file.deleteOnExit();
		}
	}

	public static boolean makeDir(String dirpath, boolean parentdirs) {
		return makeDir(new File(dirpath), parentdirs);
	}

	public static boolean makeDir(File directory, boolean parentdirs) {
		if(directory == null) {
			throw new RuntimeException("Variable[directory] - Is Null");
		}

		if(!directory.exists()) {
			return (parentdirs ? directory.mkdirs() : directory.mkdir());
		}

		return false;
	}

	public static void deleteDir(File directroy, boolean include) {
		if(directroy.exists() && directroy.isDirectory()) {
			final File[] FILES = directroy.listFiles();

			for(int X = 0; X < FILES.length; X++) {
				if(FILES[X].isDirectory()) {
					if(include) {
						deleteDir(FILES[X], include);
					}
				} else {
					if(FILES[X].exists()) {
						FILES[X].delete();
					}
				}
			}

			directroy.delete();
		}
	}

	public static boolean isFileType(String path, byte[] header) {
		return isFileType(new File(path), header, 0, header.length);
	}

	public static boolean isFileType(String path, byte[] header, int offset, int length) {
		return isFileType(new File(path), header, offset, length);
	}

	public static boolean isFileType(File file, byte[] header) {
		return isFileType(file, header, 0, header.length);
	}

	public static boolean isFileType(File file, byte[] header, int offset, int length) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(!file.exists()) {
			throw new RuntimeException("Variable[file] - Does Not Exists");
		}

		if(file.isDirectory()) {
			throw new RuntimeException("Variable[file] - Is Not A Directory");
		}

		FileInputStream IStream = null;
		try {
			IStream = new FileInputStream(file);

			return isFileType(IStream, header, offset, length);
		} catch(Exception e) {
			_LOG_.printError(e);
			return false;
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception e) {}
				IStream = null;
			}
		}
	}

	public static boolean isFileType(InputStream istream, byte[] header) {
		return isFileType(istream, header, 0, header.length);
	}

	/**
	 * 
	 * @param istream
	 *            Stream To Read From
	 * @param header
	 *            headerbytes
	 * @param offset
	 * @param length
	 *            Length Of Header Bytes
	 * @return
	 */
	public static boolean isFileType(InputStream istream, byte[] header, int offset, int length) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		if(header == null) {
			throw new RuntimeException("Variable[headerbytes] - Is Null");
		}

		if(header.length == 0 || offset < 0 || length > header.length || (offset + length) > header.length) {
			throw new RuntimeException("Variable[headerbytes] - Invalid Number Of Bytes");
		}

		final byte[] HEADER = new byte[length];
		final int HEADERLEN;
		try {
			HEADERLEN = istream.read(HEADER, 0, HEADER.length);
//            if(HEADERLEN == length) {
//                for(int X = 0; X < HEADERLEN && (offset + X) < headerbytes.length; X++) {
//                    if(HEADER[X] != headerbytes[offset + X]) {
//                        return false;
//                    }
//                }
//            } else {
//            	return false;
//            }
		} catch(Exception e) {
			_LOG_.printError(e);
			return false;
		}

		return isFileType(HEADER, 0, HEADERLEN, header, offset, length);
	}

	public static boolean isFileType(byte[] buffer, byte[] header) {
		return isFileType(buffer, 0, buffer.length, header, 0, header.length);
	}

	public static boolean isFileType(byte[] buffer, int boffset, int blength, byte[] header) {
		return isFileType(buffer, boffset, blength, header, 0, header.length);
	}

	public static boolean isFileType(byte[] buffer, int boffset, int blength, byte[] header, int hoffset, int hlength) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		if(header == null) {
			throw new RuntimeException("Variable[headerbytes] - Is Null");
		}

		if(buffer.length == 0 || boffset < 0 || blength > buffer.length || (boffset + blength) > buffer.length) {
			throw new RuntimeException("Variable[buffer] - Invalid Number Of Bytes");
		}

		if(header.length == 0 || hoffset < 0 || hlength > header.length || (hoffset + hlength) > header.length) {
			throw new RuntimeException("Variable[headerbytes] - Invalid Number Of Bytes");
		}

		if(blength < hlength) {
			return false;
		}

		for(int X = 0; (boffset + X) < buffer.length && (hoffset + X) < header.length && X < blength && X < hlength; X++) {
			if(buffer[boffset + X] != header[hoffset + X]) {
				return false;
			}
		}

		return true;
	}

//    public static void main(String[] args) {
////		System.out.println(isFileType(new byte[] {1, 2, 3, 4}, 0, 4, new byte[] {1, 2, 3, 4}, 0, 4));
//		System.out.println(isFileType(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, new byte[] {1, 2, 3, 4}));
//		
////		System.out.println(isFileType(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 0, 8, new byte[] {2, 2, 3, 4}, 0, 4));
////		System.out.println(isFileType(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 0, 8, new byte[] {1, 2, 2, 4}, 0, 4));
//	}

	public static File renameTo(File file, String newname) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(newname == null) {
			throw new RuntimeException(" Variable[newname] - Is Null");
		}

		if(file.exists()) {
			final File FILE = new File(file.getParent(), newname);

			if(file.renameTo(FILE)) {
				return FILE;
			}
		}

		return null;
	}

	public static File moveFile(String file, String destdir) {
		return moveFile(new File(file), new File(destdir));
	}

	public static File moveFile(File file, String destdir) {
		return moveFile(file, new File(destdir));
	}

	public static File moveFile(String file, File destdir) {
		return moveFile(new File(file), destdir);
	}

	public static File moveFile(File file, File destdir) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(destdir == null) {
			throw new RuntimeException("Variable[destdir] - Is Null");
		}

		if((file.exists() && file.isFile()) && (destdir.exists() && destdir.isDirectory())) {
			final File OUTPUT_FILE = new File(destdir, file.getName());

			FileInputStream IStream = null;
			FileOutputStream OStream = null;
			try {
				IStream = new FileInputStream(file);
				OStream = new FileOutputStream(OUTPUT_FILE);

				if(copyStream(IStream, OStream, true)) {
					if(file.delete()) {
						return OUTPUT_FILE;
					}
				}
			} catch(Exception e) {
				_LOG_.printError(e);
			} finally {
				IStream = null;
				OStream = null;
			}
		}

		return null;
	}

	public static boolean copyFile(File source, File target) {
		if(source == null) {
			throw new RuntimeException("Variable[source] - Is Null");
		}

		if(target == null) {
			throw new RuntimeException("Variable[target] - Is Null");
		}

		if(!source.exists() || !source.isFile()) {
			throw new RuntimeException("Variable[source] - Does Not Exists");
		}

//        if(!target.exists() || !target.isFile()) {
//            throw new RuntimeException("Class[FileUtil] - Method[copyFile] - Variable[target] - Does Not Exists");
//        }

		FileInputStream IStream = null;
		FileOutputStream OStream = null;
		try {
			IStream = new FileInputStream(source);
			OStream = new FileOutputStream(target);

			return copyStream(IStream, OStream, 32, false);
		} catch(Exception e) {
			_LOG_.printError(e);
		} finally {
			if(IStream != null) {
				try {
					IStream.close();
				} catch(Exception e) {}
				IStream = null;
			}
			if(OStream != null) {
				try {
					OStream.close();
				} catch(Exception e) {}
				OStream = null;
			}
		}

		return false;
	}

	public static boolean copyStream(InputStream istream, OutputStream ostream) {
		return copyStream(istream, ostream, 32, false);
	}

	public static boolean copyStream(InputStream istream, OutputStream ostream, boolean close) {
		return copyStream(istream, ostream, 32, close);
	}

	public static boolean copyStream(InputStream istream, OutputStream ostream, int buffersize, boolean close) {
		if(istream == null) {
			throw new RuntimeException("Variable[istream] - Is Null");
		}

		if(ostream == null) {
			throw new RuntimeException("Variable[ostream] - Is Null");
		}

		if(buffersize <= 0) {
			throw new RuntimeException("Variable[buffersize] - Must Be Greater Than Zero");
		}

		try {
			byte[] Buffer = new byte[buffersize];
			int ReadLen = 0;
			while((ReadLen = istream.read(Buffer, 0, Buffer.length)) > 0) {
				ostream.write(Buffer, 0, ReadLen);
			}

			return true;
		} catch(Exception e) {
			_LOG_.printError(e);
		} finally {
			if(close) {
				if(istream != null) {
					try {
						istream.close();
					} catch(Exception e) {}
					istream = null;
				}
				if(ostream != null) {
					try {
						ostream.close();
					} catch(Exception e) {}
					ostream = null;
				}
			}
		}

		return false;
	}

	public static boolean hasFilePermissions(String file, Access fileaccess) {
		return hasFilePermissions(new File(file), fileaccess);
	}

	public static boolean hasFilePermissions(File file, Access fileaccess) {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		if(fileaccess == null) {
			throw new RuntimeException("Variable[fileaccess] - Is Null");
		}

		switch(fileaccess) {
			case READ:
				return file.canRead();

			case WRITE:
				return file.canWrite();

			case READ_WRITE:
				return file.canRead() && file.canWrite();
		}

		return false;
	}

//    public static void main(String[] args) {
//		System.out.println(combineTwoPaths("C:\\", "/JustinPalinkas", File_Separator.LINUX));
//		System.out.println(combineTwoPaths("C:", "JustinPalinkas", File_Separator.LINUX));
//		System.out.println(combineTwoPaths("C:", "Justin\\Palinkas/", File_Separator.LINUX));
//		System.out.println(combineTwoPaths("C:\\", "Justin\\Palinkas", File_Separator.LINUX));
//		System.out.println(combineTwoPaths("C:", "\\Justin/Palinkas", File_Separator.LINUX));
//		System.out.println(combineTwoPaths("C:", "/Justin/Palinkas", File_Separator.LINUX));
//		System.out.println();
//		System.out.println(combineTwoPaths("C:\\", "/JustinPalinkas", File_Separator.WIN));
//		System.out.println(combineTwoPaths("C:", "JustinPalinkas", File_Separator.WIN));
//		System.out.println(combineTwoPaths("C:", "Justin\\Palinkas/", File_Separator.WIN));
//		System.out.println(combineTwoPaths("C:\\", "Justin\\Palinkas", File_Separator.WIN));
//		System.out.println(combineTwoPaths("C:", "\\Justin/Palinkas", File_Separator.WIN));
//		System.out.println(combineTwoPaths("C:", "/Justin/Palinkas", File_Separator.WIN));
//	}
}

/*
 * private static boolean isDriveTimeAccurate(File drive) { if(isDrive(drive)) {
 * if(drive.canWrite()) { try { final File TIMEFILE;
 * if(drive.getPath().endsWith(_S_)) { TIMEFILE = new File(drive.getPath() +
 * getTimeTestFile()); } else { TIMEFILE = new File(drive.getPath() + _S_ +
 * getTimeTestFile()); }
 * 
 * if(!TIMEFILE.exists()) { final Date TODAY = new Date();
 * 
 * if(TIMEFILE.createNewFile()) { TIMEFILE.setLastModified(TODAY.getTime());
 * 
 * final long LAST_MODIFIED = TIMEFILE.lastModified();
 * 
 * if(TIMEFILE.exists()) { TIMEFILE.delete(); }
 * 
 * return (LAST_MODIFIED == TODAY.getTime()); } }
 * 
 * return false; } catch (Exception e) {} } }
 * 
 * return false; }
 * 
 * private static String getTimeTestFile() { return "TimeTest " +
 * Long.toString(new Date().getTime()) + ".tmp"; }
 */
//    private static final File _DRIVE_C_ = new File("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Java Test\\Time Test.txt");
//    private static final File _DRIVE_K_ = new File("K:\\Time Test.txt");

//    private static final String _LOCAL_DRIVE_TYPE_ = "Local Disk";
//    private static final String _CD_DRIVE_TYPE_ = "CD Drive";
//    private static final String _REMOVABLE_DRIVE_TYPE_ = "Removable Disk";

//C:\ Local Disk
//I:\ Local Disk
//D:\ CD Drive
//G:\ Removable Disk
/*
 * public static void main(String[] args) { //
 * System.out.println(removeFileSepAtEnd("C:\\Test")); //
 * System.out.println(removeFileSepAtEnd("C:\\Test\\"));
 * 
 * // System.out.println(combineTwoPaths("C:\\", "Test")); //
 * System.out.println(combineTwoPaths("C:\\", "\\Test")); //
 * System.out.println(combineTwoPaths("C:\\", "\\Test\\")); //
 * System.out.println(combineTwoPaths("C:", "\\Test")); //
 * System.out.println(combineTwoPaths("C:", "\\Test\\")); //
 * System.out.println(combineTwoPaths("C:", "Test\\"));
 * 
 * // System.out.println("C:\\ " + getSystemDisplayName(getDrive("C:\\"))); //
 * System.out.println("I:\\ " + getSystemDisplayName(getDrive("I:\\"))); //
 * System.out.println("D:\\ " + getSystemDisplayName(getDrive("D:\\"))); //
 * System.out.println("G:\\ " + getSystemDisplayName(getDrive("G:\\")));
 * 
 * 
 * // int Counter = 0; // int Retry = 0; // while(Counter < 20) { // try { //
 * final java.util.Date TODAY = new java.util.Date(); // //
 * System.out.println(); // System.out.println(Integer.toString(Counter)); //
 * System.out.println("Today: " + TODAY.getTime() + " - " + new
 * java.util.Date(TODAY.getTime()).toString()); // // if(_DRIVE_C_.exists() ||
 * _DRIVE_K_.exists()) { // if(Retry++ == 5) {return;} // //
 * if(_DRIVE_C_.exists()) {_DRIVE_C_.delete();} // if(_DRIVE_K_.exists())
 * {_DRIVE_K_.delete();} // //
 * System.out.println("File(s) Exists! Retrying in One Second."); //
 * utillib.utilities.ThreadUtil.sleep(1000); // continue; // } // //
 * if(_DRIVE_C_.createNewFile()) { //
 * _DRIVE_C_.setLastModified(TODAY.getTime()); // //
 * System.out.println("[C:\\] " + (_DRIVE_C_.lastModified() == TODAY.getTime() ?
 * "Good" : "Bad ") + // " Time (File: " + _DRIVE_C_.lastModified() + " - " +
 * new java.util.Date(_DRIVE_C_.lastModified()).toString() + ")"); // } // //
 * if(_DRIVE_K_.createNewFile()) { //
 * _DRIVE_K_.setLastModified(TODAY.getTime()); // //
 * System.out.println("[K:\\] " + (_DRIVE_K_.lastModified() == TODAY.getTime() ?
 * "Good" : "Bad ") + // " Time (File: " + _DRIVE_K_.lastModified() + " - " +
 * new java.util.Date(_DRIVE_K_.lastModified()).toString() + ")"); // } // // }
 * catch (Exception e) { // e.printStackTrace(); // } // //
 * if(_DRIVE_C_.exists()) { // _DRIVE_C_.delete(); // } // //
 * if(_DRIVE_K_.exists()) { // _DRIVE_K_.delete(); // } // //
 * utillib.utilities.ThreadUtil.sleep(2000); // Counter++; // } }
 */

//        MyStringBuffer Buffer = new MyStringBuffer(path);
//        
//        for(int X = 0; X < InValidChars.length; X++) {
//            if(InValidChars[X] == ':') {
//                Buffer.replace(2, InValidChars[X], '_');
//            } else {
//                Buffer.replace(InValidChars[X], '_');
//            }
//        }