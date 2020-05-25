package indi.toaok.http.rx.request

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
interface IRequest<T> {
    fun toRequestBody(): T
}