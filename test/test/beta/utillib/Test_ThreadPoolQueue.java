package test.beta.utillib;

import utillib.utilities.ADefaultConsole;

import beta.utillib.LinkedListExtended;
import beta.utillib.ThreadPoolQueue;

import beta.utillib.ThreadPoolQueue.QueueTask;

import utillib.strings.StringUtil;

import utillib.utilities.RandomUtil;
import utillib.utilities.ThreadUtil;

public class Test_ThreadPoolQueue {
    private static ThreadPoolQueue _Q_ = null;
	
    private static LinkedListExtended<Thread> _DATATHREADS_ = new LinkedListExtended<Thread>();
    
	public static class DataTaskGenerator implements Runnable {
		private final String _NAME;
		
		public DataTaskGenerator(String name) {
			_NAME = name;
		}
		
		@Override
		public void run() {
			_DATATHREADS_.push(Thread.currentThread());
			
			System.out.println(_NAME + " Generating Task...");
			final int LENGTH = RandomUtil.randomInt(1000, 2000);
			for(int Y = 0; Y < LENGTH; Y++) {
				final int YY = Y;
				_Q_.addTask(new QueueTask() {
					@Override
					public void run() {
//						System.out.println(_NAME + " - " + YY);
						
						ThreadUtil.sleep(RandomUtil.randomInt(60, 110));
					}
				});
				ThreadUtil.sleep(RandomUtil.randomInt(6, 12));
			}
			System.out.println("... " + _NAME + " Task Generated");
			
			_DATATHREADS_.remove(Thread.currentThread());
		}
	}    
    
    private static void createData(int max) {
    	for(int X = 0; X < max; X++) {
    		final Thread THREAD = new Thread(new DataTaskGenerator("Task"), "Generating Data Thread");
    		
    		THREAD.start();
    	}
    }
    
    public static void main(String[] args) {
    	final ADefaultConsole CONSOLE = new ADefaultConsole() {
			
			@Override
			public boolean processCommand(String cmd, String parameters) {
				if("start".equals(cmd)) {
					if(_Q_ == null || !_Q_.isRunning()) {
						if(parameters == null) {
							int Min = RandomUtil.randomInt(1, 4);
							
							_Q_ = new ThreadPoolQueue(Min, RandomUtil.randomInt(Min + 1, Min + 4), RandomUtil.randomInt(50, 100));
							_Q_.start();
						} else {
							final String[] SPLIT = StringUtil.split(parameters, ' ');
							
							if(SPLIT.length > 3) {
								System.out.println("Invaild Number Of Parameters");
							} else {								
								if(SPLIT.length == 1) {
									if(StringUtil.isPositiveWholeNumber(SPLIT[0])) {
										int Min = StringUtil.getIntValue(SPLIT[0], 0, -1);

										if(Min <= 0) {
											System.out.println("Min Number Must Be Greater Than Zero");
										} else {											
											start(Min, RandomUtil.randomInt(Min + 1, Min +4), 
													RandomUtil.randomInt(50, 100));
										}
									} else {
										System.out.println("Invaild Min Number");
									}
								} else if(SPLIT.length == 2) {
									if(!StringUtil.isPositiveWholeNumber(SPLIT[0])) {
										System.out.println("Invaild Min Number");
									} else if(!StringUtil.isPositiveWholeNumber(SPLIT[1])) {
										System.out.println("Invaild Max Number");
									} else {										
										start(StringUtil.getIntValue(SPLIT[0], 0, -1), 
												StringUtil.getIntValue(SPLIT[1], 0, -1), 
												RandomUtil.randomInt(50, 100));
									}
								} else if(SPLIT.length == 3) {
									if(!StringUtil.isPositiveWholeNumber(SPLIT[0])) {
										System.out.println("Invaild Min Number");
									} else if(!StringUtil.isPositiveWholeNumber(SPLIT[1])) {
										System.out.println("Invaild Max Number");
									} else if(!StringUtil.isPositiveWholeNumber(SPLIT[2])) {
										System.out.println("Invaild Threshold Number");
									} else {
										start(StringUtil.getIntValue(SPLIT[0], 0, -1), 
												StringUtil.getIntValue(SPLIT[1], 0, -1), 
												StringUtil.getIntValue(SPLIT[1], 0, -1));
									}
								}
							}
						}
					} else {
						System.out.println("Allready Started");
					}
				} else if("stop".equals(cmd)) {
					if(_Q_ == null || !_Q_.isRunning()) {
						System.out.println("Not Running");
					} else {
						_Q_.stop();
						_Q_ = null;
					}
				} else if("createdata".equals(cmd)) {
					if(_Q_ == null || !_Q_.isRunning()) {
						System.out.println("Not Running");
					} else {
						if(parameters == null) {
							createData(2);
						} else {
							if(StringUtil.isWholeNumber(parameters)) {
								createData(StringUtil.getIntValue(parameters, 2));
							} else {
								System.out.println("Invalid Parameter");
							}
						}
					}
				} else if("list".equals(cmd)) {
					if(_Q_ == null || !_Q_.isRunning()) {
						System.out.println("Not Running");
					} else {
						System.out.println("Min: " + _Q_.getMin() + " Max: " + _Q_.getMax() + " Threshold: " + _Q_.getThreshold());
					}
				} else if("length".equals(cmd)) {
					if(_Q_ == null || !_Q_.isRunning()) {
						System.out.println("Not Running");
					} else {
						System.out.println("Queue Length: "  + _Q_.getQueueLength());
					}
				} else {
					return false;
				}
				
				return true;
			}
			
			private void start(int min, int max, int threshold) {
				if(_Q_ == null || !_Q_.isRunning()) {
					if(min <= 0) {
						System.out.println("Min Number Must Be Greater Than Zero");
					} else if(max < min) {
						System.out.println("Max Number Must Be Greater Than Min Number");
					} else if(threshold <= 0) {
						System.out.println("Threshold Number Must Be Greater Than Zero");
					} else {											
						_Q_ = new ThreadPoolQueue(min, max, threshold);
						_Q_.start();
					}
				} else {
					System.out.println("Allready Started");
				}
			}
			
			@Override
			public void helpCmd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitCmd() {
				// TODO Auto-generated method stub
				if(_Q_ != null) {
					_Q_.stop();
					_Q_ = null;
				}
			}
		};
		CONSOLE.run();
		//System.out.println("Min: " + _MIN_ + " Max: " + _MAX_ + " Threshold: " + _THRESHOLD_);
	}
}
