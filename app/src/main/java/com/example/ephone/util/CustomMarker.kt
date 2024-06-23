package com.example.ephone.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.ephone.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat


@SuppressLint("ViewConstructor")
class CustomMarker(context: Context, layoutResource: Int):
    MarkerView(context, layoutResource) {
    private var tvContent: TextView = findViewById(R.id.tvContent);

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val value = entry?.y?.toDouble() ?: 0.0
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        val decimalFormatSymbols: DecimalFormatSymbols =
            (format as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol=""
        format.decimalFormatSymbols=decimalFormatSymbols
        tvContent.text = "ETB ${format.format(value)}"
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}