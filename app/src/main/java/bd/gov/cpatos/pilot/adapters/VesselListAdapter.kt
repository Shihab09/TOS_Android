package bd.gov.cpatos.pilot.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.activities.VesselDetailsActivity
import bd.gov.cpatos.pilot.activities.VesselListTypeWiseActivity
import bd.gov.cpatos.pilot.models.Vessel
import java.util.*


class VesselListAdapter(private val myvesselListTypeWiseActivity: VesselListTypeWiseActivity, context: Context, private val dataSource: ArrayList<Vessel>, ACTIVITY_FOR:String?) : BaseAdapter() ,
    Filterable {

    private var vesselList: List<Vessel>?  = dataSource
    private var mContext:Context?  = context
    private var ACTIVITY_FOR:String?  = ACTIVITY_FOR
    private var vesselListTypeWiseActivity:VesselListTypeWiseActivity?  = myvesselListTypeWiseActivity


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cItem = getItem(position) as Vessel
        val rowView = inflater.inflate(R.layout.item_vessel, parent, false)

        // Get title
        val rotation = rowView.findViewById(R.id.etRotation) as TextView
        rotation.text = cItem.ibVvg
        val agent = rowView.findViewById(R.id.etAgent) as TextView
        agent.text = cItem.agent
        val vslName = rowView.findViewById(R.id.etVesselName) as TextView
        vslName.text = cItem.vesselName
        val status = rowView.findViewById(R.id.etStatus) as TextView
        if(cItem.phaseStr == "CLOSED" && cItem.noOfDays !="-1") {
            status.text = "PENDING FOR " + cItem.noOfDays + "  Days"
            status.setTextColor(Color.parseColor("#ff0000"))
        }else {
            status.text = "ACTIVE"
            status.setTextColor(Color.parseColor("#00ff00"))
        }
        val eta = rowView.findViewById(R.id.etEta) as TextView
        eta.text = cItem.eta

        rowView?.setOnClickListener({
            val intent = Intent(mContext, VesselDetailsActivity::class.java)
            // Pass the values to next activity (StationActivity)
            intent.putExtra("TITLE", "Vessle Details")
            intent.putExtra("VVD_GKEY", cItem.vvdGkey)
            intent.putExtra("ROTATION", cItem.ibVvg)
            intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
            Log.e("ROTATION", cItem.ibVvg)
            vesselListTypeWiseActivity?.startActivityForResult(intent,701)
        })
        return rowView
    }

    override fun getCount(): Int {
        return vesselList!!.size
    }

    override fun getItem(p0: Int): Vessel? {
        return vesselList?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return vesselList?.get(p0)?.vvdGkey!!.toLong()
    }

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                vesselList = filterResults.values as List<Vessel>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = Filter.FilterResults()
                filterResults.values =
                    if (queryString==null || queryString.isEmpty())
                        dataSource
                    else
                        dataSource.filter { it.vesselName.lowercase(Locale.getDefault()).contains(queryString) || it.ibVvg.lowercase(
                            Locale.getDefault()
                        )
                            .contains(queryString)
                    }
                return filterResults
            }
        }
    }
}


