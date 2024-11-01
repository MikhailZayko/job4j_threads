package ru.job4j.pool.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {

    private final T target;

    private final T[] array;

    private final int from;

    private final int to;

    public ParallelIndexSearch(T target, T[] array, int from, int to) {
        this.target = target;
        this.array = array;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return linearIndexSearch();
        }
        int middle = (from + to) / 2;
        ParallelIndexSearch<T> leftSearch = new ParallelIndexSearch<>(target, array, from, middle);
        ParallelIndexSearch<T> rightSearch = new ParallelIndexSearch<>(target, array, middle + 1, to);
        leftSearch.fork();
        rightSearch.fork();
        Integer left = leftSearch.join();
        Integer right = rightSearch.join();
        return left != -1 ? left : right;
    }

    private Integer linearIndexSearch() {
        int result = -1;
        for (int i = from; i <= to; i++) {
            if (target.equals(array[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static <T> int search(T target, T[] array) {
        return ForkJoinPool.commonPool()
                .invoke(new ParallelIndexSearch<>(target, array, 0, array.length - 1));
    }
}