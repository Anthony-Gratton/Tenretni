package com.example.tenretni.data.repositories

import com.example.tenretni.data.datasources.TicketDataSource
import com.example.tenretni.core.ApiResult
import com.example.tenretni.core.Constants
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TicketRepository {

    private val ticketDataSource = TicketDataSource()

    fun retrieveAll() : Flow<ApiResult<List<Ticket>>> {
        return flow {
            while(true) {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(ticketDataSource.retrieveAll()))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.Delay.TICKET_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(ticketDataSource.retrieveOne(href)))
            }catch (ex: Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }


    fun updateOne(href: String, action: String) : Flow<ApiResult<Ticket>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(ticketDataSource.updateOne(href, action)))
            }catch (ex: Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}