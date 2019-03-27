package com.butterfly.klepto.tweetitsweet.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status

class TweetsRepository : TwitterManager.TwitterManagerSearchCallback {

    private var nextQuery: Query? = null

    private var queryResultLiveData:MutableLiveData<QueryResult>  = MutableLiveData()

    fun getResults(query: String, isPagination: Boolean):LiveData<QueryResult> {
        if (isPagination && nextQuery != null) {
            TwitterManager.getTweets(nextQuery!!, this)
        } else {
            val queryObj = Query(query)
            queryObj.resultType = Query.ResultType.recent
            TwitterManager.getTweets(queryObj, this)
        }
        return queryResultLiveData
    }

    override fun onSearchResultReady(queryResult: QueryResult):MutableLiveData<QueryResult> {
        if (queryResult.hasNext() )
            nextQuery = queryResult.nextQuery()
        queryResultLiveData.value = queryResult
        return MutableLiveData()
    }
}