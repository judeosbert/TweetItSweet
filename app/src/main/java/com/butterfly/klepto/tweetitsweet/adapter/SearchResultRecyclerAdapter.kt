package com.butterfly.klepto.tweetitsweet.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.butterfly.klepto.tweetitsweet.R
import com.butterfly.klepto.tweetitsweet.utils.CommonUtils
import twitter4j.Status
import java.text.SimpleDateFormat

class SearchResultRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mData:MutableList<Status> = mutableListOf()
    private var mUnSortedData:MutableList<Status> = mutableListOf()
    private lateinit var mContext:Context
    private var postDateFormat = SimpleDateFormat("MMM dd")
    private var hasNextPage: Boolean = true
    private lateinit var mQuery:String
    private val ITEM_VIEW_TYPE_BASIC = 0
    private val ITEM_VIEW_TYPE_FOOTER = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mContext = parent.context
        var view:View
            if(viewType == ITEM_VIEW_TYPE_BASIC) {
                view = LayoutInflater.from(mContext).inflate(R.layout.search_result_rv_item, parent, false)
                return ViewHolder(view)
            }
            else {
                view = LayoutInflater.from(mContext).inflate(R.layout.progressbar_rv_item, parent, false)
                return ProgressViewHolder(view)
                }


    }

    fun getUnsortedData():MutableList<Status>{
        return mData
    }

    override fun getItemViewType(position: Int): Int {
        if(position == mData.size){
            return ITEM_VIEW_TYPE_FOOTER
        }
        return ITEM_VIEW_TYPE_BASIC
    }

    override fun getItemCount(): Int {
        return mData.size+1
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            val currentStatus = mData[position]
            holder.userNameTV.text = currentStatus.user.name

            if(currentStatus.user.isVerified){
                holder.verifiedIV.visibility= View.VISIBLE
            }else{
                holder.verifiedIV.visibility= View.INVISIBLE
            }


            holder.userHandleTV.text = mContext.getString(R.string.userHandlePlaceholder, currentStatus.user.screenName)
            Glide.with(mContext).load(currentStatus.user.biggerProfileImageURLHttps)
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.placeholder_circle)
                )

                .into(holder.userImageIV)
            CommonUtils
                .setColor(
                    holder.postContentTV, currentStatus.text, mQuery
                    , ContextCompat.getColor(mContext, R.color.highlightedText)
                )
            val formattedDateText = postDateFormat.format(currentStatus.createdAt)
            holder.postDateTV.text = formattedDateText

            holder.favCountTV.text = currentStatus.favoriteCount.toString()
            holder.repostCountTV.text = currentStatus.retweetCount.toString()
        }else{
            if(holder is ProgressViewHolder){
                if(hasNextPage) {
                    holder.progressBar.visibility = View.VISIBLE
                    holder.endText.verticalScrollbarPosition = View.GONE
                }
                else{
                    holder.progressBar.visibility = View.GONE
                    holder.endText.visibility = View.VISIBLE
                }
            }
        }



    }

    fun setData(
        data: List<Status>,
        isToBeUpdated: Boolean,
        isSilentUpdate:Boolean
    ){
        if(isToBeUpdated) {
            mUnSortedData.addAll(data)
            mData.addAll(data)
        }else{
            mUnSortedData = mutableListOf()
            mUnSortedData.addAll(data)
            mData = mutableListOf()
            mData.addAll(data)
        }
        if(!isSilentUpdate)
            notifyDataSetChanged()
    }

    fun setQuery(query:String){
        mQuery = query
    }



    fun updateWithSortedData(sortedData: MutableList<Status>,isToBeUpdated: Boolean){
        if(isToBeUpdated){
            mData.addAll(sortedData)
        }
        else{
            mData = mutableListOf()
            mData.addAll(sortedData)
        }
        notifyDataSetChanged()

    }

    fun restoreWithUnsortedData() {
        mData = mutableListOf()
        mData.addAll(mUnSortedData)
        notifyDataSetChanged()
    }



    fun hideProgressBar(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var userImageIV = itemView.findViewById(R.id.user_image_iv) as ImageView
        var verifiedIV = itemView.findViewById(R.id.verified_iv) as ImageView
        var userHandleTV = itemView.findViewById(R.id.user_handle_tv) as TextView
        var userNameTV = itemView.findViewById(R.id.user_name_tv) as TextView
        var postDateTV = itemView.findViewById(R.id.post_date_tv) as TextView
        var postContentTV = itemView.findViewById(R.id.tweet_content_tv) as TextView
        var favCountTV = itemView.findViewById(R.id.fav_count_tv) as TextView
        var repostCountTV = itemView.findViewById(R.id.repost_count_tv) as TextView

    }
    inner class ProgressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var progressBar = itemView.findViewById(R.id.progressBar) as ProgressBar
        var endText = itemView.findViewById(R.id.endText) as TextView


    }


}