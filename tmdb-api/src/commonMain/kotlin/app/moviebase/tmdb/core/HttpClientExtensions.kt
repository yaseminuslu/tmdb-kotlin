package app.moviebase.tmdb.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.pipeline.*

internal suspend inline fun <reified T> HttpClient.getByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = get(urlString = buildPaths(*paths), block = block).body()

internal suspend inline fun <reified T> HttpClient.postByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {},
): T = post(urlString = buildPaths(*paths), block = block).body()

internal suspend inline fun <reified T> HttpClient.deleteByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {},
): T = delete(urlString = buildPaths(*paths), block = block).body()

private fun buildPaths(vararg paths: String): String = paths.joinToString(separator = "/")

typealias RequestInterceptor = suspend (HttpRequestBuilder) -> Unit
typealias ResponseInterceptor = suspend (HttpClientCall) -> Unit

internal fun HttpClient.interceptRequest(phase: PipelinePhase = HttpRequestPipeline.Render, interceptor: RequestInterceptor) =
    requestPipeline.intercept(phase) { interceptor(context) }

/**
 * Interceptor for throwing an exception must run before [HttpResponsePipeline.Transform] phase.
 */
internal fun HttpClient.interceptResponse(phase: PipelinePhase = HttpResponsePipeline.Parse, interceptor: ResponseInterceptor) =
    responsePipeline.intercept(phase) { interceptor(context) }