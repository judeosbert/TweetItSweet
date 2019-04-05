package com.butterfly.klepto.tweetitsweet.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status
import java.util.*

class TweetsRepository : TwitterManager.TwitterManagerSearchCallback {

    private var nextQuery: Query? = null

    private var queryResultLiveData:MutableLiveData<QueryResult>  = MutableLiveData()



    @SuppressLint("CheckResult")
    fun getResults(query: String, isPagination: Boolean):LiveData<QueryResult> {
        if (isPagination && nextQuery != null) {
            TwitterManager.getTweets(nextQuery!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        nextQuery = if(it.hasNext())
                            it.nextQuery() else null
                        queryResultLiveData.postValue(it)},
                    onError = {it.printStackTrace()}
                )

        } else {
            if (query.isNotEmpty()) {
                val queryObj = Query(query)
                queryObj.resultType = Query.ResultType.recent
                TwitterManager.getTweets(queryObj)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onNext = {
                            nextQuery = if(it.hasNext())
                                it.nextQuery()
                            else
                                null
                            queryResultLiveData.postValue(it) },
                        onError = { it.printStackTrace() }
                    )
            }
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