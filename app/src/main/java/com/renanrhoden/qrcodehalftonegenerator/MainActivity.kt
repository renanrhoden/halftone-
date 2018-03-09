package com.renanrhoden.qrcodehalftonegenerator

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.renanrhoden.qrcodehalftonegenerator.halftone.transformations.ColorHalftoneTransformation
import com.renanrhoden.qrcodehalftonegenerator.utils.toBlackAndWhite
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import jp.wasabeef.picasso.transformations.CropTransformation
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dottedImage.tag = getTarget()
        loadImage(4, 6)
    }

    private fun loadImage(halftone: Int, vignette: Int) {
        val v = 1f - vignette / 100f
        Picasso.with(this)
                .load(R.drawable.dory)
                .placeholder(R.drawable.ic_launcher_background)
                .transform(CropTransformation(1f, CropTransformation.GravityHorizontal.CENTER,
                        CropTransformation.GravityVertical.CENTER))
                .transform(CropTransformation(1f, CropTransformation.GravityHorizontal.CENTER,
                        CropTransformation.GravityVertical.CENTER))
                .transform(ColorHalftoneTransformation(halftone.toFloat()))
                .transform(VignetteFilterTransformation(applicationContext, PointF(0.5f, 0.5f),
                        floatArrayOf(0.0f, 0.0f, 0.0f), 0f, v)) //0.75f*/
                .into(dottedImage.tag as Target)
    }

    private fun getTarget() = object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            val bitmapBlackWhite = toBlackAndWhite(bitmap)
            this@MainActivity.bitmap = bitmapBlackWhite
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        }
    }

}
