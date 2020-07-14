package runner;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import process.MainProcess;
import process.ProcessIF;

public class ExamRunner<T extends ProcessIF> {
	T proc = null;
	
	public ExamRunner(T t) {
		this.proc = t; 
	}
	private final int THREAD_CNT = 5;
	
	public void startAsync() {
		
		ExecutorService service = Executors.newFixedThreadPool(THREAD_CNT);
		
		for(int inx=0; inx < THREAD_CNT ; inx++) {
			String threadName = "th-" + inx;
			AsyncRunner<T> arunner = new AsyncRunner<T>(this.proc,threadName);
			service.execute(arunner);
		}
		
		
		service.shutdown();

        // 20초 대기 후 모든  Task 강제종료
        try {
			if (service.awaitTermination(20, TimeUnit.SECONDS)) {
			    System.out.println(LocalTime.now() + " All jobs are terminated");
			} else {
			    System.out.println(LocalTime.now() + " some jobs are not terminated");

			    // 모든 Task를 강제 종료
			    service.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("end");
		
	}
	
	public void startSync() {
		SyncRunner<T> runner = new SyncRunner<T>();
		
		runner.start(this.proc);
	}

	public static void main(String[] args) {
		ExamRunner<MainProcess> runner = new ExamRunner(new MainProcess());
		
//		runner.startAsync();
		runner.startSync();
	}

}
