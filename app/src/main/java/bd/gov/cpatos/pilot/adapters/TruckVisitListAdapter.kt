package bd.gov.cpatos.pilot.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.models.TruckVisit
import java.util.*


class TruckVisitListAdapter(context: Context, private val dataSource: ArrayList<TruckVisit>) : BaseAdapter() ,
    Filterable {

    private var truckVisitList: List<TruckVisit>?  = dataSource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cItem = getItem(position) as TruckVisit
        val rowView = inflater.inflate(R.layout.item_gate_pass, parent, false)

        // Get title

        val etVisitid = rowView.findViewById(R.id.etVisitId) as TextView
        etVisitid.text = cItem.visit_id
        val rotation = rowView.findViewById(R.id.etRotation) as TextView
        rotation.text = cItem.import_rotation
        val etCont = rowView.findViewById(R.id.etCont) as TextView
        etCont.text = cItem.cont_no
        val etDriverName = rowView.findViewById(R.id.etDriverName) as TextView
        etDriverName.text = cItem.driver_name



        return rowView
    }

    override fun getCount(): Int {
        return truckVisitList!!.size
    }

    override fun getItem(p0: Int): TruckVisit? {
        return truckVisitList?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return truckVisitList?.get(p0)?.visit_id!!.toLong()
    }

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Filter Class


    override fun getFilter(): Filter {
        // TODO("Not yet implemented")
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                truckVisitList = filterResults.values as List<TruckVisit>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    dataSource
                else
                    dataSource.filter {
                        it.visit_id.lowercase(Locale.getDefault()).contains(queryString) ||
                                it.cont_no.lowercase(Locale.getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }


}


