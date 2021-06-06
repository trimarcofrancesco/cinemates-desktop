package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class InappropriatesResponse(
    @SerializedName("review") var reviews: List<Review>,
    @SerializedName("comment") var comments: List<Comment>
)

data class Review(
    @SerializedName("reviewId") var reviewId: Int,
    @SerializedName("username") var username: String,
    @SerializedName("profilePic") var profilePic: String,
    @SerializedName("reviewText") var reviewText: String,
    @SerializedName("data") var data: String
)

data class Comment(
    @SerializedName("commentId") var commentId: Int,
    @SerializedName("username") var username: String,
    @SerializedName("profilePic") var profilePic: String,
    @SerializedName("commentText") var commentText: String,
    @SerializedName("data") var data: String
)
