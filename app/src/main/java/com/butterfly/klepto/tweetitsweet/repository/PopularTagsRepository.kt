package com.butterfly.klepto.tweetitsweet.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Trend
import twitter4j.Trends

class PopularTagsRepository {


    fun fetchPopularTags():LiveData<List<Trend>>{
        var response:MutableLiveData<List<Trend>> = MutableLiveData()
        TwitterManager.getPopularTags(object : TwitterManager.TwitterManagerPopularTagsCallback {
            override fun onPopularTagsReady(results: Trends) {
                response.value = results.trends.toList()
            }
        })
        return response;
    }
}