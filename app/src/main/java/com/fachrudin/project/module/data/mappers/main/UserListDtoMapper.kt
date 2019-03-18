package com.fachrudin.project.module.data.mappers.main

import com.fachrudin.project.core.common.protocol.Mapper
import com.fachrudin.project.module.biz.entities.main.UserList
import com.fachrudin.project.module.data.datasource.webapi.dto.main.UserListDto

/**
 * @author achmad.fachrudin
 * @date 21-Nov-18
 */
class UserListDtoMapper : Mapper<UserListDto, UserList> {
    override fun transform(from: UserListDto): UserList {
        val result = UserList()

        from.total_count?.let { result.userTotal = it }
        from.incomplete_results?.let { result.incompleteResults = it }
        from.items?.let {
            val itemMapper = UserItemDtoMapper()
            result.userItem = itemMapper.transform(it)
        }

        return result
    }
}