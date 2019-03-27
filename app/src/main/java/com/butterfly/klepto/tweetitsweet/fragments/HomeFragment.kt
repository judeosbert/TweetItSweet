package com.butterfly.klepto.tweetitsweet.fragments

import android.app.Activity
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
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.butterfly.klepto.tweetitsweet.NoInternetActivity
import com.butterfly.klepto.tweetitsweet.R
import com.butterfly.klepto.tweetitsweet.adapter.PopularTagsRecyclerAdapter
import com.butterfly.klepto.tweetitsweet.utils.CommonUtils
import com.butterfly.klepto.tweetitsweet.viewmodel.PopularTagsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import twitter4j.Trend


class HomeFragment : Fragment(), PopularTagsRecyclerAdapter.PopularTagsInterface {



    private var listener: OnFragmentInteractionListener? = null
    private val viewModel by viewModel<PopularTagsViewModel>()
    private lateinit var mPopularTagsRV: RecyclerView
    private lateinit var mSearchET: EditText
    private lateinit var mPopularTagsAdapter:PopularTagsRecyclerAdapter
    private var mEditTextWatcher = EditTextWatcher()
    private lateinit var mCircularProgress:ProgressBar

    private fun showProgress() {
        mCircularProgress.visibility = VISIBLE
        mPopularTagsRV.visibility = GONE
    }
    private fun hideProgress() {
        mCircularProgress.visibility = GONE
        mPopularTagsRV.visibility = VISIBLE
    }



    private fun updateRecyclerViewData(result: List<Trend>?) {

        mPopularTagsAdapter.setData(result)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupRecyclerView(view)
        showProgress()
        startObserve()


    }

    private fun startObserve() {
        viewModel.trendResultLiveData.observe(this,
            Observer<List<Trend>> { result ->
                hideProgress()
                updateRecyclerViewData(result)
            })
        if(CommonUtils.isInternetConnected(context!!))
            viewModel.fetchPopularTags()
        else
            startActivityForResult(Intent(activity,NoInternetActivity::class.java),1000)

    }

    private fun initializeViews(view: View) {
        mPopularTagsRV = view.findViewById(R.id.popular_topics_rv)
        mSearchET = view.findViewById(R.id.search_et)
        mCircularProgress = view.findViewById(R.id.progress_circular)

    }

    override fun onStart() {
        super.onStart()
        mSearchET.addTextChangedListener(mEditTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        mSearchET.removeTextChangedListener(mEditTextWatcher)
        mSearchET.text = Editable.Factory.getInstance().newEditable("")
    }

    override fun onPopularTagsClicked(query: String) {
        listener!!.showQueryResults(query,mSearchET)
        var view = ImageView(activity)
    }



    private fun setupRecyclerView(view:View) {
        mPopularTagsAdapter  = PopularTagsRecyclerAdapter(this)
        mPopularTagsRV.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        mPopularTagsRV.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        mPopularTagsRV.adapter = mPopularTagsAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    interface OnFragmentInteractionListener {
        fun showQueryResults(toString: String,editText: EditText)
        fun showNoInternet()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()

    }

    inner class EditTextWatcher:TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            listener!!.showQueryResults(s.toString(),mSearchET)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           return
        }

    }
}
