package com.wafflestudio.snuday.ui.main.schedule

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.ChannelFilter
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.preference.SnudayPrefObjects
import com.wafflestudio.snuday.repository.ChannelColorManager
import com.wafflestudio.snuday.repository.ChannelDataRepository
import com.wafflestudio.snuday.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val snudayPrefObjects: SnudayPrefObjects,
    private val channelColorManager: ChannelColorManager,
    private val channelDataRepository: ChannelDataRepository
): ViewModel() {

    var personalChannelId: Int? = null

    val channelFilter = snudayPrefObjects.channelFilter

    private val _subscribingList = BehaviorSubject.createDefault<List<Channel>>(listOf())
    val subscribingList = Observable.combineLatest(_subscribingList, channelColorManager.getChannelColor()) { subList, channelColorList ->
        subList.forEach { channel ->
            channel.channelColor = channelColorList?.find { channelColor -> channel.id == channelColor.channelId }
        }
        _subscribingListWithColor.onNext(subList)
        subList
    }

    private val _subscribingListWithColor = BehaviorSubject.createDefault<List<Channel>>(listOf())
    val subscribingListWithColor = _subscribingListWithColor.hide()

    val filterList = Observable.combineLatest(subscribingList, channelFilter.asObservable()) { subList, filter ->
        val returnList = mutableListOf<Channel>()
        filter.filterList.forEach { channelId ->
            subList.find { it.id == channelId }?.let { returnList.add(it) }
        }
        returnList.toList()
    }

    private val _eventList = BehaviorSubject.createDefault<List<Event>>(listOf())
    var eventListValue: List<Event> = _eventList.value
    val eventList: Observable<List<Event>> = Observable.combineLatest(
        Observable.combineLatest(_eventList, channelColorManager.getChannelColor()) { eventList, channelColorList ->
            val returnList = mutableListOf<Event>()
            eventList.forEach { event ->
                val channelColor = channelColorList?.find { channelColor -> event.channelId == channelColor.channelId }
                returnList.add(event.apply { this.channelColor = channelColor })
            }
            returnList
        }, channelFilter.asObservable()) { eventList, filter ->
        val returnList = mutableListOf<Event>()
        filter.filterList.forEach { channelId ->
            returnList.addAll( eventList.filter { it.channelId == channelId } )
        }
        returnList.toList()
    }

    fun fetchPersonalChannelId() = userDataRepository.getPersonalChannel().doOnSuccess {
        Timber.d(it.toString())
        personalChannelId = it?.id
        channelFilter.getValue().filterList
        Timber.d("id : $personalChannelId")
    }

    fun postEvent(
        title: String,
        hasTime: Boolean,
        startDateTime: LocalDateTime,
        dueDateTime: LocalDateTime,
        memo: String
    ) = personalChannelId?.let { channelDataRepository.postEvent(title, hasTime, startDateTime, dueDateTime, memo, it) }
        ?: Maybe.create<Event>(null).toSingle()

    fun updateEvent(
        title: String,
        hasTime: Boolean,
        startDateTime: LocalDateTime,
        dueDateTime: LocalDateTime,
        memo: String,
        eventId: Int
    ) = personalChannelId?.let { channelDataRepository.updateEvent(title, hasTime, startDateTime, dueDateTime, memo, it, eventId) }
        ?: Maybe.create<Event>(null).toSingle()

    fun deleteMyEvent(
        eventId: Int
    ) = personalChannelId?.let { channelDataRepository.deleteEvent(it, eventId) }
        ?: Maybe.create<Event>(null).toSingle()

    fun fetchEvent() = userDataRepository.fetchEvent().doOnSuccess {
        _eventList.onNext(it)
    }

    fun fetchSubscribingChannel() = userDataRepository.fetchSubscribingChannel(false).doOnSuccess {
        _subscribingList.onNext(it)
    }

    fun checkAllSubChannelHaveColor(): Observable<List<Channel>> =
        Observable.combineLatest(
            channelColorManager.getChannelColor(),
            subscribingList
        ) { channelColorList, subList ->
            Timber.d("channelColorList : ${channelColorList}")
            Timber.d("subList : ${subList}")

        subList.filter { channel ->
            channelColorList?.forEach { channelColor ->
                if (channelColor.channelId == channel.id) return@filter false
            }
            return@filter true
        }
    }

    fun addChannelColor(channelId: Int) = channelColorManager.addNewChannelColor(channelId)

    fun getChannelColorById(channelId: Int) = channelColorManager.getChannelColorById(channelId)

    fun getChannelColor() = channelColorManager.getChannelColor()

    fun setChannelColor(channelId: Int, colorCode: Int) = channelColorManager.setChannelColor(channelId, colorCode)

    fun addChannelToFilter(channelId: Int): Completable {
        val updateList = channelFilter.getValue().filterList.toMutableList()
        updateList.find { it == channelId } ?: run { updateList.add(channelId) }
        return channelFilter.setValue(ChannelFilter(updateList))
    }

    fun deleteChannelFromFilter(channelId: Int): Completable {
        val updateList = channelFilter.getValue().filterList.toMutableList()
        updateList.remove(channelId)
        return channelFilter.setValue(ChannelFilter(updateList))
    }

}