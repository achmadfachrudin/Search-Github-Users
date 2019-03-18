package com.fachrudin.project.app.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachrudin.project.R
import com.fachrudin.project.app.internal.di.DaggerMainFeatureComponent
import com.fachrudin.project.app.internal.di.MainFeatureComponent
import com.fachrudin.project.app.presentation.view.UserListView
import com.fachrudin.project.app.presentation.view.adapter.UserListItemAdapter
import com.fachrudin.project.app.presentation.viewmodel.UserListViewModel
import com.fachrudin.project.core.base.BaseFeatureActivity
import com.fachrudin.project.core.di.components.FeatureComponent
import com.fachrudin.project.core.owner.ViewDataBindingOwner
import com.fachrudin.project.core.owner.ViewModelOwner
import com.fachrudin.project.core.utils.EndlessRecyclerViewScrollListener
import com.fachrudin.project.databinding.ActivityUserListBinding
import com.fachrudin.project.module.base.di.components.MainRepositoryComponent
import javax.inject.Inject


/**
 * @author achmad.fachrudin
 * @date 07-Mar-2019
 */
class UserListActivity : BaseFeatureActivity(),
    UserListView,
    ViewModelOwner<UserListViewModel>,
    ViewDataBindingOwner<ActivityUserListBinding> {

    @Inject
    override lateinit var listAdapter: UserListItemAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override lateinit var binding: ActivityUserListBinding
    override val viewModel: UserListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel::class.java)
    }

    override fun getViewLayoutResId(): Int {
        return R.layout.activity_user_list
    }

    override fun injectComponent() {
        componentAs(MainFeatureComponent::class.java).inject(this)
    }

    override fun buildComponent(): FeatureComponent {
        return DaggerMainFeatureComponent.builder()
            .appComponent(appComponent)
            .mainRepositoryComponent(getProvidedComponent(MainRepositoryComponent::class.java))
            .build()
    }

    private fun createScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(binding.rvUser.layoutManager as LinearLayoutManager) {
            override fun onLoadPosition(lastPosition: Int, view: RecyclerView?) {
                //ignore
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (!viewModel.bPageLast.get()!! && !viewModel.bLoadMore.get()!!) {
                    viewModel.bLoadMore.set(true)
                    viewModel.bShowLoadingView.set(true)
                    viewModel.bPageNumber.set(viewModel.bPageNumber.get()?.plus(1))
                    viewModel.getUserListFromApi()
                }
            }
        }
    }

    override var layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        set(value) {}

    private var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.user_list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setUI()

        observeError()
        observeData()
        viewModel.getUserListFromApi()
    }

    private fun setUI() {
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.bTextQuery.set(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getUserListFromApi()
                return false
            }
        })

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.vertical_space_divider)!!)
        binding.rvUser.addItemDecoration(divider)
    }


    private fun observeError() {
        observeData(viewModel.getError()) { error ->
            error?.let {
                if (it.message == "Forbidden") {
                    viewModel.bTextError.set(getString(R.string.user_list_error_limit))
                } else {
                    viewModel.bTextError.set(it.message.toString())
                }
                viewModel.bShowErrorView.set(true)
            }
        }
    }

    private fun observeData() {
        observeData(viewModel.getUserList()) { user ->
            user?.userTotal?.let {
                viewModel.bPageTotal.set(countPageTotal(it))
                viewModel.bPageLast.set(viewModel.bPageNumber.get() == viewModel.bPageTotal.get())
            }

            user?.userItem?.let {
                if (viewModel.bPageNumber.get() == 1) {
                    if (it.isEmpty()) {
                        viewModel.bShowErrorView.set(true)
                        viewModel.bTextError.set(getString(R.string.user_list_error_empty))
                    }
                    listAdapter.setData(it)
                    binding.rvUser.clearOnScrollListeners()
                    binding.rvUser.layoutManager?.let { binding.rvUser.addOnScrollListener(createScrollListener()) }
                } else {
                    listAdapter.addData(it)
                }
                viewModel.bLoadMore.set(false)
            }
        }
    }

    private fun countPageTotal(itemTotal: Int): Int {
        return itemTotal / viewModel.bPageSize.get()!! + if (itemTotal % viewModel.bPageSize.get()!! == 0) 0 else 1
    }

    override fun onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.app_msg_close), Toast.LENGTH_SHORT).show()
        }
        time = System.currentTimeMillis()
    }
}