package test.beta.utillib;

import beta.utillib.LinkedListFirstLast;;

public class Test_LinkedListFirstLast {
	public static void main(String[] args) {
		final LinkedListFirstLast<Integer> LIST = new LinkedListFirstLast<Integer>();

		for(int X = 5; X >= 0; X--) {
			LIST.pushLast(X);
		}
		
		for(int X = 5; X <= 10; X++) {
			LIST.pushFirst(X);
		}

		while(!LIST.isEmpty()) {
			System.out.println(LIST.popFirst());
		}
	}
}
