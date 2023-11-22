package com.jdk.latest.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class SequencedCollectionsTest {
    @Test
    public void 마지막_요소를_가져온다() {
        List<Integer> result = new ArrayList<>() ;

        IntStream
                .range(0, 100)
                .forEach(result::add);

        Assertions.assertEquals(99, result.getLast());
    }

    @Test
    public void 현재_마지막_요소를_제거한후_마지막_요소를_가져온다() {
        List<Integer> result = new ArrayList<>() ;

        IntStream
                .range(0, 100)
                .forEach(result::add);

        result.remove(99);

        Assertions.assertEquals(98, result.getLast());
    }
}
