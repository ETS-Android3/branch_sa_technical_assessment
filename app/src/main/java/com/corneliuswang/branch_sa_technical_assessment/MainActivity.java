package com.corneliuswang.branch_sa_technical_assessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.validators.IntegrationValidator;

import static io.branch.referral.Defines.Jsonkey.SDK;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.corneliuswang.branch_sa_technical_assessment.Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }
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
        System.out.println(sessionParams);

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
        System.out.println(installParams);
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
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        new BranchEvent("send_message")
                .addCustomDataProperty("Key11", message)
                .setCustomerEventAlias("message")
                .logEvent(MainActivity.this);
        startActivity(intent);
    }

    public void getInfo(View view) {
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
}