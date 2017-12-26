package com.tejadroid.spiderview.map

import android.annotation.SuppressLint
import android.graphics.Point
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.tejadroid.spiderview.utils.ActivityUtils
import com.tejadroid.spiderview.widget.SpiderLineView
import com.tejadroid.spiderview.R
import com.tejadroid.spiderview.extension.hide
import com.tejadroid.spiderview.extension.show
import com.tejadroid.spiderview.model.User

/**
 * Created by Tejas on 12/19/17.
 */
class AvatarPinOption {
    private lateinit var spiderLineView: SpiderLineView
    private var isShow = false
    private var popoutAnimation: Animation? = null
    private var popinAnimation: Animation? = null
    private var rlAvatarPinOptionViewLayoutParams: RelativeLayout.LayoutParams? = null
    private var pinOptionsViewWidth: Int = 0
    private var pinOptionsViewHeight: Int = 0
    private lateinit var mItemListener: SpiderLineView.OnSpiderItemClickListener

    fun setPinLayout(optionView: SpiderLineView) {
        this.spiderLineView = optionView

        popoutAnimation = AnimationUtils.loadAnimation(
                this.spiderLineView.context, R.anim.popout_map)
        popinAnimation = AnimationUtils.loadAnimation(
                this.spiderLineView.context, R.anim.popin_map)
        popoutAnimation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                spiderLineView.show()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        popinAnimation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                spiderLineView.hide()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        this.spiderLineView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                // Ensure you call it only once :
                rlAvatarPinOptionViewLayoutParams = this@AvatarPinOption.spiderLineView.layoutParams as RelativeLayout.LayoutParams
                pinOptionsViewWidth = this@AvatarPinOption.spiderLineView.width
                pinOptionsViewHeight = this@AvatarPinOption.spiderLineView.height
                this@AvatarPinOption.spiderLineView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        this@AvatarPinOption.spiderLineView.startAnimation(popinAnimation)
    }

    fun showAvatarPinOption(screenPosition: Point) {
        if (!isShow) {
            val leftMargin = screenPosition.x - pinOptionsViewWidth / 2
            val topMargin = screenPosition.y - pinOptionsViewHeight / 2 -
                    ActivityUtils.dpToPx(spiderLineView.context, 70) + (ActivityUtils.dpToPx(spiderLineView.context, 50) / 2)
            rlAvatarPinOptionViewLayoutParams!!.setMargins(leftMargin, topMargin, 0, 0)
            spiderLineView.layoutParams = rlAvatarPinOptionViewLayoutParams
            spiderLineView.startAnimation(popoutAnimation)
            spiderLineView.show()
            isShow = true
        }
    }

    var user = User()

    fun setUserObject(user: User) {
        this.user = user
    }

    fun setMapItemClickListener(listener: SpiderLineView.OnSpiderItemClickListener) {
        this.mItemListener = listener
    }


    fun dismissAvatarPinOption() {
        if (isShow) {
            spiderLineView.startAnimation(popinAnimation)
            spiderLineView.hide()
            isShow = false
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var pinOptions: AvatarPinOption? = null

        fun newInstance(): AvatarPinOption {
            if (pinOptions == null) {
                pinOptions = AvatarPinOption()
            }
            return pinOptions!!
        }
    }

}