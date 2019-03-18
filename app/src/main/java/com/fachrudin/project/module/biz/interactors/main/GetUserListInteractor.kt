package com.fachrudin.project.module.biz.interactors.main

import com.fachrudin.project.module.biz.contract.experimental.Interactor
import com.fachrudin.project.module.biz.entities.main.UserList
import com.fachrudin.project.module.biz.repositories.MainRepository

/**
 * @author achmad.fachrudin
 * @date 21-Nov-18
 */
class GetUserListInteractor(val repository: MainRepository) :
    Interactor<GetUserListInteractor.Params, UserList>() {

    override fun execute(params: Params?): UserList {
        val query = params?.query
            ?: throw IllegalArgumentException("missing params: ${Params::page}")
        val page = params?.page
        val pageSize = params?.pageSize
        return repository.userList(query, page, pageSize)
    }

    data class Params(val query: String, val page: Int, val pageSize: Int)
}