package io.github.sign_in_with_facebook

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class KFacebookSignIn actual constructor() {
    private var loginResult: LoginResult? = null

    // Get Facebook credentials (login token)
    actual fun getCredential(
        scopes: List<String>,
        onSignInFailed: (Exception) -> Unit,
        onSignedIn: (String?) -> Unit
    ) {
        val loginManager = LoginManager.getInstance()
        loginManager.logInWithReadPermissions(AndroidSignInWithFacebook.getActivity(), scopes)

        // Register Facebook login callback
        loginManager.registerCallback(null, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                loginResult = result
                onSignedIn(result.accessToken.token) // Return the token on success
            }

            override fun onCancel() {
                onSignedIn(null) // Handle cancellation
            }

            override fun onError(error: FacebookException) {
                onSignInFailed(Exception(error)) // Handle error
            }
        })

    }

    // Fetch user data from Facebook
    actual suspend fun getUserData(): Result<FacebookUser> =
        suspendCancellableCoroutine { continuation ->
            if (loginResult == null) {
                continuation.resume(Result.failure(Exception("Get Credential first")))
                return@suspendCancellableCoroutine // Fail if loginResult is null
            }

            // Fetch user data using GraphRequest
            GraphRequest.newMeRequest(loginResult!!.accessToken) { jsonObject, response ->
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
            }.apply {
                val parameters = Bundle().apply {
                    putString("fields", "id,name,email,picture") // Specify the fields to fetch
                }
            }.executeAsync()
        }

    @Composable
    actual fun SignInButton(
        scopes: List<String>,
        onSignInFailed: (Exception) -> Unit,
        onSignedIn: (String?) -> Unit
    ) {
        val scope = rememberCoroutineScope()
        AndroidView(
            modifier = Modifier,
            factory = { context ->
                LoginButton(context).apply {
                    val callbackManager = CallbackManager.Factory.create()
                    setPermissions("email", "public_profile")
                    registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                        override fun onCancel() {
                            // do nothing
                        }

                        override fun onError(error: FacebookException) {
                            onSignInFailed(error)
                        }

                        override fun onSuccess(result: LoginResult) {
                            scope.launch {
                                val token = result.accessToken.token
                                onSignedIn(token)

                            }
                        }
                    })
                }

            }
        )
    }

}

