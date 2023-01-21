package com.example.noteapp_mvvm_with_navgraph_example.presentation.activity

import android.os.Bundle
import com.example.noteapp_mvvm_with_navgraph_example.databinding.ActivityMainBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
    }

}