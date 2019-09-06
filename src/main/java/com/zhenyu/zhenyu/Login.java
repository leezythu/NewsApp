package com.zhenyu.zhenyu;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.zhenyu.zhenyu.RequestData.Reception;
import com.zhenyu.zhenyu.utils.LogController;



public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText usernameEdit=(EditText)findViewById(R.id.username_edit);
        final EditText passwordEdit=(EditText)findViewById(R.id.userpassword_edit);


        final LogController logController = LogController.getInstance(getApplication());
        logController.getOnline().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    logController.setUsername(usernameEdit.getText().toString());
                    logController.setPassword(passwordEdit.getText().toString());
//                    try {
//                        Thread.sleep(1000);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    Login.this.finish();
                }
            }
        });

        logController.getLoginfo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });


        Button r_button= (Button) findViewById(R.id.register);
        Button l_button=(Button) findViewById(R.id.login_button);
        r_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "注册成功！\n用户名："+username.getText().toString()+"\n"+"密码："+password.getText().toString(),Toast.LENGTH_LONG).show();
                Reception.usrRegister(usernameEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });
        l_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                 Toast.makeText(getApplicationContext(), "登录成功！\n用户名："+username.getText().toString()+"\n"+"密码："+password.getText().toString(),Toast.LENGTH_LONG).show();
                Reception.usrLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());

            }
        });
    }

}
