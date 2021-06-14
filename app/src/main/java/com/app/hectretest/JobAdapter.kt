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

class JobAdapter(val context: Context, val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var jobsDataList: ArrayList<JobsModel.Data> = arrayListOf()

    fun updateData(newData: ArrayList<JobsModel.Data>) {
        jobsDataList.clear()
        jobsDataList.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = if (viewType == 1) R.layout.pruning_itemview else R.layout.thinning_itemview
        val finalView = LayoutInflater.from(context).inflate(itemView, parent, false)
        return if (viewType == 1) {
            PruningVH(finalView)
        } else {
            ThinningVH(finalView)
        }
    }

    override fun getItemCount(): Int {
        return jobsDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return jobsDataList[position].jobId
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val mHolder = holder as PruningVH
            mHolder.tvJobName.text = jobsDataList[position].jobName
            mHolder.rvPruningStaff.layoutManager = LinearLayoutManager(context)
            mHolder.rvPruningStaff.adapter =
                StaffAdapter(context,
                    jobsDataList[position].jobDetails.staff,
                    jobsDataList[position].jobName,
                    object : OnItemClickListener {
                        override fun onItemClicks(adapter: Any) {
                            (adapter as StaffAdapter).notifyDataSetChanged()
                        }
                    })
            mHolder.tvAddMaxTrees.setOnClickListener {
                val rowMap: HashMap<String, ArrayList<String>> = hashMapOf()
                for (staff: JobsModel.Data.JobDetails.Staff in jobsDataList[position].jobDetails.staff) {
                    val staffName = staff.firstName + staff.lastName
                    for (treeRow: JobsModel.Data.JobDetails.Staff.TreeRow in staff.treeRows) {
                        if (treeRow.isSelected) {
                            if (rowMap.contains(treeRow.rowNo)) {
                                val staffList = rowMap[treeRow.rowNo]
                                staffList?.add(staffName)
                                rowMap[treeRow.rowNo] = staffList!!
                            } else {
                                rowMap[treeRow.rowNo] = arrayListOf(staffName)
                            }
                        }
                    }
                }
                for ((k, v) in rowMap) {
                    for (staff: JobsModel.Data.JobDetails.Staff in jobsDataList[position].jobDetails.staff) {
                        for (treeRow: JobsModel.Data.JobDetails.Staff.TreeRow in staff.treeRows) {
                            if (treeRow.rowNo == k) {
                                val reminTrees = treeRow.totalTreeCount - treeRow.prevWorkDone
                                treeRow.workDone = reminTrees / (v.size)
                            }
                        }
                    }
                }
                onItemClickListener.onItemClicks(mHolder.bindingAdapter as Any)
            }
        } else {
            val mHolder = holder as ThinningVH
            mHolder.tvJobName.text = jobsDataList[position].jobName
            initStaffDetails(mHolder, jobsDataList[position].jobDetails)
            initRecycleViewsThinning(mHolder, jobsDataList[position].jobDetails)
            rateTypeToggleThin(mHolder, jobsDataList[position].jobName)
            initRateData(mHolder, jobsDataList[position].jobDetails)
            mHolder.tvAddMaxTrees.setOnClickListener {
                val rowMap: HashMap<String, ArrayList<String>> = hashMapOf()
                for (staff: JobsModel.Data.JobDetails.Staff in jobsDataList[position].jobDetails.staff) {
                    val staffName = staff.firstName + staff.lastName
                    for (treeRow: JobsModel.Data.JobDetails.Staff.TreeRow in staff.treeRows) {
                        if (treeRow.isSelected) {
                            if (rowMap.contains(treeRow.rowNo)) {
                                val staffList = rowMap[treeRow.rowNo]
                                staffList?.add(staffName)
                                rowMap[treeRow.rowNo] = staffList!!
                            } else {
                                rowMap[treeRow.rowNo] = arrayListOf(staffName)
                            }
                        }
                    }
                }
                for ((k, v) in rowMap) {
                    for (staff: JobsModel.Data.JobDetails.Staff in jobsDataList[position].jobDetails.staff) {
                        for (treeRow: JobsModel.Data.JobDetails.Staff.TreeRow in staff.treeRows) {
                            if (treeRow.rowNo == k) {
                                val reminTrees = treeRow.totalTreeCount - treeRow.prevWorkDone
                                treeRow.workDone = reminTrees / (v.size)
                            }
                        }
                    }
                }
                onItemClickListener.onItemClicks(mHolder.bindingAdapter as Any)
            }
        }
    }

    //============================== Thinning Code starts here ==================================

    private fun initStaffDetails(
        mHolder: ThinningVH,
        jobDetails: JobsModel.Data.JobDetails
    ) {
        //staff one details
        mHolder.tvStaffInitial.text = jobDetails.staff[0].initial
        (jobDetails.staff[0].firstName + " " + jobDetails.staff[0].lastName).also {
            mHolder.tvStaffName.text = it
        }
        mHolder.tvStaffOrchard.text = jobDetails.staff[0].orchard
        mHolder.tvStaffBlock.text = jobDetails.staff[0].block
    }

    private fun initRateData(mHolder: ThinningVH, jobDetails: JobsModel.Data.JobDetails) {
        if (jobDetails.staff[0].rateType == "Piece") {
            mHolder.tvPieceRate.performClick()
            mHolder.etRate.setText(jobDetails.staff[0].ratePerHour.toString())
        } else {
            mHolder.tvWages.performClick()
        }
    }

    private fun initRecycleViewsThinning(
        mHolder: ThinningVH,
        jobDetails: JobsModel.Data.JobDetails
    ) {
        mHolder.rvTreeRowsDetails.layoutManager = LinearLayoutManager(context)
        mHolder.rvTreeRowsDetails.adapter = TreeRowDetailsAdapter(
            context,
            jobDetails.staff[0].treeRows.filter { it.isSelected })
        mHolder.rvTreeRows.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mHolder.rvTreeRows.adapter =
            TreeRowsAdapter(context, jobDetails.staff[0].treeRows, object : OnItemClickListener {
                override fun onItemClicks(adapter: Any) {
                    (adapter as TreeRowsAdapter).notifyDataSetChanged()
                    mHolder.rvTreeRowsDetails.layoutManager = LinearLayoutManager(context)
                    mHolder.rvTreeRowsDetails.adapter = TreeRowDetailsAdapter(
                        context,
                        jobDetails.staff[0].treeRows.filter { it.isSelected })
                }
            })
    }

    private fun rateTypeToggleThin(mHolder: ThinningVH, jobName: String) {
        mHolder.tvPieceRate.setOnClickListener {
            mHolder.tvPieceRate.isSelected = true
            mHolder.tvWages.isSelected = false
            mHolder.tvPieceRate.setTextColor(ContextCompat.getColor(context, R.color.white))
            mHolder.tvWages.setTextColor(ContextCompat.getColor(context, R.color.black))
            mHolder.rlRateView.visibility = View.VISIBLE
        }
        mHolder.tvWages.setOnClickListener {
            mHolder.tvPieceRate.isSelected = false
            mHolder.tvWages.isSelected = true
            mHolder.tvPieceRate.setTextColor(ContextCompat.getColor(context, R.color.black))
            mHolder.tvWages.setTextColor(ContextCompat.getColor(context, R.color.white))
            mHolder.rlRateView.visibility = View.GONE
        }
    }

    class PruningVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPruningStaff: RecyclerView = itemView.findViewById(R.id.rvPruningStaff)
        val tvJobName: TextView = itemView.findViewById(R.id.tvJobName)
        val tvAddMaxTrees: TextView = itemView.findViewById(R.id.tvAddMaxTrees)
    }

    class ThinningVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJobName: TextView = itemView.findViewById(R.id.tvJobName)
        val rvTreeRows: RecyclerView = itemView.findViewById(R.id.rvTreeRows)
        val rvTreeRowsDetails: RecyclerView = itemView.findViewById(R.id.rvTreeRowsDetails)
        val tvPieceRate: TextView = itemView.findViewById(R.id.tvPieceRate)
        val tvWages: TextView = itemView.findViewById(R.id.tvWages)
        val rlRateView: RelativeLayout = itemView.findViewById(R.id.rlRateView)
        val etRate: EditText = itemView.findViewById(R.id.etRate)

        val tvStaffInitial: TextView = itemView.findViewById(R.id.tvStaffInitial)
        val tvStaffName: TextView = itemView.findViewById(R.id.tvStaffName)
        val tvAddMaxTrees: TextView = itemView.findViewById(R.id.tvAddMaxTrees)
        val tvStaffOrchard: TextView = itemView.findViewById(R.id.tvStaffOrchard)
        val tvStaffBlock: TextView = itemView.findViewById(R.id.tvStaffBlock)
    }
}