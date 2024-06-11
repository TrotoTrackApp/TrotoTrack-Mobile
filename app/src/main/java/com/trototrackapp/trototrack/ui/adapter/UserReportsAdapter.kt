package com.trototrackapp.trototrack.ui.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.data.remote.response.DataReportUser
import com.trototrackapp.trototrack.databinding.ItemReportBinding
import com.trototrackapp.trototrack.ui.detail.DetailReportActivity
import com.trototrackapp.trototrack.util.convertIso8601ToDate

class UserReportsAdapter : ListAdapter<DataReportUser, UserReportsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
    }

    class MyViewHolder(private val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(report: DataReportUser){
            binding.reportLocation.text = report.location
            binding.reportStatus.text = report.status
            binding.reportStatusDamage.text = report.statusDamage
            binding.reportDate.text = convertIso8601ToDate(report.createdAt ?: "")
            binding.reportVote.text = report.like.toString()

            val backgroundColor = when (report.statusDamage) {
                "light damaged" -> ContextCompat.getColor(itemView.context, R.color.light_yellow)
                "heavy damaged" -> ContextCompat.getColor(itemView.context, R.color.light_red)
                "good" -> ContextCompat.getColor(itemView.context, R.color.light_green)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataReportUser>() {
            override fun areItemsTheSame(oldItem: DataReportUser, newItem: DataReportUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataReportUser, newItem: DataReportUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}