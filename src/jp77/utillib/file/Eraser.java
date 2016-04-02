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

package jp77.utillib.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.security.SecureRandom;
import jp77.utillib.interfaces.IProgress;
import jp77.utillib.utilities.BitOperations;

/**
 * 
 * @author Justin Palinkas
 */
public class Eraser {
	private static final int[][] _GUTMANN_ = {new int[] {0x55, 0x55, 0x55},//1   01010101 01010101 01010101, 0x55
	new int[] {0xAA, 0xAA, 0xAA},//2   10101010 10101010 10101010, 0xAA
	new int[] {0x92, 0x49, 0x24},//3   10010010 01001001 00100100, 0x92 0x49 0x24
	new int[] {0x49, 0x24, 0x92},//4   01001001 00100100 10010010, 0x49 0x24 0x92
	new int[] {0x24, 0x92, 0x49},//5   00100100 10010010 01001001, 0x24 0x92 0x49
	new int[] {0x00, 0x00, 0x00},//6   00000000 00000000 00000000, 0x00 
	new int[] {0x11, 0x11, 0x11},//7   00010001 00010001 00010001, 0x11 
	new int[] {0x22, 0x22, 0x22},//8   00100010 00100010 00100010, 0x22 
	new int[] {0x33, 0x33, 0x33},//9   00110011 00110011 00110011, 0x33 
	new int[] {0x44, 0x44, 0x44},//10  01000100 01000100 01000100, 0x44
	new int[] {0x55, 0x55, 0x55},//11  01010101 01010101 01010101, 0x55
	new int[] {0x66, 0x66, 0x66},//12  01100110 01100110 01100110, 0x66
	new int[] {0x77, 0x77, 0x77},//13  01110111 01110111 01110111, 0x77 
	new int[] {0x88, 0x88, 0x88},//14  10001000 10001000 10001000, 0x88
	new int[] {0x99, 0x99, 0x99},//15  10011001 10011001 10011001, 0x99 
	new int[] {0xAA, 0xAA, 0xAA},//16  10101010 10101010 10101010, 0xAA
	new int[] {0xBB, 0xBB, 0xBB},//17  10111011 10111011 10111011, 0xBB
	new int[] {0xCC, 0xCC, 0xCC},//18  11001100 11001100 11001100, 0xCC
	new int[] {0xDD, 0xDD, 0xDD},//19  11011101 11011101 11011101, 0xDD
	new int[] {0xEE, 0xEE, 0xEE},//20  11101110 11101110 11101110, 0xEE
	new int[] {0xFF, 0xFF, 0xFF},//21  11111111 11111111 11111111, 0xFF
	new int[] {0x92, 0x49, 0x24},//22  10010010 01001001 00100100, 0x92 0x49 0x24
	new int[] {0x49, 0x24, 0x92},//23  01001001 00100100 10010010, 0x49 0x24 0x92
	new int[] {0x55, 0x55, 0x55},//24  00100100 10010010 01001001, 0x24 0x92 0x49
	new int[] {0x6D, 0xB6, 0xDB},//25  01101101 10110110 11011011, 0x6D 0xB6 0xDB
	new int[] {0xB6, 0xDB, 0x6D},//26  10110110 11011011 01101101, 0xB6 0xDB 0x6D
	new int[] {0xDB, 0x6D, 0xB6},//27  11011011 01101101 10110110, 0xDB 0x6D 0xB6
	};

	private RandomAccessFile _Rnd = null;
	private File _File = null;

	public Eraser(String file) throws FileNotFoundException {
		this(new File(file));
	}

	public Eraser(File file) throws FileNotFoundException {
		if(file == null) {
			throw new RuntimeException("Variable[file] - Is Null");
		}

		_File = file;
		_Rnd = new RandomAccessFile(_File, "rws");
	}

	//1 Pass
	public void erasePseudoRandom(IProgress progess) throws IOException {
		final byte[] SEED = BitOperations.longToBytes(System.currentTimeMillis());

		erasePseudoRandom(progess, SEED, 1, 1);
	}

	public void erasePseudoRandom(IProgress progess, int numofpasses, int pass) throws IOException {
		final byte[] SEED = BitOperations.longToBytes(System.currentTimeMillis());

		erasePseudoRandom(progess, SEED, numofpasses, pass);
	}

	// pseudorandom number generator.
	public void erasePseudoRandom(IProgress progess, byte[] seed, int numofpasses, int pass) throws IOException {
		SecureRandom SRandom = new SecureRandom(seed);
		byte[][] Bytes = new byte[numofpasses][];

		for(int X = 0; X < Bytes.length; X++) {
			byte[] Temp = new byte[pass];
			SRandom.nextBytes(Temp);
			Bytes[X] = Temp;
		}

		erase(progess, Bytes);
	}

	public void eraseGutmann27Passes(IProgress progess) throws IOException {
		erase(progess, _GUTMANN_);
	}

	//1 Pass
	public void eraseBlankPass(IProgress progess) throws IOException {
		eraseBlank(progess, 1);
	}

	public void eraseBlank(IProgress progess, int numofpasses) throws IOException {
		erase(progess, ' ', numofpasses);
	}

	//1 Pass
	public void eraseNull(IProgress progess) throws IOException {
		eraseNull(progess, 1);
	}

	public void eraseNull(IProgress progess, int numofpasses) throws IOException {
		erase(progess, '\0', numofpasses);
	}

	//Length of Array Is The Number Of Passes
	public void erase(byte[] pattern) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < pattern.length; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.write(pattern[X]);
			}
		}
	}

	//Length of Array Is The Number Of Passes
	public void erase(int[] pattern) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < pattern.length; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.write(pattern[X]);
			}
		}
	}

	//Length of Array Is The Number Of Passes
	public void erase(IProgress progess, byte[][] pattern) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < pattern.length; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.seek(Y);

				for(int A = 0; A < pattern[X].length; A++) {
					_Rnd.write(pattern[X][A]);
					_Rnd.seek(Y);
				}
			}
		}
	}

	//Length of Array Is The Number Of Passes
	public void erase(IProgress progess, int[][] pattern) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < pattern.length; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.seek(Y);

				for(int A = 0; A < pattern[X].length; A++) {
					_Rnd.write(pattern[X][A]);
					_Rnd.seek(Y);
				}
			}
		}
	}

	public void erase(IProgress progess, byte value, int numofpasses) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < numofpasses; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.write(value);
			}
		}
	}

	public void erase(IProgress progess, int value, int numofpasses) throws IOException {
		final long LENGTH = _Rnd.length();

		for(int X = 0; X < numofpasses; X++) {
			_Rnd.seek(0);

			for(long Y = 0; Y < LENGTH; Y++) {
				_Rnd.write(value);
			}
		}
	}

	public void close() throws IOException {
		close(true);
	}

	public void close(boolean deletefile) throws IOException {
		if(_Rnd != null) {
			_Rnd.close();
			_Rnd = null;

			if(deletefile && _File.exists()) {
				_File.delete();
			}

			_File = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

//	public static void main(String[] args) {
//		File Current = new File("C:\\Documents and Settings\\Dalton Dell\\Desktop\\New Notepad++ Document.txt");
//
//		EraserBeta Erase = null;
//		try {
//			Erase = new EraserBeta(Current);
//
//			Erase.erase(new int[][] {new int[] {0}});
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//            try {
//                if(Erase != null) {
//                    Erase.close(false);
//                }
//            } catch (Exception e) {}
//        }
//	}
}
/*
Justin This Is Just A Test

 */