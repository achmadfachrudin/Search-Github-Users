package com.fachrudin.project.module.data.datasource.webapi.dto.main

import com.fachrudin.project.module.data.datasource.webapi.dto.BaseApiDto
import com.google.gson.annotations.SerializedName

/**
 * @author achmad.fachrudin
 * @date 21-Nov-18
 */
class UserItemDto : BaseApiDto() {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("login")
    val login: String? = null
    @SerializedName("avatar_url")
    val avatar_url: String? = null
}