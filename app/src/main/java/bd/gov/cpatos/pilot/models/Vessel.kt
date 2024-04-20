package bd.gov.cpatos.pilot.models

import com.google.gson.annotations.SerializedName


data class Vessel(
    @SerializedName("vvd_gkey")
    val vvdGkey: String,
    @SerializedName("name")
    val vesselName: String,
    @SerializedName("ib_vvg")
    val ibVvg: String,
    @SerializedName("ob_vvg")
    val obVvg: String,
    @SerializedName("phase_num")
    val phaseNum: String,
    @SerializedName("phase_str")
    val phaseStr: String,
    @SerializedName("agent")
    val agent: String,
    @SerializedName("noOfDays")
    val noOfDays: String,
    @SerializedName("eta")
    val eta: String
)