package indi.toaok.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 自定义拦截器添加请求头
 * @author Toaok
 * @version 1.0  2019/10/28.
 */
class CommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var oldRequest = chain.request()
        //
        var authorizedUrlBuilder = oldRequest
            .url()
            .newBuilder()
            .scheme(oldRequest.url().scheme())
            .host(oldRequest.url().host())

        var requestBuilder = oldRequest
            .newBuilder()
            .method(oldRequest.method(), oldRequest.body())
            .url(authorizedUrlBuilder.build())
        /**
         * 已经登陆的用户 带上token
         * requestBuilder.addHeader("Token",eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiOTM5NmRhYmI5MjhhNGI5YmIwYjE4ZTJkNWY4Y2Q5NzIiLCJleHAiOjE1Nzc1ODU3MDksImlhdCI6MTU3NjcyMTcwOX0.KpqW0EFs5G3v3ey0iImezqg71bRvG4Ihz9pX_Uesflc)
         */
        return chain.proceed(requestBuilder.build())
    }
}