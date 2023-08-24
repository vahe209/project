package com.example.project

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project.databinding.FragmentWrongDataBinding

class WrongDataFragment : Fragment() {
    private lateinit var binding: FragmentWrongDataBinding
    private var fragmentInteractionListener: FragmentInteractionListener? = null
    private val autoCloseDelayMillis = 3000
    private val handler = Handler()
    private val autoCloseRunnable = Runnable {
        fragmentInteractionListener?.onCloseButtonPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWrongDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener {
            fragmentInteractionListener?.onCloseButtonPressed()
        }
        startAutoCloseTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(autoCloseRunnable)
    }

    private fun startAutoCloseTimer() {
        handler.postDelayed(autoCloseRunnable, autoCloseDelayMillis.toLong())
    }
    fun setFragmentInteractionListener(listener: FragmentInteractionListener) {
        fragmentInteractionListener = listener
    }
    interface FragmentInteractionListener {
        fun onCloseButtonPressed()
    }
}