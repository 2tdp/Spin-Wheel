package com.spin.wheel.chooser.wheeloffortune.di

import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePref
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePrefImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharePrefModule {

    @Provides
    @Singleton
    fun provideSharePref(sharePrefImpl: MySharePrefImpl) : MySharePref {
        return sharePrefImpl
    }
}