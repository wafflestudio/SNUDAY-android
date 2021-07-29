package com.wafflestudio.snuday.ui.main.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wafflestudio.snuday.databinding.FragmentChannelBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private lateinit var binding: FragmentChannelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }
}