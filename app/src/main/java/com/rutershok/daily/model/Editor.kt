package com.rutershok.daily.model

import com.rutershok.daily.R

class Editor {
    enum class Background(val titleRes: Int, val iconRes: Int) {
        BACKGROUND_COLOR(R.string.color, R.drawable.ic_color), BACKGROUND_GALLERY(
            R.string.gallery,
            R.drawable.ic_camera
        ),
        BACKGROUND_GRADIENT(
            R.string.gradient,
            R.drawable.ic_gradient
        ),
        BACKGROUND_OPACITY(
            R.string.opacity,
            R.drawable.ic_opacity
        ),  //BACKGROUND_FOREGROUND(R.string.foreground, R.drawable.ic_foreground),
        BACKGROUND_PHOTOS(R.string.photos, R.drawable.ic_photos);
    }

    enum class Text(val titleRes: Int, val iconRes: Int) {
        TEXT_COLOR(R.string.color, R.drawable.ic_color), TEXT_SIZE(
            R.string.text_size,
            R.drawable.ic_text
        ),
        TEXT_OPACITY(R.string.opacity, R.drawable.ic_opacity), TEXT_ALIGNMENT(
            R.string.alignment,
            R.drawable.ic_alignment
        ),
        TEXT_SPACING(R.string.spacing, R.drawable.ic_spacing), TEXT_HIGHLIGHT(
            R.string.highlight,
            R.drawable.ic_highlight
        ),
        TEXT_BORDER(R.string.board, R.drawable.ic_border);
    }
}