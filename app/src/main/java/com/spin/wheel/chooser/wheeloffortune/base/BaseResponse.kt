package com.spin.wheel.chooser.wheeloffortune.base

import com.spin.wheel.chooser.wheeloffortune.utils.Constants

data class BaseResponse <out T>(val status: Constants.Status?, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): BaseResponse<T> {
            return BaseResponse(Constants.Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): BaseResponse<T> {
            return BaseResponse(Constants.Status.ERROR, null, message)
        }

        fun <T> loading(): BaseResponse<T> {
            return BaseResponse(Constants.Status.LOADING, null, null)
        }

        fun <T> none(): BaseResponse<T> {
            return BaseResponse(null, null, null)
        }
    }
}