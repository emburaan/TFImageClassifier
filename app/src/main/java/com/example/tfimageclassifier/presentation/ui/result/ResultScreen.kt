package com.example.tfimageclassifier.presentation.ui.result

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfimageclassifier.domain.model.ClassificationResult
import com.example.tfimageclassifier.presentation.viewmodel.ClassifierUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    uiState: ClassifierUiState.Success?,
    onNavigateBack: () -> Unit
) {
    BackHandler {
        onNavigateBack.invoke()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Results",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No results available.")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "Top ${uiState.results.size} Predictions",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            itemsIndexed(uiState.results) { index, result ->
                AnimatedResultCard(
                    result = result,
                    rank = index + 1,
                    animationDelay = index * 80L
                )
            }
        }
    }
}

// ── Result card with staggered entrance animation ─────────────────────────────

@Composable
private fun AnimatedResultCard(
    result: ClassificationResult,
    rank: Int,
    animationDelay: Long
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInHorizontally(
            animationSpec = tween(300, easing = EaseOutCubic),
            initialOffsetX = { it / 2 }
        )
    ) {
        ResultCard(result = result, rank = rank)
    }
}

@Composable
private fun ResultCard(
    result: ClassificationResult,
    rank: Int
) {
    // Animated progress bar
    val animatedScore by animateFloatAsState(
        targetValue = result.score,
        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
        label = "score_anim"
    )

    val isTop = rank == 1
    val cardColor = if (isTop)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isTop) 4.dp else 1.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rank badge
                RankBadge(rank = rank, isTop = isTop)

                Spacer(Modifier.width(12.dp))

                // Label
                Text(
                    text = result.label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (isTop) FontWeight.Bold else FontWeight.Medium,
                        fontSize = if (isTop) 17.sp else 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                // Score percent
                Text(
                    text = result.scorePercent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isTop)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(10.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedScore)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isTop)
                                Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            else
                                Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                    )
                                )
                        )
                )
            }
        }
    }
}

@Composable
private fun RankBadge(rank: Int, isTop: Boolean) {
    val bgColor = if (isTop)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.secondaryContainer

    val textColor = if (isTop)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "#$rank",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor
        )
    }
}
