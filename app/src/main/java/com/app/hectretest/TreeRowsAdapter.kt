package com.app.hectretest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TreeRowsAdapter(
    private val context: Context,
    private val dataList: List<JobsModel.Data.JobDetails.Staff.TreeRow>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.tree_rows_item, parent, false)
        return TreeRowsVH(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as TreeRowsVH
        mHolder.tvRowName.text = dataList[position].rowNo
        mHolder.rlTreeRowNoView.isSelected = dataList[position].isSelected
        if(dataList[position].isSelected){
            mHolder.tvRowName.setTextColor(ContextCompat.getColor(context, R.color.white))
        }else{
            mHolder.tvRowName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        initOrangeView(mHolder, dataList[position])
        initClicks(mHolder, position)
    }

    private fun initClicks(
        mHolder: TreeRowsVH,
        position: Int
    ) {
        mHolder.rlTreeRowNoView.setOnClickListener {
            if (dataList[position].isSelected) {
                mHolder.tvRowName.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                mHolder.tvRowName.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            dataList[position].isSelected = !dataList[position].isSelected
            onItemClickListener.onItemClicks(mHolder.bindingAdapter as Any)
        }
    }

    private fun initOrangeView(
        mHolder: TreeRowsVH,
        treeRowModel: JobsModel.Data.JobDetails.Staff.TreeRow
    ) {
        if (treeRowModel.prevWorkDone == 0) {
            mHolder.viewOrangeDot.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class TreeRowsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRowName: TextView = itemView.findViewById(R.id.tvRowName)
        val rlTreeRowNoView: RelativeLayout = itemView.findViewById(R.id.rlTreeRowNoView)
        val viewOrangeDot: View = itemView.findViewById(R.id.viewOrangeDot)
    }
}