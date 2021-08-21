package com.wafflestudio.snuday.ui.channel_detail.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wafflestudio.snuday.databinding.FragmentChannelAddNoticeBinding
import com.wafflestudio.snuday.ui.channel_detail.ChannelDetailViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class ChannelAddNoticeFragment : Fragment() {

    private lateinit var binding: FragmentChannelAddNoticeBinding
    private val vm: ChannelDetailViewModel by activityViewModels()
    private val args: ChannelAddNoticeFragmentArgs by navArgs()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelAddNoticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSave.setOnClickListener {
            vm.postNotice(args.channelId, binding.editTextTitle.text.toString(), binding.editTextDescription.text.toString())
                .subIoObsMain()
                .subscribe({
                    findNavController().popBackStack()
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

}