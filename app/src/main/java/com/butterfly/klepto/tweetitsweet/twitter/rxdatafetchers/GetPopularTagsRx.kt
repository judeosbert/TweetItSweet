package com.butterfly.klepto.tweetitsweet.twitter.rxdatafetchers

import com.butterfly.klepto.tweetitsweet.constants.Constants
import com.butterfly.klepto.tweetitsweet.twitter.TwitterManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import twitter4j.Trends
import java.util.*



    fun getPopularTags(): Observable<Trends> {
      return io.reactivex.Observable.fromCallable {
          TwitterManager.getTwitterInstance()!!.trends().getPlaceTrends(Constants.WORLD_WOEID)
      }
    }
