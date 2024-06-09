package com.trototrackapp.trototrack.ui.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trototrackapp.trototrack.data.remote.response.DataItem
import com.trototrackapp.trototrack.databinding.ItemReportBinding
import com.trototrackapp.trototrack.ui.detail.DetailReportActivity
import com.trototrackapp.trototrack.util.convertIso8601ToDate

class AllReportsAdapter : ListAdapter<DataItem, AllReportsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
    }

    class MyViewHolder(private val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(report: DataItem){
            binding.reportLocation.text = report.location
            binding.reportStatus.text = report.status
            binding.reportStatusDamage.text = report.statusDamage
            binding.reportDate.text = convertIso8601ToDate(report.createdAt ?: "")
            binding.reportVote.text = report.like.toString()

            val backgroundColor = when (report.statusDamage) {
                "light damaged" -> Color.YELLOW
                "heavy damaged" -> Color.RED
                "good" -> Color.GREEN
                else -> Color.TRANSPARENT
            }

            binding.reportStatusDamage.backgroundTintList = ColorStateList.valueOf(backgroundColor)

            Glide.with(binding.root)
                .load(report.image)
                .into(binding.reportImage)
                .clearOnDetach()
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailReportActivity::class.java).apply {
                    putExtra(DetailReportActivity.REPORT_ID, report.id.toString())
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    fun filterData(text: String) {
        Log.d("FilterData", "Search text: $text")
        submitList(if (text.isEmpty()) {
            currentList
        } else {
            val filteredList = currentList.filter { report ->
                val location = report.location ?: ""
                val containsText = location.contains(text, ignoreCase = true)
                Log.d("FilterData", "Location: $location, Contains text: $containsText")
                containsText
            }
            filteredList
        })
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}