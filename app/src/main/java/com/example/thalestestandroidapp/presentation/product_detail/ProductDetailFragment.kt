package com.example.thalestestandroidapp.presentation.product_detail

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.databinding.FragmentProductDetailBinding
import com.example.thalestestandroidapp.presentation.utils.let2
import com.example.thalestestandroidapp.presentation.utils.let5
import com.example.thalestestandroidapp.presentation.utils.observerScope
import com.example.thalestestandroidapp.presentation.utils.toFile
import com.example.thalestestandroidapp.presentation.utils.toFormattedPrice
import com.example.thalestestandroidapp.presentation.utils.toType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

/**
 * Handles the cases where the user is viewing/updating an existing Product or creating a new one
 */
@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)
    private val viewModel: ProductDetailViewModel by activityViewModels()

    private val args by navArgs<ProductDetailFragmentArgs>()

    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                binding.detailImage.loadProductImage(uri)
                validImageChange = true
                handleButtonEnableSetting()

                uri.toFile(requireContext())?.let {
                    currentImageFile = it
                }
            }
        }

    private var validNameChange = false
    private var validImageChange = false
    private var validTypeChange = false
    private var validDescChange = false
    private var validPriceChange = false

    private var currentImageFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(ProductDetailAction.OnProductReceived(args.product))

        subscribeObservers()
        initViews()
    }

    private fun handleButtonEnableSetting() {
        binding.confirmButton.isEnabled = if (viewModel.isUpdatingProduct) {
            validNameChange || validImageChange || validDescChange || validTypeChange || validPriceChange
        } else {
            validNameChange && validImageChange && validDescChange && validTypeChange && validPriceChange
        }

    }

    override fun onResume() {
        super.onResume()

        // Set the adapter in onResume (https://github.com/material-components/material-components-android/issues/2012#issuecomment-808853621)
        val productTypes = resources.getStringArray(R.array.product_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, productTypes)
        binding.detailTypeDropdownLayout.setAdapter(arrayAdapter)
    }

    private fun initViews() {
        binding.apply {
            title.text = getString(R.string.edit_product)
            detailImage.apply {
                load(args.product?.imageUrl) {
                    crossfade(true)
                    error(R.drawable.cannot_load_image)
                }
                setOnClickListener {
                    pickImageResultLauncher.launch(
                        PickVisualMediaRequest.Builder().setMediaType(
                            ImageOnly
                        ).build()
                    )
                }
            }
            detailName.editText?.let {
                it.setText(args.product?.name)
                it.doAfterTextChanged { editable ->
                    validNameChange = args.product?.name != editable?.toString() &&
                            !editable?.toString().isNullOrBlank()
                    handleButtonEnableSetting()
                }
            }
            detailType.editText?.let {
                it.setText(args.product?.type?.name)
                it.doAfterTextChanged { editable ->
                    validTypeChange = args.product?.type?.name != editable?.toString()
                    handleButtonEnableSetting()
                }
            }
            detailDescription.editText?.let {
                it.setText(args.product?.description)
                it.doAfterTextChanged { editable ->
                    validDescChange = args.product?.description != editable?.toString() &&
                            !editable?.toString().isNullOrBlank()
                    handleButtonEnableSetting()
                }
            }
            detailPrice.editText?.let {
                it.setText(args.product?.price?.toFormattedPrice())
                it.doAfterTextChanged { editable ->
                    validPriceChange =
                        args.product?.price?.toFormattedPrice() != editable?.toString() &&
                                !editable?.toString().isNullOrBlank()
                    handleButtonEnableSetting()
                }
            }
            confirmButton.setOnClickListener {
                viewModel.apply {
                    if (isUpdatingProduct) {
                        args.product?.id?.let { id ->
                            onAction(
                                ProductDetailAction.UpdateProduct(
                                    id = id,
                                    name = detailNameEditText.text.toString(),
                                    image = currentImageFile,
                                    description = detailDescriptionEditText.text.toString(),
                                    type = detailTypeDropdownLayout.text.toString().toType(),
                                    price = detailPriceEditText.text.toString().toDouble()
                                )
                            )
                        }
                    } else {
                        if (currentImageFile == null) {
                            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            let5(
                                detailTypeDropdownLayout.text.toString().toType(),
                                currentImageFile,
                                detailNameEditText.text.toString(),
                                detailDescriptionEditText.text.toString(),
                                detailPriceEditText.text.toString().toDouble(),
                            ) { type, imageFile, name, desc, price ->
                                onAction(
                                    ProductDetailAction.CreateProduct(
                                        name = name,
                                        imageFile = imageFile,
                                        description = desc,
                                        type = type,
                                        price = price
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subscribeObservers() {
        observerScope {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        observerScope {
            viewModel.product.collect { product ->
                product?.let {
                    binding.apply {
                        detailImage.loadProductImage(it.imageUrl)
                        detailName.editText?.setText(it.name)
                        detailType.editText?.setText(it.type.name)
                        detailDescription.editText?.setText(it.description)
                    }
                }
            }
        }

        observerScope {
            viewModel.events.collect { event ->
                when (event) {
                    is ProductDetailEvents.Error -> {
                        Toast.makeText(
                            requireContext(),
                            event.error.asString(requireContext()),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ProductDetailEvents.ProductUpdated -> {
                        Toast.makeText(
                            requireContext(),
                            event.message.asString(requireContext()),
                            Toast.LENGTH_LONG
                        ).show()

                        Navigation.findNavController(binding.root).navigate(
                            ProductDetailFragmentDirections.actionProductDetailFragmentToProductListFragment(
                                updatedOrCreatedProduct = viewModel.product.value
                            )
                        )
                    }
                }
            }
        }
    }

    private fun ImageView.loadProductImage(imageUrl: Any?) {
        load(imageUrl) {
            crossfade(true)
            error(R.drawable.cannot_load_image)
            listener(
                onError = { _, _ ->
                    imageTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.md_theme_onPrimaryContainer
                    )
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.color.md_theme_inversePrimary_highContrast
                    )
                },
                onSuccess = { _, _ ->
                    imageTintList = null
                }
            )
        }
    }

}