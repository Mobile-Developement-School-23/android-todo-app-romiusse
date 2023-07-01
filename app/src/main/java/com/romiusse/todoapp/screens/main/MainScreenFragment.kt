package com.romiusse.todoapp.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.romiusse.todoapp.R
import com.romiusse.todoapp.adaptor.Adapter
import com.romiusse.todoapp.adaptor.BottomAdapter
import com.romiusse.todoapp.databinding.FragmentMainScreenBinding
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.utils.factory
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken


class MainScreenFragment : Fragment() {

    companion object {
        fun newInstance() = MainScreenFragment()
    }

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var adapter: Adapter
    private lateinit var bottomAdapter: BottomAdapter


    private val viewModel: MainScreenViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val arguments = arguments

        viewModel.init(arguments?.getString("token"))

        binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearInfo()

        initAdapter()
        initRecyclerView()
        initAddButton()
        initInternetListener()
        initBottomSheetDialog()
        initPullToRefresh()

        viewModel.items.observe(viewLifecycleOwner) {
            adapter.items = it.sortedBy { it.createdAt }
            countCompletedTasks(it)
        }

        viewModel.bottomItems.observe(viewLifecycleOwner) { it ->
            bottomAdapter.items = it.sortedBy { it.createdAt }
        }

        viewModel.info.observe(viewLifecycleOwner) {
            val text = when(it){
                DATA_WAS_UPDATED -> context?.getString(R.string.update_succses)
                WRONG_AUTH -> context?.getString(R.string.auth_error)
                CONNECTION_TIME_OUT -> context?.getString(R.string.connection_time_out)
                SERVER_ERROR -> context?.getString(R.string.server_error)
                CONNECTION_LOST -> context?.getString(R.string.connection_lost)
                else -> it
            }
            if(text.toString() != "")
                Snackbar.make(view, text.toString(), Snackbar.LENGTH_LONG).show()
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


    }


    private fun initPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
            viewModel.refresh()
        }
    }

    private fun initInternetListener(){
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager = getSystemService(requireContext(),ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, viewModel.networkCallback)
    }

    private fun initBottomSheetDialog(){
        bottomAdapter = BottomAdapter()


        val bottomSheetDialog = BottomSheetDialog(requireContext())


        bottomSheetDialog.setContentView(R.layout.main_botto_sheet_dialog)
        bottomSheetDialog.findViewById<TextView>(R.id.set)?.setOnClickListener {
            viewModel.setActualData()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.findViewById<TextView>(R.id.get)?.setOnClickListener {
            viewModel.getActualData()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnCancelListener {
            viewModel.bottomSheetClosed()
        }
        val recyclerView = bottomSheetDialog.findViewById<RecyclerView>(R.id.recyclerview1)!!
        recyclerView.layoutManager = GridLayoutManager(activity, 1)
        recyclerView.adapter = bottomAdapter

        viewModel.isBottomSheetShow.observe(viewLifecycleOwner) {
            if(it) bottomSheetDialog.show()
        }

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