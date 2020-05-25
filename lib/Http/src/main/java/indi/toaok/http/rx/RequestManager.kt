package indi.toaok.http.rx

import indi.toaok.http.util.HostUtil
import indi.toaok.http.retrofit.RetrofitClient

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/28.
 */
class RequestManager {
    companion object {
        private var requestManager = HashMap<Class<*>, Any?>()

        @SuppressWarnings("unchecked")
        fun <T> getRequest(clazz: Class<T>, serverHost: String): T {
            if (!clazz.isInterface) throw IllegalArgumentException("API declarations must be interfaces.")
            var t: T = requestManager[clazz] as T
            if (t == null) {
                t = RetrofitClient.createApi(clazz, serverHost)
                requestManager[clazz] = t
            }
            return t
        }

        fun <T> getRequest(clazz: Class<T>): T {
            return getRequest(clazz, HostUtil.HOST_URL_DEFAULT)
        }
    }
}