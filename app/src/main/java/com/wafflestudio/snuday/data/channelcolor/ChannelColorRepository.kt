package com.wafflestudio.snuday.data.channelcolor

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ChannelColorRepository (context: Context) {

    private val channelColorDao = ChannelColorDatabase.getInstance(context).channelColorDao()
    private var colorListSubject = BehaviorSubject.create<List<ChannelColor>>()


    fun getAllChannelColors() =
        channelColorDao
            .getAllChannelColors()
            .doOnSuccess {
                colorListSubject.onNext(it)
            }
            .doOnError {
                colorListSubject.onNext(emptyList())
            }


    fun getChannelColors(): Observable<List<ChannelColor>> = colorListSubject.hide()

    fun insertChannelColors(channelColor: ChannelColor): Completable =
        channelColorDao.insertChannelColor(channelColor)

}