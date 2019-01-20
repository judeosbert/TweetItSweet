package com.butterfly.klepto.tweetitsweet.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.butterfly.klepto.tweetitsweet.NoInternetActivity
import com.butterfly.klepto.tweetitsweet.R
import com.butterfly.klepto.tweetitsweet.adapter.SearchResultRecyclerAdapter
import com.butterfly.klepto.tweetitsweet.constants.Constants
import com.butterfly.klepto.tweetitsweet.constants.Constants.Companion.QUERY
import com.butterfly.klepto.tweetitsweet.extensions.getRank
import com.butterfly.klepto.tweetitsweet.utils.CommonUtils
import com.butterfly.klepto.tweetitsweet.viewmodel.TweetsViewModel
import kotlinx.android.synthetic.main.search_result_fragment.*
import twitter4j.Status
import java.net.URLDecoder
import java.util.*


class SearchResultFragment : Fragment(), View.OnClickListener {

    private lateinit var mResultsRV: RecyclerView
    private lateinit var mSearchET:EditText
    private lateinit var mViewModel: TweetsViewModel
    private lateinit var mResultCountTV:TextView
    private lateinit var mSearchResultAdapter:SearchResultRecyclerAdapter
    private var mSearchTimer:Timer? = null
    private lateinit var mSearchProgress:ProgressBar
    private lateinit var mSortButton: ToggleButton
    private lateinit var mDividerView:View
    private var hasNextPage:Boolean = false

    private var mIsDataBeingLoaded = false

    companion object {
        fun newInstance() = SearchResultFragment()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.slide_left)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.butterfly.klepto.tweetitsweet.R.layout.search_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(TweetsViewModel::class.java)

        mViewModel.getData().observe(this, Observer {result->
            updateRecyclerView(result)
            onDataUpdated()

        })

        mViewModel.getIsHaveNext().observe(this, Observer<Boolean> {
            hasNextPage = it!!
            updateRecyclerViewProgressBar(hasNextPage)
        })
    }

    private fun updateRecyclerViewProgressBar(hasNextPage: Boolean) {
        mSearchResultAdapter.hideProgressBar(hasNextPage)

    }

    fun onDataUpdated() {
        val resultSize = mSearchResultAdapter.itemCount-1
        if(resultSize == 0)
            hideResultUI()
        else {
            showResultUI()
            }
        mResultCountTV.text = context!!.getString(R.string.resultsFoundPlaceholder, resultSize.toString())

    }



    private fun updateRecyclerView(result: List<Status>?) {
        mSearchProgress.visibility = GONE
        if(mSortButton.isChecked) {
            mSearchResultAdapter.setData(result!!,mIsDataBeingLoaded,true)
            sortData()
            Toast.makeText(context,context!!.getString(R.string.toast_message),Toast.LENGTH_SHORT).show()
        }else{
            mSearchResultAdapter.setData(result!!,mIsDataBeingLoaded,false)
        }
        mSearchResultAdapter.setQuery(mSearchET.text.toString())
        mIsDataBeingLoaded = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupRecyclerView()
        setupListenersForSearch()
        hideResultUI()
        checkConnectivity()


    }

    private fun checkConnectivity() {
        if(!CommonUtils.isInternetConnected(context!!)){
            startActivityForResult(Intent(context!!, NoInternetActivity::class.java),1000)
        }
    }

    private fun initializeViews(view:View) {
        mResultsRV = view.findViewById(com.butterfly.klepto.tweetitsweet.R.id.search_result_rv)
        mSearchET = view.findViewById(com.butterfly.klepto.tweetitsweet.R.id.search_et)
        mResultCountTV = view.findViewById(R.id.result_count_tv)
        mSearchProgress  = view.findViewById(R.id.search_progress)
        mSortButton = view.findViewById(R.id.sort_button)
        mDividerView = view.findViewById(R.id.divider_view)
        mSortButton.setOnClickListener(this)

    }

    private fun setupListenersForSearch() {
        mSearchET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(mSearchTimer != null){
                    mSearchTimer!!.cancel()
                    mSearchProgress.visibility = GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    mSearchTimer = Timer()
                    mSearchProgress.visibility = VISIBLE
                    mSearchTimer!!.schedule(object : TimerTask() {
                        override fun run() {
                            hasNextPage  = true
                            mViewModel.fetchResults(s.toString(),false)

                        }

                    }, 600)


                }else{
                    hideResultUI()
                }
            }


        })

        mSearchET.text = Editable
            .Factory.getInstance()
            .newEditable(
                URLDecoder.decode(
                    arguments?.getString(QUERY),Constants.UTF8))
        mSearchET.requestFocus()

        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(mSearchET, InputMethodManager.SHOW_IMPLICIT)

    }
    private fun showResultUI(){
        result_count_tv.visibility = VISIBLE
        mSortButton.visibility = VISIBLE
        mDividerView.visibility = VISIBLE
        mResultsRV.visibility = VISIBLE
    }
    private fun hideResultUI(){
        result_count_tv.visibility = GONE
        mSortButton.visibility = GONE
        mDividerView.visibility = GONE
        mResultsRV.visibility = GONE
    }
    private fun setupRecyclerView() {
        mSearchResultAdapter = SearchResultRecyclerAdapter()
        mResultsRV.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        mResultsRV.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        mResultsRV.adapter = mSearchResultAdapter

        mResultsRV.addOnScrollListener(object:RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = mResultsRV.layoutManager!!.itemCount
                val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if(lastVisibleItem == totalItemCount - 1){
                    loadMoreData()
                }
            }


        })




    }

    private fun loadMoreData() {
        if(!mIsDataBeingLoaded && hasNextPage){
            mIsDataBeingLoaded = true
            mViewModel.fetchResults("",true)
        }

    }



    override fun onClick(v: View?) {
       when(v){
           mSortButton->{
               if(mSortButton.isChecked)
                    sortData()
               else
                    mSearchResultAdapter.restoreWithUnsortedData()
           }

       }
    }

    private fun sortData() {
        val dataList = mSearchResultAdapter.getUnsortedData()
        dataList.sortByDescending {
            it.getRank()
        }
        mSearchResultAdapter.updateWithSortedData(dataList,false)
    }


}
