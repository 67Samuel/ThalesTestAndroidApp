package com.example.thalestestandroidapp.presentation.product_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.text.setOnClickListener {
//            Navigation.findNavController(binding.root).popBackStack()
//        }
    }
}