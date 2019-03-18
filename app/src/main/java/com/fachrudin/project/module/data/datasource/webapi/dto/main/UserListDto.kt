package com.fachrudin.project.module.data.datasource.webapi.dto.main

import com.fachrudin.project.module.data.datasource.webapi.dto.BaseApiDto
import com.google.gson.annotations.SerializedName

/**
 * @author achmad.fachrudin
 * @date 21-Nov-18
 */
class UserListDto : BaseApiDto() {
    @SerializedName("total_count")
    val total_count: Int? = 0
    @SerializedName("incomplete_results")
    val incomplete_results: Boolean? = false
    @SerializedName("items")
    val items: List<UserItemDto>? = null
}