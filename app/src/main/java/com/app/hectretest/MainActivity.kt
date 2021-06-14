package com.app.hectretest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var jobsAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        jobsAdapter = JobAdapter(this, object : OnItemClickListener {
            override fun onItemClicks(adapter: Any) {
                (adapter as JobAdapter).notifyDataSetChanged()
            }
        })
        rvJobs.layoutManager = LinearLayoutManager(this)
        rvJobs.adapter = jobsAdapter
    }

    override fun onStart() {
        super.onStart()
        val dataModel: JobsModel = Gson().fromJson(readFromAsset(), JobsModel::class.java)
        jobsAdapter.updateData(dataModel.data as ArrayList<JobsModel.Data>)
        jobsAdapter.notifyDataSetChanged()
    }

    private fun readFromAsset(): String {
        val bufferReader = application.assets.open("response.json").bufferedReader()
        val data = bufferReader.use {
            it.readText()
        }
        Log.d("readFromAsset", data)
        return data;
    }

}