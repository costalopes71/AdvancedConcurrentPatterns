package com.costalopes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerWithLocks {

	public static void main(String[] args) throws InterruptedException {

		List<Integer> buffer = new ArrayList<>();

		Lock lock = new ReentrantLock();
		Condition isEmpty = lock.newCondition();
		Condition isFull = lock.newCondition();

		class Consumer implements Callable<String> {

			@Override
			public String call() throws InterruptedException, TimeoutException {

				int count = 0;
				while (count++ < 50) {

					try {

						lock.lock();

						while (isEmpty(buffer)) {
							// wait
							if (!isEmpty.await(10, TimeUnit.MILLISECONDS)) {
								throw new TimeoutException("Consumer timed out");
							}
						}

						buffer.remove(buffer.size() - 1);
						// signal
						isFull.signalAll();

					} finally {
						lock.unlock();
					}

				}

				return "Cosumed " + (count - 1);
			}
		}

		class Producer implements Callable<String> {

			@Override
			public String call() throws Exception {

				int count = 0;
				while (count++ < 50) {

					try {

						lock.lock();
						
						// causing a exception on purpouse
						int a = 10/0;
						
						while (isFull(buffer)) {
							// wait
							isFull.await();
						}

						buffer.add(1);
						// signal
						isEmpty.signalAll();

					} finally {
						lock.unlock();
					}

				}

				return "Produced " + (count - 1);
			}

		}

		List<Producer> producers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			producers.add(new Producer());
		}
		
		List<Consumer> consumers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			consumers.add(new Consumer());
		}
		
		System.out.println("Producers and Consumers launched");
		
		List<Callable<String>> producersAndConsumers = new ArrayList<>();
		producersAndConsumers.addAll(consumers);
		producersAndConsumers.addAll(producers);
		
		ExecutorService executor = Executors.newFixedThreadPool(8);
		
		try {
			
			List<Future<String>> futures = executor.invokeAll(producersAndConsumers);
			
			futures.forEach(future -> {
				
				try {
					System.out.println(future.get());
				} catch (InterruptedException | ExecutionException e) {
					System.out.println("Exception: " + e.getMessage());
				}
				
			});
			
		} finally {
			executor.shutdown();
			System.out.println("Executor Service shut down");
		}
		
	}

	public static boolean isEmpty(List<Integer> buffer) {
		return buffer.size() == 0;
	}

	public static boolean isFull(List<Integer> buffer) {
		return buffer.size() == 10;
	}

}
