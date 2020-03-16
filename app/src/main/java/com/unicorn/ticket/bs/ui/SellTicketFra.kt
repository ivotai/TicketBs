package com.unicorn.ticket.bs.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.addDefaultItemDecoration
import com.unicorn.ticket.bs.app.helper.DialogHelper.showMask
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.RestoreEvent
import com.unicorn.ticket.bs.data.event.SumChangeEvent
import com.unicorn.ticket.bs.data.model.CreateOrderParam
import com.unicorn.ticket.bs.data.model.ProductQ
import com.unicorn.ticket.bs.data.model.ProductS
import com.unicorn.ticket.bs.data.model.TakeTicketParam
import com.unicorn.ticket.bs.ui.adapter.ProductQAdapter
import com.unicorn.ticket.bs.ui.adapter.ProductSAdapter
import com.unicorn.ticket.bs.ui.base.BaseFra
import com.unicorn.ticket.bs.ui.dialog.PayDialogView
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fra_sell_ticket.*
import java.text.DecimalFormat

class SellTicketFra : BaseFra() {

    override fun initViews() {
        fun initRvProductS() = with(rvProductS) {
            layoutManager = GridLayoutManager(context!!, 2)
            productSAdapter.bindToRecyclerView(this)
            addItemDecoration(GridSpanDecoration(ConvertUtils.dp2px(24f)))
        }

        fun initRvProductQ() = with(rvProductQ) {
            layoutManager = LinearLayoutManager(context!!)
            productQAdapter.bindToRecyclerView(this)
            addDefaultItemDecoration(1)
            productQAdapter.setEmptyView(R.layout.ui_no_data)
        }

        initRvProductS()
        initRvProductQ()
    }

    override fun bindIntent() {
        fun getProduct() {
            val mask = showMask(context!!)
            api.getProduct()
                .observeOnMain(this)
                .subscribeBy(
                    onSuccess = { response ->
                        mask.dismiss()
                        if (response.failed) return@subscribeBy
                        productSAdapter.setNewData(response.data.map {
                            ProductS(
                                it,
                                response.data.indexOf(it)
                            )
                        })
                    },
                    onError = {
                        mask.dismiss()
                        ExceptionHelper.showPrompt(it)
                    }
                )
        }

        getProduct()

        llClear.safeClicks().subscribe { RxBus.post(RestoreEvent()) }

        fun createOrderX() {
            fun createOrder() {
                fun showPayDialog(orderId: Long) {
                    val payDialogView = PayDialogView(activity!!, productQAdapter.sum, orderId)
                    payDialogView.dialog =
                        CustomDialog.show(activity as AppCompatActivity, payDialogView)
                    payDialogView.dialog.cancelable = false
                }

                val mask = showMask(context!!)
                api.createOrder(createOrderParam)
                    .observeOnMain(this)
                    .subscribeBy(
                        onSuccess = {
                            mask.dismiss()
                            if (it.failed) return@subscribeBy
                            if (createOrderParam.totalPrice != 0.0)
                                showPayDialog(it.data.orderId)
                            else {
                                RxBus.post(RestoreEvent())
                                RxBus.post(TakeTicketParam(orderId = it.data.orderId))
                            }
                        },
                        onError = {
                            mask.dismiss()
                            ExceptionHelper.showPrompt(it)
                        }
                    )
            }

            if (createOrderParam.stypeNum == 0) {
                ToastUtils.showShort("至少选择一种票")
                return
            }
            createOrder()
        }

        llPay.safeClicks().subscribe { createOrderX() }
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, ProductS::class.java, Consumer { productS ->
            with(productQAdapter) {
                if (productS.isSelected) {
                    data.add(ProductQ(productS.product, productS.index))
                    data.sortBy { it.index }
                    notifyDataSetChanged()
                    RxBus.post(SumChangeEvent())
                } else {
                    data.removeIf { it.product.productId == productS.product.productId }
                    notifyDataSetChanged()
                    RxBus.post(SumChangeEvent())
                }
            }
        })

        // 还原未选择状态
        RxBus.registerEvent(this, RestoreEvent::class.java, Consumer {
            productSAdapter.data.forEach { it.isSelected = false }
            productSAdapter.notifyDataSetChanged()
            productQAdapter.data.clear()
            productQAdapter.notifyDataSetChanged()
            RxBus.post(SumChangeEvent())
        })

        // 每当 productQAdapter.data 改变，刷新 sum，有没有观察者模式啊，貌似没有
        RxBus.registerEvent(this, SumChangeEvent::class.java, Consumer {
            tvSum.text = "￥${productQAdapter.sum}"
        })
    }

    private val createOrderParam
        get() = CreateOrderParam(
            detailList = productQAdapter.data.map {
                it.product.apply { quantity = it.quantity }
            },
            totalPrice = productQAdapter.sum
        )

    private val productSAdapter = ProductSAdapter()
    private val productQAdapter = ProductQAdapter()

    override val layoutId = R.layout.fra_sell_ticket

}