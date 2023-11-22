package com.jdk.latest.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StructuredThreadTest {
    @Test
    public void 중간_작업이_실패하면_모든_작업이_종료된다() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                Supplier<List<String>> list = scope.fork(this::list);
                Supplier<String> str = scope.fork(this::str);
                list.get().forEach(System.out::println); // 찍히지 않는다.

                scope.join().throwIfFailed();

                assertEquals("This is String", str.get());

            } catch(InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public String str() {
        throw new RuntimeException();
    }

    public List<String> list() {
        List<String> result = new ArrayList<>() ;

        IntStream
                .range(0, 100)
                .forEach(index -> result.add("This is list_" + index));

        return result;
    }
}
