package test.utillib.arrays.buffer;

import utillib.utilities.ADefaultConsole;

import utillib.arrays.buffer.ResizingByteBuffer;

import utillib.utilities.ThreadUtil;

public class Test_ResizingByteBuffer {
    private static final char[] _CHARACTERS_ = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M',
        'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '0','1','2','3','4','5','6','7','8','9'};
    
	private static final ResizingByteBuffer _BUFFER_ = new ResizingByteBuffer(6);
	
	private static class ReaderThread extends Thread {
		public ReaderThread() {
			super("ReaderThread");
		}
		
		@Override
		public void run() {
			while(!_Cancel_) {
				while(_BUFFER_.isEmpty()) {
					ThreadUtil.sleep(2);
				}
				
				final byte[] BUFFER = new byte[5];
				final int LEN = _BUFFER_.get(BUFFER);
				
				if(LEN + _Reader_Position_ >= _CHARACTERS_.length) {
					final int COUNT = _CHARACTERS_.length - _Reader_Position_;
					
					System.out.println(new String(BUFFER, 0, COUNT));
					System.out.print(new String(BUFFER, COUNT, LEN - COUNT));
					
					_Reader_Position_ = LEN - COUNT;
				} else {
					System.out.print(new String(BUFFER, 0, LEN));
					
					_Reader_Position_ += LEN;
				}
				
				ThreadUtil.sleep(1000);
			}
		}
	}
	
	private static class WriterThread extends Thread {
		public WriterThread() {
			super("WriterThread");
		}
		
		@Override
		public void run() {
			while(!_Cancel_) {
				_BUFFER_.put(new byte[] {(byte)_CHARACTERS_[_Writer_Position_++]});
				
				if(_Writer_Position_ >= _CHARACTERS_.length) {
					_Writer_Position_ = 0;
				}
				
				ThreadUtil.sleep(100);
			}
		}
	}
	
	private static int _Reader_Position_ = 0;
	private static int _Writer_Position_ = 0;
	
	private static ReaderThread _Reader_ = null;
	private static WriterThread _Writer_ = null;
	
	private static boolean _Cancel_ = false;
	
	public static void main(String[] args) {
		final ADefaultConsole CONSOLE = new ADefaultConsole() {
			
			@Override
			public boolean processCommand(String cmd, String parameters) {
				if("start".equals(cmd)) {
					if(_Reader_ == null && _Writer_ == null) {
						_Reader_ = new ReaderThread();
						_Writer_ = new WriterThread();
						
						_Reader_.start();
						_Writer_.start();
					} else {
						System.out.println("Allready Running");
					}
				} else if("stop".equals(cmd)) {
					if(_Reader_ == null && _Writer_ == null) {
						System.out.println("Not Running");
					} else {
						_Cancel_ = true;
						
						ThreadUtil.wait(_Reader_);
						ThreadUtil.wait(_Writer_);
						
						_Reader_ = null;
						_Writer_ = null;
					}
				} else {
					return false;
				}				
				
				return true;
			}
			
			@Override
			public void helpCmd() {
				System.out.println("start");
				System.out.println("stop");
			}
			
			@Override
			public void exitCmd() {
				processCommand("stop", null);
			}
		};
		CONSOLE.run();
	}
}
