package com.rutershok.daily.model

import com.rutershok.daily.R
import java.util.*

enum class Social(val packageName: String, val iconResId: Int) {
    WHATSAPP("com.whatsapp", R.drawable.ic_whatsapp_share), INSTAGRAM(
        "com.instagram.android",
        R.drawable.ic_instagram_share
    ),
    FACEBOOK("com.facebook.katana", R.drawable.ic_facebook_share), MESSENGER(
        "com.facebook.orca",
        R.drawable.ic_messenger_share
    ),
    TWITTER("com.twitter.android", R.drawable.ic_twitter_share), SNAPCHAT(
        "com.snapchat.android",
        R.drawable.ic_snapchat_share
    ),
    TUMBRL("com.tumblr", R.drawable.ic_tumblr_share);

    companion object {
        @JvmStatic
        val list: List<Social> = ArrayList(Arrays.asList(*values()))
    }
}