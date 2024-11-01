package ru.job4j.pool.matrixsums;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        return IntStream.range(0, matrix.length)
                .mapToObj(i -> getSums(matrix, i))
                .toArray(Sums[]::new);
    }

    public static Sums[] asyncSum(int[][] matrix) {
        int size = matrix.length;
        CompletableFuture<Sums>[] futures = new CompletableFuture[size];
        IntStream.range(0, size).forEach(i ->
                futures[i] = CompletableFuture.supplyAsync(() -> getSums(matrix, i)));
        return IntStream.range(0, size)
                .mapToObj(i -> futures[i].join())
                .toArray(Sums[]::new);
    }

    private static Sums getSums(int[][] matrix, int i) {
        int rowSum = 0;
        int colSum = 0;
        for (int j = 0; j < matrix.length; j++) {
            rowSum += matrix[i][j];
            colSum += matrix[j][i];
        }
        return new Sums(rowSum, colSum);
    }
}