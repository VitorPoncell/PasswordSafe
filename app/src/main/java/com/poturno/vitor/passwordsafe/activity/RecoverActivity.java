package com.poturno.vitor.passwordsafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.controler.Auth;
import com.poturno.vitor.passwordsafe.helper.Base64Custom;
import com.poturno.vitor.passwordsafe.model.User;

public class RecoverActivity extends AppCompatActivity {

    private EditText token;
    private EditText email;
    private EditText password0;
    private EditText password1;
    private Button recover;
    private ImageView back;
    private Auth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        token = (EditText)findViewById(R.id.edit_forgot_token);
        email = (EditText)findViewById(R.id.edit_forgot_email);
        password0 = (EditText)findViewById(R.id.edit_forgot_password0);
        password1 = (EditText)findViewById(R.id.edit_forgot_password1);
        recover = (Button)findViewById(R.id.btn_recover);
        back = (ImageView)findViewById(R.id.btn_back);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenValue = token.getText().toString();
                String emailValue = email.getText().toString();
                String password0Value = password0.getText().toString();
                String password1Value = password1.getText().toString();
                if(tokenValue.isEmpty()||
                        emailValue.isEmpty()||
                        password0Value.isEmpty()||
                        password1Value.isEmpty()){
                    Toast.makeText(RecoverActivity.this,"Todos os campos sao obrigatorios",Toast.LENGTH_LONG).show();
                }else {
                    if (password0Value.equals(password1Value)){
                        auth = new Auth();
                        user = new User();
                        user.setId(Base64Custom.encodeBase64(emailValue));
                        user.setToken(tokenValue);
                        user.setEmail(emailValue);
                        user.setPassword(password0Value);
                        auth.recover(user, RecoverActivity.this);
                    }else {
                        Toast.makeText(RecoverActivity.this,"As senhas devem ser iguais",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });
    }

    public void openLogin(){
        Intent intent = new Intent(RecoverActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
