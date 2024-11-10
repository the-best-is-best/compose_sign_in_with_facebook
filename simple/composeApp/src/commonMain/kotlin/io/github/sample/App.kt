package io.github.sample

import SignInWithFacebookIcon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FacebookAuthProvider
import dev.gitlive.firebase.auth.auth
import io.github.sample.theme.AppTheme
import io.github.sign_in_with_facebook.KFacebookSignIn
import io.github.sign_in_with_facebook.SignInButton
import io.github.sign_in_with_facebook.SignInButtonConfig
import kotlinx.coroutines.launch
import signinwithfacebookicon.Facebook

@Composable
internal fun App() = AppTheme {
    val faceBookSignIn = KFacebookSignIn()

    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SignInButton(
            scopes = listOf("email", "public_profile"),
            onSignInFailed = {
                println("sign in error is ${it}")

            },
            onSignedIn = {
                scope.launch {
                    if (it != null) {
                        Firebase.auth.signInWithCredential(
                            FacebookAuthProvider.credential(
                                accessToken = it
                            )
                        )
                    }
                }
            },
            config = SignInButtonConfig(
                buttonText = "Sign in with Facebook",
                iconRes = SignInWithFacebookIcon.Facebook,
                iconResModifier = Modifier.size(24.dp),
                modifier = Modifier,
                textColor = Color.Black,


                )
        )
//                println("facebook access token ${faceBookSignIn.getAccessToken()}")
        Button(onClick = {
            println("facebook access token ${faceBookSignIn.getAccessToken()}")

        }) {
            Text("Get last access token")
        }
        Button(onClick = {
            scope.launch {
                val userData = faceBookSignIn.getUserData()
                userData.onSuccess {
                    println("user email ${it.email}")
                }
                userData.onFailure {

                }
            }
        }) {
            Text("Get user data")
        }

        Button(onClick = {
            scope.launch {
                faceBookSignIn.signOut()
                // Firebase.auth.signOut()
            }
        }) {
            Text("Logut")
        }
    }
}
