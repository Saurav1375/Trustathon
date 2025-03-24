package com.example.trustoken_starter.core.domain.util

// Type alias for representing domain-specific errors
typealias DomainError = Error

/**
 * A sealed interface representing the result of an operation.
 * It can either be a Success with data or an Error with a specific error type.
 */
sealed interface Result<out D, out E: Error> {

    /**
     * Represents a successful operation with the resulting data.
     *
     * @param D The type of data returned on success.
     * @property data The actual data returned from a successful operation.
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>

    /**
     * Represents a failed operation with an associated error.
     *
     * @param E The type of error returned on failure.
     * @property error The error details.
     */
    data class Error<out E: DomainError>(val error: E) : Result<Nothing, E>
}

/**
 * Maps a successful result to a new type while preserving errors.
 *
 * @param map A function that transforms the success data into a new type.
 * @return A new [Result] containing either transformed data or the original error.
 */
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error) // Preserve error state
        is Result.Success -> Result.Success(map(data)) // Transform success data
    }
}

/**
 * Converts a successful result into an empty result, preserving errors.
 *
 * @return An [EmptyResult] that contains only the error if present.
 */
fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { } // Maps success to an empty result
}

/**
 * Executes an action only if the result is successful.
 *
 * @param action A function to execute when the result is [Result.Success].
 * @return The original [Result] to allow for method chaining.
 */
inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this // Do nothing on error
        is Result.Success -> {
            action(data) // Execute the action
            this
        }
    }
}

/**
 * Executes an action only if the result is an error.
 *
 * @param action A function to execute when the result is [Result.Error].
 * @return The original [Result] to allow for method chaining.
 */
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error) // Execute the action
            this
        }
        is Result.Success -> this // Do nothing on success
    }
}

// Type alias for a result that does not return data but may contain an error.
typealias EmptyResult<E> = Result<Unit, E>
