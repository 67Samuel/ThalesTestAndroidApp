package com.example.thalestestandroidapp.presentation.product_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductListBinding

class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private val binding by viewBinding(FragmentProductListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.text.setOnClickListener {
//            Navigation.findNavController(binding.root)
//                .navigate(
//                    ProductListFragmentDirections
//                        .actionProductListFragmentToProductDetailFragment()
//                )
//        }
    }
}

