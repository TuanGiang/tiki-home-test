package vn.tiki.android.androidhometest.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.tiki.android.androidhometest.R
import vn.tiki.android.androidhometest.data.api.response.Deal
import java.util.*

class DealAdapter : RecyclerView.Adapter<DealAdapter.DealHolder>() {

    private var deals: List<Deal>? = null

    fun setDeals(deals: List<Deal>) {
        this.deals = deals
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealAdapter.DealHolder {
        return DealHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_deal, parent, false))
    }

    override fun getItemCount(): Int {
        if (deals != null) {
            return deals!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: DealAdapter.DealHolder, position: Int) {
        val deal = deals!![position]
        holder.binding(deal)
    }

    class DealHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun binding(deal: Deal) {
            itemView.findViewById<TextView>(R.id.tvName).text = deal.productName
            itemView.findViewById<TextView>(R.id.tvPrice).text = itemView.context.getString(R.string.vietnam_currency_format, deal.getPrice())
            itemView.findViewById<TextView>(R.id.tvTimeLeft).text = deal.getTimeLeft(Calendar.getInstance().time)
            Glide.with(itemView).load(Uri.parse(deal.productThumbnail)).apply(RequestOptions().placeholder(R.mipmap.product_placeholder).error(R.mipmap.product_placeholder)).into(itemView.findViewById(R.id.thumbnail))
        }
    }

}