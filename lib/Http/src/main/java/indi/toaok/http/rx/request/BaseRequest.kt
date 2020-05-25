package indi.toaok.http.rx.request

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
class BaseRequest constructor(body: Any) : IRequest<Map<String, Any?>> {

    private var body = body

    override fun toRequestBody(): Map<String, Any?> {
        return convertToMap(body)
    }

    private fun convertToMap(obj: Any): HashMap<String, Any?> {
        var map = HashMap<String, Any?>()
        for (field in obj.javaClass.declaredFields) {
            var accessFlag = field.isAccessible
            field.isAccessible = true
            var o = field.get(obj)
            map[field.name] = o?.toString()?:""
            field.isAccessible = accessFlag
        }
        return map
    }
}