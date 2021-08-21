package com.wafflestudio.snuday.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.wafflestudio.snuday.R


class EventView : androidx.appcompat.widget.AppCompatTextView {

    init {
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9F)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setLineSpacing(resources.getDimension(R.dimen.event_line_spacing), 1.0F)
        ellipsize = TextUtils.TruncateAt.END
        maxLines = 1
        setPadding(
            resources.getDimension(R.dimen.event_horizontal_padding).toInt(),
            0,
            resources.getDimension(R.dimen.event_horizontal_padding).toInt(),
            0
        )

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
