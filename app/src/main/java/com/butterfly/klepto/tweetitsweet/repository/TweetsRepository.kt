package com.butterfly.klepto.tweetitsweet.repository

import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status

class TweetsRepository : TwitterManager.TwitterManagerSearchCallback {
    private lateinit var mListener: TweetRepositoryCallback
    private var nextQuery: Query? = null

    fun getResults(query: String, listener: TweetRepositoryCallback, isPagination: Boolean) {
        mListener = listener
        if (isPagination && nextQuery != null) {
            TwitterManager.getTweets(nextQuery!!, this)
        } else {
            val queryObj = Query(query)
            queryObj.resultType = Query.ResultType.recent
            TwitterManager.getTweets(queryObj, this)
        }
    }

    override fun onSearchResultReady(queryResult: QueryResult) {
        if (queryResult.hasNext() )
            nextQuery = queryResult.nextQuery()
        mListener.onResultReady(queryResult.tweets,queryResult.hasNext())
    }

    interface TweetRepositoryCallback {
        fun onResultReady(result: List<Status>, hasNext: Boolean)
    }
}