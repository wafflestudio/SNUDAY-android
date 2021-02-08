package com.wafflestudio.snuday.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ActivityMainBinding
import com.wafflestudio.snuday.ui.main.channel.ChannelFragment
import com.wafflestudio.snuday.ui.main.mypage.MyPageFragment
import com.wafflestudio.snuday.ui.main.notification.NotificationFragment
import com.wafflestudio.snuday.ui.main.schedule.ScheduleFragment
import com.wafflestudio.snuday.ui.main.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = binding.bottomNavView

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ScheduleFragment>(R.id.nav_host_fragment)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_my_schedules -> {
                    supportFragmentManager.commit {
                        replace<ScheduleFragment>(R.id.nav_host_fragment)
                        setReorderingAllowed(true)
                    }
                }
                R.id.navigation_notifications -> {
                    supportFragmentManager.commit {
                        replace<NotificationFragment>(R.id.nav_host_fragment)
                        setReorderingAllowed(true)
                    }
                }
                R.id.navigation_search -> {
                    supportFragmentManager.commit {
                        replace<SearchFragment>(R.id.nav_host_fragment)
                        setReorderingAllowed(true)
                    }
                }
                R.id.navigation_channel -> {
                    supportFragmentManager.commit {
                        replace<ChannelFragment>(R.id.nav_host_fragment)
                        setReorderingAllowed(true)
                    }
                }
                R.id.navigation_my_page -> {
                    supportFragmentManager.commit {
                        replace<MyPageFragment>(R.id.nav_host_fragment)
                        setReorderingAllowed(true)
                    }
                }
                else -> null
            }
            return@setOnNavigationItemSelectedListener true
        }

    }





}