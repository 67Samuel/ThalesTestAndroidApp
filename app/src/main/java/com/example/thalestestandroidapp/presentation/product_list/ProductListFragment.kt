package com.example.thalestestandroidapp.presentation.product_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductListBinding
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.presentation.product_detail.ProductDetailFragmentArgs
import com.example.thalestestandroidapp.presentation.utils.observerScope
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list),
    ProductRecyclerViewAdapter.Interaction {

    private val binding by viewBinding(FragmentProductListBinding::bind)
    private val viewModel: ProductListViewModel by activityViewModels()

    private val args by navArgs<ProductListFragmentArgs>()

    private lateinit var recyclerViewAdapter: ProductRecyclerViewAdapter

    //region Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("test: product=${args.updatedOrCreatedProduct}")

        subscribeObservers()
        initViews()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        args.updatedOrCreatedProduct?.let {
            viewModel.onAction(ProductListAction.RefreshProducts)
        }
    }

    //endregion

    private fun initViews() {
        binding.productCreationFab.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(
                ProductListFragmentDirections
                    .actionProductListFragmentToProductDetailFragment()
            )
        }
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
        observerScope {
            viewModel.productList.collect { list ->
                recyclerViewAdapter.submitList(list)
            }
        }

        observerScope {
            viewModel.events.collect { event ->
                when (event) {
                    is ProductListEvents.Error -> {
                        Toast.makeText(
                            requireContext(),
                            event.error.asString(requireContext()),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ProductListEvents.NavigateToProductDetails -> {
                        Navigation.findNavController(binding.root).navigate(
                            ProductListFragmentDirections
                                .actionProductListFragmentToProductDetailFragment(
                                    product = event.product
                                )
                        )
                    }
                }
            }
        }

        observerScope {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    //region Overrides

    override fun onItemSelected(position: Int, item: Product) {
        viewModel.onAction(ProductListAction.OnProductClick(item))
    }

    //endregion
}

