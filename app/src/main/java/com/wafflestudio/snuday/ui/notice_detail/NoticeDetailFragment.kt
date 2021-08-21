package com.wafflestudio.snuday.ui.notice_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentNoticeDetailBinding
import com.wafflestudio.snuday.utils.getResource
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.toPrettyString
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class NoticeDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoticeDetailBinding
    private val vm: NoticeDetailViewModel by viewModels()
    private val args: NoticeDetailFragmentArgs by navArgs()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeDetailBinding.inflate(inflater, container, false)
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

        vm.getChannelColorById(channelId = args.channelId)
            .subscribe({
                 it?.let { binding.tagChannel.cardView.setCardBackgroundColor(it.getResource(requireContext())) }
            }, {Timber.d(it)}).also { compositeDisposable.add(it) }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}