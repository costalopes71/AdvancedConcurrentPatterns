package com.costalopes.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlayingWithCallableAndFutureAndExceptions {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Callable<String> task = () -> {
			throw new IllegalStateException("I throw an exception in thread " + Thread.currentThread().getName());
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		
		try {
			
			for (int i = 0; i < 10; i++) {
				Future<String> future = executorService.submit(task);
				System.out.println("I get: " + future.get());
			}
			
		} finally {
			executorService.shutdown();
		}
		
	}
	
}
