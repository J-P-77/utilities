package utillib.net.protocol.http.client;

import java.io.InputStream;
import java.net.URL;

import utillib.net.protocol.http.client.HttpEntry.Method;

public class Test_MyHttpConnection {
	//STATIC	
	private static void printBuffer(String msg, byte[] buffer, int offset, int length) {
		System.out.println("[" + msg + " BUFFER START]");
		for(int X = 0; X < length; X++) {
			final byte BYTE = buffer[X + offset];

//			switch(BYTE) {
//				case 10:
//				case 13:
//					System.out.print('(' + Byte.toString(BYTE) + ')');					
//					break;
//			}

			System.out.print((char)BYTE);
		}
	}
	
	private static void fetch(URL url) {
		MyHttpConnection Connection = null;
		try {
			Connection = new MyHttpConnection(url);

			final HttpEntry ENTRY = new HttpEntry(Method.GET, url.getPath());

			ENTRY.setContentLength(0);

			Connection.request(ENTRY);

			final InputStream ISTREAM = Connection.reply(ENTRY);

			//System.out.println(ENTRY.getStatusCode() + " Status Msg: " + ENTRY.getStatusMsg());

			if(HttpCodes.statusRedirection(ENTRY)) {
				System.out.println("Redirection From: " + ENTRY.getHost() + " TO " + ENTRY.getHeaderValue("Location"));
			} else if(HttpCodes.statusSuccess(ENTRY)) {
				if(ISTREAM == null) {
					System.out.println("Zero Length Stream");
				} else {
//					System.out.println("[              DATA              ]");
					int Read = -1;
					while((Read = ISTREAM.read()) != -1) {
//						System.out.print((char)Read);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(Connection != null) {
				try {
					Connection.close();
				} catch(Exception e2) {}
				Connection = null;
			}
		}
	}

	public static void main(String[] args) {
//		try {
//			System.out.println(InetAddress.getByName("zyn.ga").toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		System.out.println(Integer.parseInt("f1", 16));
//		
//		if(true) return;
//		
		try {
			fetch(new URL("https://www.google.com"));
			fetch(new URL("http://zyn.ga/1qJ"));
//			fetch(new URL("http://tinyurl.com/4y7ovhq"));
//			fetch(new URL("http://www.cnet.com:80"));
			fetch(new URL("http://www.yahoo.com:80"));
//			fetch(new URL("http://www.twit.tv:80"));
//			fetch(new URL("http://wildfiregames.com:80/0ad/"));

			//fetch(new URL("http://www.firefox.com:80"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
