package com.mightsana.goodminton.features.profile.other_profile

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mightsana.goodminton.features.profile.ProfileViewModel
import com.mightsana.goodminton.model.repository.AppRepository
import com.mightsana.goodminton.model.repository.friend_requests.FriendRequestJoint
import com.mightsana.goodminton.model.repository.users.MyUser
import com.mightsana.goodminton.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    accountService: AccountService,
    appRepository: AppRepository,
    application: Application
): ProfileViewModel(accountService, appRepository, application) {
    private val _otherUser = MutableStateFlow(MyUser())
    val otherUser = _otherUser.asStateFlow()

    private fun observeOtherUser(uid: String) {
        appRepository.observeUserJoint(uid) {
            _otherUser.value = it
        }
    }

    private val _dialogVisible = MutableStateFlow(false)
    val dialogVisible = _dialogVisible.asStateFlow()

    fun showDialog() {
        _dialogVisible.value = true
    }

    fun hideDialog() {
        _dialogVisible.value = false
    }

    private val _friendRequestReceived = MutableStateFlow(listOf<FriendRequestJoint>())
    val friendRequestReceived = _friendRequestReceived.asStateFlow()

    private val _friendRequestSent = MutableStateFlow(listOf<FriendRequestJoint>())
    val friendRequestSent = _friendRequestSent.asStateFlow()

    init {
        appLoading()
        observeUser(accountService.currentUserId)
        appRepository.observeFriendRequestsJoint(
            userId = accountService.currentUserId,
            onFriendRequestsSentUpdate = {
                _friendRequestSent.value = it
            },
            onFriendRequestsReceivedUpdate = {
                _friendRequestReceived.value = it
            }
        )
    }

    fun observeOther(uid: String) {
        observeOtherUser(uid)
        observeFriendsJoint(uid)
        appLoaded()
    }

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    fun acceptFriendRequest() {
        viewModelScope.launch {
            val request = _friendRequestReceived.value.find { it.sender.uid == otherUser.value.uid }
            request?.let {
                appRepository.acceptFriendRequest(
                    requestId = it.id,
                    userIds = listOf(request.sender.uid, request.receiver.uid)
                )
            }
        }
    }


}
