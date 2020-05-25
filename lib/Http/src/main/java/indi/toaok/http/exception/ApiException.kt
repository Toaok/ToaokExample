package indi.toaok.http.exception

/**
 *
 * @author Toaok
 * @version 1.0  2019/11/1.
 */
class ApiException(val code: Int, override val message: String?, cause: Throwable?) :
    Exception(message, cause) {
    init {
        if (cause is ServerException) {

        }
    }
}