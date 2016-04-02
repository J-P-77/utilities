/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.utilities;

import jp77.utillib.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
@Deprecated
public class Zip {
	@Deprecated
	public static void decompressZipFile(String filename) {
		decompressZipFile(filename, "");
	}

	@Deprecated
	public static void decompressZipFile(String filename, String todirectory) {
		if(filename == null) {
			throw new NullPointerException("filename");
		} else if(!new File(filename).exists()) {
			printOut("File: " + filename + "Does Not Exists");
		} else if(!isExtension(filename, ".zip")) {
			printOut("File: " + filename + "Is Not A Zip File");
		} else if(todirectory == null) {
			decompressZipFile(filename, "");
		} else {
			ZipFile FileZip = null;
			int Counter = 1;
			try {
				FileZip = new ZipFile(filename);
				printOut("Number of Entries: " + Integer.toString(FileZip.size()));

				Enumeration Entries = FileZip.entries();

				while(Entries.hasMoreElements()) {
					ZipEntry Entry = (ZipEntry)Entries.nextElement();

					String EntryName = reformatEntryName(Entry);
					printEntry(Counter, Entry, EntryName);

					if(Entry.isDirectory()) {
						createDirectory(EntryName);
					} else {
						writeFile(FileZip.getInputStream(Entry), todirectory + EntryName);
					}

					Counter++;
				}
			} catch(ZipException e) {
				printOut(e.getMessage());
			} catch(Exception e) {
				printOut(e.toString());
			} finally {
				try {
					if(FileZip != null) {
						FileZip.close();
						FileZip = null;
					}
				} catch(Exception ex) {
					printOut(ex.toString());
				}
			}
		}
	}

	private static void writeFile(InputStream istream, String filename) {
		if(istream == null) {
			throw new NullPointerException("istream");
		} else if(filename == null) {
			throw new NullPointerException("filename");
		} else {
			//READER
			BufferedInputStream bInput = null;
			//WRITER
			FileOutputStream wFile = null;
			BufferedOutputStream bOutput = null;

			try {
				//READ FROM SOURCE
				bInput = new BufferedInputStream(istream);
				//WRITE TO TARGET
				wFile = new FileOutputStream(filename);
				bOutput = new BufferedOutputStream(wFile);

				int B = 0;
				while((B = bInput.read()) != -1) {
					bOutput.write(B);
				}
				//CLEAR'S WRITE STREAM BUFFER
				bOutput.flush();

			} catch(Exception e) {
				printOut(e.toString());
			} finally {
				//CLOSE SOURCE STREAM
				if(istream != null) {
					try {
						istream.close();
					} catch(Exception e) {
						System.out.println("InputStream ERROR");
					}
				}
				if(bInput != null) {
					try {
						bInput.close();
					} catch(Exception e) {
						System.out.println("BufferedInputStream ERROR");
					}
				}
				//CLOSE TARGET FILE
				if(wFile != null) {
					try {
						wFile.close();
					} catch(Exception e) {
						System.out.println("FileOutputStream ERROR");
					}
				}
				if(bOutput != null) {
					try {
						bOutput.close();
					} catch(Exception e) {
						System.out.println("BufferedOutputStream ERROR");
					}
				}
			}
		}
	}

	private static boolean isExtension(String filename, String ext) {
		return filename.endsWith(ext);
	}

	private static void createDirectory(String directory) {
		File Folder = new File(directory);

		if(!Folder.exists()) {
			Folder.mkdir();
		}
	}

	private static void printOut(String msg) {
		System.out.println(msg);
	}

	private static void printEntry(int Counter, ZipEntry entry) {
		printEntry(Counter, entry, reformatEntryName(entry));
	}

	private static void printEntry(int Counter, ZipEntry entry, String entryname) {
		printOut("Entry " + "Number: " + Integer.toString(Counter));
		printOut("      " + "Name:   " + entryname);
		if(!entry.isDirectory()) {
			printOut("      " + "Size:   " + entry.getSize());
		}
	}

	private static String reformatEntryName(ZipEntry entry) {
		StringBuffer Buffer = new StringBuffer(entry.getName());
		int Index = 0;

		while(Index != -1) {
			Index = Buffer.indexOf("/", Index);

			if(Index == -1) {
				break;
			}

			Buffer.replace(Index, Index + FileUtil._S_.length(), FileUtil._S_);
		}

		return Buffer.toString();
	}

	@Deprecated
	public static void compressZipFile(File[] files, String toFile) {
		if(files == null) {
			throw new NullPointerException("files");
		} else if(!isExtension(toFile, ".zip")) {
			printOut("File: " + toFile + "Is Not A Zip File");
		} else if(toFile == null || toFile.equals("")) {
			compressZipFile(files, "Temp Name.zip");
		} else {
			ZipOutputStream FileZip = null;
			FileOutputStream oStream = null;

			try {
				oStream = new FileOutputStream(toFile);
				FileZip = new ZipOutputStream(oStream);

				for(int X = 0; X < files.length; X++) {
					ZipEntry Entry = new ZipEntry(files[X].getName());

					FileZip.putNextEntry(Entry);
					FileZip.write(getBytes(files[X]));
				}
			} catch(ZipException e) {
				printOut(e.getMessage());
			} catch(Exception e) {
				printOut(e.toString());
			} finally {
				try {
					if(FileZip != null) {
						FileZip.close();
						FileZip = null;
					}
				} catch(Exception ex) {
					printOut(ex.toString());
				}
			}
		}
	}

	private static byte[] getBytes(File file) {
		FileInputStream rFile = null;
		byte[] Bytes = null;

		try {
			rFile = new FileInputStream(file);
			Bytes = new byte[(int)file.length()];

			rFile.read(Bytes);

			rFile.close();
		} catch(Exception e) {}

		return Bytes;
	}
/*
    public static void main(String[] args) {
        String FileName = "C:\\Documents and Settings\\Dalton Dell\\Desktop\\Justin's\\Java\\Projects\\Tests\\";
        
        //FileName += "2005-11-22 pass.zip";
        FileName += "jabak-2005-11-22.zip";
                
        Zip.decompressZipFile(FileName);
    }
*/
}
