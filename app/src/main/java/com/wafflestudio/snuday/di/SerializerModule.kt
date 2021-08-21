package com.wafflestudio.snuday.di

import com.wafflestudio.snuday.moshi.MoshiSerializer
import com.wafflestudio.snuday.moshi.Serializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class SerializerModule {
    @Binds
    abstract fun bindSerializer(moshiSerializer: MoshiSerializer): Serializer
}
