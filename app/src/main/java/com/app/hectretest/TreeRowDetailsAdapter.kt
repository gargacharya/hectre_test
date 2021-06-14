package com.app.hectretest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TreeRowDetailsAdapter(
    private val context: Context,
    private val dataList: List<JobsModel.Data.JobDetails.Staff.TreeRow>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.tree_row_details_item, parent, false)
        return TreeRowsVH(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataList[position].isSelected) {
            val mHolder = holder as TreeRowsVH

            ("Trees for row " + dataList[position].rowNo).also {
                mHolder.tvTreeRowNum.text = it
            }

            (dataList[position].workDone.toString() + "/" + dataList[position].totalTreeCount.toString()).also {
                mHolder.tvTreeNumbers.text = it
            }
            if (dataList[position].prevWorkDone > 0) {
                (dataList[position].prevStaffName + " (" + dataList[position].prevWorkDone + ")").also {
                    mHolder.tvStaffWorked.text = it
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class TreeRowsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTreeRowNum: TextView = itemView.findViewById(R.id.tvTreeRowNum)
        val tvTreeNumbers: TextView = itemView.findViewById(R.id.tvTreeNumbers)
        val tvStaffWorked: TextView = itemView.findViewById(R.id.tvStaffWorked)
    }
}