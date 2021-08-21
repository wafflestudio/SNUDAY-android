package com.wafflestudio.snuday.ui.awaiter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.databinding.FragmentAwaiterBinding
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class AwaiterFragment : Fragment() {

    private lateinit var binding: FragmentAwaiterBinding
    private val vm: AwaiterViewModel by viewModels()
    private val args: AwaiterFragmentArgs by navArgs()

    private lateinit var awaiterAdapter: AwaiterAdapter
    private lateinit var awaiterLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAwaiterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        awaiterAdapter = AwaiterAdapter(
            onAcceptClickListener = { userId ->
                vm.acceptAwaiter(args.channelId, userId).subIoObsMain().subscribe({
                    vm.getAwaiter(args.channelId).subIoObsMain().subscribe({
                        awaiterAdapter.awaiterList = it
                        awaiterAdapter.notifyDataSetChanged()
                    }, { Timber.d(it) }).also { compositeDisposable.add(it) }
                }, { Timber.d(it) }).also { compositeDisposable.add(it) }
            },
            onRejectClickListener = { userId ->
                vm.rejectAwaiter(args.channelId, userId).subIoObsMain().subscribe({
                    vm.getAwaiter(args.channelId).subIoObsMain().subscribe({
                        awaiterAdapter.awaiterList = it
                        awaiterAdapter.notifyDataSetChanged()
                    }, { Timber.d(it) }).also { compositeDisposable.add(it) }
                }, { Timber.d(it) }).also { compositeDisposable.add(it) }
            }
        )
        awaiterLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAwaiter.apply {
            adapter = awaiterAdapter
            layoutManager = awaiterLayoutManager
        }

        vm.getAwaiter(args.channelId).subIoObsMain().subscribe({
            awaiterAdapter.awaiterList = it
            awaiterAdapter.notifyDataSetChanged()
        }, { Timber.d(it) }).also { compositeDisposable.add(it) }
    }


    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}