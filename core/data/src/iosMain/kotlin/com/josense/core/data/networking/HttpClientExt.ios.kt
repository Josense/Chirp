package com.josense.core.data.networking

import com.josense.core.domain.DataError
import com.josense.core.domain.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend HttpResponse.() -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        execute().handleResponse()
    } catch(e: IOException) {
        handleDarwinException(e.cause as? NSError)
    } catch(e: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}

private fun handleDarwinException(nsError: NSError?): Result<Nothing, DataError.Remote> {

    return if(nsError?.domain == NSURLErrorDomain) {
        when(nsError?.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed -> {
                Result.Failure(DataError.Remote.NO_INTERNET)
            }

            NSURLErrorTimedOut -> Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
            else -> Result.Failure(DataError.Remote.UNKNOWN)
        }
    } else Result.Failure(DataError.Remote.UNKNOWN)
}