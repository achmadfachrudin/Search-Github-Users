package com.fachrudin.project.app.presentation.viewmodel

import androidx.databinding.ObservableField
import com.fachrudin.project.core.base.BaseViewModel
import javax.inject.Inject

/**
 * @author achmad.fachrudin
 * @date 07-Mar-2019
 */
class UserListItemViewModel @Inject constructor() : BaseViewModel() {
    var bTextName = ObservableField<String>()
}