package com.example.greenbytebazaar.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.greenbytebazaar.util.Constants.Introduction_SP
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth()=FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestoreDatabase()= Firebase.firestore

    @Provides
    fun provideIntroductionSP(
        application: Application
    )=application.getSharedPreferences(Introduction_SP , MODE_PRIVATE)
}



