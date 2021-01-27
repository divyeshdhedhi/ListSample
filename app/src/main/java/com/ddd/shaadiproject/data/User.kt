package com.ddd.shaadiproject.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
        @PrimaryKey
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("username")
        var userName: String? = null,

        @SerializedName("email")
        var email: String? = null,

        @SerializedName("phone")
        var phone: String? = null,

        @SerializedName("website")
        var website: String? = null,

        @SerializedName("status")
        var status: Boolean? = null
) : RealmObject()