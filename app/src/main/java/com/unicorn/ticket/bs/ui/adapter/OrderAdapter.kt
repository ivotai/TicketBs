package com.unicorn.ticket.bs.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.di.ComponentHolder
import com.unicorn.ticket.bs.app.helper.DialogHelper
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.CancelOrderEvent
import com.unicorn.ticket.bs.data.event.RefundOrderEvent
import com.unicorn.ticket.bs.data.model.Order
import com.unicorn.ticket.bs.data.model.OrderIdEntity
import com.unicorn.ticket.bs.data.model.TakeTicketParam
import com.unicorn.ticket.bs.ui.OrderDetailDialogView
import com.unicorn.ticket.bs.ui.base.KVHolder
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_order.*
import kotlinx.android.synthetic.main.item_order.tvCreatedDate
import kotlinx.android.synthetic.main.item_order.tvOrderId
import kotlinx.android.synthetic.main.item_order.tvPayType
import kotlinx.android.synthetic.main.item_order.tvQuantity
import kotlinx.android.synthetic.main.item_order.tvStatus
import kotlinx.android.synthetic.main.item_order.tvTotalPrice
import org.joda.time.DateTime

class OrderAdapter : BaseQuickAdapter<Order, KVHolder>(R.layout.item_order) {

    override fun convert(helper: KVHolder, item: Order) {
        helper.apply {
            tvCreatedDate.text = DateTime(item.createdDate).toString("yyyy-MM-dd HH:mm")
            tvOrderId.text = item.orderId.toString()
            tvQuantity.text = item.quantity.toString()
            tvTotalPrice.text = item.totalPrice.toString()
            tvPayType.text = item.payTypeName
            tvStatus.text = item.statusText
//            rtvOperation.safeClicks().subscribe { showListDialog(item) }
        }
        helper.root.safeClicks().subscribe {
            showDetail(item)
        }
    }

//    private fun showListDialog(order: Order) {
//        val updateStatus = "更新支付状态"
//        val refund = "退款"
//        val cancel = "取消"
//        val print = "打印"
//        val list = when (order.status) {
//            2 -> listOf(updateStatus, refund, print)
//            0 -> listOf(updateStatus, cancel)
//            else -> listOf(updateStatus)
//        }
//        MaterialDialog(mContext).show {
//            listItems(items = list) { _, _, text ->
//                when (text) {
//                    updateStatus -> queryPayStatus(order)
//                    refund -> showConfirmRefundDialog(order.orderId)
//                    print -> RxBus.post(TakeTicketParam(order.orderId))
//                    cancel -> showConfirmCancelDialog(order.orderId)
//                }
//            }
//        }
//    }
//
//    private fun showConfirmCancelDialog(orderId: Long) {
//        MaterialDialog(mContext).show {
//            title(text = "提示")
//            message(text = "确认取消？")
//            positiveButton(text = "确定") { RxBus.post(CancelOrderEvent(orderId = orderId)) }
//        }
//    }
//
//    private fun showConfirmRefundDialog(orderId: Long) {
//        MaterialDialog(mContext).show {
//            title(text = "提示")
//            message(text = "确认退款？")
//            positiveButton(text = "确定") { RxBus.post(RefundOrderEvent(orderId = orderId)) }
//        }
//    }
//
//    private fun queryPayStatus(order: Order) {
//        val mask = DialogHelper.showMask(mContext)
//        ComponentHolder.appComponent.api().queryPayStatus(OrderIdEntity(orderId = order.orderId))
//            .observeOnMain(mContext as LifecycleOwner)
//            .subscribeBy(
//                onSuccess = {
//                    mask.dismiss()
//                    if (it.failed) return@subscribeBy
//                    order.status = it.data.orderStatus
//                    order.statusText = it.data.orderStatusText
//                    notifyItemChanged(data.indexOf(order))
//                    ToastUtils.showShort("支付状态已更新")
//                },
//                onError = {
//                    mask.dismiss()
//                    ExceptionHelper.showPrompt(it)
//                }
//            )
//    }

    private fun showDetail(order: Order) {
        val orderDetailDialogView = OrderDetailDialogView(mContext, order)
        orderDetailDialogView.dialog =
            CustomDialog.show(mContext as AppCompatActivity, orderDetailDialogView)
    }

}