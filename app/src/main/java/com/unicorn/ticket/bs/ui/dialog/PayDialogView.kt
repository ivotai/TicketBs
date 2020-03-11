package com.unicorn.ticket.bs.ui.dialog

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.di.ComponentHolder
import com.unicorn.ticket.bs.app.helper.DialogHelper.showMask
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.GetInventoryEvent
import com.unicorn.ticket.bs.data.event.RestoreEvent
import com.unicorn.ticket.bs.data.model.*
import com.unicorn.ticket.bs.ui.adapter.PayTypeSAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dialog_pay_type.view.*
import java.net.SocketTimeoutException

class PayDialogView(private val mContext: Context, sum: Double, private val orderId: Long) :
    FrameLayout(mContext), LayoutContainer {

    private val payTypeSAdapter = PayTypeSAdapter()

    private val soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)

    init {
        soundPool.load(context, R.raw.audio, 1)

        LayoutInflater.from(context).inflate(R.layout.dialog_pay_type, this, true)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            payTypeSAdapter.bindToRecyclerView(this)
            HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.md_grey_400)
                .size(1)
                .margin(ConvertUtils.dp2px(32f))
                .build().let { addItemDecoration(it) }
        }

        payTypeSAdapter.setNewData(PayTypeS.all)
        tvSum.text = "需要收款：￥${sum}"
        tvSum2.text = "已收款：￥${sum}"

        RxBus.registerEvent(context as LifecycleOwner, PayTypeS::class.java, Consumer {
            Glide.with(context).load(it.payType.img).into(ivImg)
            tvPrompt.text = it.payType.prompt
            rtvPaymentReceived.visibility =
                if (it.payType == PayType.QRCode) View.GONE else View.VISIBLE
        })
        // 默认选中扫码付款
        RxBus.post(PayTypeS(payType = PayType.QRCode, isSelect = true))


        rtvCancel.safeClicks().subscribe {
            dialog.doDismiss()
        }
        rtvComplete.safeClicks().subscribe {
            RxBus.post(RestoreEvent())
            dialog.doDismiss()
        }
        rtvPrintTicket.safeClicks().subscribe {
            RxBus.post(TakeTicketParam(orderId))
        }
        rtvPaymentReceived.safeClicks().subscribe {
            payOrder()
        }
    }

    private var paying = false

    private fun payOrder(authCode: String = "0") {
        paying = true
        val i = PayOrderParam(orderId = orderId, payType = payType.type, authCode = authCode)
        val mask = showMask(context!!)
        ComponentHolder.appComponent.api().payOrder(i)
            .observeOnMain(mContext as LifecycleOwner)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
                    paying = false
                    if (it.failed) return@subscribeBy
                    payTypeSAdapter.payFinish = true
                    soundPool.play(1, 1f, 1f, 10, 0, 1f)
                    constraintLayout1.visibility = View.INVISIBLE
                    constraintLayout2.visibility = View.VISIBLE
                    if (SystemInfo.autoPrint) RxBus.post(TakeTicketParam(orderId))
                    RxBus.post(GetInventoryEvent())
                },
                onError = {
                    mask.dismiss()
                    paying = false
                    if (it is SocketTimeoutException) {
                        ToastUtils.showShort("支付超时")
                        return@subscribeBy
                    }
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private val payType: PayType get() = payTypeSAdapter.data.first { it.isSelect }.payType

    lateinit var dialog: CustomDialog

    override val containerView = this

    private val sb = StringBuilder()
    private val myHandler = Handler()

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_MENU) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_HOME) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_POWER) {
                    return super.dispatchKeyEvent(event)
                }
                val unicodeChar = event.unicodeChar
                sb.append(unicodeChar.toChar())
                len++
                startScan()
                return true
            }
            else -> {
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private var isScaning = false
    private var len = 0
    private var oldLen = 0

    private fun startScan() {
        if (isScaning) {
            return
        }
        isScaning = true
        timerScanCal()
    }

    private fun timerScanCal() {
        oldLen = len
        myHandler.postDelayed(scan, 100)
    }


    private var scan: Runnable = Runnable {
        if (oldLen != len) {
            timerScanCal()
            return@Runnable
        }
        isScaning = false
        if (sb.isNotEmpty()) {
            val result = sb.substring(0, sb.length - 2)

            // 干正事
            if (payType == PayType.QRCode && !paying && !payTypeSAdapter.payFinish) {
                payOrder(authCode = result)
            }

            sb.setLength(0)
        }
    }

}