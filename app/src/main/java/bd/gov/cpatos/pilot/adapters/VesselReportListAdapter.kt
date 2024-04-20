package bd.gov.cpatos.pilot.adapters


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.activities.ReportViewActivity
import bd.gov.cpatos.pilot.activities.VesselDetailsActivity
import bd.gov.cpatos.pilot.activities.reports.ReportActivity
import bd.gov.cpatos.pilot.models.Vessel
import bd.gov.cpatos.pilot.models.VesselReport
import java.util.*


class VesselReportListAdapter(private val reportActivity2: ReportActivity, context: Context, private val dataSource: ArrayList<VesselReport>, ACTIVITY_FOR:String?) : BaseAdapter() ,
    Filterable {

    private var vesselList: List<VesselReport>?  = dataSource
    private var mContext:Context?  = context
    private var ACTIVITY_FOR:String?  = ACTIVITY_FOR
    private var reportActivity:ReportActivity?  = reportActivity2


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cItem = getItem(position) as VesselReport
        val rowView = inflater.inflate(R.layout.item_vessel_report, parent, false)

        // Get title
        val rotation = rowView.findViewById(R.id.etRotation) as TextView
        rotation.text = cItem.Import_Rotation_No
        val agent = rowView.findViewById(R.id.etVesselName) as TextView
        agent.text = cItem.Vessel_Name
        val vslName = rowView.findViewById(R.id.etMasterName) as TextView
        vslName.text = cItem.Name_of_Master
        Log.e("INPUT_TYPE-",cItem.input_type)

        rowView?.setOnClickListener({
            var intent = Intent(mContext, ReportViewActivity::class.java)
            if (cItem.input_type == "incoming")
                intent.putExtra("TITLE", "Incoming Vessel:(" + cItem.Import_Rotation_No + ")")
            else if (cItem.input_type == "shifting")
                intent.putExtra("TITLE", "Shifting Vessel:(" + cItem.Import_Rotation_No + ")")
            else if (cItem.input_type == "outgoing")
                intent.putExtra("TITLE", "Outgoing Vessel:(" + cItem.Import_Rotation_No + ")")
            else if (cItem.input_type == "cancel")
                intent.putExtra("TITLE", "Cancel Vessel:(" + cItem.Import_Rotation_No + ")")
            else
                intent.putExtra("TITLE", "Outgoing Vessel:(" + cItem.Import_Rotation_No + ")")
            // Pass the values to next activity (StationActivity)
            intent.putExtra("VVD_GKEY", cItem.vvd_gkey)
            intent.putExtra("ROTATION", cItem.Import_Rotation_No)
            intent.putExtra("ACTIVITY_FOR", cItem.input_type)
            reportActivity?.startActivityForResult(intent,701)
        })



        return rowView
    }

    override fun getCount(): Int {
        return vesselList!!.size
    }

    override fun getItem(p0: Int): VesselReport? {
        return vesselList?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return vesselList?.get(p0)?.vvd_gkey!!.toLong()
    }

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                vesselList = filterResults.values as List<VesselReport>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = Filter.FilterResults()
                filterResults.values =
                    if (queryString==null || queryString.isEmpty())
                        dataSource
                    else
                        dataSource.filter { it.Import_Rotation_No.lowercase(Locale.getDefault()).contains(queryString) || it.Vessel_Name.lowercase(
                            Locale.getDefault()
                        )
                            .contains(queryString)
                        }
                return filterResults
            }
        }
    }




}

