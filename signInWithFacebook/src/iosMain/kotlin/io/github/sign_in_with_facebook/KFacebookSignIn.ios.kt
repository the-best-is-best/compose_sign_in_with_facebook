package io.github.sign_in_with_facebook

import androidx.compose.runtime.Composable


@Composable
actual fun SignInButton(
    scopes: List<String>,
    onSignInFailed: (Exception) -> Unit,
    onSignedIn: (String?) -> Unit,
    config: SignInButtonConfig
) {
}

actual class KFacebookSignIn actual constructor() {

    actual suspend fun getUserData(): Result<FacebookUser> {
        TODO("Not yet implemented")
    }

    actual fun isSignIn(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun signOut() {
    }


}