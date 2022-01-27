# Branch SDK Implementation
This is an example of what the branch SDK will look like once it's implemented on Android via AndroidStudio

## Documentation
Please reference https://help.branch.io/developers-hub/docs/android-basic-integration for more information regarding further integration

## Steps
First install the branch SDK by adding the dependencies to your app-level build.gradle file

```bash
dependencies {
    ...
    // required for all Android apps
    implementation 'io.branch.sdk.android:library:5.+'
    // required if your app is in the Google Play Store (tip: avoid using bundled play services libs)
    implementation 'com.google.android.gms:play-services-ads-identifier:17.1.0+'
    // optional
    // Chrome Tab matching (enables 100% guaranteed matching based on cookies)
    implementation 'androidx.browser:browser:1.0.0'
    // Replace above with the line below if you do not support androidx
    // implementation 'com.android.support:customtabs:28.0.0'
    ...
}
```
