package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.projectzeta.constants.FirebaseDatabases
import com.example.projectzeta.model.LiveNoteSharing
import com.example.projectzeta.Repository.RealtimeFirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow

class LiveNotesSharingViewModel: ViewModel() {
    var footerindex =  MutableStateFlow<Int>(0)
    var selectedTab =  MutableStateFlow<Int>(0)
    var searchQuery =  MutableStateFlow("")
    var searchTitle =  MutableStateFlow("Title")
    var searchLiveText =  MutableStateFlow("Live")
    var goLiveTitle =  MutableStateFlow("")
    var goLiveDescription =  MutableStateFlow("")
    var liveId =  MutableStateFlow("")

    fun serachIdinLiveShare(value:String){
        RealtimeFirebaseHelper.readItemUsingProperty(
            FirebaseDatabases.LIVE_NOTESHARING,"id",value,
            LiveNoteSharing::class.java){ notesSharing ->
            if(notesSharing!=null){
                searchTitle.value= notesSharing.title
                searchLiveText.value=notesSharing.description
            }
            else{
                println("empty id")
            }
        }
    }
    fun liveNotesSharing(userViewModel: UserViewModel, title: String, desc:String){
        RealtimeFirebaseHelper.writeItem(FirebaseDatabases.LIVE_NOTESHARING,userViewModel.userId.value,
            LiveNoteSharing(userViewModel.userId.value,title,desc))
    }
}
