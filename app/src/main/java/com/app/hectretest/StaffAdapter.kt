package com.app.hectretest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StaffAdapter(
    private val context: Context,
    val staffList: List<JobsModel.Data.JobDetails.Staff>,
    val jobName: String,
    val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.staff_itemview, parent, false)
        return StaffVH(itemView)
    }

    override fun getItemCount(): Int {
        return staffList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as StaffVH
        rateTypeToggle(holder, jobName)
        initStaffDetails(holder, staffList[position])
        initRateData(holder, staffList[position])
        initRecycleViews(holder, staffList[position])
        initClicks(holder)
    }

    private fun initClicks(holder: StaffVH) {
        holder.tvApplyToAll.setOnClickListener {
            val rates = holder.etRate.text.toString().toInt()
            for (staff: JobsModel.Data.JobDetails.Staff in staffList) {
                staff.ratePerHour = rates
            }
            onItemClickListener.onItemClicks(holder.bindingAdapter as Any)
        }
    }

    private fun initStaffDetails(
        mHolder: StaffVH,
        staff: JobsModel.Data.JobDetails.Staff
    ) {
        //staff one details
        mHolder.tvStaffInitial.text = staff.initial
        (staff.firstName + " " + staff.lastName).also {
            mHolder.tvStaffName.text = it
        }
        mHolder.tvStaffOrchard.text = staff.orchard
        mHolder.tvStaffBlock.text = staff.block
    }

    private fun initRateData(mHolder: StaffVH, staff: JobsModel.Data.JobDetails.Staff) {
        if (staff.rateType == "Piece") {
            mHolder.tvPieceRate.performClick()
            mHolder.etRate.setText(staff.ratePerHour.toString())
        } else {
            mHolder.tvWages.performClick()
        }
    }

    private fun initRecycleViews(mHolder: StaffVH, staff: JobsModel.Data.JobDetails.Staff) {

        mHolder.rvTreeRowsDetails.layoutManager = LinearLayoutManager(context)
        mHolder.rvTreeRowsDetails.adapter = TreeRowDetailsAdapter(
            context,
            staff.treeRows.filter { it.isSelected })

        mHolder.rvTreeRows.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mHolder.rvTreeRows.adapter =
            TreeRowsAdapter(context, staff.treeRows, object : OnItemClickListener {
                override fun onItemClicks(adapter: Any) {
                    (adapter as TreeRowsAdapter).notifyDataSetChanged()
                    mHolder.rvTreeRowsDetails.layoutManager = LinearLayoutManager(context)
                    mHolder.rvTreeRowsDetails.adapter = TreeRowDetailsAdapter(
                        context,
                        staff.treeRows.filter { it.isSelected })
                }
            })
    }

    private fun rateTypeToggle(mHolder: StaffVH, jobName: String) {
        mHolder.tvPieceRate.setOnClickListener {
            mHolder.tvPieceRate.isSelected = true
            mHolder.tvWages.isSelected = false
            mHolder.tvPieceRate.setTextColor(ContextCompat.getColor(context, R.color.white))
            mHolder.tvWages.setTextColor(ContextCompat.getColor(context, R.color.black))
            mHolder.rlRateView.visibility = View.VISIBLE
            mHolder.tvWagesViewMsg.visibility = View.GONE
        }
        mHolder.tvWages.setOnClickListener {
            mHolder.tvPieceRate.isSelected = false
            mHolder.tvWages.isSelected = true
            mHolder.tvPieceRate.setTextColor(ContextCompat.getColor(context, R.color.black))
            mHolder.tvWages.setTextColor(ContextCompat.getColor(context, R.color.white))
            mHolder.rlRateView.visibility = View.GONE
            mHolder.tvWagesViewMsg.visibility = View.VISIBLE
            mHolder.tvWagesViewMsg.text = "$jobName will be paid by wages in this timesheet."
        }
    }


    class StaffVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvTreeRows: RecyclerView = itemView.findViewById(R.id.rvTreeRows)
        val rvTreeRowsDetails: RecyclerView = itemView.findViewById(R.id.rvTreeRowsDetails)
        val tvPieceRate: TextView = itemView.findViewById(R.id.tvPieceRate)
        val tvWages: TextView = itemView.findViewById(R.id.tvWages)
        val tvWagesViewMsg: TextView = itemView.findViewById(R.id.tvWagesViewMsg)
        val rlRateView: RelativeLayout = itemView.findViewById(R.id.rlRateView)
        val etRate: EditText = itemView.findViewById(R.id.etRate)
        val tvApplyToAll: TextView = itemView.findViewById(R.id.tvApplyToAll)
        val tvStaffInitial: TextView = itemView.findViewById(R.id.tvStaffInitial)
        val tvStaffName: TextView = itemView.findViewById(R.id.tvStaffName)
        val tvStaffOrchard: TextView = itemView.findViewById(R.id.tvStaffOrchard)
        val tvStaffBlock: TextView = itemView.findViewById(R.id.tvStaffBlock)
    }
}