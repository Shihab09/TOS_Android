package bd.gov.cpatos.assignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import bd.gov.cpatos.R
import bd.gov.cpatos.assignment.models.ContainerForAssignment
import java.util.*
import kotlin.collections.ArrayList

class AssignmentContainerListAdapter(context: Context, private var dataSource: ArrayList<ContainerForAssignment>, private  var tvtotalContainer:TextView) : BaseAdapter(),Filterable {
    var cContainer: ContainerForAssignment? = null
    private var mContainerData: List<ContainerForAssignment> = dataSource
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        cContainer = getItem(position) as ContainerForAssignment
        val rowView = inflater.inflate(R.layout.item_container, parent, false)

        // Get title
        val sl = rowView.findViewById(R.id.etSl) as TextView
        sl.text = cContainer?.sl.toString()
        val container = rowView.findViewById(R.id.etContainer) as TextView
        container.text = cContainer?.cont_no
        val assignment = rowView.findViewById(R.id.etAssignment) as TextView
        assignment.text = cContainer?.mfdch_value
        val cnf = rowView.findViewById(R.id.etCfName) as TextView
        cnf.text = cContainer?.cnf
        val rotation = rowView.findViewById(R.id.etRotation) as TextView
        rotation.text = cContainer?.rot_no
        val vesselName = rowView.findViewById(R.id.etVesselName) as TextView
        vesselName.text = cContainer?.v_name
        val size = rowView.findViewById(R.id.etSize) as TextView
        size.text = cContainer?.size
        val height = rowView.findViewById(R.id.etHeight) as TextView
        height.text = cContainer?.height
        val bl = rowView.findViewById(R.id.etBl) as TextView
        bl.text = cContainer?.bl_nbr

        return rowView
    }

    override fun getItem(position: Int): Any {
        return mContainerData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        tvtotalContainer.text = "Total Container|"+mContainerData.size
        return mContainerData.size
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                mContainerData = filterResults.values as ArrayList<ContainerForAssignment>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    dataSource
                else
                    dataSource.filter {
                        it.cont_no.uppercase(Locale.getDefault()).contains(queryString.uppercase(Locale.getDefault()))

                    }
                return filterResults
            }
        }
    }
}