package com.toyibnurseha.androidwebserver

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseModel (
    var score : Int? = null,
    var win_status : Int? = null,
    var user_id : Int? = null
) : Parcelable