package com.example.todoapplication.after_reg.features.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<ConnectionStatus>

    enum class ConnectionStatus{
        Available, Unavailable, Losing, Lost
    }
}