package fr.cashmanager.beans

import com.google.gson.annotations.SerializedName

class ServerSettings(
    @field:SerializedName("login")
    var login: String?,
    @field:SerializedName("password")
    var password: String?
)