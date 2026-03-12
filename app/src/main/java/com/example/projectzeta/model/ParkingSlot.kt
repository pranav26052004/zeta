package com.example.projectzeta.Model

data class ParkingSlot (
    val parkingId:Int = 0,
    var available:Boolean = true,
    var reservedBy:String = ""
)
