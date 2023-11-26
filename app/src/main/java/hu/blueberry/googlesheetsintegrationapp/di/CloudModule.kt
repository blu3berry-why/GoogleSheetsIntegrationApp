package hu.blueberry.googlesheetsintegrationapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.blueberry.googlesheetsintegrationapp.drive.DriveManager
import hu.blueberry.googlesheetsintegrationapp.drive.GoogleSheetsManager
import hu.blueberry.googlesheetsintegrationapp.drive.base.CloudBase

@Module
@InstallIn(SingletonComponent::class)
class CloudModule {

    @Provides
    fun cloudBase(context: Context) = CloudBase(context)

    //@Provides
    //fun drive(context: Context, cloudBase: CloudBase) = DriveManager(context, cloudBase)
}