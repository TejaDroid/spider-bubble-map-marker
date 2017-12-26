package com.tejadroid.spiderview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.tejadroid.spiderview.R
import com.tejadroid.spiderview.utils.ActivityUtils
import java.util.*

/**
 * Created by Tejas on 12/15/17.
 */
class SpiderLineView : View {
    lateinit var paint: Paint

    internal var icon_size = 29
    internal var line = 5
    internal var insideRadius = 19
    internal var outsideRadius = 88
    internal var lines_width = 2
    internal var lines_height = 55
    internal var line_color: Int = Color.parseColor("#000000")

    internal var pointAngle = 36f
    internal var startAngle = 180f
    internal var endAngle = 360f


    private var drawableResource = ArrayList<Int>()
    private var viewClickPoint: MutableMap<Point, Int> = mutableMapOf<Point, Int>()
    private var clickablePoint: Point? = null

    private var pointImage: Bitmap? = null
    private var markerImage: Bitmap? = null
    private var clickArea = 50

    private val inflater = LayoutInflater.from(context)
    private var iconView = inflater.inflate(R.layout.round_shape, null, false)
    private var iv = iconView.findViewById<ImageView>(R.id.iv)

    internal var angle = 0f

    private var mOnClickArcView: OnSpiderItemClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getXmlVariableValue(context, attrs, 0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getXmlVariableValue(context, attrs, defStyleAttr)
        init()
    }

    private fun getXmlVariableValue(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.drawline, defStyleAttr, 0)
        try {
            icon_size = ta.getInt(R.styleable.drawline_icon_size, 29)
            line = ta.getInt(R.styleable.drawline_lines, 5)
            insideRadius = ta.getInt(R.styleable.drawline_inside_radius, 19)
            lines_width = ta.getInt(R.styleable.drawline_lines_width, 1)
            lines_height = ta.getInt(R.styleable.drawline_lines_height, 50)
            line_color = ta.getColor(R.styleable.drawline_line_color, Color.parseColor("#000000"))
        } finally {
            ta.recycle()
        }
    }

    private fun init() {
        viewClickPoint = HashMap()
        iv = iconView.findViewById(R.id.iv)
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = line_color
        paint.strokeWidth = ActivityUtils.dpToPx(context, lines_width).toFloat()
        paint.style = Paint.Style.FILL
        drawableResource.add(R.drawable.logo1)
        markerImage = BitmapFactory.decodeResource(getResources(), R.drawable.pin)
        markerImage = Bitmap.createScaledBitmap(markerImage, ActivityUtils.dpToPx(context, 40),
                ActivityUtils.dpToPx(context, 57), false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (line > 0) {
            getAngles()
            val startX = width / 2
            val startY = height / 2

            var position = 0

            viewClickPoint.clear()
            //move round the circle to each point
            angle = startAngle
            while (angle < endAngle) {

                pointImage = getBitmap(drawableResource[position % drawableResource.size])

                if (pointImage != null) {

                    //convert angle to radians for x and y coordinates
                    val x = (startX + Math.cos(Math.toRadians(angle.toDouble())).toFloat() * insideRadius).toInt()
                    val y = (startY + Math.sin(Math.toRadians(angle.toDouble())).toFloat() * insideRadius).toInt()

                    outsideRadius = insideRadius + ActivityUtils.dpToPx(context, lines_height)

                    val xPos = (startX + Math.cos(Math.toRadians(angle.toDouble())).toFloat() * outsideRadius).toInt()
                    val yPos = (startY + Math.sin(Math.toRadians(angle.toDouble())).toFloat() * outsideRadius).toInt()

                    canvas.drawLine(x.toFloat(), y.toFloat(), xPos.toFloat(), yPos.toFloat(), paint) //draw a line from center point back to the point
                    canvas.drawBitmap(pointImage!!, (xPos - pointImage!!.width / 2).toFloat(), (yPos - pointImage!!.height / 2).toFloat(), paint)
                    clickArea = pointImage!!.width / 2
                    clickablePoint = Point()
                    clickablePoint!!.set(xPos, yPos)
                    viewClickPoint.put(clickablePoint!!, position)
                }
                position = position + 1
                angle = angle + pointAngle

//                markerImage!!.width / 2
//                canvas.drawBitmap(markerImage!!, (startX - markerImage!!.width / 2).toFloat(), (startY - markerImage!!.width / 2).toFloat(), paint)

            }
        }

    }

    private fun getAngles() {
        when (line) {
            4 -> {
                pointAngle = 36f
                startAngle = 180 + pointAngle
                endAngle = startAngle + pointAngle * 4
            }
            5 -> {
                pointAngle = 30f
                startAngle = 180 + pointAngle
                endAngle = startAngle + pointAngle * 5
            }
            6 -> {
                pointAngle = 45f
                startAngle = 180 - pointAngle / 2
                endAngle = startAngle + pointAngle * 6
            }
            7 -> {
                pointAngle = 45f
                startAngle = 180 - pointAngle
                endAngle = startAngle + pointAngle * 7
            }
            8 -> {
                pointAngle = 35f
                startAngle = 180 - pointAngle
                endAngle = startAngle + pointAngle * 8
            }
            else -> {
                pointAngle = (360 / (line + 1)).toFloat()
                startAngle = 90 + pointAngle
                endAngle = startAngle + pointAngle * line
            }
        }
    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        iv.setImageResource(drawableRes)
        iv.isDrawingCacheEnabled = true
        iv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        iv.layout(0, 0, ActivityUtils.dpToPx(context, icon_size), ActivityUtils.dpToPx(context, icon_size))
        iv.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(iv.drawingCache)
        iv.isDrawingCacheEnabled = false
        return bitmap
    }

    override fun performClick(): Boolean {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick()

        // Handle the action for the custom click here

        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
            val xTouch = event.x.toInt()
            val yTouch = event.y.toInt()
            for (point in viewClickPoint.keys) {
                if (point.x - clickArea <= xTouch && xTouch <= point.x + clickArea && point.y - clickArea <= yTouch && yTouch <= point.y + clickArea) {
                    if (mOnClickArcView != null) {
                        mOnClickArcView!!.onSpiderItemClick(viewClickPoint[point]!!)
                    }
                    return true
                }
            }
        }
        return false
    }

    fun setSpiderImages(resources: ArrayList<Int>) {
        drawableResource = resources
        line = drawableResource.size
        invalidate()
    }

    fun setOnSpiderItemClickListener(listener: OnSpiderItemClickListener) {
        mOnClickArcView = listener
    }

    interface OnSpiderItemClickListener {
        fun onSpiderItemClick(clickPosition: Int)
    }
}