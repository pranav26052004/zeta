package com.example.ProjectZeta.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import  com.example.ProjectZeta.ViewModels.UserViewModel
 import  com.example.ProjectZeta.model.LiveNoteSharing
import com.example.ProjectZeta.constants.FirebaseDatabases
import com.example.myapplication.LiveNotesSharing
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class LiveNotesSharing: ViewModel() {
    var footerindex =  MutableStateFlow<Int>(0)
    var selectedTab =  MutableStateFlow<Int>(0)
    var searchQuery =  MutableStateFlow("")
    var searchTitle =  MutableStateFlow("Title")
    var searchLiveText =  MutableStateFlow("Live")
    var goLiveTitle =  MutableStateFlow("")
    var goLiveDescription =  MutableStateFlow("")
    var liveId =  MutableStateFlow("")

    fun serachIdinLiveShare(value:String){
        RealtimeFirebaseHelper.readItemUsingProperty(FirebaseDatabases.LIVE_NOTESHARING,"id",value,
            LiveNoteSharing::class.java){notesSharing ->
            if(notesSharing!=null){
                searchTitle.value= notesSharing.title
                searchLiveText.value=notesSharing.description
            }
            else{
                println("empty id")
            }
        }
    }
    fun liveNotesSharing(userViewModel: UserViewModel,title: String,desc:String){
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LIVE_NOTESHARING,userViewModel.userId.value,
            LiveNoteSharing(userViewModel.userId.value,title,desc))
    }
}