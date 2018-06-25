package com.lvshou.pxy.ui.activity

import android.os.SystemClock
import android.util.Log
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_zip.*
import loge


/**
 * @desc：
 * Created by JamesPxy on 2018/6/25 10:58
 */
class TestZip : BaseActivity() {

//    private var observable1: Observable<HomeListResponse>? = null
//
//    private var observable2: Observable<FriendListResponse>? = null

    private var zipObservable: Observable<*>? = null

    private var result = ""

    override fun init() {
//        observable1=RetrofitHelper.retrofitService.getData1(0)
//        observable2=RetrofitHelper.retrofitService.getData2()

//        zipObservable= zip(observable1,observable2, BiFunction<HomeListResponse,FriendListResponse,String> { t1, t2->
//            var code1=t1.errorMsg
//            t2.errorMsg!!
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(Consumer<String>(){
//
//                },Consumer<Throwable>(){
//
//                })

        val observable1 = Observable.create(ObservableOnSubscribe<String> { e ->
//            Log.d("test", "A")
//            e.onNext("A")
//            SystemClock.sleep(1000)
//            Log.d("test", "B")
//            e.onNext("B")
//            SystemClock.sleep(1000)
//            Log.d("test", "C")
//            e.onNext("C")
//            SystemClock.sleep(1000)
//            Log.d("test", "onComplete")
//            e.onComplete()
            e.onNext("ABC")
            Log.d("test observable1", "ABC")
            SystemClock.sleep(1000)
            Log.d("test observable1", "onComplete")
            e.onComplete()
        }).subscribeOn(Schedulers.io())
        val observable2 = Observable.create(ObservableOnSubscribe<String> { e ->
           /* Log.d("test observable2", "DD")
            e.onNext("DD")
            SystemClock.sleep(1000)
            Log.d("test observable2", "EE")
            e.onNext("EE")
            SystemClock.sleep(1000)
            Log.d("test observable2", "FF")
            e.onNext("FF")
            SystemClock.sleep(1000)
            Log.d("test observable2", "onComplete")
            e.onComplete()*/
            SystemClock.sleep(4000)
            e.onNext("DEF")
            Log.d("test observable2", "DEF")
            SystemClock.sleep(1000)
            Log.d("test observable2", "onComplete")
            e.onComplete()

        }).subscribeOn(Schedulers.io())

        Observable.zip(observable1, observable2, BiFunction<String, String, String> { t1, t2 ->
            t1 + t2
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<String>({
                    loge("test  consumer=", it)
                    textView.text = textView.text.toString() + it
                }), Consumer<Throwable>({
                    loge("test  Throwable=", it.localizedMessage)
                    textView.text = it.localizedMessage
                }))


//        Observable.zip(observable1, observable2, BiFunction<Int, String, String> {
//            @Throws(Exception::class)
//            override fun apply(a: Int?, b: String): String {
//                return a!!.toString() + b
//            }
//        }).subscribe { o -> Log.d("test", o) }


    }

    override fun setLayoutId(): Int = R.layout.activity_test_zip

    override fun cancelRequest() {
    }


}