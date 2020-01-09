package com.unicorn.ticket.bs

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ToastUtils
import com.unicorn.ticket.bs.app.Globals
import com.unicorn.ticket.bs.app.RxBus
import com.unicorn.ticket.bs.app.helper.DialogHelper
import com.unicorn.ticket.bs.app.helper.ExceptionHelper
import com.unicorn.ticket.bs.app.observeOnMain
import com.unicorn.ticket.bs.app.safeClicks
import com.unicorn.ticket.bs.data.event.ChangePagerEvent
import com.unicorn.ticket.bs.data.event.FormFeed
import com.unicorn.ticket.bs.data.model.SideMenuS
import com.unicorn.ticket.bs.data.model.TakeTicketParam
import com.unicorn.ticket.bs.data.model.Ticket
import com.unicorn.ticket.bs.ui.adapter.MainPagerAdapter
import com.unicorn.ticket.bs.ui.adapter.SideMenuSAdapter
import com.unicorn.ticket.bs.ui.base.BaseAct
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.act_main.*
import net.posprinter.posprintersdk.posprinterface.PrintBinder
import net.posprinter.posprintersdk.posprinterface.ReadListener
import net.posprinter.posprintersdk.posprinterface.UiInterface
import net.posprinter.posprintersdk.service.PrinterService
import net.posprinter.posprintersdk.utils.ByteUtil
import net.posprinter.posprintersdk.utils.DataForSendToPrinterTSC
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.experimental.and
import kotlin.math.roundToInt

class MainAct : BaseAct() {

    override val layoutId = R.layout.act_main

    override fun initViews() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        tvUsername.text = Globals.loginResponse.user.username
        tvDate.text = DateTime().toString("日期：yyyy年MM月dd日")

        fun initViewPaper() = with(viewPaper) {
            offscreenPageLimit = MainPagerAdapter.titles.size - 1
            adapter = MainPagerAdapter(supportFragmentManager)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {
                }


                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }


