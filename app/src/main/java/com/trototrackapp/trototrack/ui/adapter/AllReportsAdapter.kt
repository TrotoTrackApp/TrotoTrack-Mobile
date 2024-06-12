package com.trototrackapp.trototrack.ui.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trototrackapp.trototrack.R
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
                "Light Damaged" -> ContextCompat.getColor(itemView.context, R.color.light_yellow)
                "Heavy Damaged" -> ContextCompat.getColor(itemView.context, R.color.Red)
                "Good" -> ContextCompat.getColor(itemView.context, R.color.light_green)
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