package com.wafflestudio.snuday.views

import android.content.Context
import android.icu.util.Measure
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ViewEventBinding
import timber.log.Timber

class EventView : androidx.appcompat.widget.AppCompatTextView {

    init {
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9F)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setLineSpacing(resources.getDimension(R.dimen.event_line_spacing), 1.0F)
    }

    var startDayOfWeek: Int = 0
    var endDayOfWeek: Int = 6
    var line: Int = 0

    private var basicWidth: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        basicWidth = MeasureSpec.getSize(widthMeasureSpec) / 7
        mWidth = (MeasureSpec.getSize(widthMeasureSpec) / 7) * (endDayOfWeek - startDayOfWeek + 1)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)

        Timber.d("mWidth ${mWidth}, mHeight ${mHeight}")
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(
            l + basicWidth * startDayOfWeek,
            t + getLineSize(line),
            l + basicWidth * startDayOfWeek + mWidth,
            b + getLineSize(line)
        )
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
