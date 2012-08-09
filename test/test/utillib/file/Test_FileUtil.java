package test.utillib.file;

import utillib.file.FileUtil;

public class Test_FileUtil {

	public static void main(String[] args) {
		System.out.println(FileUtil.combinePaths("C:\\Windows\\"));

		System.out.println(FileUtil.combinePaths("C:\\Windows\\", "\\Justin\\", "\\Desktop"));
		System.out.println(FileUtil.combinePaths("C:\\Windows", "\\Justin", "\\Desktop"));
		System.out.println(FileUtil.combinePaths("C:\\Windows\\", "Justin", "\\Desktop"));
		System.out.println(FileUtil.combinePaths("C:\\Windows\\", "\\Justin\\", "Desktop"));

		System.out.println(FileUtil.combinePaths("C:\\Windows\\", "\\Justin\\", "Desktop", "Test", ".txt"));

		System.out.println(FileUtil.combinePaths("C:\\Windows\\", "\\Justin\\", "\\", "Test" + ".txt"));
	}

}
