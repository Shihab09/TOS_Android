package bd.gov.cpatos.pilot.models

import com.google.gson.annotations.SerializedName

data class TruckVisit(
    @SerializedName("visit_id")
    val visit_id: String,
    @SerializedName("import_rotation")
    val import_rotation: String,
    @SerializedName("cont_no")
    val cont_no: String,
    @SerializedName("truck_id")
    val truck_id: String,
    @SerializedName("driver_name")
    val driver_name: String,
    @SerializedName("driver_gate_pass")
    val driver_gate_pass: String,
    @SerializedName("assistant_name")
    val assistant_name: String,
    @SerializedName("assistant_gate_pass")
    val assistant_gate_pass: String,
    @SerializedName("visit_time_slot_start")
    val visit_time_slot_start: String,
    @SerializedName("visit_time_slot_end")
    val visit_time_slot_end: String
)