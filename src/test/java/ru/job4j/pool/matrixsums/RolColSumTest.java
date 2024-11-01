package ru.job4j.pool.matrixsums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {

    @Test
    void whenSum() {
        int[][] matrix = {{1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        Sums[] expected = new Sums[]{new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)};
        assertThat(RolColSum.sum(matrix)).isEqualTo(expected);
    }

    @Test
    void whenAsyncSum() {
        int[][] matrix = {{1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}};
        Sums[] expected = new Sums[]{new Sums(10, 28),
                new Sums(26, 32),
                new Sums(42, 36),
                new Sums(58, 40)};
        assertThat(RolColSum.asyncSum(matrix)).isEqualTo(expected);
    }
}