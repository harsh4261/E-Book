package com.example.e_book.di

import com.example.e_book.data_Layer.network.repo_Impl.AllBookRepoImpl
import com.example.e_book.domain_Layer.repo.AllBookRepo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModel {

    @Provides
    @Singleton
    fun provideFirebaseRealTimeDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun  provideRepo(firebaseDatabase: FirebaseDatabase): AllBookRepo {
        return AllBookRepoImpl(firebaseDatabase)
    }

}