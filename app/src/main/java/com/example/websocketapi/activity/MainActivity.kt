package com.example.websocketapi.activity

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.websocketapi.R
import com.example.websocketapi.databinding.ActivityMainBinding
import com.example.websocketapi.manager.SocketHandler
import com.example.websocketapi.manager.SocketManager
import com.example.websocketapi.model.Currency
import com.example.websocketapi.model.CurrencyResponse
import com.example.websocketapi.model.Data
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chart: LineChart
    var realDataBtcUsd: Float? = null
    var realDataBtcEuro: Float? = null
    lateinit var tvSocketBtcUsd: TextView
    lateinit var tvSocketBtcEuro: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chart = binding.lineChart
        initViews()

        configureChart()
        val btcusd = Currency(Data("live_trades_btcusd"), "bts:subscribe")
        connectSocketBtcUsd(btcusd)


    }

    private fun initViews() {
        tvSocketBtcUsd = findViewById(R.id.tv_socket_btc_usd)
        tvSocketBtcEuro = findViewById(R.id.tv_socket_btc_euro)

    }

    private fun configureChart() {

        chart!!.description.isEnabled = true
        chart!!.setTouchEnabled(true)
        chart!!.isDragEnabled = true
        chart!!.setScaleEnabled(true)
        chart!!.setDrawGridBackground(false)
        chart!!.setPinchZoom(true)
        chart!!.setBackgroundColor(resources.getColor(R.color.blue2))
        val data = LineData()
        data.setValueTextColor(Color.WHITE)
        chart!!.data = data
        val l = chart!!.legend
        l.form = Legend.LegendForm.LINE
        l.textColor = Color.WHITE
        val xl = chart!!.xAxis

        xl.textColor = Color.WHITE
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        val leftAxis = chart!!.axisLeft

        leftAxis.textColor = Color.WHITE
        leftAxis.axisMaximum = 50000f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)

        val rightAxis = chart!!.axisRight
        rightAxis.isEnabled = false
    }

    private fun addEntry() {
        val data: LineData = chart!!.getData()
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(Entry(set.entryCount.toFloat(), (realDataBtcUsd!!) + 0f), realDataBtcUsd!!.toInt())

            data.notifyDataChanged()
            // let the chart know it's data has changed
            chart!!.notifyDataSetChanged()
            // limit the number of visible entries
            chart!!.setVisibleXRangeMaximum(120F)
            // chart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            chart!!.moveViewToX(data.entryCount.toFloat())
            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Dynamic Data")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
        set.lineWidth = 3f
        set.circleRadius = 5f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 30f
        set.setDrawValues(false)
        return set
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("HH mm")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun connectSocketBtcUsd(currency: Currency) {
            SocketManager.connectToSocket(currency, object : SocketHandler {

                override fun onResponse(response: CurrencyResponse) {
                    runOnUiThread {
                        realDataBtcUsd = response.data.price.toFloat()
                        addEntry()
                        tvSocketBtcUsd.text = response.data.price.toString()
                    }

                }

                override fun onFailure(t: Throwable) {

                }

            })
        }

        private fun connectSocketBtcEuro(currency: Currency) {
            SocketManager.connectToSocket(currency, object : SocketHandler {

                override fun onResponse(response: CurrencyResponse) {
                    runOnUiThread {
                        realDataBtcEuro = response.data.price.toFloat()
                        addEntry()
                        tvSocketBtcEuro.text = response.data.price.toString()
                    }

                }

                override fun onFailure(t: Throwable) {

                }

            })
        }


    }
