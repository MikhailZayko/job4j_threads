package ru.job4j.nba;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {

    @Test
    void whenOneThread50Then50() throws InterruptedException {
        CASCount count = new CASCount();
        Thread thread = new Thread(() -> IntStream.range(0, 50).forEach(i -> count.increment()));
        thread.start();
        thread.join();
        assertThat(count.get()).isEqualTo(50);
    }

    @Test
    void whenTwoThreads50Then100() throws InterruptedException {
        CASCount count = new CASCount();
        Thread firstThread = new Thread(() -> IntStream.range(0, 50).forEach(i -> count.increment()));
        Thread secondThread = new Thread(() -> IntStream.range(0, 50).forEach(i -> count.increment()));
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();
        assertThat(count.get()).isEqualTo(100);
    }
}