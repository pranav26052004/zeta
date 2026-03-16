package com.example.projectzeta.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectzeta.model.Found
import com.example.projectzeta.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FoundViewModel: ViewModel() {

    val count2 = MutableStateFlow(0)
    val search = MutableStateFlow("")
    val selectedtabindex=MutableStateFlow(0)
    val footerindex= MutableStateFlow(0)
    val foundindex= MutableStateFlow(0)
    val lists=MutableStateFlow<MutableList<Found>>(mutableListOf())

    init {
        fetchLastCount()
    }

    private fun fetchLastCount() {
        MainRepository.getAllFound { allFoundItems ->
            val maxId = allFoundItems.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
            count2.value = maxId
        }
    }

    fun ReadFoundByText(value: String){
        MainRepository.getFoundByText(value) { result ->
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
        val foundItem = Found(description = description, foundByUser = userViewModel.nameOfUser.value, id = newId, text = text)
        Log.d("FoundItem", foundItem.toString())
        MainRepository.writeFound(newId, foundItem)
    }
}
