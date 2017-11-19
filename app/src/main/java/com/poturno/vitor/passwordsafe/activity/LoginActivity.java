package com.poturno.vitor.passwordsafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.controler.Auth;
import com.poturno.vitor.passwordsafe.model.User;

import java.security.PrivateKey;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView signup;
    private TextView forgot;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.edit_login_email);
        password = (EditText)findViewById(R.id.edit_login_password);
        login = (Button)findViewById(R.id.btn_login);
        signup = (TextView)findViewById(R.id.txt_signup);
        forgot = (TextView)findViewById(R.id.txt_forgot);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()||
                        password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Email ou senha invalidos",Toast.LENGTH_LONG).show();
                }else {
                    auth = new Auth();
                    auth.authenticate(email.getText().toString(), password.getText().toString(), LoginActivity.this);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignup();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecover();
            }
        });
    }

    private void openSignup(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRecover(){
        Intent intent = new Intent(LoginActivity.this, RecoverActivity.class);
        startActivity(intent);
        finish();
    }
}
