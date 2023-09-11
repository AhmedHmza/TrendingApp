package com.example.trendingapp.dto

sealed class Answer<out R> {

    data class Success<out T>(val data: T) : Answer<T>()
    data class Error(val exception: Exception? = null) : Answer<Nothing>()
    object Loading : Answer<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val Answer<*>.succeeded
    get() = this is Answer.Success && data != null