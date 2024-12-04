package com.mightsana.goodminton.features.profile.other_profile

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mightsana.goodminton.R
import com.mightsana.goodminton.model.ext.onLongPress
import com.mightsana.goodminton.model.ext.onTap
import com.mightsana.goodminton.view.Loader
import com.mightsana.goodminton.view.MyIcon
import com.mightsana.goodminton.view.MyIcons
import com.mightsana.goodminton.view.MyImage
import com.mightsana.goodminton.view.MyTextField
import com.mightsana.goodminton.view.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileScreen(
    uid: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: OtherProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.observeOther(uid)
    }

    val otherUser by viewModel.otherUser.collectAsState()
    val friendsJoint by viewModel.friendsJoint.collectAsState()
    val currentUser by viewModel.user.collectAsState()
    val friendRequestsReceived by viewModel.friendRequestReceived.collectAsState()
    val friendRequestsSent by viewModel.friendRequestSent.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val isFriend = friendsJoint.any { it.user.uid == currentUser.uid }
    val isFriendRequested = friendRequestsSent.any {
        Log.d("OtherProfileScreen", "isFriendRequested: ${it.receiver.uid} & ${otherUser.uid}")
        it.receiver.uid == otherUser.uid
    }
    val isFriendRequestReceived = friendRequestsReceived.any { it.sender.uid == otherUser.uid }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val profilePictureExpanded by viewModel.profilePictureExpanded.collectAsState()
    val imagePosition = remember { mutableStateOf(IntOffset(0, 0)) }
    val imageExpandedDuration = 600
    val imageMinWidth = 100.dp
    val imageMaxWidth = 400.dp
    val expandedImageWidth by animateDpAsState(
        targetValue = if (profilePictureExpanded) imageMaxWidth else imageMinWidth,
        animationSpec = tween(durationMillis = imageExpandedDuration),
        label = ""
    )
    val blurRadius by animateDpAsState(
        targetValue = if(profilePictureExpanded) 15.dp else 0.dp,
        animationSpec = tween(durationMillis = imageExpandedDuration),
        label = ""
    )
    val imageAlpha by animateFloatAsState(
        targetValue = if (profilePictureExpanded) 0f else 1f,
        animationSpec = tween(durationMillis = imageExpandedDuration),
        label = ""
    )

    Loader(viewModel.isLoading.collectAsState().value) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .blur(radius = blurRadius),
            topBar = {
                MyTopBar(
                    title = {
                        Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = otherUser.username)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            }
                        ) { MyIcon(MyIcons.Back) }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if(otherUser.profilePhotoUrl == null)
                            MyImage(
                                painter = painterResource(R.drawable.google_logo),
                                modifier = Modifier
                                    .width(imageMinWidth)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                            )
                        else
                            MyImage(
                                model = otherUser.profilePhotoUrl,
                                modifier = Modifier
                                    .width(imageMinWidth)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .onGloballyPositioned { coordinates ->
                                        imagePosition.value = IntOffset(
                                            x = coordinates.positionInRoot().x.toInt(),
                                            y = coordinates.positionInRoot().y.toInt()
                                        )
                                    }
                                    .onLongPress { viewModel.expandProfilePicture() }
                                    .alpha(imageAlpha)
                            )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.onTap {
                                    if (friendsJoint.size > 0)
//                                        navController.navigateSingleTop("$FRIEND_LIST/${profile.uid}/${profile.username}")
                                        viewModel.comingSoon()
                                }
                            ) {
                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "${friendsJoint.size}")
                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Friends")
                            }
                        }
                    }

                }

                item {
                    Column {
                        Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = otherUser.name)
                        Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "~ ${otherUser.nickname} ~", style = MaterialTheme.typography.titleMedium, fontStyle = FontStyle.Italic)
                    }
                }
                item {
                    if (isFriend) {
                        Button(
                            onClick = {
                                viewModel.showDialog()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MyIcon(MyIcons.Minus)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Unfriend")
                        }
                    } else if (isFriendRequestReceived) {

                        Button(
                            enabled = !isProcessing,
                            onClick = {
//                                viewModel.cancelFriendRequest()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MyIcon(MyIcons.Cancel)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Cancel")
                        }
                    }
                    Log.d("OtherProfileScreen", "isFriendRequestReceived: $isFriendRequestReceived")
                    Log.d("OtherProfileScreen", "isFriendRequested: $isFriendRequested")
//                    } else if (isFriendRequestReceived) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            MyButton(
//                                enabled = !viewModel.isProcessing.collectAsState().value,
//                                onClick = {
//                                    viewModel.acceptFriendRequest()
//                                },
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Accept")
//                            }
//                            Button(
//                                enabled = !viewModel.isProcessing.collectAsState().value,
//                                onClick = {
//                                    viewModel.declineFriendRequest()
//                                },
//                                modifier = Modifier.weight(1f)
//                            ) {
//                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Decline")
//                            }
//                        }
//                    } else if (isFriendRequested) {
//                        TertiaryButton(
//                            enabled = !viewModel.isProcessing.collectAsState().value,
//                            onClick = {
//                                viewModel.cancelFriendRequest()
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            MyIcon(MyIcons.Cancel)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Cancel")
//                        }
//                    } else {
//                        MyButton(
//                            enabled = !viewModel.isProcessing.collectAsState().value,
//                            onClick = {
//                                viewModel.sendFriendRequest()
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            MyIcon(MyIcons.Plus)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Request")
//                        }
//                    }
                }
                item {
                    MyTextField(
                        value = otherUser.bio ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        placeholder = { Text("No bio yet. 😊") },
                        minLines = 3,
                        singleLine = false
                    )
                }

            }
            AnimatedVisibility(viewModel.dialogVisible.collectAsState().value) {
                AlertDialog(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    onDismissRequest = {
                        viewModel.hideDialog()
                    },
                    text = {
                        Text("Are you sure you want to unfriend ${otherUser.username}?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
//                                viewModel.unfriend()
                                viewModel.hideDialog()
                            }
                        ) {
                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Unfriend")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                viewModel.hideDialog()
                            }
                        ) {
                            Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "Cancel")
                        }
                    }
                )
            }
        }
        AnimatedVisibility(
            profilePictureExpanded,
            enter = fadeIn(
//                initialOffset = { fullSize ->
//                    IntOffset(
//                        x = imagePosition.value.x - fullSize.width / 2 + imageMinWidth.value.toInt() / 2,
//                        y = imagePosition.value.y - fullSize.height / 2 + imageMinWidth.value.toInt() / 2
//                    )
//                },
                animationSpec = tween(durationMillis = imageExpandedDuration)
            ),
//            exit = slideOut(
//                targetOffset = { fullSize ->
//                    IntOffset(
//                        x = - fullSize.width / 2 + imagePosition.value.x + imageMinWidth.value.toInt() / 2,
//                        y = - fullSize.height / 2 + imagePosition.value.y + imageMinWidth.value.toInt() / 2
//                    )
//                },
//                animationSpec = tween(durationMillis = imageExpandedDuration)
//            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = imageExpandedDuration)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onTap {
                        viewModel.dismissProfilePicture()
                    },
                contentAlignment = Alignment.Center
            ) {
                MyImage(
                    model = otherUser.profilePhotoUrl,
                    modifier = Modifier
                        .width(expandedImageWidth)
                        .padding(horizontal = 16.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable(false) {}
                )
            }
        }
    }
}