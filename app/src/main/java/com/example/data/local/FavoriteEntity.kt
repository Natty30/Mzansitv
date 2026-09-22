package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_channels")
data class FavoriteEntity(
    @PrimaryKey
    val channelId: String,
    val addedAt: Long = System.currentTimeMillis()
)
