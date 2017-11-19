package com.poturno.vitor.passwordsafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.poturno.vitor.passwordsafe.R;

public class MainActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView)findViewById(R.id.txt_teste);

        Bundle bundle = getIntent().getExtras();
        txt.setText(bundle.getString("id")+bundle.getString("name")+bundle.getString("password"));
    }
}
