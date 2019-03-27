package com.butterfly.klepto.tweetitsweet.viewmodel

import android.arch.lifecycle.*
import com.butterfly.klepto.tweetitsweet.repository.PopularTagsRepository
import twitter4j.Trend
import twitter4j.Trends

class PopularTagsViewModel(repository:PopularTagsRepository): ViewModel() {

    var trendResultLiveData:MediatorLiveData<List<Trend>> = MediatorLiveData()
    private var mRepository = repository

    fun fetchPopularTags(){
        trendResultLiveData.addSource(mRepository.fetchPopularTags()
        ) { t -> trendResultLiveData.value = t }
    }


}