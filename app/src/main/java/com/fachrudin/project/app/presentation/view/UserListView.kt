package com.fachrudin.project.app.presentation.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.fachrudin.project.app.presentation.view.adapter.UserListItemAdapter
import com.fachrudin.project.core.view.LifecycleView

/**
 * @author achmad.fachrudin
 * @date 07-Mar-2019
 */
interface UserListView : LifecycleView {
    var listAdapter: UserListItemAdapter
    var layoutManager: LinearLayoutManager
}