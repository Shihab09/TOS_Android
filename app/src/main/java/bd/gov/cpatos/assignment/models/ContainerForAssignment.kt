package bd.gov.cpatos.assignment.models

import com.google.gson.annotations.SerializedName

data class ContainerForAssignment(
    @SerializedName("sl")
    val sl: Int,
    @SerializedName("gkey")
    val gkey: String,
    @SerializedName("cont_no")
    val cont_no: String,
    @SerializedName("cnf")
    val cnf: String,
    @SerializedName("cnf_addr")
    val cnf_addr: String,
    @SerializedName("flex_date01")
    val flex_date01: String,
    @SerializedName("assignDt")
    val assignDt: String,
    @SerializedName("bl_nbr")
    val bl_nbr: String,
    @SerializedName("mfdch_value")
    val mfdch_value: String,
    @SerializedName("mfdch_desc")
    val mfdch_desc: String,
    @SerializedName("Yard_No")
    val Yard_No: String,
    @SerializedName("Block_No")
    val Block_No: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("created1")
    val created1: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("cvcvd_gkey")
    val cvcvd_gkey: String,
    @SerializedName("rot_no")
    val rot_no: String,
    @SerializedName("v_name")
    val v_name: String
  )







