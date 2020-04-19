package com.sasuke.flickr.di.module.activity

import com.sasuke.flickr.di.module.libraries.PicassoModule
import com.sasuke.flickr.di.scope.PerActivityScope
import com.sasuke.flickr.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [PicassoModule::class])
abstract class ActivityBindingModule {

    @PerActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun mainActivity(): MainActivity
}