package indi.toaok.http.rx

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat

/**
 *
 * @author Toaok
 * @version 1.0  2019/11/1.
 */
class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //遮罩的透明度
        window?.setDimAmount(0f)
        context.let {
            //设置dialog背景
            window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    it,
                    android.R.color.transparent
                )
            )
        }
        window?.setContentView(LoadingView(context))
    }

}