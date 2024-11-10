package io.github.sign_in_with_facebook

import androidx.compose.runtime.Composable

expect class KFacebookSignIn() {
    fun getCredential(
        scopes: List<String>,
        onSignInFailed: (Exception) -> Unit,
        onSignedIn: (String?) -> Unit
    )

    suspend fun getUserData(): Result<FacebookUser>

    @Composable
    fun SignInButton(
        scopes: List<String>,
        onSignInFailed: (Exception) -> Unit,
        onSignedIn: (String?) -> Unit,
    )
}