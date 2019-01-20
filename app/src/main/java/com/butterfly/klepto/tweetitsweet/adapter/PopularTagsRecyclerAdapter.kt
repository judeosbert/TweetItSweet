package com.butterfly.klepto.tweetitsweet.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.butterfly.klepto.tweetitsweet.R
import com.butterfly.klepto.tweetitsweet.constants.Constants
import twitter4j.Trend
import java.net.URLDecoder

class PopularTagsRecyclerAdapter( listener: PopularTagsInterface) :
    RecyclerView.Adapter<PopularTagsRecyclerAdapter.ViewHolder>() {


    lateinit var mContext:Context
    private var mData:List<Trend> = emptyList()
    private var mListener = listener
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PopularTagsRecyclerAdapter.ViewHolder {
       mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.popular_tags_recyclerview_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: PopularTagsRecyclerAdapter.ViewHolder, position: Int) {
        val currentTrend:Trend  = mData[position]
        holder.popularTagTV.text = currentTrend.name
        holder.queryTermTV.text= URLDecoder.decode(currentTrend.url,Constants.UTF8)

        holder.popularTagTV.setOnClickListener {
            mListener.onPopularTagsClicked(currentTrend.query)
        }


    }



    fun setData(result: List<Trend>?) {
        mData = result!!
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var popularTagTV = view.findViewById(R.id.populat_tags_tv) as TextView
        var  queryTermTV= view.findViewById(R.id.query_term_tv) as TextView

    }
    
    
    interface PopularTagsInterface{
            fun onPopularTagsClicked(query: String)
    }
}