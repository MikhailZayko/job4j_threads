package ru.job4j.nba;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {

    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int currentValue;
        do {
            currentValue = count.get();
        } while (!count.compareAndSet(currentValue, currentValue + 1));
    }

    public int get() {
        return count.get();
    }
}