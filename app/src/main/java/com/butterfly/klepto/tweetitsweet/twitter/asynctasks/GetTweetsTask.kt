package com.butterfly.klepto.tweetitsweet.twitter.asynctasks

import android.os.AsyncTask
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.TwitterException

class GetTweetsTask(listener: TwitterManager.TwitterManagerSearchCallback): AsyncTask<Query, Void, QueryResult>() {
    var mListener = listener

    override fun doInBackground(vararg params: Query?): QueryResult? {

        try {
            return TwitterManager.getTwitterInstance()!!.search(params[0])
        } catch (e: TwitterException) {
            e.printStackTrace()
        }
        return null

    }

    override fun onPostExecute(queryResult: QueryResult?) {
        if(queryResult != null) {
            super.onPostExecute(queryResult)
            mListener.onSearchResultReady(queryResult)
        }
    }
}