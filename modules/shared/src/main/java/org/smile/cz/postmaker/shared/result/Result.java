package org.smile.cz.postmaker.shared.result;

import java.util.function.Function;

public sealed interface Result<T> permits Result.Success, Result.Failure {

    record Success<T>(T value) implements Result<T> { }

    record Failure<T>(ErrorCode errorCode) implements Result<T> { }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(ErrorCode error) {
        return new Failure<>(error);
    }


    default boolean isSuccess() {
        return this instanceof Success<T>;
    }

    default boolean isFailure() {
        return this instanceof Failure<T>;
    }

    default <R> Result<R> map(Function<? super T, ? extends R> mapper) {

        if (this instanceof Success<T>(T value)) {
            return Result.success(mapper.apply(value));
        }

        Failure<T> failure = (Failure<T>) this;
        return Result.failure(failure.errorCode);

    }

    default <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
        if (this instanceof Success<T>(T value)) {
            return mapper.apply(value);
        }

        Failure<T> failure = (Failure<T>) this;
        return Result.failure(failure.errorCode);
    }

}
