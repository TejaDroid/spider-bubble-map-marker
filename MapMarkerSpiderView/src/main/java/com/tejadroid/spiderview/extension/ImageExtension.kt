package com.tejadroid.spiderview.extension

import android.graphics.drawable.BitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.squareup.picasso.*
import com.tejadroid.spiderview.R
import java.io.File

/**
 * Created by Tejas on 12/18/17.
 */

fun ImageView.loadUrl(url: String) {
    Picasso.with(this.context).load(url).into(this)
}


fun ImageView.loadUrl(url: String, drawable: Int) {
    Picasso.with(this.context).load(url).placeholder(drawable).into(this)
}

inline fun ImageView.loadProfilePic(url: String) {
    if (url.isNotEmpty()) {
        Picasso.with(this.context).load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .intoWithCallback(this)
    }
}

inline fun ImageView.loadUrlWithCircleImage(url: String) {
    if (url.isNotEmpty()) {
        Picasso.with(this.context).load(url).intoWithCallback(this)
    }
}

inline fun ImageView.loadFileWithCircleImage(url: String) {
    if (url.isNotEmpty()) {
        val file = File(url)
        Picasso.with(this.context).load(file).intoWithCallback(this)
    }
}

inline fun RequestCreator.intoWithCallback(target: ImageView) {
    this.into(target, KCallback(target))
}

class KCallback(private val imageView: ImageView) : Callback {

    override fun onSuccess() {
        val imageBitmap = (imageView.drawable as BitmapDrawable).bitmap
        val imageDrawable = RoundedBitmapDrawableFactory.create(imageView.resources, imageBitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = Math.min(imageBitmap.width, imageBitmap.height) / 2.0f
        imageView.setImageDrawable(imageDrawable)
    }

    override fun onError() {
        imageView.setImageResource(R.drawable.white_shadow_bg)
    }
}