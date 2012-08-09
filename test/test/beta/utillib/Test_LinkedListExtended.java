package test.beta.utillib;

import beta.utillib.LinkedListExtended;

import java.util.Iterator;

public class Test_LinkedListExtended {
	public static void main(String[] args) {
		final LinkedListExtended<Integer> INTS = new LinkedListExtended<>();
		
		INTS.push(new Integer(0));
		INTS.push(new Integer(1));
		INTS.push(new Integer(2));
		INTS.push(new Integer(3));
		INTS.push(new Integer(4));
		
		System.out.println(INTS.exists(new Integer(10)) + " = false");
		System.out.println(INTS.exists(new Integer(2)) + " = true");
		
		INTS.push(new Integer(5));
		INTS.push(new Integer(6));
		
		Iterator T = INTS.iterator();
		while(T.hasNext()) {
			System.out.println(T.next());
		}		
		
		System.out.println(INTS.pop(new Integer(3)) + " = 3");
		
		while(!INTS.isEmpty()) {
			System.out.println(INTS.pop());
		}
	}
}
