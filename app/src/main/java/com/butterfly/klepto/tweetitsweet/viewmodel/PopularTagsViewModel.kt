package com.butterfly.klepto.tweetitsweet.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.butterfly.klepto.tweetitsweet.repository.PopularTagsRepository
import twitter4j.Trend
import twitter4j.Trends

class PopularTagsViewModel: ViewModel(), PopularTagsRepository.PopularTagsCallback {


    private var trends:MutableLiveData<List<Trend>> = MutableLiveData()
    private var repository: PopularTagsRepository? = null

    fun fetchPopularTags(){
        if(repository == null){
            repository = PopularTagsRepository()
        }
        repository!!.fetchPopularTags(this)

    }

    fun getData(): MutableLiveData<List<Trend>> {
        return trends;
    }


    override fun onPopularTagsReady(result: Trends) {
        trends.value = result.trends.toList();
    }

}