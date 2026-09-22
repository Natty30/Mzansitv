package com.example.ui.viewmodel

import android.app.Application
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.data.local.AppDatabase
import com.example.data.model.Channel
import com.example.data.model.ChannelCatalog
import com.example.data.model.ChannelCategory
import com.example.data.repository.ChannelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TvUiState(
    val allChannels: List<Channel> = ChannelCatalog.channels,
    val selectedChannel: Channel = ChannelCatalog.channels.first(),
    val filteredChannels: List<Channel> = ChannelCatalog.channels,
    val selectedCategory: ChannelCategory = ChannelCategory.ALL,
    val searchQuery: String = "",
    val showFavoritesOnly: Boolean = false,
    val favoriteIds: Set<String> = emptySet(),
    val isPlaying: Boolean = true,
    val isMuted: Boolean = false,
    val isFullscreen: Boolean = false,
    val isBuffering: Boolean = false,
    val playbackError: String? = null,
    val retryCount: Long = 0L
)

@OptIn(UnstableApi::class)
class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChannelRepository(
        AppDatabase.getInstance(application).favoriteDao()
    )

    private var autoRetryAttempts = 0
    private val maxAutoRetries = 2

    // HTTP Data Source with desktop/mobile compliant User-Agent, redirects and headers
    private val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
        .setConnectTimeoutMs(15000)
        .setReadTimeoutMs(15000)
        .setAllowCrossProtocolRedirects(true)
        .setKeepPostFor302Redirects(true)
        .setDefaultRequestProperties(
            mapOf(
                "Referer" to "https://sabc-plus.com/",
                "Origin" to "https://sabc-plus.com"
            )
        )

    private val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

    // Single shared ExoPlayer instance with decoder fallback enabled
    val player: ExoPlayer = run {
        val renderersFactory = DefaultRenderersFactory(application)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
            .setEnableDecoderFallback(true)

        ExoPlayer.Builder(application, renderersFactory)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                playWhenReady = true
            }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    _uiState.update { it.copy(isBuffering = true, playbackError = null) }
                }
                Player.STATE_READY -> {
                    autoRetryAttempts = 0
                    _uiState.update { it.copy(isBuffering = false, playbackError = null) }
                }
                Player.STATE_ENDED -> {
                    _uiState.update { it.copy(isBuffering = false) }
                }
                Player.STATE_IDLE -> {
                    // Player is idle
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            val cause = error.cause
            val channel = _uiState.value.selectedChannel

            val isHttp403 = cause is HttpDataSource.InvalidResponseCodeException && cause.responseCode == 403
            val isHttp404 = cause is HttpDataSource.InvalidResponseCodeException && cause.responseCode == 404

            val errorMessage = when {
                isHttp403 -> {
                    "Stream unavailable (HTTP 403). ${channel.name} broadcast is geo-restricted to South Africa or requires official SABC+ access."
                }
                isHttp404 -> {
                    "Stream feed currently offline (HTTP 404). Broadcast is temporarily not transmitting."
                }
                cause is HttpDataSource.HttpDataSourceException -> {
                    "Connection failed while streaming ${channel.name}. Please verify network connectivity."
                }
                error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED ||
                error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    "Connection timed out while loading ${channel.name} live stream."
                }
                else -> {
                    "Stream unavailable. ${error.localizedMessage ?: "Unable to connect to live broadcast."}"
                }
            }

            // Automatic retry up to 2 times for transient connection drops (skip if geofence 403)
            if (!isHttp403 && autoRetryAttempts < maxAutoRetries) {
                autoRetryAttempts++
                viewModelScope.launch {
                    delay(1500L * autoRetryAttempts)
                    if (_uiState.value.selectedChannel.id == channel.id) {
                        prepareChannel(channel)
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isBuffering = false,
                        playbackError = errorMessage
                    )
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(
        TvUiState(
            allChannels = repository.allChannels,
            selectedChannel = repository.allChannels.first(),
            filteredChannels = repository.allChannels
        )
    )
    val uiState: StateFlow<TvUiState> = _uiState.asStateFlow()

    init {
        player.addListener(playerListener)
        prepareChannel(_uiState.value.selectedChannel)

        // Collect favorites from Room database and update state reactively
        viewModelScope.launch {
            repository.favoriteIds.collect { favorites ->
                _uiState.update { current ->
                    val updated = current.copy(favoriteIds = favorites)
                    updated.copy(filteredChannels = computeFilteredChannels(updated))
                }
            }
        }
    }

    private fun prepareChannel(channel: Channel) {
        try {
            // Cleanly stop any existing playback and clear media queue to prevent simultaneous streams
            player.stop()
            player.clearMediaItems()

            val mediaItem = MediaItem.Builder()
                .setUri(channel.streamUrl)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = _uiState.value.isPlaying
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isBuffering = false,
                    playbackError = e.localizedMessage ?: "Failed to load channel stream"
                )
            }
        }
    }

    private fun computeFilteredChannels(state: TvUiState): List<Channel> {
        var list = state.allChannels
        if (state.showFavoritesOnly) {
            list = list.filter { state.favoriteIds.contains(it.id) }
        } else if (state.selectedCategory != ChannelCategory.ALL) {
            list = list.filter { it.category == state.selectedCategory }
        }

        if (state.searchQuery.isNotBlank()) {
            list = list.filter {
                it.name.contains(state.searchQuery, ignoreCase = true) ||
                it.description.contains(state.searchQuery, ignoreCase = true) ||
                it.slogan.contains(state.searchQuery, ignoreCase = true)
            }
        }
        return list
    }

    fun selectChannel(channel: Channel) {
        if (_uiState.value.selectedChannel.id != channel.id) {
            autoRetryAttempts = 0
            // Immediately stop previous stream to prevent simultaneous audio/video playback
            player.stop()
            _uiState.update { current ->
                current.copy(
                    selectedChannel = channel,
                    playbackError = null,
                    isBuffering = true,
                    isPlaying = true
                )
            }
            prepareChannel(channel)
        }
    }

    fun togglePlayPause() {
        _uiState.update { current ->
            val newPlaying = !current.isPlaying
            player.playWhenReady = newPlaying
            current.copy(isPlaying = newPlaying)
        }
    }

    fun setPlaying(playing: Boolean) {
        player.playWhenReady = playing
        _uiState.update { it.copy(isPlaying = playing) }
    }

    fun toggleMute() {
        _uiState.update { current ->
            val newMuted = !current.isMuted
            player.volume = if (newMuted) 0f else 1f
            current.copy(isMuted = newMuted)
        }
    }

    fun toggleFullscreen() {
        _uiState.update { it.copy(isFullscreen = !it.isFullscreen) }
    }

    fun setFullscreen(fullscreen: Boolean) {
        _uiState.update { it.copy(isFullscreen = fullscreen) }
    }

    fun setCategory(category: ChannelCategory) {
        _uiState.update { current ->
            val updated = current.copy(
                selectedCategory = category,
                showFavoritesOnly = false
            )
            updated.copy(filteredChannels = computeFilteredChannels(updated))
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { current ->
            val updated = current.copy(searchQuery = query)
            updated.copy(filteredChannels = computeFilteredChannels(updated))
        }
    }

    fun toggleFavoritesOnly() {
        _uiState.update { current ->
            val updated = current.copy(showFavoritesOnly = !current.showFavoritesOnly)
            updated.copy(filteredChannels = computeFilteredChannels(updated))
        }
    }

    fun toggleFavorite(channelId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(channelId, _uiState.value.favoriteIds)
        }
    }

    fun setBuffering(buffering: Boolean) {
        _uiState.update { it.copy(isBuffering = buffering) }
    }

    fun setPlaybackError(error: String?) {
        _uiState.update { it.copy(playbackError = error) }
    }

    fun retryPlayback() {
        autoRetryAttempts = 0
        _uiState.update {
            it.copy(
                playbackError = null,
                isBuffering = true,
                isPlaying = true,
                retryCount = it.retryCount + 1
            )
        }
        prepareChannel(_uiState.value.selectedChannel)
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(playerListener)
        player.release()
    }
}

