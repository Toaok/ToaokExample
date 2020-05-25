package indi.toaok.http.util

import indi.toaok.http.BuildConfig


/**
 *
 * @author Toaok
 * @version 1.0  2019/10/28.
 */
class HostUtil {
    companion object {
        //API服务器地址
        const val HOST_URL_DEFAULT: String =BuildConfig.API
        //文件服务器地址
        const val IMAGE_HOST_URL: String = BuildConfig.FileServer
    }
}