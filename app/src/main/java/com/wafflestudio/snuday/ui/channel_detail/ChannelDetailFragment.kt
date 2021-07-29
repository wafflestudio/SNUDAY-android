package com.wafflestudio.snuday.ui.channel_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentChannelDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelDetailFragment : Fragment() {

    private lateinit var binding: FragmentChannelDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}