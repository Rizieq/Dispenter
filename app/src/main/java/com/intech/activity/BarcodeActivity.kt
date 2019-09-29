package com.intech.activity

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.intech.R
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BarcodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionGenerateQrcode.setOnClickListener {
            actionGenerateQrcode()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun actionGenerateQrcode() {
        GlobalScope.launch(Dispatchers.IO) {
            val bitmapMatrix = MultiFormatWriter().run {
                encode(
                    TEXT_TO_ENCODE, BarcodeFormat.QR_CODE,
                    QRCODE_SIZE,
                    QRCODE_SIZE
                )
            }
            val qrCode = BarcodeEncoder().createBitmap(bitmapMatrix)
            launch(Dispatchers.Main) {
                displayQrCode(qrCode)
            }
        }
    }

    private fun displayQrCode(qrCode: Bitmap) {
        dispayQrCode.setImageBitmap(qrCode)
    }

    companion object {
        private const val QRCODE_SIZE = 200
        private const val TEXT_TO_ENCODE = "jadi nih ra :p"
    }
}
