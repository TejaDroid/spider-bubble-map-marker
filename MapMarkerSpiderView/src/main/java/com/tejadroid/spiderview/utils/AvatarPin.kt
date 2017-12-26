package com.tejadroid.spiderview.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tejadroid.spiderview.R

/**
 * Created by Tejas on 12/19/17.
 */
class AvatarPin {
    private var isDestroyView = false

    fun setIsdestroyed(IsDestroyed: Boolean) {
        this.isDestroyView = IsDestroyed
    }

    interface PinListener {
        fun onReadyPin(bitmap: Bitmap)
    }


    fun getAvatarPinImage(context: Context, imageUrl: String, color: Int, pinListener: PinListener?) {
//        Picasso.with(context).load(imageUrl)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .transform(CircleTransform())
//                .into(object : Target {
//                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//
//                    }
//
//                    override fun onBitmapFailed(errorDrawable: Drawable?) {
//                        val bitmap = BitmapFactory.decodeResource(context.resources,
//                                R.drawable.logo1)
//                        generateAvatarPin(context, bitmap, color, pinListener)
//                    }
//
//                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                        generateAvatarPin(context, bitmap!!, color, pinListener)
//                    }
//                })

        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .transform(RoundImageCircleTransform(context))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        if (!isDestroyView) {
                            generateAvatarPin(context, resource!!, color, pinListener)
                        }
                    }

                    override fun onLoadFailed(e: Exception, errorDrawable: Drawable?) {
                        if (!isDestroyView) {
                            val bitmap = BitmapFactory.decodeResource(context.resources,
                                    R.drawable.logo1)
                            generateAvatarPin(context, bitmap!!, color, pinListener)
                        }
                    }

                })
    }

    private fun generateAvatarPin(mContext: Context, bitmap: Bitmap, color: Int, listener: PinListener?) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.r_pin, null)
        val ivPinImage: ImageView = view.findViewById(R.id.iv_img)
        val ivPin: ImageView = view.findViewById(R.id.iv_pin)
        val ll_pin_bg: FrameLayout = view.findViewById(R.id.ll_pin)

        ivPin.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        ivPinImage.setImageBitmap(bitmap)

        ll_pin_bg.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        ll_pin_bg.layout(0, 0, ll_pin_bg.measuredWidth, ll_pin_bg.measuredHeight)

        val pinBitmap = Bitmap.createBitmap(ll_pin_bg.measuredWidth,
                ll_pin_bg.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(pinBitmap)
        view.draw(canvas)

        if (!isDestroyView && listener != null) {
            listener.onReadyPin(pinBitmap)
        }
    }


    companion object {

        private var avatarPin: AvatarPin? = null

        fun newInstance(): AvatarPin {
            if (avatarPin == null) {
                avatarPin = AvatarPin()
            }
            return avatarPin!!
        }
    }

}