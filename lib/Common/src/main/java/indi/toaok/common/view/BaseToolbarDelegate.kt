package indi.toaok.common.view

import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import indi.toaok.common.R

/**
 *
 * @author Yun
 * @version 1.0  2019-12-13.
 */
class BaseToolbarDelegate : BaseDelegate() {

    private val TAG = BaseToolbarDelegate::class.java.simpleName

    lateinit var tvLeft: TextView
    lateinit var ivLeft: ImageView


    lateinit var tvTitle: TextView
    lateinit var ivTitle: ImageView

    lateinit var tvRight: TextView
    lateinit var ivRight: ImageView

    override lateinit var toolbar: Toolbar

    override fun getRootLayoutId(): Int {
        return R.layout.layout_toolbar
    }


    override fun initView() {
        tvLeft = rootView.findViewById(R.id.tvLeft)
        ivLeft = rootView.findViewById(R.id.ivLeft)
        tvTitle = rootView.findViewById(R.id.tvTitle)
        ivTitle = rootView.findViewById(R.id.ivTitle)
        tvRight = rootView.findViewById(R.id.tvRight)
        ivRight = rootView.findViewById(R.id.ivRight)
        toolbar = rootView.findViewById(R.id.toolbar)
    }

    fun setBackgroundColor(color: Int) {
        toolbar.setBackgroundColor(color)
    }

    fun setLeftText(leftText: CharSequence) {
        tvLeft.visibility = VISIBLE
        tvLeft.text = leftText
    }

    fun setLeftImage(@DrawableRes drawableResId: Int) {
        ivLeft.visibility = VISIBLE
        ivLeft.setImageResource(drawableResId)
    }

    fun setTitle(title: CharSequence) {
        tvTitle.visibility = VISIBLE
        tvTitle.text = title
    }

    fun setTitle(@StringRes title: Int) {
        if (ivTitle.visibility == VISIBLE) {
            ivTitle.visibility = GONE
        }

        tvTitle.visibility = VISIBLE
        tvTitle.setText(title)
    }

    fun setTitleDrawable(title: Drawable) {
        if (tvTitle.visibility == VISIBLE) {
            tvTitle.visibility = GONE
        }

        ivTitle.visibility = VISIBLE
        ivTitle.setImageDrawable(title)
    }

    fun setTitleResource(@DrawableRes title: Int) {
        if (tvTitle.visibility == VISIBLE) {
            tvTitle.visibility = GONE
        }
        ivTitle.visibility = VISIBLE
        ivTitle.setImageResource(title)
    }

    fun setRightText(rightText: CharSequence) {
        tvRight.visibility = VISIBLE
        tvRight.text = rightText
    }

    fun setRightImage(@DrawableRes drawableResId: Int) {
        ivRight.visibility = VISIBLE
        ivRight.setImageResource(drawableResId)
    }


    @IntDef(value = [VISIBLE, INVISIBLE, GONE])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Visibility

    fun setLeftVisiable(@Visibility visiable: Int) {
        tvLeft.visibility = visiable
        ivLeft.visibility = visiable
    }

    fun setCenterVisiable(@Visibility visiable: Int) {
        tvTitle.visibility = visiable
        ivTitle.visibility = visiable
    }

    fun setRightVisiable(@Visibility visiable: Int) {
        tvRight.visibility = visiable
        ivRight.visibility = visiable
    }

    fun setToolbarVisiable(@Visibility visiable: Int) {
        toolbar.visibility = visiable
    }

    fun setImageBackListener(l: OnClickListener) {
        ivLeft.visibility = VISIBLE
        ivLeft.setOnClickListener(l)
    }


    fun setLeftClickListener(l: OnClickListener) {
        ivLeft.setOnClickListener(l)
        tvLeft.setOnClickListener(l)
    }

    fun setRightClickListener(l: OnClickListener) {
        ivRight.setOnClickListener(l)
        tvRight.setOnClickListener(l)
    }

    fun addView(view: View) {

    }
}