package io.github.resilience4j.core;

import io.vavr.CheckedFunction0;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckFunctionUtilsTest {

    @Test
    public void shouldRecoverCallableFromException() throws Throwable {
        CheckedFunction0<String> callable = () -> {
            throw new IOException("BAM!");
        };
        CheckedFunction0<String> callableWithRecovery = CheckFunctionUtils.recover(callable, (ex) -> "Bla");

        String result = callableWithRecovery.apply();

        assertThat(result).isEqualTo("Bla");
    }

    @Test(expected = RuntimeException.class)
    public void shouldRethrowException() throws Throwable {
        CheckedFunction0<String> callable = () -> {
            throw new IOException("BAM!");
        };
        CheckedFunction0<String> callableWithRecovery = CheckFunctionUtils.recover(callable, (ex) -> {
            throw new RuntimeException();
        });

        callableWithRecovery.apply();
    }

    @Test(expected = RuntimeException.class)
    public void shouldRethrowException2() throws Throwable {
        CheckedFunction0<String> callable = () -> {
            throw new RuntimeException("BAM!");
        };
        CheckedFunction0<String> callableWithRecovery = CheckFunctionUtils.recover(callable, IllegalArgumentException.class, (ex) -> "Bla");

        callableWithRecovery.apply();
    }
}