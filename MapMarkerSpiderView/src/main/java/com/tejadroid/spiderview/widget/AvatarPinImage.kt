package com.tejadroid.spiderview.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.tejadroid.spiderview.R
import com.tejadroid.spiderview.utils.ActivityUtils
import com.tejadroid.spiderview.utils.CircleTransform


/**
 * Created by Tejas on 12/18/17.
 */
class AvatarPinImage : View {

    lateinit var paint: Paint

    lateinit var bgPaint: Paint

    lateinit var clearPaint: Paint

    private var padding = 4

    internal var pin_color: Int = Color.parseColor("#000000")

    lateinit var srcBitmap: Bitmap

    lateinit var bgBitmap: Bitmap

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttributeSet(context, attrs, 0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttributeSet(context, attrs, defStyleAttr)
        init()
    }

    private fun getAttributeSet(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.pin, defStyleAttr, 0)
        try {
            pin_color = ta.getColor(R.styleable.pin_color, Color.parseColor("#000000"))
            var srcDrawable = ta.getDrawable(R.styleable.pin_src)
            srcBitmap = ActivityUtils.getBitmapFromDrawable(srcDrawable)
        } finally {
            ta.recycle()
        }
    }

    fun init() {
        paint = Paint()

        bgPaint = Paint()
        bgPaint.colorFilter = PorterDuffColorFilter(pin_color, PorterDuff.Mode.SRC_IN)

        bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.pin)
        srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo1)

        clearPaint = Paint()
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    }

    fun getResizeBitmap(bitmap: Bitmap): Bitmap {
        this.padding = ActivityUtils.dpToPx(context, padding)
        return Bitmap.createScaledBitmap(bitmap, bgBitmap.width - padding, bgBitmap.width - padding, false)
    }

    /*
    * Set the canvas bounds here
    * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val screenWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val screenHeight = View.MeasureSpec.getSize(heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
//        canvas.drawRect(0f, 0f, 0f, 0f, clearPaint)
        canvas.drawBitmap(bgBitmap, 0f, 0f, bgPaint)
        if (srcBitmap != null) {
            srcBitmap = getResizeBitmap(srcBitmap)
            canvas.drawBitmap(srcBitmap, (bgBitmap.width / 2 - srcBitmap.width / 2).toFloat(), (bgBitmap.width / 2 - srcBitmap.width / 2).toFloat(), paint)
        }
    }

    fun setPadding(padding: Int) {
        this.padding = padding
        invalidate()
    }

    fun setPinColor(@ColorInt color: Int) {
        this.pin_color = color
        bgPaint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        invalidate()
    }

    fun setPinImage(drawableId: Int) {
        srcBitmap = ActivityUtils.getBitmapFromDrawable(ContextCompat.getDrawable(context, drawableId))
        invalidate()
    }

    fun setPinBitmap(bitmap: Bitmap) {
        srcBitmap = bitmap
        invalidate()
    }

    fun setPinUrl(url: String) {
        Picasso.with(context).load(url)
                .transform(CircleTransform())
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        srcBitmap = bitmap!!
                        invalidate()
                    }
                })
    }


    fun getPinBitmap(): Bitmap? {
        setDrawingCacheEnabled(true)
        measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight())
        buildDrawingCache(true)
        val b = Bitmap.createBitmap(drawingCache)
        setDrawingCacheEnabled(false)
        return b;
    }

    fun getBitmapPin(v: View): Bitmap? {
        val b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height)
        v.draw(c)
        return b
    }
}
