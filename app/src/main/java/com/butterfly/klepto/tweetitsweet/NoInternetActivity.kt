package com.butterfly.klepto.tweetitsweet

import android.app.Activity
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.widget.Button
import com.butterfly.klepto.tweetitsweet.utils.CommonUtils

class NoInternetActivity : AppCompatActivity() {

    lateinit var mRetryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        setStatusBarColor(ContextCompat.getColor(this,R.color.darkColor))
        mRetryButton = findViewById(R.id.retry_button)
        mRetryButton.setOnClickListener {
            if(CommonUtils.isInternetConnected(this))
                setResult(Activity.RESULT_OK)
                finish()
        }
        setStatusBarColor(ContextCompat.getColor(this,R.color.darkColor))

    }
    private fun setStatusBarColor(color: Int) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }
}
