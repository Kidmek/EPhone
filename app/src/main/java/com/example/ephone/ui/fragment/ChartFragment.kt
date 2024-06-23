package com.example.ephone.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ephone.R
import com.example.ephone.model.Bank
import com.example.ephone.model.BankColors
import com.example.ephone.model.Message
import com.example.ephone.util.CustomMarker
import com.example.ephone.viewModel.SharedViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.Duration
import java.time.LocalDateTime


class ChartFragment : Fragment() {

    private lateinit var chart: LineChart
    private lateinit var viewModel: SharedViewModel

    private lateinit var spinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chart, container, false);
        chart = root.findViewById(R.id.idLineChart)
        val datas = ArrayList<LineDataSet>()

        spinner= root.findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dropdown_items, // This is a string array resource, defined in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.custom_drop_down_item)
        spinner.adapter = adapter

        // Drop down filter for different banks
        // banks strings found in res/values/strings.xml should be the same as the
        // sms originating address and the full name can be found in Message data class with colors
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                val filteredDataSets = if (item == "All") {
                    getAllBankDataSets()
                } else {
                    getBankDataSets(item)
                }

                chart.data = LineData(filteredDataSets as List<ILineDataSet>?)
                chart.notifyDataSetChanged()
                chart.invalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        chart.xAxis.labelRotationAngle = 0f
        chart.data = LineData(datas as List<ILineDataSet>?)

        val mv = CustomMarker(requireContext(), R.layout.custom_marker)
        chart.marker = mv
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum=0f
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.description.text = "Days Ago"
        chart.description.textSize=12f
        chart.setNoDataText("No transaction!")
        chart.animateX(1800, Easing.EaseInExpo)

        return root
    }

    private fun getData(messages:List<Message>): ArrayList<Entry> {
        var day = 0L
        val entries = ArrayList<Entry>()

        messages.forEach {
            val duration = Duration.between(it.date_time,LocalDateTime.now())
            var daysBefore = duration.toDays().toFloat()
            if(duration.toDays() != day){
                day = duration.toDays()
            }else{
                // if transaction on the same day take the fraction of the day
                // (duration.toHours()-daysBefore*24) gets the hour of the day
               daysBefore+= (duration.toHours()-daysBefore*24)/24
            }

            entries.add(Entry(
                daysBefore,
                it.remaining_balance.filter
                // remove comma
                {c->c!=',' }.toFloat(),
            ))
        }

        return entries;

    }

    fun getAllBankDataSets(): ArrayList<ILineDataSet> {
        val dataSets = ArrayList<ILineDataSet>()
        Bank.values().forEach { bank ->
            dataSets += createDataSetForBank(bank.name)
        }
        return dataSets
    }

    fun getBankDataSets(bankName: String): ArrayList<ILineDataSet> {
        val dataSets = ArrayList<ILineDataSet>()
        dataSets += createDataSetForBank(bankName)
        return dataSets
    }

    fun createDataSetForBank(bankName: String): LineDataSet {
        val newMessages = viewModel.messages.value?.filter { it.bank_name == Bank.valueOf(bankName).fullName }
        val entries = getData(newMessages ?: emptyList())
        val dataSet = LineDataSet(entries, bankName)
        dataSet.color = BankColors.valueOf(bankName).color
        dataSet.setDrawValues(true)
        dataSet.lineWidth = 3f
        return dataSet
    }


}