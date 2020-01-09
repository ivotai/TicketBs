package com.unicorn.ticket.bs.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.*
import com.unicorn.ticket.bs.app.helper.DialogHelper
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.data.event.CancelOrderEvent
import com.unicorn.ticket.bs.data.event.RefundOrderEvent
import com.unicorn.ticket.bs.data.model.Order
import com.unicorn.ticket.bs.data.model.OrderIdEntity
import com.unicorn.ticket.bs.ui.adapter.OrderAdapter
import com.unicorn.ticket.bs.ui.base.KVHolder
import com.unicorn.ticket.bs.ui.base.SimplePageFra
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fra_search_order.*
import org.joda.time.DateTime
import java.util.*

class OrderSearchFra : SimplePageFra<Order, KVHolder>() {

    private var status: Int? = null

    private var dateTime = DateTime()

    override val simpleAdapter = OrderAdapter()

    override fun loadPage(page: Int) = api.getOrder(
        page = page,
        keyword = "",
        status = status,
        beginDate = dateTime.toString("yyyy/MM/dd"),
        endDate = dateTime.toString("yyyy/MM/dd")
    )

    override val layoutId = R.layout.fra_search_order

    override fun initViews() {
        super.initViews()
        displayDate()
        mRecyclerView.addDefaultItemDecoration(1)
    }

    private fun cancelOrder(orderId: Long) {
        val mask = DialogHelper.showMask(context!!)
        api.cancelOrder(OrderIdEntity(orderId = orderId))
            .observeOnMain(this)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
                    if (it.failed) return@subscribeBy
                    "取消成功".toast()
                    loadFirstPage()
                },
                onError = {
                    mask.dismiss()
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun refundOrder(orderId: Long) {
        val mask = DialogHelper.showMask(context!!)
        api.refundOrder(OrderIdEntity(orderId = orderId))
            .observeOnMain(this)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
                    if (it.failed) return@subscribeBy
                    "退款成功".toast()
                    loadFirstPage()
                },
                onError = {
                    mask.dismiss()
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, CancelOrderEvent::class.java, Consumer {
            cancelOrder(it.orderId)
        })
        RxBus.registerEvent(this, RefundOrderEvent::class.java, Consumer {
            refundOrder(it.orderId)
        })
    }

    override fun bindIntent() {
        super.bindIntent()
        segmentedGroup.setOnCheckedChangeListener { _, checkedId ->
            status = when (checkedId) {
                R.id.radioButton1 -> null   // 全部
                R.id.radioButton2 -> 2      // 已出票
                R.id.radioButton3 -> 1      // 未出票
                else -> null
            }
            loadFirstPage()
        }

        rllDate.safeClicks().subscribe {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        MaterialDialog(context!!).show {
            datePicker { _, date ->
                dateTime = DateTime(date)
                displayDate()
                loadFirstPage()
            }
        }
    }

    private fun displayDate() {
        tvDate.text =
            dateTime.toString("yyyy-MM-dd ") + dateTime.dayOfWeek().getAsShortText(Locale.CHINESE)
    }

    override val mRecyclerView: RecyclerView
        get() = recyclerView

    override val mSwipeRefreshLayout: SwipeRefreshLayout
        get() = swipeRefreshLayout

}