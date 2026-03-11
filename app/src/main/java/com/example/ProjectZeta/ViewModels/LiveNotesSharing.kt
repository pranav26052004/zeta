package com.example.ProjectZeta.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
}