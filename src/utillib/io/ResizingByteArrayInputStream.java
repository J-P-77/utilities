package utillib.io;

import utillib.arrays.ResizingByteArray;

import java.io.IOException;

public class ResizingByteArrayInputStream extends AInputStream {
	private ResizingByteArray _Buffer;

	private int _Pos = 0;

	public ResizingByteArrayInputStream(ResizingByteArray buffer) {
		this(buffer, 0);
	}

	public ResizingByteArrayInputStream(ResizingByteArray buffer, int offset) {
		if(buffer == null) {
			throw new RuntimeException("Variable[buffer] - Is Null");
		}

		if(offset != 0 && buffer.length() != 0) {
			if(offset < 0 || offset >= buffer.length()) {
				throw new RuntimeException("Variable[offset] - Offset Must Be Greater Than Zero And Less Than Buffer Length");
			}
		}

		_Buffer = buffer;
		_Pos = offset;
	}

	public ResizingByteArray getResizingByteArray() {
		return _Buffer;
	}

	public int getPosition() {
		return _Pos;
	}

	@Override
	public int read() throws IOException {
		if(_Pos < _Buffer.length()) {
			return _Buffer.get(_Pos);
		}

		return -1;
	}

	@Override
	public int available() throws IOException {
		return _Buffer.length() - _Pos;
	}

	@Override
	public boolean isClosed() {
		return _Buffer == null;
	}

	@Override
	public void close() throws IOException {
		_Buffer = null;
	}

//	public static void main(String[] args) {
//		final ResizingByteArray BUFFER = new ResizingByteArray(36);
//		
//		ResizingByteArrayOutputStream BOStream = null;
//		try {
//			BOStream = new ResizingByteArrayOutputStream(BUFFER);
//			
//			BOStream.writeString("Justin Palinkas");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		ResizingByteArrayInputStream BIStream = null;
//		try {
//			BIStream = new ResizingByteArrayInputStream(BUFFER);
//			
//			System.out.println(BIStream.available());
//			System.out.println(BIStream.readString(15));
//			System.out.println(BIStream.available());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
