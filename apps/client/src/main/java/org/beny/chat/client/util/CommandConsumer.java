package org.beny.chat.client.util;

import java.util.Objects;

@FunctionalInterface
public interface CommandConsumer<T> {

    void accept(T t) throws Exception;

    default CommandConsumer<T> andThen(CommandConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

}
