package com.mightsana.goodminton.features.main.main

import android.app.Application
import com.mightsana.goodminton.MyViewModel
import com.mightsana.goodminton.features.main.model.InvitationJoint
import com.mightsana.goodminton.model.repository.AppRepository
import com.mightsana.goodminton.model.repository.friend_requests.FriendRequestJoint
import com.mightsana.goodminton.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountService: AccountService,
    appRepository: AppRepository,
    application: Application
): MyViewModel(accountService, appRepository, application) {

    private val _selectedItem: MutableStateFlow<Any> = MutableStateFlow(Home)
    val selectedItem = _selectedItem.asStateFlow()

    fun onSelectItem(item: Any) {
        _selectedItem.value = item
    }

    private val _friendRequestReceived = MutableStateFlow(listOf<FriendRequestJoint>())
    val friendRequestReceived = _friendRequestReceived.asStateFlow()

    private val _invitationReceived = MutableStateFlow(listOf<InvitationJoint>())
    val invitationReceived = _invitationReceived.asStateFlow()

    private fun observeFriendRequests() {
        appRepository.observeFriendRequestsJoint(
            userId = accountService.currentUserId,
            onFriendRequestsReceivedUpdate = {
                _friendRequestReceived.value = it
            }
        )
    }

    private fun observeInvitations() {
        appRepository.observeLeagueInvitationsReceivedJoint(
            userId = accountService.currentUserId,
            onInvitationsReceivedUpdate = {
                _invitationReceived.value = it
            }
        )
    }

    init {
        observeFriendRequests()
        observeInvitations()
    }
}

sealed class FormValidationResult {
    data object Valid: FormValidationResult()

    sealed class NewLeagueResult: FormValidationResult() {
        data class NameError(val message: String): NewLeagueResult()
        data class MatchPointsError(val message: String): NewLeagueResult()
    }
}