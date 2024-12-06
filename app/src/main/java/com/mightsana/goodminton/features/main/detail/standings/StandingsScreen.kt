package com.mightsana.goodminton.features.main.detail.standings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mightsana.goodminton.features.main.detail.DetailViewModel
import com.mightsana.goodminton.features.main.model.ParticipantStatsJoint
import com.mightsana.goodminton.model.values.Size
import com.mightsana.goodminton.view.Tables

@Suppress("unused")
@Composable
fun StandingsScreen(
    viewModel: DetailViewModel
) {
    val participantsStats by viewModel.participantsStats.collectAsState()
    val tableData = participantsStats
        .sortedWith(
            compareByDescending<ParticipantStatsJoint> { it.wins }
                .thenBy { it.matches }
                .thenBy { it.losses }
                .thenByDescending { (it.pointsScored - it.pointsConceded) }
                .thenByDescending { it.pointsScored }
                .thenBy { it.pointsConceded }
                .thenBy { it.user.name }
        )
        .mapIndexed { index, it ->
            TableData(
                position = index + 1,
                name = it.user.name,
                matches = it.matches,
                wins = it.wins,
                losses = it.losses,
                pointsScored = it.pointsScored,
                pointsConceded = it.pointsConceded,
                pointsDifference = it.pointsScored - it.pointsConceded
            )
        }
    val tableColumns = listOf("No", "Name", "M", "W", "L", "PS", "PC", "PD")

    Box(
        modifier = Modifier.fillMaxSize().padding(vertical = Size.padding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Tables(
            data = tableData,
            enableTableHeaderTitles = true,
            headerTableTitles = tableColumns,
            headerTitlesTextStyle = MaterialTheme.typography.titleMedium,
            headerTitlesBackGroundColor = MaterialTheme.colorScheme.surfaceVariant,
            columnToIndexIncreaseWidth = 1,
            columnToFontWeightModified = mapOf(3 to FontWeight.ExtraBold),
            columnToColorModified = mapOf(
                2 to MaterialTheme.colorScheme.secondary,
                3 to MaterialTheme.colorScheme.primary,
                7 to MaterialTheme.colorScheme.tertiary
            ),
            rowTextStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = Size.smallPadding)
        )
    }

}

data class TableData(
    val position: Int,
    val name: String,
    val matches: Int,
    val wins: Int,
    val losses: Int,
    val pointsScored: Int,
    val pointsConceded: Int,
    val pointsDifference: Int
)