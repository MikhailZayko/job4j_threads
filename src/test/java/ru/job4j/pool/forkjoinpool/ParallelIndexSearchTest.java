package ru.job4j.pool.forkjoinpool;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class ParallelIndexSearchTest {

    @Test
    void whenTypeIsString() {
        String[] array = new String[] {"One", "Two", "Three", "Four"};
        int result = ParallelIndexSearch.search("Three", array);
        assertThat(result).isEqualTo(2);
    }

    @Test
    void whenTypeIsInteger() {
        Integer[] array = new Integer[] {4, 6, 3, 9, 55, 4567};
        int result = ParallelIndexSearch.search(55, array);
        assertThat(result).isEqualTo(4);
    }

    @Test
    void whenTheArraySizeIsLarge() {
        Integer[] array = new Integer[40000];
        IntStream.range(0, array.length).forEach(i -> array[i] = i);
        int result = ParallelIndexSearch.search(33333, array);
        assertThat(result).isEqualTo(33333);
    }

    @Test
    void whenElementNotFound() {
        Integer[] array = new Integer[4444];
        IntStream.range(0, array.length).forEach(i -> array[i] = i);
        int result = ParallelIndexSearch.search(5555, array);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void whenTheTargetIsTheLastElement() {
        Integer[] array = new Integer[555778];
        IntStream.range(0, array.length).forEach(i -> array[i] = i);
        int result = ParallelIndexSearch.search(array.length - 1, array);
        assertThat(result).isEqualTo(555777);
    }
}