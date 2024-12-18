package com.example.thalestestandroidapp.presentation.product_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductListBinding
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.SortOption
import com.example.thalestestandroidapp.presentation.utils.observerScope
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

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

    //region Init

    private fun initViews() {
        binding.apply {
            productCreationFab.setOnClickListener {
                Navigation.findNavController(root).navigate(
                    ProductListFragmentDirections
                        .actionProductListFragmentToProductDetailFragment()
                )
            }

            filterButton.setOnClickListener {
                var selectedSortOption = SortOption.DEFAULT
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.sort_products_title))
                    .setPositiveButton(getString(R.string.sort_products_confirm)) { dialogInterface, _ ->
                        viewModel.onAction(ProductListAction.SortProducts(selectedSortOption))
                        dialogInterface.dismiss()
                    }
                    .setSingleChoiceItems(
                        SortOption.entries.map { it.toString() }.toTypedArray(),
                        0,
                    ) { _, which ->
                        selectedSortOption = when (which) {
                            0 -> SortOption.DEFAULT
                            1 -> SortOption.NAME_ASC
                            2 -> SortOption.NAME_DESC
                            3 -> SortOption.PRICE_ASC
                            4 -> SortOption.PRICE_DESC
                            else -> SortOption.DEFAULT
                        }
                    }
                    .show()
            }

            searchBarEditText.addTextChangedListener { editable ->
                viewModel.onAction(ProductListAction.FilterProducts(editable?.toString() ?: ""))
            }

            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                if (!imeVisible) {
                    searchBar.clearFocus()
                }
                insets
            }
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

    //endregion

    //region Overrides

    override fun onItemSelected(position: Int, item: Product) {
        viewModel.onAction(ProductListAction.OnProductClick(item))
    }

    //endregion
}

