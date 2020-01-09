package com.unicorn.ticket.bs.app.helper

import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils
import com.kaopiz.kprogresshud.KProgressHUD
import com.unicorn.ticket.bs.MainAct
import com.unicorn.ticket.bs.app.*
import com.unicorn.ticket.bs.app.di.ComponentHolder
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.Call
import java.io.File

object UpdateHelper {

    fun checkVersion(activity: AppCompatActivity) {
        val api = ComponentHolder.appComponent.api()
        api.checkVersion()
            .observeOnMain(activity)
            .subscribeBy(
                onSuccess = {
                    if (it.version != AppUtils.getAppVersionName())
                        download(activity = activity, apkUrl = it.url)
                    else
                        activity.startAct(MainAct::class.java)
                },
                onError = {
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun download(activity: AppCompatActivity, apkUrl: String) {
        val mask = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
            .setCancellable(true)
            .setDimAmount(0.5f)
            .setMaxProgress(100)
            .show()
        OkHttpUtils
            .get()
            .url(apkUrl)
            .addHeader(Cookie, "${SESSION}=${Globals.session}")
            .build()
            .execute(object : FileCallBack(
                activity.cacheDir.path,
                "TicketBs.apk"
            ) {
                override fun onResponse(response: File, id: Int) {
                    mask.dismiss()
                    AppUtils.installApp(response)
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    val p = (100 * progress).toInt()
                    mask.setProgress(p)
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    mask.dismiss()
                }
            })
    }

}