package com.rutershok.daily.model

import com.google.gson.annotations.SerializedName

class DailyQuote : Quote() {
    @SerializedName("Day")
    val day: String? = null
}