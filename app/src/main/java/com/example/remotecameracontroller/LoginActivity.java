package com.example.remotecameracontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import network_resurce.PostLoginData;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editId = (EditText) findViewById(R.id.login_id);
        final EditText  editPassword = (EditText) findViewById(R.id.login_password);

        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    public void run(){
                        PostLoginData pd = new PostLoginData(editId.getText().toString(), editPassword.getText().toString());
                        if(pd.okSign.equals("ok")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }.start();
            }
        });
    }
}
