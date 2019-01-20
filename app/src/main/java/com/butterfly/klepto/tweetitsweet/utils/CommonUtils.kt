package com.butterfly.klepto.tweetitsweet.utils

import android.content.Context
import android.net.ConnectivityManager
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.TextView


class CommonUtils {

    companion object {
        fun setColor(view: TextView, fulltext: String, subtext: String, color: Int) {
            view.setText(fulltext, TextView.BufferType.SPANNABLE)
            val str = view.text as Spannable
            var subTextTypes:MutableList<String> = mutableListOf()
            subTextTypes.add(subtext.capitalize())
            subTextTypes.add(subtext.decapitalize())
            subTextTypes.add(subtext.toUpperCase())
            subTextTypes.add(subtext.toLowerCase())
            for (hightlightString in subTextTypes) {
                val i = fulltext.indexOf(hightlightString)
                if (i > -1)
                    str.setSpan(ForegroundColorSpan(color), i, i + hightlightString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return (activeNetworkInfo != null && activeNetworkInfo.isConnected)

        }


    }

}