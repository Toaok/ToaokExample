package indi.toaok.http.rx

import android.R
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


/**
 *
 * @author Toaok
 * @version 1.0  2019/11/1.
 */
class LoadingView(context: Context?) : ConstraintLayout(context) {
    init {
        //加载中进度条
        val mProgressBar = ProgressBar(context)
        mProgressBar.id = View.generateViewId()
        val mProgressLayoutParams = LayoutParams(-1, -1)
        mProgressLayoutParams.topToTop = 0
        mProgressLayoutParams.bottomToBottom = 0
        mProgressLayoutParams.startToStart = 0
        mProgressLayoutParams.endToEnd = 0

        //加载提示文本
        val mTextView = TextView(context)
//        mTextView.text = "Loading..."
        mTextView.textSize = 14f
        context?.let {
            //获取主题配色
            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
            mTextView.setTextColor(typedValue.data)
        }
        val mTextLayoutParams = LayoutParams(-1, -1)
        mTextLayoutParams.startToStart = mProgressBar.id
        mTextLayoutParams.endToEnd = mProgressBar.id
        mTextLayoutParams.topToBottom = mProgressBar.id

        //将控件添加到父容器中
        addView(mProgressBar, mProgressLayoutParams)
        addView(mTextView, mTextLayoutParams)
    }
}