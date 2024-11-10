package io.github.sign_in_with_facebook

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cocoapods.FBSDKCoreKit.FBSDKGraphRequest
import cocoapods.FBSDKLoginKit.FBSDKAccessToken
import cocoapods.FBSDKLoginKit.FBSDKLoginManager
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import kotlin.coroutines.resume


actual class KFacebookSignIn actual constructor() {

    actual suspend fun getUserData(): Result<FacebookUser> =
        suspendCancellableCoroutine { continuation ->
            val accessToken = FBSDKAccessToken.currentAccessToken()
            if (accessToken == null || accessToken.isExpired()) {
                continuation.resume(Result.failure(Exception("Access token is null or expired")))
                return@suspendCancellableCoroutine
            }

            val request = FBSDKGraphRequest(
                graphPath = "me",
                parameters = mapOf("fields" to "id,name,email,picture")
            )
            request.startWithCompletion { _, result, error ->
                if (error != null) {
                    continuation.resume(Result.failure(error.toException()))
                } else if (result is Map<*, *>) {
                    val userId = result["id"] as? String
                    val name = result["name"] as? String
                    val email = result["email"] as? String
                    val pictureUrl = (result["picture"] as? Map<*, *>)?.get("data")?.let { data ->
                        (data as? Map<*, *>)?.get("url") as? String
                    }

                    if (userId != null && name != null) {
                        val user = FacebookUser(
                            id = userId,
                            name = name,
                            email = email,
                            pictureUrl = pictureUrl
                        )
                        continuation.resume(Result.success(user))
                    } else {
                        continuation.resume(Result.failure(Exception("Failed to parse user data")))
                    }
                } else {
                    continuation.resume(Result.failure(Exception("Unknown error occurred")))
                }
            }
        }


    actual fun isSignIn(): Boolean {
        val accessToken = FBSDKAccessToken.currentAccessToken()
        val isExpired = accessToken?.isExpired() ?: true
        return !isExpired
    }

    actual fun signOut() {
        val loginManager = FBSDKLoginManager()
        loginManager.logOut()
    }

    actual fun getAccessToken(): String? {
        val accessToken = FBSDKAccessToken.currentAccessToken()
        return if (accessToken != null && !accessToken.isExpired()) {
            accessToken.tokenString()
        } else {
            null
        }
    }


}


@Composable
actual fun SignInButton(
    scopes: List<String>,
    onSignInFailed: (Exception) -> Unit,
    onSignedIn: (String?) -> Unit,
    config: SignInButtonConfig
) {

    Button(
        onClick = {

            val loginManager = FBSDKLoginManager()
            val windowScene: UIWindowScene? =
                UIApplication.sharedApplication.connectedScenes.first() as? UIWindowScene
            val window = windowScene?.windows?.first() as? UIWindow
            val rootViewController: UIViewController? = window?.rootViewController
            if (rootViewController == null) {
                onSignInFailed(Exception("root view controller is null"))
                return@Button
            }
            loginManager.logInWithPermissions(scopes, rootViewController) { result, error ->
                if (error != null) {
                    onSignInFailed(Exception(error.toException()))
                } else {
                    onSignedIn(KFacebookSignIn().getAccessToken())
                }

            }

        },
        modifier = config.modifier,
        shape = config.shape ?: ButtonDefaults.shape,
        colors = config.buttonColors ?: ButtonDefaults.buttonColors(),
        elevation = config.elevation ?: ButtonDefaults.buttonElevation(),
        border = config.border,
        contentPadding = config.contentPadding,
        interactionSource = config.interactionSource
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            config.iconRes?.let {

                Image(
                    modifier = config.iconResModifier,
                    imageVector = it,
                    contentDescription = null
                )

            }
            Text(
                text = config.buttonText,
                color = config.textColor,
                style = config.buttonTextStyle ?: TextStyle()
            )
        }
    }
}


fun NSError.toException(): Exception {
    return Exception("NSError - Domain: $domain, Code: $code, Description: ${localizedDescription}")
}