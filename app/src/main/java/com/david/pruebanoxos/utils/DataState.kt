package com.david.pruebanoxos.utils

data class DataState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)