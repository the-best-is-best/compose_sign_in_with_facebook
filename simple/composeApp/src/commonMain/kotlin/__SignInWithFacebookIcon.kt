import androidx.compose.ui.graphics.vector.ImageVector
import signinwithfacebookicon.Facebook
import kotlin.collections.List as ____KtList

object SignInWithFacebookIcon

private var __AllIcons: ____KtList<ImageVector>? = null

val SignInWithFacebookIcon.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(Facebook)
        return __AllIcons!!
    }
