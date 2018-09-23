package com.kwunai.github.ext

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kwunai.github.widgets.AvatarImageView

fun AvatarImageView.loadWithGlide(url: String,
                                  textPlaceHolder: Char,
                                  requestOptions: RequestOptions = RequestOptions()) {
    textPlaceHolder.toString().let {
        setTextAndColorSeed(it.toUpperCase(), it.hashCode().toString())
    }

    Glide.with(this.context)
            .load(url)
            .apply(requestOptions)
            .into(this)
}