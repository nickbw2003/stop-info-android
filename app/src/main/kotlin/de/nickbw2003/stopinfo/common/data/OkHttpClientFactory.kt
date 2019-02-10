package de.nickbw2003.stopinfo.common.data

import android.os.Build
import de.nickbw2003.stopinfo.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    private const val DEFAULT_TIMEOUT_SECONDS = 60L
    private const val USER_AGENT_HEADER_KEY = "User-Agent"
    private const val USER_AGENT_VALUE_APP_PART = "Stop-Info"
    private const val USER_AGENT_VALUE_PLATFORM_PART = "Android"

    val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                USER_AGENT_HEADER_KEY,
                userAgent
            ).build()
            chain.proceed(request)
        }
        .build()

    private val userAgent: String
        get() {
            return "$USER_AGENT_VALUE_APP_PART/${BuildConfig.VERSION_NAME} $USER_AGENT_VALUE_PLATFORM_PART/${Build.VERSION.RELEASE} (${Build.BRAND} ${Build.MODEL})"
        }
}