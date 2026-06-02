package dev.picon.android.miyabinano.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.picon.android.miyabinano.data.genai.MlKitCapabilityPreparationClientFactory
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClientFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenAiModule {
    @Provides
    @Singleton
    fun provideCapabilityPreparationClientFactory(
        @ApplicationContext context: Context
    ): CapabilityPreparationClientFactory =
        MlKitCapabilityPreparationClientFactory(context)
}
