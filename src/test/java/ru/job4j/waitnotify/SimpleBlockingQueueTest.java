package ru.job4j.waitnotify;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    private Thread producer(SimpleBlockingQueue<Integer> queue, int iterations) {
        return new Thread(() -> {
            try {
                for (int i = 0; i < iterations; i++) {
                    queue.offer(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private Thread consumer(SimpleBlockingQueue<Integer> queue, int iterations) {
        return new Thread(() -> {
            try {
                for (int i = 0; i < iterations; i++) {
                    queue.poll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Test
    void whenOfferAndPollAreCorrect() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = producer(queue, 5);
        Thread consumer = consumer(queue, 4);
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll()).isEqualTo(4);
    }

    @Test
    void whenBlockingProducer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = producer(queue, 4);
        Thread consumer = consumer(queue, 3);
        producer.start();
        Thread.sleep(500);
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll()).isEqualTo(3);
    }

    @Test
    void whenBlockingConsumer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = producer(queue, 3);
        Thread consumer = consumer(queue, 2);
        consumer.start();
        Thread.sleep(200);
        producer.start();
        producer.join();
        consumer.join();
        assertThat(queue.poll()).isEqualTo(2);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = producer(queue, 7);
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4, 5, 6)
                .hasSize(7)
                .doesNotContainNull();
    }
}