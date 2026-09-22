package com.example.data.model

enum class ChannelCategory(val displayName: String) {
    ALL("All"),
    NEWS("News"),
    ENTERTAINMENT("Entertainment"),
    SPORTS("Sports"),
    EDUCATION("Education")
}

enum class StreamType(val displayName: String) {
    HLS("HLS (m3u8)"),
    DASH("DASH (mpd)"),
    MP4("MP4 Video"),
    WEB_STREAM("Web Stream")
}

data class Channel(
    val id: String,
    val name: String,
    val category: ChannelCategory,
    val streamUrl: String,
    val streamType: StreamType = StreamType.HLS,
    val officialSourceUrl: String,
    val logoUrl: String,
    val fallbackText: String,
    val description: String,
    val slogan: String,
    val broadcastLanguage: String = "English, isiZulu, isiXhosa, Sesotho",
    val quality: String = "HD 720p",
    val isActive: Boolean = true,
    val isGeoRestricted: Boolean = false
)

object ChannelCatalog {
    val channels: List<Channel> = listOf(
        Channel(
            id = "sabc_news",
            name = "SABC News",
            category = ChannelCategory.NEWS,
            streamUrl = "https://sabconetanw.cdn.mangomolo.com/news/smil:news.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/news",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabcnews.png",
            fallbackText = "SABC News",
            description = "24-hour South African and international breaking news, politics, business, and investigative journalism.",
            slogan = "Independent. Impartial.",
            broadcastLanguage = "English, isiZulu, Sesotho",
            quality = "HD 1080p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "sabc_education",
            name = "SABC Education",
            category = ChannelCategory.EDUCATION,
            streamUrl = "https://sabctretaed.cdn.mangomolo.com/edu/smil:edu.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/education",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabceducation.png",
            fallbackText = "SABC Edu",
            description = "Enriching educational broadcasts, school curriculum revision, STEM learning, and children's knowledge content.",
            slogan = "Enriching Minds, Enriching Lives",
            broadcastLanguage = "English, isiZulu, isiXhosa, Sesotho",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "supersport_schools",
            name = "SuperSport Schools",
            category = ChannelCategory.SPORTS,
            streamUrl = "https://sabctretasch.cdn.mangomolo.com/ssch/smil:ssch.stream.smil/chunklist_b1600000_t64NzIwcA==.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/sport",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/supersport.png",
            fallbackText = "SS Schools",
            description = "Live youth and school sports tournaments across South Africa: rugby, cricket, netball, and athletics.",
            slogan = "Every Game, Every Athlete",
            broadcastLanguage = "English, Afrikaans",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "sportscast_africa",
            name = "SportsCast Africa",
            category = ChannelCategory.SPORTS,
            streamUrl = "https://sabcpluscdn.sportscastafrica.com/sca/smil:sca.stream.smil/chunklist_b1600000_t64NzIwcA==.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/sport",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabcsport.png",
            fallbackText = "SportsCast",
            description = "Pan-African sports commentary, grassroots athletics, local tournaments, and athlete showcases.",
            slogan = "The Pulse of African Sports",
            broadcastLanguage = "English",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "brics_africa",
            name = "BRICS Africa",
            category = ChannelCategory.NEWS,
            streamUrl = "https://sabcbrtanw.cdn.mangomolo.com/br/smil:br.stream.smil/chunklist_b1600000_t64NzIwcA==.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabcnews.png",
            fallbackText = "BRICS TV",
            description = "Dedicated coverage of economic cooperation, trade, cultural diplomacy, and global emerging market news.",
            slogan = "Connecting Emerging Economies",
            broadcastLanguage = "English",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "wildearth_sa",
            name = "WildEarth Safari",
            category = ChannelCategory.ENTERTAINMENT,
            streamUrl = "https://wildearth-xumo.amagi.tv/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://wildearth.tv",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/wildearth.png",
            fallbackText = "WildEarth",
            description = "Live interactive wildlife safaris broadcast 24/7 directly from Djuma Game Reserve and Greater Kruger Park.",
            slogan = "Connecting People with Nature",
            broadcastLanguage = "English",
            quality = "HD 1080p",
            isActive = true,
            isGeoRestricted = false
        ),
        Channel(
            id = "sabc_1",
            name = "SABC 1",
            category = ChannelCategory.ENTERTAINMENT,
            streamUrl = "https://sabconeta.cdn.mangomolo.com/sabc1/smil:sabc1.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/live/320/SABC-1",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabc1.png",
            fallbackText = "SABC 1",
            description = "Mzansi's leading youth and entertainment channel featuring local dramas (Generations, Uzalo), soapies, and music.",
            slogan = "Mzansi Fo Sho",
            broadcastLanguage = "isiZulu, isiXhosa, English, Siswati",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = true
        ),
        Channel(
            id = "sabc_2",
            name = "SABC 2",
            category = ChannelCategory.ENTERTAINMENT,
            streamUrl = "https://sabctwota.cdn.mangomolo.com/sabc2/smil:sabc2.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/live/321/SABC-2",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabc2.png",
            fallbackText = "SABC 2",
            description = "Family-focused programming, cultural documentaries, traditional arts, Muvhango, and multilingual entertainment.",
            slogan = "You Belong",
            broadcastLanguage = "Sesotho, Setswana, Sepedi, Afrikaans, English",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = true
        ),
        Channel(
            id = "sabc_3",
            name = "S3",
            category = ChannelCategory.ENTERTAINMENT,
            streamUrl = "https://sabctreta.cdn.mangomolo.com/sabc3/smil:sabc3.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/live/322/S3",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabc3.png",
            fallbackText = "S3",
            description = "Contemporary urban lifestyle, international drama series, movies, lifestyle magazines, and high-impact talk shows.",
            slogan = "The Stage Is Yours",
            broadcastLanguage = "English",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = true
        ),
        Channel(
            id = "sabc_sport",
            name = "SABC Sport",
            category = ChannelCategory.SPORTS,
            streamUrl = "https://sabctwotasp.cdn.mangomolo.com/sport/smil:sport.stream.smil/master.m3u8",
            streamType = StreamType.HLS,
            officialSourceUrl = "https://sabc-plus.com/live/323/SABC-Sport",
            logoUrl = "https://raw.githubusercontent.com/iptv-org/logos/master/sabcsport.png",
            fallbackText = "SABC Sport",
            description = "Live South African sports, Premier Soccer League (PSL), rugby, athletics, boxing, and post-match analysis.",
            slogan = "For the Love of the Game",
            broadcastLanguage = "English, isiZulu, Sesotho",
            quality = "HD 720p",
            isActive = true,
            isGeoRestricted = true
        )
    )

    fun findById(id: String): Channel {
        return channels.find { it.id == id } ?: channels.first()
    }
}

