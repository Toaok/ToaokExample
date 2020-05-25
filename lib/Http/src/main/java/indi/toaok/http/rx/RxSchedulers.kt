package indi.toaok.http.rx

import indi.toaok.http.exception.ServerException
import indi.toaok.http.rx.response.IResponse
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 *
 * @author Toaok
 * @version 1.0  2019/10/29.
 */
class RxSchedulers {
    companion object {
        fun <T> rxSchedulerHelper(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }


        fun <T> handleResult(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                return@ObservableTransformer it.flatMap(Function<T, Observable<T>> { response ->
                    if (response is IResponse) {
                        if (response.isSuccess()) {
                            return@Function createData(response)
                        }else{
                            return@Function Observable.error(ServerException(response.message,response.status,response))
                        }
                    }
                    return@Function createData(response)
                })
            }
        }

        private fun <T> createData(t: T): Observable<T> {
            return Observable.create {
                try {
                    it.onNext(t)
                } catch (e: Exception) {
                    it.onError(e)
                }
                it.onComplete()
            }
        }
    }
}