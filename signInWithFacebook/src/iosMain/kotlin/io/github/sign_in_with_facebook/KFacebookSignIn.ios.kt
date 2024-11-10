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