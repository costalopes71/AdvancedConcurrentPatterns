package com.costalopes.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlayingWithCallableAndFutures {

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		
		Callable<String> task = () -> {
			Thread.sleep(300);
			return "Im in thread " + Thread.currentThread().getName();
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		
		try {
			for (int i = 0; i < 10; i++) {
				Future<String> future = executorService.submit(task);
				// o metodo get com Timeout lanca excecao apos passado o timeout
				System.out.println("I get: " + future.get(100, TimeUnit.MILLISECONDS));
			}
		} finally {
			executorService.shutdown();
		}
		
	}
	
}
