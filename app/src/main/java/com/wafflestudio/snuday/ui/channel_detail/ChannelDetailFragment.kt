package com.wafflestudio.snuday.ui.channel_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentChannelDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.absoluteValue

@AndroidEntryPoint
class ChannelDetailFragment : Fragment() {

    private lateinit var binding: FragmentChannelDetailBinding

    private val imageHeight: Float by lazy { resources.getDimension(R.dimen.channel_detail_image_height) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.channelCollapsingToolbar.channelCollapsingToolbar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
                updateViews(appBarLayout, offset.toFloat())
            }
        )

        binding.channelCollapsingToolbar.buttonBackBlack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.channelCollapsingToolbar.buttonBackWhite.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateViews(appBarLayout: AppBarLayout, offset: Float) {

        if (offset.absoluteValue < imageHeight + 1F) {
            binding.channelCollapsingToolbar.toolbar.apply { alpha = 0F }
        } else {
            val position = (offset.absoluteValue - imageHeight) / (appBarLayout.totalScrollRange.toFloat() - imageHeight)
            when (position) {
                in 0F..0.8F -> {
                    binding.channelCollapsingToolbar.toolbar.apply {
                        alpha = position * 1.25F
                    }
                }
                in 0.8F..1F -> {
                    binding.channelCollapsingToolbar.toolbar.apply {
                        alpha = 1F
                    }
                }
                else -> {
                    binding.channelCollapsingToolbar.toolbar.apply {
                        alpha = 1F
                    }
                }
            }
        }
    }

}