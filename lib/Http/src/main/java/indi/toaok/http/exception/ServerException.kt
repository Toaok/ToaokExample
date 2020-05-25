package indi.toaok.http.exception

import java.lang.RuntimeException

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/30.
 */

class ServerException @JvmOverloads constructor(
    override var message: String?,
    var code: Int,
    var obj: Any? = null
) : RuntimeException()
