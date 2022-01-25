package com.corneliuswang.branch_sa_technical_assessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import io.branch.referral.Branch;

public class GetInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
        setContentView(R.layout.activity_get_info);

        // Get the Intent that started this activity and extract the string
        Intent intent2 = getIntent();
        String message = intent2.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);

        TextView textView1 = findViewById(R.id.textView3);
        textView1.setText(R.string.information);
    }
}