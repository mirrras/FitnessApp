package com.example.fitnessapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.DaysListItemBinding
import com.example.fitnessapp.databinding.ExerciseListItemsBinding
import pl.droidsonroids.gif.GifDrawable

class ExerciseAdapter() :ListAdapter<ExerciseModel, ExerciseAdapter.ExerciseHolder>(MyComparator()) {

    class ExerciseHolder(view: View) :RecyclerView.ViewHolder(view){
        private val binding = ExerciseListItemsBinding.bind(view)
        fun setData(exercise: ExerciseModel)= with(binding){
            tvName.text = exercise.name
            tvCount.text = exercise.time
            cBox.isChecked = exercise.isDone
            imEx.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.exercise_list_items,parent,false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class MyComparator: DiffUtil.ItemCallback<ExerciseModel>(){
        override fun areItemsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }

    }

}