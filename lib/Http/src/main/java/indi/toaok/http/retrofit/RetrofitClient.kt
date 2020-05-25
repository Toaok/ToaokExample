package indi.toaok.http.retrofit

import android.util.Log
import indi.toaok.http.interceptor.CommonInterceptor
import indi.toaok.utils.core.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/28.
 */
class RetrofitClient {
    companion object {
        private const val TAG = "Toaok"
        private const val READ_TIME_OUT = 10000L
        private const val CONNECT_TIME_OUT = 10000L
        private const val MAX_STALE = 10
        private const val MAX_AGE = 6

        fun <T> createApi(clazz: Class<T>, baseUrl: String): T {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build()
                    .create(clazz)
        }

        private fun getClient(): OkHttpClient {
            var sslContext: SSLContext = SSLContext.getInstance("TLS")
            sslContext.init(null, Array<TrustManager>(1) {
                object : X509TrustManager {
                    override fun checkClientTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            }, SecureRandom())
            var sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            return OkHttpClient.Builder()
                    .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(getInterceptor())
                    .addNetworkInterceptor(getInterceptor())
                    .addInterceptor(CommonInterceptor())
                    .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger(fun(it: String) {
                        Log.d(TAG, it)
                    })).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .sslSocketFactory(sslSocketFactory, object : X509TrustManager {
                        override fun checkClientTrusted(
                                chain: Array<out X509Certificate>?,
                                authType: String?
                        ) {

                        }

                        override fun checkServerTrusted(
                                chain: Array<out X509Certificate>?,
                                authType: String?
                        ) {

                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    })
                    .hostnameVerifier { _, _ -> true }
                    .build()
        }

        private fun getInterceptor(): Interceptor {
            return Interceptor(fun(chain: Interceptor.Chain): Response? {
                return if (NetworkUtils.isConnected())
                    chain.proceed(
                            chain.request()
                    ).newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=$MAX_AGE")
                            .build()
                else
                    chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .cacheControl(
                                            CacheControl.Builder()
                                                    .onlyIfCached()
                                                    .maxStale(MAX_STALE, TimeUnit.SECONDS)
                                                    .build()
                                    ).build()
                    ).newBuilder()
                            .build()
            })
        }
    }
}