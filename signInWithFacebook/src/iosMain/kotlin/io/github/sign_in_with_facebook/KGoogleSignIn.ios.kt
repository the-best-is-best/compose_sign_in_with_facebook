package io.github.sign_in_with_facebook

actual class KFacebookSignIn actual constructor() {
    actual suspend fun getCredential(
        clientId: String,
        setFilterByAuthorizedAccounts: Boolean
    ) {
    }
}