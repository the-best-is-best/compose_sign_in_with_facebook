package io.github.sign_in_with_facebook

data class FacebookAuthResult(
    val accessToken: String,
    val user: FacebookUser

)

data class FacebookUser(
    val id: String?,
    val name: String?,
    val email: String?,
    val pictureUrl: String?
)