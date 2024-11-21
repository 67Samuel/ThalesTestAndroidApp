package com.example.thalestestandroidapp.presentation.product_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductListBinding
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.presentation.utils.toMessage
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductListFragment : Fragment(R.layout.fragment_product_list),
    ProductRecyclerViewAdapter.Interaction {

    private val binding by viewBinding(FragmentProductListBinding::bind)
    private val viewModel: ProductListViewModel by activityViewModels()

    private lateinit var recyclerViewAdapter: ProductRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = ProductRecyclerViewAdapter(this@ProductListFragment)
        val flexLayoutManager = FlexboxLayoutManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = flexLayoutManager
            adapter = recyclerViewAdapter
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productList.collect { list ->
                    recyclerViewAdapter.submitList(list)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productListEvents.collect { event ->
                    when (event) {
                        is ProductListEvents.Error -> {
                            Timber.e(event.error.toMessage(requireContext()))
                        }
                        is ProductListEvents.NavigateToProductDetails -> {
                            Navigation.findNavController(binding.root).navigate(
                                ProductListFragmentDirections
                                    .actionProductListFragmentToProductDetailFragment()
                            )
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }

    override fun onItemSelected(position: Int, item: Product) {
        viewModel.onAction(ProductListAction.OnProductClick(item.id))
    }
}

