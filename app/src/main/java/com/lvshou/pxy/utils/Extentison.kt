import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.lvshou.pxy.constant.Constant
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.JobCancellationException
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Log
 */
fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

/**
 * show toast
 * @param content String
 */
@SuppressLint("ShowToast")
fun Context.toast(content: String) {
    Constant.showToast?.apply {
        setText(content)
        show()
    } ?: run {
        Toast.makeText(this.applicationContext, content, Toast.LENGTH_SHORT).apply {
            Constant.showToast = this
        }.show()
    }
}

/**
 * show toast
 * @param id strings.xml
 */
fun Context.toast(@StringRes id: Int) {
    toast(getString(id))
}

fun Deferred<Any>?.cancelByActive() = this?.run {
    tryCatch {
        if (isActive) {
            cancel()
        }
    }
}

/**
 * save cookie string
 */
fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
            .map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            .forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}

/**
 * get random color
 * @return 16777215 is FFFFFF, 0 is 000000
 */
//fun getRandomColor(): String = "#${Integer.toHexString((Math.random() * 16777215).toInt())}"

fun getRandomColor(): String {
//    val ranColor = intArrayOf(0x00FF4500, 0x00FF1493, -0x00FFD700, 0x00E066FF, 0x00CDAD00, 0x007CFC00) //0xff000000 | random.nextInt(0x00ffffff);
    val ranColor = mutableListOf("#FF4500", "#EEC900", "#EE2C2C", "#CD69C9", "#CDAD00", "#836FFF") //0xff000000 | random.nextInt(0x00ffffff);
    return ranColor[((Math.random() * 6).toInt())]
// return   "#${Integer.toHexString((Math.random() * 16777215).toInt())}"
}

/**
 * getAgentWeb
 */
fun String.getAgentWeb(
        activity: Activity, webContent: ViewGroup,
        layoutParams: ViewGroup.LayoutParams,
        receivedTitleCallback: ChromeClientCallbackManager.ReceivedTitleCallback?
) = AgentWeb.with(activity)//传入Activity or Fragment
        .setAgentWebParent(webContent, layoutParams)//传入AgentWeb 的父控件
        .useDefaultIndicator()// 使用默认进度条
        .defaultProgressBarColor() // 使用默认进度条颜色
        .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
        .createAgentWeb()//
        .ready()
        .go(this)!!

/**
 * LayoutInflater.from(this).inflate
 * @param resource layoutId
 * @return View
 */
fun Context.inflater(@LayoutRes resource: Int): View =
        LayoutInflater.from(this).inflate(resource, null)

/**
 * In disappear assist cheng (cancel) will be submitted to the Job Cancellation Exception Exception.
 */
inline fun tryCatch(catchBlock: (Throwable) -> Unit = {}, tryBlock: () -> Unit) {
    try {
        tryBlock()
    } catch (e: JobCancellationException) {
        loge("JobCancellationException:", "${e.message}")

    } catch (t: Throwable) {
        catchBlock(t)
    }
}

//Kotlin 协程版的 AutoDispose
//https://mp.weixin.qq.com/s/7HgjujtgqfuAqEVYGxsl2g
//rxJava https://github.com/uber/AutoDispose
/*fun View.onClickAutoDisposable (
        context: CoroutineContext = Dispatchers.Main,
       handler: suspend CoroutineScope.(v: android.view.View?) -> Unit
) {
    setOnClickListener { v ->
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
                 handler(v)
            }.asAutoDisposable(v)
       }
}*/

