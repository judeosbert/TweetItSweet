package com.butterfly.klepto.tweetitsweet.di

import com.butterfly.klepto.tweetitsweet.repository.PopularTagsRepository
import com.butterfly.klepto.tweetitsweet.repository.TweetsRepository
import com.butterfly.klepto.tweetitsweet.viewmodel.PopularTagsViewModel
import com.butterfly.klepto.tweetitsweet.viewmodel.TweetsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single <PopularTagsRepository> { PopularTagsRepository() }
    single <TweetsRepository>  { TweetsRepository() }

    viewModel { PopularTagsViewModel(get())}
    viewModel { TweetsViewModel(get()) }


}