                override fun onPageSelected(position: Int) {
                    sideMenuSAdapter.data.forEach { it.isSelected = false }
                    sideMenuSAdapter.data[position].isSelected = true
                    sideMenuSAdapter.notifyDataSetChanged()
                }
            })

        }
        initViewPaper()

        fun initRvSideMenu() = with(constraintLayout3) {
            layoutManager = LinearLayoutManager(this@MainAct)
            sideMenuSAdapter.bindToRecyclerView(this)
            HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.md_grey_700)
                .size(1)
                .build().let { this.addItemDecoration(it) }
        }
        initRvSideMenu()
    }

    private val sideMenuSAdapter = SideMenuSAdapter()

    override fun bindIntent() {
        connectService()
        sideMenuSAdapter.setNewData(SideMenuS.all)
        tvPrinterStatus.safeClicks().subscribe { formFeed() }

        getPayTypeStatInfo()
//        tvPrinterStatus.safeClicks().subscribe { formFeed() }
    }


    private fun getPayTypeStatInfo() {
        api.getPayTypeStatInfo(
            beginSaleDate = DateTime().toString("yyyy/MM/dd"),
            endSaleDate = DateTime().toString("yyyy/MM/dd")
        )
            .observeOnMain(this)
            .subscribeBy(
                onSuccess = { response ->
                    if (response.failed) return@subscribeBy
                    val sb = StringBuilder("分类支付:   ")
//                    "pay_type":3,"pay_type_name":"银联","total_amount":0.01}
//                    tvSum.text = "分类支付：  扫码付￥25000    刷卡付￥25000    现金付￥1000    |    合计：￥50100    "
                    with(response.data.payTypeStatInfoList) {
                        this.firstOrNull { it.pay_type_name == "微信" }
                            ?.let { sb.append("微信付￥${it.total_amount}   ") }
                        this.firstOrNull { it.pay_type_name == "支付宝" }
                            ?.let { sb.append("支付宝付￥${it.total_amount}   ") }
                        this.firstOrNull { it.pay_type_name == "现金" }
                            ?.let { sb.append("现金付￥${it.total_amount}   ") }
                        this.firstOrNull { it.pay_type_name == "银联" }
                            ?.let { sb.append("银联付￥${it.total_amount}   ") }
                        var sum = sumByDouble { it.total_amount }
                        sum = (sum * 100).roundToInt() / 100.0
                        sb.append("合计$sum  ")
                        tvSum.text = sb.toString()
                    }

                },
                onError = {
                    ExceptionHelper.showPrompt(it)
                }
            )
    }


    private var binder: PrintBinder? = null

    private val conn: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            ToastUtils.showShort("打印服务断开连接")
            binder = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            binder = service as PrintBinder
            binder!!.connectUsbPrint("")
            //注册监听打印机的状态
            binder!!.registerListerner { isConnect, _ ->
                if (isConnect) {
                    ToastUtils.showLong("打印机已连接")
                    tvPrinterStatus.text = "[ 点击走纸 ]"
                } else {
                    ToastUtils.showLong("打印机未连接")
                    tvPrinterStatus.text = "[ 未连接 ]"
//                    unbindService(this)
                    binder = null
                }
            }
        }
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, TakeTicketParam::class.java, Consumer {
            takeTicket(it)
        })

        RxBus.registerEvent(this, ChangePagerEvent::class.java, Consumer {
            viewPaper.setCurrentItem(it.position, false)
        })

        RxBus.registerEvent(this, FormFeed::class.java, Consumer {
            formFeed()
        })
    }

    lateinit var tickets: List<Ticket>

    private fun takeTicket(takeTicketParam: TakeTicketParam) {
        val mask = DialogHelper.showMask(this)
        api.takeTicket(takeTicketParam)
            .observeOnMain(this)
            .subscribeBy(
                onSuccess = {
                    mask.dismiss()
                    if (it.failed) return@subscribeBy
                    tickets = it.data
                    checkStatus(object : CheckStatusListener {
                        override fun doOnNormal() {
                            printNext()
                        }
                    })
                },
                onError = {
                    mask.dismiss()
                    ExceptionHelper.showPrompt(it)
                }
            )
    }

    private fun connectService() {
        val intent = Intent(this, PrinterService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    private fun printNext() {
        val ticket = tickets.find { !it.isPrinted }
        if (ticket == null) return
        //清除缓存
        val start = 760
        val step = 30
        val startX = 65
        val endX = 380

        if (ticket.title == "纸质车票") return   // 以下打印语句

        val list = ArrayList<ByteArray>()
        //通过工具类得到一个指令的byte[]数据,以文本为例
        //首先得设置size标签尺寸,宽60mm,高100mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
        list.add(DataForSendToPrinterTSC.sizeBymm(80.0, 127.0))
        //设置打印浓度(1-15),标准浓度是7，级数越大，打印越黑，耗电越大，不可设置过大，
        list.add(DataForSendToPrinterTSC.density(8))
        //设置打印方向（0-1）；默认是0
        list.add(DataForSendToPrinterTSC.direction(0))
        list.add(DataForSendToPrinterTSC.cls())
        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start,
                "TSS24.BF2",
                0,
                1,
                1,
                "No:${ticket.ticketNo}"
            )
        )
        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start + step,
                "TSS24.BF2",
                0,
                1,
                1,
                "票型：${ticket.title}"
            )
        )
        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start + step * 2,
                "TSS24.BF2",
                0,
                1,
                1,
                "票价：${ticket.price}元"
            )
        )
        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start + step * 3,
                "TSS24.BF2",
                0,
                1,
                1,
                "数量：${ticket.quantity}"
            )
        )
        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start + step * 4,
                "TSS24.BF2",
                0,
                1,
                1,
                "日期：${DateTime(ticket.travelDate).toString("yyyy-MM-dd")}"
            )
        )

        list.add(
            DataForSendToPrinterTSC.text(
                startX,
                start + step * 5,
                "TSS24.BF2",
                0,
                1,
                1,
                "仅限当日使用${if (ticket.stype == 4) "" else "，有效证件入园"}"
            )
        )

        list.add(
            DataForSendToPrinterTSC.qrCode(
                endX, start,
                "M", 8, "A", 0, "M2", "S7", ticket.ticketCode
            )
        )

        list.add(DataForSendToPrinterTSC.print(1))
        list.add(DataForSendToPrinterTSC.cut())  //切纸放在打印之后
        val d = ByteUtil.byteMerger(list)
        binder?.write(d, object : UiInterface {

            override fun onsucess() {
                Observable.timer(3, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe {
                    checkStatus(object :CheckStatusListener{
                        override fun doOnNormal() {
                            finishPrint(ticket)
                        }
                    })
                }
            }

            override fun onfailed(msg: String) {

            }
        })
    }

    private fun formFeed() {
        //初始化一个list
        val list = ArrayList<ByteArray>()
        val data0 = DataForSendToPrinterTSC.formFeed()
        //通过工具类得到一个指令的byte[]数据,以文本为例
        //首先得设置size标签尺寸,宽60mm,高100mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册

        list.add(data0)
        list.add(DataForSendToPrinterTSC.cls())
        list.add(DataForSendToPrinterTSC.print(1))
        list.add(DataForSendToPrinterTSC.cut())

        val d = ByteUtil.byteMerger(list)
        binder?.write(d, object : UiInterface {
            override fun onsucess() {
            }

            override fun onfailed(msg: String) {
                msg
            }
        })
    }

    interface CheckStatusListener {
        fun doOnNormal()
    }

    private fun checkStatus(listener: CheckStatusListener) {
        val data = DataForSendToPrinterTSC.checkStatus()
        var str = "正常"
        binder?.checkStatus(data, ReadListener { isRead, msg, readData ->
            if (!isRead) {
                str = "未读取到打印机状态"
            } else {
                var value = readData[0]
                val byteArr = ByteArray(8) //一个字节八位
                for (i in 0..7) {
                    byteArr[i] = value.and(1)
                    value = value.toInt().shr(1).toByte()
                }
                if (byteArr[0].toInt() == 1) {
                    str = "缺纸错误, "
                }
                if (byteArr[1].toInt() == 1) {
                    str = str + "面盖打开错误, "
                }
                if (byteArr[2].toInt() == 1) {
                    str = str + "切刀错误, "
                }
                if (byteArr[3].toInt() == 1) {
                    str = str + "黑标或孔洞定位错误, "
                }
                if (byteArr[4].toInt() == 1) {
                    //未定义
                }
                if (byteArr[5].toInt() == 1) {
                    //5:纸将尽（票仓纸卷即将打印完，提示作用，不是错误，此功能需要打印机接一个三线的纸将尽传感器）
                }
                if (byteArr[6].toInt() == 1) {
                    //6:出纸嘴有纸，这个位不用，起提示作用
                }
                if (byteArr[7].toInt() == 1) {
                    //str=str+"正在打印中, ";//正在打印中
                }
            }
            if (str == "正常") listener.doOnNormal()
            else ToastUtils.showShort(str)
        })
    }

    private fun finishPrint(ticket: Ticket) {
        api.finishPrint(ticket.ticketId).subscribeOn(Schedulers.io()).subscribeBy(
            onSuccess = {
                if (it.failed) return@subscribeBy
                ticket.isPrinted = true
                printNext()
            },
            onError = {
                ExceptionHelper.showPrompt(it)
            }
        )
    }

}