package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.projectzeta.model.Lost
import com.example.projectzeta.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow

class LostViewModel: ViewModel() {

    val count = MutableStateFlow(0)
    val search = MutableStateFlow("")
    val selectedtabindex=MutableStateFlow(0)
    val footerindex= MutableStateFlow(0)
    val foundindex= MutableStateFlow(0)
    val lists=MutableStateFlow<MutableList<Lost>>(mutableListOf())

    init {
        fetchLastCount()
    }

    private fun fetchLastCount() {
        MainRepository.getAllLost { allLostItems ->
            val maxId = allLostItems.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
            count.value = maxId
        }
    }

    fun ReadLostByText(value: String){
        MainRepository.getLostByText(value ?: "Laptop") { result ->
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
        MainRepository.writeLost(newId, Lost(description, newId, userViewModel.nameOfUser.value, text))
    }
}
