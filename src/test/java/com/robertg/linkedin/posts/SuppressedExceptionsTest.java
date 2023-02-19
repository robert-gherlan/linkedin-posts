package com.robertg.linkedin.posts;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class SuppressedExceptionsTest {

    class PositiveNumbersProcessor implements AutoCloseable {

        private final int number;

        PositiveNumbersProcessor(int number){
            this.number = number;
        }

        int add(int a) {
            if (a < 0){
                throw new IllegalArgumentException("Only values greater or equal to zero are allowed. Received %s instead.".formatted(a));
            }
            return a + number;
        }

        @Override
        public void close() {
            throw new IllegalStateException("Can't call close method.");
        }
    }

    @Test
    void testPositiveNumbersProcessorSuppressedException(){
        assertThatThrownBy(() -> {
            try(var positiveNumbersProcessor = new PositiveNumbersProcessor(-1)){
                System.out.println(positiveNumbersProcessor.add(-1));
            }
        }).hasMessage("Only values greater or equal to zero are allowed. Received -1 instead.")
          .hasSuppressedException(new IllegalStateException("Can't call close method."));
    }

    @Test
    void testMultipleSuppressedExceptions(){
        final var exception = new Exception();
        exception.addSuppressed(new IllegalArgumentException());
        exception.addSuppressed(new IllegalStateException());
        exception.addSuppressed(new NullPointerException());

        assertThat(exception)
                .hasSuppressedException(new IllegalArgumentException())
                .hasSuppressedException(new IllegalStateException())
                .hasSuppressedException(new NullPointerException());
    }
}
