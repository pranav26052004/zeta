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

    fun WriteLostById(userViewModel: UserViewModel,count:Int,text: String,description: String){
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LOST_TABLE,count.toString(),
            Lost(description,count.toString(),userViewModel.nameOfUser.value,text))
    }
}