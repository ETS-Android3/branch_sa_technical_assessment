# Branch SDK Implementation
This is an example of what the branch SDK will look like once it's implemented on Android via AndroidStudio

## Documentation
Please reference https://help.branch.io/developers-hub/docs/android-basic-integration for more information regarding further integration

## Steps
1) First install the branch SDK by adding the dependencies to your app-level / module build.gradle file

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

2) Configure the branch SDK to work within your application by adding the uses-permissions, intent-filters (to your activity set to android.intent.category.LAUNCHER), and meta data to your AndroidManifest.xml file

```bash
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.eneff.branch.example.android">
    
    <!-- uses-permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>

    <application
        android:allowBackup="true"
        android:name="com.eneff.branch.example.android.CustomApplicationClass"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <!-- intent filters to Launcher Activity to handle incoming Branch intents -->
        <activity
            android:name=".LauncherActivity"
            android:launchMode="singleTask"
            android:label="@string/app_name"
            android:theme="@style/AppTheme.NoActionBar">

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

            <!-- Branch URI Scheme -->
            <intent-filter>
                <data android:scheme="yourapp" android:host="open" />
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
            </intent-filter>

            <!-- Branch App Links -->
            <intent-filter android:autoVerify="true">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="https" android:host="example.app.link" />
                <!-- example-alternate domain is required for App Links when the Journeys/Web SDK and Deepviews are used inside your website.  -->
                <data android:scheme="https" android:host="example-alternate.app.link" />
            </intent-filter>
        </activity>

        <!-- Branch init meta-data -->
        <meta-data android:name="io.branch.sdk.BranchKey" android:value="key_live_kaFuWw8WvY7yn1d9yYiP8gokwqjV0Sw" />
        <meta-data android:name="io.branch.sdk.BranchKey.test" android:value="key_test_hlxrWC5Zx16DkYmWu4AHiimdqugRYMr" />
        <meta-data android:name="io.branch.sdk.TestMode" android:value="false" />     <!-- Set to true to use Branch_Test_Key (useful when simulating installs and/or switching between debug and production flavors) -->

    </application>

</manifest>
