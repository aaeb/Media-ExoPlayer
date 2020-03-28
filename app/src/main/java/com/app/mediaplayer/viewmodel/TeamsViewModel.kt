package com.app.mediaplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.mediaplayer.data.PlayerModel
import com.app.mediaplayer.data.ServiceGenerator
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TeamsViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)
    val teamsLiveData = MutableLiveData<PlayerModel>()

    fun fetchAllTeamsData() {
        scope.async {
            val teams = ServiceGenerator.create().fetchTeamsData().await()
            teamsLiveData.postValue(teams)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}