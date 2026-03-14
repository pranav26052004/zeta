package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import com.example.projectzeta.constants.FirebaseDatabases
import com.example.projectzeta.model.Lost
import kotlinx.coroutines.flow.MutableStateFlow

class LostViewModel: ViewModel() {

    val count = MutableStateFlow(0)
    val search = MutableStateFlow("")
    val selectedtabindex=MutableStateFlow(0)
    val footerindex= MutableStateFlow(0)
    val foundindex= MutableStateFlow(0)
    val lists=MutableStateFlow<Lost>(Lost("","","",""))

    init {
        fetchLastCount()
    }

    private fun fetchLastCount() {
        RealtimeFirebaseHelper.readList(FirebaseDatabases.LOST_TABLE, Lost::class.java) { allLostItems ->
            val maxId = allLostItems.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
            count.value = maxId
        }
    }

    fun ReadLostByText(value: String){
        RealtimeFirebaseHelper.readItemUsingProperty(
            FirebaseDatabases.LOST_TABLE,"text",value ?: "Laptop", Lost::class.java){result->
            if (result!=null){
                println(result.toString())
                lists.value=result
            }
            else{
                println(" No data found")
            }
        }
    }

    fun WriteLostById(userViewModel: UserViewModel, text: String, description: String){
        count.value++
        val newId = count.value.toString()
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LOST_TABLE, newId,
            Lost(description, newId, userViewModel.nameOfUser.value, text))
    }
}
