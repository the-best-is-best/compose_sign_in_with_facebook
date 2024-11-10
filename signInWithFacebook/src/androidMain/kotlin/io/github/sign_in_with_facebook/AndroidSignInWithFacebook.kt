package io.github.sign_in_with_facebook

import android.app.Activity
import androidx.core.content.ContextCompat.getString
import com.facebook.FacebookSdk

object AndroidSignInWithFacebook {


    fun initialization(
        activity: Activity,
        appId: Int,
    ) {

        FacebookSdk.setApplicationId(getString(activity, appId))


    }
}