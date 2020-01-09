package com.unicorn.ticket.bs.app.helper

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

object DialogHelper {

    fun showMask(context: Context): KProgressHUD {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setDimAmount(0.5f)
            .show()
    }

}