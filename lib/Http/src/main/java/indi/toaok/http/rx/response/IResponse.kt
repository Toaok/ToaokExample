package indi.toaok.http.rx.response

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
interface IResponse {
    var status: Int
    var message:String?
    var success:Boolean

    fun isSuccess():Boolean
}