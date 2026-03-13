package com.example.projectzeta

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    fun saveLogin(phone:String){
        prefs.edit().putString("user_phone", phone).apply()
    }

    fun getLoggedInUser():String? {
        return prefs.getString("user_phone", null)
    }

    fun logout(){
        prefs.edit().clear().apply()
    }

}