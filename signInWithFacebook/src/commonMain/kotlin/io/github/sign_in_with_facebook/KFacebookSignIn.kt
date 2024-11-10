package io.github.sign_in_with_facebook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

expect class KFacebookSignIn() {
//    fun getCredential(
//        scopes: List<String>,
//        onSignInFailed: (Exception) -> Unit,
//        onSignedIn: (String?) -> Unit
//    )

    suspend fun getUserData(): Result<FacebookUser>
    fun isSignIn(): Boolean
    fun signOut()


}

@Composable
expect fun SignInButton(
    scopes: List<String>,
    onSignInFailed: (Exception) -> Unit,
    onSignedIn: (String?) -> Unit,
    config: SignInButtonConfig
)

data class SignInButtonConfig(
    val buttonText: String,
    val iconRes: ImageVector? = null,
    val iconResModifier: Modifier = Modifier,
    val modifier: Modifier = Modifier,
    val buttonTextStyle: TextStyle? = null,
    val textColor: Color,
    val shape: Shape? = null,
    val buttonColors: ButtonColors? = null,
    val elevation: ButtonElevation? = null,
    val border: BorderStroke? = null,
    val contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    val interactionSource: MutableInteractionSource? = null,

    )