package com.example.tfimageclassifier.di

import android.content.Context
import com.example.tfimageclassifier.data.repository.ImageClassifierRepositoryImpl
import com.example.tfimageclassifier.domain.repository.ImageClassifierRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the concrete [ImageClassifierRepositoryImpl] to the domain
     * [ImageClassifierRepository] interface.  The ViewModel and use-case never
     * see the implementation class — only the interface.
     */
    @Binds
    @Singleton
    abstract fun bindImageClassifierRepository(
        impl: ImageClassifierRepositoryImpl
    ): ImageClassifierRepository
}
