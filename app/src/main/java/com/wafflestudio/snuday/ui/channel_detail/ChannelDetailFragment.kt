package com.wafflestudio.snuday.ui.channel_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentChannelDetailBinding
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import kotlin.math.absoluteValue
import kotlin.random.Random

@AndroidEntryPoint
class ChannelDetailFragment : Fragment() {

    private val vm: ChannelDetailViewModel by viewModels()
    private lateinit var binding: FragmentChannelDetailBinding
    private val args: ChannelDetailFragmentArgs by navArgs()

    private val imageHeight: Float by lazy { resources.getDimension(R.dimen.channel_detail_image_height) }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.channelCollapsingToolbar.imageChannelBackground.setRandomBackground()

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

        vm.fetchChannelData(args.channelId).subIoObsMain()
            .subscribe({ channelData ->
                channelData.image?.let {
                    binding.channelCollapsingToolbar.imageChannel.setImage(it)
                }
                binding.channelCollapsingToolbar.tagChannelName.channelNameText.text = channelData.name
                binding.channelCollapsingToolbar.textChannelTitleToolbar.text = channelData.name
                binding.channelCollapsingToolbar.textChannelDetail.text = channelData.description


            }, {

            }).also { compositeDisposable.add(it) }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
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

    private fun ImageView.setRandomBackground() {
        val imageSource = when((0..8).random()) {
            0 -> R.drawable.image_channel_background_1
            1 -> R.drawable.image_channel_background_2
            2 -> R.drawable.image_channel_background_3
            3 -> R.drawable.image_channel_background_4
            4 -> R.drawable.image_channel_background_5
            5 -> R.drawable.image_channel_background_6
            6 -> R.drawable.image_channel_background_7
            7 -> R.drawable.image_channel_background_8
            8 -> R.drawable.image_channel_background_9
            else -> R.drawable.image_channel_background_1
        }

        Glide.with(requireContext())
            .load(imageSource)
            .into(this)
    }

}