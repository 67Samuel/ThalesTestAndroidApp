package com.example.thalestestandroidapp.presentation.product_detail

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductDetailBinding
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.domain.models.Type
import com.example.thalestestandroidapp.presentation.utils.ifLet
import com.example.thalestestandroidapp.presentation.utils.toType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)
    private val viewModel: ProductDetailViewModel by activityViewModels()

    private val args by navArgs<ProductDetailFragmentArgs>()

    private var nameChanged = false
    private var imageChanged = false
    private var typeChanged = false
    private var descChanged = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(ProductDetailAction.OnProductReceived(args.product))

        subscribeObservers()

        binding.apply {
            detailImage.apply {
                load(args.product.imageUrl) {
                    crossfade(true)
                    error(R.drawable.thales_image_placeholder)
                }
                setOnClickListener {
                    // Let user choose a photo
                    // Send image as bitmap??
                }
            }
            detailName.editText?.let {
                it.setText(args.product.name)
                it.doAfterTextChanged { editable ->
                    nameChanged = args.product.name != editable.toString()
                    handleButtonEnableSetting()
                }
            }
            detailType.editText?.let {
                it.setText(args.product.type.name)
                it.doAfterTextChanged { editable ->
                    typeChanged = args.product.type.name != editable.toString()
                    handleButtonEnableSetting()
                }
            }
            detailDescription.editText?.let {
                it.setText(args.product.description)
                it.doAfterTextChanged { editable ->
                    descChanged = args.product.description != editable.toString()
                    handleButtonEnableSetting()
                }
            }
            confirmButton.setOnClickListener {
                viewModel.onAction(ProductDetailAction.OnConfirmProductUpdate(
                    name = detailNameEditText.text.toString(),
                    image = null, // TODO: Pass image data
                    description = detailDescriptionEditText.text.toString(),
                    type = detailTypeDropdownLayout.text.toString().toType()
                ))
            }
        }
    }

    private fun handleButtonEnableSetting() {
        binding.confirmButton.isEnabled = nameChanged || imageChanged || descChanged || typeChanged
    }

    override fun onResume() {
        super.onResume()

        // Set the adapter in onResume (https://github.com/material-components/material-components-android/issues/2012#issuecomment-808853621)
        val productTypes = resources.getStringArray(R.array.product_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, productTypes)
        binding.detailTypeDropdownLayout.setAdapter(arrayAdapter)
    }

    private fun subscribeObservers() {
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

}