package indi.toaok.common.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.lang.ref.WeakReference

/**
 *
 * @author Yun
 * @version 1.0  2019-12-14.
 */
abstract class BaseDelegate : IDelegate {

    override lateinit var rootView: View
    @LayoutRes
    abstract fun getRootLayoutId(): Int

    override fun create(inflater: LayoutInflater, container: ViewGroup?) {
        val rootLayoutId = getRootLayoutId()
        rootView = inflater.inflate(rootLayoutId, container)
    }

    override fun getOptionsMenuId(): Int {
        return 0
    }

    override fun initView() {}
}