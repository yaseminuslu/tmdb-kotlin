package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.*
import io.ktor.client.*
import io.ktor.client.request.*

class TmdbMovieApi(private val client: HttpClient) {

    suspend fun getDetails(id: Int, language: String, appendResponses: Iterable<AppendResponse>? = null): TmdbMovieDetail = client.get {
        endPoint("movie", id.toString())

        parameterLanguage(language)
        appendResponses?.let { parameterAppendResponses(it) }
    }

    suspend fun getTranslations(id: Int): TmdbTranslations = client.get {
        endPoint("movie", id.toString(), "translations")
    }

    suspend fun getWatchProviders(id: Int): TmdbProviderResult = client.get {
        endPoint("movie", id.toString(), "watch", "providers")
    }

    suspend fun getExternalIds(id: Int): TmdbExternalIds = client.get {
        endPoint("movie", id.toString(), "external_ids")
    }

}
