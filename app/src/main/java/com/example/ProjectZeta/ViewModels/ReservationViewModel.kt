package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.ProjectZeta.constants.FirebaseDatabases
import com.example.projectzeta.Model.ParkingSlot
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//package com.example.ProjectZeta.ViewModels
//
//import androidx.lifecycle.ViewModel
//import com.example.projectzeta.Model.ParkingSlot
//import com.example.projectzeta.Repository.RealtimeFirebase
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//class ReservationViewModel : ViewModel() {
//    private val _slots = MutableStateFlow<List<ParkingSlot>>(emptyList())
//    init{
//        loadSlots()
//    }
//
//    val tableName:String = "parkingSlots"
//    fun loadSlots(){
//        RealtimeFirebase.readList(
//            tableName = tableName,
//            ParkingSlot::class.java
//        ){list-> _slots.value = list}
//    }
//
//    val slots: StateFlow<List<ParkingSlot>> = _slots
//
//    fun reserveWithDb(slot: ParkingSlot, currentUser:String){
//        if(slot.available){
//            val updatedSlot = slot.copy(available = false, reservedBy = currentUser)
//            RealtimeFirebase.writeItem(tableName, (slot.parkingId-1).toString(), updatedSlot)
//        } else {
//            if(slot.reservedBy == currentUser){
//                val updatedSlot = slot.copy(available = true, reservedBy = "")
//                RealtimeFirebase.writeItem(tableName, (slot.parkingId-1).toString(), updatedSlot)
//            }
//        }
//    }
//}
class ReservationViewModel : ViewModel(){
    private val _slots = MutableStateFlow<List<ParkingSlot>>(emptyList())
    init{
        loadSlots()
    }

//    val tableName:String = "parkingSlots"
    fun loadSlots(){
        RealtimeFirebaseHelper.readList(
            tableName = FirebaseDatabases.PARKING_SLOT,//"parkingSlots",
            ParkingSlot::class.java
        ){list-> _slots.value = list}
    }

    val slots: StateFlow<List<ParkingSlot>> = _slots

    fun reserveWithDb(slot: ParkingSlot, currentUser:String){
        if(slot.available){
            val updatedSlot = slot.copy(available = false, reservedBy = currentUser)
            RealtimeFirebaseHelper.writeItem(FirebaseDatabases.PARKING_SLOT/*"parkingSlots"*/, (slot.parkingId-1).toString(), updatedSlot)
        } else {
            if(slot.reservedBy == currentUser){
                val updatedSlot = slot.copy(available = true, reservedBy = "")
                RealtimeFirebaseHelper.writeItem(FirebaseDatabases.PARKING_SLOT/*"parkingSlots"*/, (slot.parkingId-1).toString(), updatedSlot)
            }
        }
    }
}
