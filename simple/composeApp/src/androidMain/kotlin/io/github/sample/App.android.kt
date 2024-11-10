package io.github.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import io.github.firebase_core.AndroidKFirebaseCore
import io.github.sign_in_with_facebook.AndroidSignInWithFacebook

class AppActivity : FragmentActivity() {

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
