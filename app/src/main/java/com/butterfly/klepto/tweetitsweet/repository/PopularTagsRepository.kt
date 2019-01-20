package com.butterfly.klepto.tweetitsweet.repository

import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Trends

class PopularTagsRepository : TwitterManager.TwitterManagerPopularTagsCallback {

    private lateinit var mListener:PopularTagsCallback

    fun fetchPopularTags(listener:PopularTagsCallback){
        mListener  = listener;
        TwitterManager.getPopularTags(this)
    }

    override fun onPopularTagsReady(results: Trends) {
        mListener.onPopularTagsReady(results)
    }

    interface PopularTagsCallback{
        fun onPopularTagsReady(result:Trends)
    }
}