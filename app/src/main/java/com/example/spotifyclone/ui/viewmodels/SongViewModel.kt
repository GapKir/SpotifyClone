package com.example.spotifyclone.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.exoplayer.MusicService
import com.example.spotifyclone.exoplayer.MusicServiceConnection
import com.example.spotifyclone.exoplayer.currentPlaybackPosition
import com.example.spotifyclone.other.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel(){

    private val playbackState = musicServiceConnection.playbackState

    private val _curSongDuration = MutableLiveData<Long>()
    val curSongDuration: LiveData<Long> = _curSongDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition


    init {
        updateCurPlayerPosition()
    }

    private fun updateCurPlayerPosition(){
        viewModelScope.launch {
            while (isActive){
                val pos = playbackState.value?.currentPlaybackPosition ?: 0L
                if (curPlayerPosition.value != pos){
                    _curPlayerPosition.postValue(pos)
                    _curSongDuration.postValue(MusicService.curSongDuration)
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }
}