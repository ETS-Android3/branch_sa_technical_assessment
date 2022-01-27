# Branch SDK Implementation
This is an example of what the branch SDK will look like once it's implemented on Android via AndroidStudio

## Documentation
Please reference https://help.branch.io/developers-hub/docs/android-basic-integration for more information regarding configuration / initialization

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

2) Configure the branch SDK to work within your application by adding the uses-permissions, intent-filters (to your activity set to android.intent.category.LAUNCHER), and meta data to your AndroidManifest.xml file. Make sure to replace the keys in the meta data with the ones found in your <a href="https://dashboard.branch.io/account-settings/profile"> Branch Dashboard Account Settings</a> and the links with the ones found in your <a href="https://dashboard.branch.io/configuration/general"> Branch Dashboard Configuration Settings </a>

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

#Branch URI Scheme / intent filters / links go here
            <intent-filter>
                <data android:scheme="https" android:host="29nzl.app.link" />
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
            </intent-filter>

#Branch links from configuration also go here
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

3) Load branch into the Activity / Class that you want to link to and track data from

```bash

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

#Make sure to import the classes into the activity
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.validators.IntegrationValidator;

import static io.branch.referral.Defines.Jsonkey.SDK;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.corneliuswang.branch_sa_technical_assessment.Message";
    
#Add branch logging and obj. initialization here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }
```

4) Be sure to initialize branch in the activity you have set to android.intent.category.LAUNCHER in onStart()

```bash
@Override
    protected void onStart(){
        super.onStart();
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        if (intent != null &&
                intent.hasExtra("branch_force_new_session") &&
                intent.getBooleanExtra("branch_force_new_session", true)) {
            Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
        }
    }
    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject linkProperties, BranchError error) {
            // do stuff with deep link data (nav to page, display content, etc)
        }
    };
```

5) After configuration, you can <a href="https://help.branch.io/developers-hub/docs/android-testing#section-Test-Your-Branch-Integration">test </a> if it properly calls the Branch API / Dashboard by inserting Integration Validator into the onStart() method and checking the logs after building and running the app on your device.

```bash
@Override
    protected void onStart(){
        super.onStart();
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
#Integration Validator goes here
        IntegrationValidator.validate(MainActivity.this);
```

6) Since you requested custom event tracking, here's what it would look like when you implement it within the buttons you want tracked

```bash
public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
 #Adding branch custom event logging
        new BranchEvent("send_message")
                .addCustomDataProperty("Key11", message)
                .setCustomerEventAlias("message")
                .logEvent(MainActivity.this);
        startActivity(intent);
 }

 public void getInfo(View view) {
 #Adding branch custom event logging
        new BranchEvent("get_info")
                .addCustomDataProperty("Key33", String.valueOf(R.string.information))
                .addCustomDataProperty("Key44", "Val44")
                .setCustomerEventAlias("my_custom_alias")
                .logEvent(MainActivity.this);
        Intent intent2 = new Intent(this, GetInfoActivity.class);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent2.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent2);
 }
 ```
 
7) You can then run the application on the device you use to test it and the device logs should mention that branch API made a POST call. You can use the <a href="https://dashboard.branch.io/liveview/events"> Liveview Dashboard </a> to check if the events are being tracked properly. Here's what it will look like when they are:


![Branch_Dashboard_Liveview](https://user-images.githubusercontent.com/56694544/151289229-21ddfc9a-b9ac-4fd7-8dc9-e9ca05d1f8f7.PNG)

8) You also requested deep link routing, so here's how you enable it in your launcher activity.

```bash
@Override
    protected void onStart(){
        super.onStart();
        Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }).withData(this.getIntent().getData()).init();

        // latest
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
    }
```

Unfortunately, I wasn't able to print out the deep link routing data, but was able to set up a quick link via the branch dashboard and test opening it using the testing device, here's what it looks like on the <a href="https://dashboard.branch.io/"> Summary Dashboard </a>:

![Branch_Dashboard_Summary](https://user-images.githubusercontent.com/56694544/151290215-3630942e-dcbc-4aa3-b452-414891789d9f.PNG)

