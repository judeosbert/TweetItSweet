package com.butterfly.klepto.tweetitsweet.twitter

import com.butterfly.klepto.tweetitsweet.constants.Constants
import com.butterfly.klepto.tweetitsweet.twitter.asynctasks.GetPopularTagsTask
import com.butterfly.klepto.tweetitsweet.twitter.asynctasks.GetTweetsTask
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



        fun getPopularTags(listener:TwitterManagerPopularTagsCallback){
            GetPopularTagsTask(listener).execute()
        }

    }



    interface TwitterManagerSearchCallback{
        fun onSearchResultReady(queryResult: QueryResult)
    }
    interface TwitterManagerPopularTagsCallback{
        fun onPopularTagsReady(results: Trends)
    }
}