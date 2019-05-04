package de.nickbw2003.stopinfo.departures.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import de.nickbw2003.stopinfo.common.data.OkHttpClientFactory
import de.nickbw2003.stopinfo.departures.data.models.Departure
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.WebException
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DeparturesWebClient(baseUrl: String) {
    private interface DeparturesApi {
        @GET("departures/byOriginStation/{originStationId}")
        fun findByOriginStationAsync(@Path("originStationId") originStationId: String, @Header("x-network") network: String): Deferred<Response<List<Departure>>>
    }

    private val departuresApi: DeparturesApi = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(OkHttpClientFactory.client)
        .build()
        .create()

    suspend fun findByOriginStation(originStationId: String, network: String): List<Departure>? {
        val response: Response<List<Departure>>?

        try {
            response = departuresApi.findByOriginStationAsync(originStationId, network).await()
        } catch (ex: Exception) {
            throw WebException(
                Error.DATA_LOADING_ERROR_DEPARTURES_BY_ORIGIN_STATION,
                WebException.UNKNOWN_RESPONSE_CODE,
                ex
            )
        }

        if (response.isSuccessful) {
            return response.body()
        } else {
            throw WebException(
                Error.DATA_LOADING_ERROR_DEPARTURES_BY_ORIGIN_STATION,
                response.code()
            )
        }
    }
}