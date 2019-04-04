package com.butterfly.klepto.tweetitsweet.twitter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.butterfly.klepto.tweetitsweet.constants.Constants
import com.butterfly.klepto.tweetitsweet.twitter.asynctasks.GetPopularTagsTask
import com.butterfly.klepto.tweetitsweet.twitter.asynctasks.GetTweetsTask
import io.reactivex.Observable
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder


class TwitterManager {

    companion object{


        var configBuilder: ConfigurationBuilder? = ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(Constants.OAuthConsumerKey)
            .setOAuthConsumerSecret(Constants.OAuthConsumerKeySecret)
            .setOAuthAccessToken(Constants.AccessTokenKey)
            .setOAuthAccessTokenSecret(Constants.AccessTokenKeySecret)
        var twitterFactory = TwitterFactory(configBuilder!!.build())


        fun getTwitterInstance(): Twitter? {
            return twitterFactory.instance

        }
        fun getTweets(query:Query,listener:TwitterManagerSearchCallback){

            GetTweetsTask(listener).execute(query)
        }



        fun getPopularTags(): Observable<Trends> {
            return com.butterfly.klepto.tweetitsweet.twitter.rxdatafetchers.getPopularTags()
        }

    }



    interface TwitterManagerSearchCallback{
        fun onSearchResultReady(queryResult: QueryResult):MutableLiveData<QueryResult>
    }
    interface TwitterManagerPopularTagsCallback{
        fun onPopularTagsReady(results: Trends)
    }
}