package com.wafflestudio.snuday.preference

import android.content.SharedPreferences
import com.wafflestudio.snuday.moshi.Serializer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.lang.reflect.Type

class Preference<T>(
    private val key: String,
    private val defaultValue: T,
    private val sharedPreferences: SharedPreferences,
    private val serializer: Serializer,
    private val type: Type
) {
    private val prefSubject = BehaviorSubject.createDefault(sharedPreferences)

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
        prefSubject.onNext(sharedPreferences)
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    fun setValue(value: T): Completable = prefSubject
        .firstOrError()
        .editSharedPreferences {
            putString(key, serializer.serialize(value, type))
        }

    fun asObservable(): Observable<T> = prefSubject
        .map { it.getString(key, null)?.let { serializer.deserialize<T>(it, type) } ?: defaultValue }

    fun getValue(): T = sharedPreferences.getString(key, null)?.let { serializer.deserialize<T>(it, type) } ?: defaultValue

    private fun Single<SharedPreferences>.editSharedPreferences(batch: SharedPreferences.Editor.() -> Unit): Completable =
        flatMapCompletable {
            Completable.fromAction {
                it.edit().also(batch).apply()
            }
        }

    private fun Single<SharedPreferences>.clearSharedPreferences(batch: SharedPreferences.Editor.() -> Unit): Completable =
        flatMapCompletable {
            Completable.fromAction {
                it.edit().also(batch).apply()
            }
        }

}
