package com.axionlabs.keyhive.di

import android.content.Context
import androidx.room.Room
import com.axionlabs.keyhive.data.PasswordDao
import com.axionlabs.keyhive.data.PasswordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providePasswordDao(passwordDatabase: PasswordDatabase): PasswordDao =
        passwordDatabase.passwordDao()

    @Provides
    @Singleton
    fun providePasswordDatabase(@ApplicationContext context: Context): PasswordDatabase =
        Room.databaseBuilder(
            context, PasswordDatabase::class.java,
            "password_db"
        ).fallbackToDestructiveMigration()
            .build()

}