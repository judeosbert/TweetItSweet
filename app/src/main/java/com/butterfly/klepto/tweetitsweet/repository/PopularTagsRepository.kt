package com.butterfly.klepto.tweetitsweet.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import io.reactivex.internal.operators.observable.ObservableTake
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import twitter4j.Trend
import twitter4j.Trends

class PopularTagsRepository {


    @SuppressLint("CheckResult")
    fun fetchPopularTags():LiveData<List<Trend>>{
        var response:MutableLiveData<List<Trend>> = MutableLiveData()
       TwitterManager.getPopularTags()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .flatMap{
               return@flatMap Observable.fromIterable(it.trends.toMutableList())
           }
           .collectInto(ArrayList<Trend>()) { acc, value -> acc.add(value)  }
           .subscribeBy{
               response.postValue(it)
           }
        return response
    }




}