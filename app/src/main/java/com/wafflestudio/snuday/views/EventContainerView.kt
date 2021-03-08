package com.wafflestudio.snuday.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.wafflestudio.snuday.R
import timber.log.Timber

class EventContainerView : FrameLayout {

    private var mWidth: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Timber.d("width : ${width}, measuredWidth : ${measuredWidth}, getSize : ${MeasureSpec.getSize(widthMeasureSpec)}")
        mWidth = MeasureSpec.getSize(widthMeasureSpec) / 7

    }

    fun addEvent(eventView: EventView) {

        this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        Timber.d("After Measure ${this.measuredWidth}")

        val par = LayoutParams(
            mWidth * (eventView.endDayOfWeek - eventView.startDayOfWeek + 1),
            resources.getDimensionPixelSize(R.dimen.event_height)
        )

        par.apply {
            this.marginStart = mWidth * eventView.startDayOfWeek
            this.topMargin = getLineSize(eventView.line)
        }

        Timber.d("width : ${par.width}, height : ${par.height}")

        eventView.layoutParams = par
        this.addView(eventView)
    }

    private fun getLineSize(line: Int): Int {
        return when (line) {
            0       -> resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
            1       -> resources.getDimensionPixelSize(R.dimen.second_line_event_margin)
            2       -> resources.getDimensionPixelSize(R.dimen.third_line_event_margin)
            3       -> resources.getDimensionPixelSize(R.dimen.fourth_line_event_margin)
            4       -> resources.getDimensionPixelSize(R.dimen.fifth_line_event_margin)
            else    -> resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
        }
    }



}