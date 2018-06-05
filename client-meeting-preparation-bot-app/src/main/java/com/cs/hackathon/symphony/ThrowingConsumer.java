package com.cs.hackathon.symphony;

import java.util.function.Consumer;

public interface ThrowingConsumer<T> extends Consumer<T> {

    default void accept(T t) {
        try {
            acceptThrowing(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrowing(T t) throws Exception;
}

