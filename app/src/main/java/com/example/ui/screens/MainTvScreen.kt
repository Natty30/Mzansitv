package com.example.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ads.AdMobManager
import com.example.data.model.ChannelCategory
import com.example.ui.components.BannerAdView
import com.example.ui.components.ChannelCard
import com.example.ui.components.PrivacyPolicyDialog
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.BackgroundObsidian
import com.example.ui.theme.LiveRed
import com.example.ui.theme.MzansiEmerald
import com.example.ui.theme.MzansiGold
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.TvViewModel

@Composable
fun MainTvScreen(
    viewModel: TvViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    // Pulsing animation for header LIVE badge
    val infiniteTransition = rememberInfiniteTransition(label = "HeaderLivePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HeaderPulseAlpha"
    )

    // Intercept back button when in fullscreen
    if (uiState.isFullscreen) {
        BackHandler {
            viewModel.setFullscreen(false)
        }
    }

    if (uiState.isFullscreen) {
        // Fullscreen player view
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            VideoPlayerView(
                player = viewModel.player,
                channel = uiState.selectedChannel,
                isPlaying = uiState.isPlaying,
                isMuted = uiState.isMuted,
                isFullscreen = true,
                isBuffering = uiState.isBuffering,
                playbackError = uiState.playbackError,
                onTogglePlayPause = viewModel::togglePlayPause,
                onToggleMute = viewModel::toggleMute,
                onToggleFullscreen = viewModel::toggleFullscreen,
                onRetry = viewModel::retryPlayback,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundObsidian),
            containerColor = BackgroundObsidian,
            bottomBar = {
                BannerAdView(modifier = Modifier.fillMaxWidth())
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // App Header
                Surface(
                    color = Color(0xFF11141C),
                    border = BorderStroke(1.dp, SurfaceCardHighlight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Brand Title & LIVE indicator
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_tv_logo),
                                    contentDescription = "Mzansi TV logo",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Mzansi TV",
                                            color = TextPrimary,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // Pulse indicator
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = LiveRed.copy(alpha = 0.2f),
                                            border = BorderStroke(0.8.dp, LiveRed)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .alpha(pulseAlpha)
                                                        .background(LiveRed, CircleShape)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = stringResource(R.string.status_live),
                                                    color = LiveRed,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = stringResource(R.string.header_title),
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Header Actions
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Privacy Policy button
                                IconButton(
                                    onClick = { showPrivacyDialog = true },
                                    modifier = Modifier.testTag("privacy_policy_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PrivacyTip,
                                        contentDescription = "Privacy Policy and Consent",
                                        tint = TextSecondary
                                    )
                                }

                                // Favorites filter toggle
                                IconButton(
                                    onClick = viewModel::toggleFavoritesOnly,
                                    modifier = Modifier.testTag("filter_favorites_button")
                                ) {
                                    Icon(
                                        imageVector = if (uiState.showFavoritesOnly) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "Toggle Favorites",
                                        tint = if (uiState.showFavoritesOnly) MzansiGold else TextSecondary
                                    )
                                }

                                // Search toggle
                                IconButton(
                                    onClick = {
                                        isSearchExpanded = !isSearchExpanded
                                        if (!isSearchExpanded) {
                                            viewModel.setSearchQuery("")
                                        }
                                    },
                                    modifier = Modifier.testTag("toggle_search_button")
                                ) {
                                    Icon(
                                        imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                        contentDescription = "Toggle Search",
                                        tint = if (isSearchExpanded) MzansiGold else TextSecondary
                                    )
                                }
                            }
                        }

                        // Search input bar
                        AnimatedVisibility(
                            visible = isSearchExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = viewModel::setSearchQuery,
                                placeholder = {
                                    Text(
                                        text = "Search South Africa channels...",
                                        color = TextTertiary,
                                        fontSize = 13.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = MzansiGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (uiState.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear search",
                                                tint = TextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MzansiGold,
                                    unfocusedBorderColor = SurfaceCardHighlight,
                                    focusedContainerColor = SurfaceCard,
                                    unfocusedContainerColor = SurfaceCard,
                                    cursorColor = MzansiGold,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .testTag("search_channels_field")
                            )
                        }
                    }
                }

                // Main Content (Player + Channel List) in Scrollable Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Video Player Item spanning both columns
                    item(span = { GridItemSpan(2) }) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, SurfaceCardHighlight),
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        ) {
                            VideoPlayerView(
                                player = viewModel.player,
                                channel = uiState.selectedChannel,
                                isPlaying = uiState.isPlaying,
                                isMuted = uiState.isMuted,
                                isFullscreen = false,
                                isBuffering = uiState.isBuffering,
                                playbackError = uiState.playbackError,
                                onTogglePlayPause = viewModel::togglePlayPause,
                                onToggleMute = viewModel::toggleMute,
                                onToggleFullscreen = viewModel::toggleFullscreen,
                                onRetry = viewModel::retryPlayback,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Now Playing Info Box spanning both columns
                    item(span = { GridItemSpan(2) }) {
                        val currentChannel = uiState.selectedChannel
                        val isFavorite = uiState.favoriteIds.contains(currentChannel.id)

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = BorderStroke(1.dp, SurfaceCardHighlight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = currentChannel.name,
                                                color = TextPrimary,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MzansiGold.copy(alpha = 0.2f),
                                                border = BorderStroke(0.6.dp, MzansiGold)
                                            ) {
                                                Text(
                                                    text = currentChannel.category.displayName,
                                                    color = MzansiGold,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = currentChannel.slogan,
                                            color = MzansiEmerald,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Reconnect Stream
                                        IconButton(
                                            onClick = viewModel::retryPlayback,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Reconnect stream",
                                                tint = TextSecondary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        // Favorite button
                                        IconButton(
                                            onClick = { viewModel.toggleFavorite(currentChannel.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                                contentDescription = "Favorite current channel",
                                                tint = if (isFavorite) MzansiGold else TextSecondary,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = currentChannel.description,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(currentChannel.broadcastLanguage, fontSize = 10.sp) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            labelColor = TextTertiary,
                                            containerColor = Color.Transparent
                                        ),
                                        border = BorderStroke(0.6.dp, SurfaceCardHighlight),
                                        modifier = Modifier.height(26.dp)
                                    )

                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Stream: HLS Live", fontSize = 10.sp) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            labelColor = MzansiGold,
                                            containerColor = Color.Transparent
                                        ),
                                        border = BorderStroke(0.6.dp, SurfaceCardHighlight),
                                        modifier = Modifier.height(26.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Category Filter Section spanning both columns
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChannelCategory.entries.forEach { category ->
                                val isCatSelected = !uiState.showFavoritesOnly && uiState.selectedCategory == category

                                FilterChip(
                                    selected = isCatSelected,
                                    onClick = { viewModel.setCategory(category) },
                                    label = {
                                        Text(
                                            text = if (category == ChannelCategory.ALL) "All (${uiState.filteredChannels.size})" else category.displayName,
                                            fontSize = 11.sp,
                                            fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MzansiGold,
                                        selectedLabelColor = Color.Black,
                                        containerColor = SurfaceCard,
                                        labelColor = TextSecondary
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isCatSelected) MzansiGold else SurfaceCardHighlight
                                    ),
                                    modifier = Modifier.height(32.dp)
                                )
                            }
                        }
                    }

                    // Channel Grid Items
                    if (uiState.filteredChannels.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Tv,
                                        contentDescription = "No channels",
                                        tint = TextTertiary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = if (uiState.showFavoritesOnly) "No favorite channels yet" else "No channels match your filter",
                                        color = TextSecondary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    TextButton(
                                        onClick = {
                                            viewModel.setCategory(ChannelCategory.ALL)
                                            viewModel.setSearchQuery("")
                                            if (uiState.showFavoritesOnly) viewModel.toggleFavoritesOnly()
                                        }
                                    ) {
                                        Text("Show All Channels", color = MzansiGold, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    } else {
                        items(uiState.filteredChannels, key = { it.id }) { channel ->
                            ChannelCard(
                                channel = channel,
                                isSelected = uiState.selectedChannel.id == channel.id,
                                isFavorite = uiState.favoriteIds.contains(channel.id),
                                onSelect = {
                                    if (activity != null) {
                                        AdMobManager.showInterstitialIfAllowed(activity) {
                                            viewModel.selectChannel(channel)
                                        }
                                    } else {
                                        viewModel.selectChannel(channel)
                                    }
                                },
                                onToggleFavorite = { viewModel.toggleFavorite(channel.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPrivacyDialog) {
        PrivacyPolicyDialog(
            onDismissRequest = { showPrivacyDialog = false }
        )
    }
}
