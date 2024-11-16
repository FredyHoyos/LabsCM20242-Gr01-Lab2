package com.example.jetcaster.ui.network

data class YouTubeResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String
)

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class Thumbnails(
    val medium: ThumbnailDetail
)

data class ThumbnailDetail(
    val url: String
)

