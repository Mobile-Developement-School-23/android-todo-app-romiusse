package com.romiusse.todoapp.screens.main

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.romiusse.todoapp.R
import com.romiusse.todoapp.adaptor.Adapter
import com.romiusse.todoapp.databinding.FragmentMainScreenBinding
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.utils.factory


class MainScreenFragment : Fragment() {

    companion object {
        fun newInstance() = MainScreenFragment()
    }

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var adapter: Adapter

    private val viewModel: MainScreenViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initRecyclerView()
        initAddButton()

        viewModel.items.observe(viewLifecycleOwner) {
            adapter.items = it.sortedBy { it.createdAt }
            countCompletedTasks(it)
        }

        viewModel.info.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.syncIconStatus.observe(viewLifecycleOwner){
            binding.sync.setImageResource(
                when(it){
                    SyncIconStatus.OK -> R.drawable.ic_cloud_ok
                    SyncIconStatus.ERROR -> R.drawable.ic_cloud_error
                    SyncIconStatus.SYNCHRONIZING -> R.drawable.ic_cloud_sync
                }
            )
        }


        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager = getSystemService(requireContext(),ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, viewModel.networkCallback)


    }


    @SuppressLint("SetTextI18n")
    private fun countCompletedTasks(list: List<TodoItem>){
        binding.completeTitle.text =  (context?.getString(R.string.completed) ?: "") +
                " " + list.count { it.flag }.toString()
    }

    private fun initAddButton() {
        binding.addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_addScreenFragment
            )
        }
    }

    private fun initAdapter() {
        adapter = Adapter()
        adapter.setCheckBoxListener { viewModel.updateFromList(it) }
        adapter.setItemTouchListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_addScreenFragment,
                bundle
            )
        }
    }

    private fun initRecyclerView() {
        binding.apply {

            recyclerview.layoutManager = GridLayoutManager(activity, 1)
            recyclerview.adapter = adapter
        }
    }


}