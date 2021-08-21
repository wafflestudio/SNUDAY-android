package com.wafflestudio.snuday.ui.channel_detail.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wafflestudio.snuday.databinding.FragmentChannelNoticeDetailBinding
import com.wafflestudio.snuday.ui.channel_detail.ChannelDetailViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.toPrettyString
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class ChannelNoticeDetailFragment : Fragment() {

    private lateinit var binding: FragmentChannelNoticeDetailBinding
    private val vm: ChannelDetailViewModel by activityViewModels()
    private val args: ChannelNoticeDetailFragmentArgs by navArgs()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelNoticeDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tagChannel.channelNameText.text = args.channelName

        vm.fetchNoticeById(args.channelId, args.noticeId)
            .subIoObsMain()
            .subscribe({ notice ->
                binding.apply {
                    textTitle.text = notice.title
                    textContents.text = notice.contents
                    textNoticeCreatedAt.text = notice.createdAt.toPrettyString()
                    textWriter.text = notice.writerName
                }
            }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}