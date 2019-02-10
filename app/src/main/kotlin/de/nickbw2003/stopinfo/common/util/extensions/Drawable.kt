package de.nickbw2003.stopinfo.common.util.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.VectorDrawable


fun VectorDrawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)

    with(Canvas(bitmap)) {
        setBounds(0, 0, width, height)
        draw(this)
    }

    return bitmap
}

fun LayerDrawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    bounds = Rect(0, 0, intrinsicWidth, intrinsicHeight)
    draw(Canvas(bitmap))
    return bitmap
}