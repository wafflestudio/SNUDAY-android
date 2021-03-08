package com.wafflestudio.snuday.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ViewCalendarCustomBinding
import com.wafflestudio.snuday.databinding.ViewEventBinding
import timber.log.Timber

class CustomEventView : FrameLayout {

    private val binding: ViewEventBinding

    var startDayOfWeek: Int = 0
    var endDayOfWeek: Int = 6
    var line: Int = 0
    var text: String = "Sample Text"
    set(value) {
        field = value
        binding.textEvent.text = value
    }

    private var basicWidth: Int = 0
    private var mWidth: Int = 0

    constructor(context: Context) : super(context) {
        binding = ViewEventBinding.inflate(LayoutInflater.from(context), this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        binding = ViewEventBinding.inflate(LayoutInflater.from(context), this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = ViewEventBinding.inflate(LayoutInflater.from(context), this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        basicWidth = MeasureSpec.getSize(widthMeasureSpec) / 7
        mWidth = (MeasureSpec.getSize(widthMeasureSpec) / 7) * (endDayOfWeek - startDayOfWeek + 1)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        getChildAt(0).layout(
            left + basicWidth * startDayOfWeek,
            top + getLineSize(line),
            left + basicWidth * startDayOfWeek + mWidth,
            top + getLineSize(line) + resources.getDimensionPixelSize(R.dimen.event_height)
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