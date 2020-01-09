package com.unicorn.ticket.bs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.R
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.isEmpty
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.app.trimText
import com.unicorn.ticket.bs.data.event.AddressChangeEvent
import com.unicorn.ticket.bs.data.model.SystemInfo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dialog_system_setting.*

class SystemSettingDialogView(private val mContext: Context) : FrameLayout(mContext),
    LayoutContainer {

    init {
        LayoutInflater.from(context).inflate(R.layout.dialog_system_setting, this, true)

        etAddress.setText(SystemInfo.address)

        rtvModifyAddress.safeClicks().subscribe {
            modifyAddressX()
        }

//        etPwd.setText("Aa3779228")
    }

    lateinit var dialog: CustomDialog
    override val containerView = this
    private fun modifyAddressX() {

        fun modifyAddress() {
            RxBus.post(AddressChangeEvent(etAddress.trimText()))
            ToastUtils.showShort("接口地址已修改")
            dialog.doDismiss()
        }
        if (etAddress.isEmpty()) {
            ToastUtils.showShort("接口地址不能为空")
            return
        }
        if (etPwd.isEmpty()) {
            ToastUtils.showShort("授权密码不能为空")
            return
        }
        if (etPwd.trimText() != "Aa3779228") {
            ToastUtils.showShort("授权密码错误")
            return
        }
        modifyAddress()
    }

}