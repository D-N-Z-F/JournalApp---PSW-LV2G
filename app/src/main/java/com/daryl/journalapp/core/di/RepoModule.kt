package com.daryl.journalapp.core.di

import com.daryl.journalapp.core.services.AuthService
import com.daryl.journalapp.data.repositories.JournalRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun provideJournalRepo(authService: AuthService): JournalRepo = JournalRepo(authService)
}