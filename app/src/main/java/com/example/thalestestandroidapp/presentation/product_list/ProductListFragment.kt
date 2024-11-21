package com.example.thalestestandroidapp.presentation.product_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductListBinding
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.presentation.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager
import timber.log.Timber

class ProductListFragment : Fragment(R.layout.fragment_product_list),
    ProductRecyclerViewAdapter.Interaction {

    private val binding by viewBinding(FragmentProductListBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var recyclerViewAdapter: ProductRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        recyclerViewAdapter.submitList(
            listOf(
                Product(
                    name = "Polly Day",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=mus",
                    description = "porta"
                ),
                Product(
                    name = "Chadwick Bush",
                    type = Type.FAMILY,
                    imageUrl = "https://duckduckgo.com/?q=lorem",
                    description = "consetetur"
                ),
                Product(
                    name = "Alexis Harrington",
                    type = Type.COMMERCIAL,
                    imageUrl = "http://www.bing.com/search?q=error",
                    description = "fames"
                ),
                Product(
                    name = "Polly Day",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=mus",
                    description = "porta"
                ),
                Product(
                    name = "Chadwick Bush",
                    type = Type.FAMILY,
                    imageUrl = "https://duckduckgo.com/?q=lorem",
                    description = "consetetur"
                ),
                Product(
                    name = "Alexis Harrington",
                    type = Type.COMMERCIAL,
                    imageUrl = "http://www.bing.com/search?q=error",
                    description = "fames"
                ),
                Product(
                    name = "Polly Day",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=mus",
                    description = "porta"
                ),
                Product(
                    name = "Chadwick Bush",
                    type = Type.FAMILY,
                    imageUrl = "https://duckduckgo.com/?q=lorem",
                    description = "consetetur"
                ),
                Product(
                    name = "Alexis Harrington",
                    type = Type.COMMERCIAL,
                    imageUrl = "http://www.bing.com/search?q=error",
                    description = "fames"
                ),
                Product(
                    name = "Polly Day",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=mus",
                    description = "porta"
                ),
                Product(
                    name = "Chadwick Bush",
                    type = Type.FAMILY,
                    imageUrl = "https://duckduckgo.com/?q=lorem",
                    description = "consetetur"
                ),
                Product(
                    name = "Alexis Harrington",
                    type = Type.COMMERCIAL,
                    imageUrl = "http://www.bing.com/search?q=error",
                    description = "fames"
                ),
                Product(
                    name = "Polly Day",
                    type = Type.PERSONAL,
                    imageUrl = "https://duckduckgo.com/?q=mus",
                    description = "porta"
                ),
                Product(
                    name = "Chadwick Bush",
                    type = Type.FAMILY,
                    imageUrl = "https://duckduckgo.com/?q=lorem",
                    description = "consetetur"
                ),
                Product(
                    name = "Alexis Harrington",
                    type = Type.COMMERCIAL,
                    imageUrl = "http://www.bing.com/search?q=error",
                    description = "fames"
                ),
            )
        )
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = ProductRecyclerViewAdapter(this@ProductListFragment)
        val flexLayoutManager = FlexboxLayoutManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = flexLayoutManager
            adapter = recyclerViewAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Product) {
        Timber.d("clicked $position")
    }
}

