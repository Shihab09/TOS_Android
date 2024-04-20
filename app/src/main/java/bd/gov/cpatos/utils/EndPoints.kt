package bd.gov.cpatos.utils

object EndPoints {
    //private val URL_ROOT = IPFinder().getIPAddress(useIPv4 = true)
    val BASE_URL = "http://cpatos.gov.bd/"
    private val URL_ROOT = "http://cpatos.gov.bd/tosapi/"
    val URL_LOAD_INITIAL_DATA = URL_ROOT+"load_initial_data.php"
    val URL_LOGIN = URL_ROOT + "auth/login.php"
    val URL_RESETPASS = URL_ROOT + "auth/reset.php"
    //Gate  Module Start
    val URL_SERVER = URL_ROOT
    val URL_GATEDASHBORD_DATAGET = URL_ROOT+"gate/getGateDashbordData.php"
    //Gate in
    val URL_GATEIN_SCAN_DATA_RESONSE = URL_ROOT+"gate/gatein_scandata_response.php"
    val URL_GATEIN_SCAN_DATA = URL_ROOT+"gate/gatein_scandata.php"

    val URL_GATEIN_DATA = URL_ROOT+"gate/gateindata.php"
    val URL_GATEIN_DATAGET = URL_ROOT+"gate/gatein.php"
    //val URL_GATEIN_DATAGET = URL_ROOT+"gate/getGateInData.php"
    val URL_GATEIN_DATASAVE = URL_ROOT+"gate/updateGateInData.php"
     //Gate Out
     val URL_GATEOUT_DATAGET = URL_ROOT+"gate/gateout.php"
   // val URL_GATEOUT_DATAGET = URL_ROOT+"gate/getGateOutData.php"
    val URL_GATEOUT_DATASAVE = URL_ROOT+"gate/updateGateOutData.php"
     //Gate Module End
    //ReeferWater
     val URL_REF_SELECT = URL_ROOT+"reeferwater/reefer_select.php"
     val URL_REF_SAVE = URL_ROOT+"reeferwater/reefer_insert.php"
     val URL_WATER_SELECT = URL_ROOT+"reeferwater/water_select.php"
     val URL_WATER_INSERT = URL_ROOT+"reeferwater/water_insert.php"

     val URL_GET_WATER_VESSEL=URL_ROOT+"pilot/"+"v11042023/"+"live/"+"get_water_demond_list.php"


    //PailotModule
   // val PILOTAGE_API_VERSION = "v01092022"
    val PILOTAGE_API_VERSION = "v03212023"
    val PILOTAGE_API_FOLDER="/n4_new"
 //   val PILOTAGE_API_FOLDER="/live"

    val URL_GET_SUMMERY = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+PILOTAGE_API_FOLDER+"/get_summery.php" //Used in PilotLandingPage
    val URL_GET_VESSEL_LIST = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+PILOTAGE_API_FOLDER+"/get_vessel_list.php" // Used in VesselListTypeWiseActivity
    val URL_ADD_NEW_VESSEL = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/new_vessel_insert.php" // Used in AddVesselActivity
    val URL_GET_VESSEL_DETAILS = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+PILOTAGE_API_FOLDER+"/get_vessel_details.php" //used in VesselDetailsActivity
    val URL_ADD_VESSEL_DETAILS = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/set_vessel_details.php" //used in VesselDetailsActivity
    val URL_GET_BERTH_LIST = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/get_berth_list.php" // used in PilotageOperation,FinalSubmitPilotOperationActivity,CancelationActivity
    val URL_GET_VESSEL_EVENT = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/get_vessel_event.php" // used in PilotageOperation,AddAdditionalInformationActivity
    val URL_SET_VESSEL_EVENT = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/set_vessel_event.php" // used in PilotageOperation,AddAdditionalInformationActivity
    val URL_GET_ALL_FINAL_DATA = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/get_all_final_data.php" //used in FinalSubmitPilotOperationActivity
    val URL_EDIT_VESSEL_EVENT = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/edit_vessel_event.php" //used in FinalSubmitPilotOperationActivity
    val URL_GET_REPORT_LIST = URL_ROOT+"pilot/"+PILOTAGE_API_VERSION+ PILOTAGE_API_FOLDER+"/get_report.php" // used in ReportActivity

//
    // val URL_ADD_VESSEL_EVENT = URL_ROOT+"pilot/v20220201/add_vessel_event.php"



    //pilot
    val URL_GET_ASSIGNMENT_CONTAINER_LIST = URL_ROOT+"assignment/get_special_assignment_container_list.php"
    //SignUp
    var URL_SIGNUP =  URL_ROOT+"cnf/signup.php"
    //RESET PASSWORD
    var URL_RESET =  URL_ROOT+"cnf/reset.php"

    //CnfTruck Entry
    var URL_CNF_TRUCK_ENTRY =  URL_ROOT+"cnf/truck_entry_fcl.php"
    var URL_CNF_TRUCK_PAYMENT =  URL_ROOT+"cnf/payment.php"
    var URL_GET_TRUCK_VISIT_LIST =  URL_ROOT+"cnf/get_all_truck_visit_list.php"

    var URL_OPEN_VISIT_ID_CREATE =  URL_ROOT+"cnf/get_payment_unique_id_for_open_payment.php"
    var URL_OPEN_PAYMENT =  URL_ROOT+"cnf/open_payment.php"
    var URL_OPEN_PAYMENT_GATEPASS =  URL_ROOT+"cnf/open_payment_gatepass.php"
    val URL_GET_FnF_LIST = URL_ROOT+"cnf/get_cnf_list.php"


    //Sonali Payment GateAyURL
    var URL_SONALI_GET_TOKEN_REQUEST = "https://spg.com.bd:6314/api/SpgService/GetSessionKey"
    var URL_SONALI_PAYMENT_REQUEST = "https://spg.com.bd:6314/api/SpgService/PaymentByPortal"
    //EkPay
    var URL_EKPAY_GET_TOKEN_REQUEST = "https://spg.com.bd:6314/api/SpgService/GetSessionKey"
    var URL_EKPAY_PAYMENT_REQUEST = "https://spg.com.bd:6314/api/SpgService/PaymentByPortal"
}