package com.rutershok.daily.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.gson.annotations.SerializedName
import com.rutershok.daily.EditorActivity
import com.rutershok.daily.database.Storage
import com.rutershok.daily.utils.Constant
import java.io.Serializable

open class Quote : Serializable {
    @SerializedName("Quote")
    val quote: String? = null

    @SerializedName("Author")
    val author: String? = null

    @SerializedName("Background")
    val background: String? = null
        get() = Constant.IMAGES_URL + field

    //For favorites
    override fun equals(other: Any?): Boolean {
        if (other !is Quote) {
            return false
        }
        return this.quote == other.quote
    }

    override fun toString(): String {
        return quote!!
    }

    override fun hashCode(): Int {
        return quote.hashCode()
    }

    fun isFavorite(context: Context?): Boolean {
        return Storage.getFavoriteQuotes(context).contains(this)
    }

    fun openEditor(activity: Activity) {
        activity.startActivity(
            Intent(activity, EditorActivity::class.java).putExtra(
                Constant.QUOTE,
                this
            )
        )
    }
}