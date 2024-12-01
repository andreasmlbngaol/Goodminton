package com.mightsana.goodminton.features.main.detail.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mightsana.goodminton.features.main.detail.DetailViewModel

@Composable
fun StandingsScreen(
    viewModel: DetailViewModel
) {
    val participantsUI by viewModel.leagueParticipantsUI.collectAsState()

//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        itemsIndexed(participantsUI.sortedByDescending { it.stats.wins }) { index, item ->
//            ListItem(
//                headlineContent = {
//                    Text("${index + 1}. ${item.user.nickname}")
//                }
//            )
//        }
//    }
    val tableData = participantsUI.sortedByDescending { it.stats.wins }.mapIndexed { index, item ->
        TableData(
            position = index + 1,
            name = item.user.name,
            nickname = item.user.nickname,
            matches = item.stats.matches,
            wins = item.stats.wins,
            losses = item.stats.losses,
            pointsScored = item.stats.pointsScored,
            pointsConceded = item.stats.pointsConceded,
            pointsDifference = item.stats.pointsScored - item.stats.pointsConceded
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Standings
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text("No", style = MaterialTheme.typography.titleMedium)
            }
            items(tableData) {
                Text(it.position.toString())
            }
        }

        // Name
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.widthIn(max = 160.dp)
        ) {
            item {
                Text("Name", style = MaterialTheme.typography.titleMedium)
            }
            items(tableData) {
                Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }

        // Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("Match", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.matches.toString())
                    }
                }
            }
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("Win", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.wins.toString())
                    }
                }
            }
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("Losses", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.losses.toString())
                    }
                }
            }
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("PS", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.pointsScored.toString())
                    }
                }
            }
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("PC", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.pointsConceded.toString())
                    }
                }
            }
            item {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text("PD", style = MaterialTheme.typography.titleMedium)
                    }
                    items(tableData) {
                        Text(it.pointsDifference.toString())
                    }
                }
            }
        }
    }
}

data class TableData(
    val position: Int,
    val name: String,
    val nickname: String,
    val matches: Int,
    val wins: Int,
    val losses: Int,
    val pointsScored: Int,
    val pointsConceded: Int,
    val pointsDifference: Int
)