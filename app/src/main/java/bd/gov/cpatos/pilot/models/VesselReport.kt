package bd.gov.cpatos.pilot.models

import com.google.gson.annotations.SerializedName

data class VesselReport(
    @SerializedName("vvd_gkey")
    val vvd_gkey: String,
    @SerializedName("Import_Rotation_No")
    val Import_Rotation_No: String,
    @SerializedName("Vessel_Name")
    val Vessel_Name: String,
    @SerializedName("Name_of_Master")
    val Name_of_Master: String,
    @SerializedName("input_type")
    val input_type: String
)
