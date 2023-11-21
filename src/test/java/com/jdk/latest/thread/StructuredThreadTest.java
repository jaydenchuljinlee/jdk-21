package com.jdk.latest.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StructuredThreadTest {
    @Test
    public void test() {
        Assertions.assertThrows(InterruptedException.class, () -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                Supplier<String> str = scope.fork(this::str);
                Supplier<List<String>> list = scope.fork(this::list);

                scope.join().throwIfFailed();

                assertEquals("This is String", str.get());

            } catch(InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String str() {
        return "This is String";
    }

    public List<String> list() {
        List<String> result = new ArrayList<>();
        result.add("This is list");
        throw new RuntimeException();
    }
}
