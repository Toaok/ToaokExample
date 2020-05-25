package indi.toaok.http.rx

import android.app.Dialog
import android.content.Context
import indi.toaok.http.exception.ApiException
import indi.toaok.http.exception.ExceptionEngine
import indi.toaok.utils.core.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
abstract class RxSubscriberHelper<T>(
        context: Context? = null,
        var isShowLoad: Boolean = false,
        var isShowMessage: Boolean = true,
        var isCheckPermission: Boolean = true
) : Observer<T> {
    constructor(context: Context? = null, isShowLoad: Boolean) : this(
            context,
            isShowLoad,
            true,
            true
    )

    private var mLoadingDialog: Dialog? = null
    private var weakReference: WeakReference<Context?> = WeakReference(context)

    override fun onComplete() {
        // onDismiss()
    }

    override fun onSubscribe(d: Disposable) {
        if (isShowLoad) {
            onShowLoading()
        }
    }

    override fun onError(e: Throwable) {
        onDismiss()
        val apiException = ExceptionEngine.handleException(e)
        if (isShowMessage) {
            onShowMessage(apiException)
        }

        if (isCheckPermission && apiException?.code == ExceptionEngine.ERROR.PERMISSION_ERROR) {
            onPermissionError(apiException)
        }
    }

    private fun onShowLoading() {
        if (mLoadingDialog == null || weakReference.get()?.equals(mLoadingDialog?.context) == true) {
            mLoadingDialog = weakReference.get()?.let { LoadingDialog(it) }
            mLoadingDialog?.setCancelable(false)
            mLoadingDialog?.show()
        }
    }

    fun onDismiss() {
        if (isShowLoad && mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
    }

    open fun onShowMessage(apiException: ApiException?) {
        ToastUtils.showLong(apiException?.message ?: ExceptionEngine.ERROR_UNKNOWN)
    }

    open fun onPermissionError(apiException: ApiException?) {
    }
}