package com.unicorn.ticket.bs.ui

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.addDefaultItemDecoration
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.ui.adapter.TicketSaleStatAdapter
import com.unicorn.ticket.bs.ui.base.BaseFra
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fra_stat.*
import org.joda.time.DateTime
import java.util.*
import kotlin.math.roundToInt

class StatFra : BaseFra() {

    override val layoutId = R.layout.fra_stat

    private val ticketSaleStatAdapter = TicketSaleStatAdapter()

    override fun initViews() {
        displayDate(beginSaleDate, tvBeginSaleDate)
        displayDate(endSaleDate, tvEndSaleDate)

        fun initRecyclerView() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context!!)
                ticketSaleStatAdapter.bindToRecyclerView(this)
                addDefaultItemDecoration(1)
            }
        }
        initRecyclerView()
    }

    override fun bindIntent() {
        getStat()
        rllSearch.safeClicks().subscribe { getStat() }
        rllBeginDate.safeClicks().subscribe {
            MaterialDialog(context!!).show {
                datePicker { _, date ->
                    beginSaleDate = DateTime(date)
                    displayDate(beginSaleDate, this@StatFra.tvBeginSaleDate)
                }
            }
        }
        rllEndDate.safeClicks().subscribe {
            MaterialDialog(context!!).show {
                datePicker { _, date ->
                    endSaleDate = DateTime(date)
                    displayDate(endSaleDate, this@StatFra.tvEndSaleDate)
                }
            }
        }
    }

    private var beginSaleDate = DateTime()

    private var endSaleDate = DateTime()

    private fun getStat() {
        api.getSaleStatInfo(
            beginSaleDate = beginSaleDate.toString("yyyy/MM/dd"),
            endSaleDate = endSaleDate.toString("yyyy/MM/dd")
        ).observeOnMain(this)
            .subscribeBy(
                onSuccess = { response ->
                    if (response.failed) return@subscribeBy
                    ticketSaleStatAdapter.setNewData(response.data.ticketSaleStatInfoList)
                    tvWeChat.text = "￥0.0"
                    tvAlipay.text = "￥0.0"
                    tvCash.text = "￥0.0"
                    tvBank.text = "￥0.0"
                    with(response.data.ticketPayTypeStatInfoList) {
                        this.firstOrNull { it.pay_type_name == "微信" }
                            ?.let { tvWeChat.text = "￥${it.total_amount}" }
                        this.firstOrNull { it.pay_type_name == "支付宝" }
                            ?.let { tvAlipay.text = "￥${it.total_amount}" }
                        this.firstOrNull { it.pay_type_name == "现金" }
                            ?.let { tvCash.text = "￥${it.total_amount}" }
                        this.firstOrNull { it.pay_type_name == "银联" }
                            ?.let { tvBank.text = "￥${it.total_amount}" }
                        var sum = sumByDouble { it.total_amount }
                        sum = (sum * 100).roundToInt() / 100.0
                        tvSum.text = "￥${sum}"
                    }
                },
                onError = {
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun displayDate(dateTime: DateTime, tvDate: TextView) {
        tvDate.text =
            dateTime.toString("yyyy-MM-dd ") + dateTime.dayOfWeek().getAsShortText(Locale.CHINESE)
    }

}