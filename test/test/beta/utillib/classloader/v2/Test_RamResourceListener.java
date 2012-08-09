package test.beta.utillib.classloader.v2;

import beta.utillib.classloader.v2.RamResourceListener;

import java.net.URL;
import java.net.URLConnection;

public class Test_RamResourceListener {
	public static void main(String[] args) {
		try {
			RamResourceListener L = new RamResourceListener();
			L.addResource("test.txt", "Justin Palinkas".getBytes());
			
			URL U = L.findResource("test.txt");
			
			System.out.println(U.toString());
			
			URLConnection C = U.openConnection();
			
			C.connect();
			
			int Read = 0;
			while((Read = C.getInputStream().read()) != -1 ) {
				System.out.print((char)Read);
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
