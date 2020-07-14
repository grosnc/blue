package runner;

public class SyncRunner<T extends process.ProcessIF>{
	
	
	public void start(T process) {
		process.start();
	}

}
