package indi.toaok.common.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

/**
 *
 * @author Yun
 * @version 1.0  2019-12-14.
 */
interface IDelegate {
    var rootView: View
    var toolbar: Toolbar

    abstract fun create(inflater: LayoutInflater, container: ViewGroup?)

    abstract fun getOptionsMenuId(): Int

    abstract fun initView()
}