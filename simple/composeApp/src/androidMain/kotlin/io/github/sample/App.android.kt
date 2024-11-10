package io.github.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.firebase_core.AndroidKFirebaseCore
import io.github.sign_in_with_facebook.AndroidSignInWithFacebook

class AppActivity : ComponentActivity() {
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AndroidKFirebaseCore.initialize(this)

        AndroidSignInWithFacebook.initialization(this, R.string.facebook_app_id)




        setContent { App() }
    }


}

@Preview
@Composable
fun AppPreview() {
    App()
}
