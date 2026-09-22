package com.example.data.repository

import com.example.data.local.FavoriteDao
import com.example.data.local.FavoriteEntity
import com.example.data.model.Channel
import com.example.data.model.ChannelCatalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChannelRepository(private val favoriteDao: FavoriteDao) {

    val allChannels: List<Channel> = ChannelCatalog.channels

    val favoriteIds: Flow<Set<String>> = favoriteDao.getAllFavoriteIds().map { it.toSet() }

    suspend fun toggleFavorite(channelId: String, currentFavorites: Set<String>) {
        if (currentFavorites.contains(channelId)) {
            favoriteDao.removeFavorite(channelId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(channelId = channelId))
        }
    }
}
