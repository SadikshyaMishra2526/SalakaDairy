package com.eightpeak.salakafarm.views.pages.videos

import com.google.gson.annotations.SerializedName

data class YoutubeVideoModel(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("regionCode") val regionCode: String,
    @SerializedName("pageInfo") val pageInfo: PageInfo,
    @SerializedName("items") val items: List<Items>
)

data class PageInfo(
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("resultsPerPage") val resultsPerPage: Int
)

data class Items(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("id") val id: Id,
    @SerializedName("snippet") val snippet: Snippet
)

data class Id(
    @SerializedName("kind") val kind: String,
    @SerializedName("videoId") val videoId: String
)

data class Snippet(
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("channelId") val channelId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnails") val thumbnails: Thumbnails,
    @SerializedName("channelTitle") val channelTitle: String,
    @SerializedName("liveBroadcastContent") val liveBroadcastContent: String,
    @SerializedName("publishTime") val publishTime: String
)

data class Thumbnails(
    @SerializedName("default") val default: Default,
    @SerializedName("medium") val medium: Medium,
    @SerializedName("high") val high: High
)

data class Default(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)

data class High(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)

data class Medium(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)