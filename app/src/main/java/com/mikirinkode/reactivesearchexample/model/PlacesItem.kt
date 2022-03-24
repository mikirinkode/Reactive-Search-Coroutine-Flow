package com.mikirinkode.reactivesearchexample.model

import com.google.gson.annotations.SerializedName

data class PlacesItem(

    @field:SerializedName("id")
    val placeId: String,

    @field:SerializedName("place_name")
    val placeName: String
)
