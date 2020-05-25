package indi.toaok.http.rx.response

import com.google.gson.annotations.SerializedName


/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
data class BaseResponse<T>(
        override var status: Int = -1,
        @SerializedName("msg")
        override var message: String?,
        override var success: Boolean,
        @SerializedName("dataWrapper")
        var data: T?
) : IResponse {

    companion object {
        val SUCCESS_CODE = 0
    }

    override fun isSuccess(): Boolean {
        return success && status == SUCCESS_CODE
    }
}