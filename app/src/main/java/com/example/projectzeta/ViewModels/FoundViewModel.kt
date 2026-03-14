package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import com.example.projectzeta.constants.FirebaseDatabases
import com.example.projectzeta.model.Found
import kotlinx.coroutines.flow.MutableStateFlow

class FoundViewModel: ViewModel() {

    val count2 = MutableStateFlow(0)
    val search = MutableStateFlow("")
    val selectedtabindex=MutableStateFlow(0)
    val footerindex= MutableStateFlow(0)
    val foundindex= MutableStateFlow(0)
    val lists=MutableStateFlow<Found>(Found("","","",""))

    init {
        fetchLastCount()
    }

    private fun fetchLastCount() {
        RealtimeFirebaseHelper.readList(FirebaseDatabases.FOUND_TABLE, Found::class.java) { allFoundItems ->
            val maxId = allFoundItems.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
            count2.value = maxId
        }
    }

    fun ReadFoundByText(value: String){
        RealtimeFirebaseHelper.readItemUsingProperty(
            FirebaseDatabases.FOUND_TABLE,"text",value, Found::class.java){ result->
            if (result!=null){
                println(result.toString())
                lists.value=result
            }
            else{
                println(" No data found")
            }
        }
    }

    fun WriteFoundById(userViewModel: UserViewModel, text: String, description: String){
        count2.value++
        val newId = count2.value.toString()
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.FOUND_TABLE, newId,
            Found(description, userViewModel.nameOfUser.value, newId, text))
    }
}
