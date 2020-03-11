package com.unicorn.ticket.bs.data.api

import com.unicorn.ticket.bs.app.Globals
import com.unicorn.ticket.bs.app.defaultPageSize
import com.unicorn.ticket.bs.data.model.*
import io.reactivex.Single
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.http.*

interface SimpleApi {

    @GET("login/account")
    fun login(@Query("username") username: String, @Query("password") password: String): Single<LoginResponse>

    @GET("login/silence")
    fun loginSilently(@Query("token") token: String = Globals.token): Call<LoginResponse>

    @GET("api/v1/pda/ticket/product/list")
    fun getProduct(): Single<Response<List<Product>>>

    @POST("api/v1/pda/ticket/order/create")
    fun createOrder(@Body createOrderParam: CreateOrderParam): Single<Response<CreateOrderResponse>>

    @POST("api/v1/pda/ticket/order/pay")
    fun payOrder(@Body payOrderParam: PayOrderParam): Single<Response<PayOrderResponse>>

    @POST("api/v1/pda/ticket/take")
    fun takeTicket(@Body takeTicketParam: TakeTicketParam): Single<Response<List<Ticket>>>

    @GET("api/v1/pda/version/2/check")
    fun checkVersion(): Single<CheckVersionResponse>

    @GET("api/v1/pda/ticket/order/list")
    fun getOrder(
        @Query("beginDate") beginDate: String,
        @Query("endDate") endDate: String,
        @Query("status") status: Int?,
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
        @Query("category") category: Int = 1,
        @Query("pageSize") pageSize: Int = defaultPageSize
    ): Single<Response<Page<Order>>>

    @POST("api/v1/pda/ticket/order/cancel")
    fun cancelOrder(@Body orderIdEntity: OrderIdEntity): Single<Response<OrderIdEntity>>

    @POST("api/v1/pda/ticket/order/refund")
    fun refundOrder(@Body orderIdEntity: OrderIdEntity): Single<Response<OrderIdEntity>>

    @GET("api/v1/pda/ticket/getSaleStatInfo")
    fun getSaleStatInfo(
        @Query("beginSaleDate") beginSaleDate: String,
        @Query("endSaleDate") endSaleDate: String
    ): Single<Response<SaleStatInfo>>

    @POST("api/v1/pda/ticket/modifyPassword")
    fun modifyPassword(@Body modifyPasswordParam: ModifyPasswordParam): Single<Response<Any>>

    @GET("api/v1/pda/ticket/getPayTypeStatInfo")
    fun getPayTypeStatInfo(
        @Query("beginSaleDate") beginSaleDate: String,
        @Query("endSaleDate") endSaleDate: String
    ): Single<Response<PayTypeStatInfoResponse>>

    @GET("api/v1/pda/ticket/order/getTicketOrderInfo")
    fun getTicketOrderInfo(@Query("orderId") orderId: Long): Single<Order>

    @POST("api/v1/pda/ticket/order/pay/query")
    fun queryPayStatus(@Body orderIdEntity: OrderIdEntity): Single<Response<QueryPayStatusResponse>>

    @POST("api/v1/pda/ticket/{ticketId}/finishPrint")
    fun finishPrint(@Path("ticketId") ticketId: String): Single<Response<Any>>

    @GET("api/v1/pda/ticket/getInventory")
    fun getInventory(@Query("travelDate") beginDate: String = DateTime().toString("yyyy-MM-dd")): Single<Int>

}