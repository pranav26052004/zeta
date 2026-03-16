package com.example.projectzeta.ViewModels

import androidx.lifecycle.ViewModel
import com.example.projectzeta.model.LiveNoteSharing
import com.example.projectzeta.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow

class LiveNotesSharingViewModel: ViewModel() {
    var footerindex =  MutableStateFlow<Int>(0)
    var selectedTab =  MutableStateFlow<Int>(0)
    var searchQuery =  MutableStateFlow("")
    var searchTitle =  MutableStateFlow("Title")
    var searchLiveText =  MutableStateFlow("Live")
    var goLiveTitle =  MutableStateFlow("")
    var goLiveDescription =  MutableStateFlow("")

    var selectedtabindex = MutableStateFlow(0)

    fun serachIdinLiveShare(value:String){
        MainRepository.getLiveNoteById(value) { notesSharing ->
            if(notesSharing!=null){
                searchTitle.value= notesSharing.title
                searchLiveText.value=notesSharing.description
            }
            else{
                println("empty id")
            }
        }
    }

    fun startLiveObservation(value: String) {
        if (value.isEmpty()) return
        MainRepository.observeLiveNoteById(value) { notesSharing ->
            if (notesSharing != null) {
                searchTitle.value = notesSharing.title
                searchLiveText.value = notesSharing.description
            }
        }
    }

    fun liveNotesSharing(userViewModel: UserViewModel, title: String, desc:String){
        MainRepository.writeLiveNote(userViewModel.userId.value, LiveNoteSharing(userViewModel.userId.value, title, desc))
    }
}
