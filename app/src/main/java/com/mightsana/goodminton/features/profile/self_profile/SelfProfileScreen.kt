package com.mightsana.goodminton.features.profile.self_profile

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mightsana.goodminton.R
import com.mightsana.goodminton.model.ext.onTap
import com.mightsana.goodminton.model.values.Size
import com.mightsana.goodminton.view.Loader
import com.mightsana.goodminton.view.MyIcon
import com.mightsana.goodminton.view.MyIcons
import com.mightsana.goodminton.view.MyImage
import com.mightsana.goodminton.view.MyTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfProfileScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateToFriendList: (String) -> Unit,
    onNavigateToEditProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelfProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val friendsJoint by viewModel.friends.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val profilePictureExpanded by viewModel.profilePictureExpanded.collectAsState()
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
    val scope = rememberCoroutineScope()

    Loader(viewModel.isLoading.collectAsState().value) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .blur(radius = blurRadius),
            topBar = {
                MyTopBar(
                    title = {
                        Text(user.username)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            MyIcon(MyIcons.Back)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.showSignOutDialog() }
                        ) { MyIcon(MyIcons.Logout) }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = Size.padding)
                    .padding(top = Size.smallPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Size.smallPadding)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Size.padding)
                    ) {
                        if(user.profilePhotoUrl == null)
                            MyImage(
                                painter = painterResource(R.drawable.google_logo),
                                modifier = Modifier
                                    .width(imageMinWidth)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                            )
                        else
                            MyImage(
                                model = user.profilePhotoUrl,
                                modifier = Modifier
                                    .width(imageMinWidth)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .onTap { viewModel.expandProfilePicture() }
                                    .alpha(imageAlpha)
                            )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Size.smallPadding),
                                modifier = Modifier
                                    .onTap {
                                        if (friendsJoint.isNotEmpty())
                                            onNavigateToFriendList(user.uid)
                                    }
                            ) {
                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = "${friendsJoint.size}")
                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = stringResource(R.string.friends))
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .onTap {
                                        if (friendsJoint.isNotEmpty())
                                            onNavigateToFriendList(user.uid)
                                    }
                            ) {
                                Switch(
                                    checked = user.openToAdd,
                                    onCheckedChange = { viewModel.updateOpenToAdd(it) }
                                )
                                Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = stringResource(R.string.public_text))
                            }
                        }
                    }
                }
                item {
                    Column {
                        Text(user.name)
                        Text("~ ${user.nickname} ~", style = MaterialTheme.typography.titleMedium, fontStyle = FontStyle.Italic)
                    }
                }
                item {
                    Button(
                        onClick = onNavigateToEditProfile,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MyIcon(MyIcons.Edit)
                        Spacer(modifier = Modifier.width(Size.smallPadding))
                        Text(stringResource(R.string.edit_profile))
                    }
                }
                item {
                    val containerColor = MaterialTheme.colorScheme.secondaryContainer
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Size.smallPadding),
                        colors = CardDefaults.cardColors().copy(
                            containerColor = containerColor,
                            contentColor = contentColorFor(containerColor)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .heightIn(min = 150.dp)
                                .padding(horizontal = Size.padding)
                                .padding(vertical = Size.smallPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = user.bio ?: stringResource(R.string.no_bio),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            profilePictureExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = imageExpandedDuration)),
            exit = fadeOut(animationSpec = tween(durationMillis = imageExpandedDuration))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onTap { viewModel.dismissProfilePicture() },
                contentAlignment = Alignment.Center
            ) {
                MyImage(
                    model = user.profilePhotoUrl,
                    modifier = Modifier
                        .width(expandedImageWidth)
                        .padding(horizontal = Size.padding)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable(false) {}
                )
            }
        }
        AnimatedVisibility(viewModel.signOutDialogVisible.collectAsState().value) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissSignOutDialog() },
                modifier = Modifier
                    .padding(horizontal = Size.padding),
                text = {
                    Text(stringResource(R.string.sign_out_text))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.dismissSignOutDialog()
                                delay(1000L)
                                viewModel.onSignOut(onSignOut)
                            }
                        },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = stringResource(R.string.sign_out_button_label))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.dismissSignOutDialog()
                        }
                    ) {
                        Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
