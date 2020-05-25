package indi.toaok.http.exception

import com.google.gson.JsonParseException
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.HTTP_ERROR
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.NETWORK_ERROR
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.PARSE_ERROR
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.PERMISSION_ERROR
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.RETURN_ERROR
import indi.toaok.http.exception.ExceptionEngine.ERROR.Companion.UNKNOWN
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.text.ParseException

/**
 *
 * @author Toaok
 * @version 1.0  2019/11/1.
 */
class ExceptionEngine {
    companion object {
        private const val ERROR_FORBINDDEN = "登录已过期，请重新登录"
        private const val ERROR_SERVER_TRYAGAIN = "遇到错误了，请稍后再试"
        private const val ERROR_CONNET_TRYAGAIN = "网络不太好，请检查网络再试试"
        const val ERROR_UNKNOWN = "恭喜你，触发程序意外关闭功能，该程序员已祭天"

        /**
         * 401-未经授权：访问由于凭据无效被拒绝
         */
        private const val UNAUTHORIZED = 401

        /**
         * 403-资源不可用。服务器接收了请求，但拒绝处理它
         */
        private const val FORBIDDEN = 403

        /**
         * 404-请求资源不存在
         */
        private const val NOT_FOUND = 404

        /**
         * 408-请求超时
         */
        private const val REQUEST_TIMEOUT = 408

        /**
         * 500-服务器内部错误
         */
        private const val INTERNAL_SERVER_ERROR = 500

        /**
         * 502-网关错误
         */
        private const val BAD_GATEWAY = 502
        /**
         * 503-服务不可用
         */
        private const val SERVER_UNAVAILABLE = 503
        /**
         * 504-网关超时
         */
        private const val GATEWAY_TIMEOUT = 504


        fun handleException(e: Throwable): ApiException? {
            if (e is HttpException) {
                /**
                 * 1网络协议异常
                 */
                return when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN -> {
                        //权限错误
                        ApiException(PERMISSION_ERROR, ERROR_FORBINDDEN, e)
                    }
                    REQUEST_TIMEOUT, GATEWAY_TIMEOUT -> {
                        //网络错误
                        ApiException(NETWORK_ERROR, ERROR_CONNET_TRYAGAIN, e)
                    }
                    NOT_FOUND, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVER_UNAVAILABLE -> {
                        //服务器错误
                        ApiException(NETWORK_ERROR, ERROR_SERVER_TRYAGAIN, e)
                    }
                    else -> {
                        ApiException(HTTP_ERROR, ERROR_UNKNOWN, e)
                    }
                }
            } else if (e is ConnectException || e is SocketException) {
                /**
                 * 2连接异常
                 */
                return ApiException(NETWORK_ERROR, ERROR_CONNET_TRYAGAIN, e)
            } else if (e is ServerException) {
                /**
                 * 3服务器返回异常
                 */
                return ApiException(RETURN_ERROR, e.message, e)
            } else if (e is JsonParseException || e is JSONException || e is ParseException) {
                /**
                 * 4解析错误
                 */
                return ApiException(PARSE_ERROR, ERROR_SERVER_TRYAGAIN, e)
            } else {
                /**
                 * 5未知错误
                 */
                return e.message?.let { ApiException(UNKNOWN, it, e) }
            }
        }

    }

    class ERROR {
        companion object {
            /**
             * 未知错误
             */
            const val UNKNOWN = 1000

            /**
             * 解析错误
             */
            const val PARSE_ERROR = 1001
            /**
             * 网络错误
             */
            const val NETWORK_ERROR = 1002

            /**
             * 协议错误
             */
            const val HTTP_ERROR = 1003

            /**
             * 权限
             */
            const val PERMISSION_ERROR = 1004

            /**
             * 返回结果错误
             */
            const val RETURN_ERROR = 1005

        }
    }
}