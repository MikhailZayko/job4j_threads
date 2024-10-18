package ru.job4j.pool;

import ru.job4j.waitnotify.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class ThreadPool {

    private final List<Thread> threads = new LinkedList<>();

    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(4);

    public ThreadPool() {
        IntStream.range(0, Runtime.getRuntime().availableProcessors()).forEach(i -> {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        });
    }

    public void work(Runnable job) {
        try {
            tasks.offer(job);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool();
        IntStream.range(0, 30).forEach(i -> pool.work(() ->
                System.out.println("Задачу №" + i + " выполнил поток " + Thread.currentThread().getName())));
        pool.shutdown();
    }
}