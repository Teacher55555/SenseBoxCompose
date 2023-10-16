package com.example.sensebox.data.di

import android.content.Context
import androidx.room.Room
import com.example.sensebox.data.database.BoxDao
import com.example.sensebox.data.database.BoxDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val DB_NAME = "favBoxes_db"

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun provideFavBoxDataBase(@ApplicationContext appContext: Context) : BoxDataBase {
        return Room.databaseBuilder(
            context = appContext,
            klass = BoxDataBase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideFavBoxDao(dataBase: BoxDataBase) : BoxDao {
        return dataBase.boxDao()
    }
}