package com.app.mediaplayer.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayerModel(
    @SerializedName("Lineups")
    val lineups: Lineups
) {
    class Lineups {
        @SerializedName("Success")
        @Expose
        var success: Boolean? = null
        @SerializedName("Data")
        @Expose
        var data: Data? = null
    }

    class Data {
        @SerializedName("HomeTeam")
        @Expose
        var homeTeam: Team? = null
        @SerializedName("AwayTeam")
        @Expose
        var awayTeam: Team? = null
    }

    class Team {
        @SerializedName("Players")
        @Expose
        var players: List<Player>? = null

    }

    class Player {
        @SerializedName("Order")
        @Expose
        var order: Int? = null
        @SerializedName("StartInField")
        @Expose
        var startInField: Boolean? = null
        @SerializedName("Role")
        @Expose
        var role: String? = null
        @SerializedName("IsCaptain")
        @Expose
        var isCaptain: Boolean? = null
        @SerializedName("JerseyNumber")
        @Expose
        var jerseyNumber: String? = null
        @SerializedName("Id")
        @Expose
        var id: Int? = null
        @SerializedName("Name")
        @Expose
        var name: String? = null
        @SerializedName("XCoordinate")
        @Expose
        var xCoordinate: Int? = null
        @SerializedName("YCoordinate")
        @Expose
        var yCoordinate: Int? = null

    }
}