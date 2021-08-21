package com.wafflestudio.snuday.ui.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wafflestudio.snuday.databinding.FragmentMypageBinding
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private val vm: MyPageViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getMyData().subIoObsMain().subscribe({
            binding.textUserid.text = it.username
            binding.textUsername.text = it.lastName + " " + it.firstName
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}