package ru.job4j.concurrent;

import static java.lang.Thread.State.TERMINATED;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        first.start();
        second.start();
        while (first.getState() != TERMINATED || second.getState() != TERMINATED) {
            System.out.println(first.getState() + " first");
            System.out.println(second.getState() + " second");
        }
        System.out.println("Работа завершена");
    }
}