package com.example.thalestestandroidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.databinding.FragmentProductDetailBinding
import com.example.thalestestandroidapp.presentation.product_list.ProductListFragment

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.text.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }
}