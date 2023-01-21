package com.example.noteapp_mvvm_with_navgraph_example.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Singleton


abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = this.setBinding()
        setContentView(binding.root)

        onViewReady(savedInstanceState)
    }

    abstract fun onViewReady(savedInstanceState: Bundle?)

    abstract fun setBinding(): T

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}