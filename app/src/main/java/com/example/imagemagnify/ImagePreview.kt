package com.example.imagemagnify
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.imagemagnify.databinding.ActivityImagePreviewBinding

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private lateinit var loupeView: LoupeView
    private var magnificationFactor = 1.0f
    private var loupeRadius = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loupeView = binding.loupeView
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        if (imageBitmap != null) {
            binding.imageView.setImageBitmap(imageBitmap)
            loupeView.setNewImage(imageBitmap, imageBitmap)
        }

        binding.sbFactorBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                magnificationFactor = (progress / 20f) + 1
                updateLoupeViewSettings()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.sbRadiusBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loupeRadius = progress
                updateLoupeViewSettings()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateLoupeViewSettings() {
        loupeView.setMagnificationFactor(magnificationFactor)
        loupeView.setLoupeRadius(loupeRadius)
    }
}

