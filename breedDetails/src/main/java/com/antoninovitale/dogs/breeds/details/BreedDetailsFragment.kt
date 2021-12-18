package com.antoninovitale.dogs.breeds.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.antoninovitale.dogs.breeds.details.BreedDetailsViewModel.ItemsState.*
import com.antoninovitale.dogs.breeds.details.contract.BreedDetailsArguments
import com.antoninovitale.dogs.breeds.details.databinding.BreedDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BreedDetailsFragment : Fragment() {

    @Inject
    lateinit var adapter: BreedImagesAdapter

    private val vm: BreedDetailsViewModel by viewModels()
    private var binding: BreedDetailsFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        BreedDetailsFragmentBinding.inflate(inflater, container, false)
            .let {
                binding = it
                it.root
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breed = requireNotNull(arguments?.getString(BreedDetailsArguments.BREED_NAME))
        val parent = arguments?.getString(BreedDetailsArguments.BREED_PARENT_NAME)
        binding!!.breedName.text = breed
        binding!!.breedImages.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding!!.breedImages.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            vm.itemsState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { state ->
                    when (state) {
                        is ItemsReady -> {
                            binding!!.breedImages.isVisible = true
                            binding!!.errorState.isVisible = false
                            adapter.submitList(state.items)
                        }
                        is ItemsFailure -> {
                            binding!!.breedImages.isVisible = false
                            binding!!.errorState.isVisible = true
                        }
                        is Loading -> {
                            binding!!.breedImages.isVisible = false
                            binding!!.errorState.isVisible = false
                        }
                    }
                }
        }

        vm.loadData(breed, parent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
