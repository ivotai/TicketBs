package com.unicorn.ticket.bs.ui

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.amulyakhare.textdrawable.TextDrawable
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding3.view.clicks
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.*
import com.unicorn.ticket.bs.app.helper.DialogHelper
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.data.event.ModifyPwdEvent
import com.unicorn.ticket.bs.data.model.ModifyPasswordParam
import com.unicorn.ticket.bs.data.model.SystemInfo
import com.unicorn.ticket.bs.ui.base.BaseFra
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fra_system.*

class SystemFra : BaseFra() {

    override val layoutId = R.layout.fra_system

    override fun initViews() {
        if (SystemInfo.autoPrint) showAutoPrintYes() else showAutoPrintNo()
    }

    override fun bindIntent() {
        rllLogout.safeClicks().subscribe {
            activity?.finish()
        }

        rtvModify.safeClicks().subscribe {
            modifyPwdX()
        }

        rtvYes.clicks().subscribe {
            showAutoPrintYes()
            SystemInfo.autoPrint = true
        }
        rtvNo.clicks().subscribe {
            showAutoPrintNo()
            SystemInfo.autoPrint = false
        }
    }

    private fun showAutoPrintYes() {
        val yesSelected = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.WHITE)
            .fontSize(ConvertUtils.dp2px(14f))
            .endConfig()
            .buildRound("是", ContextCompat.getColor(context!!, R.color.md_blue_600))
        val noUnSelected = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.BLACK)
            .fontSize(ConvertUtils.dp2px(14f))
            .endConfig()
            .buildRound("否", Color.WHITE)
        rtvYes.setImageDrawable(yesSelected)
        rtvNo.setImageDrawable(noUnSelected)
    }

    private fun showAutoPrintNo() {
        val noSelected = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.WHITE)
            .fontSize(ConvertUtils.dp2px(14f))
            .endConfig()
            .buildRound("否", ContextCompat.getColor(context!!, R.color.md_blue_600))
        val yesUnSelected = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.BLACK)
            .fontSize(ConvertUtils.dp2px(14f))
            .endConfig()
            .buildRound("是", Color.WHITE)
        rtvYes.setImageDrawable(yesUnSelected)
        rtvNo.setImageDrawable(noSelected)
    }

    private fun modifyPwdX() {
        fun modifyPwd() {
            val mask = DialogHelper.showMask(context!!)
            api.modifyPassword(
                ModifyPasswordParam(
                    oldPassword = etOldPwd.trimText(),
                    newPassword = etNewPwd.trimText()
                )
            ).observeOnMain(this)
                .subscribeBy(
                    onSuccess = {
                        mask.dismiss()
                        if (it.failed) return@subscribeBy
                        RxBus.post(ModifyPwdEvent())
                        activity?.finish()
                    },
                    onError = {
                        mask.dismiss()
                        ExceptionHelper.showPrompt(it)
                    }
                )
        }
        if (etOldPwd.isEmpty()) {
            ToastUtils.showShort("原密码不能为空")
            return
        }
        if (etNewPwd.isEmpty()) {
            ToastUtils.showShort("新密码不能为空")
            return
        }
        if (etConfirmPwd.isEmpty()) {
            ToastUtils.showShort("确认密码不能为空")
            return
        }
        if (etNewPwd.trimText() != etConfirmPwd.trimText()) {
            ToastUtils.showShort("确认密码不一致")
            return
        }
        modifyPwd()
    }
}