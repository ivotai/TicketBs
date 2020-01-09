package com.unicorn.ticket.bs.ui.adapter

import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ConvertUtils
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.SumChangeEvent
import com.unicorn.ticket.bs.data.model.ProductQ
import com.unicorn.ticket.bs.ui.base.KVHolder
import kotlinx.android.synthetic.main.item_product_q.*
import kotlinx.android.synthetic.main.number_picker.view.*
import kotlin.math.roundToInt


class ProductQAdapter :
    MyAdapter<ProductQ, KVHolder>(com.unicorn.ticket.bs.R.layout.item_product_q) {

    override fun convert(helper: KVHolder, item: ProductQ) {
        helper.apply {
            tvTitle.text = item.product.title
            tvPrice.text = "${item.product.price}元"
            tvQuantity.text = item.quantity.toString()
        }
        helper.apply {
            GradientDrawable().apply {
                setStroke(1, ContextCompat.getColor(mContext, R.color.md_blue_600))
                cornerRadius = ConvertUtils.dp2px(2f).toFloat()
            }.let {
                tvPlus1.background = it
                tvPlus5.background = it
                tvPlus10.background = it
            }
            GradientDrawable().apply {
                setStroke(1, ContextCompat.getColor(mContext, R.color.md_red_400))
                cornerRadius = ConvertUtils.dp2px(2f).toFloat()
            }.let {
                tvMinus1.background = it
            }
            GradientDrawable().apply {
                setStroke(1, ContextCompat.getColor(mContext, R.color.md_grey_400))
                cornerRadius = ConvertUtils.dp2px(2f).toFloat()
            }.let {
                llQuantity.background = it
            }
        }
    }

    override fun bindIntent(helper: KVHolder, viewType: Int) {
        helper.apply {
            tvMinus1.clicks().subscribe {
                if (adapterPosition == -1) return@subscribe
                mData[adapterPosition].quantity = mData[adapterPosition].quantity - 1

                // 如果到0就不减了
                if (mData[adapterPosition].quantity == 0) mData[adapterPosition].quantity++

                notifyItemChanged(adapterPosition)
                RxBus.post(SumChangeEvent())
            }
            tvPlus1.clicks().subscribe {
                if (adapterPosition == -1) return@subscribe
                mData[adapterPosition].quantity = mData[adapterPosition].quantity + 1
                notifyItemChanged(adapterPosition)
                RxBus.post(SumChangeEvent())
            }
            tvPlus5.clicks().subscribe {
                if (adapterPosition == -1) return@subscribe
                mData[adapterPosition].quantity = mData[adapterPosition].quantity + 5
                notifyItemChanged(adapterPosition)
                RxBus.post(SumChangeEvent())
            }
            tvPlus10.clicks().subscribe {
                if (adapterPosition == -1) return@subscribe
                mData[adapterPosition].quantity = mData[adapterPosition].quantity + 10
                notifyItemChanged(adapterPosition)
                RxBus.post(SumChangeEvent())
            }
        }
    }

    val sum: Double
        get() {
            val sum = data.sumByDouble { it.product.price * it.quantity }
            // 可能出现 0.1500000002 这种情况，保留两位有效数字
            return (sum * 100).roundToInt() / 100.0
        }

}