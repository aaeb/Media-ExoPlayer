package com.app.mediaplayer.data

import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface ApiService {

    @GET("sample.json")
    fun fetchTeamsData(): Deferred<PlayerModel>
}