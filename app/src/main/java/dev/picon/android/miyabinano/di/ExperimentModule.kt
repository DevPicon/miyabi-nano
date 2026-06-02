package dev.picon.android.miyabinano.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.picon.android.miyabinano.data.AndroidExperimentContextProvider
import dev.picon.android.miyabinano.domain.model.ExperimentContextProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExperimentModule {
    @Binds
    @Singleton
    abstract fun bindExperimentContextProvider(
        provider: AndroidExperimentContextProvider
    ): ExperimentContextProvider
}
