package vn.tiki.android.androidhometest

import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import vn.tiki.android.androidhometest.adapter.DealAdapter
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.di.initDependencies
import vn.tiki.android.androidhometest.di.inject
import vn.tiki.android.androidhometest.di.releaseDependencies
import java.util.*


class MainActivity : AppCompatActivity() {

    val apiServices by inject<ApiServices>()
    val deals = ArrayList<Deal>()
    private var dealAdapter: DealAdapter? = null
    var dealTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies(this)
        setContentView(R.layout.activity_main)
        initView()
        getDeals()
        startTimer()
    }

    private fun startTimer() {
        dealTimer?.cancel()

        dealTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val now = Calendar.getInstance()
                val dealFilter = deals.filter { deal -> !deal.isExpired(now.time) }
                deals.clear()
                deals.addAll(dealFilter)
                dealAdapter?.setDeals(deals)
                dealAdapter?.notifyDataSetChanged()

            }

            override fun onFinish() {
                dealTimer?.start()
            }
        }
        dealTimer?.start()
    }

    private fun initView() {
        rcDeal.layoutManager = GridLayoutManager(this, 2)
        dealAdapter = DealAdapter()
        rcDeal.adapter = dealAdapter
    }

    fun isDiff(newList: List<Deal>, oldList: List<Deal>): Boolean {
        if (newList.size != oldList.size)
            return true
        val now = Calendar.getInstance().time
        newList.forEachIndexed { index, newDeal ->
            val oldDeal = oldList[index]
            val newLeftString = newDeal.getTimeLeft(now)
            val oldLeftString = oldDeal.getTimeLeft(now)
            if (newLeftString != oldLeftString) {
                return true
            }
        }
        return false
    }

    private fun getDeals() {

        object : AsyncTask<Unit, Unit, List<Deal>>() {
            override fun doInBackground(vararg params: Unit?): List<Deal> {
                return apiServices.getDeals()
            }

            override fun onPostExecute(result: List<Deal>?) {
                super.onPostExecute(result)
                result.orEmpty()
                        .forEach { deal ->
                            deals.add(deal)
                        }
                dealAdapter?.setDeals(deals)
                dealAdapter?.notifyDataSetChanged()

            }
        }.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseDependencies()
    }
}
