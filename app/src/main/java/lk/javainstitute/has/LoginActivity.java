package lk.javainstitute.has;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import lk.javainstitute.has.service.UserService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        requestPermissions(new String[]{
                Manifest.permission.POST_NOTIFICATIONS
        },1);

        Button button1 = findViewById(R.id.NewUser);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,signin_activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText username = findViewById(R.id.signInusername);
                TextInputEditText password = findViewById(R.id.signInpassword);

                new UserService(LoginActivity.this).userAuth(String.valueOf(username.getText()),String.valueOf(password.getText()));

            }
        });

    }
}