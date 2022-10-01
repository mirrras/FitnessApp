package com.example.fitnessapp.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.ExerciseModel
import com.example.fitnessapp.databinding.ExerciseBinding
import com.example.fitnessapp.utils.FragmentManager
import com.example.fitnessapp.utils.MainViewModel
import com.example.fitnessapp.utils.TimeUtils
import pl.droidsonroids.gif.GifDrawable

class ExerciseFragment : Fragment() {
    private lateinit var binding: ExerciseBinding
    private var exerciseCounter= 0
    private var timer: CountDownTimer? = null
    private var exList: ArrayList<ExerciseModel>? = null
    private var ab: ActionBar? = null
    private var currentDay = 0
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.cuurentDay
        exerciseCounter = model.getExerciseCount()
        ab = (activity as AppCompatActivity).supportActionBar
        model.mutableListExercise.observe(viewLifecycleOwner){
            exList = it
            nextExercise()
        }
        binding.bNext.setOnClickListener{
            nextExercise()
        }
    }

    private fun nextExercise(){
        if(exerciseCounter < exList?.size!!){
            val ex = exList?.get(exerciseCounter++) ?: return
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            exerciseCounter++
            FragmentManager.setFragment(DaysDoneFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    private fun showExercise(exercise:ExerciseModel) = with(binding){
        imMain.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        tvName.text = exercise.name
        val title = "$exerciseCounter / ${exList?.size}"
        ab?.title = title
    }

    private fun setExerciseType(exercise: ExerciseModel){
        if(exercise.time.startsWith("x")){
            binding.tvTime.text = exercise.time
            timer?.cancel()
            binding.progressBar2.visibility = View.INVISIBLE

        } else {
            startTimer(exercise)
            binding.progressBar2.visibility = View.VISIBLE
        }
    }

    private fun showNextExercise() = with(binding){
        if(exerciseCounter < exList?.size!!){
            val ex = exList?.get(exerciseCounter) ?: return
            imNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            setTimeType(ex)
        }else{
            imNext.setImageDrawable(GifDrawable(root.context.assets, "gj.gif"))
            tvNextName.text = getString(R.string.done)

        }
    }

    private fun setTimeType(ex: ExerciseModel){
        if(ex.time.startsWith("x")){
            binding.tvNextName.text = "${ex.name}: ${ex.time}"
        } else {
            val name = ex.name + ": ${TimeUtils.getTime(ex.time.toLong() * 1000)}"
            binding.tvNextName.text = name
        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding){
        progressBar2.max = exercise.time.toInt() * 1000
        timer = object: CountDownTimer(exercise.time.toLong() * 1000, 1){
            override fun onTick(restTime: Long) {
                tvTime.text = TimeUtils.getTime(restTime)
                progressBar2.progress = restTime.toInt()
            }

            override fun onFinish() {
                nextExercise()
            }

        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1)
        timer?.cancel()
    }



        companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}