package com.fachrudin.project.app.presentation.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fachrudin.project.core.base.BaseViewModel
import com.fachrudin.project.core.common.AndroidContext.Companion.UI
import com.fachrudin.project.module.biz.entities.main.UserList
import com.fachrudin.project.module.biz.interactors.main.GetUserListInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author achmad.fachrudin
 * @date 07-Mar-2019
 */
class UserListViewModel @Inject constructor(private val getUserListInteractor: GetUserListInteractor) : BaseViewModel() {
    var bTextQuery = ObservableField<String>("tom")
    var bPageNumber = ObservableField(1)
    var bPageTotal = ObservableField<Int>()
    var bPageLast = ObservableField<Boolean>(false)
    var bPageSize = ObservableField(20)
    var bTextError = ObservableField<String>()

    var bLoadMore = ObservableField<Boolean>(false)
    val bShowErrorView = ObservableField<Boolean>(false)
    val bShowLoadingView = ObservableField<Boolean>(true)

    private var error: MutableLiveData<Exception>? = null
    private var userList: MutableLiveData<UserList>? = null

    fun getError(): LiveData<Exception> {
        if (error == null)
            error = MutableLiveData()
        return error as LiveData<Exception>
    }

    fun getUserList(): LiveData<UserList> {
        if (userList == null)
            userList = MutableLiveData()
        return userList as LiveData<UserList>
    }

    fun getUserListFromApi() {
        bShowLoadingView.set(true)
        GlobalScope.launch(UI) {
            try {
                val result = getUserListInteractor.executeAsync(GetUserListInteractor.Params(
                    bTextQuery.get()!!, bPageNumber.get()!!, bPageSize.get()!!))
                userList?.value = result
                if (!result.userItem?.isEmpty()!!) {
                    bShowErrorView.set(false)
                }
                bShowLoadingView.set(false)
            } catch (e: Exception) {
                error?.value = e
                bShowLoadingView.set(false)
            }
        }
    }
}