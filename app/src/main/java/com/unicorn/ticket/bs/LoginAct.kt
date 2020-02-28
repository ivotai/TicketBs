package com.unicorn.ticket.bs

import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v3.CustomDialog
import com.unicorn.ticket.bs.app.*
import com.unicorn.ticket.bs.app.helper.DialogHelper
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.helper.UpdateHelper
import com.unicorn.ticket.bs.data.event.AddressChangeEvent
import com.unicorn.ticket.bs.data.event.ModifyPwdEvent
import com.unicorn.ticket.bs.data.model.SystemInfo
import com.unicorn.ticket.bs.data.model.UserInfo
import com.unicorn.ticket.bs.ui.base.BaseAct
import com.unicorn.ticket.bs.ui.dialog.SystemSettingDialogView
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.act_login.*
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

class LoginAct : BaseAct() {

    override val layoutId = R.layout.act_login

    override fun initViews() {

        root.background = ContextCompat.getDrawable(this, R.mipmap.login_bg)

        fun restoreUserInfo() = with(UserInfo) {
            if (keepPwd) {
                etUsername.setText(username)
                etPassword.setText(password)
                mcbKeepPwd.isChecked = keepPwd
            }
        }
        restoreUserInfo()

        tvSystemSetting.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvSystemSetting.paint.isAntiAlias = true
    }

    override fun bindIntent() {
        RetrofitUrlManager.getInstance().setGlobalDomain(SystemInfo.address)
        mbLogin.safeClicks().subscribe {
            loginX()
        }
        tvSystemSetting.safeClicks().subscribe {
            showSystemSettingDialog()
        }
    }

    private fun showSystemSettingDialog() {
        val systemSettingDialogView = SystemSettingDialogView(this)
        systemSettingDialogView.dialog = CustomDialog.show(this, systemSettingDialogView)
    }

    private fun loginX() {
        fun saveUserInfo() = with(UserInfo) {
            username = etUsername.trimText()
            password = etPassword.trimText()
            keepPwd = mcbKeepPwd.isChecked
        }

        fun login() {
            val mask = DialogHelper.showMask(this)
            api.login(etUsername.trimText(), etPassword.trimText())
                .observeOnMain(this)
                .subscribeBy(
                    onSuccess = {
                        mask.dismiss()
                        if (it.failed) return@subscribeBy
                        Globals.loginResponse = it
                        saveUserInfo()
                        UpdateHelper.checkVersion(this)
                    },
                    onError = {
                        mask.dismiss()
                        ExceptionHelper.showPrompt(it)
                    }
                )
        }

        if (etUsername.isEmpty()) {
            ToastUtils.showShort("工号不能为空")
            return
        }
        if (etPassword.isEmpty()) {
            ToastUtils.showShort("密码不能为空")
            return
        }
        login()
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, ModifyPwdEvent::class.java, Consumer {
            etPassword.setText("")
        })

        RxBus.registerEvent(this, AddressChangeEvent::class.java, Consumer {
            SystemInfo.address = it.address
            RetrofitUrlManager.getInstance().setGlobalDomain(SystemInfo.address)
        })
    }

}
