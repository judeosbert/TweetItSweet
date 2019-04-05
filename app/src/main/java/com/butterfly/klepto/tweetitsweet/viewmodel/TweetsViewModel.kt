package com.butterfly.klepto.tweetitsweet.viewmodel

import android.arch.lifecycle.*
import com.bumptech.glide.Glide.init
import com.butterfly.klepto.tweetitsweet.repository.TweetsRepository
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager.Companion.getTweets
import twitter4j.QueryResult
import twitter4j.Status

class TweetsViewModel(repository: TweetsRepository): ViewModel() {

    private var mIsDataBeingLoaded:Boolean  = false

    var tweetsRepository = repository
    var query:String = String()
    var hasNext:MutableLiveData<Boolean> = MutableLiveData()
    var tweetsLiveData:MediatorLiveData<List<Status>> = MediatorLiveData()
    var respositoryLiveData = tweetsRepository.getResults(query,mIsDataBeingLoaded)
    init{
        tweetsLiveData.addSource(respositoryLiveData) {result:QueryResult? ->
            tweetsLiveData.postValue(result!!.tweets)
            hasNext.postValue(result.hasNext())
        }
    }

     fun restLoadingFlag(){
        mIsDataBeingLoaded = false
    }



    private fun getTweets(query:String){
        tweetsRepository.getResults(query,mIsDataBeingLoaded)

    }

    fun getIsDataBeingLoaded():Boolean{
        return mIsDataBeingLoaded
    }

    fun fetchResults(query:String){
        tweetsLiveData.postValue(ArrayList())
        getTweets(query)
    }

    fun loadMore(totalItemCount: Int, lastVisibleItem: Int) {

        if(lastVisibleItem == totalItemCount - 1){
            if(!mIsDataBeingLoaded && hasNext.value!!){
                mIsDataBeingLoaded = true
                getTweets("")
            }
        }

    }



    fun getIsHaveNext():LiveData<Boolean>{
        return this.hasNext
    }

}