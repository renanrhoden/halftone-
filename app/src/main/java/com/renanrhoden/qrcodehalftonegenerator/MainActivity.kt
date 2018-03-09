package com.renanrhoden.qrcodehalftonegenerator

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.renanrhoden.qrcodehalftonegenerator.R.drawable.*
import com.renanrhoden.qrcodehalftonegenerator.halftone.transformations.ColorHalftoneTransformation
import com.renanrhoden.qrcodehalftonegenerator.utils.toBlackAndWhite
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import jp.wasabeef.picasso.transformations.CropTransformation
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dottedImage.tag = getTarget()
        loadImage(1, 6)
    }

    private fun loadImage(halftone: Int, vignette: Int) {
        val v = 1f - vignette / 100f
        val bitmap = BitmapFactory.decodeResource(
                resources,
                dory
        )
        val filePath = applicationContext.filesDir.path.toString() + "/dorybw.txt"
        val file = File(filePath)
        file.setWritable(true)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }

        Picasso.with(this)
                .load(file.absolutePath + "dorybw")
                .placeholder(ic_launcher_background)
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
            dottedImage.setImageBitmap(bitmapBlackWhite)
            this@MainActivity.bitmap = bitmapBlackWhite
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
            Toast.makeText(applicationContext, "Erro ao carregar bitmap", Toast.LENGTH_SHORT).show()
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        }
    }

}
