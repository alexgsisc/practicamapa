package com.example.practicamaps.maps.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicamaps.MainActivity
import com.example.practicamaps.R
import com.example.practicamaps.commons.base.BaseFragment
import com.example.practicamaps.databinding.ListRouterFragmentBinding
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.data.repository.RoutesMapsRepoImp
import com.example.practicamaps.maps.domain.usecase.RoutersMapsUseImp
import com.example.practicamaps.maps.presentation.ui.adapter.ListRouterMapsAdapter
import com.example.practicamaps.maps.presentation.viewmodel.RoutersMapsViewModel
import com.example.practicamaps.maps.presentation.viewmodel.RoutersMapsViewModelFactory
import com.google.gson.Gson


class ListRouterUser : BaseFragment<ListRouterFragmentBinding>(),
    ListRouterMapsAdapter.ListRouterMapsAdapterView {

    private val viewModelRouters: RoutersMapsViewModel by viewModels {
        RoutersMapsViewModelFactory(
            RoutersMapsUseImp(
                RoutesMapsRepoImp(
                    MainActivity.dataBase!!.routerDao()
                )
            )
        )
    }
    lateinit var adaterList: ListRouterMapsAdapter

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ListRouterFragmentBinding {
        return ListRouterFragmentBinding.inflate(inflater, container, false)
    }

    override fun init() {
        adaterList = ListRouterMapsAdapter(this)
        starRecyclerView()
        viewModelRouters.getAllRouters()
        onStarView()
    }

    private fun starRecyclerView() {
        binding.rvRouters.apply {
            layoutManager = LinearLayoutManager(this@ListRouterUser.requireContext())
            adapter = adaterList
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun onStarView() {
        viewModelRouters.responseListRouters.observe(this, Observer {
            adaterList.setRouters(it)
        })
    }

    private fun openFragment(fragment: Fragment, tag: String) {

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.root_layout, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }

    companion object {
        const val TAG = "ListRouterUser"
        fun newInstance() = ListRouterUser()
    }

    override fun setOnViewClickItem(result: RoutersModel) {
        val fragment = DetailRoutesUserFragment.newInstance()
        val bunde = Bundle()
        bunde.putString("RESULT_ONCLICK_ROUTERS", Gson().toJson(result))
        fragment.arguments = bunde
        openFragment(fragment, DetailRoutesUserFragment.TAG)
    }
}