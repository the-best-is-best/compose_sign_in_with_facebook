package io.github.sign_in_with_facebook

import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.FragmentActivity
import com.facebook.FacebookSdk
import java.lang.ref.WeakReference

object AndroidSignInWithFacebook {
    private var activity: WeakReference<FragmentActivity?> = WeakReference(null)

    internal fun getActivity(): FragmentActivity {
        return activity.get()!!
    }

    fun initialization(activity: FragmentActivity, appId: Int) {
        this.activity = WeakReference(activity)
        FacebookSdk.setApplicationId(getString(getActivity(), appId))


    }
}