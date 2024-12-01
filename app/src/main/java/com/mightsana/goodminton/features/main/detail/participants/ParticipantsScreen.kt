package com.mightsana.goodminton.features.main.detail.participants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.mightsana.goodminton.features.main.detail.DetailViewModel
import com.mightsana.goodminton.features.main.model.Role
import com.mightsana.goodminton.view.MyIcon
import com.mightsana.goodminton.view.MyImage

@Composable
@Suppress("unused")
fun ParticipantsScreen(viewModel: DetailViewModel) {
    val uid = viewModel.user.collectAsState().value.uid
    val participantsUI by viewModel.leagueParticipantsUI.collectAsState()
    val roleDropdownExpanded by viewModel.participantsRoleExpanded.collectAsState()
    val currentUserRole = participantsUI.find { it.user.uid == uid }?.info?.role

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 150.dp)
    ) {

        participantsUI
            .sortedWith(
                compareBy(
                    { it.info.role.ordinal },
                    { it.user.name }
                )
            )
            .forEachIndexed { index, participant ->
                val isDropdownExpanded = roleDropdownExpanded[participant.info.userId] ?: false

                ListItem(
                    leadingContent = {
                        MyImage(
                            model = participant.user.profilePhotoUrl,
                            modifier = Modifier.width(40.dp).clip(CircleShape).aspectRatio(1f)
                        )
                    },
                    headlineContent = {
                        Text(
                            text = participant.user.name,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium,
                            overflow = Ellipsis
                        )
                    },
                    supportingContent = {
                        Text(
                            text = participant.user.username,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleSmall,
                            overflow = Ellipsis
                        )
                    },
                    trailingContent = {
                        if (currentUserRole == Role.Creator) {
                            OutlinedButton(
                                onClick = { viewModel.toggleParticipantsRoleExpanded(participant.user.uid) },
                                contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                                enabled = participant.info.role != Role.Creator
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(participant.info.role.name)
                                    MyIcon(if (isDropdownExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown)
                                }
                            }
                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = {
                                    viewModel.dismissParticipantsRoleExpanded(
                                        participant.user.uid
                                    )
                                }
                            ) {
                                Role.entries.forEach { role ->

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                role.name,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.titleSmall,
                                                overflow = Ellipsis
                                            )
                                        },
                                        onClick = {
                                            viewModel.changeParticipantRole(
                                                leagueId = participant.info.leagueId,
                                                userId = participant.info.userId,
                                                newRole = role.name
                                            )
                                            viewModel.dismissParticipantsRoleExpanded(participant.user.uid)
                                        },
                                        enabled = role != Role.Creator || role != participant.info.role
                                    )
                                }
                            }
                        } else {
                            Text(
                                participant.info.role.name,
                                maxLines = 1,
                                style = MaterialTheme.typography.titleSmall,
                                overflow = Ellipsis
                            )
                        }
                    }
                )
                Text("League ID: ${participant.info.leagueId}", maxLines = 1)
                Text("Uid: ${participant.info.id}", maxLines = 1)
                Text("Uid: ${participant.info.id}", maxLines = 1)
//            Text("Name: ${participant.user.name}", maxLines = 1)
//            Text("Nickname: ${participant.user.nickname}", maxLines = 1)
//            Text("Username: ${participant.user.username}", maxLines = 1)
//            Text("Email: ${participant.user.email}", maxLines = 1)
//            Text("Photo URL: ${participant.user.profilePhotoUrl}", maxLines = 1)
//            Text("Phone Number: ${participant.user.phoneNumber}", maxLines = 1)
//            Text("Bio: ${participant.user.bio}", maxLines = 1)
//            Text("Birthday: ${participant.user.birthDate.toString()}", maxLines = 1)
//            Text("Gender: ${participant.user.gender}", maxLines = 1)
//            Text("Address: ${participant.user.address}", maxLines = 1)
//            Text("Created At: ${participant.user.createdAt}", maxLines = 1)
//            Text("Is Verified: ${participant.user.verified}", maxLines = 1)
//            Text("\n")
            }
    }
}

