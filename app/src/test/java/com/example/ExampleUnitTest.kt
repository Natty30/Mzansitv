package com.example

import com.example.data.model.ChannelCatalog
import com.example.data.model.ChannelCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun channelCatalog_hasAllCoreMzansiChannels() {
        val channels = ChannelCatalog.channels
        assertEquals(10, channels.size)

        val channelIds = channels.map { it.id }
        assertTrue(channelIds.contains("sabc_news"))
        assertTrue(channelIds.contains("sabc_1"))
        assertTrue(channelIds.contains("sabc_2"))
        assertTrue(channelIds.contains("sabc_3"))
        assertTrue(channelIds.contains("sabc_sport"))
        assertTrue(channelIds.contains("sabc_education"))
        assertTrue(channelIds.contains("supersport_schools"))
        assertTrue(channelIds.contains("sportscast_africa"))
        assertTrue(channelIds.contains("brics_africa"))
        assertTrue(channelIds.contains("wildearth_sa"))
    }

    @Test
    fun channelCatalog_streamUrlsAreValidHls() {
        for (channel in ChannelCatalog.channels) {
            assertTrue(
                "Channel ${channel.name} must have a valid .m3u8 stream URL",
                channel.streamUrl.endsWith(".m3u8")
            )
            assertNotNull(channel.category)
            assertTrue(channel.name.isNotBlank())
        }
    }
}
