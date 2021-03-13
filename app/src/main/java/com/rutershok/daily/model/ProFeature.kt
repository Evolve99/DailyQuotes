package com.rutershok.daily.model

import com.rutershok.daily.R

enum class ProFeature(val title: Int, val iconRes: Int) {
    NO_ADS(R.string.remove_ads, R.drawable.ic_pro_ads), PREMIUM_FONTS(
        R.string.more_fonts,
        R.drawable.ic_pro_font
    ),
    SEARCH_QUOTES(
        R.string.search_quotes,
        R.drawable.ic_pro_search
    ),
    COLLECTION_OF_PHOTOS(
        R.string.collection_of_photos,
        R.drawable.ic_pro_photos
    ),
    MORE(
        R.string.support_our_work,
        R.drawable.ic_pro_support
    ),
    REMOVE_LOGO(R.string.remove_logo_from_images, R.mipmap.ic_launcher);
}