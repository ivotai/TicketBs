package com.unicorn.ticket.bs.ui.other

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ConvertUtils
import com.jakewharton.rxbinding3.view.clicks
import com.unicorn.ticket.bs.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.number_picker.view.*

class NumberPicker(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    LayoutContainer {

    override val containerView = this
    private var mValue: Int = 1

    init {
        LayoutInflater.from(context).inflate(R.layout.number_picker, this, true)
        background = GradientDrawable().apply {
            cornerRadius = ConvertUtils.dp2px(10f).toFloat()
            setStroke(1, ContextCompat.getColor(context, R.color.md_grey_300))
        }

        flMinus.clicks().subscribe {
            if (mValue != 1) setValue(mValue - 1)
        }
        flPlus.clicks().subscribe {
            if (mValue != 99) setValue(mValue + 1)
        }
    }

    fun setValue(@IntRange(from = 1, to = 99) value: Int) {
        mValue = value
        tvNumber.text = mValue.toString()
        refresh()
    }

    private fun refresh() {
        val idRes =
            if (mValue == 1) R.drawable.ic__ionicons_svg_md_remove_grey else R.drawable.ic__ionicons_svg_md_remove
        ivMinus.setImageDrawable(ContextCompat.getDrawable(context, idRes))

        val idRes2 =
            if (mValue == 99) R.drawable.ic__ionicons_svg_md_add_grey else R.drawable.ic__ionicons_svg_md_add
        ivPlus.setImageDrawable(ContextCompat.getDrawable(context, idRes2))

    }
}