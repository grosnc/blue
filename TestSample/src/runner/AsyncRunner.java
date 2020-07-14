package runner;

import process.ProcessIF;

public class AsyncRunner<T extends ProcessIF> extends Thread{
	T proc = null;
	String threadName = "";

	public AsyncRunner(T proc,String threadName){
		this.proc = proc;
		this.threadName = threadName;
	}
	
	public void run() {
		System.out.println(this.getThreadName() + " is started.");
		proc.start();
	}
	
	public String getThreadName() {
		return this.threadName;
	}
}
