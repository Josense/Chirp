package com.josense.core.domain

sealed class Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
}

fun <T, E: Error, R> Result<T, E>.map(f: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(f(this.data))
        is Result.Failure -> this
    }
}

fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> {
            action(this.data)
            this
        }
        is Result.Failure -> this
    }
}

fun <T, E: Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> this
        is Result.Failure -> {
            action(this.error)
            this
        }
    }
}

typealias EmptyResult<E> = Result<Unit, E>
fun <T, E: Error> Result<T, E>.toEmptyResult(): EmptyResult<E> {
    return map { Unit }
}
