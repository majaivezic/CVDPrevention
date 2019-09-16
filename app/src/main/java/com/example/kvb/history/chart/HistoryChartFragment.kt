package com.example.kvb.history.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kvb.R
import android.graphics.Color
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.example.kvb.history.chart.data.HistoryChartItem
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_history_chart.*
import kotlin.math.absoluteValue


class HistoryChartFragment: Fragment(){

    val mFirebaseDatabase = FirebaseDatabase.getInstance().reference
    val mFirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLineChartData()
    }

    private fun setupLineChartData() {
        val userId = mFirebaseAuth.currentUser?.uid

        if(userId != null){
            mFirebaseDatabase.child("user").child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@HistoryChartFragment.context, p0.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {

                        val data = p0.value as Map<String, Map<String, String>>

                        val adapterData = data.map {
                            val date = getDate(it.key)
                            val dateMilis = it.key
                            val rel = it.value["relativeScore"]!!.toFloat()

                            val abs_string = it.value["absoluteRisk"]
                            val abs = if (abs_string != null && abs_string != "null" && abs_string != "") {
                                abs_string.toFloat()
                            } else {
                                -1f
                            }

                            HistoryChartItem(dateMilis.toLong(), date, abs, rel)
                        }.sortedBy { it.dateMilis }

                        val relative_entrys = mutableListOf<Entry>()
                        val absolute_entrys = mutableListOf<Entry>()
                        adapterData.forEachIndexed { index, item ->
                            relative_entrys.add(Entry(index.toFloat(), item.relativeScore))
                            if (item.absoluteScore > -1) {
                                absolute_entrys.add(Entry(index.toFloat(), item.absoluteScore))
                            }
                        }

                        val relative_data_set = LineDataSet(relative_entrys, "Relative")
                        relative_data_set.color = Color.BLUE
                        relative_data_set.circleColors = listOf(Color.BLUE)

                        val absolute_data_set = LineDataSet(absolute_entrys, "Absolute")
                        absolute_data_set.color = Color.RED
                        absolute_data_set.circleColors = listOf(Color.RED)

                        val data_list = listOf<ILineDataSet>(relative_data_set, absolute_data_set)
                        val line_data = LineData(data_list)

                        lineChart.data = line_data

                        lineChart.description.isEnabled = false
                        lineChart.setPinchZoom(false)
                        lineChart.axisRight.isEnabled = false
                        lineChart.axisLeft.granularity = 1f
                        lineChart.setScaleEnabled(false)
                        lineChart.setVisibleXRange(0f, 3f)

                        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        lineChart.xAxis.labelCount = 3
                        lineChart.xAxis.granularity = 1f
                        lineChart.xAxis.isGranularityEnabled = false
                        lineChart.xAxis.axisMaximum = adapterData.size + 1f
                        lineChart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val index = value.toInt()
                                return if (index < adapterData.size)
                                    adapterData[value.toInt()].date
                                else ""
                            }
                        }

                        lineChart.invalidate()
                    }
                }

            })

        }


    }

    private fun getDate(date: String): String {
        return DateFormat.format("dd-MM-yyyy", date.toLong()).toString()
    }
}