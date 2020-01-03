package com.intech.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.intech.R
import com.intech.data.UserToken
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
        val data = intent.getStringExtra(TYPE)
        Log.d("LOG_QR",data)

        if (data.equals("normal")){
            txtTitleBarcode.setText("Normal Water")
        } else if(data.equals("dingin")){
            txtTitleBarcode.setText("Cold Water")
        }


        if (!data.isNullOrEmpty()) {
            actionGenerateQrcode(data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun actionGenerateQrcode(type: String) {
        val encodedText = "${UserToken.email},$type"
        GlobalScope.launch(Dispatchers.IO) {
            val bitmapMatrix = MultiFormatWriter().run {
                encode(
                    encodedText, BarcodeFormat.QR_CODE,
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
        const val TYPE = "type"
    }
}
