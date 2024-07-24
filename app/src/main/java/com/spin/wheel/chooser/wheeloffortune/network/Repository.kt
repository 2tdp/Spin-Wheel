package com.spin.wheel.chooser.wheeloffortune.network

import com.spin.wheel.chooser.wheeloffortune.base.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getData(id: Int, name: String): Flow<BaseResponse<String>> = flow {
        emit(BaseResponse.success(apiInterface.getData(id, name)))
    }.flowOn(Dispatchers.IO)
}