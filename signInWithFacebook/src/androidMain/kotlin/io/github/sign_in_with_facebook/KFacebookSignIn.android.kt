package io.github.sign_in_with_facebook

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class KFacebookSignIn {


    // Fetch user data from Facebook
    actual suspend fun getUserData(): Result<FacebookUser> =
        suspendCancellableCoroutine { continuation ->
            if (!isSignIn()) {
                continuation.resume(Result.failure(Exception("Get Credential first")))
                return@suspendCancellableCoroutine // Fail if loginResult is null
            }

            // Fetch user data using GraphRequest
            val request =
                GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { jsonObject, response ->
                    if (response?.error != null) {
                        continuation.resume(Result.failure(Exception("Error fetching user data: ${response.error?.errorMessage}")))
                    } else {
                        try {
                            val userName = jsonObject?.getString("name")
                            val userEmail = jsonObject?.getString("email")
                            val userId = jsonObject?.getString("id")
                            val userPictureUrl =
                                jsonObject?.getJSONObject("picture")?.getJSONObject("data")
                                    ?.getString("url")

                            // Create and return a FacebookUser object
                            val user = FacebookUser(userId, userName, userEmail, userPictureUrl)
                            continuation.resume(Result.success(user))
                        } catch (e: Exception) {
                            continuation.resume(Result.failure(Exception("Error parsing user data: ${e.message}")))
                        }
                    }
                }
            val parameters = Bundle().apply {
                putString(
                    "fields",
                    "id,name,email,picture"
                ) // Modify this list based on the data you need
            }
            request.parameters = parameters
            request.executeAsync()
        }

    actual fun signOut() {
        LoginManager.getInstance().logOut()

    }

    actual fun isSignIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isExpired = accessToken?.isExpired ?: true
        println("is sign in: $isExpired")
        return !isExpired
    }

    actual fun getAccessToken(): String? {
        return AccessToken.getCurrentAccessToken()?.token
    }

}

@Composable
actual fun SignInButton(
    scopes: List<String>,
    onSignInFailed: (Exception) -> Unit,
    onSignedIn: (String?) -> Unit,
    config: SignInButtonConfig
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {

    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {
                onSignInFailed(error)
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    onSignedIn(KFacebookSignIn().getAccessToken())
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

    Button(
        onClick = {
            launcher.launch(listOf("email", "public_profile"))
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



