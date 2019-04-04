package com.butterfly.klepto.tweetitsweet.twitter.asynctasks

import android.os.AsyncTask
import com.butterfly.klepto.tweetitsweet.constants.Constants.Companion.WORLD_WOEID
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Trends

class GetPopularTagsTask(listener: TwitterManager.TwitterManagerPopularTagsCallback): AsyncTask<Void, Void, Trends>() {
    var mListener = listener

    override fun doInBackground(vararg params: Void): Trends {

        return TwitterManager.getTwitterInstance()!!.trends().getPlaceTrends(WORLD_WOEID)
    }

    override fun onPostExecute(result: twitter4j.Trends) {
        super.onPostExecute(result)
        mListener.onPopularTagsReady(result)
    }

}