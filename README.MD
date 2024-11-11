<h1 align="center">ComposeSignWithFacebook</h1><br>

<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=21" rel="nofollow">
    <img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" style="max-width: 100%;">
</a>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/iOS-13%2B-blue.svg?logo=apple" alt="iOS 13+ Badge" />

<a href="https://github.com/the-best-is-best/">

<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a>
</div>

### The ComposeSignWithFacebook package for Compose Multiplatform enables seamless Google sign-in integration for both Android and iOS platforms. It simplifies the process of implementing Google authentication in apps built with Jetpack Compose and targets multiple platforms

<hr>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/compose-facebook-sigin-in)](https://central.sonatype.com/artifact/io.github.the-best-is-best/compose-facebook-sigin-in)

ComposeSignWithGoogle is available on `mavenCentral()`.

```kotlin
implementation("io.github.the-best-is-best:compose-facebook-sigin-in:1.0.0")
```

## Need add this in pod file if not exist run ` pod init `

```pod
  #  use_frameworks!
   use_modular_headers!
  pod "FBSDKLoginKit" , "17.4.0"
  pod "FBSDKCoreKit" , "17.4.0"
```

## Integration

### Android

- 1 add in string.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="facebook_app_id">123</string>
    <string name="fb_login_protocol_scheme">fb123</string>
    <string name="facebook_client_token">456</string>
    <string name="app_name">app name</string>
</resources>
```

- 2 edit androidManifest.xml

```xml
 <uses-permission android:name="android.permission.INTERNET" />
<uses-permission
    android:name="com.google.android.gms.permission.AD_ID"
    tools:node="remove" />

        <application>
    ....
  <activity
        android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>
...
        </application>
```

### IOS

- 1 info.plist

```plist
<key>CFBundleURLTypes</key>
    <array>
        <dict>
            <key>CFBundleURLSchemes</key>
            <array>
                <string>fb123</string>
            </array>
        </dict>
    </array>
    <key>FacebookAppID</key>
    <string>123</string>
    <key>FacebookDisplayName</key>
    <string>App name</string>

    <!-- Add the LSApplicationQueriesSchemes key with Facebook-specific schemes -->
    <key>LSApplicationQueriesSchemes</key>
    <array>
        <string>fbapi</string>
        <string>fb-messenger-api</string>
        <string>fbauth2</string>
        <string>fbshareextension</string>
    </array>

    <!-- Add the FacebookClientToken key -->
    <key>FacebookClientToken</key>
    <string>456</string>
    
    <key>NSUserTrackingUsageDescription</key>
    <string>This identifier will be used to deliver personalized ads to you.</string>

```

- 2 in iosApp.swift

```swift
  import UIKit
import ComposeApp
import FirebaseCore
import FBSDKCoreKit
import AppTrackingTransparency

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // add this
        FirebaseApp.configure()
        ApplicationDelegate.shared.application(application, didFinishLaunchingWithOptions: launchOptions)
       // end this
        window = UIWindow(frame: UIScreen.main.bounds)
        if let window = window {
            window.rootViewController = MainKt.MainViewController()
            window.makeKeyAndVisible()
        }
        return true
    }
    
    // add this
    func application(
            _ app: UIApplication,
            open url: URL,
            options: [UIApplication.OpenURLOptionsKey : Any] = [:]
        ) -> Bool {
            ApplicationDelegate.shared.application(
                app,
                open: url,
                sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
                annotation: options[UIApplication.OpenURLOptionsKey.annotation]
            )
        }
        // add this request permission
    func applicationDidBecomeActive(_ application: UIApplication) {
        if #available(iOS 14, *) {
            ATTrackingManager.requestTrackingAuthorization { _ in
                
            }
        }
    }
}

```

## How to use it

### First in androidMain

<br>

```kotlin
AndroidSignInWithFacebook.initialization(this, R.string.facebook_app_id)
```

### Second in commonMain

- Note i use firebase

```kotlin
    val faceBookSignIn = KFacebookSignIn()
SignInButton(
            scopes = listOf("email", "public_profile"),
            onSignInFailed = {
                println("sign in error is ${it}")

            },
            onSignedIn = {
                println("sign in success $it")
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

           Button(onClick = {
            println("facebook access token ${faceBookSignIn.getAccessToken()}")

        }) {
            Text("Get last access token")
        }
        Button(onClick = {
            scope.launch {
                val userData = faceBookSignIn.getUserData()
                userData.onSuccess {
                    println("user photo ${it.pictureUrl}")
                }
                userData.onFailure {
                    println("error get user data $it")
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
            Text("Logout")
        }
```
