package com.romiusse.todo_list.screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.romiusse.todo_list.R
import com.romiusse.todo_list.adaptors.BottomAdapter
import com.romiusse.todo_list.adaptors.Adapter
import com.romiusse.todo_list.databinding.FragmentMainScreenBinding
import com.romiusse.todo_list.di.ListComponentViewModel
import com.romiusse.todo_list.screen.snack_bar_msg.MessageStatus
import com.romiusse.todo_repository.TodoItem
import dagger.Lazy
import javax.inject.Inject

/**
 *
 * Main screen fragment
 *
 * @author Romiusse
 */
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var bottomAdapter: BottomAdapter
    private lateinit var adapter: Adapter

    @Inject
    internal lateinit var mainScreenViewModelFactory: Lazy<MainScreenViewModel.Factory>

    private val viewModel: MainScreenViewModel by viewModels {
        mainScreenViewModelFactory.get()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ViewModelProvider(this).get<ListComponentViewModel>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val arguments = arguments
        viewModel.initToken(arguments?.getString("token"))

        binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAll()
        startListener()
    }

    private fun initializeAll(){
        initAdapter()
        initRecyclerView()
        initAddButton()
        initInternetListener()
        initBottomSheetDialog()
        initPullToRefresh()
        initSnackBar()
    }

    private fun startListener(){
        listenSnackBarMessages()
        listenSyncIconStatus()
        listenItemsUpdates()
        listenBottomItemsUpdates()
    }

    private fun listenItemsUpdates(){
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.items = it.sortedBy { it.createdAt }
            countCompletedTasks(it)
        }
    }

    private fun listenBottomItemsUpdates(){
        viewModel.bottomItems.observe(viewLifecycleOwner) { items ->
            bottomAdapter.items = items.sortedBy { it.createdAt }
        }
    }

    private fun listenSyncIconStatus(){
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

    private fun listenSnackBarMessages(){
        viewModel.info.observe(viewLifecycleOwner) {

            it?.let {
                val text = when(it.status){
                    MessageStatus.DATA_WAS_UPDATED -> context?.getString(R.string.update_succses)
                    MessageStatus.WRONG_AUTH -> context?.getString(R.string.auth_error)
                    MessageStatus.CONNECTION_TIME_OUT -> context?.getString(R.string.connection_time_out)
                    MessageStatus.SERVER_ERROR -> context?.getString(R.string.server_error)
                    MessageStatus.CONNECTION_LOST -> context?.getString(R.string.connection_lost)
                    MessageStatus.RETRYING -> context?.getString(R.string.retrying)
                }
                val prefix = it.prefix ?: ""
                val suffix = it.suffix ?: ""
                Snackbar.make(binding.root, "$prefix $text $suffix", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun initSnackBar(){
        viewModel.clearInfo()
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

        val connectivityManager = getSystemService(requireContext(),
            ConnectivityManager::class.java) as ConnectivityManager
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
        val recyclerView =
            bottomSheetDialog.findViewById<RecyclerView>(R.id.recyclerview1)!!
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