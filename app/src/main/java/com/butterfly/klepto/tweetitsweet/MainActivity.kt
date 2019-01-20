package com.butterfly.klepto.tweetitsweet

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.widget.EditText
import com.butterfly.klepto.tweetitsweet.constants.Constants.Companion.QUERY
import com.butterfly.klepto.tweetitsweet.fragments.HomeFragment
import com.butterfly.klepto.tweetitsweet.fragments.SearchResultFragment
import com.butterfly.klepto.tweetitsweet.utils.CommonUtils
import android.view.WindowManager
import android.os.Build
import android.support.v4.content.ContextCompat


class MainActivity : AppCompatActivity(),HomeFragment.OnFragmentInteractionListener {


    override fun showQueryResults(query: String,editText: EditText) {
        var instance = SearchResultFragment.newInstance()
        var args = Bundle()
        args.putString(QUERY,query)
        instance.arguments = args
        supportFragmentManager.beginTransaction()
            .addSharedElement(editText,ViewCompat.getTransitionName(editText)!!)
            .addToBackStack("tag")
            .replace(R.id.frame,instance)
            .commit()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame,HomeFragment.newInstance())
                .commit()

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


    override fun onStart() {
        super.onStart()
        showNoInternet()

    }

    override fun showNoInternet() {
        if(!CommonUtils.isInternetConnected(this)){
            startActivityForResult(Intent(this,NoInternetActivity::class.java),1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame,HomeFragment.newInstance())
                    .commit()
            }
        }
    }
}
