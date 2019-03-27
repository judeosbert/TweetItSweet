package com.butterfly.klepto.tweetitsweet.extensions

import twitter4j.Status
import java.util.*

fun Status.getRank():Double{

    val growthOfUser:Double =  computerUserGrowth(this)

    val growthOfPost:Double = computePostGrowth(this)

    var isVerified:Int = 0
    if (this.user.isVerified)
        isVerified = 1
    return growthOfPost+growthOfUser + isVerified
}


fun computePostGrowth(status: Status): Double {

    val secondsSincePost = getSecondsSincePost(status.user.createdAt.time)
    val currentActivityStatus = status.favoriteCount+(1.5*status.retweetCount)
    return (currentActivityStatus/secondsSincePost)
}

fun getSecondsSincePost(time: Long): Int {
    val diff:Double = (Date().time - time).toDouble()
    return Math.round(diff/1000).toInt()

}

fun computerUserGrowth(status: Status): Double {

    val totalFollowers = status.user.followersCount
    val noOfDaysSinceAccountCreation = getNumberOfDays(status.user.createdAt.time)
    return (totalFollowers/noOfDaysSinceAccountCreation).toDouble()



}

fun getNumberOfDays(time: Long): Int {
    var diff:Double = (Date().time - time).toDouble()
    diff = (((diff/1000)/60)/60)/24
    return Math.round(diff).toInt()

}
