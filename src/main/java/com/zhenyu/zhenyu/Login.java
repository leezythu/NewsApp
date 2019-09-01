package com.zhenyu.zhenyu;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.utils.LogController;


import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText username=(EditText)findViewById(R.id.username_edit);
        final EditText password=(EditText)findViewById(R.id.userpassword_edit);


        LogController logController = LogController.getInstance(getApplication());
        logController.getOnline().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    Login.this.onDestroy();
            }
        });


        Button r_button= (Button) findViewById(R.id.register);
        Button  l_button=(Button) findViewById(R.id.login_button);
        r_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "注册成功！\n用户名："+username.getText().toString()+"\n"+"密码："+password.getText().toString(),Toast.LENGTH_LONG).show();
                Reception.usrRegister(username.getText().toString(), password.getText().toString());


            }
        });
        l_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                 Toast.makeText(getApplicationContext(), "登录成功！\n用户名："+username.getText().toString()+"\n"+"密码："+password.getText().toString(),Toast.LENGTH_LONG).show();
                Reception.usrLogin(username.getText().toString(), password.getText().toString());

            }
        });
    }
}
