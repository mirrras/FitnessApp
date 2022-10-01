package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.DayFinishBinding
import com.example.fitnessapp.utils.FragmentManager
import pl.droidsonroids.gif.GifDrawable


class DaysDoneFragment : Fragment() {
    private lateinit var binding: DayFinishBinding
    private var ab: ActionBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayFinishBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.done)
        binding.imMain.setImageDrawable(GifDrawable((activity as AppCompatActivity).assets, "gj.gif"))

        binding.bDone.setOnClickListener {
            FragmentManager.setFragment(DaysFragment.newInstance(), activity as AppCompatActivity)
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = DaysDoneFragment()
    }
}