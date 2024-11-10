package io.github.sign_in_with_facebook

import com.facebook.CallbackManager

object FacebookUtil {
    val callbackManager by lazy {
        CallbackManager.Factory.create()
    }
}
