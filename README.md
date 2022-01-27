# Branch SDK Implementation
This is an example of what the branch SDK will look like once it's implemented on Android via AndroidStudio

## Documentation
Please reference https://help.branch.io/developers-hub/docs/android-basic-integration for more information regarding further integration

## Steps
1) First install the branch SDK by adding the dependencies to your app-level / module build.gradle file

```bash
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 31
    buildToolsVersion "31.0.0"

    defaultConfig {
        applicationId "com.corneliuswang.branch_sa_technical_assessment"
        minSdkVersion 18
        targetSdkVersion 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

#Here is where you'll add the code which will install the branch files
dependencies {

    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Branch sdk required for all Android apps
    implementation 'io.branch.sdk.android:library:5.+'
    // required if your app is in the Google Play Store (tip: avoid using bundled play services libs)
    implementation 'com.google.android.gms:play-services-ads-identifier:17.1.0+'
    // Chrome Tab matching (enables 100% guaranteed matching based on cookies)
    implementation 'androidx.browser:browser:1.0.0'

}
```

2) Configure the branch SDK to work within your application by adding the uses-permissions, intent-filters (to your activity set to android.intent.category.LAUNCHER), and meta data to your AndroidManifest.xml file. Make sure to replace the keys with the ones found in your <a href="https://dashboard.branch.io/account-settings/profile"> Branch Dashboard</a>

```bash
<?xml version="1.0" encoding="utf-8"?>

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.corneliuswang.branch_sa_technical_assessment">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Branch_sa_technical_assessment">


        <!-- Branch init -->

        <activity
            android:name="com.corneliuswang.branch_sa_technical_assessment.GetInfoActivity"
            android:parentActivityName="com.corneliuswang.branch_sa_technical_assessment.MainActivity">
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.corneliuswang.branch_sa_technical_assessment.MainActivity" />
        </activity>
        <activity
            android:name="com.corneliuswang.branch_sa_technical_assessment.DisplayMessageActivity"
            android:parentActivityName="com.corneliuswang.branch_sa_technical_assessment.DisplayMessageActivity">
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value="com.corneliuswang.branch_sa_technical_assessment.MainActivity" />
        </activity>
        <activity
            android:name="com.corneliuswang.branch_sa_technical_assessment.MainActivity"
            android:launchMode="singleTask"
            android:label="@string/app_name"
            android:exported="true">

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                <data android:scheme="test" android:host="corywang.com" />
            </intent-filter>

#Branch URI Scheme / intent filters go here
            <intent-filter>
                <data android:scheme="https" android:host="29nzl.app.link" />
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
            </intent-filter>

            <!-- Branch App Links -->
            <intent-filter android:autoVerify="true">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="https" android:host="29nzl.app.link" />
                <!-- example-alternate domain is required for App Links when the Journeys/Web SDK and Deepviews are used inside your website.  -->
                <data android:scheme="https" android:host="29nzl-alternate.app.link" />
                <data android:scheme="https" android:host="29nzl.test-app.link" />
                <data android:scheme="https" android:host="29nzl-alternate.test-app.link" />
            </intent-filter>
        </activity>
        
#Branch Initialization / meta-data goes here. Make sure to replace live/test keys with the ones found in your branch dashboard
        <meta-data android:name="io.branch.sdk.BranchKey" android:value="key_live_gjYMhFnypzHJZGCHRxb3xafauwjUnroK" />
        <meta-data android:name="io.branch.sdk.BranchKey.test" android:value="key_test_hb9SdylvlCTUXSyJQAm5CopkDynJiFd9" />
        <meta-data android:name="io.branch.sdk.TestMode" android:value="false" />     <!-- Set to true to use Branch_Test_Key (useful when simulating installs and/or switching between debug and production flavors) -->
    </application>

</manifest>
```

