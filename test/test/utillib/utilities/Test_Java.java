package test.utillib.utilities;

import utillib.utilities.Java;

public class Test_Java {

    public static void main(String[] args) {
		System.out.println("Version:  " + Java.getVersionString());
		System.out.println("Is Java5: " + Java._IS_JAVA5_);
		System.out.println("Is Java6: " + Java._IS_JAVA6_);
		System.out.println("Is Java7: " + Java._IS_JAVA7_);
	}
}
