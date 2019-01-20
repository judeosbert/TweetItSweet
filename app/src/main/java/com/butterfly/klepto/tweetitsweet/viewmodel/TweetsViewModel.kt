package com.butterfly.klepto.tweetitsweet.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.butterfly.klepto.tweetitsweet.repository.TweetsRepository
import twitter4j.Status

class TweetsViewModel: ViewModel() {


    lateinit var query:String
    var tweets:MutableLiveData<List<Status>> = MutableLiveData()
    var tweetsRepository: TweetsRepository? = null
    var hasNext:MutableLiveData<Boolean> = MutableLiveData();

    fun fetchResults(query:String,isPagination:Boolean){
        this.query  = query
        if(tweetsRepository == null) {
            tweetsRepository = TweetsRepository()
        }

        tweetsRepository!!.getResults(this.query,object : TweetsRepository.TweetRepositoryCallback {
            override fun onResultReady(result: List<Status>, hasNextPage: Boolean) {
                tweets.value = result
                hasNext.value = hasNextPage
            }
        },isPagination)
    }

    fun getData():LiveData<List<Status>>{
        return this.tweets
    }

    fun getIsHaveNext():LiveData<Boolean>{
        return this.hasNext
    }

}