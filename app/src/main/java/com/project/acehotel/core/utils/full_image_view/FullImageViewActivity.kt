package com.project.acehotel.core.utils.full_image_view

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.acehotel.databinding.ActivityFullImageViewBinding
import timber.log.Timber
import java.lang.Float.max
import java.lang.Float.min

class FullImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullImageViewBinding

    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        loadImage()

        handleButtonBack()

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun loadImage() {
        val imageSource = intent.getStringExtra(IMAGE_SOURCE)
        Timber.tag("IMAGE").e(imageSource)
        Glide.with(this).load(imageSource).into(binding.ivFullImage)
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        mScaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))

            binding.ivFullImage.scaleX = mScaleFactor
            binding.ivFullImage.scaleY = mScaleFactor

            return true
        }
    }

    companion object {
        private const val IMAGE_SOURCE = "image_source"
    }
}