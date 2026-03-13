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
    fun WriteFoundById(userViewModel: UserViewModel,count:Int,text: String,description: String){
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.FOUND_TABLE,count.toString(),
            Found(description,userViewModel.nameOfUser.value,count.toString(),text))
    }
}