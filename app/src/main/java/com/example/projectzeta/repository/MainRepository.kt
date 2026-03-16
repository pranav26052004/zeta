package com.example.projectzeta.repository

import com.example.projectzeta.FirebaseDatabaseHelper.RealtimeFirebaseHelper
import com.example.projectzeta.constants.FirebaseDatabases
import com.example.projectzeta.Model.User
import com.example.projectzeta.Model.ParkingSlot
import com.example.projectzeta.model.Lost
import com.example.projectzeta.model.Found
import com.example.projectzeta.model.LiveNoteSharing

object MainRepository {

    // User Methods
    fun getUser(mobileNo: String, onResult: (User?) -> Unit) {
        RealtimeFirebaseHelper.readItemUsingProperty(
            FirebaseDatabases.USER_TABLE, "mobileNo", mobileNo, User::class.java, onResult
        )
    }

    fun getUserByUserId(userId: String, onResult: (User?) -> Unit) {
        RealtimeFirebaseHelper.readItemUsingProperty(
            FirebaseDatabases.USER_TABLE, "userId", userId, User::class.java, onResult
        )
    }

    fun saveUser(user: User) {
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.USER_TABLE, user.userId, user)
    }

    fun deleteUser(userId: String) {
        RealtimeFirebaseHelper.deleteItem(FirebaseDatabases.USER_TABLE, userId)
    }

    // Parking/Reservation Methods
    fun getParkingSlots(onResult: (List<ParkingSlot>) -> Unit) {
        RealtimeFirebaseHelper.readList(FirebaseDatabases.PARKING_SLOT, ParkingSlot::class.java, onResult)
    }

    fun updateParkingSlot(index: String, slot: ParkingSlot) {
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.PARKING_SLOT, index, slot)
    }

    // Lost Methods
    fun getAllLost(onResult: (List<Lost>) -> Unit) {
        RealtimeFirebaseHelper.readList(FirebaseDatabases.LOST_TABLE, Lost::class.java, onResult)
    }

    fun getLostByText(text: String, onResult: (MutableList<Lost>) -> Unit) {
        RealtimeFirebaseHelper.readListByText(FirebaseDatabases.LOST_TABLE, "text", text, Lost::class.java, onResult)
    }

    fun writeLost(id: String, lost: Lost) {
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LOST_TABLE, id, lost)
    }

    // Found Methods
    fun getAllFound(onResult: (List<Found>) -> Unit) {
        RealtimeFirebaseHelper.readList(FirebaseDatabases.FOUND_TABLE, Found::class.java, onResult)
    }

    fun getFoundByText(text: String, onResult: (MutableList<Found>) -> Unit) {
        RealtimeFirebaseHelper.readListByText(FirebaseDatabases.FOUND_TABLE, "text", text, Found::class.java, onResult)
    }

    fun writeFound(id: String, found: Found) {
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.FOUND_TABLE, id, found)
    }

    // Live Note Sharing Methods
    fun getLiveNoteById(id: String, onResult: (LiveNoteSharing?) -> Unit) {
        RealtimeFirebaseHelper.readItemUsingProperty(FirebaseDatabases.LIVE_NOTESHARING, "id", id, LiveNoteSharing::class.java, onResult)
    }

    fun observeLiveNoteById(id: String, onResult: (LiveNoteSharing?) -> Unit) {
        RealtimeFirebaseHelper.observeItemUsingProperty(FirebaseDatabases.LIVE_NOTESHARING, "id", id, LiveNoteSharing::class.java, onResult)
    }

    fun writeLiveNote(userId: String, note: LiveNoteSharing) {
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LIVE_NOTESHARING, userId, note)
    }
}
