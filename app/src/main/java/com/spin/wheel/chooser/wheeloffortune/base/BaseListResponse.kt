package com.spin.wheel.chooser.wheeloffortune.base

import com.spin.wheel.chooser.wheeloffortune.utils.Constants

data class BaseListResponse <out T>(val status: Constants.Status?, val data: List<T>?, val message: String?) {

    companion object {
        fun <T> success(data: List<T>?): BaseListResponse<T> {
            return BaseListResponse(Constants.Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): BaseListResponse<T> {
            return BaseListResponse(Constants.Status.ERROR, null, message)
        }

        fun <T> loading(): BaseListResponse<T> {
            return BaseListResponse(Constants.Status.LOADING, null, null)
        }

        fun <T> none(): BaseListResponse<T> {
            return BaseListResponse(null, null, null)
        }
    }
}