package com.renanrhoden.qrcodehalftonegenerator.utils

import android.graphics.Bitmap
import android.graphics.Color

fun toBlackAndWhite(bitmap: Bitmap): Bitmap {
    val bwBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)
    val hsv = FloatArray(3)
    for (col in 0 until bitmap.width) {
        for (row in 0 until bitmap.height) {
            Color.colorToHSV(bitmap.getPixel(col, row), hsv)
            if (hsv[2] > 0.5f) {
                bwBitmap.setPixel(col, row, -0x1000000)
            } else {
                bwBitmap.setPixel(col, row, -0x1)
            }
        }
    }
    return bwBitmap
}