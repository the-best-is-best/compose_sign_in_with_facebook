package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import io.github.sample.theme.AppTheme
import io.github.sign_in_with_facebook.KFacebookSignIn
import kotlinx.coroutines.launch

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
        faceBookSignIn.SignInButton(listOf("email", "public_profile"), onSignInFailed = {
            println("error is ${it}")
        }, onSignedIn = {
            println("login FB success")
        })
        Button(onClick = {
            scope.launch {
                faceBookSignIn.getCredential(listOf("email", "public_profile"), onSignInFailed = {
                    println("error is ${it}")

                }, onSignedIn = {
                    println("login FB success")

                })

            }
        }) {
            Text("Sign in with FB")
        }
        Button(onClick = {
            scope.launch {
                val userData = faceBookSignIn.getUserData()
                userData.onSuccess {
                    println("user email ${it.email}")
                }
            }
        }) {
            Text("Get user data")
        }
//        Button(onClick = {
//
//            scope.launch {
//               val cred  = googleSign.getStoredCredential()
//                cred.onSuccess {
//                    Firebase.auth.signInWithCredential(GoogleAuthProvider.credential(idToken = it.idToken, it.accessToken))
//                    println("user is ${Firebase.auth.currentUser}")
//                }
//            }
//        }){
//            Text("get stored cred")
//        }
        Button(onClick = {
            scope.launch {
                // googleSign.signOut()
                Firebase.auth.signOut()
            }
        }) {
            Text("Logut")
        }
    }
}
