package com.unicorn.ticket.bs.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.di.ComponentHolder
import com.unicorn.ticket.bs.app.helper.DialogHelper.showMask
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.CancelOrderEvent
import com.unicorn.ticket.bs.data.event.RefundOrderEvent
import com.unicorn.ticket.bs.data.model.*
import com.unicorn.ticket.bs.ui.adapter.TicketInfoAdapter
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.dialog_order_detail.view.*

class OrderDetailDialogView(private val mContext: Context, order: Order) :
    FrameLayout(mContext), LayoutContainer {

    private val ticketInfoAdapter = TicketInfoAdapter()

    init {

        LayoutInflater.from(context).inflate(R.layout.dialog_order_detail, this, true)

        getOrderDetail(order.orderId)

        rtvOperation.safeClicks().subscribe { showListDialog(order = order) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = ticketInfoAdapter
            addItemDecoration(LinearSpanDecoration(ConvertUtils.dp2px(16f)))
        }
    }

    private fun showListDialog(order: Order) {
        val updateStatus = "更新支付状态"
        val refund = "退款"
        val cancel = "取消"
        val printAll = "全部打印"
        val printRemain = "打印未打印"
        val list = when (order.status) {
            1, 8, 9, 10 -> listOf(updateStatus)
            2 -> listOf(printAll, printRemain, cancel, refund)
            15 -> listOf(updateStatus, printAll, printRemain)
            else -> listOf()
        }
        MaterialDialog(mContext).show {
            listItems(items = list) { _, _, text ->
                when (text) {
                    updateStatus -> queryPayStatus(order)
                    refund -> showConfirmRefundDialog(order.orderId)
                    printAll -> RxBus.post(TakeTicketParam(order.orderId, printType = 1))
                    printRemain -> RxBus.post(TakeTicketParam(order.orderId, printType = 2))
                    cancel -> showConfirmCancelDialog(order.orderId)
                }
            }
        }
    }

    private fun queryPayStatus(order: Order) {
        val mask = showMask(mContext)
        ComponentHolder.appComponent.api().queryPayStatus(OrderIdEntity(orderId = order.orderId))
            .observeOnMain(mContext as LifecycleOwner)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
                    if (it.failed) return@subscribeBy
                    order.status = it.data.orderStatus
                    order.statusText = it.data.orderStatusText
                    display(order)
                    // TODO 只更新了详情的支付状态 没有更新列表里的支付状态
                    ToastUtils.showShort("支付状态已更新")
                },
                onError = {
                    mask.dismiss()
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun showConfirmCancelDialog(orderId: Long) {
        MaterialDialog(mContext).show {
            title(text = "提示")
            message(text = "确认取消？")
            positiveButton(text = "确定") { RxBus.post(CancelOrderEvent(orderId = orderId)) }
        }
    }

    private fun showConfirmRefundDialog(orderId: Long) {
        MaterialDialog(mContext).show {
            title(text = "提示")
            message(text = "确认退款？")
            positiveButton(text = "确定") { RxBus.post(RefundOrderEvent(orderId = orderId)) }
        }
    }

    private fun getOrderDetail(orderId: Long) {
        val mask = showMask(context!!)
        ComponentHolder.appComponent.api().getTicketOrderInfo(orderId = orderId)
            .observeOnMain(mContext as LifecycleOwner)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
//                    if (it.failed) return@subscribeBy
                    display(order = it)
                },
                onError = {
                    mask.dismiss()
//                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun display(order: Order) {
        with(order) {
            tvCreatedDate.text = org.joda.time.DateTime(createdDate).toString("yyyy-MM-dd HH:mm")
            tvOrderId.text = orderId.toString()
            tvTotalPrice.text = "￥$totalPrice"
            tvPayTypeName.text = payTypeName
            tvStatusText.text = statusText
            ticketInfoAdapter.setNewData(order.detailInfoList.flatMap { it.ticketInfoList })
        }
    }


    lateinit var dialog: CustomDialog

    override val containerView = this


}