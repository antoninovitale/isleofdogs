package com.antoninovitale.dogs.breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antoninovitale.dogs.breeds.databinding.BreedsFragmentBinding
import com.antoninovitale.dogs.breeds.details.contract.BreedDetailsArguments
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BreedsFragment : Fragment() {

    private val vm: BreedsViewModel by viewModels()
    private var binding: BreedsFragmentBinding? = null

    @Inject
    lateinit var adapter: BreedsItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        BreedsFragmentBinding.inflate(inflater, container, false)
            .let {
                binding = it
                it.root
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup views
        adapter.setItemClickListener(vm::onItemClicked)
        binding!!.breeds.adapter = adapter
        binding!!.swipeToRefresh.setOnRefreshListener { vm.loadData() }

        viewLifecycleOwner.lifecycleScope.launch {
            // Observe items
            vm.itemsState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    when (state) {
                        is BreedsViewModel.ItemsState.ItemsReady -> {
                            binding!!.swipeToRefresh.isRefreshing = false
                            binding!!.breeds.isVisible = true
                            binding!!.errorState.isVisible = false
                            adapter.submitList(state.items)
                        }
                        is BreedsViewModel.ItemsState.ItemsFailure -> {
                            binding!!.swipeToRefresh.isRefreshing = false
                            binding!!.breeds.isVisible = false
                            binding!!.errorState.isVisible = true
                            // Here we can display some text on the screen.
                            // Using snackbar is possible but the event will be collected again
                            // on screen rotation or app collapse/expand action.
                        }
                        is BreedsViewModel.ItemsState.Loading -> {
                            binding!!.swipeToRefresh.isRefreshing = true
                            binding!!.breeds.isVisible = false
                            binding!!.errorState.isVisible = false
                        }
                    }
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            // Observe click/submit/single event result
            vm.itemClickResult
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { item ->
                    // This event will be fired only once!
                    // If we rotate the screen or collapse/expand the app - no event collected.
                    findNavController().navigate(
                        R.id.action_breeds_to_breed,
                        bundleOf(
                            BreedDetailsArguments.BREED_NAME to item.name,
                            BreedDetailsArguments.BREED_PARENT_NAME to item.parent
                        )
                    )
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
