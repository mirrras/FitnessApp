package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.DayModel
import com.example.fitnessapp.adapters.DaysAdapter
import com.example.fitnessapp.adapters.ExerciseModel
import com.example.fitnessapp.databinding.FragmentDaysBinding
import com.example.fitnessapp.utils.FragmentManager
import com.example.fitnessapp.utils.MainViewModel

class DaysFragment : Fragment(), DaysAdapter.Listener {
    private lateinit var binding: FragmentDaysBinding
    private var ab: ActionBar? = null
    private val model: MainViewModel by activityViewModels()
    private lateinit var adapter: DaysAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.cuurentDay = 0
        initRcView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.reset){
            model.pref?.edit()?.clear()?.apply()
            adapter.submitList(fillDaysArray())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRcView() = with(binding){
        adapter = DaysAdapter(this@DaysFragment)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.days)
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter
        adapter.submitList(fillDaysArray())
    }

    private fun fillDaysArray():ArrayList<DayModel>{
        val tArray = ArrayList<DayModel>()
        var daysDoneCounter = 0
        resources.getStringArray(R.array.day_exercise).forEach {
            model.cuurentDay++
            val exCounter = it.split(",").size
            tArray.add(DayModel(it, 0,model.getExerciseCount() == exCounter))
        }
        binding.progressBar.max = tArray.size
        tArray.forEach {
            if(it.isDone) daysDoneCounter++
        }
        updateRestDaysUI(tArray.size - daysDoneCounter, tArray.size)
        return tArray
    }

    private fun updateRestDaysUI(restDays: Int, days: Int) = with(binding){
        val rDays = "$restDays " + getString(R.string.left)+ " " + getString(R.string.rest)
        tvRestDays.text = rDays
        progressBar.progress = days - restDays
    }

   private fun fillExerciseList(day: DayModel){
       val tempList = ArrayList<ExerciseModel>()
       day.exercises.split(",").forEach {
           val exerciseList = resources.getStringArray(R.array.exercise)
           val exercise = exerciseList[it.toInt()]
           val exerciseArray = exercise.split("|")
           tempList.add(ExerciseModel(exerciseArray[0], exerciseArray[1], false, exerciseArray[2]))
       }
       model.mutableListExercise.value = tempList
   }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    override fun onClick(day: DayModel) {
        fillExerciseList(day)
        model.cuurentDay = day.dayNumber
        FragmentManager.setFragment(ExerciseListFragment.newInstance(),
            activity as AppCompatActivity)
    }
}