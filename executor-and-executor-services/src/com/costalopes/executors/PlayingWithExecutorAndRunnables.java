package com.costalopes.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Threads sao recursos caros para o SO, tanto para criar quanto para destruir. Por isso usar o padrao Thread nao eh a melhor maneira de usar threads. Um padrao mto mais
 * ideal eh o padrao Executor, que eh uma interface com um unico metodo chamado execute que espera como parametro um Runnable.
 * Existem varias implementacoes de Executor, e tambem existe uma outra interface que extende Executor chamada ExecutorService que possui diversos outros metodos. Todas
 * as implementacoes de Executor tbm sao implementacoes de ExecutorService.
 * O padrao Runnable nao eh um bom padrao pois nao permite que a thread criada retorne um resultado, pois o metodo eh void, e tambem nao permite propagar um excecao
 * uma vez que nao esta em sua assinatura.
 * Um padrao mto melhor eh o padrao Callable o qual retorna um Future<T> sendo T o objeto que possui o resultado da tarefa executada pela thread, esse padrao tbm permite
 * que uma excecao seja propagada para a thread principal.
 * @author Jose Paulmard
 * @implementedBy Joao Lopes
 * @commentedBy Joao Lopes
 */
public class PlayingWithExecutorAndRunnables {

	public static void main(String[] args) {

		Runnable task = () -> System.out.println("I am in threa " + Thread.currentThread().getName());

//		ExecutorService executorService = Executors.newSingleThreadExecutor();
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		
// 		criar threads assim faz com que a JVM crie realmente uma thread para cada tarefa e apos a execucao de cada uma delas destroi a thread
//		for (int i = 0; i < 10; i++) {
//			new Thread(task).start();
//		}

		for (int i = 0; i < 10; i++) {
			executorService.submit(task);
		}
		
		executorService.shutdown();
		
	}

}
