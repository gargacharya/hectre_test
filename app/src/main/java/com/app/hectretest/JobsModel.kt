package com.app.hectretest

class JobsModel(
    val `data`: List<Data> = listOf()
) {
    class Data(
        val jobDetails: JobDetails = JobDetails(),
        val jobId: Int = 0,
        val jobName: String = ""
    ) {
        class JobDetails(
            val staff: List<Staff> = listOf()
        ) {
            class Staff(
                val block: String = "",
                val firstName: String = "",
                val initial: String = "",
                val lastName: String = "",
                val orchard: String = "",
                var ratePerHour: Int = 0,
                val rateType: String = "",
                val treeRows: List<TreeRow> = listOf()
            ) {
                class TreeRow(
                    val prevStaffName: String = "",
                    val prevWorkDone: Int = 0,
                    val rowNo: String = "",
                    val totalTreeCount: Int = 0,
                    var workDone: Int = 0,
                    var isSelected: Boolean = false
                )
            }
        }
    }
}